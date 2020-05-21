package kl.springboot.demo.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;

public class FileUtil {
    private static WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
    /**
     * 构建文件上传路径
     * @param path
     * @return
     */
    public static String buildFilePath(String path){
        if(StringUtils.isBlank(path)){
            path = "/";
        }
        return webApplicationContext.getServletContext().getRealPath(path);
    }
    /**
     * 单文件上传,可自定义文件名
     * @param path
     * @param path
     * @param customFileName
     * @throws Exception
     */
    public static File uploadFileCustomFileName1(String path, String customFileName, CommonsMultipartFile uploadFile) throws Exception{
        //有文件上传，获取文件存储路径
        String realPath = buildFilePath(path);
        //判定存储路径是否存在，不存在则创建
        createDir(realPath);
        if(!uploadFile.isEmpty()){
            //构建目标文件对象
            String fileName = customFileName  + "-" + uploadFile.getOriginalFilename();
            File desFile = new File(realPath+File.separator + fileName);
            //复制文件到目标地址
            uploadFile.transferTo(desFile);
            return desFile;
        }
        return null;
    }
    public static File uploadFileCustomFileName2(String path,  CommonsMultipartFile uploadFile) throws Exception{
        //有文件上传，获取文件存储路径
        String realPath = buildFilePath(path);
        //判定存储路径是否存在，不存在则创建
        createDir(realPath);
        if(!uploadFile.isEmpty()){
            //构建目标文件对象
            String fileName =uploadFile.getOriginalFilename();
            File desFile = new File(realPath+File.separator + fileName);
            //复制文件到目标地址
            uploadFile.transferTo(desFile);
            return desFile;
        }
        return null;
    }
    /**
     * 创建目录
     * @param destDirName
     * @return
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {// 判断目录是否存在
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
            destDirName = destDirName + File.separator;
        }
        if (dir.mkdirs()) {// 创建目标目录
            return true;
        } else {
            return false;
        }
    }
    /**
     * 解压文件
     * @param zipFilezipFilePath
     *   压缩包文件路径
     * @param descDir
     *   解压目录
     * @param password
     * 	 解压密码
     * @return
     */
    public static String unzipFile(String zipFilePath,String descDir,String encode,String password){
        StringBuffer result = new StringBuffer();
        long startTime = System.currentTimeMillis();
        try {
            //创建压缩文件
            ZipFile zipFile = new ZipFile(zipFilePath);
            //设置文件名编码格式
            if(StringUtils.isNotBlank(encode)){
                zipFile.setFileNameCharset(encode);
            }
            //判定zip文件是否存在和是否合法
            if (!zipFile.isValidZipFile()) {
                throw new ZipException("文件不合法或不存在");
            }
            //判定目标路径是否存在
            File file=new File(descDir);
            if (file.isDirectory() && !file.exists()) {
                file.mkdirs();
            }
            //判定是否需要密码
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            //判定是否需要密码
            zipFile.extractAll(descDir);
        } catch (ZipException e) {
            e.printStackTrace();
            result.append(e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        result.append("耗时: ").append(endTime - startTime).append(" ms");
        return result.toString();
    }
    /**
     * 解压压缩包返回数量
     * @param zipFilePath
     * @param descDir
     * @param encode
     * @param password
     * @return
     */

    public static int unzipFileCount(String zipFilePath,String descDir,String encode,String password){
        long startTime = System.currentTimeMillis();
        int count=0;
        try {
            //创建压缩文件
            ZipFile zipFile = new ZipFile(zipFilePath);
            count=zipFile.getFileHeaders().size();

            //设置文件名编码格式
            if(StringUtils.isNotBlank(encode)){
                zipFile.setFileNameCharset(encode);
            }
            //判定zip文件是否存在和是否合法
            if (!zipFile.isValidZipFile()) {
                throw new ZipException("文件不合法或不存在");
            }
            //判定目标路径是否存在
            File file=new File(descDir);
            if (file.isDirectory() && !file.exists()) {
                file.mkdirs();
            }
            //判定是否需要密码
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            //判定是否需要密码
            zipFile.extractAll(descDir);
        } catch (ZipException e) {
            e.printStackTrace();
        }

        return count;
    }
}
