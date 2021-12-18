package view;

import model.Player;

import javax.swing.*;
import java.awt.*;

/**
 * 显示玩家昵称，地址等信息
 */
public class UserPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    static JPanel myPanel, otherPanel;
    private static JLabel myName, myColor, myGrade;
    private static JLabel otherName, otherColor, otherGrade;
    static UserPanel userPanel;
    /**
     * 用户信息面板，左面版(我的信息)
     */
    public static final int left = 0;
    /**
     * 用户信息面板，右面版(对方信息)
     */
    public static final int right = 1;

    public UserPanel() {
        // userPanel.setSize(200,300); //使用该方法
        // userPanel.setSize()

        this.setVisible(true);
        this.setLayout(null);
        // this.setBackground(Color.blue);
        this.setBackground(new Color(180, 180, 180));

        myPanel = new JPanel();
        myPanel.setLayout(new GridLayout(0, 1));
        myName = new JLabel("  玩家昵称: ");
        myColor = new JLabel("  玩家棋色: ");
        myGrade = new JLabel("  玩家分数: ");
        myPanel.add(myName);
        myPanel.add(myColor);
        myPanel.add(myGrade);

        otherPanel = new JPanel();
        otherPanel.setLayout(new GridLayout(0, 1));
        otherName = new JLabel("  玩家昵称: ");
        // otherName.setFont(new Font("宋体", Font.PLAIN, 16));
        otherColor = new JLabel("  玩家棋色: ");
        otherGrade = new JLabel("  玩家分数: ");
        otherPanel.add(otherName);
        otherPanel.add(otherColor);
        otherPanel.add(otherGrade);

        this.add(myPanel);
        this.add(otherPanel);
        userPanel = this;
    }

    /**
     * 界面显示，控件加载完毕后执行
     */
    public static void init() {
        // public void setBounds(int x,
        //           int y,
        //           int width,
        //           int height) 
        // myPanel.setBounds(2, 2, userPanel.getWidth() / 2 - 2, userPanel.getHeight() - 5);
        myPanel.setBounds(0, 2, userPanel.getWidth()-10, userPanel.getHeight() /2-2);
        otherPanel.setBounds(0, userPanel.getHeight() /2+2, userPanel.getWidth()-4, userPanel.getHeight() /2-2);

        setUserInfo(null, left);
        setUserInfo(null, right);
        userPanel.repaint();
    }

    /**
     * 在用户信息版面，显示用户信息
     */
    public static void setUserInfo(Player player, int position) {
        if (player == null) 
            player = new Player();
        if (position == left) {
            myName.setText("  玩家昵称: " + player.getName());
            myColor.setText("  玩家棋色: " + player.getColorString());
            myGrade.setText("  玩家分数: " + player.getGrade());
        } else {
            otherName.setText("  玩家昵称: " + player.getName());
            otherColor.setText("  玩家棋色: " + player.getColorString());
            otherGrade.setText("  玩家分数: " + player.getGrade());
        }
    }

    /**
     * 设置信息
     */
    public static void setGrade(int grade, int position) {
        if (position == left)
            myGrade.setText("  玩家分数: " + grade);
        else 
            otherGrade.setText("  玩家分数: " + grade);
    }
}
