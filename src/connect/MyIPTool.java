package connect;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MyIPTool {

    /**
     * 获取本机所有IP
     * @return 返回一个IP的string列表
     */
    public static List<String> getAllLocalHostIP() {
        ArrayList<String> res = new ArrayList<>();
        // res.add("127.0.0.1");
        // 网上找的代码，具体不太懂，可以返回在本机上的所有地址。
        try {
            Enumeration<?> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration<?> nii = ni.getInetAddresses();
                while (nii.hasMoreElements()) {
                    InetAddress ip = (InetAddress) nii.nextElement();
                    if (!ip.getHostAddress().contains(":")) {
                        res.add(ip.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return res;
    }

}
