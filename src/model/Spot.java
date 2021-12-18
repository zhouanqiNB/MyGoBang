package model;

/**
 * 棋子类
 */
public class Spot {
    public final static String notChess = "none";//没有棋子
    public final static String blackChess = "black";
    public final static String whiteChess = "white";

    private String color;
    private int row;
    private int col;

    public Spot() {}

    public Spot(int row, int col, String color) {
        this.color = color;
        this.row = row;
        this.col = col;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * 获得相反颜色棋子的值
     * @param mColor: 如果是白的，返回黑；是黑，返回白。
     */
    public static String getBackColor(String mColor) {
        if(Spot.blackChess.equals(mColor))
            return Spot.whiteChess;
        return Spot.blackChess;
    }

    /**
     * 返回棋子颜色字符串
     * @param mColor: 
     */
    public static String getColorString(String mColor) {
        if (Spot.notChess.equals(mColor)) {
            return "无棋子";
        }
        if (Spot.blackChess.equals(mColor)) {
            return "黑棋";
        }
        if (Spot.whiteChess.equals(mColor)) {
            return "白棋";
        }
        return "参数错误！" + mColor;
    }
}
