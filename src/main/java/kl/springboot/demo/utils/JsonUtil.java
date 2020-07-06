package kl.springboot.demo.utils;

import java.io.Serializable;
import com.alibaba.fastjson.JSON;
public class JsonUtil implements Serializable {
    private static final long serialVersionUID = 3850015655927422933L;
    private String status;
    private String message;
    private Object data;

    public JsonUtil() {
    }

    public JsonUtil(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonUtil(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        setData(data);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        try {
            if(data instanceof String){
                this.data=JSON.parse((String)data);
            }else{
                this.data = data;
            }
        } catch (Exception e) {
            this.data = data;

        }
    }

}
