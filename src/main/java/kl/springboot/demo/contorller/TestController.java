package kl.springboot.demo.contorller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;
import java.util.Map;


@Controller
public class TestController {
    //日志记录器
    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/hello")
    @ResponseBody
    public void LogCs() {
        //级别由低到高
        logger.trace("trace");//跟踪轨迹
        logger.debug("de");//调试信息
        logger.info("info");//自定义的消息
        logger.warn("jingg");//警告信息
        logger.error("cuowu");//错误信息
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @ResponseBody
    @GetMapping("query")
    public Map<String, Object> querylist() {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from cmn_users");
        return maps.get(0);

    }

}
