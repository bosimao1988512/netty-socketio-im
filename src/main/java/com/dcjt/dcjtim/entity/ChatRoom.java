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
public class ChatRoom implements Serializable {

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
    private Integer msgType;

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
    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
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

    @Override
    public String toString() {
        return "ChatRoom{" +
            "id=" + id +
            ", source=" + source +
            ", target=" + target +
            ", msgType=" + msgType +
            ", content=" + content +
            ", materialName=" + materialName +
            ", materialId=" + materialId +
            ", time=" + time +
        "}";
    }
}
