package kl.springboot.demo.utils;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {

	/**
	 * 获取uuid
	 * @return
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}
	/**
	* 获取指定长度随机数
	* @param len
	* @return
	* @author XZS
	*/
	public static String getRandom(int len){
		Random r = new Random();
		int bitNum=1;
		for(int i=0;i<len;i++){
			bitNum=bitNum*10;
		}
        long num = Math.abs(r.nextLong() % (bitNum));
        String s = String.valueOf(num);
		for (int i = len - s.length(); i >0 ;i--) {
			s = "0" + s;
		}
		if(s.length()>len){
			s=s.substring(0,len);
        }
        return s;
	}
	/**
	 * 获取config文件数据
	 * @return
	 */
	public static String getPropertiesValue(String name) {
    	//读取配置文件配置项   
		ResourceBundle resource = ResourceBundle.getBundle("config/config");//文件路径
		return resource.getString(name);
	}
	

	
	/**
	 * 取的参数map
	 * @param request
	 * @return
	 */
	public static Map<String,Object> getParameterMap(HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Map<String,String[]> paramMap = request.getParameterMap();
		Set<Map.Entry<String, String[]>> entrys = paramMap.entrySet();
		if(null!=paramMap){
			for (Map.Entry<String, String[]> entry: entrys) {
				String key = entry.getKey();
				String[] value = entry.getValue();
				if(StringUtils.isNotBlank(key) && null!=value && value.length>0){
//					if(isDate(value[0])){
//						try {
//							resultMap.put(key,DateUtils.parseDate(value[0], "yyyy/MM/dd HH:mm:ss","yyyy/MM/dd"));
//						} catch (ParseException e) {
//							resultMap.put(key,value[0]);
//						}
//					}else{
						resultMap.put(key,value[0]);
//					}
				}
			}
		}
		return resultMap;
	}
	
	/** 取的参数map<String,String>类型
	 * @param request
	 * @return
	 */
	public static Map<String,String> getParameterStrMap(HttpServletRequest request){
		Map<String,String> resultMap = new HashMap<String,String>();
		Map<String,String[]> paramMap = request.getParameterMap();
		Set<Map.Entry<String, String[]>> entrys = paramMap.entrySet();
		if(null!=paramMap){
			for (Map.Entry<String, String[]> entry: entrys) {
				String key = entry.getKey();
				String[] value = entry.getValue();
				if(StringUtils.isNotBlank(key) && null!=value && value.length>0){
					resultMap.put(key,value[0]);
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * 判定传入的字串是否为日期格式
	 * @param dateStr
	 * @return
	 */
	public static boolean isDate(String dateStr){
		//定义日期格式正则表达式,仅判定格式是否正确，不负责有效性验证
		try {
			String patternstr = "^(\\d{4}\\/\\d{2}\\/\\d{2}){1}( \\d{2}:{1}\\d{2}:{1}\\d{2})?$";
			Pattern pattern = Pattern.compile(patternstr);
			Matcher matcher = pattern.matcher(dateStr);
			return matcher.matches();
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * list去重
	 * @param list
	 * @return
	 */
	public static List removeDuplicate(List list) {   
	    HashSet h = new HashSet(list);   
	    list.clear();   
	    list.addAll(h);   
	    return list;   
	}
	
	/**
	 *
	 * 获取文件名
	 * @param original
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	  public static String getFileName(String original) throws UnsupportedEncodingException {
			
			//根据request的locale 得出可能的编码，中文操作系统通常是gb2312  
			String	fileName = new String(original.getBytes("gb2312"), "ISO8859-1");  
		
		return fileName; 
  }
	/**
	   * 根据数量返回指定空字符串符
	 * @param blankNum
	 * @return
	 * @throws Exception
	 */
	public static String getBlankStr(int blankNum) throws Exception {
			String	blank = "";  
			for (int i = 0; i < blankNum; i++) {
				blank += " ";
			}
			return blank; 
	}  

	public static void main(String[] args) {
		try {
			System.out.println(getBlankStr(10));
			System.out.println(5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	  
	  
}
