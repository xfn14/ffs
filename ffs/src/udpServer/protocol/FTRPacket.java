package udpServer.protocol;

import utils.SecurityUtils;

import java.io.Serializable;

public class FTRPacket implements Serializable {
    private final String hash;

    public FTRPacket(String prefix){
        this.hash = SecurityUtils.getStringSHA1(prefix);
    }

    public String getHash() {
        return this.hash;
    }
}
