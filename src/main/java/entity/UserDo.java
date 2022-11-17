package entity;

import lombok.Builder;
import lombok.Data;

@Data
public class UserDo {
	private String wechatId;

	private String wechatName;


	private String passwd;

	public UserDo(String wechatId, String wechatName, String passwd) {
		this.wechatId = wechatId;
		this.wechatName = wechatName;
		this.passwd = passwd;
	}
}
