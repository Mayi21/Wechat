package netty;

import com.fasterxml.jackson.annotation.JsonProperty;
import enums.MessageTypeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {
	@JsonProperty(value = "user_name")
	private String userName;

	@JsonProperty(value = "user_id")
	private String userId;

	private String message;

	@JsonProperty(value = "to_user_name")
	private String toUserName;

	@JsonProperty(value = "to_user_id")
	private String toUserId;

	/**
	 * 类型目前有：
	 * 	普通消息 message;
	 * 	注册消息 sign;
	 * 	好友列表 friendList
	 * 	通知消息 notification
	 * */
	private MessageTypeEnum type;

	@Override
	public String toString() {
		return "Message{" +
				"userName='" + userName + '\'' +
				", userId=" + userId +
				", message='" + message + '\'' +
				", toUserName='" + toUserName + '\'' +
				", toUserId=" + toUserId +
				", type=" + type.getValue() +
				'}';
	}
}
