package cn.edu.njnu.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.njnu.mapper.FavoriteMapper;
import cn.edu.njnu.mapper.ResourceMapper;
import cn.edu.njnu.pojo.Resource;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.bouncycastle.util.test.Test;
import org.neo4j.driver.v1.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import static org.neo4j.driver.v1.Values.parameters;

@Service
public class DownloadService {
    private final ResourceMapper resourceMapper;
    private final FavoriteMapper favoriteMapper;
    //在文件操作中，不用/或者\最好，推荐使用File.separator
    private  final static String rootPath = "http://222.192.6.62:8082";
    private Driver createDrive(){
        return GraphDatabase.driver( "bolt://222.192.6.62:7687", AuthTokens.basic( "neo4j", "123456" ) );
    }
    public DownloadService(ResourceMapper resourceMapper, FavoriteMapper favoriteMapper) {
        this.resourceMapper = resourceMapper;
        this.favoriteMapper = favoriteMapper;
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

    public Result downloadFolder(Map<String, Object> folderIDMap) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        String folderID = (String) folderIDMap.get("folderID");
        System.out.println(folderID);
        String path = "E:\\TestResource\\download\\" + folderID + "\\";
//        String path = "D:\\" + folderID + "\\";
        File file = new File(path);
        file.mkdirs();
        String fileName = "知识点+学习目标+学习重难点收藏.docx";
        String filePath = path + fileName;
        //创建word
        createWord(path,fileName);
        //写入数据
        ArrayList<String> data = getStr(folderID);
        writeDataDocx(filePath,data,false,12);
        copyAll(folderID, path);
        String zipPath = "E:\\TestResource\\download\\" + folderID + ".zip";
//        String zipPath = "D:\\" + folderID + ".zip";
        System.out.println(zipPath);
        FileOutputStream fos1 = new FileOutputStream(zipPath);
        toZip(path, fos1, true);
        long end = System.currentTimeMillis();
        System.out.println("打包完成，耗时：" + (end - start) +" ms");
//        return ResultFactory.buildSuccessResult("success",null);
        File zip = new File(zipPath);
        String zipURl = "222.192.6.62:8082\\download\\" + folderID + ".zip";
        if (zip.exists()){
            return ResultFactory.buildSuccessResult("success",zipURl);
        }
        else {
            return ResultFactory.buildFailResult("下载失败");
        }
    }
    //获取收藏的文本
    public ArrayList<String> getStr(String folderID){
        Driver driver = createDrive();
        Session session = driver.session();
        ArrayList<String> text = new ArrayList<>();
        ArrayList<Map> contentList = favoriteMapper.collectionStr(folderID);
        text.add("知识点");
        for(Map contentMap:contentList){
            text.add((String) contentMap.get("content"));
        }
        ArrayList<Map> keyList = favoriteMapper.key(folderID);
        text.add(" ");
        text.add("学习重难点");
        if (keyList.size()>0){
            for (Map singleKey:keyList){
                if (singleKey!=null){
                    int id = (int) singleKey.get("key");
                    StatementResult node = session.run( "MATCH (n:GoalAndKey) where n.id = {id} " +
                                    "RETURN n.key as key",
                            parameters( "id", id) );
                    while ( node.hasNext() )
                    {
                        Record record = node.next();
                        String keyText = record.get( "key" ).asString();
                        text.add(keyText);
                    }
                }

            }
        }
        ArrayList<Map> goalList = favoriteMapper.goal(folderID);
        text.add(" ");
        text.add("学习目标");
        if (keyList.size()>0){
            for (Map singleGoal:goalList){
                if (singleGoal!=null){
                    int id = (int) singleGoal.get("goal");
                    StatementResult node = session.run( "MATCH (n:GoalAndKey) where n.id = {id} " +
                                    "RETURN n.goal as goal",
                            parameters( "id", id) );
                    while ( node.hasNext() )
                    {
                        Record record = node.next();
                        String goalText = record.get( "goal" ).asString();
                        text.add(goalText);
                    }
                }

            }
        }
        return text;
    }
    public static void createWord(String path, String fileName) {
        //判断目录是否存在
        File file = new File(path);
        //exists()测试此抽象路径名表示的文件或目录是否存在。
        //mkdir()创建此抽象路径名指定的目录。
        //mkdirs()创建此抽象路径名指定的目录，包括所有必需但不存在的父目录。
        if (!file.exists()) {
            file.mkdirs();
        }
        //因为HWPFDocument并没有提供公共的构造方法 所以没有办法构造word
        //这里使用word2007及以上的XWPFDocument来进行构造word
        @SuppressWarnings("resource")
        XWPFDocument document = new XWPFDocument();
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(file, fileName));
            document.write(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) ;
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //向word中写入数据
    /**
     * 有些方法需要传特殊类型的参数的时候，一般可以用★静态的接口.参数★来传参
     *
     * @param path
     * @param data
     */
    public static void writeDataDocx(String path, ArrayList<String> data, boolean jiacu, int size) {
        InputStream istream = null;
        OutputStream ostream = null;
        try {
            istream = new FileInputStream(path);
            ostream = new FileOutputStream(path);
            @SuppressWarnings("resource")
            XWPFDocument document = new XWPFDocument();
            Iterator textList = data.iterator();
            while(textList.hasNext()){
                String text = (String) textList.next();
                //添加一个段落
                XWPFParagraph p = document.createParagraph();
                XWPFRun r = p.createRun();//p1.createRun()将一个新运行追加到这一段
                r.setText(text);
            }
            //添加一个段落
            //XWPFParagraph p1 = document.createParagraph();
            //setAlignment()指定应适用于此段落中的文本的段落对齐方式。CENTER LEFT...
            //p1.setAlignment(ParagraphAlignment.LEFT);
            //p1.setBorderBetween(Borders.APPLES);
            //p1.setBorderBottom(Borders.APPLES);
            //p1.setBorderLeft(Borders.APPLES);指定应显示在左边页面指定段周围的边界。
            //p1.setBorderRight(Borders.ARCHED_SCALLOPS);指定应显示在右侧的页面指定段周围的边界。
            //p1.setBorderTop(Borders.ARCHED_SCALLOPS);指定应显示上方一组有相同的一组段边界设置的段落的边界。这几个是对段落之间的格式的统一，相当于格式刷
            //p1.setFirstLineIndent(99);//---正文宽度会稍微变窄
            //p1.setFontAlignment(1);//---段落的对齐方式 1左 2中 3右 4往上 左 不可写0和负数
            //p1.setIndentationFirstLine(800);//---首行缩进,指定额外的缩进，应适用于父段的第一行。
            //p1.setIndentationHanging(400);//---首行前进,指定的缩进量，应通过第一行回到开始的文本流的方向上移动缩进从父段的第一行中删除。
            //p1.setIndentationLeft(400);//---整段缩进（右移）指定应为从左到右段，该段的内容的左边的缘和这一段文字左边的距和右边文本边距和左段权中的那段文本的右边缘之间的缩进,如果省略此属性，则应假定其值为零。
            //p1.setIndentationRight(400);//---指定应放置这一段，该段的内容从左到右段的右边缘的正确文本边距和右边文本边距和左段权中的那段文本的右边缘之间的缩进,如果省略此属性，则应假定其值为零。
            //p1.setIndentFromLeft(400);//---整段右移
            //p1.setIndentFromRight(400);
            //p1.setNumID(BigInteger.TEN);
            //p1.setPageBreak(true);//--指定当渲染此分页视图中的文档，这一段的内容都呈现在文档中的新页的开始。
            //p1.setSpacingAfter(6);//--指定应添加在文档中绝对单位这一段的最后一行之后的间距。
            //p1.setSpacingAfterLines(6);//--指定应添加在此线单位在文档中的段落的最后一行之后的间距。
            //p1.setSpacingBefore(6);//--指定应添加上面这一段文档中绝对单位中的第一行的间距。
            //p1.setSpacingBeforeLines(6);//--指定应添加在此线单位在文档中的段落的第一行之前的间距。
            //p1.setSpacingLineRule(LineSpacingRule.AT_LEAST);//--指定行之间的间距如何计算存储在行属性中。
            //p1.setStyle("");//--此方法提供了样式的段落，这非常有用.
            //p1.setVerticalAlignment(TextAlignment.CENTER);//---指定的文本的垂直对齐方式将应用于此段落中的文本
            //p1.setWordWrapped(true);//--此元素指定是否消费者应中断超过一行的文本范围，通过打破这个词 （打破人物等级） 的两行或通过移动到下一行 （在词汇层面上打破） 这个词的拉丁文字。
            //XWPFRun r1 = p1.createRun();//p1.createRun()将一个新运行追加到这一段
            //setText(String value)或
            //setText(String value,int pos)
            //value - the literal text which shall be displayed in the document
            //pos - - position in the text array (NB: 0 based)
            //r1.setText(data);
            //r1.setTextPosition(20);//这个相当于设置行间距的，具体这个20是怎么算的，不清楚,此元素指定文本应为此运行在关系到周围非定位文本的默认基线升降的量。不是真正意义上的行间距
            //---This element specifies the amount by which text shall be ★raised or lowered★ for this run in relation to the default baseline of the surrounding non-positioned text.
            //r1.setStrike(true);//---设置删除线的,坑人!!!
            //r1.setStrikeThrough(true);---也是设置删除线，可能有细微的区别吧
            //r1.setEmbossed(true);---变的有重影（变黑了一点）
            //r1.setDoubleStrikethrough(true);---设置双删除线
            //r1.setColor("33CC00");//---设置字体颜色 ★
            //r1.setFontFamily("fantasy");
            //r1.setFontFamily("cursive");//---设置ASCII(0 - 127)字体样式
            //r1.setBold(jiacu);//---"加黑加粗"
            //r1.setFontSize(size);//---字体大小

//            XWPFParagraph p2 = document.createParagraph();
//            XWPFRun p2Run = p2.createRun();
//            p2Run.setText("200. 你好");
            //p2Run.setFontSize(size);//---字体大小
            //p2.setIndentationFirstLine(800);//---首行缩进,指定额外的缩进，应适用于父段的第一行。

            //r1.setImprinted(true);//感觉与setEmbossed(true)类似，有重影
            //r1.setItalic(true);//---文本会有倾斜，是一种字体？
            //r1.setShadow(true);//---文本会变粗有重影，与前面两个有重影效果的方法感觉没什么区别
            //r1.setSmallCaps(true);//---改变了  英文字母  的格式
            //r1.setSubscript(VerticalAlign.BASELINE);//---valign垂直对齐的
            //r1.setUnderline(UnderlinePatterns.DASH);//--填underline type设置下划线
            //document.createTable(2, 2);//--创建一个制定行列的表
            //document.enforceReadonlyProtection();//--强制执行制度保护

            /**
             * r1.setDocumentbackground(doc, "FDE9D9");//设置页面背景色
             r1.testSetUnderLineStyle(doc);//设置下划线样式以及突出显示文本
             r1.addNewPage(doc, BreakType.PAGE);
             r1.testSetShdStyle(doc);//设置文字底纹
             */
            document.write(ostream);
            System.out.println("创建word成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (istream != null) {
                try {
                    istream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ostream != null) {
                try {
                    ostream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void copyAll(String folderID, String path){
        ArrayList<Resource> collection = favoriteMapper.collection(folderID);
        for (Resource resource:collection){
            int id = resource.getId();
            String url = resourceMapper.queryUrl(id);
//            System.out.println(url);
            copyFile("E:\\TestResource" + url, path);
        }
    }
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }
    /**
     * 压缩成ZIP 方法1
     * @param srcDir 压缩文件夹路径
     * @param out    压缩文件输出流
     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
     *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
        throws RuntimeException{
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) +" ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 递归压缩方法
     * @param sourceFile 源文件
     * @param zos        zip输出流
     * @param name       压缩后的名称
     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
     *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception{
        int  BUFFER_SIZE = 2 * 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }
                }
            }
        }
    }
}
