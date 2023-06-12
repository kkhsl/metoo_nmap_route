package com.metoo.nspm.container.data.structure.enums.test;

import lombok.Getter;

public class EnumTest {

    public static void main(String[] args) {
        System.out.println(SexEnum.MALE.getSex());
    }
}

@Getter
enum SexEnum{
    MALE(1, "男"),
    FEMALE(2, "女");

    private Integer sex;
    private String sexName;


    SexEnum(int sex, String sexName) {
        this.sex = sex;
        this.sexName = sexName;
    }
}
