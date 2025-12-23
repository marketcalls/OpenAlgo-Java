package in.openalgo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * WebSocket Feed API for real-time market data streaming.
 */
public abstract class FeedApi extends UtilitiesApi {

    private WebSocketClient wsClient;
    private volatile boolean connected = false;
    private volatile boolean authenticated = false;
    private final Gson gson = new Gson();

    // Thread-safe data caches
    private final ConcurrentHashMap<String, Map<String, Object>> ltpData = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Map<String, Object>> quotesData = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Map<String, Object>> depthData = new ConcurrentHashMap<>();

    // Callbacks
    private Consumer<Map<String, Object>> ltpCallback;
    private Consumer<Map<String, Object>> quoteCallback;
    private Consumer<Map<String, Object>> depthCallback;

    // Connection latch
    private CountDownLatch authLatch;

    protected FeedApi(String apiKey, String host, String version, double timeout, int wsPort, String wsUrl) {
        super(apiKey, host, version, timeout, wsPort, wsUrl);
    }

    /**
     * Connect to the WebSocket server.
     * Supports both ws:// and wss:// URLs.
     *
     * @return true if connected and authenticated successfully
     */
    public boolean connect() {
        if (connected && authenticated) {
            return true;
        }

        try {
            authLatch = new CountDownLatch(1);
            String wsUrl = getWsUrl();

            wsClient = new WebSocketClient(new URI(wsUrl)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    connected = true;
                    authenticate();
                }

                @Override
                public void onMessage(String message) {
                    processMessage(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    connected = false;
                    authenticated = false;
                }

                @Override
                public void onError(Exception ex) {
                    connected = false;
                    authenticated = false;
                }
            };

            // Configure SSL for wss:// URLs
            if (wsUrl.startsWith("wss://")) {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, null);
                SSLSocketFactory factory = sslContext.getSocketFactory();
                wsClient.setSocketFactory(factory);
            }

            wsClient.connect();

            // Wait for authentication (timeout 5 seconds)
            return authLatch.await(5, TimeUnit.SECONDS) && authenticated;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Disconnect from the WebSocket server.
     */
    public void disconnect() {
        if (wsClient != null) {
            wsClient.close();
            wsClient = null;
        }
        connected = false;
        authenticated = false;
        ltpData.clear();
        quotesData.clear();
        depthData.clear();
    }

    private void authenticate() {
        JsonObject authMsg = new JsonObject();
        authMsg.addProperty("action", "authenticate");
        authMsg.addProperty("api_key", apiKey);
        wsClient.send(gson.toJson(authMsg));
    }

    private void processMessage(String message) {
        try {
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();
            String type = json.has("type") ? json.get("type").getAsString() : "";

            switch (type) {
                case "auth":
                    if (json.has("status") && "success".equals(json.get("status").getAsString())) {
                        authenticated = true;
                    }
                    authLatch.countDown();
                    break;

                case "market_data":
                    handleMarketData(json);
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            // Ignore parse errors
        }
    }

    private void handleMarketData(JsonObject json) {
        String exchange = json.has("exchange") ? json.get("exchange").getAsString() : "";
        String symbol = json.has("symbol") ? json.get("symbol").getAsString() : "";
        int mode = json.has("mode") ? json.get("mode").getAsInt() : 0;
        String key = exchange + ":" + symbol;

        JsonObject data = json.has("data") ? json.get("data").getAsJsonObject() : new JsonObject();

        switch (mode) {
            case 1: // LTP
                handleLtpData(key, exchange, symbol, data);
                break;
            case 2: // Quote
                handleQuoteData(key, exchange, symbol, data);
                break;
            case 3: // Depth
                handleDepthData(key, exchange, symbol, data);
                break;
        }
    }

    private void handleLtpData(String key, String exchange, String symbol, JsonObject data) {
        Map<String, Object> ltpInfo = new HashMap<>();
        ltpInfo.put("ltp", data.has("ltp") ? data.get("ltp").getAsDouble() : 0);
        ltpInfo.put("timestamp", data.has("timestamp") ? data.get("timestamp").getAsLong() : 0);
        ltpData.put(key, ltpInfo);

        if (ltpCallback != null) {
            Map<String, Object> callbackData = new HashMap<>();
            callbackData.put("type", "market_data");
            callbackData.put("symbol", symbol);
            callbackData.put("exchange", exchange);
            callbackData.put("mode", 1);
            callbackData.put("data", ltpInfo);
            ltpCallback.accept(callbackData);
        }
    }

    private void handleQuoteData(String key, String exchange, String symbol, JsonObject data) {
        Map<String, Object> quoteInfo = new HashMap<>();
        quoteInfo.put("open", data.has("open") ? data.get("open").getAsDouble() : 0);
        quoteInfo.put("high", data.has("high") ? data.get("high").getAsDouble() : 0);
        quoteInfo.put("low", data.has("low") ? data.get("low").getAsDouble() : 0);
        quoteInfo.put("close", data.has("close") ? data.get("close").getAsDouble() : 0);
        quoteInfo.put("ltp", data.has("ltp") ? data.get("ltp").getAsDouble() : 0);
        quoteInfo.put("volume", data.has("volume") ? data.get("volume").getAsLong() : 0);
        quoteInfo.put("timestamp", data.has("timestamp") ? data.get("timestamp").getAsLong() : 0);
        quotesData.put(key, quoteInfo);

        if (quoteCallback != null) {
            Map<String, Object> callbackData = new HashMap<>();
            callbackData.put("type", "market_data");
            callbackData.put("symbol", symbol);
            callbackData.put("exchange", exchange);
            callbackData.put("mode", 2);
            callbackData.put("data", quoteInfo);
            quoteCallback.accept(callbackData);
        }
    }

    private void handleDepthData(String key, String exchange, String symbol, JsonObject data) {
        Map<String, Object> depthInfo = new HashMap<>();
        depthInfo.put("ltp", data.has("ltp") ? data.get("ltp").getAsDouble() : 0);
        depthInfo.put("timestamp", data.has("timestamp") ? data.get("timestamp").getAsLong() : 0);
        if (data.has("depth")) {
            depthInfo.put("depth", gson.fromJson(data.get("depth"), Map.class));
        }
        depthData.put(key, depthInfo);

        if (depthCallback != null) {
            Map<String, Object> callbackData = new HashMap<>();
            callbackData.put("type", "market_data");
            callbackData.put("symbol", symbol);
            callbackData.put("exchange", exchange);
            callbackData.put("mode", 3);
            callbackData.put("data", depthInfo);
            depthCallback.accept(callbackData);
        }
    }

    /**
     * Subscribe to LTP updates for instruments.
     *
     * @param instruments List of instrument maps with exchange, symbol keys
     * @param callback    Callback function for data updates (optional)
     * @return true if subscription sent successfully
     */
    public boolean subscribeLtp(List<Map<String, String>> instruments, Consumer<Map<String, Object>> callback) {
        if (!connected || !authenticated) {
            return false;
        }

        this.ltpCallback = callback;

        for (Map<String, String> instrument : instruments) {
            JsonObject subMsg = new JsonObject();
            subMsg.addProperty("action", "subscribe");
            subMsg.addProperty("symbol", instrument.get("symbol"));
            subMsg.addProperty("exchange", instrument.get("exchange"));
            subMsg.addProperty("mode", 1);
            subMsg.addProperty("depth", 5);
            wsClient.send(gson.toJson(subMsg));
        }

        return true;
    }

    /**
     * Subscribe to LTP updates without callback.
     */
    public boolean subscribeLtp(List<Map<String, String>> instruments) {
        return subscribeLtp(instruments, null);
    }

    /**
     * Unsubscribe from LTP updates.
     *
     * @param instruments List of instrument maps with exchange, symbol keys
     * @return true if unsubscription sent successfully
     */
    public boolean unsubscribeLtp(List<Map<String, String>> instruments) {
        if (!connected || !authenticated) {
            return false;
        }

        for (Map<String, String> instrument : instruments) {
            String key = instrument.get("exchange") + ":" + instrument.get("symbol");
            ltpData.remove(key);

            JsonObject unsubMsg = new JsonObject();
            unsubMsg.addProperty("action", "unsubscribe");
            unsubMsg.addProperty("symbol", instrument.get("symbol"));
            unsubMsg.addProperty("exchange", instrument.get("exchange"));
            unsubMsg.addProperty("mode", 1);
            wsClient.send(gson.toJson(unsubMsg));
        }

        return true;
    }

    /**
     * Subscribe to Quote updates for instruments.
     *
     * @param instruments List of instrument maps with exchange, symbol keys
     * @param callback    Callback function for data updates (optional)
     * @return true if subscription sent successfully
     */
    public boolean subscribeQuote(List<Map<String, String>> instruments, Consumer<Map<String, Object>> callback) {
        if (!connected || !authenticated) {
            return false;
        }

        this.quoteCallback = callback;

        for (Map<String, String> instrument : instruments) {
            JsonObject subMsg = new JsonObject();
            subMsg.addProperty("action", "subscribe");
            subMsg.addProperty("symbol", instrument.get("symbol"));
            subMsg.addProperty("exchange", instrument.get("exchange"));
            subMsg.addProperty("mode", 2);
            subMsg.addProperty("depth", 5);
            wsClient.send(gson.toJson(subMsg));
        }

        return true;
    }

    /**
     * Subscribe to Quote updates without callback.
     */
    public boolean subscribeQuote(List<Map<String, String>> instruments) {
        return subscribeQuote(instruments, null);
    }

    /**
     * Unsubscribe from Quote updates.
     *
     * @param instruments List of instrument maps with exchange, symbol keys
     * @return true if unsubscription sent successfully
     */
    public boolean unsubscribeQuote(List<Map<String, String>> instruments) {
        if (!connected || !authenticated) {
            return false;
        }

        for (Map<String, String> instrument : instruments) {
            String key = instrument.get("exchange") + ":" + instrument.get("symbol");
            quotesData.remove(key);

            JsonObject unsubMsg = new JsonObject();
            unsubMsg.addProperty("action", "unsubscribe");
            unsubMsg.addProperty("symbol", instrument.get("symbol"));
            unsubMsg.addProperty("exchange", instrument.get("exchange"));
            unsubMsg.addProperty("mode", 2);
            wsClient.send(gson.toJson(unsubMsg));
        }

        return true;
    }

    /**
     * Subscribe to Market Depth updates for instruments.
     *
     * @param instruments List of instrument maps with exchange, symbol keys
     * @param callback    Callback function for data updates (optional)
     * @return true if subscription sent successfully
     */
    public boolean subscribeDepth(List<Map<String, String>> instruments, Consumer<Map<String, Object>> callback) {
        if (!connected || !authenticated) {
            return false;
        }

        this.depthCallback = callback;

        for (Map<String, String> instrument : instruments) {
            JsonObject subMsg = new JsonObject();
            subMsg.addProperty("action", "subscribe");
            subMsg.addProperty("symbol", instrument.get("symbol"));
            subMsg.addProperty("exchange", instrument.get("exchange"));
            subMsg.addProperty("mode", 3);
            subMsg.addProperty("depth", 5);
            wsClient.send(gson.toJson(subMsg));
        }

        return true;
    }

    /**
     * Subscribe to Market Depth updates without callback.
     */
    public boolean subscribeDepth(List<Map<String, String>> instruments) {
        return subscribeDepth(instruments, null);
    }

    /**
     * Unsubscribe from Market Depth updates.
     *
     * @param instruments List of instrument maps with exchange, symbol keys
     * @return true if unsubscription sent successfully
     */
    public boolean unsubscribeDepth(List<Map<String, String>> instruments) {
        if (!connected || !authenticated) {
            return false;
        }

        for (Map<String, String> instrument : instruments) {
            String key = instrument.get("exchange") + ":" + instrument.get("symbol");
            depthData.remove(key);

            JsonObject unsubMsg = new JsonObject();
            unsubMsg.addProperty("action", "unsubscribe");
            unsubMsg.addProperty("symbol", instrument.get("symbol"));
            unsubMsg.addProperty("exchange", instrument.get("exchange"));
            unsubMsg.addProperty("mode", 3);
            wsClient.send(gson.toJson(unsubMsg));
        }

        return true;
    }

    /**
     * Get cached LTP data.
     *
     * @param exchange Exchange filter (optional)
     * @param symbol   Symbol filter (optional)
     * @return Map with LTP data
     */
    public Map<String, Object> getLtp(String exchange, String symbol) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Map<String, Object>> ltpResult = new HashMap<>();

        for (Map.Entry<String, Map<String, Object>> entry : ltpData.entrySet()) {
            String[] parts = entry.getKey().split(":");
            String exch = parts[0];
            String sym = parts[1];

            if ((exchange == null || exchange.equals(exch)) && (symbol == null || symbol.equals(sym))) {
                ltpResult.computeIfAbsent(exch, k -> new HashMap<>());
                ((Map<String, Object>) ltpResult.get(exch)).put(sym, entry.getValue());
            }
        }

        result.put("ltp", ltpResult);
        return result;
    }

    /**
     * Get all cached LTP data.
     */
    public Map<String, Object> getLtp() {
        return getLtp(null, null);
    }

    /**
     * Get cached Quote data.
     *
     * @param exchange Exchange filter (optional)
     * @param symbol   Symbol filter (optional)
     * @return Map with Quote data
     */
    public Map<String, Object> getQuotes(String exchange, String symbol) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Map<String, Object>> quoteResult = new HashMap<>();

        for (Map.Entry<String, Map<String, Object>> entry : quotesData.entrySet()) {
            String[] parts = entry.getKey().split(":");
            String exch = parts[0];
            String sym = parts[1];

            if ((exchange == null || exchange.equals(exch)) && (symbol == null || symbol.equals(sym))) {
                quoteResult.computeIfAbsent(exch, k -> new HashMap<>());
                ((Map<String, Object>) quoteResult.get(exch)).put(sym, entry.getValue());
            }
        }

        result.put("quote", quoteResult);
        return result;
    }

