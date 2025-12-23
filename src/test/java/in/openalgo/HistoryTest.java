package in.openalgo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Test for History API.
 *
 * Usage: java -cp target/openalgo-1.0.0.jar in.openalgo.HistoryTest
 */
public class HistoryTest {

    public static void main(String[] args) {
        // Replace with your API key
        String apiKey = "your-api-key-here";

        // Create OpenAlgo client
        OpenAlgo client = new OpenAlgo(apiKey);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Test history with 5-minute interval
        System.out.println("=== Testing history() - 5 minute interval ===");
        System.out.println("Request: history(\"SBIN\", \"NSE\", \"5m\", \"2024-12-01\", \"2024-12-12\")");
        System.out.println();

        JsonObject response = client.history("SBIN", "NSE", "5m", "2024-12-01", "2024-12-12");
        System.out.println("Response:");
        System.out.println(gson.toJson(response));
        System.out.println();

        // Test history with daily interval
        System.out.println("=== Testing history() - Daily interval ===");
        System.out.println("Request: history(\"RELIANCE\", \"NSE\", \"D\", \"2024-11-01\", \"2024-12-12\")");
        System.out.println();

        JsonObject dailyResponse = client.history("RELIANCE", "NSE", "D", "2024-11-01", "2024-12-12");
        System.out.println("Response:");
        System.out.println(gson.toJson(dailyResponse));
        System.out.println();

        // Test intervals
        System.out.println("=== Testing intervals() ===");
        System.out.println("Request: intervals()");
        System.out.println();

        JsonObject intervalsResponse = client.intervals();
        System.out.println("Response:");
        System.out.println(gson.toJson(intervalsResponse));
    }
}
