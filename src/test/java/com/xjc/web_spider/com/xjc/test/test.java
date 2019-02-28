package com.xjc.web_spider.com.xjc.test;

import com.xjc.util.Base;

public class test {
    public static void main(String[] args) {
        String keyword = "光";
        String path = "E:\\软件\\testDown";
        int downThreadNum = 3;
        int spiderThreadNum = 3;
        Base base = new Base(keyword, path, downThreadNum, spiderThreadNum);
        base.start();
    }
}
