package com.rnginx.test.http.controller;

import com.rnginx.test.http.domain.User;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @program: RNginx
 * @description:
 * @author: 任鹏宇
 * @create: 2022-06-29 18:51
 **/
@RestController
@RequestMapping("/iphash")
public class IpHashController {


    @Value("${server.port}")
    private String port;

    private String filePath = "E:\\workstudy\\vertx\\RNginx\\RNginx-test-http\\file\\";

    @GetMapping("/get")
    public String test_get(){
        return "w1" + port;
    }


    @PostMapping("/post")
    public String test_post(@RequestBody User user){
        return user.toString() + port;
    }


    @PostMapping("/upload")
    public String test_upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        file.transferTo(new File(filePath + originalFilename));
        return originalFilename + port;
    }


    @GetMapping("/download/{fileName}")
    public void test_download(HttpServletResponse response, @PathVariable("fileName") String fileName) throws IOException {
        System.out.println("下载文件的名称：" + fileName);
        //// 1. 根据下载相对目录获取下载目录在服务器部署之后绝对目录
        //String realPath = request.getSession().getServletContext().getRealPath("/down");
        // 2. 通过文件输入流读取文件
        FileInputStream is = new FileInputStream(new File(filePath, fileName));
        // 3. 获取响应输出流
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName="+ URLEncoder.encode(fileName+port, "UTF-8"));
        // 5. 处理下载流复制
        IOUtils.copy(is,response.getOutputStream());
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(response.getOutputStream());
    }

}
