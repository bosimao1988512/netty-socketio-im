package com.dcjt.dcjtim.socket.server;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.dcjt.dcjtim.listener.imlistener.DataListenerImpl;
import com.dcjt.dcjtim.service.IChatSoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * socket服务
 * Created by 唐文滔 on 2020/6/3
 */
@Slf4j
@Component
public class SocketServerStart implements CommandLineRunner {

    @Autowired
    SocketIOServer socketIOServer;
    @Autowired
    DataListenerImpl dataListenerImpl;
    @Autowired
    ConnectListener connectListener;
    @Autowired
    DisconnectListener disconnectListener;
    @Autowired
    IChatSoleService soleService;

    @Override
    public void run(String... args) throws Exception {
        socketIOServer.addConnectListener(connectListener);
        socketIOServer.addDisconnectListener(disconnectListener);
        socketIOServer.addListeners(dataListenerImpl);
        socketIOServer.start();
        log.info("IM服务已启动…………");

        //订阅消息
        /*socketIOServer.getConfiguration().getStoreFactory().pubSubStore().subscribe(PubSubType.DISPATCH, data -> {
            String roomId = data.getRoom();
            Packet packet = data.getPacket();
            String event = packet.getName();
            if (Objects.requireNonNull(roomId).isEmpty()) {
                socketIOServer.getRoomOperations(roomId).sendEvent(event, packet.getData());
            } else {
                ChatSole sole= packet.getData();
                String targetToken = sole.getTarget().trim();
                Set<SocketIOClient> clients = SessionMap.inst.getSession(targetToken);
                if (null != clients) {
                    sole.setFlag(true);
                    for (SocketIOClient client : clients) {
                        client.sendEvent(event, sole);
                    }
                    UpdateWrapper<ChatSole> wrapper = new UpdateWrapper<>();
                    wrapper.eq("id", sole.getId()).set("flag", true);
                    soleService.update(wrapper);
                }
            }

        }, DispatchMessage.class);*/
    }
}
