package view;

import connect.MySocket;
import data.GameCenter;
import data.Spot;
import game.GameCoupe;
import game.GameOnline;

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
    public static ControlPanel my;

    public ControlPanel() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setBackground(new Color(220, 220, 220, 220));
        btnAgain = new JButton("重新游戏");

        btnCoupe = new JButton("双人对战");
        btnOnline = new JButton("联机对战");

        btnAgain.setBackground(new Color(176,196,222)); 
        btnCoupe.setBackground(new Color(176,196,222)); 
        btnOnline.setBackground(new Color(176,196,222)); 

        Dimension preferredSize = new Dimension(180,40);
        btnAgain.setPreferredSize(preferredSize);
        btnCoupe.setPreferredSize(preferredSize);
        btnOnline.setPreferredSize(preferredSize);



        this.add(btnAgain);
        this.add(btnCoupe);
        this.add(btnOnline);

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
    }
}
