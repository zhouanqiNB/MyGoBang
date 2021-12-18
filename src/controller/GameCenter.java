package controller;

import model.TableData;
/**
 * 游戏中心类，控制游戏数据
 */
public class GameCenter {

    /**
     * 储存游戏模式
     */
    private static int gameModel;
    /**
     * 游戏模式 ：游戏结束，或者是游戏还没开始。
     */
    public final static int MODE_END = 0;
    /**
     * 游戏模式 ：双人对战
     */
    public final static int MODE_COUPE = 1;
    /**
     * 游戏模式 ：联机对战
     */
    public final static int MODE_ONLINE = 2;

    /**
     * 重新游戏
     */
    public static void reStart() {
        // 初始化棋桌数据
        TableData.reset();
        gameModel = MODE_END;
    }

    /**
     * 仅供调试，显示所有棋子
     */
    public static void showChess() {
        System.out.println("显示棋桌： 游戏是否结束： isOver() " + TableData.isOver());
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                String color = TableData.getSpot(i, j).getColor();
                System.out.print(color + ", ");
            }
            System.out.println();
        }
    }



    public static int getMode() {
        return gameModel;
    }

    public static void setMode(int mode) {
        gameModel = mode;
    }

    public static boolean isEnd() {
        return gameModel == MODE_END;
    }
}
