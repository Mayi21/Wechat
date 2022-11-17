package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClient {
	private final Logger LOG = LoggerFactory.getLogger(NettyClient.class);

	private String host = "127.0.0.1";

	private int port = 12345;
	private Bootstrap b = new Bootstrap();

	private Channel channel;

	private void start() {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		b.group(workerGroup).option(ChannelOption.SO_KEEPALIVE, true)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						ChannelPipeline pipeline = socketChannel.pipeline();
						//字符串编码解码
						pipeline.addLast("encoder", new ObjectEncoder());
						pipeline.addLast("decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
						//客户端的逻辑
						pipeline.addLast("handler", new NettyClientHandler());
					}
				});
		ChannelFuture f = b.connect(host, port);
		channel = f.channel();
	}

//	/**
//	 * 构造正常发送的消息
//	 * */
//	public Message getChatMessage(String msg) {
//		Message message = new Message();
//		message.setType(0);
//		message.setMessage(msg);
//		message.setUserName(userName);
//		message.setUserId(userId);
//		message.setToUserId(toUserId);
//		message.setToUserName(noToUserName.get(String.valueOf(toUserId)));
//		return message;
//	}
//
//	public Message getRegisterMessage() {
//		Message message = new Message();
//		message.setUserId(userId);
//		message.setUserName(userName);
//		message.setType(1);
//		return message;
//	}
}