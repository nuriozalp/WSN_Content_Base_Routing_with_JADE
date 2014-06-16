package agentsensorsystem;

public interface Constants {

    //Valid node address interval
    public static final int MAX_VALID_NODE_ADDRESS = 254;
    public static final int MIN_VALID_NODE_ADDRESS = 0;
    public static final int BROADCAST_ADDRESS = 255;

    //Broadcast ID
    public static final int MAX_BROADCAST_ID = Integer.MAX_VALUE;
    public static final int FIRST_BROADCAST_ID = 0;

    //Sequence Numbers
    public static final int MAX_SEQUENCE_NUMBER = Integer.MAX_VALUE;
    public static final int INVALID_SEQUENCE_NUMBER = -1;
    public static final int UNKNOWN_SEQUENCE_NUMBER = 0;
    public static final int FIRST_SEQUENCE_NUMBER = 1;
    public static final int SEQUENCE_NUMBER_INTERVAL = (Integer.MAX_VALUE / 2);

    // user package type
    public static final byte USER_DATA_PACKET_PDU = 0;

    // user package max size equivalent 54kb
    public static final int MAX_PACKAGE_SIZE = 54000;

    // AODV PDU types
    public static final byte RERR_PDU = 1;
    public static final byte RREP_PDU = 2;
    public static final byte RREQ_PDU = 3;
    public static final byte RREQ_FAILURE_PDU = 4;
    public static final byte FORWARD_ROUTE_CREATED = 5;

    // hello package type
    public static final byte HELLO_PDU = 6;

    public static final byte SINK_SETTING = 7;
    public static final byte SINK_SUB = 8;
    public static final byte SINK_PUB = 9;
    public static final byte TRIGGER_SINK_PUB = 10;
    public static final byte STOP_SINK_SUB = 11;
    public static final byte NODE_DIED = 12;
    //alive time for a route 
    public static final int ROUTE_ALIVETIME = 4000;

    //the time to wait between each hello message sent
    public static final int BROADCAST_INTERVAL = 1000;

    public static final int MAX_NUMBER_OF_RREQ_RETRIES = 2;

    //the amount of time to store a RREQ entry before the entry dies
    public static final int PATH_DESCOVERY_TIME = 3000;
    public static final long serialVersionUID = 1L;
    public static int minWitdh = 10;
    public static int totalsensor = 140;
    public static int defaultHeat = 100;
    public static int defaultLight = 100;
    public static int defaultEnergy = 150;
    public static int noiceTreshold = 100;
    public static int decreaseEnergyRate = 10;
    public static int decreaseEnergyHeatRate = 40;
    public static int decreaseEnergyLightRate = 20;
    public static int decreaseEnergyNoiceRate = 30;
    public static int maxComminicationDistance = 100; //metre
    public static int distanceEachSensorWithEachOther = 50; //metre
    public static int maxX = distanceEachSensorWithEachOther * totalsensor; //metre
    public static int maxY = distanceEachSensorWithEachOther * totalsensor; //metre

}
