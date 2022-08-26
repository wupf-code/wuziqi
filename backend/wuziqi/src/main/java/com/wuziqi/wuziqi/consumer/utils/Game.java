package com.wuziqi.wuziqi.consumer.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.locks.ReentrantLock;

import static com.wuziqi.wuziqi.consumer.WebSocketServer.users;

/**
 * @author: 武鹏飞
 * @user:ASUS
 * @date:2022/8/26 - 11:32
 * @projectName:wuziqi
 */
public class Game extends  Thread{
    private ReentrantLock lock = new ReentrantLock();
    public Integer aId;
    public Integer bId;
    public Integer canNext = 1;
    public Integer x;
    public Integer y;

    public Integer userID;

    public void receiveMessage(Integer userId, Integer x ,Integer y){
        lock.lock();
        try {
            this.userID = userId;
            this.x = x;
            this.y = y;
            System.out.println(userId +" "+ x+" " + y);
        }finally {
            lock.unlock();
        }
    }

    private boolean receive(){
        while(true){
            lock.lock();
            try{
                if((aId!=null || bId != null) &&userID!=null){
                    return true;
                }
            }finally {
                lock.unlock();
            }
        }
    }

    private void sendMove(){

        lock.lock();
        try{
            if(aId!=null &&userID!=null && userID.equals(aId)) {
//                canNext=2;
                JSONObject respA = new JSONObject();
                respA.put("event", "moveown");
                respA.put("own_x", this.x);
                respA.put("own_y", this.y);
                respA.put("can_next",false);
                users.get(aId).sendMessage(respA.toJSONString());
                JSONObject respB = new JSONObject();
                respB.put("event", "moveopponent");
                respB.put("opponent_x", this.x);
                respB.put("opponent_y", this.y);
                respB.put("can_next",true);
                users.get(bId).sendMessage(respB.toJSONString());
                userID = x = y =null;
            }else if( bId!=null &&userID!=null && userID.equals(bId)){
//                canNext=1;
                JSONObject respA = new JSONObject();
                respA.put("event", "moveown");
                respA.put("own_x", this.x);
                respA.put("own_y", this.y);
                respA.put("can_next",false);
                users.get(bId).sendMessage(respA.toJSONString());
                JSONObject respB = new JSONObject();
                respB.put("event", "moveopponent");
                respB.put("opponent_x", this.x);
                respB.put("opponent_y", this.y);
                respB.put("can_next",true);
                users.get(aId).sendMessage(respB.toJSONString());
                userID = x = y =null;
            }
        }finally {
            lock.unlock();
        }

    }
    public Game(Integer aId, Integer bId) {
        this.aId = aId;
        this.bId = bId;
    }
    @Override
    public void run () {
//        while((aId!=null || bId != null) &&userID!=null)
        for(int i = 0; i < 100;i++)
            if(receive())
                sendMove();
    }

}
