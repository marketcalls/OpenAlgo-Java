package in.openalgo;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Market data API methods for OpenAlgo.
 */
public abstract class DataApi extends OrderApi {

    protected DataApi(String apiKey, String host, String version, double timeout, int wsPort, String wsUrl) {
        super(apiKey, host, version, timeout, wsPort, wsUrl);
    }

    /**
     * Get real-time quotes for a symbol.
     *
     * @param symbol   Trading symbol (required)
     * @param exchange Exchange code (required)
     * @return JsonObject response with quote data
     */
    public JsonObject quotes(String symbol, String exchange) {
        Map<String, Object> payload = createPayload();
        payload.put("symbol", symbol);
        payload.put("exchange", exchange);
        return makeRequest("quotes", payload);
    }

    /**
     * Get real-time quotes for multiple symbols.
     *
     * @param symbols List of symbol-exchange maps (required)
     * @return JsonObject response with quotes data
     */
    public JsonObject multiquotes(List<Map<String, String>> symbols) {
        Map<String, Object> payload = createPayload();
        payload.put("symbols", symbols);
        return makeRequest("multiquotes", payload);
    }

    /**
     * Get market depth (order book) for a symbol.
     *
     * @param symbol   Trading symbol (required)
     * @param exchange Exchange code (required)
     * @return JsonObject response with depth data
     */
    public JsonObject depth(String symbol, String exchange) {
        Map<String, Object> payload = createPayload();
        payload.put("symbol", symbol);
        payload.put("exchange", exchange);
        return makeRequest("depth", payload);
    }

    /**
     * Get symbol details including token and lot size.
     *
     * @param symbol   Trading symbol (required)
     * @param exchange Exchange code (required)
     * @return JsonObject response with symbol details
     */
    public JsonObject symbol(String symbol, String exchange) {
        Map<String, Object> payload = createPayload();
        payload.put("symbol", symbol);
        payload.put("exchange", exchange);
        return makeRequest("symbol", payload);
    }

    /**
     * Search for symbols matching a query.
     *
     * @param query    Search query (required)
     * @param exchange Exchange filter (optional)
     * @return JsonObject response with matching symbols
     */
    public JsonObject search(String query, String exchange) {
        Map<String, Object> payload = createPayload();
        payload.put("query", query);
        if (exchange != null) {
            payload.put("exchange", exchange);
        }
        return makeRequest("search", payload);
    }

    /**
     * Search for symbols without exchange filter.
     */
    public JsonObject search(String query) {
        return search(query, null);
    }

    /**
     * Get historical OHLCV data for a symbol.
     *
     * @param symbol    Trading symbol (required)
     * @param exchange  Exchange code (required)
     * @param interval  Time interval (required)
     * @param startDate Start date in YYYY-MM-DD format (required)
     * @param endDate   End date in YYYY-MM-DD format (required)
     * @return JsonObject response with historical data
     */
    public JsonObject history(String symbol, String exchange, String interval,
                               String startDate, String endDate) {
        Map<String, Object> payload = createPayload();
        payload.put("symbol", symbol);
        payload.put("exchange", exchange);
        payload.put("interval", interval);
        payload.put("start_date", startDate);
        payload.put("end_date", endDate);
        return makeRequest("history", payload);
    }

    /**
     * Get supported time intervals for historical data.
     *
     * @return JsonObject response with supported intervals
     */
    public JsonObject intervals() {
        Map<String, Object> payload = createPayload();
        return makeRequest("intervals", payload);
    }

    /**
     * Get supported time intervals (legacy method).
     *
     * @return JsonObject response with supported intervals
     */
    public JsonObject interval() {
        return intervals();
    }

    /**
     * Get expiry dates for a derivative symbol.
     *
     * @param symbol         Trading symbol (required)
     * @param exchange       Exchange code (required)
     * @param instrumenttype Instrument type: futures/options (required)
     * @return JsonObject response with expiry dates
     */
    public JsonObject expiry(String symbol, String exchange, String instrumenttype) {
        Map<String, Object> payload = createPayload();
        payload.put("symbol", symbol);
        payload.put("exchange", exchange);
        payload.put("instrumenttype", instrumenttype);
        return makeRequest("expiry", payload);
    }

    /**
     * Get all instruments for an exchange.
     * Uses GET request unlike other methods.
     *
     * @param exchange Exchange code (optional, null for all exchanges)
     * @return JsonObject response with instruments data
     */
    public JsonObject instruments(String exchange) {
        Map<String, String> params = new HashMap<>();
        if (exchange != null) {
            params.put("exchange", exchange);
        }
        return makeGetRequest("instruments", params);
    }

    /**
     * Get all instruments for all exchanges.
     */
    public JsonObject instruments() {
        return instruments(null);
    }

    /**
     * Calculate synthetic future price.
     *
     * @param underlying Underlying symbol (required)
     * @param exchange   Exchange code (required)
     * @param expiryDate Expiry date in DDMMMYY format (required)
     * @return JsonObject response with synthetic future price
     */
    public JsonObject syntheticfuture(String underlying, String exchange, String expiryDate) {
        Map<String, Object> payload = createPayload();
        payload.put("underlying", underlying);
        payload.put("exchange", exchange);
        payload.put("expiry_date", expiryDate);
        return makeRequest("syntheticfuture", payload);
    }
}
