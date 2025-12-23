package in.openalgo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test for Quotes API.
 *
 * Usage: java -cp target/openalgo-1.0.0.jar in.openalgo.QuotesTest
 */
public class QuotesTest {

    public static void main(String[] args) {
        // Replace with your API key
        String apiKey = "your-api-key-here";

        // Create OpenAlgo client
        OpenAlgo client = new OpenAlgo(apiKey);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Test single quote
        System.out.println("=== Testing quotes() ===");
        System.out.println("Request: quotes(\"RELIANCE\", \"NSE\")");
        System.out.println();

        JsonObject response = client.quotes("RELIANCE", "NSE");
        System.out.println("Response:");
        System.out.println(gson.toJson(response));
        System.out.println();

        // Test multi quotes
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

        System.out.println("Request: multiquotes([RELIANCE, SBIN, INFY])");
        System.out.println();

        JsonObject multiResponse = client.multiquotes(symbols);
        System.out.println("Response:");
        System.out.println(gson.toJson(multiResponse));
        System.out.println();

        // Test depth
        System.out.println("=== Testing depth() ===");
        System.out.println("Request: depth(\"RELIANCE\", \"NSE\")");
        System.out.println();

        JsonObject depthResponse = client.depth("RELIANCE", "NSE");
        System.out.println("Response:");
        System.out.println(gson.toJson(depthResponse));
    }
}
