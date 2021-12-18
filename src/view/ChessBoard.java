package view;

import connect.DataSocket;
import controller.GameCenter;
import model.Player;
import model.Spot;
import model.TableData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 界面显示 之 棋桌面板，显示棋盘，绘制棋子
 * 将胜负判断加在绘制棋子里面则导致，游戏结束后，界面重新绘制时，导致多次判断胜负
 * 
 * 因此，新增一个函数，用于判断提交的绘制请求是否符合要求
 * 
 * 绘制圆形fillOval,指定的位置是左顶点的位置！
 * 
 * 新增绘制五子连珠情况
 * 
 * 主界面重新绘制后，棋盘绘制不完整(加入线程,等待棋盘绘制完成,等待棋子绘制完成,再绘制结果)
 */
public class ChessBoard extends JPanel {

    private static final long serialVersionUID = 1L;

    protected static int chessSize;//棋子大小
    public static ChessBoard myBoard;//棋盘
    // 多线程，避免棋盘覆盖棋子
    static Thread gThread;
    static Thread allChessThread;

    public ChessBoard() {
        this.setVisible(true);
        // 设置鼠标形状为手型
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setBackground(new Color(255,231,96));
        this.addListener();
        myBoard = this;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintTable();
        paintAllChess();
        paintResult();
    }

    /**
     * 界面显示，控件加载完毕后执行
     */
    public static void init() {
        chessSize = myBoard.getWidth() / 19;
        paintTable();
        myBoard.repaint();
    }

    public static void addGrade() {
        // 当前应该下白棋，则黑棋获胜
        if (!Player.myPlayer.getColor().equals(TableData.getNowColor())) {
            int grade = Player.myPlayer.addGrade(100);
            UserPanel.setGrade(grade, UserPanel.left);
        } else {
            int grade = Player.otherPlayer.addGrade(100);
            UserPanel.setGrade(grade, UserPanel.right);
        }
    }

    /**
     * 添加监听事件，监听树苗点击事件
     */
    private void addListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int cx = e.getX();
                int cy = e.getY();
                System.out.print("点击坐标" + cx + ":" + cy);

                // 根据屏幕坐标，获得行列
                int row = Coordinate.yToRow(cy);
                int col = Coordinate.xToCol(cx);
                Spot spot = new Spot(row - 1, col - 1, TableData.getNowColor());
                System.out.println("， 点击行列" + row + ":" + col);

