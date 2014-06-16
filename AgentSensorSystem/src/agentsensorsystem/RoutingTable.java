package agentsensorsystem;

import java.awt.geom.Point2D;

public class RoutingTable {

    int seqNumber;
    int sourceAddress;
    int destinationAddress;
    int sinkAddress=-1;
    int hopcount;
    double heat;
    boolean isGreaterHeat;
    double light;
    boolean isGreaterLight;
    double noice;
    boolean isGreaterNoice;
    double energy;

    public int getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(int sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public int getHopcount() {
        return hopcount;
    }

    public void setHopcount(int hopcount) {
        this.hopcount = hopcount;
    }

    public double getHeat() {
        return heat;
    }

    public void setHeat(double heat) {
        this.heat = heat;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }

    public double getNoice() {
        return noice;
    }

    public void setNoice(double noice) {
        this.noice = noice;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

}
