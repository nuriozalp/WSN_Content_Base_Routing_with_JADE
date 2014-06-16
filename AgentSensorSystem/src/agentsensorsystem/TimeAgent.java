package agentsensorsystem;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Properties;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This example shows the usage of the behaviours that allow scheduling actions
 * at a given point in time: <code>WakerBehaviour</code> and
 * <code>TickerBehaviour</code>. More in details this agent executes a
 * <code>TickerBehaviour</code> that prints the agent name every second and a
 * <code>WakerBehaviour</code> ] that kill the agent after 10 seconds.
 *
 * @author Giovanni Caire - TILAB
 */
public class TimeAgent extends Agent {

    public Sensor sensor;
    MessageTemplate template
            = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);
    public boolean stopSub = false;

    @Override
    protected void setup() {
        Object[] arguments = this.getArguments();

        //int id, double heat, double light, double noice,double pozX,double pozY
        sensor = new Sensor(
                Integer.parseInt(arguments[0].toString()),
                Integer.parseInt(arguments[1].toString()),
                Integer.parseInt(arguments[2].toString())
        );
        System.out.println("Sensor Agent " + getLocalName() + " started.");

        // mesaj gönderiyor
        addBehaviour(new TickerBehaviour(this, 100) {
            @Override
            protected void onTick() {

                //öncelikle trigger mesajı kontolu başlat
                ACLMessage msg = blockingReceive();

                if (msg != null) {

                    try {
                        if (!stopSub) {

                            if (msg.getOntology().equalsIgnoreCase("" + Constants.STOP_SINK_SUB)) {
                                stopSub = true;
                            } else if (msg.getOntology().equalsIgnoreCase("" + Constants.SINK_SETTING)) {
                                Properties params = msg.getAllUserDefinedParameters();
                                loadSinkParameter(params);
                                ArrayList<Integer> neighbourList = AgentList.agentList.get(sensor.getId());
                                for (Integer dest_id : neighbourList) {
                                    Properties rreqparams = setSinkMsgParameter(dest_id, 0);

                                    ACLMessage sendMsg = new ACLMessage(ACLMessage.INFORM);
                                    sendMsg.addReceiver(new AID("" + dest_id, AID.ISLOCALNAME));
                                    sendMsg.setLanguage("English");
                                    sendMsg.setOntology("" + Constants.SINK_SUB);
                                    try {
                                        sendMsg.setAllUserDefinedParameters(rreqparams);
                                    } catch (Exception ex) {
                                        Logger.getLogger(TimeAgent.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                    send(sendMsg);
                                    sensor.decreaseSubEnergy();
                                    descreaseSubEnergy();
                                }
                                System.out.println("Sensor Agent " + getLocalName() + " " + Constants.SINK_SETTING);
                            } else if (msg.getOntology().equalsIgnoreCase("" + Constants.SINK_SUB)) {
                                Properties params = msg.getAllUserDefinedParameters();
                                RequestMessage sink_msg = loadSinkSubMessageParameter(params);
                                if (sink_msg.sinkAddress != sensor.getId()) {

                                    boolean isExist = false;
                                    boolean isGreaterHop = false;
                                    int index = -1;
                                    for (int i = 0; i < sensor.routingTableList.size(); i++) {
                                        if (sink_msg.sourceAddress == sensor.routingTableList.get(i).destinationAddress && sensor.routingTableList.get(i).sinkAddress == sink_msg.sinkAddress) {
//                                       
                                            isExist = true;
                                            break;

                                        } else {
                                            isExist = false;
                                        }
                                    }

//                                }
                                    if (!isExist) {
                                        RoutingTable rt = setRoutingTable(sink_msg);
                                        ArrayList<Integer> neighbourList = AgentList.agentList.get(sensor.getId());
                                        for (Integer dest_id : neighbourList) {
                                            Properties rreqparams = setSinkResendMsgParameter(dest_id, sink_msg);
                                            ACLMessage sendMsg = new ACLMessage(ACLMessage.INFORM);
                                            sendMsg.addReceiver(new AID("" + dest_id, AID.ISLOCALNAME));
                                            sendMsg.setLanguage("English");
                                            sendMsg.setOntology("" + Constants.SINK_SUB);
                                            try {
                                                sendMsg.setAllUserDefinedParameters(rreqparams);
                                            } catch (Exception ex) {
                                                Logger.getLogger(TimeAgent.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            send(sendMsg);
                                            descreaseSubEnergy();
                                        }
                                        System.out.println("isExist Agent " + getLocalName() + " Constants.SINK_SUB");

                                    }
                                }
                            }
                            System.out.println(sensor.toString());
                        }
                        if (msg.getOntology().equalsIgnoreCase("" + Constants.NODE_DIED)) {
                            ArrayList<Integer> deletedList = new ArrayList<Integer>();
                            Integer sensorId = Integer.parseInt(msg.getContent());
                            for (int i = 0; i < sensor.routingTableList.size(); i++) {
                                if (sensor.routingTableList.get(i).destinationAddress == sensorId) {
                                    deletedList.add(i);
                                }
                            }
                            for (Integer de : deletedList) {
                                boolean remove = sensor.routingTableList.remove(de);
                            }

                        } else if (msg.getOntology().equalsIgnoreCase("" + Constants.TRIGGER_SINK_PUB)) {

                            Properties params = msg.getAllUserDefinedParameters();
                            publishDataMsg(params);
                            Calendar mydate = Calendar.getInstance();
                            mydate.setTimeInMillis(msg.getPostTimeStamp() * 1000);

                            System.out.println(" START Time :" + mydate.getTime().getSeconds());

                        } else if (msg.getOntology().equalsIgnoreCase("" + Constants.SINK_PUB)) {
                            //  System.out.println(sensor.id+ " qs: "+this.myAgent.getQueueSize());
                            try {
                                Properties params = msg.getAllUserDefinedParameters();
                                int sinkAddress = Integer.parseInt(params.getProperty("sinkAddress"));

                                if (sinkAddress == sensor.id) {
                                    Calendar mydate = Calendar.getInstance();
                                    mydate.setTimeInMillis(msg.getPostTimeStamp() * 1000);

                                    System.out.println(" END Time :" + mydate.getTime().getSeconds());
                                    System.out.println("*************HEDEFE ULAŞTI***************");
                                } else {
                                    RoutingTable sendPublishedDataMsg = sendPublishedDataMsg(sinkAddress);

                                    params.setProperty("sinkAddress", "" + sendPublishedDataMsg.sinkAddress);
                                    ACLMessage sendMsg = new ACLMessage(ACLMessage.INFORM);
                                    sendMsg.addReceiver(new AID("" + sendPublishedDataMsg.destinationAddress, AID.ISLOCALNAME));
                                    sendMsg.setLanguage("English");
                                    sendMsg.setOntology("" + Constants.SINK_PUB);
                                    try {
                                        sendMsg.setAllUserDefinedParameters(params);
                                        System.out.println("sinkAddress : " + sinkAddress + " from : " + sensor.id + " destinationAddress : " + sendPublishedDataMsg.destinationAddress);
                                    } catch (Exception ex) {
                                        Logger.getLogger(TimeAgent.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    send(sendMsg);
                                    if (params.containsKey("light")) {
                                        descreasePubEnergy("light");
                                    } else if (params.containsKey("noice")) {
                                        descreasePubEnergy("noice");
                                    } else if (params.containsKey("heat")) {
                                        descreasePubEnergy("heat");
                                    }

                                }
                            } catch (Exception e) {
                            }

                        }

                        //  System.out.println(sensor.toString());
                    } catch (Exception ex) {
                        Logger.getLogger(TimeAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

            public  void publishDataMsg(Properties params) throws NumberFormatException {
                RoutingTable rTable = new RoutingTable();
                String type = "";
                for (Object object : params.keySet()) {
                    switch (object.toString()) {
                        case "light":

                            for (RoutingTable routingTable : sensor.routingTableList) {
                                if (routingTable.light > Integer.parseInt(params.getProperty("light"))) {
                                    if (!routingTable.isGreaterLight) {
                                        if (rTable.hopcount > routingTable.hopcount) {
                                            rTable = routingTable;
                                        }
                                    }
                                } else {
                                    if (routingTable.isGreaterLight) {
                                        rTable = routingTable;
                                        if (rTable.hopcount > routingTable.hopcount) {
                                            rTable = routingTable;
                                        }
                                    }
                                }
                            }
                            type = "light";
                            break;
                        case "heat":
                            for (RoutingTable routingTable : sensor.routingTableList) {
                                if (routingTable.heat > Integer.parseInt(params.getProperty("heat"))) {
                                    if (!routingTable.isGreaterHeat) {
                                        if (rTable.hopcount > routingTable.hopcount) {
                                            rTable = routingTable;
                                        }
                                    }
                                } else {
                                    if (routingTable.isGreaterHeat) {
                                        rTable = routingTable;
                                        if (rTable.hopcount > routingTable.hopcount) {
                                            rTable = routingTable;
                                        }
                                    }
                                }
                            }
                            type = "heat";
                            break;
                        case "noice":

                            for (RoutingTable routingTable : sensor.routingTableList) {
                                if (routingTable.noice > Integer.parseInt(params.getProperty("noice"))) {
                                    if (!routingTable.isGreaterNoice) {
                                        if (rTable.hopcount > routingTable.hopcount) {
                                            rTable = routingTable;
                                        }
                                    }
                                } else {
                                    if (routingTable.isGreaterNoice) {
                                        rTable = routingTable;
                                        if (rTable.hopcount > routingTable.hopcount) {
                                            rTable = routingTable;
                                        }
                                    }
                                }
                            }
                            type = "noice";
                            break;
                    }
                    System.out.println("MESSAGE TRIGGERED - TYPE :" + type + " value :" + params.getProperty(type));
                }

                if (rTable.hopcount > 0) {
                    params.setProperty("sinkAddress", "" + rTable.sinkAddress);
                    ACLMessage sendMsg = new ACLMessage(ACLMessage.INFORM);
                    sendMsg.addReceiver(new AID("" + rTable.destinationAddress, AID.ISLOCALNAME));
                    sendMsg.setLanguage("English");
                    sendMsg.setOntology("" + Constants.SINK_PUB);
                    try {
                        sendMsg.setAllUserDefinedParameters(params);
                    } catch (Exception ex) {
                        Logger.getLogger(TimeAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    send(sendMsg);
                    descreasePubEnergy(type);
                } else {
                    System.out.println("THERE IS NO SUITABLE SINK");
                }
            }

            public  RoutingTable sendPublishedDataMsg(int sinkAddress) throws NumberFormatException {
                RoutingTable rTable = new RoutingTable();
                boolean first = false;
                for (RoutingTable routingTable : sensor.routingTableList) {
                    if (routingTable.sinkAddress == sinkAddress) {
                        if (!first) {
                            rTable = routingTable;
                            first = true;
                        }

                        if (routingTable.hopcount < rTable.hopcount) {
                            rTable = routingTable;
                        }
                    }
                }

                return rTable;
            }

            private  RoutingTable setRoutingTable(RequestMessage sink_msg) {
                RoutingTable rt = new RoutingTable();
                rt.destinationAddress = sink_msg.sourceAddress;
                rt.seqNumber = sink_msg.seqNumber;
                rt.sinkAddress = sink_msg.sinkAddress;
                rt.sourceAddress = sensor.getId();
                rt.heat = sink_msg.heat;
                rt.hopcount = ++sink_msg.hopcount;
                rt.isGreaterHeat = sink_msg.isGreaterHeat;
                rt.isGreaterLight = sink_msg.isGreaterLight;
                rt.isGreaterNoice = sink_msg.isGreaterNoice;
                rt.light = sink_msg.light;
                rt.noice = sink_msg.noice;
                sensor.routingTableList.add(rt);
                return rt;
            }

            private  Properties setSinkMsgParameter(Integer dest_id, Integer hopcount) {
                Properties rreqparams = new Properties();
                rreqparams.setProperty("sourceAddress", "" + sensor.getId());
                rreqparams.setProperty("destinationAddress", "" + dest_id);
                rreqparams.setProperty("sinkAddress", "" + sensor.getId());
                rreqparams.setProperty("hopcount", "" + hopcount);
                rreqparams.setProperty("heat", "" + sensor.heat);
                rreqparams.setProperty("isGreaterHeat", "" + sensor.isGreaterHeat);
                rreqparams.setProperty("light", "" + sensor.light);
                rreqparams.setProperty("isGreaterLight", "" + sensor.isGreaterLight);
                rreqparams.setProperty("noice", "" + sensor.noice);
                rreqparams.setProperty("isGreaterNoice", "" + sensor.isGreaterNoice);
                rreqparams.setProperty("energy", "" + sensor.energy);
                rreqparams.setProperty("isGreaterHeat", "" + sensor.isGreaterHeat);
                rreqparams.setProperty("seqNumber", "" + sensor.getId());
                return rreqparams;
            }

            private  Properties setSinkResendMsgParameter(Integer dest_id, RequestMessage ms) {
                Properties rreqparams = new Properties();
                rreqparams.setProperty("sourceAddress", "" + sensor.getId());
                rreqparams.setProperty("destinationAddress", "" + dest_id);
                rreqparams.setProperty("sinkAddress", "" + ms.sinkAddress);
                rreqparams.setProperty("hopcount", "" + ms.hopcount);
                rreqparams.setProperty("heat", "" + ms.heat);
                rreqparams.setProperty("isGreaterHeat", "" + ms.isGreaterHeat);
                rreqparams.setProperty("light", "" + ms.light);
                rreqparams.setProperty("isGreaterLight", "" + ms.isGreaterLight);
                rreqparams.setProperty("noice", "" + ms.noice);
                rreqparams.setProperty("isGreaterNoice", "" + ms.isGreaterNoice);
                rreqparams.setProperty("isGreaterHeat", "" + ms.isGreaterHeat);
                rreqparams.setProperty("seqNumber", "" + ms.sinkAddress);
                return rreqparams;
            }

            public  RequestMessage loadSinkSubMessageParameter(Properties params) throws NumberFormatException {
                RequestMessage ms = new RequestMessage();
                try {

                    ms.isGreaterHeat = Boolean.parseBoolean(params.getProperty("isGreaterHeat"));
                    ms.light = Integer.parseInt(params.getProperty("light"));
                    ms.isGreaterLight = Boolean.parseBoolean(params.getProperty("destSeqNum"));
                    ms.noice = Integer.parseInt(params.getProperty("noice"));
                    ms.isGreaterNoice = Boolean.parseBoolean(params.getProperty("isGreaterNoice"));
                    ms.heat = Integer.parseInt(params.getProperty("heat"));
                    ms.destinationAddress = Integer.parseInt(params.getProperty("destinationAddress"));
                    ms.sourceAddress = Integer.parseInt(params.getProperty("sourceAddress"));
                    ms.sinkAddress = Integer.parseInt(params.getProperty("sinkAddress"));
                    ms.hopcount = Integer.parseInt(params.getProperty("hopcount"));
                    ms.seqNumber = Integer.parseInt(params.getProperty("seqNumber"));

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                return ms;

            }

            public void loadSinkParameter(Properties params) throws NumberFormatException {
                sensor.isGreaterHeat = Boolean.parseBoolean(params.getProperty("isGreaterHeat"));
                sensor.light = Integer.parseInt(params.getProperty("light"));
                sensor.isGreaterLight = Boolean.parseBoolean(params.getProperty("destSeqNum"));
                sensor.noice = Integer.parseInt(params.getProperty("noice"));
                sensor.isGreaterNoice = Boolean.parseBoolean(params.getProperty("isGreaterNoice"));
                sensor.heat = Integer.parseInt(params.getProperty("heat"));

            }

            public  void descreaseSubEnergy() {
                if (sensor.getEnergy() < 1) {

                    for (RoutingTable routingTable : sensor.routingTableList) {
                        if (sensor.id != routingTable.destinationAddress) {
                            ACLMessage sendMsg = new ACLMessage(ACLMessage.INFORM);
                            sendMsg.addReceiver(new AID("" + routingTable.destinationAddress, AID.ISLOCALNAME));
                            sendMsg.setLanguage("English");
                            sendMsg.setOntology("" + Constants.NODE_DIED);
                            sendMsg.setContent("" + sensor.id);
                            send(sendMsg);
                        }
                    }
                    System.out.println("ENERGY FINISHED BYE ID : " + sensor.id);
                    myAgent.doDelete();
                    sensor.setIsAlive(false);

                }
            }

            public  void descreasePubEnergy(String type) {
                sensor.decreasePubEnergy(type);
                if (sensor.getEnergy() < 11) {
                    for (RoutingTable routingTable : sensor.routingTableList) {
                        if (sensor.id != routingTable.destinationAddress) {
                            ACLMessage sendMsg = new ACLMessage(ACLMessage.INFORM);
                            sendMsg.addReceiver(new AID("" + routingTable.destinationAddress, AID.ISLOCALNAME));
                            sendMsg.setLanguage("English");
                            sendMsg.setOntology("" + Constants.NODE_DIED);
                            sendMsg.setContent("" + sensor.id);
                            send(sendMsg);
                        }
                    }
                    System.out.println("ENERGY FINISHED BYE ID : " + sensor.id);
                    myAgent.doDelete();
                    sensor.setIsAlive(false);

                }
            }
        }
        );

    }
}
