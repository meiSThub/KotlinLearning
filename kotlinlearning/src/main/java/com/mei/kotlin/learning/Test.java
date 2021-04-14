package com.mei.kotlin.learning;

import java.util.ArrayList;

/**
 * @author mxb
 * @date 2021/4/8
 * @desc
 * @desired
 */
public class Test<T>{

    public static void main(String[] args) {
        Number number = new Integer(666);
        ArrayList<? extends Number> numberList = new ArrayList<Integer>();
        ArrayList<? super Number> numberList2 = new ArrayList();
        numberList2.add(2.5);

        Object object = numberList2.get(0);
        System.out.printf("object="+object);

//        Number num = numberList.get(0);
//        numberList.add(new Double(10.0));

        MyClass myClass=new MyClass();
        myClass.getName();
    }
}
