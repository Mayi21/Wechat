package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogPo {
	private Long id;

	private String log;

	private String from;

	private String to;

	public LogPo(String log, String from, String to) {
		this.log = log;
		this.from = from;
		this.to = to;
	}
}
