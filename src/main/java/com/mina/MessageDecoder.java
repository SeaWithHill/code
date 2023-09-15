package com.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MessageDecoder extends CumulativeProtocolDecoder {
    private static Logger logger =  LoggerFactory.getLogger(MessageDecoder.class.getName());

    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        logger.info("接收到报文---");
        int size = ioBuffer.getInt();
        byte[] message = new byte[size];
        ioBuffer.get(message);
        protocolDecoderOutput.write(message);
        logger.info("接收到报文："+new String(message));
        return true;
    }
}
