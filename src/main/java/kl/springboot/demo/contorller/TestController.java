package kl.springboot.demo.contorller;

import kl.springboot.demo.entity.Sysuser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


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

    //redis
    @Autowired
    private RedisTemplate redisTemplate;
    //查询所有用户信息
    @RequestMapping("/textredis.do")
    @ResponseBody
    public Sysuser queryUserList(HttpServletRequest request) {
        /**
         * 使用分页查询
         */
        ValueOperations<String, Sysuser> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey("username");
        if (hasKey) {
            Sysuser user = operations.get("username");
            System.out.println("==========从缓存中获得数据=========");
            System.out.println(user.getUserName());
            System.out.println("==============================");
            return user;
        } else {
            Sysuser user=new Sysuser();
            user.setUserName("张三");
            System.out.println("==========从数据表中获得数据=========");
            System.out.println(user.getUserName());
            System.out.println("==============================");

            // 写入缓存
            operations.set("username", user, 5, TimeUnit.HOURS);
            System.out.println(redisTemplate.hasKey("username"));
            return user;
        }
    }

}
