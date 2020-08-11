package com.dcjt.dcjtim.bean;

/**
 * Created by 唐文滔 on 2020/6/2
 */
public enum MsgType {
    /**
     * 消息类型
     */
    ONMSG(1, "文本和emoji表情"),
    ONIMAGE(2, "图片"),
    ONFILE(3, "文件"),
    ONMSGFILE(4,"文本和文件"),
    ONMSGIMAGE(5,"文本和图片"),
    JOINROOM(9,"加入到组");

    public int code;
    public String msgNote;

    MsgType(int code, String msgNote) {
        this.code = code;
        this.msgNote = msgNote;
    }
}
