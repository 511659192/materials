package com.ym.materials.serializer;

import com.alibaba.fastjson.JSON;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

/**
 * Created by ym on 2018/5/20.
 */
public class ObjenesisDemo {

    public static void main(String[] args) {
        Objenesis objenesis = new ObjenesisStd();
        MessageInfo messageInfo = objenesis.newInstance(MessageInfo.class);
        System.out.println(messageInfo.getClass());
        System.out.println(JSON.toJSONString(messageInfo));


        Objenesis objenesis2 = new ObjenesisStd();
        ObjectInstantiator<? extends MessageInfo> instantiatorOf = objenesis2.getInstantiatorOf(messageInfo.getClass());
        MessageInfo messageInfo1 = instantiatorOf.newInstance();
        System.out.println(messageInfo1.getClass());
        System.out.println(JSON.toJSONString(messageInfo1));
    }
}

class MessageInfo {
    private Long messageId;

    public MessageInfo(Long messageId) {
        this.messageId = messageId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public MessageInfo setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }
}
