import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import javax.lang.model.util.Elements;
import javax.swing.text.Document;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;

public class ActionAgent extends Agent {

    private static final long serialVersionUID = 1L;
    boolean AState = false; //action state.

    @Override
    protected void setup() {
// TODO Auto-generated method stub
        addBehaviour(new CyclicBehaviour() {
            private static final long serialVersionUID = 1L;

            @Override
            public void action() {
// TODO Auto-generated method stub


                ACLMessage RMsg = receive(); //receive Decision message.
                if (RMsg != null) { //if there is a message.
// process it.
//if the decision is to "turn on".
// and last state is "off".
                    if (RMsg.getContent().equals("TurnOn") && !AState) {


                        System.out.println("Action Agent: Sending TURN ON request to HEATER");
                        System.out.println("----------------------------------------------");

                        AState = true; //CS is now "on".
                        // IF MODE IS AUTO -> NON STOP, ELSE START AT 15:30
                        //POST decision[state] = true TO ecothermostat.herokuapps.com/decision
                        //SEND ARDUINO SIGNAL TO TURN ON LED that simulates the heater
                        try {
                            RequestService.sendONDecision();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (RMsg.getContent().equals("TurnOff") && AState) {
                        System.out.println("Action Agent: Sending TURN OFF request to HEATER");
                        System.out.println("----------------------------------------------");
                        AState = false; // CS is now "off".
                        //SEND ARDUINO SIGNAL TO TURN OFF LED THAT SIMULATES THE HEATER
                        try {
                            RequestService.sendOFFDecision();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
