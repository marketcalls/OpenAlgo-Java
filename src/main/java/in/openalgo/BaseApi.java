package in.openalgo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Base API class with HTTP request handling for OpenAlgo.
 */
public abstract class BaseApi {

    protected final String apiKey;
    protected final String baseUrl;
    protected final OkHttpClient client;
    protected final Gson gson;
    protected final int wsPort;
    protected final String wsUrl;

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    /**
     * Creates a new BaseApi instance.
     *
     * @param apiKey  API key for authentication
     * @param host    Base host URL (default: http://127.0.0.1:5000)
     * @param version API version (default: v1)
     * @param timeout Request timeout in seconds (default: 120)
     * @param wsPort  WebSocket port (default: 8765)
     * @param wsUrl   Custom WebSocket URL (optional)
     */
    protected BaseApi(String apiKey, String host, String version, double timeout, int wsPort, String wsUrl) {
        this.apiKey = apiKey;
        this.baseUrl = host + "/api/" + version + "/";
        this.wsPort = wsPort;
        this.wsUrl = wsUrl != null ? wsUrl : buildWsUrl(host, wsPort);
        this.gson = new Gson();
        this.client = new OkHttpClient.Builder()
                .connectTimeout((long) timeout, TimeUnit.SECONDS)
                .readTimeout((long) timeout, TimeUnit.SECONDS)
                .writeTimeout((long) timeout, TimeUnit.SECONDS)
                .build();
    }

    private String buildWsUrl(String host, int wsPort) {
        String wsHost = host.replace("http://", "").replace("https://", "");
        if (wsHost.contains(":")) {
            wsHost = wsHost.substring(0, wsHost.indexOf(":"));
        }
        return "ws://" + wsHost + ":" + wsPort;
    }

    /**
     * Creates a base payload with API key.
     *
     * @return Map with apikey
     */
    protected Map<String, Object> createPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("apikey", apiKey);
        return payload;
    }

    /**
     * Makes a POST request to the specified endpoint.
     *
     * @param endpoint API endpoint
     * @param payload  Request payload
     * @return JsonObject response
     */
    protected JsonObject makeRequest(String endpoint, Map<String, Object> payload) {
        String url = baseUrl + endpoint;
        String jsonBody = gson.toJson(payload);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "{}";
            return JsonParser.parseString(responseBody).getAsJsonObject();
        } catch (IOException e) {
            JsonObject error = new JsonObject();
            error.addProperty("status", "error");
            error.addProperty("message", e.getMessage());
            return error;
        }
    }

    /**
     * Makes a GET request to the specified endpoint.
     *
     * @param endpoint API endpoint
     * @param params   Query parameters
     * @return JsonObject response
     */
    protected JsonObject makeGetRequest(String endpoint, Map<String, String> params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + endpoint).newBuilder();
        urlBuilder.addQueryParameter("apikey", apiKey);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }
        }

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "{}";
            return JsonParser.parseString(responseBody).getAsJsonObject();
        } catch (IOException e) {
            JsonObject error = new JsonObject();
            error.addProperty("status", "error");
            error.addProperty("message", e.getMessage());
            return error;
        }
    }

    /**
     * Gets the API key.
     *
     * @return API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Gets the base URL.
     *
     * @return Base URL
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Gets the WebSocket URL.
     *
     * @return WebSocket URL
     */
    public String getWsUrl() {
        return wsUrl;
    }
}
