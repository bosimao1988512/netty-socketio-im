package com.dcjt.dcjtim.listener.imlistener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.dcjt.dcjtim.bean.ChatProtocol;
import com.dcjt.dcjtim.bean.ImConstants;
import com.dcjt.dcjtim.entity.ChatRoom;
import com.dcjt.dcjtim.entity.ChatSole;
import com.dcjt.dcjtim.event.ChatEvent;
import com.dcjt.dcjtim.service.IChatRoomService;
import com.dcjt.dcjtim.service.IChatSoleService;
import com.dcjt.dcjtim.socket.session.SessionMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * socket事件
 */
@Slf4j
@Component
public class DataListenerImpl {

    @Autowired
    IChatSoleService soleService;
    @Autowired
    IChatRoomService roomService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    /**
     * 点对点发送信息
     */
    @OnEvent(ImConstants.EVENT_CHAT)
    public void onChat(SocketIOClient socketIOClient, String content, AckRequest ackRequest) {
        log.info(content);
        //ChatProtocol chat=JSONObject.parseObject(content, ChatProtocol.class);
        ChatProtocol chat = (ChatProtocol) ConvertUtils.convert(content, ChatProtocol.class);
        chat.setTime(LocalDateTime.now());
        ChatSole sole = new ChatSole();
        BeanUtils.copyProperties(chat, sole);
        sole.setFlag(false);
        if (!chat.getTarget().isEmpty()) {
            if (soleService.save(sole)) {
                chat.setId(sole.getId());
                applicationEventPublisher.publishEvent(new ChatEvent(chat, ImConstants.EVENT_CHAT));
            }
        }
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData("Done");
        }
    }

    /**
     * 聊天室
     */
    @OnEvent(ImConstants.EVENT_ROOM_CHAT)
    public void onInRoom(SocketIOClient socketIOClient, String content, AckRequest ackRequest) {
        log.info(content);
        ChatProtocol chat = JSONObject.parseObject(content, ChatProtocol.class);
        chat.setTime(LocalDateTime.now());
        ChatRoom room = new ChatRoom();
        BeanUtils.copyProperties(chat, room);
        if (!chat.getTarget().isEmpty()) {
            socketIOClient.joinRoom(chat.getTarget());
            if (roomService.save(room)) {
                chat.setId(room.getId());
                applicationEventPublisher.publishEvent(new ChatEvent(chat, ImConstants.EVENT_ROOM_CHAT));
            }
        }
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData("Done");
        }
    }

    @OnEvent(ImConstants.EVENT_BIN_CHAT)
    public void binEvent(SocketIOClient socketIOClient, byte[] data, AckRequest ackRequest) {
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData("Done");
        }
        socketIOClient.sendEvent(ImConstants.EVENT_BIN_CHAT, data);
    }

}
