package com.user.user.domain.responseMessage;

public class ResponseMessage<T> {
    private String clientMsg;
    private String serverMsg;
    private T data;

    public ResponseMessage(String clientMsg, String serverMsg, T data) {
        this.clientMsg = clientMsg;
        this.serverMsg = serverMsg;
        this.data = data;
    }

    public ResponseMessage(String clientMsg) {
        this.clientMsg = clientMsg;
    }

    public ResponseMessage(String clientMsg, String serverMsg) {
        this.clientMsg = clientMsg;
        this.serverMsg = serverMsg;
    }

    public ResponseMessage(String clientMsg, T data) {
        this.clientMsg = clientMsg;
        this.data = data;
    }

    public ResponseMessage(T data) {
        this.data = data;
    }

    public String getClientMsg() {
        return clientMsg;
    }

    public void setClientMsg(String clientMsg) {
        this.clientMsg = clientMsg;
    }

    public String getServerMsg() {
        return serverMsg;
    }

    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "clientMsg='" + clientMsg + '\'' +
                ", serverMsg='" + serverMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
