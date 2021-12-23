package utils;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class NetUtils {

    /**
     * Método que retorna o ip do computador
     * @return ip
     * @throws SocketException
     */
    public static InetAddress getLocalAddress() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for(NetworkInterface net : Collections.list(nets)) {
            if (!net.isLoopback() || !net.isUp()) {
                Enumeration<InetAddress> addrs = net.getInetAddresses();
                for(InetAddress addr : Collections.list(addrs))
                    if (addr instanceof Inet4Address)
                        return addr;
            }
        } return null;
    }

    /**
     * Método que converte um objeto num array de bytes
     * @param o objeto a ser convertido
     * @return array de bytes
     * @throws IOException
     */
    public static byte[] objectToBytes(Object o) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);
        try{
            return bos.toByteArray();
        } finally {
            oos.flush(); bos.flush();
            oos.close(); bos.close();
        }
    }

    /**
     * Método que converte um array de bytes num objeto
     * @param arr array de bytes a ser convertido
     * @return objeto
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object bytesToObject(byte[] arr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        try (bis; ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        }
    }
}
