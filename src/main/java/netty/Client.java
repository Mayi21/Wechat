package netty;

import com.alibaba.fastjson.JSON;
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
import util.LocalContext;

import java.util.Map;
import java.util.concurrent.Callable;

public class Client implements Callable<Channel> {
	private final Logger LOG = LoggerFactory.getLogger(Client.class);

	private String host = "127.0.0.1";

	private int port = 12345;
	private Bootstrap b = new Bootstrap();

	private io.netty.channel.Channel channel;

	@Override
	public Channel call() throws Exception {
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
						pipeline.addLast("handler", new ClientHandler());
					}
				});
		ChannelFuture f = b.connect(host, port);
		return f.channel();
	}
}

class ClientHandler extends SimpleChannelInboundHandler<Message> {

	private ClientAdapter clientAdapter;

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
		switch (message.getType()) {
			case MESSAGE:
				System.out.printf("收到一条来自 %d.%s 的消息：%s%n", message.getUserId(),
						message.getUserName(), message.getMessage());
				LocalContext.getChatCon().addChat(message);
				break;
			case FRIENDLIST:
				String s = message.getMessage();
				Map<String, String> map = (Map<String, String>) JSON.parseObject(s, Map.class);
				clientAdapter.setId2UserNameMap(map);
				break;
		}

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String clientName = ctx.channel().remoteAddress().toString();
		System.out.println("RemoteAddress:"+clientName+"active!");
		clientAdapter.setClientName2Channel(clientName, ctx.channel());
		super.channelActive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println(cause.getMessage());
		super.exceptionCaught(ctx, cause);
	}
}


