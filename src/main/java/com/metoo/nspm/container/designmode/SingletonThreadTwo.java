package com.metoo.nspm.container.designmode;

/**
 * 单例模式：懒汉式（线程安全）
 * 双重if判断
 */
public class SingletonThreadTwo {

    private volatile static SingletonThreadTwo singletonThreadTwo;

    private SingletonThreadTwo(){}

    private static SingletonThreadTwo getInstance(){
        if(singletonThreadTwo == null){
            synchronized (SingletonThreadTwo.class){
                if(singletonThreadTwo == null){
                    singletonThreadTwo = new SingletonThreadTwo();
                }
            }
        }
        return singletonThreadTwo;
    }

    //测试方法
    public static void main(String[] args) {
        //利用for循环 模拟多线程环境调用
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                //看每次获取对象的hashcode是否一致 判断是否获取了同一个对象
                System.out.println("获取的hashCode是： " + SingletonThreadTwo.getInstance().hashCode());
            }).start();
        }
    }
}