    /**
     * Get all cached Quote data.
     */
    public Map<String, Object> getQuotes() {
        return getQuotes(null, null);
    }

    /**
     * Get cached Market Depth data.
     *
     * @param exchange Exchange filter (optional)
     * @param symbol   Symbol filter (optional)
     * @return Map with Depth data
     */
    public Map<String, Object> getDepth(String exchange, String symbol) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Map<String, Object>> depthResult = new HashMap<>();

        for (Map.Entry<String, Map<String, Object>> entry : depthData.entrySet()) {
            String[] parts = entry.getKey().split(":");
            String exch = parts[0];
            String sym = parts[1];

            if ((exchange == null || exchange.equals(exch)) && (symbol == null || symbol.equals(sym))) {
                depthResult.computeIfAbsent(exch, k -> new HashMap<>());
                ((Map<String, Object>) depthResult.get(exch)).put(sym, entry.getValue());
            }
        }

        result.put("depth", depthResult);
        return result;
    }

    /**
     * Get all cached Market Depth data.
     */
    public Map<String, Object> getDepth() {
        return getDepth(null, null);
    }

    /**
     * Check if WebSocket is connected.
     *
     * @return true if connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Check if WebSocket is authenticated.
     *
     * @return true if authenticated
     */
    public boolean isAuthenticated() {
        return authenticated;
    }
}
