package com.me.tomcat.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;

public class Response {
    private ChannelHandlerContext ctx;

    private HttpRequest req;
    public Response(ChannelHandlerContext ctx, HttpRequest req) {
        this.ctx = ctx;
        this.req = req;
    }
    public void write(String message){
        if (null  ==  message || message.length() == 0) {
            return;
        }
        try {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(message.getBytes("utf-8"))
            );
            response.headers().set("Content-Type", "text/html");
            if (HttpUtil.isKeepAlive(req)) {
                response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.write(response);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ctx.flush();
            ctx.close();
        }

    }
}
