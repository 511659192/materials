package com.ym.materials.thrift.server;

import com.facebook.swift.codec.ThriftField;
import com.facebook.swift.codec.ThriftStruct;

/**
 * Created by ym on 2018/5/17.
 */
@ThriftStruct
public final class User {

    private String name;

    private String email;

    @ThriftField(1)
    public String getName() {
        return name;
    }

    @ThriftField
    public User setName(String name) {
        this.name = name;
        return this;
    }

    @ThriftField(2)
    public String getEmail() {
        return email;
    }

    @ThriftField
    public User setEmail(String email) {
        this.email = email;
        return this;
    }
}
