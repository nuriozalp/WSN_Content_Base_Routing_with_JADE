package agentsensorsystem;

import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import java.util.ArrayList;

public class AgentSensorSystem extends Agent {

    private static final long serialVersionUID = 1L;

    @Override
    public void setup() {
        setAgentList();
        Runtime rt = Runtime.instance();
        ProfileImpl p = new ProfileImpl();
        p.setParameter(Profile.MAIN, "false");

        try {
            AgentContainer ac = rt.createAgentContainer(p);

            for (int i = 0; i < Constants.totalsensor; i++) {
                String str = TimeAgent.class.getName();
                Object[] obj = setArgument(i);

                AgentController a = ac.createNewAgent("" + i, str, obj);
                a.start();
            }

            AgentController triggerAgent
                    = ac.createNewAgent("" + Constants.totalsensor, TriggerAgent.class.getName(), null);
            triggerAgent.start();
            
            AgentController as = ac.createNewAgent("" + Constants.totalsensor+1, AgentSinkSetter.class.getName(), null);
            as.start();
            
        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    public Object[] setArgument(int i) {
        //int id, double pozX, double pozY

        Object[] obj = new Object[7];
        obj[0] = i;
        obj[1] =( i % Constants.minWitdh)*Constants.distanceEachSensorWithEachOther;
        obj[2] = (i / Constants.minWitdh)*Constants.distanceEachSensorWithEachOther;
        return obj;
    }

    public void setAgentList() {
        for (int i = 0; i < Constants.totalsensor; i++) {
            ArrayList<Integer> neigbourList = new ArrayList<Integer>();
            for (int k = 0; k < Constants.totalsensor; k++) {
                if (i==k) {
                    continue;
                }
                float dist = (float) Math.sqrt(
                        Math.pow(( i % Constants.minWitdh)*Constants.distanceEachSensorWithEachOther - 
                                ( k % Constants.minWitdh)*Constants.distanceEachSensorWithEachOther, 2)
                        + Math.pow((i / Constants.minWitdh)*Constants.distanceEachSensorWithEachOther - 
                                (k / Constants.minWitdh)*Constants.distanceEachSensorWithEachOther, 2));

                if (dist < Constants.maxComminicationDistance) {
                    if (!neigbourList.contains(k)) {
                         neigbourList.add(k);
                    }
                   
                }
            }
            AgentList.agentList.put(i, neigbourList);
        }
    }
}
