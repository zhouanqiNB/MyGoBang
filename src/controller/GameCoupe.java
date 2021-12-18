package controller;

import model.Player;
import model.Spot;
import view.UserPanel;

/**
 * 双人对战
 */
public class GameCoupe {
    private GameCoupe() {}

    public static void reStart() {
        GameCenter.reStart();
        GameCenter.setMode(GameCenter.MODE_COUPE);
        Player.myPlayer.start(Spot.blackChess);
        Player.otherPlayer.start(Spot.whiteChess);

        // 用户面板，更新用户信息
        UserPanel.setUserInfo(Player.myPlayer, UserPanel.left);
        UserPanel.setUserInfo(Player.otherPlayer, UserPanel.right);
    }

}
