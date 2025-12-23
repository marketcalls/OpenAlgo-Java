package in.openalgo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Test for WebSocket Feed API.
 *
 * Usage: java -cp target/openalgo-1.0.0.jar in.openalgo.WebSocketTest
 *
 * This test demonstrates:
 * - Connecting to WebSocket
 * - Subscribing to LTP, Quote, and Depth data
 * - Receiving real-time updates via callbacks
 * - Getting cached data
 */
public class WebSocketTest {

    public static void main(String[] args) throws InterruptedException {
        // Replace with your API key and WebSocket URL
        String apiKey = "your-api-key-here";
        String wsUrl = "ws://127.0.0.1:8765";  // or "wss://demo.openalgo.in/ws" for SSL

        // Create OpenAlgo client with custom WebSocket URL
        OpenAlgo client = new OpenAlgo.Builder(apiKey)
            .wsUrl(wsUrl)
            .build();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Create instrument list
        List<Map<String, String>> instruments = new ArrayList<>();

        Map<String, String> reliance = new HashMap<>();
        reliance.put("symbol", "RELIANCE");
        reliance.put("exchange", "NSE");
        instruments.add(reliance);

        Map<String, String> sbin = new HashMap<>();
        sbin.put("symbol", "SBIN");
        sbin.put("exchange", "NSE");
        instruments.add(sbin);

        // Test connection
        System.out.println("=== Testing connect() ===");
        boolean connected = client.connect();
        System.out.println("Connected: " + connected);
        System.out.println("isConnected: " + client.isConnected());
        System.out.println("isAuthenticated: " + client.isAuthenticated());
        System.out.println();

        if (!connected) {
            System.out.println("Failed to connect. Make sure WebSocket server is running.");
            return;
        }

        // Test LTP subscription
        System.out.println("=== Testing subscribeLtp() ===");
        System.out.println("Subscribing to: " + gson.toJson(instruments));
        System.out.println();

        boolean ltpSubscribed = client.subscribeLtp(instruments, data -> {
            System.out.println("[LTP Callback] Received:");
            System.out.println(gson.toJson(data));
            System.out.println();
        });
        System.out.println("LTP Subscribed: " + ltpSubscribed);
        System.out.println();

        // Wait for data
        System.out.println("Waiting for LTP data (5 seconds)...");
        TimeUnit.SECONDS.sleep(5);

        // Get cached LTP data
        System.out.println("=== Testing getLtp() ===");
        Map<String, Object> ltpData = client.getLtp();
        System.out.println("Cached LTP Data:");
        System.out.println(gson.toJson(ltpData));
        System.out.println();

        // Unsubscribe LTP
        System.out.println("=== Testing unsubscribeLtp() ===");
        boolean ltpUnsubscribed = client.unsubscribeLtp(instruments);
        System.out.println("LTP Unsubscribed: " + ltpUnsubscribed);
        System.out.println();

        // Test Quote subscription
        System.out.println("=== Testing subscribeQuote() ===");
        boolean quoteSubscribed = client.subscribeQuote(instruments, data -> {
            System.out.println("[Quote Callback] Received:");
            System.out.println(gson.toJson(data));
            System.out.println();
        });
        System.out.println("Quote Subscribed: " + quoteSubscribed);
        System.out.println();

        // Wait for data
        System.out.println("Waiting for Quote data (5 seconds)...");
        TimeUnit.SECONDS.sleep(5);

        // Get cached Quote data
        System.out.println("=== Testing getQuotes() ===");
        Map<String, Object> quoteData = client.getQuotes();
        System.out.println("Cached Quote Data:");
        System.out.println(gson.toJson(quoteData));
        System.out.println();

        // Unsubscribe Quote
        System.out.println("=== Testing unsubscribeQuote() ===");
        boolean quoteUnsubscribed = client.unsubscribeQuote(instruments);
        System.out.println("Quote Unsubscribed: " + quoteUnsubscribed);
        System.out.println();

        // Test Depth subscription
        System.out.println("=== Testing subscribeDepth() ===");
        boolean depthSubscribed = client.subscribeDepth(instruments, data -> {
            System.out.println("[Depth Callback] Received:");
            System.out.println(gson.toJson(data));
            System.out.println();
        });
        System.out.println("Depth Subscribed: " + depthSubscribed);
        System.out.println();

        // Wait for data
        System.out.println("Waiting for Depth data (5 seconds)...");
        TimeUnit.SECONDS.sleep(5);

        // Get cached Depth data
        System.out.println("=== Testing getDepth() ===");
        Map<String, Object> depthData = client.getDepth();
        System.out.println("Cached Depth Data:");
        System.out.println(gson.toJson(depthData));
        System.out.println();

        // Unsubscribe Depth
        System.out.println("=== Testing unsubscribeDepth() ===");
        boolean depthUnsubscribed = client.unsubscribeDepth(instruments);
        System.out.println("Depth Unsubscribed: " + depthUnsubscribed);
        System.out.println();

        // Disconnect
        System.out.println("=== Testing disconnect() ===");
        client.disconnect();
        System.out.println("Disconnected");
        System.out.println("isConnected: " + client.isConnected());
        System.out.println("isAuthenticated: " + client.isAuthenticated());
    }
}
