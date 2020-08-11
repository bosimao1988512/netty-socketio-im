package com.dcjt.dcjtim.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.dcjt.dcjtim.bean.ChatProtocol;
import com.dcjt.dcjtim.bean.ImConstants;
import com.dcjt.dcjtim.entity.ChatRoom;
import com.dcjt.dcjtim.entity.ChatSole;
import com.dcjt.dcjtim.event.ChatEvent;
import com.dcjt.dcjtim.service.IChatSoleService;
import com.dcjt.dcjtim.socket.session.SessionMap;
import com.dcjt.dcjtim.socket.session.TransmitAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * IM处理
 */
@Slf4j
@Component
public class ChatEventListerner implements ApplicationListener<ChatEvent> {

    @Autowired
    IChatSoleService soleService;
    @Autowired
    SocketIOServer socketIOServer;

    @Async
    @Override
    public void onApplicationEvent(ChatEvent event) {
        ChatProtocol chat = (ChatProtocol) event.getSource();
        switch (event.getType()) {
            case ImConstants.EVENT_CHAT:
                TransmitAction.sole(chat);
                break;
            case ImConstants.EVENT_ROOM_CHAT:
                TransmitAction.room(chat);
                break;
            default:
                break;
        }
    }
}
