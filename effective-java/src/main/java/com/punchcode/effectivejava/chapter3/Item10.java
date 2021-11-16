package com.punchcode.effectivejava.chapter3;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Item 10: Obey the general contract when overriding equals
 * @author huanruiz
 * @since 2021/11/16
 */
public class Item10 {

    /**
     * 2 Pattern不提供equals
     */
    private static Pattern pattern = Pattern.compile("a*b");

    public static void main(String[] args) {
        /** 不必重写 **/
        // 1 Thread本身没有提供value的概念
        Thread thread = new Thread();
        thread.start();
        // 2 Pattern不提供equals, eg.域中的p
        // 3. AbstractList已重写equals
        List<Integer> list = new ArrayList<>();
        // 4.私有类可以抛错, 不是必须的
        new PrivateClass();


    }
}

class PrivateClass {

    @Override
    public boolean equals(Object obj) {
        throw new AssertionError();
    }
}