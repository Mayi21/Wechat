package Client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
public class ConnetcServer {
	/**
	 * TODO 判断用户名和密码，本地登录将用户名和密码发给服务器，验证成功后返回特定字符串
	 */
	public String judgeUser(String id,String passwd){
		String s = null;
		try{
			Socket socket = new Socket("127.0.0.1",9000);
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write((id + "\n" + passwd).getBytes());
			outputStream.close();
			InputStream inputStream = socket.getInputStream();
			byte[] bytes = new byte[10];
			int bl = inputStream.read(bytes);
			s = new String(bytes,0,bl);

		} catch (Exception e){
			e.printStackTrace();
		}
		return s;
	}
	/**
	* TODO 将发送者，接收者，信息封装到message中，将其发送个服务器
	*/
	public void sendMessage(String sendName, String toName, String msg){
		Message message = new Message();
		message.setSendId(sendName);
		message.setToId(toName);
		message.setMessage(msg);

	}
}
