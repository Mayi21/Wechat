package entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserViewVo {
	private String id;
	private String name;

	@Override
	public String toString() {
		return String.format("%s/%s", this.name, this.id);
	}

}
