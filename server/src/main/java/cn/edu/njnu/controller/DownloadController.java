package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/e-resource/api")
public class DownloadController {
    @GetMapping("/v1.0/public/download")
    public String download(@RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = (String) map.get("url");
        System.out.println(url);
        Map<String, Object> reMap = new HashMap<>();
        String path = "http://223.2.50.241:8082";
        String filePath = path + url;
        OutputStream os = null;
        InputStream is = null;
        try {
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(url.getBytes("UTF-8"), "ISO8859-1"));
            File f = new File(filePath);
            is = new FileInputStream(f);
            if (is == null) {
                reMap.put("msg", "下载附件失败");
            }
            ServletOutputStream sout = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sout);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();
            bos.close();
            bis.close();
            is.close();
            os.close();
        } catch (Exception e) {
            reMap.put("msg", "下载附件失败,error:" + e.getMessage());
        }

        System.out.println(reMap);

        return "success";
        // return ResultFactory.buildSuccessResult("success", reMap);
    }

}
