package com.mina;


import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MessageProtocolCodecFactory  implements ProtocolCodecFactory {
    /**
     * message decoder
     */
    private MessageDecoder decoder=new MessageDecoder();

    /**
     * message encoder
     */
    private MessageEncoder encoder= new MessageEncoder();

    /**
     * get encoder
     */
    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    /**
     * get decoder
     */
    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
}
