package in.openalgo;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Utilities API methods for OpenAlgo including Telegram, Market Timings, and Holidays.
 */
public abstract class UtilitiesApi extends OptionsApi {

    protected UtilitiesApi(String apiKey, String host, String version, double timeout, int wsPort, String wsUrl) {
        super(apiKey, host, version, timeout, wsPort, wsUrl);
    }

    /**
     * Send custom alert messages to Telegram users.
     *
     * Prerequisites:
     * 1. Telegram Bot must be running (started from OpenAlgo Telegram settings)
     * 2. User must be linked via /link command in Telegram bot
     * 3. Valid and active API key required
     *
     * Priority Levels:
     * - 1-3: Low Priority (General updates, market news)
     * - 4-6: Normal Priority (Trade signals, daily summaries)
     * - 7-8: High Priority (Price alerts, position updates)
     * - 9-10: Urgent (Stop loss hits, risk alerts)
     *
     * @param username OpenAlgo login username (NOT Telegram username) (required)
     * @param message  Alert message to send, max 4096 characters (required)
     * @param priority Message priority 1-10 (default: 5)
     * @return JsonObject response
     */
    public JsonObject telegram(String username, String message, int priority) {
        Map<String, Object> payload = createPayload();
        payload.put("username", username);
        payload.put("message", message);
        payload.put("priority", priority);
        return makeRequest("telegram/notify", payload);
    }

    /**
     * Send custom alert messages to Telegram users with default priority.
     */
    public JsonObject telegram(String username, String message) {
        return telegram(username, message, 5);
    }

    /**
     * Get trading holidays for a year.
     *
     * @param year Year to get holidays for (required)
     * @return JsonObject response with holidays list
     */
    public JsonObject holidays(int year) {
        Map<String, Object> payload = createPayload();
        payload.put("year", year);
        return makeRequest("market/holidays", payload);
    }

    /**
     * Get exchange timings for a date.
     *
     * @param date Date in YYYY-MM-DD format (required)
     * @return JsonObject response with exchange timings
     */
    public JsonObject timings(String date) {
        Map<String, Object> payload = createPayload();
        payload.put("date", date);
        return makeRequest("market/timings", payload);
    }
}
