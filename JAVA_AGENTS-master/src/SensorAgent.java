import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONException;

import java.io.IOException;

public class SensorAgent extends Agent {
    double minTemp;
    double lastTemp;
    boolean TSens; //thermal sensor state.
    private static final long serialVersionUID = 1L;

    @Override
    protected void setup() {
// First Ticker Behavior to simulate reading from
//Motion sensor each 2 Seconds.
        addBehaviour(new TickerBehaviour(this, 10000){
            private static final long serialVersionUID = 1L;
            protected void onTick() {

                try {
                     minTemp = (double) RequestService.GetMinTemp();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                try {
                    lastTemp = (double) RequestService.GetLastTemp();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                ACLMessage MSMsg = new ACLMessage(ACLMessage.INFORM);
                MSMsg.addReceiver(new AID("DAgent", AID.ISLOCALNAME));
                try {
                    System.out.println("Modul de functionare este: " +RequestService.GetMode());

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                if (lastTemp < minTemp) {
                    TSens = true; //there is a thermal reading.
                    System.out.println("Temperatura actuala: " +lastTemp+ " este mai mica decat temperatura minima setata: " + minTemp);
                    MSMsg.setContent("T1"); //true
                } else if (lastTemp >= minTemp) {
                    TSens = false; //there is no thermal reading.
                    System.out.println("Temperatura actuala: " +lastTemp+ " este mai mare decat temperatura minima setata: " + minTemp);

                    MSMsg.setContent("T0"); //false
                }
//
                send(MSMsg);
                System.out.println("Sensing Agent: Temperature Sensor= " + TSens);

                try {
                    if(RequestService.GetDecision()){
                        System.out.println("Centrala este pornita");
                    }
                    else System.out.println("Centrala este oprita");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("----------------------------------------------");

            }
        });

    }
}