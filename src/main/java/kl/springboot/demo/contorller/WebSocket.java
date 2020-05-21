package kl.springboot.demo.contorller;


import com.alibaba.fastjson.JSON;
import jdk.internal.instrumentation.Logger;
import kl.springboot.demo.entity.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kl
 * @date 2019/4/25 20:50
 */

@ServerEndpoint("/websocket/{userId}")
@Component
@Slf4j
public class WebSocket {
    //Logger log;
    //声明日志
    private Log log = LogFactory.getLog(FileUploadController.class);
    /**
     * String : 用户ID
     * Session： 回话
     */
    public static Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();

    /**
     * onOpen是当用户发起连接时，会生成一个用户的Session 注意此Session是 javax.websocket.Session;
     * 然后我们用userId作为Key Session作为Vaule存入Map中暂存起来
     *
     * @param userId
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        log.info("====>WebSocketService onOpen:欢迎 " + userId);
        if (sessionMap == null) {
            sessionMap = new ConcurrentHashMap<String, Session>();
        }
        sessionMap.put(userId, session);
    }

    /**
     * onClose 是用户关闭聊天窗时，将用户session移除
     *
     * @param userId
     */
    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        log.info("====>WebSocketService OnClose:再见 " + userId);
        sessionMap.remove(userId);
    }

    /**
     * onMessage 实现聊天功能， message是前端传来的JSON字符串。其中含有MessageVo里的信息。根据vo实现点对点/广播聊天。
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("====>WebSocketService OnMessage:发出消息 " + message);
        MessageVo vo = JSON.parseObject(message, MessageVo.class);
        if (vo.getConsumerId() == null || vo.getConsumerId().isEmpty()) {
            //广播
            toAll(message);
        } else {
            //点对点
            one2one(vo, message);
        }
    }

    /**
     * 当出现异常时候自动调用该方法。
     *
     * @param t
     */
    @OnError
    public void error(Throwable t) {
        t.printStackTrace();
    }

    /**
     * 点对点
     *  session.getAsyncRemote().sendText(message); 即向目标session发送消息。
     *
     */
    private static void one2one(MessageVo vo, String message) {
        Session consumerSession = sessionMap.get(vo.getConsumerId());
        if (consumerSession == null) {
            //log.info("消息消费者不存在");

        } else {
            consumerSession.getAsyncRemote().sendText(message);
        }
    }


    /**
     * 广播
     * 向所有session发送消息
     */
    private static void toAll(String message) {
        for (Session session : sessionMap.values()) {
            session.getAsyncRemote().sendText(message);
            //log.info("sss");
        }

    }
}