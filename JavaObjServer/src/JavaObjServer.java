//JavaObjServer.java ObjectStream ��� ä�� Server

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

	private ServerSocket socket; // ��������
	private Socket client_socket; // accept() ���� ������ client ����
	private Vector UserVec = new Vector(); // ����� ����ڸ� ������ ����
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����
	
	/* test code */
	private Vector RoomVec = new Vector(); // ä�ù��� ������ ����
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
				btnServerStart.setEnabled(false); // ������ ���̻� �����Ű�� �� �ϰ� ���´�
				txtPortNumber.setEnabled(false); // ���̻� ��Ʈ��ȣ ������ �ϰ� ���´�
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// ���ο� ������ accept() �ϰ� user thread�� ���� �����Ѵ�.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept�� �Ͼ�� �������� ���� �����
					AppendText("���ο� ������ from " + client_socket);
					// User �� �ϳ��� Thread ����
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // ���ο� ������ �迭�� �߰�
					new_user.start(); // ���� ��ü�� ������ ����
					AppendText("���� ������ �� " + UserVec.size());
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
	// User �� �����Ǵ� Thread
	// Read One ���� ��� -> Write All
	class RoomService extends Thread { // �� ������ ����
		public String RoomID = "";
		public String RoomUserlist = "";
		public String RoomChat = "";
		public List<ImageIcon> RoomImage;
	}
	
	class UserLogService extends Thread { // ���� �α� ����
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
			// �Ű������� �Ѿ�� �ڷ� ����
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
			//AppendText("���ο� ������ " + UserName + " ����.");
			//WriteOne("Welcome to Java chat server\n");
			//WriteOne(UserName + "�� ȯ���մϴ�.\n"); // ����� ����ڿ��� ���������� �˸�
//			String msg = user_list.toString();
			String msg =""; // ���� �̸�, ���� ���¸� ���� msg
			for(int i=0; i < loguser_vc.size(); i++) { // ���� ���� ���� ���鼭
				UserLogService loguser = (UserLogService) loguser_vc.elementAt(i);
				msg += loguser.logusername +" "+loguser.loguserstatus + " " + loguser.loguserprofileimg.toString() + ","; // ���� �̸�, ���¸� ���
//				System.out.println("Server" + loguser.logusername);
			}
			// msg�� u1 ON,u2 OFF,u3 ON �� ���� �������� �����
			WriteAllList(msg); // ��ο��� ���
	}
		
		public void Logout() {
			//String msg = "[" + UserName + "]���� ���� �Ͽ����ϴ�.\n";
			UserVec.removeElement(this); // Logout�� ���� ��ü�� ���Ϳ��� �����
			//WriteAll(msg); // ���� ������ �ٸ� User�鿡�� ����
			//AppendText("����� " + "[" + UserName + "] ����. ���� ������ �� " + UserVec.size());
		}

		// ��� User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
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
		// ��� User�鿡�� Object�� ���. ä�� message�� image object�� ���� �� �ִ�
		public void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOneObject(ob);
			}
		}

		// ���� ������ User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
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
											obcm.lastchat = "(�̹���)";
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

		// Windows ó�� message ������ ������ �κ��� NULL �� ����� ���� �Լ�
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

		// UserService Thread�� ����ϴ� Client ���� 1:1 ����
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
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
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
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
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
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
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
		
		// �ӼӸ� ����
		public void WritePrivate(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("�ӼӸ�", "200", msg);
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
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
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
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
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
						UserStatus = "O"; // Online ����
						for(int i=0; i < loguser_vc.size(); i++) {
							UserLogService loguser = (UserLogService) loguser_vc.elementAt(i);
							if(cm.getId().equals(loguser.logusername)){ // �ش� ����
								loguser.loguserstatus = "ON"; // ������ ���¸� ON����
								WriteOneGo(loguser.logusername);
							}
						}
						if(!user_list.contains(UserName)) { // �α׿� ����Ǿ� ���� ���� �̸��̶��
							user_list.add(UserName); // ���� ����Ʈ�� ����
							UserLogService newuser = new UserLogService();
							newuser.logusername = UserName; // ���Ϳ� �̸� ����
							newuser.loguserstatus = "ON"; // �ʱ� ���� ON���� ����
							newuser.loguserprofileimg = profileBasic;
							LoggedUserVec.add(newuser); // ���Ϳ� ����
						}
						Login(); // �α���
					} else if (cm.getCode().matches("200")) {
						msg = String.format("[%s] %s", cm.getId(), cm.getData());
						AppendText(msg); // server ȭ�鿡 ���
						String[] args = msg.split(" "); // �ܾ���� �и��Ѵ�.
						if (args.length == 1) { // Enter key �� ���� ��� Wakeup ó���� �Ѵ�.
							UserStatus = "O";
						} else if (args[1].matches("/exit")) {
							Logout();
							break;
						} 
					else { // �Ϲ� ä�� �޽���
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
					} else if (cm.getCode().matches("400")) { // logout message ó��
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
					else if (cm.getCode().equals("600")) { // ����Ʈ ó��
						for (int i = 0; i < user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
							WriteOne(user.UserName);
						}
					} 
					else if (cm.getCode().equals("601")) { // ���� ���� ���� ����Ʈ ��û
						String ul = "";
						for (int i = 0; i < user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
							ul += user.UserName+" ";
						}
						cm.con_user_list = ul;
						WriteOneTest(cm);
					}
					else if (cm.getCode().equals("700")) { // ������ ���� ���� ��û, ��ü �״�� �������ش�
						for(int i=0; i < loguser_vc.size(); i++) {
							UserLogService loguser = (UserLogService) loguser_vc.elementAt(i);
							if(cm.getId().equals(loguser.logusername)) {
								loguser.loguserprofileimg = cm.img;
							}
						}
						WriteAllObject(cm);
					} else if (cm.getCode().equals("800")) { // ä�ù� ID�� ������
//						WriteAllObject(cm); // ���� ���������� ��ο��� ���, ���� ���� ����
//						System.out.println(cm.selected_userlist);
//						System.out.println(cm.getData()); // ä�ù� ID �ֿܼ� ����
//						room_ids.add(cm.getData()); // ä�ù� ID ����Ʈ�� ä�ù� ID �߰�
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
						Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����
			} // while
		} // run
	}

}
