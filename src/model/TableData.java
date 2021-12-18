package model;

import view.ChessBoard;

import java.util.Stack;

/**
 * 储存处理棋桌数据，19*19
 */
public class TableData {
    // 现在该下棋的玩家的颜色
    private static String nowColor = Spot.notChess;
    // 游戏是否结束
    private static boolean gameOver = false;
    // 棋盘数组
    private final static Spot[][] spots = new Spot[19][19];

    private final static Stack<Spot> stack=new Stack<Spot>();

    // 游戏结束时，五子连珠起始位置和结束位置
    public static int indexRow = 0;
    public static int indexCol = 0;
    public static int endRow = 0;
    public static int endCol = 0;


    private TableData() {}

    public static void reset() {
        // 初始化棋桌
        for (int i = 0; i < 19; i++) 
            for (int j = 0; j < 19; j++) 
                spots[i][j] = new Spot(i, j, Spot.notChess);
        gameOver = false;
        nowColor = Spot.blackChess;//先走给
        indexRow = indexCol = endRow = endCol = 0;
        while(stack.size()!=0){
            stack.pop();
        }
        System.out.println("已初始化");
    }

    /**
     * 下棋，坐标是(0,0)~(18,18)，范围0-18，调用将更新棋盘的数据
     * @param spot: 下的棋的坐标
     */
    public static void putDownChess(Spot spot) {
        stack.push(spot);

        printStack();


        String mColor = spot.getColor();
        int row = spot.getRow();
        int col = spot.getCol();
        if (gameOver) // 游戏已结束则直接返回
            return;

        if (!nowColor.equals(mColor)) {
            System.out.println("不是此玩家的回合！" + mColor);
            return;
        }

        if (hasSpot(row, col)) {
            System.out.println("此位置已有棋子" + row + ":" + col);
            return;
        }

        nowColor = Spot.getBackColor(nowColor);
        spots[row][col].setColor(mColor);
    }

    /**
     * 判读玩家是否获胜
     * 算法：逐个计算每个棋子，判断这个棋子是否复核胜利条件
     */
    public static boolean isOver() {
        if (gameOver) {// 游戏已结束则直接返回结果
            return true;
        }

        for (int i = 0; i < spots.length; i++) {
            for (int j = 0; j < spots[0].length; j++) {
                //此点向右最大最大连棋数
                int countRow = 0;
                //此点向下最大最大连棋数
                int countCol = 0;
                //此点右下最大最大连棋数
                int countCR = 0;
                //此点左下最大最大连棋数
                int countCL = 0;
                indexRow = i;
                indexCol = j;

                Spot spot = spots[i][j];

                // 获取当前点的棋子颜色
                String color = spot.getColor();

                // 判断此点是否有棋子
                if (Spot.notChess.equals(color)) {
                    continue;
                }
                // 获得此点向右最大连棋数
                if (j <= 14) {
                    for (int m = 1; m <= 4; m++) {
                        String mColor = spots[i][j + m].getColor();
                        if (mColor.equals(color)) {
                            countRow++;
                        }
                    }
                }

                // 判断此点向下最大连棋数
                if (i <= 14)
                    for (int m = 1; m <= 4; m++) {
                        String mColor = spots[i + m][j].getColor();
                        if (mColor.equals(color)) {
                            countCol++;
                        }
                    }

                // 判断此点右下最大连棋数
                if (i <= 14 && j <= 14)
                    for (int m = 1; m <= 4; m++) {
                        String mColor = spots[i + m][j + m].getColor();
                        if (mColor.equals(color)) {
                            countCR++;
                        }
                    }

                // 判断此点左下最大连棋数
                if (i <= 14 && j >= 4)
                    for (int m = 1; m <= 4; m++) {
                        String mColor = spots[i + m][j - m].getColor();
                        if (mColor.equals(color)) {
                            countCL++;
                        }
                    }

                // 得到五子连珠的结束行列值
                if (countRow == 4) {
                    endRow = indexRow;
                    endCol = indexCol + 4;
                    gameOver = true;
                }
                if (countCol == 4) {
                    endRow = indexRow + 4;
                    endCol = indexCol;
                    gameOver = true;
                }
                if (countCR == 4) {
                    endRow = indexRow + 4;
                    endCol = indexCol + 4;
                    gameOver = true;
                }
                if (countCL == 4) {
                    endRow = indexRow + 4;
                    endCol = indexCol - 4;
                    gameOver = true;
                }
                //注意：此点已判断胜利，立即返回胜利结果，不应该再判断下面的点
                if (gameOver) {
                    return gameOver;
                }
            }
        }
        return gameOver;
    }

    public static Spot getSpot(int row, int col) {
        return spots[row][col];
    }

    public static String getNowColor() {
        return nowColor;
    }

    public static boolean hasSpot(int row, int col) {
        Spot spot = spots[row][col];
        String color = spot.getColor();
        return !Spot.notChess.equals(color);
    }


    public static void iRegret() {
        
        //我悔棋
        //如果是我的回合
        // 从栈里pop出两个，
        // 把这两个从spots[][]中除名
        // 并且重绘桌面
        // 问题是在什么时候加这个stack
        if(nowColor==Player.myPlayer.getColor()){
            Spot firstChess=stack.pop();
            Spot secondChess=stack.pop();
            spots[firstChess.getRow()][firstChess.getCol()].setColor(Spot.notChess);
            spots[secondChess.getRow()][secondChess.getCol()].setColor(Spot.notChess);

            ChessBoard.myBoard.repaint();
            printStack();

            nowColor=secondChess.getColor();//最后一个弹出的是我的棋子

        }
        //如果是他的回合
        // 从栈里pop出一个，        
        // 把这1个从spots[][]中除名
        // 并且重绘桌面
        else{
            Spot firstChess=stack.pop();
            spots[firstChess.getRow()][firstChess.getCol()].setColor(Spot.notChess);

            ChessBoard.myBoard.repaint();
            printStack();

            nowColor=firstChess.getColor();

        }
    }

    public static void printStack(){
        System.out.println("================================================");

        for (Spot x : stack) {
            System.out.println(x.getRow()+","+x.getCol()+","+x.getColor());
        }
        System.out.println("================================================");
    }
    public static void heRegret() {
        // 对方悔棋

        //如果是我的回合
        // 从栈里pop出1个，
        // 把这1个从spots[][]中除名
        // 并且重绘桌面
        if(nowColor==Player.myPlayer.getColor()){
            System.out.println("现在的颜色是"+nowColor);
            Spot firstChess=stack.pop();
            
            spots[firstChess.getRow()][firstChess.getCol()].setColor(Spot.notChess);

            ChessBoard.myBoard.repaint();
            printStack();
            
            nowColor=firstChess.getColor();



        }
        //如果是他的回合
        // 从栈里pop出2个，        
        // 把这2个从spots[][]中除名
        // 并且重绘桌面
        else{
            System.out.println("现在的颜色是"+nowColor);
            Spot firstChess=stack.pop();
            Spot secondChess=stack.pop();
            spots[firstChess.getRow()][firstChess.getCol()].setColor(Spot.notChess);
            spots[secondChess.getRow()][secondChess.getCol()].setColor(Spot.notChess);

            ChessBoard.myBoard.repaint();
            printStack();

            nowColor=secondChess.getColor();//最后一个弹出的是我的棋子

        }
    }

}
