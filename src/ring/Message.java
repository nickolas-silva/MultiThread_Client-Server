package ring;

import java.io.Serializable;

public class Message <T> implements Serializable{
    private static final long serialVersionUID = 1L;
    private T msg;

    public Message(T msg) {
        this.msg = msg;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }   
}
