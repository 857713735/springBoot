package kl.springboot.demo.contorller;


import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import kl.springboot.demo.utils.CommonUtil;
import org.apache.commons.io.FileUtils;
import kl.springboot.demo.utils.DataDefUtils;
import kl.springboot.demo.utils.FileUtil;
import kl.springboot.demo.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

@Controller
public class FileUploadController {
    //声明日志
    private Log log = LogFactory.getLog(FileUploadController.class);

    /**
     * 上传单个文件
     */
    @RequestMapping("/uploadFile.do")
    @ResponseBody
    public JsonUtil uploadSingleFile(@RequestParam CommonsMultipartFile file, @RequestParam String path, @RequestParam(required=false) String oldFile, @RequestParam String wjlx) throws Exception {//HttpServletRequest request
        /**
         * 这里提到两种接收参数的方式：需要注意一点区别 下面的上传多个文件也是同理
         * 直接用 @RequestParam("fileUpName") MultipartFile file
         *      接收文件参数 如果前端没有选择文件 请求后台报错，Current request is not a multipart request
         *      前端显示的是500错误
         * 替换为  HttpServletRequest request
         *        MultipartFile file = ((MultipartHttpServletRequest)request).getFile("fileUpName"); 获取文件参数
         *        如果没有选择文件 后台报错 cannot be cast to org.springframework.web.multipart.MultipartHttpServletRequest
         *        不过方便在该行代码做异常处理等返回设置对应的code msg等统一形式。
         */
        /**参考
         * $.ajax({
         * 	        url: rootPath + '/basic/fileUpload/uploadFile.do',
         * 	        type: 'POST',
         * 	        async: false,
         * 	        data: formData,
         * 	        dataType : 'json',
         * 	        success: function(result){
         * 	        	if(result.status == 200){
         * 	        		$.drawEasyui.alert("info",result.message);
         * 	        		$("#"+oldFile).val('/resources/file/xfjm/'+result.data);
         *                                }else{
         * 	        		$.drawEasyui.alert("info","文件上传失败！");
         *                }* 	        },
         * 	        error:function(a,b,c){
         * 	        	alert(c);* 	        },
         * 	        cache: false,
         * 	        contentType: false,
         * 	        processData: false
         *                });
         */
        try {
            //MultipartFile file = ((MultipartHttpServletRequest)request).getFile("fileUpName");
            if (file.getSize() == 0) {
                return new JsonUtil(DataDefUtils.FAILED_STATUS, "文件内容为空，请重新选择文件！");
            }
            //获取文件名称
            String fileName = file.getOriginalFilename();
            //判断文件名中是否包含”\\”
            if (fileName.contains("\\")) {
                //取”\\”之后的内容作为文件名
                fileName = StringUtils.substringAfterLast(fileName, "\\");
            }
            String realPath = FileUtil.buildFilePath(path);
            File dir = new File(realPath + File.separator + oldFile);
            //如果存在删除
            if (dir.exists()) {
                if (dir.isFile()) {
                    dir.delete();
                }
            }

            File upfile = null;
            if(wjlx=="0"){//不带参数
                upfile = FileUtil.uploadFileCustomFileName2(path, file);
            }else{
                upfile = FileUtil.uploadFileCustomFileName1(path, "连接符", file);
            }
            String newFileName = upfile.getName();
            return new JsonUtil(DataDefUtils.SUCCESS_CODE, "上传成功！", newFileName);
        }catch (Exception e) {
            log.error(e);
            return new JsonUtil(DataDefUtils.FAILED_STATUS,"上传失败！");

        }

    }

