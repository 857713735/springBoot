package kl.springboot.demo.contorller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kl.springboot.demo.dao.SysuserMapper;
import kl.springboot.demo.entity.Sysuser;
import kl.springboot.demo.entity.SysuserExample;
import kl.springboot.demo.utils.DataDefUtils;
import kl.springboot.demo.utils.JsonUtil;
import kl.springboot.demo.utils.Pagination;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SysUserController {
    @Autowired
    SysuserMapper sysUserMapper;


    /**
     * 跳转用户列表
     * @return
     */
    @GetMapping("/to_userlist.do")
    public String to_userlist() {
        return "sysUserList";
    }

    private static final GsonBuilder gbuilder = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss");
    //查询所有用户信息
    @RequestMapping("/queryUserList.do")
    @ResponseBody
    public JsonUtil queryUserList( HttpServletRequest request) {
        /**
         * 使用分页查询
         */
        String pagestr=request.getParameter("page");
        int page=1;
        if(StringUtils.isNotBlank(pagestr)){
            page=Integer.parseInt(request.getParameter("page"));
        }
        String sizestr=request.getParameter("size");
        int size=1;
        if(StringUtils.isNotBlank(sizestr)){
            size=Integer.parseInt(request.getParameter("size"));
        }
        PageHelper.startPage(page, size);
        SysuserExample example=new SysuserExample();
        example.setOrderByClause("loginname asc");//根据loginname排序
        List<Sysuser> collection = sysUserMapper.selectByExample(example);

        Gson gson = gbuilder.create();
        String pagination= gson.toJson(collection);

        return new JsonUtil(DataDefUtils.SUCCESS_CODE,DataDefUtils.SUCCESS_MESSAGE,new Pagination(collection.size(),collection));
    }
    /**
     * 跳转至个人中心
     */
    @GetMapping("/personal.do")
    public String personal() {
        return "personal";
    }
    /**
     * 跳转至用户添加页面
     */
    @GetMapping("/to_adduser.do")
    public String to_adduser() {
        return "addUsers";
    }
}
