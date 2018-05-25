package com.ym.materials.customProxyFactoryBean;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by ym on 2018/5/24.
 */
public class NettyServerInvokeHandler extends SimpleChannelInboundHandler<AresRequest> {

    private final static Map<String, Semaphore> serverKeySemaphoreMap = Maps.newConcurrentMap();

    public NettyServerInvokeHandler() {
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, AresRequest request) throws Exception {
        if (ctx.channel().isWritable()) {
            ProviderServcie providerService = request.getProviderService();
            Long consumeTimeout = request.getInvokeTimeout();
            String methodName = request.getInvokeMethodName();
            String serviceKey = providerService.getServicerItf().getName();
            Integer workerThreads = providerService.getWorkerThreads();
            Semaphore semaphore = serverKeySemaphoreMap.get(serviceKey);
            if (semaphore == null) {
                synchronized (serverKeySemaphoreMap) {
                    semaphore = serverKeySemaphoreMap.get(serviceKey);
                    if (semaphore == null) {
                        semaphore = new Semaphore(workerThreads);
                        serverKeySemaphoreMap.put(serviceKey, semaphore);
                    }
                }
            }

            IRegisterCenter4Provider registerCenter4Provider = RegisterCenter.singleton();
            List<ProviderServcie> providerServcieCaches = registerCenter4Provider.getProviderServiceMap().get(serviceKey);
            ProviderServcie localProviderCache = Collections2.filter(providerServcieCaches, new Predicate<ProviderServcie>() {
                @Override
                public boolean apply(ProviderServcie input) {
                    return StringUtils.equals(input.getServiceMethod().getName(), methodName);
                }
            }).iterator().next();

            Object serviceObject = localProviderCache.getServiceObject();
            Method method = localProviderCache.getServiceMethod();
            Object result = null;
            boolean acquire = false;
            try {
                acquire = semaphore.tryAcquire(consumeTimeout, TimeUnit.MICROSECONDS);
                if (acquire) {
                    result = method.invoke(serviceObject, request.getArgs());
                }
            } catch (Exception e) {
                result = e;
            } finally {
                if (acquire) {
                    semaphore.release();
                }
            }
            AresResponse response = new AresResponse();
            response.setInvokeTimeout(consumeTimeout);
            response.setUniqueKey(request.getUniqueKey());
            response.setResult(result);
            ctx.writeAndFlush(response);
        } else {
            System.out.println("----------------channel close----------------");
        }
    }
}
