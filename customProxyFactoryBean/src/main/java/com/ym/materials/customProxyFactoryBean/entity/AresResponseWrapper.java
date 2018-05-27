package com.ym.materials.customProxyFactoryBean.entity;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by ym on 2018/5/25.
 */
public class AresResponseWrapper {

    private BlockingQueue<AresResponse> responseQueue = new ArrayBlockingQueue<AresResponse>(1);

    private long reponseTime;

    public boolean isExpire() {
        AresResponse response = responseQueue.peek();
        if (response == null) {
            return false;
        }

        long timeout = response.getInvokerTimeout();
        if (System.currentTimeMillis() - reponseTime > timeout) {
            return true;
        }
        return false;
    }

    public static AresResponseWrapper of() {
        return new AresResponseWrapper();
    }

    public BlockingQueue<AresResponse> getResponseQueue() {
        return responseQueue;
    }

    public long getReponseTime() {
        return reponseTime;
    }

    public void setReponseTime(long reponseTime) {
        this.reponseTime = reponseTime;
    }
}
