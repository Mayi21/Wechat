package entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DBConfigEntity {
	private String userName;

	private String passwd;

	private String url;
}
