package kl.springboot.demo.contorller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DictionaryController {


    /**
     * 跳转至数据字典
     */
    @GetMapping("/dictionary.do")
    public String dictionary() {
        return "dictionary";
    }
}
