package com.metoo.nspm.container.designmode;

/**
 * 单例模式：加锁（针对懒汉模式的线程不安全）
 *  线程安全
 *  效率低，受Synchronized锁升级的影响
 */
public class SingletonThread {

    // 私有 静态 对象
    private static SingletonThread singletonThread;

    // 私有静态变量
    private SingletonThread(){}

    // 公共静态方法，在if中加锁
    public static SingletonThread getInstance(){
        if(singletonThread == null){
            synchronized (SingletonThread.class){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                singletonThread  = new SingletonThread();
            }
        }
        return singletonThread;
    }

    //测试方法
    public static void main(String[] args) {
        //利用for循环 模拟多线程环境调用
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                //看每次获取对象的hashcode是否一致 判断是否获取了同一个对象
                System.out.println("获取的hashCode是： " + SingletonThread.getInstance().hashCode());
            }).start();
        }
    }
}
