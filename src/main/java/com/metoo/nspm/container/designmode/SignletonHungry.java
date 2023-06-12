package com.metoo.nspm.container.designmode;

/**
 * 单例模式
 * 饿汉式
 *  上来就new对象
 */
public class SignletonHungry {

    //1. 私有的静态的最终的对象
    private static final SignletonHungry singlton = new SignletonHungry();

    // 私有无参构造方法
    private SignletonHungry(){}

    public static SignletonHungry getInstance(){
        return singlton;
    }

    // 测试方法
    public static void main(String[] args) {
        // 利用for循环， 模拟多线程调用
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                //看每次获取对象的hashcode是否一致 判断是否获取了同一个对象
                System.out.println("获取的hashCode是： "+SignletonHungry.getInstance().hashCode());
            }).start();
        }
    }


}
