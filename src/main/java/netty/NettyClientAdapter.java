package netty;

import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NettyClientAdapter {
	private Map<String, Channel> id2ChannelMap = new ConcurrentHashMap<>();

	private Map<String, String> id2UserNameMap = new ConcurrentHashMap<>();

	private Map<String, Channel> clientName2ChannelMap = new ConcurrentHashMap<>();

	public void setId2ChannelMap(Long id, Channel channel) {
		id2ChannelMap.put(String.valueOf(id), channel);
	}

	/**
	 * 发送消息
	 * */
	public void sendMessage(Message message) {
		Channel channel = id2ChannelMap.get(String.valueOf(message.getUserId()));
		channel.writeAndFlush(message);
	}

	public void setId2UserNameMap(Map<String, String> map) {
		id2UserNameMap = map;
	}

	public void setClientName2Channel(String clientName, Channel channel) {
		clientName2ChannelMap.put(clientName, channel);
	}


}
