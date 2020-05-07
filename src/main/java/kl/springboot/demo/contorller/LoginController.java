package kl.springboot.demo.contorller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController {

    //跳转列表并跳转首页
    @PostMapping("/Login.do")
    public String userlist(@RequestParam("username") String username, @RequestParam("password") String password, Map<String, Object> map, HttpSession session) {
        if ("admin".equals(username) && "123456".equals(password)) {
            session.setAttribute("loginName", username);
            return "redirect:/main.html";

        } else {
            map.put("mes", "账号或密码错误!");
            return "index";
        }

    }
}
