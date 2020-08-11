package com.dcjt.dcjtim.bean;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author 滔哥
 * @since 2020/7/27
 */
@Data
public class ChatProtocol {
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
     * 消息类型
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
     * 文件标识
     */
    private String materialId;
    /**
     * 发送时间
     */
    private LocalDateTime time;
    /**
     * 是否是组消息
     */
    private Boolean group;
}
