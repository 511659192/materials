package com.ym.materials.serializer;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ym on 2018/5/20.
 */
public class Entity implements Serializable {

    private Date date;

    private String name;

    public Entity() {
    }

    public Entity(Date date, String name) {
        this.date = date;
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public Entity setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getName() {
        return name;
    }

    public Entity setName(String name) {
        this.name = name;
        return this;
    }
}
