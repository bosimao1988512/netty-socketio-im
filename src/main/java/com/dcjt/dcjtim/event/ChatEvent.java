package com.dcjt.dcjtim.event;

import org.springframework.context.ApplicationEvent;

/**
 * 聊天事件
 */
public class ChatEvent extends ApplicationEvent {

    private String type;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ChatEvent(Object source,String type) {
        super(source);
        this.type=type;
    }

    public String getType() {
        return type;
    }
}
