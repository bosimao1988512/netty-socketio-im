package com.dcjt.dcjtim.socket.session;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.dcjt.dcjtim.bean.ChatProtocol;
import com.dcjt.dcjtim.bean.ImConstants;
import com.dcjt.dcjtim.entity.ChatSole;
import com.dcjt.dcjtim.service.IChatRoomService;
import com.dcjt.dcjtim.service.IChatSoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author 滔哥
 * @since 2020/7/30
 */
@Component
public class TransmitAction {

    private static IChatSoleService soleService;
    private static SocketIOServer server;

    @Autowired
    public TransmitAction(IChatSoleService chatSoleService, SocketIOServer socketIOServer) {
        soleService = chatSoleService;
        server = socketIOServer;
    }

    /**
     * 单用户
     * @param chat
     */
    public static void sole(ChatProtocol chat) {
        if (!chat.getTarget().isEmpty()) {
            Set<SocketIOClient> clients = SessionMap.inst.getSession(chat.getTarget());
            if (null != clients) {
                for (SocketIOClient client : clients) {
                    client.sendEvent(ImConstants.EVENT_CHAT, new AckCallback<String>(String.class) {
                        @Override
                        public void onSuccess(String result) {
                            soleService.update(Wrappers.<ChatSole>lambdaUpdate().eq(ChatSole::getId, chat.getId()).set(ChatSole::getFlag, true));
                        }
                    }, JSON.toJSONString(chat));
                }
            } /*else {
                Packet packet = new Packet(PacketType.MESSAGE);
                packet.setData(chat);
                packet.setName(ImConstants.EVENT_CHAT);
                server.getConfiguration().getStoreFactory().pubSubStore().publish(PubSubType.DISPATCH, new DispatchMessage("", packet, ""));
            }*/
        }
    }

    /**
     * 组用户
     * @param chat
     */
    public static void room(ChatProtocol chat) {
        if (!chat.getTarget().isEmpty()) {
            server.getRoomOperations(chat.getTarget()).sendEvent(ImConstants.EVENT_ROOM_CHAT, JSON.toJSONString(chat));
            /*Packet roomChat = new Packet(PacketType.MESSAGE);
            roomChat.setName(ImConstants.EVENT_ROOM_CHAT);
            roomChat.setData(chat);
            server.getConfiguration().getStoreFactory().pubSubStore().publish(PubSubType.DISPATCH, new DispatchMessage(roomId, roomChat, ""));
            */
        }
    }
}
