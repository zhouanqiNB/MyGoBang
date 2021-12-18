package controller;

import connect.MySocket;
import controller.GameCenter;
import model.Player;
import view.MyDialog;
import view.UserPanel;

/**
 * 联机对战类
 */
public class GameOnline {

    private GameOnline() {}

    public static void reStart() {
        GameCenter.reStart();
        GameCenter.setMode(GameCenter.MODE_ONLINE);
    }

    /**
     * 监控对方是否下线
     */
    public static void monitor() {
        // 用于检测游戏是否结束
        new Thread(() -> {
            while (MySocket.isStart) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
            MyDialog.monitor();
            System.out.println("在线监控线程 已终止！");
        }).start();
    }

    /**
     * 相互发送接收玩家信息
     */
    public static void getOtherAddress() {
        // 用于检测游戏是否结束
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                if (MySocket.isStart) {
                    Player.otherPlayer.setAddress(MySocket.peAddress);
                    UserPanel.setUserInfo(Player.otherPlayer, UserPanel.right);
                }
            } catch (InterruptedException e) {
            }

        }).start();
    }
}

