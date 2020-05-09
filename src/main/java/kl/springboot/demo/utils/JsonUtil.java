package kl.springboot.demo.utils;

import java.io.Serializable;
import com.alibaba.fastjson.JSON;
public class JsonUtil implements Serializable {
    private static final long serialVersionUID = 3850015655927422933L;
    private String status;
    private String message;
    private Object rows;
    private int total;

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

    public JsonUtil(String status, String message, Object rows) {
        this.status = status;
        this.message = message;
        setRows(rows);
        this.total=50;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        try {
            if(rows instanceof String){
                this.rows=JSON.parse((String)rows);
            }else{
                this.rows = rows;
            }
        } catch (Exception e) {
            this.rows = rows;

        }
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
