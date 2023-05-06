package com.metoo.nspm.core.utils;

public class Global {

    private static Global global = new Global();

    public final static  String BOARDCPU = "boardcpu";
    public static final String BOARDMEM = "boardmem";
    public static final String BOARDTEMP = "boardtemp";

    public Global() {
    }

    public static Global getInstance() {
        return global;
    }
}
