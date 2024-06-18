import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JavaObjClientLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUserName;
	private JTextField txtIpAddress;
	private JTextField txtPortNumber;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaObjClientLogin frame = new JavaObjClientLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public JavaObjClientLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 394, 630);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(254, 229, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("이름");
		lblNewLabel.setBounds(56, 308, 29, 33);
		lblNewLabel.setFont(new Font("굴림체", Font.PLAIN, 14));
		contentPane.add(lblNewLabel);
		
		txtUserName = new JTextField();
		txtUserName.setHorizontalAlignment(SwingConstants.CENTER);
		txtUserName.setBounds(97, 308, 205, 33);
		contentPane.add(txtUserName);
		txtUserName.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP 주소");
		lblIpAddress.setFont(new Font("굴림체", Font.PLAIN, 14));
		lblIpAddress.setBounds(35, 184, 50, 33);
		contentPane.add(lblIpAddress);
		
		txtIpAddress = new JTextField();
		txtIpAddress.setHorizontalAlignment(SwingConstants.CENTER);
		txtIpAddress.setText("127.0.0.1");
		txtIpAddress.setColumns(10);
		txtIpAddress.setBounds(97, 184, 205, 33);
		contentPane.add(txtIpAddress);
		
		JLabel lblPortNumber = new JLabel("포트 번호");
		lblPortNumber.setFont(new Font("굴림체", Font.PLAIN, 14));
		lblPortNumber.setBounds(24, 227, 65, 33);
		contentPane.add(lblPortNumber);
		
		txtPortNumber = new JTextField();
		txtPortNumber.setText("30000");
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setColumns(10);
		txtPortNumber.setBounds(97, 227, 205, 33);
		contentPane.add(txtPortNumber);
		
		JButton btnConnect = new JButton("로그인");
		btnConnect.setBounds(98, 358, 205, 38);
		btnConnect.setBackground(new Color(89, 73, 65));
		btnConnect.setForeground(Color.white);
		contentPane.add(btnConnect);
		
		JPanel panel = new JPanel() {
			Image background = new ImageIcon("src/resources/main_logo.png").getImage();
			public void paint(Graphics g) {
				g.drawImage(background, 0, 0, null);
				
			}
		};
		panel.setBounds(160, 72, 75, 75);
		contentPane.add(panel);
		
		Myaction action = new Myaction();
		btnConnect.addActionListener(action);
		txtUserName.addActionListener(action);
		txtIpAddress.addActionListener(action);
		txtPortNumber.addActionListener(action);
	}
	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!txtUserName.getText().trim().equals("")) {
				String username = txtUserName.getText().trim();
				String ip_addr = txtIpAddress.getText().trim();
				String port_no = txtPortNumber.getText().trim();
//				JavaObjClientChatRoom view = new JavaObjClientChatRoom(username, ip_addr, port_no);
				JavaObjClientMain view = new JavaObjClientMain(username, ip_addr, port_no);
				setVisible(false);
			}
		}
	}
}


