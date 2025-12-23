package in.openalgo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        String apiKey = "a4565e952022cbde7142a1f8c005e60d6d18ec868b1aba76911e95ec18cc737a";
        String wsUrl = "ws://127.0.0.1:8765";

        OpenAlgo client = new OpenAlgo.Builder(apiKey)
            .wsUrl(wsUrl)
            .build();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Test 1: quotes
        System.out.println("=== Testing quotes() ===");
        JsonObject quotesResponse = client.quotes("RELIANCE", "NSE");
        System.out.println(gson.toJson(quotesResponse));
        System.out.println();

        // Test 2: holidays
        System.out.println("=== Testing holidays() ===");
        JsonObject holidaysResponse = client.holidays(2025);
        System.out.println(gson.toJson(holidaysResponse));
        System.out.println();

        // Test 3: timings
        System.out.println("=== Testing timings() ===");
        JsonObject timingsResponse = client.timings("2025-12-23");
        System.out.println(gson.toJson(timingsResponse));
        System.out.println();

        // Test 4: multiquotes
        System.out.println("=== Testing multiquotes() ===");
        List<Map<String, String>> symbols = new ArrayList<>();

        Map<String, String> reliance = new HashMap<>();
        reliance.put("symbol", "RELIANCE");
        reliance.put("exchange", "NSE");
        symbols.add(reliance);

        Map<String, String> sbin = new HashMap<>();
        sbin.put("symbol", "SBIN");
        sbin.put("exchange", "NSE");
        symbols.add(sbin);

        Map<String, String> infy = new HashMap<>();
        infy.put("symbol", "INFY");
        infy.put("exchange", "NSE");
        symbols.add(infy);

        JsonObject multiResponse = client.multiquotes(symbols);
        System.out.println(gson.toJson(multiResponse));
        System.out.println();

        // Test 5: optionsmultiorder with NRML
        System.out.println("=== Testing optionsmultiorder (NRML) ===");
        List<Map<String, Object>> legs = new ArrayList<>();

        Map<String, Object> leg1 = new HashMap<>();
        leg1.put("offset", "ATM");
        leg1.put("option_type", "CE");
        leg1.put("action", "BUY");
        leg1.put("quantity", 75);
        leg1.put("product", "NRML");
        legs.add(leg1);

        Map<String, Object> leg2 = new HashMap<>();
        leg2.put("offset", "OTM1");
        leg2.put("option_type", "CE");
        leg2.put("action", "SELL");
        leg2.put("quantity", 75);
        leg2.put("product", "NRML");
        legs.add(leg2);

        JsonObject multiOrderResponse = client.optionsmultiorder("JavaTest", "NIFTY", "NFO", legs, "30DEC25");
        System.out.println(gson.toJson(multiOrderResponse));
        System.out.println();

        // Test 6: WebSocket LTP
        System.out.println("=== Testing WebSocket LTP ===");
        System.out.println("Connecting to: " + wsUrl);

        boolean connected = client.connect();
        System.out.println("Connected: " + connected);
        System.out.println("isAuthenticated: " + client.isAuthenticated());

        if (connected) {
            List<Map<String, String>> instruments = new ArrayList<>();

            Map<String, String> rel = new HashMap<>();
            rel.put("symbol", "RELIANCE");
            rel.put("exchange", "NSE");
            instruments.add(rel);

            Map<String, String> sb = new HashMap<>();
            sb.put("symbol", "SBIN");
            sb.put("exchange", "NSE");
            instruments.add(sb);

            System.out.println("Subscribing to LTP...");
            boolean subscribed = client.subscribeLtp(instruments, data -> {
                System.out.println("[LTP] " + gson.toJson(data));
            });
            System.out.println("Subscribed: " + subscribed);

            System.out.println("Waiting for LTP data (5 seconds)...");
            TimeUnit.SECONDS.sleep(5);

            System.out.println("\nCached LTP Data:");
            System.out.println(gson.toJson(client.getLtp()));

            client.unsubscribeLtp(instruments);
            client.disconnect();
            System.out.println("Disconnected");
        } else {
            System.out.println("Failed to connect to WebSocket. Is the server running?");
        }
    }
}
