package com.metoo.nspm.container.designmode;

/**
 * 单例模式
 * 静态内部类
 *  通过静态内部类实现懒加载与线程安全
 *  利用JVM特性实现 JVM在加载类和内部类的时候，只会在运行的时候加载一次，从而保证线程安全和懒加载
 */
public class SingletonStaticClass {

    // 私有无参构造器
    private SingletonStaticClass (){}

    // 私有静态内部类
    private static class SingletonStatic{
        // 在私有内部类中定义私有的最终的静态对象
        private static final SingletonStaticClass singletonStaticClass = new SingletonStaticClass();
    }

    // 公共静态实例方法
    public static SingletonStaticClass getInstance(){
        return SingletonStatic.singletonStaticClass;
    }

    //测试方法
    public static void main(String[] args) {
        //利用for循环 模拟多线程环境调用
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                //看每次获取对象的hashcode是否一致 判断是否获取了同一个对象
                System.out.println("获取的hashCode是： "+ SingletonStaticClass.getInstance().hashCode());
            }).start();
        }
    }

}
