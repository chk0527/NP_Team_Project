import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ImageViewer extends JFrame {

	private JPanel contentPane;
	private JLabel profileImage;
	private JLabel username;

	/**
	 * Create the frame.
	 */
	public ImageViewer(JavaObjClientMain view) {
		setBounds(view.frameX, view.frameY, 300, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		contentPane.setBackground(Color.white);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		profileImage = new JLabel();
		profileImage.setBounds(52, 50, 185, 185);
		contentPane.add(profileImage);

		username = new JLabel("", SwingConstants.CENTER);
		username.setFont(new Font("����ü", Font.PLAIN, 14));
		username.setBounds(70, 29, 150, 20);
		contentPane.add(username);

		setVisible(true);
	}

	public void setImage(Icon icon) {
		profileImage.setIcon(makeFitImage(icon));
	}

	public void setUserName(String username) {
		this.username.setText("���� ���: " + username);
	}

	private ImageIcon makeFitImage(Icon ori_icon) {
		ImageIcon new_icon = (ImageIcon) ori_icon;
		Image ori_img = ((ImageIcon) ori_icon).getImage();
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image�� �ʹ� ũ�� �ִ� ���� �Ǵ� ���� 250 �������� ��ҽ�Ų��.
		if (width < 185 || height < 185) {
			Image new_img = ori_img.getScaledInstance(185, 185, Image.SCALE_SMOOTH);
			new_icon = new ImageIcon(new_img);
		}

		return new_icon;
	}
}