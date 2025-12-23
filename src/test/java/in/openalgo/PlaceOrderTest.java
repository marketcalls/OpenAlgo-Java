package in.openalgo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Test for Place Order API.
 *
 * Usage: java -cp target/openalgo-1.0.0.jar in.openalgo.PlaceOrderTest
 *
 * WARNING: This will place a real order. Use with caution!
 */
public class PlaceOrderTest {

    public static void main(String[] args) {
        // Replace with your API key
        String apiKey = "your-api-key-here";

        // Create OpenAlgo client
        OpenAlgo client = new OpenAlgo(apiKey);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Test CNC order (delivery)
        System.out.println("=== Testing placeorder() - CNC Order ===");
        System.out.println("Request: placeorder(\"YESBANK\", \"BUY\", \"NSE\", \"MARKET\", \"CNC\", 1, \"Test\", ...)");
        System.out.println();

        JsonObject response = client.placeorder(
            "YESBANK",      // symbol
            "BUY",          // action
            "NSE",          // exchange
            "MARKET",       // priceType
            "CNC",          // product
            1,              // quantity
            "Test",         // strategy
            "0",            // price (0 for market)
            "0",            // triggerPrice
            "0",            // disclosedQuantity
            null,           // target
            null,           // stoploss
            null            // trailingSl
        );
        System.out.println("Response:");
        System.out.println(gson.toJson(response));
        System.out.println();

        // Test order status
        if (response.has("orderid")) {
            String orderId = response.get("orderid").getAsString();
            System.out.println("=== Testing orderstatus() ===");
            System.out.println("Request: orderstatus(\"" + orderId + "\", \"Test\")");
            System.out.println();

            JsonObject statusResponse = client.orderstatus(orderId, "Test");
            System.out.println("Response:");
            System.out.println(gson.toJson(statusResponse));
            System.out.println();
        }

        // Test orderbook
        System.out.println("=== Testing orderbook() ===");
        System.out.println("Request: orderbook()");
        System.out.println();

        JsonObject orderbookResponse = client.orderbook();
        System.out.println("Response:");
        System.out.println(gson.toJson(orderbookResponse));
    }
}
