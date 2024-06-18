
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class JavaObjClientChatRoom extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JLabel EmoLabel;
	private JTextField txtInput;
	private String UserName;
	private JButton btnSend;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private JLabel lblUserName;
	private JTextPane textArea;
	public EmojiView emoji;
	public JavaObjClientChatRoom view;
	public JavaObjClientMain mainview; // 메인 뷰를 담을 뷰
	private String room_id;
	private JLabel room_title;
	//frame위치
	public int frameX;
	public int frameY;
	public ImageIcon panelIMG;
	private List<OthersChatPanel> othersChatPanels;
	private Frame frame;
	private FileDialog fd;
	private JButton imgBtn;
	private JButton filebtn;
	private JButton emobtn;
	private JButton listbtn;
	
	
	public JButton usersPfImgOne;
	public JButton usersPfImgTwo_1;
	public JButton usersPfImgTwo_2;
	public JButton usersPfImgTh_1;
	public JButton usersPfImgTh_2;
	public JButton usersPfImgTh_3;
	public JButton usersPfImgF_1;
	public JButton usersPfImgF_2;
	public JButton usersPfImgF_3;
	public JButton usersPfImgF_4;
	public String roomUserlist;

	public JavaObjClientChatRoom(String username, String room_id, JavaObjClientMain mview, String userlist) { // username을 맨 앞으로 해서 방 이름 생성하면 고유한 ID 생성됨 // UserList도 인자에 추가
		view = this;
		this.room_id = room_id; // 채팅방 이름
		this.mainview = mview; // 메인뷰
		roomUserlist = userlist;
		setResizable(false);
		setBounds(mview.frameX, mview.frameY, 394, 630);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(186, 206, 224));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		othersChatPanels = new ArrayList<>();
		String[] ul = userlist.trim().split(" ");
		
		usersPfImgOne = new JButton();
		usersPfImgOne.setBounds(9, 10, 48, 46);
		usersPfImgOne.setBorderPainted(false);
		usersPfImgOne.setContentAreaFilled(false);
		usersPfImgOne.setFocusPainted(false);
		
		usersPfImgTwo_1 = new JButton();
		usersPfImgTwo_1.setBounds(9, 10, 30, 30);
		usersPfImgTwo_1.setBorderPainted(false);
		usersPfImgTwo_1.setContentAreaFilled(false);
		usersPfImgTwo_1.setFocusPainted(false);

		usersPfImgTwo_2 = new JButton();
		usersPfImgTwo_2.setBounds(25, 25, 30, 30);
		usersPfImgTwo_2.setBorderPainted(false);
		usersPfImgTwo_2.setContentAreaFilled(false);
		usersPfImgTwo_2.setFocusPainted(false);
		
		usersPfImgTh_1 = new JButton();
		usersPfImgTh_1.setBounds(20, 10, 25, 25);
		usersPfImgTh_1.setBorderPainted(false);
		usersPfImgTh_1.setContentAreaFilled(false);
		usersPfImgTh_1.setFocusPainted(false);
		
		usersPfImgTh_2 = new JButton();
		usersPfImgTh_2.setBounds(30, 25, 25, 25);
		usersPfImgTh_2.setBorderPainted(false);
		usersPfImgTh_2.setContentAreaFilled(false);
		usersPfImgTh_2.setFocusPainted(false);
		
		usersPfImgTh_3 = new JButton();
		usersPfImgTh_3.setBounds(9, 25, 25, 25);
		usersPfImgTh_3.setBorderPainted(false);
		usersPfImgTh_3.setContentAreaFilled(false);
		usersPfImgTh_3.setFocusPainted(false);
		
		usersPfImgF_1 = new JButton();
		usersPfImgF_1.setBounds(9, 10, 20, 20);
		usersPfImgF_1.setBorderPainted(false);
		usersPfImgF_1.setContentAreaFilled(false);
		usersPfImgF_1.setFocusPainted(false);
		
		usersPfImgF_2 = new JButton();
		usersPfImgF_2.setBounds(35, 10, 20, 20);
		usersPfImgF_2.setBorderPainted(false);
		usersPfImgF_2.setContentAreaFilled(false);
		usersPfImgF_2.setFocusPainted(false);

		usersPfImgF_3 = new JButton();
		usersPfImgF_3.setBounds(9, 38, 20, 20);
		usersPfImgF_3.setBorderPainted(false);
		usersPfImgF_3.setContentAreaFilled(false);
		usersPfImgF_3.setFocusPainted(false);

		usersPfImgF_4 = new JButton();
		usersPfImgF_4.setBounds(35, 38, 20, 20);
		usersPfImgF_4.setBorderPainted(false);
		usersPfImgF_4.setContentAreaFilled(false);
		usersPfImgF_4.setFocusPainted(false);

		if(ul.length == 1) {
			usersPfImgOne.setIcon(mview.getUserProfile(ul[0], 40, 36)); // 메인에서 이미지 받아옴
			add(usersPfImgOne);
		} else if (ul.length == 2) {
			usersPfImgTwo_1.setText(ul[0]);
			usersPfImgTwo_1.setIcon(mview.getUserProfile(ul[0], 36, 30));
			usersPfImgTwo_1.setFont(new Font("굴림체", Font.PLAIN, 0));
			usersPfImgTwo_2.setText(ul[1]);
			usersPfImgTwo_2.setIcon(mview.getUserProfile(ul[1], 36, 30));
			usersPfImgTwo_2.setFont(new Font("굴림체", Font.PLAIN, 0));
			add(usersPfImgTwo_1);
			add(usersPfImgTwo_2);
		} else if (ul.length == 3) {
			usersPfImgTh_1.setText(ul[0]);
			usersPfImgTh_1.setIcon(mview.getUserProfile(ul[0], 30, 24));
			usersPfImgTh_1.setFont(new Font("굴림체", Font.PLAIN, 0));
			usersPfImgTh_2.setText(ul[1]);
			usersPfImgTh_2.setIcon(mview.getUserProfile(ul[1], 30, 24));
			usersPfImgTh_2.setFont(new Font("굴림체", Font.PLAIN, 0));
			usersPfImgTh_3.setText(ul[2]);
			usersPfImgTh_3.setIcon(mview.getUserProfile(ul[2], 30 ,24));
			usersPfImgTh_3.setFont(new Font("굴림체", Font.PLAIN, 0));
			
			add(usersPfImgTh_1);
			add(usersPfImgTh_2);
			add(usersPfImgTh_3);
		} else {
			usersPfImgF_1.setText(ul[0]);
			usersPfImgF_1.setIcon(mview.getUserProfile(ul[0], 24, 16));
			usersPfImgF_1.setFont(new Font("굴림체", Font.PLAIN, 0));
			usersPfImgF_2.setText(ul[1]);
			usersPfImgF_2.setIcon(mview.getUserProfile(ul[1], 24, 16));
			usersPfImgF_2.setFont(new Font("굴림체", Font.PLAIN, 0));
			usersPfImgF_3.setText(ul[2]);
			usersPfImgF_3.setIcon(mview.getUserProfile(ul[2], 24, 16));
			usersPfImgF_3.setFont(new Font("굴림체", Font.PLAIN, 0));
			usersPfImgF_4.setText(ul[3]);
			usersPfImgF_4.setIcon(mview.getUserProfile(ul[3], 24, 16));
			usersPfImgF_4.setFont(new Font("굴림체", Font.PLAIN, 0));
			
			add(usersPfImgF_1);
			add(usersPfImgF_2);
			add(usersPfImgF_3);
			add(usersPfImgF_4);
		}


		ImageIcon close_img = new ImageIcon("src/resources/closebtn.png");

		//이모티콘 패널
		EmoLabel = new JLabel() {
		    protected void paintComponent(Graphics g)
		    {
		        g.setColor(new Color(255, 255, 255, 90));
		        g.fillRect(0, 0, getWidth(), getHeight());
		        super.paintComponent(g);
		    }
		};
		EmoLabel.setOpaque(false); 
		contentPane.add(EmoLabel);
		
		EmoLabel.setBounds(0, 393, 378, 90);
		
		
		
		EmoLabel.setOpaque(false);
		EmoLabel.setBackground(new Color(192, 192, 192, 80));		
		EmoLabel.setHorizontalAlignment(JLabel.RIGHT);
		contentPane.add(EmoLabel);
		EmoLabel.setVisible(false);
		JButton closebtn = new JButton(close_img);
		closebtn.setBounds(350, 0, 20, 20);
		closebtn.setBorder(BorderFactory.createEmptyBorder());
		//closebtn.setContentAreaFilled(false);
		closebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmoLabel.setVisible(false);
				EmoLabel.repaint();
			}
		});
		EmoLabel.add(closebtn);
				
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 57, 352, 420);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setBorder(null);
		contentPane.add(scrollPane);

		textArea = new JTextPane();
		textArea.setEditable(true);
		textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
		textArea.setOpaque(false);
		scrollPane.setViewportView(textArea);

		txtInput = new JTextField();
		txtInput.setBounds(12, 490, 366, 40);
		txtInput.setBorder(null);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton("\uC804\uC1A1");
		btnSend.setFont(new Font("굴림", Font.PLAIN, 12));
		btnSend.setBounds(305, 542, 61, 34);
		btnSend.setBackground(new Color(250, 218, 10));
		btnSend.setBorder(null);
		setHandCursor(btnSend);
		contentPane.add(btnSend);

		lblUserName = new JLabel("Name");
		lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName.setBackground(Color.WHITE);
		lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(12, 539, 62, 40);
		// contentPane.add(lblUserName);
		

		// AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
		UserName = username;
		lblUserName.setText(username);

		
		JButton btnNewButton = new JButton("종 료");
		btnNewButton.setFont(new Font("굴림", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
				msg.setRoomId(room_id); 
				SendObject(msg);
				System.exit(0);
			}
		});
		btnNewButton.setBounds(220, 540, 69, 40);
		setHandCursor(btnNewButton);
		// contentPane.add(btnNewButton);
				
		
		// 이미지 버튼 생성
		ImageIcon emobtn_img = new ImageIcon("src/resources/emoticon_btn.png");
		emobtn = new JButton(emobtn_img);
		emobtn.setBounds(10, 550, 25, 25);
		emobtn.setBorder(BorderFactory.createEmptyBorder());
		emobtn.setContentAreaFilled(false);		
		setHandCursor(emobtn);
		contentPane.add(emobtn);

		
		ImageIcon imgbtn_img = new ImageIcon("src/resources/image_btn.png");
		imgBtn = new JButton(imgbtn_img);
		imgBtn.setBounds(45, 550, 25, 25);
		imgBtn.setBorder(BorderFactory.createEmptyBorder());
		imgBtn.setContentAreaFilled(false);
		setHandCursor(imgBtn);
		contentPane.add(imgBtn);
		
		ImageIcon filebtn_img = new ImageIcon("src/resources/file_btn.png");
		filebtn = new JButton(filebtn_img);
		filebtn.setBounds(80, 550, 25, 25);
		filebtn.setBorder(BorderFactory.createEmptyBorder());
		filebtn.setContentAreaFilled(false);
		setHandCursor(filebtn);
		contentPane.add(filebtn);
		
		ImageIcon listbtn_img = new ImageIcon("src/resources/list_btn.png");
		listbtn = new JButton(listbtn_img);
		listbtn.setBounds(115, 550, 25, 25);
		listbtn.setBorder(BorderFactory.createEmptyBorder());
		listbtn.setContentAreaFilled(false);
		setHandCursor(listbtn);
