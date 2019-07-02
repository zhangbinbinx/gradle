package com.chart.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description: 编码器
 * @author: zhangbinbin
 * @create: 2019-06-26 21:15
 **/

public class IMEncoder extends MessageToByteEncoder
{
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        out.writeBytes(new MessagePack().write(msg));
    }
}
