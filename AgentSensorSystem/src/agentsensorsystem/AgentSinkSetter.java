package agentsensorsystem;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Properties;
import java.util.Random;
public class AgentSinkSetter extends Agent {

    public static int counterSq = 1;
    MessageTemplate template
            = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);
    ACLMessage reply;
    Random rn = new Random();

    protected void setup() {

        System.out.println("SinkSetter Agent Started " + getLocalName() + " started.");

        addBehaviour(new Behaviour(this) {

            @Override
            public void action() {
                int distanceCount = 24;
                int totalSink = Constants.totalsensor / distanceCount;
                for (int i = 1; i <= totalSink; i++) {
                    int sourceAddress = Constants.totalsensor - i * distanceCount;

                    //double heat, double light, double noice
                    Properties params = new Properties();
                    params.setProperty("heat", rn.nextInt(200)+"");
                    params.setProperty("isGreaterHeat", "" + rn.nextBoolean());
                    params.setProperty("light", "" + rn.nextInt(200));
                    params.setProperty("isGreaterLight", "" + rn.nextBoolean());
                    params.setProperty("noice", "" + rn.nextInt(100));
                    params.setProperty("isGreaterNoice", "" + rn.nextBoolean());

                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(new AID("" + sourceAddress, AID.ISLOCALNAME));
                    msg.setLanguage("English");
                    msg.setOntology("" + Constants.SINK_SETTING);
                    msg.setAllUserDefinedParameters(params);
                    send(msg);
                }
                myAgent.doDelete();

            }

            @Override
            public boolean done() {
            return false;
            }
        });

    }
}
