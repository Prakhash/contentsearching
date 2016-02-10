package network;

import java.util.Comparator;

/**
* Created by Ruba on 1/8/2016.
*/
public class Neighbor implements Comparator<Neighbor> {

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
    public int compare(Neighbor o1, Neighbor o2) {
        return o1.toString().compareTo(o2.toString());
    }
}
