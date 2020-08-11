package com.dcjt.dcjtim.listener.imlistener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.dcjt.dcjtim.socket.session.SessionMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 断开连接处理器
 * Created by 唐文滔 on 2020/6/2
 */
@Slf4j
@Component
public class DisconnectListenerImpl implements DisconnectListener {
    @Override
    public void onDisconnect(SocketIOClient socketIOClient) {
        if (Optional.ofNullable(socketIOClient.getHandshakeData().getSingleUrlParam("TOKEN")).isPresent()) {
            String token = socketIOClient.getHandshakeData().getSingleUrlParam("TOKEN").trim();
            SessionMap.inst.removeSession(token, socketIOClient);
            log.info("用户 {} 下线",token);
        }
    }
}
