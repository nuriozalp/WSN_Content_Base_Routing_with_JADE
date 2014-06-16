/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsensorsystem;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

public class Sensor implements Serializable {

    int id;
    int heat;
    boolean isGreaterHeat;
    int light;
    boolean isGreaterLight;
    int noice;
    boolean isGreaterNoice;
    boolean isSink;
    Point2D point;
    double energy = Constants.defaultEnergy;

    boolean isAlive = true;
    ArrayList<RoutingTable> routingTableList;

    RequestMessage rreqMessage;

    @Override
    public String toString() {
        String result = "-------sensorId :" + id + " ROUTING TABLE START--------------------------\n";
        for (RoutingTable routingTable : routingTableList) {
            result += "DA : " + routingTable.destinationAddress + " SA : " + routingTable.sourceAddress
                    + " SINK : " + routingTable.sinkAddress + " hopcount : " + routingTable.hopcount + "\n";
        }
        result += "-------sensorId :" + id + " ROUTING TABLE END--------------------------------\n";
        return result;
    }

    public Sensor(int id, double pozX, double pozY) {
        this.id = id;
        this.point = new Point2D.Double(pozX, pozY);
        routingTableList = new ArrayList<RoutingTable>();
    }

    public void setSinkSensor(int heat, int light, int noice,
            boolean isGreaterHeat, boolean isGreaterLight, boolean isGreaterNoice) {

        this.heat = heat;
        this.light = light;
        this.noice = noice;
        this.isSink = true;
        this.isGreaterHeat = isGreaterHeat;
        this.isGreaterLight = isGreaterLight;
        this.isGreaterNoice = isGreaterNoice;

    }

    public void decreaseSubEnergy() {
        if (energy > 0) {
            energy -= Constants.decreaseEnergyRate;
        }
    }

    public void decreasePubEnergy(String type) {
        if (energy > 0) {
            if (type.equals("light")) {
                energy -= Constants.decreaseEnergyLightRate;
            } else if (type.equals("noice")) {
                energy -= Constants.decreaseEnergyNoiceRate;
            } else if (type.equals("heat")) {
                energy -= Constants.decreaseEnergyHeatRate;
            }

        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public double getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getNoice() {
        return noice;
    }

    public void setNoice(int noice) {
        this.noice = noice;
    }

    public Point2D getPoint() {
        return point;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public boolean isIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isIsGreaterHeat() {
        return isGreaterHeat;
    }

    public void setIsGreaterHeat(boolean isGreaterHeat) {
        this.isGreaterHeat = isGreaterHeat;
    }

    public boolean isIsGreaterLight() {
        return isGreaterLight;
    }

    public void setIsGreaterLight(boolean isGreaterLight) {
        this.isGreaterLight = isGreaterLight;
    }

    public boolean isIsGreaterNoice() {
        return isGreaterNoice;
    }

    public void setIsGreaterNoice(boolean isGreaterNoice) {
        this.isGreaterNoice = isGreaterNoice;
    }

    public boolean isIsSink() {
        return isSink;
    }

    public void setIsSink(boolean isSink) {
        this.isSink = isSink;
    }

    public ArrayList<RoutingTable> getRoutingTableList() {
        return routingTableList;
    }

    public void setRoutingTableList(ArrayList<RoutingTable> routingTableList) {
        this.routingTableList = routingTableList;
    }

    public RequestMessage getRreqMessage() {
        return rreqMessage;
    }

    public void setRreqMessage(RequestMessage rreqMessage) {
        this.rreqMessage = rreqMessage;
    }

}
