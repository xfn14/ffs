package utils;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class NetUtils {
    public static InetAddress getLocalAddress() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for(NetworkInterface netint : Collections.list(nets)) {
            if (!netint.isLoopback() || !netint.isUp()) {
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for(InetAddress inetA : Collections.list(inetAddresses))
                    if (inetA instanceof Inet4Address)
                        return inetA;
            }
        } return null;
    }

    public static byte[] objectToBytes(Object packet) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(packet);
        try{
            return bos.toByteArray();
        } finally {
            oos.flush(); bos.flush();
            oos.close(); bos.close();
        }
    }

    public static Object bytesToObject(byte[] arr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        try (bis; ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        }
    }
}
