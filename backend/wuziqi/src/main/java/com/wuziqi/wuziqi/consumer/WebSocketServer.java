package com.wuziqi.wuziqi.consumer;

/**
 * @author: 武鹏飞
 * @user:ASUS
 * @date:2022/8/25 - 20:24
 * @projectName:wuziqi
 */
import com.alibaba.fastjson.JSONObject;
import com.wuziqi.wuziqi.consumer.utils.Game;
import com.wuziqi.wuziqi.consumer.utils.WebsocketUtil;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
     public static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();
    final private static CopyOnWriteArraySet<Integer> matchpool = new CopyOnWriteArraySet<>();

    private Session session = null;
    private Integer userId = null;

    private Game game = null;
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 建立连接
        this.session = session;
        userId = Integer.parseInt(token);
//        InetSocketAddress remoteAddress = WebsocketUtil.getRemoteAddress(session);
//        System.out.println("有新连接加入！" + remoteAddress);
//        System.out.println("connected");
//        System.out.println(userId);
//        if(!users.containsKey(userId))
//            users.put(userId,this);
//        else users.replace(userId, this);
        users.put(userId,this);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        if(this.userId !=null) {
            users.remove(userId);
        }
        matchpool.remove(userId);
//        System.out.println("disconnected");


    }

    private void move(Integer userId,Integer x, Integer y){
        game.receiveMessage(userId,x,y);
    }
    private void chat(String chat){game.receiveChat(chat);}
    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
//        System.out.println("receive message");
        JSONObject data = JSONObject.parseObject(message);
//        System.out.println(data);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatching();
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        } else if("move".equals(event)){
            move(userId,data.getInteger("x"),data.getInteger("y"));
        } else if("chat".equals(event)) {
            chat(data.getString("message"));
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

            Game game = new Game(a, b);
//            game.createMap();
            users.get(a).game = game;
            users.get(b).game = game;

            game.start();

            JSONObject respA = new JSONObject();
            respA.put("event", "start-matching");
            respA.put("opponent_id", b);
            respA.put("own_color","black");
            respA.put("opponent_color","white");
            respA.put("can_next",true);
            users.get(a).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event", "start-matching");
            respB.put("opponent_id", a);
            respB.put("own_color","white");
            respB.put("opponent_color","black");
            respB.put("can_next",false);

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


