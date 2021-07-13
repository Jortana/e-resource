package cn.edu.njnu.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.PrimitiveIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.njnu.mapper.ResourceMapper;
import cn.edu.njnu.pojo.Resource;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class DownloadService {
    private final ResourceMapper resourceMapper;
    //在文件操作中，不用/或者\最好，推荐使用File.separator
    private  final static String rootPath = "http://222.192.6.62:8082";

    public DownloadService(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    public Result downloadFile(@RequestParam Map<String, Object> resourceIDMap, final HttpServletResponse response, final HttpServletRequest request){
        int resourceID = Integer.parseInt((String)resourceIDMap.get("resourceID"));
        OutputStream os = null;
        InputStream is= null;
        try {
            Resource resource = resourceMapper.queryResourceByID(resourceID);

            String url = resource.getUrl();
//            System.out.println(url);
            String fileName = url.split("/")[url.split("/").length-1];
            URL urlfile = null;
            HttpURLConnection httpUrl = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            String resultWordPath = encode(rootPath + url);
//            System.out.println(resultWordPath);
            File fd = new File("D:\\download\\"+fileName);
            try {
                urlfile = new URL(resultWordPath);
                httpUrl = (HttpURLConnection) urlfile.openConnection();
                httpUrl.connect();
                bis = new BufferedInputStream(httpUrl.getInputStream());
                bos = new BufferedOutputStream(new FileOutputStream(fd));
                int len = 2048;
                byte[] b = new byte[len];
                while ((len = bis.read(b)) != -1) {
                    bos.write(b, 0, len);
                }
                bos.flush();
                bis.close();
                httpUrl.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bis.close();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 取得输出流
            os = response.getOutputStream();
            // 清空输出流
            response.reset();
            // response.setContentType("application/x-download;charset=GBK");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("utf-8"), "iso-8859-1"));
            //读取流
            File f = new File("D:\\download\\"+fileName);
            is = new FileInputStream(f);
            if (is == null) {
                return ResultFactory.buildFailResult("下载附件失败，请检查文件“" + fileName + "”是否存在");
            }
            resourceMapper.updateDownload(resource.getDownload()+1,resourceID);
            //复制
            IOUtils.copy(is, response.getOutputStream());
            response.getOutputStream().flush();

        } catch (IOException e) {
            return ResultFactory.buildFailResult("下载附件失败,error:"+e.getMessage());
        }
        //文件的关闭放在finally中
        finally
        {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return ResultFactory.buildSuccessResult("下载成功",null);
    }
    public static String encode(String url) {
        try {
            String resultURL = "";
            //遍历字符串
            for (int i = 0; i < url.length(); i++) {
                char charAt = url.charAt(i);
                //只对汉字处理
                if (isChineseChar(charAt)) {
                    String encode = URLEncoder.encode(charAt + "", "UTF-8");
                    resultURL += encode;
                }
                else if ((int)charAt ==32){
                    resultURL += "%20";
                }
                else {
                    resultURL += charAt;
                }
            }
            return resultURL;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //判断汉字的方法,只要编码在\u4e00到\u9fa5之间的都是汉字
    public static boolean isChineseChar(char c) {
        return String.valueOf(c).matches("[\u4e00-\u9fa5]");
    }
}
