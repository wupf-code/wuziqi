package com.wuziqi.wuziqi.consumer.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
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
    public Integer x;
    public Integer y;
    public volatile boolean exit = false;
    public Integer userID;

    private  List<Chess> mWhiteArray = new ArrayList<>();
    private  List<Chess> mBlackArray = new ArrayList<>();
    public void receiveChat(String chat){
        lock.lock();
        try{

            this.sendChat(chat);

        }finally {
            lock.unlock();
        }

    }
    public void receiveMessage(Integer userId, Integer x ,Integer y){
        lock.lock();
        try {
            this.userID = userId;
            this.x = x;
            this.y = y;
//            System.out.println(userId +" "+ x+" " + y);
        }finally {
            lock.unlock();
        }
    }

//    private boolean judgeChat()
//    {
//        if(!Objects.equals(this.chat, "")){
//            return true;
//        }else return false;
//    }

    private boolean receive() {

        lock.lock();
        try {
            if ((aId != null || bId != null) && userID != null) {
                return true;
            }
            else return false;
        } finally {
            lock.unlock();
        }
    }


    private void sendMove(){

        lock.lock();
        try{
            if(userID != null && userID.equals(aId)) {
                lock.lock();
                try {
                    Chess p = new Chess(this.x,this.y);
                    this.mBlackArray.add(p);
                }finally {
                    lock.unlock();
                }

                if(checkWin(new Chess(this.x,this.y),false)){
                    JSONObject respA = new JSONObject();
                    respA.put("event","result");
                    respA.put("win","winner");
                    respA.put("own_x", this.x);
                    respA.put("own_y", this.y);
                    respA.put("can_next",false);
                    users.get(aId).sendMessage(respA.toJSONString());
                    JSONObject respB = new JSONObject();
                    respB.put("event","result");
                    respB.put("win","loser");
                    respB.put("opponent_x", this.x);
                    respB.put("opponent_y", this.y);
                    respB.put("can_next",false);
                    users.get(bId).sendMessage(respB.toJSONString());
                    this.exit=true;
                }else {
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
                }
                userID = x = y =null;
            }else if( bId!=null &&userID!=null && userID.equals(bId)){
                lock.lock();
                try {
                    Chess p = new Chess(this.x,this.y);
                    this.mWhiteArray.add(p);
                }finally {
                    lock.unlock();
                }
                if(checkWin(new Chess(this.x,this.y),true)){
                    JSONObject respA = new JSONObject();
                    respA.put("event","result");
                    respA.put("win","winner");
                    respA.put("own_x", this.x);
                    respA.put("own_y", this.y);
                    respA.put("can_next",false);
                    users.get(bId).sendMessage(respA.toJSONString());
                    JSONObject respB = new JSONObject();
                    respB.put("event","result");
                    respB.put("win","loser");
                    respB.put("opponent_x", this.x);
                    respB.put("opponent_y", this.y);
                    respB.put("can_next",false);
                    users.get(aId).sendMessage(respB.toJSONString());
                    this.exit=true;
                }else {
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
                }
                userID = x = y =null;
            }
        }finally {
            lock.unlock();
        }

    }

    private boolean containChess(List<Chess> array, Chess tmp){
        lock.lock();
        try {
            boolean flag = false;
            for(Chess t : array){
                if(t.x == tmp.x && t.y ==tmp.y){
                    flag = true;
                    break;
                }
            }
            return flag;
        }finally {
            lock.unlock();
        }

    }


    private boolean checkByStep(Chess p, List<Chess> array, int xdiff, int ydiff) {
//        lock.lock();
//        try{
            Chess tmp = new Chess(0, 0);
            int i;
            int cnt = 0;

            //向反方向找到颜色相同的点
            for (i = 1;  i < 5; i++){
                tmp.x = p.x - xdiff * i;
                tmp.y = p.y - ydiff * i;
                if (!containChess(array,tmp))
                    break;
                cnt++;
            }

            int x = tmp.x;
            int y = tmp.y;

            for (i = 1;  i < 5; i++){
                tmp.x = p.x + xdiff * i;
                tmp.y = p.y + ydiff * i;
                if (!containChess(array,tmp))
                    break;
                cnt++;
            }
            if (cnt >= 4){
                cnt = 0;
                return true;
            }

            return false;
//        }finally {
//            lock.unlock();
//        }

    }

    private boolean checkWin(Chess p, boolean isWhite) {
//        lock.lock();
//        try{
            boolean flag = false;
            List<Chess> array;
            lock.lock();
            try {
                if (isWhite) {
                    array = this.mWhiteArray;
                } else
                {
                    array = this.mBlackArray;
                }
            }finally {
                lock.unlock();
            }

            if (checkByStep(p, array, 0, 1))    //左右直线判断
                flag = true;
            if (checkByStep(p, array, 1, 0))    //上下直线判断
                flag = true;
            if (checkByStep(p, array, -1, 1))    //右朝上直线判断
                flag = true;
            if (checkByStep(p, array, 1, 1))   //右朝下直线判断
                flag = true;
            if(flag == true){
                array.clear();
            }
            return flag;
//        }finally {
//            lock.unlock();
//        }

    }

    private void sendChat(String chat){

            JSONObject respA = new JSONObject();
            respA.put("event","chat");
            respA.put("chat",chat);
            users.get(aId).sendMessage(respA.toJSONString());
            users.get(bId).sendMessage(respA.toJSONString());
        }



    public Game(Integer aId, Integer bId) {
        this.aId = aId;
        this.bId = bId;
    }
    @Override
    public void run () {
        while(!exit){
            if(receive())
                sendMove();

        }

    }

}
