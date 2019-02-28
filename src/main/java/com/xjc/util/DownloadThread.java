package com.xjc.util;

import java.util.concurrent.BlockingQueue;

public class DownloadThread extends Thread {
    private BlockingQueue<String> queue;
    private DownloadUtil downloadUtil;
//    private String name;
//    private String path;
    private String keyword;

    public DownloadThread(BlockingQueue<String> queue, String name, String path,String keyword) {
        super();
        this.queue = queue;
//        this.name = name;
//        this.path = path;
        this.keyword=keyword;
        this.downloadUtil=new DownloadUtil(path,keyword);
    }

    @Override
    public void run() {
        while (true) {
            String take = null;
            try {
                take = queue.take();
                if ("end".equals(take)) {
                    //System.out.println(this.name + "下载完成");
                    queue.put(take);
                    break;
                }
               // System.out.println(name+"，从队列中取出了"+take);
                downloadUtil.downloadPic(take);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
