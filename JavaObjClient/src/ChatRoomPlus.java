import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;

public class ChatRoomPlus extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JavaObjClientMain mainview;
	public ChatRoomPlus view;
	public JTextPane ulArea;
	public List<ChatRoomPlusUser> userslist = new ArrayList<ChatRoomPlusUser>();

	@SuppressWarnings("null")
	public ChatRoomPlus(String myname, String con_user_list, JavaObjClientMain view) {
		mainview = view;
		setBounds(mainview.frameX + 23, mainview.frameY + 50, 350, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.white);

		JScrollPane ul_scrollPane = new JScrollPane();
		ul_scrollPane.setBounds(27, 65, 284, 379);
		ul_scrollPane.getViewport().setOpaque(false);
		ul_scrollPane.setOpaque(false);
		ul_scrollPane.setBorder(null);
		contentPane.add(ul_scrollPane);
		
		ulArea = new JTextPane();
		ulArea.setEditable(true);
		ulArea.setFont(new Font("굴림체", Font.PLAIN, 14));
		ulArea.setOpaque(false);
		ul_scrollPane.setViewportView(ulArea); // scrollpane에 ulArea 추가
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel selectUsers = new JLabel("대화상대 선택");
		selectUsers.setFont(new Font("굴림체", Font.BOLD, 16));
		selectUsers.setBounds(27, 23, 114, 30);
		contentPane.add(selectUsers);
		
		JButton confirmBtn = new JButton("확인");
		confirmBtn.setBounds(150, 460, 75, 36);
		confirmBtn.setFont(new Font("굴림", Font.PLAIN, 12));
		confirmBtn.setBackground(new Color(250, 218, 10));
		confirmBtn.setBorder(null);
		confirmBtn.getCursor();
		confirmBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(confirmBtn);
		
		JButton cancelBtn = new JButton("취소");
		cancelBtn.setBounds(235, 460, 75, 36);
		cancelBtn.setFont(new Font("굴림", Font.PLAIN, 12));
		cancelBtn.setBackground(Color.white);
		cancelBtn.getCursor();
		cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(cancelBtn);
		
		con_user_list.trim();
		String[] user = con_user_list.split(" ");
		for(int i=0; i<user.length; i++) {
			ChatRoomPlusUser users = new ChatRoomPlusUser(user[i], view);
			if(user[i].equals(myname)) {
				users.checkBox.setSelected(true);
				users.checkBox.setEnabled(false);
			}
			userslist.add(users);
			ulArea.insertComponent(users);
		}
		
		confirmBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String userlist = "";
				for(ChatRoomPlusUser username : userslist) {
					if(username.checkBox.isSelected()) {
						userlist += username.getUserName() + " ";
					}
				}
				mainview.SendRoomId(userlist);
				dispose();
			}
		});
		
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		
		setVisible(true);
	}
}
