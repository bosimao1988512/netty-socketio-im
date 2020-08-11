package com.dcjt.dcjtim.listener.imlistener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.dcjt.dcjtim.bean.ImConstants;
import com.dcjt.dcjtim.bean.MsgType;
import com.dcjt.dcjtim.entity.ChatSole;
import com.dcjt.dcjtim.service.IChatSoleService;
import com.dcjt.dcjtim.socket.session.SessionMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 连接处理器
 * Created by 唐文滔 on 2020/6/2
 */
@Slf4j
@Component
public class ConnectListenerImpl implements ConnectListener {

    @Autowired
    IChatSoleService soleService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onConnect(SocketIOClient socketIOClient) {
        if (!Optional.ofNullable(socketIOClient.getHandshakeData().getSingleUrlParam("TOKEN")).isPresent()) {
            Map<String, Object> msg = new HashMap<>();
            msg.put("content", "TOKEN is missing");
            msg.put("msgType", MsgType.ONMSG.code);
            socketIOClient.sendEvent(ImConstants.EVENT_CHAT, JSON.toJSONString(msg));
            socketIOClient.disconnect();
            return;
        }
        String token = socketIOClient.getHandshakeData().getSingleUrlParam("TOKEN").trim();
        SessionMap.inst.addSession(token, socketIOClient);

        pullIm(socketIOClient);

        log.info("用户 {} 上线……", token);
    }

    /**
     * 拉取历史未收信息
     *
     * @param socketIOClient
     */
    private void pullIm(SocketIOClient socketIOClient) {
        String token = socketIOClient.getHandshakeData().getSingleUrlParam("TOKEN").trim();
        QueryWrapper<ChatSole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("target", token).eq("flag", false).orderByAsc("time");
        List<ChatSole> soleList = soleService.list(queryWrapper);
        socketIOClient.sendEvent(ImConstants.EVENT_CHAT, new AckCallback<String>(String.class) {
            @Override
            public void onSuccess(String result) {
                UpdateWrapper<ChatSole> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("flag", false).eq("target", token).set("flag", true);
                soleService.update(updateWrapper);
            }
        }, JSON.toJSONString(soleList));
    }
}
