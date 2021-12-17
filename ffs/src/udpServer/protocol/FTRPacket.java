package udpServer.protocol;

import java.io.Serializable;

public class FTRPacket implements Serializable {
    private final String prefix;

    public FTRPacket(){
        this.prefix = "FT-Rapid Protocol";
    }

    public String getPrefix() {
        return prefix;
    }
}
