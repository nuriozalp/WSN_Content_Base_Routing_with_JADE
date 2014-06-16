package agentsensorsystem;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TriggerAgent extends Agent {
    
    public static int counterSq = 1;
    
    Random rn = new Random();
    boolean oneSend = false;
    
    protected void setup() {
        
        System.out.println("Trigger Agent Started " + getLocalName() + " started.");
        
        addBehaviour(new TickerBehaviour(this, 20000) {
            
            @Override
            public void onTick() {
                try {
                    
                    int sourceAddress = rn.nextInt(Constants.totalsensor);

                    //double heat, double light, double noice
                    Properties params = new Properties();
                    int value = rn.nextInt(3);
                    switch (value) {
                        case 0:
                            params.setProperty("heat", rn.nextInt(250)+"");
                            break;
                        case 1:
                            params.setProperty("light", "" + rn.nextInt(250));
                            break;
                        case 2:
                            params.setProperty("noice", "" + rn.nextInt(150));
                            break;
                    }
                    
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(new AID("" + sourceAddress, AID.ISLOCALNAME));
                    msg.setLanguage("English");
                    msg.setOntology("" + Constants.TRIGGER_SINK_PUB);
                    msg.setAllUserDefinedParameters(params);
                    send(msg);
                    
                } catch (Exception ex) {
                    Logger.getLogger(TimeAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        addBehaviour(new WakerBehaviour(this, 30000) {
            @Override
            protected void handleElapsedTimeout() {
                for (int i = 0; i < Constants.totalsensor; i++) {
                    ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                    msg2.addReceiver(new AID("" + i, AID.ISLOCALNAME));
                    msg2.setLanguage("English");
                    msg2.setOntology("" + Constants.STOP_SINK_SUB);
                    msg2.setContent("stop");
                    send(msg2);
                }
            }
        });
    }
}
