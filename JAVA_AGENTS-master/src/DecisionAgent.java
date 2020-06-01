import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;

public class DecisionAgent extends Agent {

    private static final long serialVersionUID = 1L;
    boolean TSens; //thermal sensor state.
    boolean DState = false; //decision state.

    int currentHour = LocalDateTime.now().getHour();
    LocalDateTime nextMorningDate = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
            LocalDateTime.now().getDayOfMonth() + 1, 8, 0,
            0);

    LocalDateTime currentDate = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
            LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(),
            LocalDateTime.now().getSecond());

    @Override
    protected void setup() {
// TODO Here goes how this agent behave.
// cyclic behavior to receive sensors state from Sensing Agent.
        addBehaviour(new CyclicBehaviour() {
            private static final long serialVersionUID = 1L;

            @Override
            public void action() {

                try {
                    if (RequestService.GetMode() == "AUTO" || (RequestService.GetMode() == "AT WORK"
                            && currentHour >= 16 && currentDate.isBefore(nextMorningDate))) {
                        ACLMessage RMsg = receive(); //receive data.
                        if (RMsg != null) { //check message.
                            //process message content.
                            if (RMsg.getContent().equals("T1")) {
                                TSens = true;
                            } else if (RMsg.getContent().equals("T0")) {
                                TSens = false;
                            }
                        }
                        //if reading ispositive.
                        //and the last action is to "turn off".
                        if (TSens && !DState) {
                            ACLMessage AMsg = new ACLMessage(ACLMessage.INFORM);
                            AMsg.addReceiver(new AID("AAgent", AID.ISLOCALNAME));
                            DState = true; //change it to "turn on".
                            AMsg.setContent("TurnOn"); // turn on the CS.
                            System.out.println("Decision Agent: Turn ON Heater");
                            send(AMsg); //send the message.
                        }
                        // any other sensor states that may change decision to "turn off".
                        else if (!TSens && DState) { //and if last action is "turn on".
                            ACLMessage AMsg = new ACLMessage(ACLMessage.INFORM);
                            AMsg.addReceiver(new AID("AAgent", AID.ISLOCALNAME));
                            DState = false; //change it to "turn off".
                            AMsg.setContent("TurnOff"); // turn off the CS.
                            System.out.println("Decision Agent: Turn OFF Heater");
                            send(AMsg); //send the message.
                        }

                    }
                    else if(DState){
                        ACLMessage AMsg = new ACLMessage(ACLMessage.INFORM);
                        AMsg.addReceiver(new AID("AAgent", AID.ISLOCALNAME));
                        DState=false;
                        AMsg.setContent("TurnOff");
                        System.out.println("Decision Agent: Turn OFF Heater because AT WORK mode started");
                        send(AMsg);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
