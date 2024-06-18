import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class FriendListPanel extends JPanel {
	private static final long serialVersionUID = 7410115339163008109L;
	private JLabel FriendList_username;
	public JButton profileBtn;
	private Frame frame;
	private FileDialog fd;
	public JavaObjClientMain mainview; // ���� SendObject Ȱ���� ����
	public FriendListPanel fview;
	public ImageIcon profile;
	public int frameX;
	public int frameY;

	public FriendListPanel(ImageIcon profile, String username, JavaObjClientMain view, String userstatus) { // �並 ���ڷ� ����
		this.profile = profile;
		mainview = view;
		fview = this;
		setLayout(null); // absolute layout
		setBackground(Color.white);
		setPreferredSize(new Dimension(320,70)); // ���� ������ �����ѵ� ?
		addMouseListener(new MouseListener() {			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				setBackground(Color.white);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				setBackground(new Color(248, 248, 248));
			}	

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		FriendList_username = new JLabel(username);
		FriendList_username.setBounds(81, 14, 114, 30);
		FriendList_username.setFont(new Font("����ü", Font.PLAIN, 14));
		add(FriendList_username);

		profileBtn = new JButton(this.profile);
		profileBtn.setBounds(12, 14, 46, 46);
		profileBtn.setBorderPainted(false);
		profileBtn.setContentAreaFilled(false);
		profileBtn.setFocusPainted(false);
		profileBtn.getCursor();
		profileBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		add(profileBtn);
		
		JPanel status = new JPanel();
		if(userstatus.equals("ON")) {
			status.setBackground(new Color(101, 226, 104));
		} else {
			status.setBackground(new Color(224, 63, 99));
		}
		status.setBounds(268, 26, 15, 15);
		add(status);

		profileBtn.addMouseListener(new MouseListener() {			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

				setBackground(Color.white);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				setBackground(new Color(248, 248, 248));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		profileBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(username.equals(mainview.UserName)) { // �г��� �̸��� ���κ��� �̸��� ���� ���, �� ������ ��쿡�� ���� ������
					if (e.getSource() == profileBtn) {
						frame = new Frame("������ ���� ����");
						fd = new FileDialog(frame, "������ ���� ����", FileDialog.LOAD);
						fd.setVisible(true);
						
						if(fd.getFile() == null) {
//							System.out.println("��ҵ�");
						} else {
							ChatMsg obcm = new ChatMsg(username, "700", "PROFILEIMG"); // ������ ���õǸ� 888 �ڵ��
							ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile()); // �����ϰ��� �ϴ� �̹��� ��������
							obcm.setImg(img); // ��ü�� ���
							mainview.SendObject(obcm); // ���κ��� SendObject ȣ��
						}	
					}
				} else {
					mainview.frameX = mainview.getBounds().x;
					mainview.frameY = mainview.getBounds().y;
					OthersProfileImage profileViewer = new OthersProfileImage(mainview);
					profileViewer.setUserName(FriendList_username.getText());
					profileViewer.setProfileImage(profileBtn.getIcon());
				}
			}
			
		});
	}
	public String getFriendList_username() {
		return FriendList_username.getText();
	}
	
}