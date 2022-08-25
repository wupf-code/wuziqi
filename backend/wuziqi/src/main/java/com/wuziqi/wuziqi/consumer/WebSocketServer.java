package com.wuziqi.wuziqi.consumer;

/**
 * @author: 武鹏飞
 * @user:ASUS
 * @date:2022/8/25 - 20:24
 * @projectName:wuziqi
 */
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    private static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();
    final private static CopyOnWriteArraySet<Integer> matchpool = new CopyOnWriteArraySet<>();

    private Session session = null;
    private Integer userId = null;
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 建立连接
        this.session = session;
        userId = Integer.parseInt(token);
        System.out.println("connected");
        System.out.println(userId);
        users.put(userId,this);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        if(this.userId !=null) {
            users.remove(userId);
        }
        matchpool.remove(userId);
        System.out.println("disconnected");


    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatching();
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        }

    }

    private void stopMatching() {
        matchpool.remove(userId);
    }

    private void startMatching() {
        matchpool.add(userId);
        while (matchpool.size() >= 2) {
            Iterator<Integer> it = matchpool.iterator();
            Integer a = it.next(), b = it.next();
            matchpool.remove(a);
            matchpool.remove(b);

//            Game game = new Game(13, 14, 20);
//            game.createMap();

            JSONObject respA = new JSONObject();
            respA.put("event", "start-matching");
            respA.put("opponent_id", b);

            users.get(a).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event", "start-matching");
            respB.put("opponent_id", a);
            users.get(b).sendMessage(respB.toJSONString());
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    public void sendMessage(String message){
        synchronized (this.session){
            try{
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


