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

        // Test 2: multiquotes
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

        // Test 3: WebSocket LTP
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
