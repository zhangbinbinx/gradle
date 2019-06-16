package com.me.demo4.serializable;

import lombok.Data;

import java.io.Serializable;
@Data
public class Man extends Persion implements Serializable {
    private String sex;

}
