package connect;

import model.Player;
import model.Spot;
import view.ChatRoom;
import view.ChessBoard;
import model.TableData;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据发送方案,数据接收，对外接口
 */
public class DataSocket {
    public final static String TYPE_chat = "1";
    public final static String TYPE_spot = "2";
    public final static String TYPE_player = "3";
    public final static String TYPE_regret = "4";

    /**
     * 发送数据，处理数据后发送
     * @param object: 要发的数据
     */
    public static void send(Object object) {
        List<String> list = new ArrayList<>();

        //下棋数据，指下在哪个地方。
        if (object instanceof Spot) {
            list.add(TYPE_spot);
            int row = ((Spot) object).getRow();
            int col = ((Spot) object).getCol();
            // 如果行列数小于10的话需要在数字前加上0
            String a=(row < 10)?("0" + row):("" + row);
            String b=(col < 10)?("0" + col):("" + col);
            list.add(a);
            list.add(b);
            MySocket.sendData(list);
        }
        //玩家信息
        if (object instanceof Player) {
            Player player = (Player) object;
            list.add(TYPE_player);
            list.add(player.getName());
            list.add("" + player.getGrade());
            MySocket.sendData(list);
        }
        //聊天内容
        if (object instanceof String) {
            list.add(TYPE_chat);
            list.add((String) object);
            MySocket.sendData(list);
        }
    }

    /**
     * 发送悔棋的信息
     */
    public static void sendRegret() {
        List<String> list = new ArrayList<>();
        list.add(TYPE_regret);
        MySocket.sendData(list);
    }


    /**
     * 接收数据，数据处理后显示
     * ------------------------------
     * @param list: 是收到的消息，在MySocket中把收到的数据包
     *              转化成List之后调用了这个函数进行处理
     */
    public static void receive(List<String> list) {
        String str = list.get(0);//用来判断是什么类型的消息
        switch (str) {
            // 如果是聊天，加在聊天室里就可以了。
            case TYPE_chat:
                ChatRoom.addText(list.get(1), ChatRoom.peText);
                break;
            case TYPE_spot:
                int row = Integer.valueOf(list.get(1));
                int col = Integer.valueOf(list.get(2));
                String color = Player.otherPlayer.getColor();
                ChessBoard.submitPaint(new Spot(row, col, color));//在棋盘上绘制
                break;
            case TYPE_player:
                String name = list.get(1);
                int grade = Integer.valueOf(list.get(2));
                Player.otherPlayer.setName(name);
                Player.otherPlayer.setGrade(grade);
                break;
            case TYPE_regret:
                System.out.println("对方请求悔棋！");
                TableData.heRegret();//他悔棋了
                break;
            default:
                System.out.println("不是有用的数据！" + str);
                break;
        }
    }
}