    /**
     * 下载文件
     */
    @RequestMapping("/download.do")
    public String download(@RequestParam("path") String path,@RequestParam("fileName") String fileName, HttpServletResponse response){
        try {
            //文件存储目录
            String realPath = FileUtil.buildFilePath(path);
            File file = new File(realPath,fileName);
            //若果文件不存在
            if(!file.exists()){
                String errorMessage = "对不起！下载文件在服务器不存在！";
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
                outputStream.close();
            }else{
                //防止中文名称乱码
//				fileName=new String(fileName.getBytes("UTF-8"),"iso-8859-1");
                // 设置强制下载不打开
                response.setContentType("application/force-download");
                //设置下载文件名
                response.addHeader("Content-Disposition","attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
                //读取文件
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                //下载文件
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }
        } catch (UnsupportedEncodingException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }

    /**
     * zip导入并解压   kl  2020/05/12
     * @param path
     * @param oldFile
     * @param encode
     * @param request
     * @return
     */
    @RequestMapping("/importZip.do")
    @ResponseBody
    public JsonUtil importZip(@RequestParam(required=false) String path,@RequestParam(required=false) String oldFile,String encode,HttpServletRequest request){
        try {
            //判断内容类型
            if(request.getHeader("content-type")!=null&&"application/x-www-form-urlencoded".equals(request.getHeader("content-type"))){
                return new JsonUtil(DataDefUtils.FAILED_STATUS,"不支持断点续传！"); //不支持断点续传，直接返回null即可
            }
            //判断是否传入上传路径，如果未存在指定默认路径
            if(StringUtils.isBlank(path)){
                path="/resources/file";
            }
            MultipartHttpServletRequest mRequest=(MultipartHttpServletRequest)request;
            //获取上传的文件列表
            Iterator<String> fns=mRequest.getFileNames();
            int allnum = 0;
            while(fns.hasNext()){
                //获取下一个文件名称
                String fileName =fns.next();
                //根据文件名获取文件
                MultipartFile mFile = mRequest.getFile(fileName);
                //如果文件存在开始解压
                if(!mFile.isEmpty()){
                    //解压目录
                    String realPath = FileUtil.buildFilePath(path);
                    File dir = new File(realPath);//dir.list().length
                    //路径不存在，自动创建
                    if(!dir.exists()){
                        dir.mkdirs();
                    }
                    //获取上传的文件名称
                    String originFileName=mFile.getOriginalFilename();
                    //根据文件名称获取文件
                    File file = new File(realPath,originFileName);
                    try {
                        //存储文件
                        FileUtils.copyInputStreamToFile(mFile.getInputStream(),file);
                        //压缩包文件路径
                        String fileLocation = file.getAbsolutePath();
                        String descFolder = fileLocation.substring(0,fileLocation.lastIndexOf("."));
                        //删除之前解压的文件,此处未考虑并发上传同一文件的问题，后续解决
                        File descFolderFile  = new File(descFolder);
                        if(descFolderFile.exists()){
                            descFolderFile.delete();
                        }
                        //解压文件
                        if(null!=file){
                            allnum=FileUtil.unzipFileCount(fileLocation, realPath,encode,"");
                        }
                        //解压完成后删除压缩包
                        file.delete();
                        //allnum
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            return new JsonUtil(DataDefUtils.SUCCESS_CODE,"导入成功,共导入:"+(allnum)+"条数据!");
        }catch(Exception e){
            e.printStackTrace();
            return new JsonUtil(DataDefUtils.SUCCESS_CODE,"导入成功,共导入:0条数据!");
        }
    }

    /**
     * 导出   方法/模板   通用
     * @param request
     * @param response
     */
    @RequestMapping("/exportFileExecl.do")
    @ResponseBody
    public void exportFileExecl(HttpServletRequest request,HttpServletResponse response){
        try {
            Map<String,Object> params = CommonUtil.getParameterMap(request);
            //获得输出流，该输出流的输出介质是客户端浏览器
            OutputStream output = response.getOutputStream();
            response.reset();
            String fileName=params.get("filename")+".xls";
            String rang=(String) params.get("rang");
            response.setHeader("Content-disposition","attachment; filename="+ new String(fileName.getBytes("GBK"),"iso-8859-1") );
//      	    response.setHeader("Content-disposition","attachment; filename="+ new String(fileName.getBytes("GBK"),"iso-8859-1") );
            response.setContentType("application/octet-stream");
            //创建可写入的Excel工作薄，且内容将写入到输出流，并通过输出流输出给客户端浏览
            WritableWorkbook wk= Workbook.createWorkbook(output);
            //创建可写入的Excel工作表
            String fileSheet =params.get("fileSheet").toString();
            WritableSheet sheet = wk.createSheet(fileSheet, 0);
            //设置列宽
            sheet.getSettings().setDefaultColumnWidth(20);
            WritableCellFormat cloumnTitleFormat=new WritableCellFormat();
            //设置字体
            cloumnTitleFormat.setFont(new WritableFont(WritableFont.createFont("宋体"),14,WritableFont.BOLD,false, UnderlineStyle.NO_UNDERLINE));
            //设置居中
            cloumnTitleFormat.setAlignment(Alignment.CENTRE);
            //设置背景色
            cloumnTitleFormat.setBackground(Colour.GRAY_25);
            //设置边框
            cloumnTitleFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.MEDIUM);

            //2019/06/15 LC ADD
            //内容行格式
            WritableCellFormat cloumnFormat=new WritableCellFormat();
            //设置字体
            cloumnFormat.setFont(new WritableFont(WritableFont.createFont("宋体"),12,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE));
            //设置居中
            cloumnFormat.setAlignment(Alignment.LEFT);
            //设置边框
            cloumnFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
            //自动换行
            cloumnFormat.setWrap(true);

            //查询表头数据
            //boolean all = true;
            //boolean range = false;
           // String pagination = iBasicService.queryListNoUser(params,null,all,range, null, param.getOrderStr());
           // JSONArray list1 = JSONArray.parseArray(pagination);
            int hrow = 0;
            int rows1=1;
            //构建表头
            //如果第一行不显示
            if(StringUtils.isBlank(params.get("titleFlg")== null ? null:params.get("titleFlg").toString())){
                //此处构建你要导出的数据
               /** for(int i=0;i<list1.size();i++){
                    JSONObject jsonObj = JSONObject.parseObject(list1.get(i).toString());
                    sheet.addCell(new Label(i,hrow,jsonObj.get("COMMENTS")== null?"":jsonObj.get("COMMENTS").toString(),cloumnTitleFormat));
                }**/
            }else{
                hrow = 1;
                rows1 = 2;
                WritableCellFormat formatTitle=new WritableCellFormat();
                formatTitle.setFont(new WritableFont(WritableFont.createFont("宋体"),16,WritableFont.BOLD,false));
                formatTitle.setAlignment(Alignment.CENTRE);
                formatTitle.setBorder(jxl.format.Border.ALL, BorderLineStyle.MEDIUM);
               /** for(int i=0;i<list1.size();i++){
                    JSONObject jsonObj = JSONObject.parseObject(list1.get(i).toString());
                    sheet.addCell(new Label(i,hrow,jsonObj.get("COMMENTS")== null?"":jsonObj.get("COMMENTS").toString(),cloumnTitleFormat));
                    if(i == 0){
                        //显示你的第一行
                        sheet.addCell(new Label(0,0,params.get("titleFlg").toString(),formatTitle));
                    }else{
                        sheet.addCell(new Label(i,0,null,formatTitle));
                    }
                }
                sheet.getSettings().setDefaultRowHeight(300);
                sheet.mergeCells(0, 0, list1.size()-1, 0);**/
            }

            String sqlidValue = params.get("sqlidValue").toString();
            //如果数据查询sql存在执行
            if(sqlidValue!=""){
                /**SysUsers user1 = (SysUsers)ShiroUtils.getToken();
                params.put("sqlid", sqlidValue);
                if("true".equals(rang)){
                    range=true;
                }
                String wws=iBasicService.queryList(params,sp,false,range, user1, null);
                JSONArray json = JSONArray.parseArray(wws);

                for(int i=0;i<json.size();i++){
                    JSONObject jsonObjValue = JSONObject.parseObject(json.get(i).toString());
                    for (int j = 0; j < list1.size(); j++) {
                        JSONObject jsonObj = JSONObject.parseObject(list1.get(j).toString());
                        String values = (String) jsonObjValue.get(jsonObj.get("COLUMN_NAME"));
                        sheet.addCell(new Label(j,rows1,values== null?"":values,cloumnFormat));
                    }
                    rows1++;
                }**/
            }
            wk.write();
            //操作完成时，关闭对象，释放占用的内存空间
            wk.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/ImportCommon.do")
    @ResponseBody
    public String ImportCommon(@RequestParam(value = "file", required = false)MultipartFile file,HttpServletRequest request) throws IOException, BiffException {
        try {
            //获取传入参数
            Map<String,Object> params1 = CommonUtil.getParameterMap(request);
            //查询表头sql
            String selectSql = params1.get("selectSql").toString();
            //删除临时表sql
            String deleteSql = params1.get("deleteSql").toString();
            //添加sql
            String insertSql = params1.get("insertSql").toString();
            //其他列名称
            String columnName=(params1.get("columnName")==null?"":params1.get("columnName").toString());
            //其他列对应数据
            String columnValue=(params1.get("columnValue")==null?"":params1.get("columnValue").toString());
            // 导入已存在的Excel文件，获得只读的工作薄对象
            Workbook wk = Workbook.getWorkbook(file.getInputStream());
            // 获取第'张Sheet表
            Sheet sheet = wk.getSheet(0);
            // 获取总行数
            int rowNum = sheet.getRows();
            int count = (rowNum % 100)== 0 ?(rowNum/100) : (rowNum/100)+1  ;
            //查询导出时的表头
           /** PaginationParam param=new PaginationParam();
            SearchParam sp=new SearchParam();
            Map<String,Object> params = new HashMap<>();
            params.put("sqlid", selectSql);
            boolean all = true;
            boolean range = false;
            String pagination = iBasicService.queryListNoUser(params,sp,all,range, null, param.getOrderStr());
            JSONArray myJsonArray = JSONArray.parseArray(pagination);
            **/
            //保存所有字段信息
            int bsf=0;//标识符
           /** for(int i=0;i<myJsonArray.size();i++){
                //判断表头是否和导出时相同
                if(!myJsonArray.getJSONObject(i).get("COMMENTS").toString().equals(sheet.getCell(i,0).getContents())){
                    bsf++;
                }
            }**/
            if(bsf>0){//表头不符
                return "导入表头与模板不符,请按照正确格式填写!";
            }else{
                /**SysUsers user = (SysUsers)ShiroUtils.getToken();
                String companyid = user.getCompanyid();
                String zrr = user.getUsername();
                String userid = user.getUserid();
                if(!StringUtils.isBlank(deleteSql)){//若删除sql不为空
                    //删除任务临时表数据
                    Map<String,Object> scmap= new HashMap<>();
                    scmap.put("userid", userid);
                    scmap.put("sqlid", deleteSql);
                    iBasicService.insertObject(scmap);
                }**/
                // 从数据行开始迭代每一行
                for (int w = 0; w < count; w++) {
                   // List<ParamVo> list=new ArrayList<ParamVo>();
                    //分批次将数据提交
                    int num = (w+1)==count ? rowNum-(w*100)-1:100;
                    for (int j = 1; j <= num; j++) {
                       /** Map<String,Object> map= new HashMap<>();
                        if(StringUtils.isBlank(sheet.getCell(1, j+(w*100)).getContents())||StringUtils.isBlank(sheet.getCell(0, j+(w*100)).getContents())){
                            rowNum--;
                            continue;
                        }
                        for(int y=0;y<myJsonArray.size();y++){
                            map.put(myJsonArray.getJSONObject(y).get("COLUMN_NAME").toString().toLowerCase(), sheet.getCell(0+y, j+(w*100)).getContents());
                        }
                        map.put("companyid", companyid);
                        map.put("creatorid", userid);
                        map.put("sqlid", insertSql);
                        //其余列不为空时将其加载inset参数中
                        if(!StringUtils.isBlank(columnName)){
                            String[] newColumnName = columnName.split("&");
                            String[] newColumnValue = columnValue.split("&");
                            for(int l=0;l<newColumnName.length;l++){
                                map.put(newColumnName[l], newColumnValue[l]);
                            }
                        }
                        ParamVo paramtj = new ParamVo();
                        paramtj.setParam(map);
                        list.add(paramtj);**/
                    }
                    //iBasicService.insertObjectBatch(list);
                }
            }
            wk.close();
            return "导入成功,共导入:"+(rowNum-1)+"条数据!";
        } catch (Exception e) {
            return "导入失败,请检查数据!";
        }
    }
}
