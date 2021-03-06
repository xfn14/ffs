package utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class SecurityUtils {
    /**
     * Método que calcula o checksum dos dados
     * @param bytes array de bytes
     * @return checksum
     */
    public static long getCRC32Checksum(byte[] bytes){
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }

    /**
     * método que verifica se a string recebida (prefixo interpretado) corresponde ao suposto
     * @param str prefixo interpretado
     * @return string
     */
    public static String getStringSHA1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(str.getBytes(StandardCharsets.UTF_8));
            return String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
