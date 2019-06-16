package com.me.demo4.serializable;

import lombok.Data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
@Data
public class SerializableTest {
    public static void main(String[] args) throws Exception {
        Man man = new Man();
        man.setSex("男");
        man.setName("AAA");
        man.setAge("18");
        FileOutputStream fs = new FileOutputStream("D:\\ideaworkspace\\springboot\\a.txt");

        fs.write(man.toString().getBytes());
        fs.close();
    }
}
