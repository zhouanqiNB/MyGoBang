package view;

import connect.MyIPTool;
import game.CountDown;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 界面显示 之 菜单栏
 */
public class MyMenuBar extends JMenuBar {
    private static final long serialVersionUID = 1L;
    JMenuItem checkIP, setInfo, exit, about, version, setTime, setLevel, login,
            register, admin, word, helpOnline;

    public MyMenuBar() {
        UIManager.put("Menu.font", new Font("宋体", Font.BOLD, 17));
        UIManager.put("MenuItem.font", new Font("宋体", Font.BOLD, 17));
        UIManager.put("MenuBar.background", Color.WHITE);

        JMenu menu = new JMenu("菜单");

        checkIP = new JMenuItem("查看本机IP");
        setTime = new JMenuItem("倒计时设置");
        exit = new JMenuItem("退出");

        menu.add(checkIP);
        menu.add(setTime);
        menu.add(exit);

        this.add(menu);
        addListener();
    }

    public void addListener() {
        exit.addActionListener(event -> MainFrame.close());
        checkIP.addActionListener(event -> {
            String localIP = "本机所有IP地址:";
            List<String> res = MyIPTool.getAllLocalHostIP();
            String allIp = res.stream().collect(Collectors.joining("\n"));
            localIP = localIP + "\n" + allIp;

            JOptionPane.showMessageDialog(MainFrame.mainFrame, localIP, "查看本机IP", JOptionPane.INFORMATION_MESSAGE);
        });
        setTime.addActionListener(event -> {
            String input = JOptionPane.showInputDialog("请输入超时时间(秒)", "30");
            try {
                int time = Integer.valueOf(input);
                CountDown.startTiming(time);
            } catch (Exception e) {
            }
        });
    }
}