//		contentPane.add(listbtn);
		
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 479, 390, 123);
		panel.setBackground(Color.white);
		contentPane.add(panel);
		
		JPanel room_info = new JPanel();
		room_info.setBounds(12, 5, 352, 47);
		room_info.setOpaque(false);
		room_info.setLayout(null);
		room_title = new JLabel();
		room_title.setBounds(50, 19, 228, 18);
		room_info.add(room_title);
		room_title.setFont(new Font("나눔고딕", Font.PLAIN, 14));
		String title_show= userlist.trim();
		title_show = title_show.replace(" ", ", ");
		room_title.setText(title_show);
		contentPane.add(room_info);
		
		setVisible(true);
		
		TextSendAction action = new TextSendAction();
		btnSend.addActionListener(action);
		txtInput.addActionListener(action);
		txtInput.requestFocus();
		ImageSendAction action2 = new ImageSendAction();
		imgBtn.addActionListener(action2);
		ListSendAction action3 = new ListSendAction(); 
		listbtn.addActionListener(action3);
		FileSendAction action4 = new FileSendAction();
		filebtn.addActionListener(action4);
		EmoticonSendAction action5 = new EmoticonSendAction();
		emobtn.addActionListener(action5);
	}
	
	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
				String msg = null;
				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				msg = txtInput.getText();
				if(msg.equals("(하이)")) { // 임시
					ChatMsg obcm = new ChatMsg(UserName, "300", "EMOTICON");
					obcm.setImg(icon1);
					obcm.setRoomId(room_id); // 룸 정보를 같이 보내줘야 함, 그래야 Main에서 룸 식별 가능, 하위 코드 및 이모티콘 패널에도 동일하게 적용
					SendObject(obcm);
				} else if(msg.equals("(히히)")) {
					ChatMsg obcm = new ChatMsg(UserName, "300", "EMOTICON");
					obcm.setImg(icon2);
					obcm.setRoomId(room_id); // 룸 정보를 같이 보내줘야 함, 그래야 Main에서 룸 식별 가능, 하위 코드 및 이모티콘 패널에도 동일하게 적용
					SendObject(obcm);
				} else if(EmoLabel.isVisible()) {
					ChatMsg obcm = new ChatMsg(UserName, "300", "Emoji");
					obcm.setImg(panelIMG);
					obcm.setRoomId(room_id); // ... 동일
					SendObject(obcm);
					SendMessage(msg);
				}
				else {				
					SendMessage(msg);
				}
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}

	class ImageSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 액션 이벤트가 sendBtn일때 또는 textField 에서 Enter key 치면
			if (e.getSource() == imgBtn) {
				frame = new Frame("이미지첨부");
				fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
				fd.setVisible(true);
				
				if(fd.getFile() == null) {
//					System.out.println("취소됨");
				} else {
					ChatMsg obcm = new ChatMsg(UserName, "300", "IMG");
					ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
					obcm.setImg(img);
					obcm.setRoomId(room_id);
					SendObject(obcm);
				}
				
			}
		}
	}
	
	
	
	class FileSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 액션 이벤트가 sendBtn일때 또는 textField 에서 Enter key 치면
			if (e.getSource() == filebtn) {
				frame = new Frame("파일첨부");
				fd = new FileDialog(frame, "파일 선택", FileDialog.LOAD);
				fd.setVisible(true);
				/* 파일 전송 로직 */
				if(fd.getFile() == null) {
//					System.out.println("취소됨");
				} else {
					ChatMsg obcm = new ChatMsg(UserName, "500", "FILE");
					File file = new File(fd.getDirectory() + fd.getFile()); // 파일 경로 및 이름
					try {
						FileInputStream fis = new FileInputStream(file);
						byte b[] = new byte[fis.available()]; 
						fis.read(b); // 실제 파일 내용 읽기
						obcm.setFile(b); // 실제 파일 내용 저장
						obcm.setFilename(fd.getFile()); // 파일 이름 저장
						obcm.setRoomId(room_id);
						SendObject(obcm);
						fis.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	class ListSendAction implements ActionListener { // 리스트 버튼 클릭 리스너
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == listbtn) {
//				ChatMsg obcm = new ChatMsg(UserName, "600", "LIST"); // 600 전송
//				SendObject(obcm);
//				SendRoomId("Sexy Room");
			}
		}
	}
	
	class EmoticonSendAction implements ActionListener { // 이모티콘 버튼 클릭 리스너
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == emobtn) {
				frameX=getBounds().x;
				frameY=getBounds().y;
				if(emoji==null) {
				emoji = new EmojiView(UserName, view);
				}
				else {
					emoji.dispose();
					emoji=null;
				}
			}
		}
	}
	
	ImageIcon icon1 = new ImageIcon("src/resources/icon1.jpg");
	ImageIcon icon2 = new ImageIcon("src/resources/icon2.jpg");
	private JPanel panel_1;

	public String getRoomId() {
		return this.room_id ;
	};


	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}



	// 화면에 출력
	public void AppendText(String msg, String username) {
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
//		textArea.replaceSelection(msg + "\n");
		
		if(UserName.equals(username)) { // 보낸 사람이 자신이라면 오른쪽 정렬
			AppendTextRight(msg);
		} else { // 보낸 사람이 타인이라면 왼쪽 정렬
			AppendTextLeft(msg, username);
		}
	}

	public void AppendTextLeft(String msg, String username) {
		String msg_t[] = msg.split("`", -1);
		msg = msg_t[0];
		
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		doc.setParagraphAttributes(textArea.getSelectionStart(), textArea.getSelectionEnd(), left, true);
		OthersChatPanel ocp = new OthersChatPanel(mainview);
		ocp.profileBtn.setIcon(mainview.getUserProfile(username, 40, 36));
		ocp.username.setText(username);
		LocalDateTime now = LocalDateTime.now();
		String time = now.format(DateTimeFormatter.ofPattern(" a HH:mm"));
		JLabel timeLabel; 
		if(msg_t.length > 1)
			timeLabel = new JLabel(" " + msg_t[1]);
		else
			timeLabel = new JLabel(time);
		timeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
		JLabel chatLabel = new JLabel(msg);
		chatLabel.setOpaque(true);
		chatLabel.setBackground(Color.white);
		chatLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		othersChatPanels.add(ocp);
		textArea.insertComponent(ocp);
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		if(!msg.equals("")) {
			textArea.replaceSelection("\n");
			textArea.insertComponent(timeLabel);
			textArea.insertComponent(chatLabel);			
		} 
//		else {
//			textArea.insertComponent(timeLabel);	
//		}
	}
	
	public void AppendTextRight(String msg) {
		String msg_t[] = msg.split("`", -1);
		msg = msg_t[0];
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet right = new SimpleAttributeSet();
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		doc.setParagraphAttributes(textArea.getSelectionStart(), textArea.getSelectionEnd(), right, false);
		LocalDateTime now = LocalDateTime.now();
		String time = now.format(DateTimeFormatter.ofPattern("a HH:mm "));
		JLabel timeLabel; 
		if(msg_t.length > 1)
			timeLabel = new JLabel(msg_t[1] + " ");
		else
			timeLabel = new JLabel(time);
		timeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
		if(!msg.equals("") && !msg.equals("~!@#")) {
			JLabel chatLabel = new JLabel(msg+"\n");
			chatLabel.setOpaque(true);
			chatLabel.setBackground(new Color(255, 235, 51));
			chatLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
			textArea.insertComponent(chatLabel);			
			textArea.insertComponent(timeLabel);
			int len = textArea.getDocument().getLength();
			textArea.setCaretPosition(len);
			textArea.replaceSelection("\n");
		}
//		 else if(!msg.equals("~!@#")) {
//			textArea.insertComponent(timeLabel);			
//		}
	}
	
	public void setHandCursor(JButton btn) { // 버튼에 커서 올릴 시 커서 변경하는 메소드
		btn.getCursor();
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	public void AppendFile(byte[] file, String filename) { // 파일 내용, 파일 전송 유저, 파일 이름
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len); // place caret at the end (with no selection)
		ImageIcon filebtn_img = new ImageIcon("src/resources/filedown_btn.png"); 
		JButton file_yes = new JButton(filebtn_img); // 파일 수신 패널의 이미지 버튼
		setHandCursor(file_yes);
		file_yes.setBorder(BorderFactory.createEmptyBorder());
		file_yes.setContentAreaFilled(false);

		JLabel file_name = new JLabel(filename);
		file_name.setFont(new Font("굴림체", Font.BOLD, 14)); // 파일 수신 패널의 파일 이름
			
		JPanel file_panel = new JPanel();
		file_panel.setBackground(Color.white);
		file_panel.add(file_name); // 파일 이름 추가
		file_panel.add(file_yes); // 파일 다운 버튼 추가

		String ext = filename.substring(filename.lastIndexOf(".")); // 파일 확장자 획득
			
		file_yes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 버튼 클릭시 파일 다운 Dialog 실행
				try {
					frame = new Frame("파일 저장");
					fd = new FileDialog(frame, "파일 저장할 디렉토리", FileDialog.LOAD);
					fd.setVisible(true);
					// 선택된 디렉터리에 입력된 파일 이름으로 파일 저장 ( 확장자 추가 )
					if(fd.getFile() == null) {
//						System.out.println("취소됨");
					} else {
						File recvfile = new File(fd.getDirectory() +"[" + UserName + "] " + fd.getFile() + ext);
						FileOutputStream fos = new FileOutputStream(recvfile);
						fos.write(file);
						fos.flush();
						fos.close();						
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}						
			}
		});
			
		textArea.insertComponent(file_panel);
			
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		textArea.replaceSelection("\n");
	}
	
	public void AppendImage(ImageIcon ori_icon, String username) {
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len); // place caret at the end (with no selection)
		Image ori_img = ori_icon.getImage();
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image가 너무 크면 최대 가로 또는 세로 250 기준으로 축소시킨다.
		if (width > 150 || height > 150) {
			if (width > height) { // 가로 사진
				ratio = (double) height / width;
				width = 100;
				height = (int) (width * ratio);
			} else { // 세로 사진
				ratio = (double) width / height;
				height = 100;
				width = (int) (height * ratio);
			}
			Image new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			ImageIcon new_icon = new ImageIcon(new_img);
			JLabel img_label = new JLabel(new_icon);
			img_label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) 
			    {
			        ImageViewer imgview = new ImageViewer(mainview);
			        imgview.setImage(new_icon);
			        imgview.setUserName(username);
			    }
			});
			textArea.insertComponent(img_label);
		} else
			textArea.insertIcon(ori_icon);
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		textArea.replaceSelection("\n");
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
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server에게 network으로 전송
	public void SendMessage(String msg) {
		EmoLabel.setVisible(false);
		ChatMsg obcm = new ChatMsg(UserName, "200", msg);
		obcm.setRoomId(room_id);
		mainview.SendObject(obcm); // 메인 뷰의 SendObject를 호출
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		EmoLabel.setVisible(false);
		mainview.SendObject(ob); // 메인 뷰의 SendObject를 호출

	}
	
	public void setOthersProfile(String othersName, ImageIcon othersProfile) {
		System.out.println(othersName);
		for(OthersChatPanel ocp: othersChatPanels) {
			if(ocp.username.getText().equals(othersName)) {
				System.out.println("hello!!\n");
				ocp.profileBtn.setIcon(othersProfile);
			}
		}
	}
}
