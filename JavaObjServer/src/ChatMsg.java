// ChatMsg.java ä�� �޽��� ObjectStream ��.
import java.io.File;
import java.io.Serializable;
import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String code; // 100:�α���, 400:�α׾ƿ�, 200:ä�ø޽���, 300:Image, 500:File, 600:���� ����Ʈ, 999:ä�ù�
	private String data;
	public ImageIcon img;
	public ImageIcon emoji;
	public byte[] file;
	public String filename;
	public String lastchat;
	
	/* test code */
	public String room_id;
	public String user_list;
	public String con_user_list; // ���� ���� ����
	public String selected_userlist; // ���õ� ����
	public ImageIcon profileImg;


	public ChatMsg(String id, String code, String msg) {
		this.id = id;
		this.code = code;
		this.data = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}
	
	public void setFile(byte[] file) {
		this.file= file; 
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	/* test code */
	public String getRoomId() {
		return room_id;
	}
	
	public void setRoomId(String room_id) {
		this.room_id = room_id;
	}

}