package in.openalgo;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Account management API methods for OpenAlgo.
 */
public abstract class AccountApi extends DataApi {

    protected AccountApi(String apiKey, String host, String version, double timeout, int wsPort, String wsUrl) {
        super(apiKey, host, version, timeout, wsPort, wsUrl);
    }

    /**
     * Get funds and margin details.
     *
     * @return JsonObject response with funds data
     */
    public JsonObject funds() {
        Map<String, Object> payload = createPayload();
        return makeRequest("funds", payload);
    }

    /**
     * Get order book details.
     *
     * @return JsonObject response with order book data
     */
    public JsonObject orderbook() {
        Map<String, Object> payload = createPayload();
        return makeRequest("orderbook", payload);
    }

    /**
     * Get trade book details.
     *
     * @return JsonObject response with trade book data
     */
    public JsonObject tradebook() {
        Map<String, Object> payload = createPayload();
        return makeRequest("tradebook", payload);
    }

    /**
     * Get position book details.
     *
     * @return JsonObject response with position book data
     */
    public JsonObject positionbook() {
        Map<String, Object> payload = createPayload();
        return makeRequest("positionbook", payload);
    }

    /**
     * Get stock holdings.
     *
     * @return JsonObject response with holdings data
     */
    public JsonObject holdings() {
        Map<String, Object> payload = createPayload();
        return makeRequest("holdings", payload);
    }

    /**
     * Get analyzer status.
     *
     * @return JsonObject response with analyzer status
     */
    public JsonObject analyzerstatus() {
        Map<String, Object> payload = createPayload();
        return makeRequest("analyzer", payload);
    }

    /**
     * Toggle analyzer mode between analyze and live modes.
     *
     * @param mode True for analyze mode (simulated), False for live mode
     * @return JsonObject response with new analyzer status
     */
    public JsonObject analyzertoggle(boolean mode) {
        Map<String, Object> payload = createPayload();
        payload.put("mode", mode);
        return makeRequest("analyzer/toggle", payload);
    }

    /**
     * Calculate margin requirements for positions.
     *
     * @param positions List of position maps (max 50 positions)
     * @return JsonObject response with margin requirements
     */
    public JsonObject margin(List<Map<String, Object>> positions) {
        if (positions == null || positions.isEmpty()) {
            JsonObject error = new JsonObject();
            error.addProperty("status", "error");
            error.addProperty("message", "Positions array cannot be empty");
            error.addProperty("error_type", "validation_error");
            return error;
        }

        if (positions.size() > 50) {
            JsonObject error = new JsonObject();
            error.addProperty("status", "error");
            error.addProperty("message", "Maximum 50 positions allowed");
            error.addProperty("error_type", "validation_error");
            return error;
        }

        Map<String, Object> payload = createPayload();

        // Process positions to ensure all required fields
        List<Map<String, Object>> processedPositions = new ArrayList<>();
        for (Map<String, Object> p : positions) {
            Map<String, Object> processed = new HashMap<>();
            processed.put("symbol", p.get("symbol"));
            processed.put("exchange", p.get("exchange"));
            processed.put("action", p.get("action"));
            processed.put("product", p.get("product"));
            processed.put("pricetype", p.get("pricetype"));
            processed.put("quantity", p.get("quantity"));
            processed.put("price", p.getOrDefault("price", "0"));
            processed.put("trigger_price", p.getOrDefault("trigger_price", "0"));
            processedPositions.add(processed);
        }

        payload.put("positions", processedPositions);
        return makeRequest("margin", payload);
    }
}
