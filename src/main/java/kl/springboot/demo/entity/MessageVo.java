package kl.springboot.demo.entity;



/**
 * @author Dazhi
 * @date 2019/4/25 20:45
 */

public class MessageVo {

    /**
     * 消息生产者ID
     */
    private String producerId;
    /**
     * 消息消费者者ID
     */
    private String consumerId;
    /**
     * 消息
     */
    private String message;

    public String getProducerId() {
        return producerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public String getMessage() {
        return message;
    }

    public String getNickname() {
        return nickname;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 生产者昵称
     */
    private String nickname;
}
