package network;

import java.util.Comparator;
import java.util.Objects;

/**
* Created by Ruba on 1/8/2016.
*/
public class Neighbor{

    private String ipAddress;
    private int portNumber;

    public Neighbor(String ipAddress, int portNumber){
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String toString(){
        return ipAddress + ":" + portNumber;
    }
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    @Override
    public boolean equals(Object o1) {

        Neighbor temp = (Neighbor) o1;
        return this.toString().equals(temp.toString());
    }
}
