package Client;

import java.io.Serializable;

public class User implements Serializable {
	String status;
	String id;
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
