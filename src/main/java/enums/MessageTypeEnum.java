package enums;

import java.util.HashMap;
import java.util.Map;

public enum MessageTypeEnum {
	MESSAGE("message"),
	FRIENDLIST("friendList"),
	SIGN("sign"),
	NOTIFICATION("notification");
	private static Map<String, MessageTypeEnum> map;

	static {
		map = new HashMap<>();
		MessageTypeEnum[] values = MessageTypeEnum.values();
		for (MessageTypeEnum v : values) {
			map.put(v.value, v);
		}
	}

	private String value;

	MessageTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static MessageTypeEnum getEnumByValue(String value) {
		return map.get(value);
	}
}
