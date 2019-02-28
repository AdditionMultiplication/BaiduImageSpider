package com.xjc.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

public class Base {
    //搜索图片的关键字
    private String keyword;
    //图片下载路径
    private String path;
    //下载图片线程数量
    private int downThreadNum;
    //爬取图片链接线程数量
    private int spiderThreadNum;

    public Base(String keyword, String path, int downThreadNum, int spiderThreadNum) {
        this.keyword = keyword;
        this.path = path;
        this.downThreadNum = downThreadNum;
        this.spiderThreadNum = spiderThreadNum;
    }

    //爬取线程和下载线程开启
    //CountDownLatch使爬取线程执行的过程中对主线程进行阻塞，当所有爬取线程执行完后往队列中加入”毒药“
    public void start() {
        BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        CountDownLatch noMoreToProduce=new CountDownLatch(spiderThreadNum);
        for (int i = 1; i <= spiderThreadNum; i++) {
            new SpiderThread(queue, "爬取线程" + i, keyword,noMoreToProduce).start();
        }
        for (int j = 1; j <= downThreadNum; j++) {
            new DownloadThread(queue, "下载线程" + j, path,keyword).start();
        }
        try {
            noMoreToProduce.await();    //爬取线程未结束时对main线程阻塞
            System.out.println("所有爬取线程结束");
            queue.put("end");       //加入毒药
            SpiderThread.pageNum=0;    //重新将pageNum置零
            System.out.println("所有下载线程结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
