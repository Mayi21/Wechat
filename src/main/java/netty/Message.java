package netty;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {
	@JsonProperty(value = "user_name")
	private String userName;

	@JsonProperty(value = "user_id")
	private Long userId;

	private String message;

	@JsonProperty(value = "to_user_name")
	private String toUserName;

	@JsonProperty(value = "to_user_id")
	private Long toUserId;

	/**
	 * 类型目前有：
	 * 	0:普通消息;
	 * 	1:注册消息;
	 * 	2:心跳
	 * 	3.好友列表
	 * */
	private int type;

	@Override
	public String toString() {
		return "Message{" +
				"userName='" + userName + '\'' +
				", userId=" + userId +
				", message='" + message + '\'' +
				", toUserName='" + toUserName + '\'' +
				", toUserId=" + toUserId +
				", type=" + type +
				'}';
	}
}
