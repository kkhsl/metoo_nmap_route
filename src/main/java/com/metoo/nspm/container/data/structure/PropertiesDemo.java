package com.metoo.nspm.container.data.structure;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @description 属性
 *
 * @date 2023/03/20
 *
 * @author HKK
 */
public class PropertiesDemo {

    enum Signal {
        // 定义一个枚举类型
        GREEN, YELLOW, RED
    }

    public static class TrafficLight {

        public static void main(String[] args) {
            change();
        }
        static Signal color = Signal.RED;

        public static void change() {
            switch (color) {
                case RED:
                    color = Signal.GREEN;
                    break;
                case YELLOW:
                    color = Signal.RED;
                    break;
                case GREEN:
                    color = Signal.YELLOW;
                    break;
            }
            System.out.println(color);
        }
    }
}
