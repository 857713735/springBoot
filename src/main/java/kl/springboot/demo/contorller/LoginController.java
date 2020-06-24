package kl.springboot.demo.contorller;


import kl.springboot.demo.dao.SysuserMapper;
import kl.springboot.demo.entity.Sysuser;
import kl.springboot.demo.entity.SysuserExample;
import kl.springboot.demo.utils.MD5;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    /**
     * 验证登录
     * @param username
     * @param password
     * @param map
     * @param session
     * @return
     */
    @Autowired
    SysuserMapper sysUserMapper;
    @RequestMapping("/Login.do")
    public String userlist(@RequestParam("username") String username, @RequestParam("password") String password, Map<String, Object> map, HttpSession session) throws Exception {

        SysuserExample example=new SysuserExample();
        SysuserExample.Criteria criteria = example.createCriteria();
        /**
         *  criteria如果定义一个的话执行效果为：
         *  select * from xx  where a=x and b=x
         *  如定义多个执行效果为
         *  select * from xx  where a=x or b=x
         */
        /**
         * 示例语法
         * setOrderByClause("age asc");//升序
         * setDistinct(false);//不去重
         */
        //username 与 password不为空
        if(StringUtils.isNotBlank(username)  &&  StringUtils.isNotBlank(password)){
            criteria.andLoginnameEqualTo(username);
            List<Sysuser> usercheck = sysUserMapper.selectByExample(example);
            if(usercheck.size()>0){
                if(MD5.verify(password,usercheck.get(0).getPassword())){
                    session.setAttribute("loginName", username);
                    //  return "index";//首页
                    return "redirect:/index.do";
                }
            }
        }
        map.put("mes", "账号或密码错误!");
        return "login";//登录

    }

    /**
     * 退出系统
     */
    @RequestMapping("/LoginOut.do")
    public String LoginOut( HttpSession session) {
            session.setAttribute("loginName", "");
            return "login";//登录
    }
    /**
     * 跳转首页
     * @return
     */
    @RequestMapping("/index.do")
    public String toindex() {
        return "index";
    }

    /**
     * 通用页面跳转
     * @param request
     * @param map
     * @return
     */
    @RequestMapping("/prePageView.do")
    public String prePageView(HttpServletRequest request, ModelMap map){
        //SysUsers user = (SysUsers) ShiroUtils.getToken();
       // map.put("userid", user.getUserid());
       // map.put("companyid", user.getCompanyid());
       // map.put("companyname", user.getCompanyname());
      //  map.addAllAttributes(CommonUtil.getParameterMap(request));
        String url=request.getParameter("viewpage");
        String viewpage ="";
        if(StringUtils.isNotBlank(url)){
            StringBuffer urlbuf = new StringBuffer(url.replace("\\","/"));
            if(urlbuf.toString().startsWith("/")){
                //删除首字符
                urlbuf.deleteCharAt(0);
            }
            if(urlbuf.toString().endsWith(".html")){
                urlbuf.delete(urlbuf.length()-5,urlbuf.length());
            }
            viewpage= urlbuf.toString();
        }else{
            viewpage= "";
        }
        return viewpage;
    }

}
