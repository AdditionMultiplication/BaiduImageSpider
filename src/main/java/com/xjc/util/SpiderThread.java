package com.xjc.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpiderThread extends Thread {
    private BlockingQueue<String> queue;
    private CountDownLatch countDownLatch;
    private String name;
    private String keyword;
    public static int pageNum = 0;
    private static int sumTime = 0;
    private static int sumUrl = 0;
    private final String reg = "thumbURL\":\"https://.+?\\.jpg";

    public SpiderThread(BlockingQueue<String> queue, String name, String keyword, CountDownLatch countDownLatch) {
        super();
        this.queue = queue;
        this.name = name;
        this.keyword = keyword;
        this.countDownLatch = countDownLatch;
    }

    //爬取的页数递增
    private synchronized int addPageNum() {
        return pageNum += 1;
    }

    private synchronized long sumTime(long time) {
        return sumTime += time;
    }

    private synchronized int sumUrl(int num) {
        return sumUrl += num;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int num = addPageNum();
                long start = System.currentTimeMillis();
                //System.out.println(this.name + "正在爬取第" + num + "页");
                String url = "https://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&queryWord=" + keyword + "&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&hd=&latest=&copyright=&word=" + keyword + "&s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&fr=&expermode=&force=&cg=girl&pn=" + pageNum * 30 + "&rn=30&gsm=1e&1550670916044=";
                // 创建Httpclient对象
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(url);
                CloseableHttpResponse execute = null;
                execute = httpClient.execute(httpGet);
                if (execute.getStatusLine().getStatusCode() == 200) {
                    String result = null;
                    result = EntityUtils.toString(execute.getEntity(), "utf-8");
                    Pattern pattern = Pattern.compile(reg);
                    Matcher matcher = pattern.matcher(result);
                    //计数器：计算本次爬到的数量
                    int count = 0;
                    while (matcher.find()) {
                        String thumbURL = matcher.group().substring(11);
                        System.out.println(this.name + ":" + thumbURL);
                        queue.put(thumbURL);
                        count++;
                    }
                    if (count == 0) {
                        pageNum -= 1;
                        // queue.put("一共爬了"+Math.round(sumUrl/30.0)+"页；总链接数：" + sumUrl+"条;总耗时：" + sumTime+"ms");
                        break;
                    }
                    long end = System.currentTimeMillis();
                    sumUrl(count);   //计算爬取到的连接数量
                    sumTime(end - start);   //计算爬取总耗时
                } else {
                    System.out.println("访问失败！！！");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            //System.out.println(this.name + "爬取结束");
            countDownLatch.countDown();
        }
    }

}