                boolean canSpot = isCanSpot(spot);
                if (canSpot) {
                    if (GameCenter.getMode() == GameCenter.MODE_ONLINE) {
                        DataSocket.send(spot);
                    }
                    submitPaint(spot);
                }
            }
        });
    }

    /**
     * 接收绘制棋子请求，先验证，后绘制
     */
    public static void submitPaint(Spot spot) {
        paintChessImages(spot);
        TableData.putDownChess(spot);

        //画完之后检测是否形成了五子。
        if (TableData.isOver()) {
            addGrade();// 增加用户积分
            GameCenter.setMode(GameCenter.MODE_END);
            paintResult();
            // 该白棋下了，游戏结束，说明黑棋赢了。
            if (Spot.whiteChess.equals(TableData.getNowColor())) {
                JOptionPane.showMessageDialog(MainFrame.mainFrame, "黑棋获胜！积分+100！",
                        "游戏结束", JOptionPane.CANCEL_OPTION);
            } 
            else {
                JOptionPane.showMessageDialog(MainFrame.mainFrame, "白棋获胜！积分+100！",
                        "游戏结束", JOptionPane.CANCEL_OPTION);
            }
        }
    }

    public static boolean isCanSpot(Spot spot) {
        if (TableData.hasSpot(spot.getRow(), spot.getCol())) {
            System.out.println("此点已有棋子！");
            return false;
        }
        //如果时联机对战，界面上只能下自己颜色的棋子
        if (GameCenter.getMode() == GameCenter.MODE_ONLINE) {
            if (!spot.getColor().equals(Player.myPlayer.getColor())) {
                JOptionPane.showMessageDialog(MainFrame.mainFrame,
                        "联机对战中，请等待对方。", "请等待..",
                        JOptionPane.CANCEL_OPTION);
                System.out.println(Player.myPlayer.getColor() + ":" + TableData.getNowColor());
                return false;
            }
        }

        // 判断符合下棋要求
        if (GameCenter.isEnd()) {
            JOptionPane.showMessageDialog(null,
                    "游戏不在进行中！\n请选择游戏模式以开始游戏。", "游戏未开始",
                    JOptionPane.CANCEL_OPTION);
            return false;
        }
        return true;
    }

    /**
     * 绘制单颗棋子
     * @param spot: 棋子类，分为无棋子、黑棋和白棋。
     */
    private static void paintChessImages(Spot spot) {
        if (spot != null) {
            int row = spot.getRow() + 1;
            int col = spot.getCol() + 1;

            int cx = Coordinate.colToX(col);//得到所要绘制棋子的那个位置。
            int cy = Coordinate.rowToY(row);
            Graphics g = myBoard.getGraphics();
            /*
             利用Graphics对象获取画笔
             */
            String color = spot.getColor();
            switch (color) {
                case Spot.blackChess:
                    g.setColor(Color.black);
                    break;
                case Spot.whiteChess:
                    g.setColor(Color.white);
                    break;
                default:
                    return;
            }
            // fillOval(int x,int y ,int width ,int  height)，填充椭圆
            // X 和 Y是矩形框的左上角的坐标，width和height是宽和高。
            // (cx,cy)就是那个交点
            g.fillOval(cx - chessSize / 2, cy - chessSize / 2, chessSize,chessSize);
        }
    }

    /**
     * 绘制棋桌
     */
    private static void paintTable() {
        final Graphics graphics = myBoard.getGraphics();
        // 在线程中绘制棋盘
        gThread = new Thread(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
            for (int i = 0; i < 19; i++) {
                int hori_startx=chessSize / 2;
                int hori_starty=chessSize * i+chessSize / 2;
                int hori_endx=chessSize / 2 + chessSize * 18;
                int hori_endy=chessSize / 2 + chessSize * i;
                graphics.drawLine(hori_startx, hori_starty, hori_endx, hori_endy);

                int vert_startx=chessSize / 2 + chessSize * i;
                int vert_starty=chessSize / 2;
                int vert_endx=chessSize / 2 + chessSize * i;
                int vert_endy=chessSize / 2+ chessSize * 18;
                graphics.drawLine(vert_startx, vert_starty, vert_endx, vert_endy);
            }
        });
        gThread.start();
    }

    /**
     * 绘制所有棋子
     */
    private static void paintAllChess() {
        // 绘制所有棋子线程，没有线程时棋子可能绘制失败！
        allChessThread = new Thread(() -> {
            try {// 等待棋桌绘制完成
                gThread.join();
            } catch (InterruptedException e) {
            }
            for (int i = 0; i < 19; i++) {
                for (int j = 0; j < 19; j++) {
                    Spot spot = TableData.getSpot(i, j);
                    paintChessImages(spot);
                }
            }
        });
        allChessThread.start();
    }

    /**
     * 绘制赢棋后的五子连珠
     */
    private static void paintResult() {
        new Thread(() -> {
            try {
                allChessThread.join();
            } catch (InterruptedException e) {
            }
            if (GameCenter.isEnd()) {
                if (TableData.endRow + TableData.endCol < 5) {
                    return;
                }

                System.out.println("赢棋起始位置：" + TableData.indexRow + " " + TableData.indexCol);
                System.out.println("赢棋终止位置：" + TableData.endRow + " " + TableData.endCol);

                Graphics2D g = (Graphics2D) myBoard.getGraphics();
                int indexX = Coordinate.colToX(TableData.indexCol + 1);
                int indexY = Coordinate.rowToY(TableData.indexRow + 1);
                int endX = Coordinate.colToX(TableData.endCol + 1);
                int endY = Coordinate.rowToY(TableData.endRow + 1);
                g.setColor(Color.yellow);
                g.setStroke(new BasicStroke(5.0f));
                g.setFont(new Font("黑体", Font.BOLD, 150));
                g.drawLine(indexX, indexY, endX, endY);
            }
        }).start();
    }
}
