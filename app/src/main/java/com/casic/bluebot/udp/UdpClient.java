package com.casic.bluebot.udp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by admin on 2015/5/10.
 */
public class UdpClient extends IoHandlerAdapter
{
    private IoConnector connector;
    private static IoSession session;
    private static final Logger LOGGER = LoggerFactory.getLogger(UdpClient.class);


    public UdpClient()
    {
        this.connector = new NioDatagramConnector();

        this.connector.setHandler(this);

        ConnectFuture connFuture = this.connector.connect(new InetSocketAddress("192.168.1.110", 2345));

        connFuture.awaitUninterruptibly();

        session = connFuture.getSession();
    }

    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception
    {
        cause.printStackTrace();
    }

    public void messageReceived(IoSession session, Object message)
            throws Exception
    {
        String result = message.toString();
    }

    public void messageSent(IoSession session, Object message)
            throws Exception
    {
        LOGGER.debug(message.toString());
    }

    public void sessionClosed(IoSession session)
            throws Exception
    {
        LOGGER.debug(session.toString());
    }

    public void sessionCreated(IoSession session)
            throws Exception
    {
        LOGGER.debug(session.toString());
    }

    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception
    {
    }

    public void sessionOpened(IoSession session)
            throws Exception
    {
        LOGGER.debug(session.toString());

    }

    public static void  send(String message)
    {

        UdpClient client = new UdpClient();
        String str = message;
        byte[] data = str.getBytes();
        IoBuffer buffer = IoBuffer.allocate(data.length);
        buffer.put(data);
        buffer.flip();
        client.session.write(buffer);
     //   client.connector.broadcast("zhangfan3");
     //  client.connector.dispose(true);

    }

}
