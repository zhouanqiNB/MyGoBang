package view;

import controller.GameCenter;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * 主界面UI 
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // 窗体大小
    private int width = 1000;
    private int height = 600;

    // 三个主面板
    public static MainFrame mainFrame;

    public MainFrame() {
        mainFrame = this;
        UIManager.put("Label.font", new Font("宋体", Font.BOLD, 15));
        UIManager.put("Button.font", new Font("宋体", Font.PLAIN, 20));

        this.setTitle("五子棋");
        this.setSize(width, height);
        this.setResizable(false);
        this.setLayout(null);

        int sWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int sHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setLocation((sWidth - width) / 2, (sHeight - height) / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWidget();
    }

    /**
     * 添加控件和菜单栏
     */
    private void addWidget() {
        this.setJMenuBar(new MyMenuBar());

        getContentPane().add(new ChatRoom());
        getContentPane().add(new ChessBoard());
        getContentPane().add(new UserPanel());
        getContentPane().add(new StatePanel());
        getContentPane().add(new ControlPanel());

        ChatRoom.myRoom.setBounds(0, 0, 230, 600);
        ChessBoard.myBoard.setBounds(230, 1, 538, 537);
        UserPanel.userPanel.setBounds(770, 0, 230, 200);
        StatePanel.my.setBounds(770, 200, 230,100);
        ControlPanel.my.setBounds(770, 300, 220,300);
    }

    // 界面显示，控件加载完毕后执行(向控件加载数据等)
    public static void init() {
        GameCenter.reStart();
        ChessBoard.init();
        UserPanel.init();
        ControlPanel.init();
        Player.init();
        mainFrame.repaint();
    }

    // 窗口关闭事件
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            close();
        } else {
            super.processWindowEvent(e); // 该语句会执行窗口事件的默认动作(如：隐藏)
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        ControlPanel.init();
    }

    public static void close() {
        int i = JOptionPane.showConfirmDialog(null, "确定要退出吗？", "dialog",
                JOptionPane.YES_NO_OPTION);
        if (i == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
