import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.LoggerFactory;

/**
 * Created by liuyumeng on 2018/7/1.
 * Echo客户端：
 * 1.连接到服务器
 * 2.发送一个或者多个消息
 * 3.对于每个消息，等待并接收从服务器发回的相同消息
 * 4.关闭连接
 *
 * 需要扩展SimpleChannelInboundHandler类以处理必须的任务
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
    private org.slf4j.Logger logger = LoggerFactory.getLogger(EchoClientHandler.class);
    /**
     * 在到服务器的连接已经建立之后被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端你在干嘛？：", CharsetUtil.UTF_8));
    }
    /**
     * 在处理过程中引发异常时被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    /**
     * 每当从服务器接收到一条消息时被调用（数据可能被分块接受、该方法可能被调用多次）
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        //logger.info("客户端接收："+byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端接收："+byteBuf.toString(CharsetUtil.UTF_8));
    }
}
