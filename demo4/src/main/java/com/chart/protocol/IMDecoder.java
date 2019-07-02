package com.chart.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 解码器
 * @author: zhangbinbin
 * @create: 2019-06-26 21:15
 **/

public class IMDecoder extends ByteToMessageDecoder {
    private Pattern pattern = Pattern.compile("^\\[(.*)\\](\\s\\-\\(.*))?");
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        final int length = in.readableBytes();
        final byte[]array = new byte[length];
        String content = new String(array,in.readerIndex(),length);
        if(!((null == content) || "".equals(content.trim()))){
            if(!IMP.isIMP(content)){
                ctx.channel().pipeline().remove(this);
                return;
            }
        }
        in.getBytes(in.readerIndex(),array,0,length);
        out.add(new MessagePack().read(array,IMMessage.class));
        in.clear();
    }

    public IMMessage decode(String msg){
        if((null == msg) || "".equals(msg.trim())){return null;}
        Matcher matcher = pattern.matcher(msg);
        String header = "";
        String content = "";
        if(matcher.matches()){
            header = matcher.group(1);
            content = matcher.group(3);
        }
        String[] headers = header.split("\\]\\[");
        long time =  Long.parseLong(headers[1]);
        String nickName = headers[2];
        nickName = nickName.length() > 10 ? nickName.substring(0,9) : nickName;
        return null;
    }
}
