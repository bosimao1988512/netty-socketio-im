package com.dcjt.dcjtim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 滔哥
 * @since 2020-07-30
 */
public class ChatSole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 源端
     */
    private String source;

    /**
     * 目标端
     */
    private String target;

    /**
     * 消息类别
     */
    private Boolean msgType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 文件名
     */
    private String materialName;

    /**
     * 文件路径
     */
    private String materialId;

    /**
     * 发送时间
     */
    private LocalDateTime time;

    /**
     * 发送标识，1：已发送，0：未发送
     */
    private Boolean flag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
    public Boolean getMsgType() {
        return msgType;
    }

    public void setMsgType(Boolean msgType) {
        this.msgType = msgType;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "ChatSole{" +
            "id=" + id +
            ", source=" + source +
            ", target=" + target +
            ", msgType=" + msgType +
            ", content=" + content +
            ", materialName=" + materialName +
            ", materialId=" + materialId +
            ", time=" + time +
            ", flag=" + flag +
        "}";
    }
}
