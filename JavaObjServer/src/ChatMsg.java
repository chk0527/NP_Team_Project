// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.io.File;
import java.io.Serializable;
import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image, 500:File, 600:유저 리스트, 999:채팅방
	private String data;
	public ImageIcon img;
	public ImageIcon emoji;
	public byte[] file;
	public String filename;
	public String lastchat;
	
	/* test code */
	public String room_id;
	public String user_list;
	public String con_user_list; // 현재 접속 유저
	public String selected_userlist; // 선택된 유저
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