package com.bryan.calladv.core;

/**
 * Author：Cxb on 2016/3/21 17:49
 */
public class SMSBean {
    private String thread_id; // 对话的序号，如100，与同一个手机号互发的短信，其序号是相同的
    private String msg_count; // 消息个数
    private String msg_snippet; // 消息片段
    private String address; // 发件人地址，即手机号，如+8613811810000
    private Long date; // 日期
    private String read; // 是否阅读0未读，1已读
    private int type;  //短信类型1是接收到的，2是已发出 　
    private String person;  //发件人，如果发件人在通讯录中则为具体姓名，陌生人为null 　

    public SMSBean(String threadId, String msgCount, String msgSnippet) {
        thread_id = threadId;
        msg_count = msgCount;
        msg_snippet = msgSnippet;
    }

    public SMSBean() {
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getMsg_count() {
        return msg_count;
    }

    public void setMsg_count(String msg_count) {
        this.msg_count = msg_count;
    }

    public String getMsg_snippet() {
        return msg_snippet;
    }

    public void setMsg_snippet(String msg_snippet) {
        this.msg_snippet = msg_snippet;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
}
