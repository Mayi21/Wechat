/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package Server.NettyStydy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
	private final int port;
	public EchoServer(int port) {
		this.port = port;
	}
	public static void main(String[] args) throws Exception {
		System.err.println("Usage: " + EchoServer.class.getSimpleName() + "<port>");
		int port = 8999;
		new EchoServer(port).start();
	}
	public void start() throws Exception{
		final EchoServerHandler serverHandler = new EchoServerHandler();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group).channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					socketChannel.pipeline().addLast(serverHandler);
				}
			});
			ChannelFuture future = b.bind().sync();
			future.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
}
