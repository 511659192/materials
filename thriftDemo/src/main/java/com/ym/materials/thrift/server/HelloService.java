package com.ym.materials.thrift.server;

import com.facebook.swift.codec.ThriftField;
import com.facebook.swift.service.ThriftMethod;
import com.facebook.swift.service.ThriftService;

/**
 * Created by ym on 2018/5/17.
 */
@ThriftService("helloService")
public interface HelloService {

    @ThriftMethod
    void sayHello(@ThriftField(name = "user") User user);

}
