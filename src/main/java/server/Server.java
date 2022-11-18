package server;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;
import enums.MessageTypeEnum;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import netty.Message;

import java.util.HashMap;
import java.util.Map;

public class Server {
	private int port = 12345;

	/**
	 * user id 到channel的映射
	 * */
	private final Map<String, Channel> channelMap = new HashMap<>();

	/**
	 * userid 到 username的映射
	 * */
	private final Map<String, String> onlineIdToNameMap = new HashMap<>();

	private final Map<String, Channel> clientName2ChannelMap = new HashMap<>();

	public synchronized void setClientName2ChannelMap(String clientName, Channel channel) {
		clientName2ChannelMap.put(clientName, channel);
	}

	public Map<String, Channel> getClientName2ChannelMap() {
		return this.clientName2ChannelMap;
	}

	public synchronized void setOnlineIdToNameMap(String userId, String userName) {
		onlineIdToNameMap.put(userId, userName);
	}

	public Map<String, String> getOnlineIdToNameMap() {
		return onlineIdToNameMap;
	}


	public synchronized void setChannel(String userId, Channel channel) {
		this.channelMap.put(userId, channel);
	}


	public Map<String, Channel> getChannelMap() {
		return this.channelMap;
	}
	/**
	 * 上线通知
	 * 新用户上线后，需要将当前的在线进行更新
	 * */
	public void notifyOnlineMemChange() {
		for (String clientName : clientName2ChannelMap.keySet()) {
			Channel channel = clientName2ChannelMap.get(clientName);
			channel.writeAndFlush(getNotifyMessage());
		}
	}

	public Message getNotifyMessage() {
		String msg = JSON.toJSONString(onlineIdToNameMap);
		Message message = new Message();
		message.setMessage(msg);
		message.setType(MessageTypeEnum.NOTIFICATION);
		return message;
	}

	//发送消息给下游设备
	public void writeMsg(Message msg) {
		Map<String, Channel> channelMap = getChannelMap();
		try {
			Channel channel = channelMap.get(String.valueOf(msg.getToUserId()));
			if (!channel.isActive()) {
				System.out.println("it's not online");
				channelMap.remove(String.valueOf(msg.getToUserId()));
			}
			channel.writeAndFlush(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void bind() {
		System.out.println("service start successful");
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						ChannelPipeline pipeline = socketChannel.pipeline();
						pipeline.addLast("decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
						pipeline.addLast("encoder", new ObjectEncoder());
						pipeline.addLast("handler", new ServerHandler(Server.this));
					}
				});
		try {
			ChannelFuture f = bootstrap.bind(port).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}

class ServerHandler extends SimpleChannelInboundHandler<Message> {
	private Server nettyServer;

	public ServerHandler(Server server){
		this.nettyServer = server;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
		switch (message.getType()) {
			case MESSAGE:
				// 发送消息
				nettyServer.writeMsg(message);
				break;

			case SIGN:
				// 新用户上线后
				String userId = message.getUserId();
				nettyServer.setChannel(userId, channelHandlerContext.channel());
				nettyServer.setOnlineIdToNameMap(userId, message.getUserName());
				System.out.println(userId + "注册成功");
				nettyServer.notifyOnlineMemChange();
				channelHandlerContext.writeAndFlush(nettyServer.getNotifyMessage());
				break;

			case NOTIFICATION:
				break;
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		/**
		 * 在收到新的链接后，先把当前在线的人员发给客户端
		 * */
		Channel channel = ctx.channel();
		String clientName = channel.remoteAddress().toString();
		System.out.println("RemoteAddress:"+clientName+"active!");
		nettyServer.setClientName2ChannelMap(clientName, channel);
		// 获取当前在线的用户，发送给新上线的用户
		channel.writeAndFlush(nettyServer.getNotifyMessage());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel channel = ctx.channel();
		channel.close();
		super.exceptionCaught(ctx, cause);
	}
}