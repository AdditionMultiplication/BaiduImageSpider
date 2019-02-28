package com.xjc.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

//下载图片工具类
public class DownloadUtil {
    //图片储存本地路径
    private String path;
    //查询的关键字
    private String keyword;

    public DownloadUtil(String path, String keyword) {
        this.path = path;
        this.keyword = keyword;
    }

    //处理路径
    private File delPath() {
        File dir = new File(path, keyword);
        boolean exists = dir.exists();
        if (!exists) {        //文件路径不存在,创建文件夹
            dir.mkdirs();
        }
        return dir;
    }

    //下载图片
    public void downloadPic(String url) {
        //System.out.println("图片链接："+url);
        File dir = delPath();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        int len;
        byte[] bytes;
        try {
            // 创建Httpclient对象,链接网络下载图片
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse execute = httpClient.execute(httpGet);
            if (execute.getStatusLine().getStatusCode() == 200) {    //网络链接成功
                String keyPath = dir.getAbsolutePath();
//                String picName = url.substring(url.lastIndexOf("/"));
                String picName = System.currentTimeMillis() + ".jpg";
                //获得图片的流
                InputStream content = execute.getEntity().getContent();
                bis = new BufferedInputStream(content);
                File file = new File(keyPath + "/" + picName);
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bytes = new byte[2048];
                while ((len = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);
                    bos.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
