import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopException;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by liuyumeng on 2018/7/1.
 */
public class EchoServer {
    private int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if(args.length!=1){
            System.out.println("需要手动设置端口号："+EchoServer.class.getSimpleName()+" <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        System.out.println("服务端已启动："+EchoServer.class.getSimpleName()+" port:"+port);
        new EchoServer(port).start();
    }

    public void start() throws Exception{
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        //核心类 ServerBootstrap 引导和绑定服务器
        ServerBootstrap b = new ServerBootstrap();
        b.group(eventLoopGroup)
                //使用NIO传输，因此指定NioServerSocketChannel接受和处理新的连接
                .channel(NioServerSocketChannel.class)
                //服务器绑定到该端口，监听连接请求
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //当一个新的连接建立时，echoServerHandler将被加入到channel的pipeline中
                        socketChannel.pipeline().addLast(echoServerHandler);
                    }
                });
        //异步地绑定服务器，调用sync阻塞等待知道绑定完成
        ChannelFuture channelFuture = b.bind().sync();
        //获取Channel的CloseFuture，并且阻塞当前线程知道它完成
        channelFuture.channel().closeFuture().sync();
        //关闭eventLoopGroup，并且释放所有资源
        eventLoopGroup.shutdownGracefully().sync();
    }
}
