package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendDo {
	private Long id;

	private String wechatId;

	private String friendId;

	private int status;

	private String createTime;

	private String updateTime;

	public UserFriendDo(String wechatId, String friendId) {
		this.wechatId = wechatId;
		this.friendId = friendId;
	}
}
