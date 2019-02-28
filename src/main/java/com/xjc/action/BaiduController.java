package com.xjc.action;

import com.xjc.util.Base;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class BaiduController {
    private static String KEYWORD;
    private static String PATH;
    @RequestMapping("")
    public String index() {
        return "index";
    }

    @RequestMapping("/checkPath")
    @ResponseBody
    public Map<String, Object> checkPath(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        //获取前台输入的path
        String path = request.getParameter("pathInput");
        File file = new File(path);
        boolean isDir = file.isDirectory();
        map.put("isDir", isDir);
        return map;
    }

    @RequestMapping("/queryPic")
    @ResponseBody
    public List<String> queryPic(HttpServletRequest request){
        List<String> list = new ArrayList<>();
        String keyword = request.getParameter("keyword");
        String path = request.getParameter("path");
        String spiderThreadNum = request.getParameter("spiderThreadNum");
        String downloadThreadNum = request.getParameter("downloadThreadNum");
        System.out.println("keyword:" + keyword + ";path:" + path + ";spiderThreadNum:" + spiderThreadNum + ";downloadThreadNum:" + downloadThreadNum);
        Base base = new Base(keyword, path, Integer.parseInt(downloadThreadNum), Integer.parseInt(spiderThreadNum));
        BaiduController.KEYWORD=keyword;
        BaiduController.PATH=path;
        base.start();
        File file = new File(path + "/" + keyword);
        File[] files = file.listFiles();
        for (File file1 : files) {
            list.add(file1.getName());
        }
        return list;
    }


    @RequestMapping("/getPic")
    @ResponseBody
    public String getPic(HttpServletResponse response,HttpServletRequest request) throws IOException {
        String imgUrl = request.getParameter("imgUrl");
        //System.out.println(imgUrl);
        FileInputStream fis = null;
        ServletOutputStream outputStream = null;
        try {
            fis = new FileInputStream(new File(PATH+"/"+KEYWORD+"/"+imgUrl));
            response.setContentType("image/jpeg");
            outputStream = response.getOutputStream();
            //读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = fis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
            fis.close();
        }
        return null;
    }
}
