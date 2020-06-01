import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;


public class RequestService {
    public RequestService() throws IOException, JSONException {
    }

    static Object GetLastTemp() throws IOException, JSONException {
        JSONObject json = readJsonFromUrl("https://ecothermostat.herokuapp.com/lasttemp.json");
        System.out.println("Ultima temperatura inregistrata " + json.get("temp"));
        return json.get("temp");
    }

    static Object GetMinTemp() throws IOException, JSONException {
        JSONObject json = readJsonFromUrl("https://ecothermostat.herokuapp.com/lastmintemp.json");
        System.out.println("Temperatura minima este " + json.get("mintemp"));
        return json.get("mintemp");
    }

    static Object GetMode() throws IOException, JSONException {
        String mode;
        JSONObject json = readJsonFromUrl("https://ecothermostat.herokuapp.com/lastmode.json");
        if (json.get("auto").toString() == "true"){
            mode = "AUTO";
        }
        else mode="AT WORK";
        return mode;
    }

    static boolean GetDecision() throws JSONException, IOException {
        boolean decision;
        JSONObject json = readJsonFromUrl("https://ecothermostat.herokuapp.com/lastdecision.json");
        if((json.get("decision").equals("ON")))
        {
           decision = true;
        }
        else decision = false;
    return decision;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static void sendONDecision()
            throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("decision[decision]", "ON")
                .build();

        Request request = new Request.Builder()
                .url("https://ecothermostat.herokuapp.com" + "/decisions/5ea54ab5f4b5c800044f5a03")
                .put(formBody)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

    }

    public static void sendOFFDecision()
            throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("decision[decision]", "OFF")
                .build();

        Request request = new Request.Builder()
                .url("https://ecothermostat.herokuapp.com" + "/decisions/5ea54ab5f4b5c800044f5a03")
                .put(formBody)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

    }


}









