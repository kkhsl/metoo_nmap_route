package com.metoo.nspm.container.designmode;

/**
 * 单例模式
 * 懒汉式：
 *  定义对象时，不直接进行初始化
 *  在实际方法中进行初始化操作，节省一定资源
 *  并发访问，线程不安全
 */
public class SingletonFull {

    //1. 私有静态对象 先不new 默认为null值
    private static SingletonFull signletonFull;

    //2. 私有的无参构造器
    private SingletonFull(){}

    //3. 公共的静态的方法
    public static SingletonFull getInstance() throws InterruptedException {
        if(signletonFull == null){
            Thread.sleep(1000);
            signletonFull = new SingletonFull();
        }
        return signletonFull;
    }

    //测试方法
    public static void main(String[] args) {
        //利用for循环 模拟多线程环境调用
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                //看每次获取对象的hashcode是否一致 判断是否获取了同一个对象
                try {
                    System.out.println("获取的hashCode是： " + SingletonFull.getInstance().hashCode());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

}
