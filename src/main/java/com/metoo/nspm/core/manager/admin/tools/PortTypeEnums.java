package com.metoo.nspm.core.manager.admin.tools;

public enum PortTypeEnums {

    one(1, "other"),
    two(6, "ethernetCsmacd"),
    three(24, "softwareLoopback"),
    four(53, "propVirtual"),
    five(117, "gigabitEthernet"),
    six(135, "l2vlan"),
    seven(136, "l3ipvlan"),
    eight(150, "mplsTunnel "),;

    private final Integer value;
    private final String filed;

    private PortTypeEnums(Integer value, String filed) {
        this.value = value;
        this.filed = filed;
    }

    public Integer getValue() {
        return value;
    }

    public String getFiled() {
        return filed;
    }

    public static PortTypeEnums codeOf(int code) {
        for (PortTypeEnums prizes : values()) {
            if (prizes.getValue() == code) {
                return prizes;
            }
        }
        throw new RuntimeException("没有找到对应的枚举");
    }

    public static void main(String[] args) {
        System.out.println(PortTypeEnums.codeOf(1).getFiled());
    }
}
