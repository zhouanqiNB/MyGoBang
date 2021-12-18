package view;

import connect.DataSocket;
import connect.MySocket;
import controller.GameCenter;
import model.Spot;
import model.TableData;
import controller.GameCoupe;
import controller.GameOnline;

import javax.swing.*;
import java.awt.*;

/**
 * 主界面右边的控制按钮面板
 */
public class ControlPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JButton btnAgain;
    private JButton btnCoupe;
    private JButton btnOnline;
    private JButton btnRegret;
    public static ControlPanel my;

    public ControlPanel() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setBackground(new Color(220, 220, 220, 220));
        btnAgain = new JButton("重新游戏");

        btnCoupe = new JButton("双人对战");
        btnOnline = new JButton("联机对战");
        btnRegret = new JButton("悔棋");

        btnAgain.setBackground(new Color(176,196,222)); 
        btnCoupe.setBackground(new Color(176,196,222)); 
        btnOnline.setBackground(new Color(176,196,222)); 
        btnRegret.setBackground(new Color(176,196,222)); 

        Dimension preferredSize = new Dimension(180,40);
        btnAgain.setPreferredSize(preferredSize);
        btnCoupe.setPreferredSize(preferredSize);
        btnOnline.setPreferredSize(preferredSize);
        btnRegret.setPreferredSize(preferredSize);

        this.add(btnAgain);
        this.add(btnCoupe);
        this.add(btnOnline);
        this.add(btnRegret);

        my = this;
        addListener();
    }

    public static void init() {
        my.repaint();
    }

    /**
     * 添加监听事件
     */
    private void addListener() {

        btnAgain.addActionListener(event -> {
            GameCenter.reStart();
            ChessBoard.myBoard.repaint();
            MySocket.close();
            btnCoupe.setEnabled(true);
            btnOnline.setEnabled(true);
            btnRegret.setEnabled(true);
            try {
                MySocket.socket.close();
            } catch (Exception e) {
            }
        });

        btnCoupe.addActionListener(event -> {
            GameCoupe.reStart();
            btnCoupe.setEnabled(false);
            btnOnline.setEnabled(false);
        });

        btnOnline.addActionListener(event -> {
            // new 一定要在前面，否则数据被重置！
            GameOnline.reStart();
            MyDialog.online();

            btnCoupe.setEnabled(false);
            btnOnline.setEnabled(false);
        });

        btnRegret.addActionListener(event -> {
            TableData.iRegret();//我悔棋了
            DataSocket.sendRegret();

            btnRegret.setEnabled(false);//只能悔棋一次
        });
    }
}
