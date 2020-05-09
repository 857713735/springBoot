package kl.springboot.demo.utils;

/**
 * 系统提示信息初始化
 * @author Administrator
 *
 */
public class DataDefUtils {
	//状态判定
	public static final Short PD_YES = 1;
	public static final Short PD_NO = 0;
	
	//正确代码
	public static final String SUCCESS_CODE="200";
	public static final String SUCCESS_MESSAGE="访问成功";
	//错误代码
	public static final String ACCESS_NORUNASCURUSER="301";
	public static final String ACCESS_NORUNASCURUSER_MESSAGE="不能切换到当前登陆用户!";
	public static final String ACCESS_NOPERMISSION="401";
	public static final String ACCESS_NOPERMISSION_MESSAGE="无权访问";
	public static final String ACCESS_SENCONDLOGIN="402";
	public static final String ACCESS_SENCONDLOGIN_MESSAGE="请进行登陆验证";
	public static final String ACCESS_SERVER_ERROR="500";
	
	//访问方式
	public static final String ANNO_ACCESS="匿名访问";
	public static final String AUTH_NOPERMISSION_ACCESS="未授权访问";
	public static final String AUTH_ACCESS="授权访问";
	
	//日志类型
	public static final String LOGIN_LOG="登陆日志";
	public static final String WARN_LOG="警告日志";
	public static final String ERROR_LOG="错误日志";
	public static final String OPER_LOG="操作日志";
	public static final String LOGOUT_LOG="注销日志";
	
	//访问结果状态
	public static final String SUCCESS_STATUS="<font color='green'>成功</font>";
	public static final String FAILED_STATUS="<font color='green'>失败</font>";
	
	//登录失败类型
	public static final String LOGIN_ERROR_001="账号[%s]不存在!";
	public static final String LOGIN_ERROR_002="账号或密码错误[%s]次，剩余[%s]次将锁定";
	public static final String LOGIN_ERROR_003="账号[%s]已禁用!";
	public static final String LOGIN_ERROR_004="账号[%s]已锁定,请[%s]后登录";
	public static final String LOGIN_ERROR_005="账号[%s]尚未开启!";
	public static final String LOGIN_ERROR_006="账号[%s]已过期!";
	public static final String LOGIN_ERROR_007="账号[%s]密码过期，请重置！";
	public static final String LOGIN_ERROR_008="账号[%s]密码为初始密码，请重置！";
	
}
