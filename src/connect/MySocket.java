package connect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class MySocket {
    public static Socket socket;//客户端的链接
    public static ServerSocket server;//服务端
    public final static int port = 8000;

    public static boolean isStart = false;
    public static String peAddress;//对方的IP

    /**
     * server
     * ----------------------------------------
     * 用于创建房间
     * 创建Socket服务，如果是创建，则保存连接到的socket
     */
    public static void startServer() throws IOException {
        // ServerSocket 类表示 Socket 服务器端，Socket 类表示 Socket 客户端，两者之间的交互过程如下：
        // 服务器端创建一个 ServerSocket（服务器端套接字），调用 accept() 方法等待客户端来连接。
        // 客户端程序创建一个 Socket，请求与服务器建立连接。
        // 服务器接收客户的连接请求，同时创建一个新的 Socket 与客户建立连接，服务器继续等待新的请求。

        server = new ServerSocket(port);    //server的端口是8000
        MySocket.socket = server.accept();  //监听连接
        startGetData();                     // 有玩家加入房间,准备接收数据

        // socket.getInetAddress()返回InetAddress对象包含远程计算机的IP地址。
        // InetAddress.getHostAddress()返回String对象与该地址的文本表示。 

        peAddress = socket.getInetAddress().getHostAddress();
        isStart = true;
        System.out.println("对方加入连接，准备接收数据。");
    }

    /**
     * server
     * ----------------------------------------
     * 接收数据，一直都运行
     */
    protected static void startGetData() {
        new Thread(() -> {
            while (true) {
                try {
                    // 获取对方发过来的数据
                    ObjectInputStream in = new ObjectInputStream(MySocket.socket.getInputStream());

                    // 读取数据
                    Object object = in.readObject();
                    // 如果对方发过来的是列表，那么接受这个列表。
                    if (object instanceof List) {
                        List<String> list = (List<String>) object;
                        DataSocket.receive(list);   //这是Datasocket类
                    }
                } catch (Exception e) {
                    System.out.println("getData() 数据接收异常，终止接收");
                    isStart = false;
                    return;
                }
            }
        }).start();
    }


    /**
     * client
     * ----------------------------------------
     * 用于加入房间
     * @param address: 要连接的房间的ip地址
     */
    public static void getSocket(final String address) throws Exception {
        // 新建一个Socket，连接给定的地址
        socket = new Socket();
        socket.connect(new InetSocketAddress(address, port));
        startGetData();// 成功加入房间则接收数据
        isStart = true;
        peAddress = socket.getInetAddress().getHostAddress();
        System.out.println("加入对方连接,准备接收数据。");
    }

    /**
     * client
     * ----------------------------------------
     * 发送数据
     * @param Object: 要发送的数据
     */
    protected static void sendData(final Object object) {
        // getInputStream方法可以得到一个输入流，客户端的Socket对象上的getInputStream方法得到输
        // 入流其实就是从服务器端发回的数据。
        // getOutputStream方法得到的是一个输出流，客户端的Socket对象上的getOutputStream方法得到
        // 的输出流其实就是发送给服务器端的数据。

        new Thread(() -> {
            try {
                ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                os.writeObject(object);//写发送给服务端的数据
            } catch (Exception e) {
                // 如果在发送数据的时候出问题，终止发送
                isStart = false;
            }
        }).start();
        // 这个线程用来发数据，发完就停止。
    }


    public static void close() {
        try {
            server.close();
            isStart = false;
        } catch (Exception e) {
        }
        try {
            socket.close();
            isStart = false;
        } catch (Exception e) {
        }
    }
}
