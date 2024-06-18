//JavaObjServer.java ObjectStream 기반 채팅 Server

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class JavaObjServer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	
	/* test code */
	private Vector RoomVec = new Vector(); // 채팅방을 저장할 벡터
	private Vector LoggedUserVec = new Vector();
	
	public ImageIcon profileBasic = new ImageIcon("src/resources/profileIMG_basic.png");
	public ImageIcon profileBasic_s = new ImageIcon("src/profileIMG_basic.png");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaObjServer frame = new JavaObjServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JavaObjServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(13, 318, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(112, 318, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
					AppendText("새로운 참가자 from " + client_socket);
					// User 당 하나씩 Thread 생성
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // 새로운 참가자 배열에 추가
					new_user.start(); // 만든 객체의 스레드 실행
					AppendText("현재 참가자 수 " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		textArea.append("code = " + msg.getCode() + "\n");
		textArea.append("id = " + msg.getId() + "\n");
		textArea.append("data = " + msg.getData() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}
	public List<String> user_list = new ArrayList<String>();
	// User 당 생성되는 Thread
	// Read One 에서 대기 -> Write All
	class RoomService extends Thread { // 룸 정보를 관리
		public String RoomID = "";
		public String RoomUserlist = "";
		public String RoomChat = "";
		public List<ImageIcon> RoomImage;
	}
	
	class UserLogService extends Thread { // 유저 로그 서비스
		public String logusername = "";
		public String loguserstatus = "";
		public ImageIcon loguserprofileimg;
	}
	
	class UserService extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;

		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Socket client_socket;
		private Vector user_vc;
		private Vector room_vc;
		private Vector loguser_vc;
		public String UserName = "";
		public String UserStatus;

		/* test code */
		public List<String> room_ids = new ArrayList<String>();

		
		
		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// 매개변수로 넘어온 자료 저장
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			this.room_vc = RoomVec;
			this.loguser_vc = LoggedUserVec;
			
			try {
				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());
			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		public void Login() {
			//AppendText("새로운 참가자 " + UserName + " 입장.");
			//WriteOne("Welcome to Java chat server\n");
			//WriteOne(UserName + "님 환영합니다.\n"); // 연결된 사용자에게 정상접속을 알림
//			String msg = user_list.toString();
			String msg =""; // 유저 이름, 유저 상태를 담을 msg
			for(int i=0; i < loguser_vc.size(); i++) { // 유저 벡터 루프 돌면서
				UserLogService loguser = (UserLogService) loguser_vc.elementAt(i);
				msg += loguser.logusername +" "+loguser.loguserstatus + " " + loguser.loguserprofileimg.toString() + ","; // 유저 이름, 상태를 담아
//				System.out.println("Server" + loguser.logusername);
			}
			// msg는 u1 ON,u2 OFF,u3 ON 과 같은 형식으로 저장됨
			WriteAllList(msg); // 모두에게 방송
	}
		
		public void Logout() {
			//String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
			UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			//WriteAll(msg); // 나를 제외한 다른 User들에게 전송
			//AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
		}

		// 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteAll(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOne(str);
			}
		}
		
		public void WriteAllList(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOneList(str);
			}
		}
		// 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
		public void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOneObject(ob);
			}
		}

		// 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteOthers(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this && user.UserStatus == "O") {
					ChatMsg obcm = new ChatMsg("SERVER", "777", str);
					try {
						oos.writeObject(obcm);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		public void WriteOneTest(Object obcm) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user == this)
					try {
						oos.writeObject(obcm);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		
		public void WriteOneGo(String logUserName) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user == this)
					try {
							for (int k = 0; k < room_vc.size(); k++) {
								RoomService room = (RoomService) room_vc.elementAt(k);
								String ru[] = room.RoomUserlist.split(" ");
								for(int j = 0; j<ru.length; j++) {
									if(ru[j].equals(logUserName)) {
										SendProfileFirst();
										ChatMsg obcm = new ChatMsg(user.UserName, "902", room.RoomID);
										obcm.selected_userlist = room.RoomUserlist;
										String gl[] = room.RoomChat.split(", ");
										String gl2[] = gl[gl.length-1].split(" ",2);
										if(gl2[1].contains(":\\") || gl2[1].contains(".png") || gl2[1].contains(".jpg")) {
											obcm.lastchat = "(이미지)";
										} else {
											obcm.lastchat = gl2[1];											
										}
										oos.writeObject(obcm);
										
									}
								}								
							}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		
		public void SendProfileFirst() {
			for(int i=0; i < loguser_vc.size(); i++) {
				UserLogService loguser = (UserLogService) loguser_vc.elementAt(i);
				
				try {
						ChatMsg obcm = new ChatMsg(loguser.logusername, "903", "PROFILE");
						if(loguser.loguserprofileimg.toString().equals(profileBasic.toString())) {
							obcm.profileImg = profileBasic_s;
						} else {
							obcm.profileImg = loguser.loguserprofileimg;							
						}
						oos.writeObject(obcm);										
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
		public byte[] MakePacket(String msg) {
			byte[] packet = new byte[BUF_LEN];
			byte[] bb = null;
			int i;
			for (i = 0; i < BUF_LEN; i++)
				packet[i] = 0;
			try {
				bb = msg.getBytes("euc-kr");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
		}

		// UserService Thread가 담당하는 Client 에게 1:1 전송
		public void WriteOne(String msg) {
			try {
				// dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
//				ChatMsg obcm = new ChatMsg("SERVER", "200", msg);
				ChatMsg obcm = new ChatMsg("SERVER", "777", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
//					dos.close();
//					dis.close();
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}
		
		public void sendChatLog (String chatlog, String room_id) {
			try {
				String cl[] = chatlog.split(", ");
				for(int i=0; i<cl.length; i++) {
					String ucl[] = cl[i].split(" ", 2);
					ChatMsg obcm = new ChatMsg(ucl[0], "200", ucl[1]);
					obcm.room_id = room_id;
					oos.writeObject(obcm);
				}
				
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}
		
		public void sendImgLog (ImageIcon imglog, String room_id, String sender, String orilog) {
			try {
					String time[] = orilog.split("`");
					ChatMsg obcm = new ChatMsg(sender, "300", time[1]);
					obcm.room_id = room_id;
					obcm.img = imglog;
					oos.writeObject(obcm);			
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}
		
		public void WriteOneList(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("SERVER", "100", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				System.out.println("ERRor!@@@"+this.UserName);
			}
		}
		
		// 귓속말 전송
		public void WritePrivate(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("귓속말", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}
		public void WriteOneObject(Object ob) {
			try {
			    oos.writeObject(ob);
			} 
			catch (IOException e) {
				AppendText("oos.writeObject(ob) error");		
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout();
			}
		}
		
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm = null;
					if (socket == null)
						break;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						AppendObject(cm);
					} else
						continue;
					if (cm.getCode().equals("100")) {
						UserName = cm.getId();
						UserStatus = "O"; // Online 상태
						for(int i=0; i < loguser_vc.size(); i++) {
							UserLogService loguser = (UserLogService) loguser_vc.elementAt(i);
							if(cm.getId().equals(loguser.logusername)){ // 해당 유저
								loguser.loguserstatus = "ON"; // 유저의 상태를 ON으로
								WriteOneGo(loguser.logusername);
							}
						}
						if(!user_list.contains(UserName)) { // 로그에 저장되어 있지 않은 이름이라면
							user_list.add(UserName); // 유저 리스트에 저장
							UserLogService newuser = new UserLogService();
							newuser.logusername = UserName; // 벡터에 이름 저장
							newuser.loguserstatus = "ON"; // 초기 상태 ON으로 설정
							newuser.loguserprofileimg = profileBasic;
							LoggedUserVec.add(newuser); // 벡터에 저장
						}
						Login(); // 로그인
					} else if (cm.getCode().matches("200")) {
						msg = String.format("[%s] %s", cm.getId(), cm.getData());
						AppendText(msg); // server 화면에 출력
						String[] args = msg.split(" "); // 단어들을 분리한다.
						if (args.length == 1) { // Enter key 만 들어온 경우 Wakeup 처리만 한다.
							UserStatus = "O";
						} else if (args[1].matches("/exit")) {
							Logout();
							break;
						} 
					else { // 일반 채팅 메시지
							UserStatus = "O";
							//WriteAll(msg + "\n"); // Write All
//							WriteAllObject(cm);
							for (int k = 0; k < room_vc.size(); k++) {
								RoomService room = (RoomService) room_vc.elementAt(k);
								if (room.RoomID.equals(cm.room_id)) {
//									System.out.println(room.RoomUserlist);
									String selectedOne[] = room.RoomUserlist.split(" ");
									for (int i = 0; i < user_vc.size(); i++) {
										UserService user = (UserService) user_vc.elementAt(i);
										for (int j = 0; j <selectedOne.length; j++) {
											if(user.UserName.equals(selectedOne[j])) {
												user.WriteOneObject(cm);
											}
										}
									}
									LocalDateTime now = LocalDateTime.now();
									String time = now.format(DateTimeFormatter.ofPattern("a HH:mm"));
									room.RoomChat += cm.getId() + " " + cm.getData() + "`" + time + ", ";
								}
							}
							
						}
					} else if (cm.getCode().matches("300")) {
						UserStatus = "O";
						for (int k = 0; k < room_vc.size(); k++) {
							RoomService room = (RoomService) room_vc.elementAt(k);
							if (room.RoomID.equals(cm.room_id)) {
//								String selectedOne[] = room.RoomUserlist.split(" ");
//								for (int i = 0; i < user_vc.size(); i++) {
//									UserService user = (UserService) user_vc.elementAt(i);
//									for (int j = 0; j <selectedOne.length; j++) {
//										if(user.UserName.equals(selectedOne[j])) {
//											user.WriteOneObject(cm);
//										}
//									}
//								}
								LocalDateTime now = LocalDateTime.now();
								String time = now.format(DateTimeFormatter.ofPattern("a HH:mm"));
								room.RoomImage.add(cm.img);
								room.RoomChat += cm.getId() + " " + cm.img.toString() + "`" + time + ", ";
							}
						}
						WriteAllObject(cm);
					} else if (cm.getCode().matches("301")) {
						UserStatus = "O";
						for (int k = 0; k < room_vc.size(); k++) {
							RoomService room = (RoomService) room_vc.elementAt(k);
							if (room.RoomID.equals(cm.room_id)) {
//								String selectedOne[] = room.RoomUserlist.split(" ");
//								for (int i = 0; i < user_vc.size(); i++) {
//									UserService user = (UserService) user_vc.elementAt(i);
//									for (int j = 0; j <selectedOne.length; j++) {
//										if(user.UserName.equals(selectedOne[j])) {
//											user.WriteOneObject(cm);
//										}
//									}
//								}
								LocalDateTime now = LocalDateTime.now();
								String time = now.format(DateTimeFormatter.ofPattern("a HH:mm"));
								room.RoomImage.add(cm.img);
								room.RoomChat += cm.getId() + " " + cm.img.toString() + "`" + time + ", ";
							}
						}
						WriteAllObject(cm);
					} else if (cm.getCode().matches("302")) {
						WriteOneObject(cm);
					} else if (cm.getCode().matches("400")) { // logout message 처리
						for(int i=0; i < loguser_vc.size(); i++) {
							UserLogService loguser = (UserLogService) loguser_vc.elementAt(i);
							if(cm.getId().equals(loguser.logusername)){
								loguser.loguserstatus = "OFF";
							}
						}
						Login();
						Logout();
						break;
					}
					else if (cm.getCode().equals("500")) {
						WriteAllObject(cm);
					}
					else if (cm.getCode().equals("600")) { // 리스트 처리
						for (int i = 0; i < user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
							WriteOne(user.UserName);
						}
					} 
					else if (cm.getCode().equals("601")) { // 현재 접속 유저 리스트 요청
						String ul = "";
						for (int i = 0; i < user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
							ul += user.UserName+" ";
						}
						cm.con_user_list = ul;
						WriteOneTest(cm);
					}
					else if (cm.getCode().equals("700")) { // 프로필 사진 변경 요청, 객체 그대로 전달해준다
						for(int i=0; i < loguser_vc.size(); i++) {
							UserLogService loguser = (UserLogService) loguser_vc.elementAt(i);
							if(cm.getId().equals(loguser.logusername)) {
								loguser.loguserprofileimg = cm.img;
							}
						}
						WriteAllObject(cm);
					} else if (cm.getCode().equals("800")) { // 채팅방 ID를 받으면
//						WriteAllObject(cm); // 방은 여러개지만 모두에게 방송, 추후 변경 예정
//						System.out.println(cm.selected_userlist);
//						System.out.println(cm.getData()); // 채팅방 ID 콘솔에 찍어보고
//						room_ids.add(cm.getData()); // 채팅방 ID 리스트에 채팅방 ID 추가
						RoomService roominfo = new RoomService();
						roominfo.RoomID = cm.getData();
						cm.selected_userlist.trim();
						roominfo.RoomUserlist = cm.selected_userlist;
						roominfo.RoomImage = new ArrayList<>();
						RoomVec.add(roominfo);
						String selectedOne[] = cm.selected_userlist.split(" ");
						for (int i = 0; i < user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
							for (int j = 0; j <selectedOne.length; j++) {
								if(user.UserName.equals(selectedOne[j])) {
									user.WriteOneObject(cm);
								}
							}
						}
					} else if(cm.getCode().equals("900")){
						for (int k = 0; k < room_vc.size(); k++) {
							RoomService room = (RoomService) room_vc.elementAt(k);
							if (room.RoomID.equals(cm.room_id)) {
//								System.out.println(room.RoomUserlist);
								if(!room.RoomChat.equals("")) {
									for(ImageIcon logimg: room.RoomImage) {
										if(cm.getData().contains(logimg.toString())) {
											sendImgLog(logimg, room.RoomID, cm.getId(), cm.getData());
										}
									}
								}
							}
						}
					} else if(cm.getCode().equals("901")) {
						for (int k = 0; k < room_vc.size(); k++) {
							RoomService room = (RoomService) room_vc.elementAt(k);
							if (room.RoomID.equals(cm.getData())) {
//								System.out.println(room.RoomUserlist);
								if(!room.RoomChat.equals("")) {
									sendChatLog(room.RoomChat, room.RoomID);								
								}
							}
						}
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
						ois.close();
						oos.close();
						client_socket.close();
						for(int i=0; i < loguser_vc.size(); i++) {
							UserLogService loguser = (UserLogService) loguser_vc.elementAt(i);
							if(this.UserName.equals(loguser.logusername)){
								loguser.loguserstatus = "OFF";
							}
						}
						Login();
						Logout(); // 에러가난 현재 객체를 벡터에서 지운다
						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝
			} // while
		} // run
	}

}
