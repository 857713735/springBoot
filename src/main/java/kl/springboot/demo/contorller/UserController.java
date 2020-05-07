package kl.springboot.demo.contorller;


import kl.springboot.demo.dao.EmployeeDao;
import kl.springboot.demo.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;

@Controller
public class UserController {
    @Autowired
    EmployeeDao employeeDao;

    //查询所有员工信息
    @GetMapping("/UserList.do")
    public String userList(Model model) {
        Collection<Employee> collection = employeeDao.getAll();
        model.addAttribute("UserList", collection);//返回值
        return "list";
    }

    //跳转用户信息添加页面
    @GetMapping("/gotoAddUser.do")
    public String gotoAddUser(Model model) {
        // Collection<Employee> collection = employeeDao.getAll();
        // model.addAttribute("UserList",collection);//返回值
        return "add";
    }

    //跳转用户信息添加页面
    @PostMapping("/insertUser.do")//put//delete
    public String insertUser(Employee employee) {
        employeeDao.save(employee);
        //redirect 重定向  forword  转发
        return "redirect:/list";

    }
}
