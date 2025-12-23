package in.openalgo;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Options trading API methods for OpenAlgo.
 */
public abstract class OptionsApi extends AccountApi {

    protected OptionsApi(String apiKey, String host, String version, double timeout, int wsPort, String wsUrl) {
        super(apiKey, host, version, timeout, wsPort, wsUrl);
    }

    /**
     * Calculate option greeks for an option symbol.
     *
     * @param symbol             Option symbol (required)
     * @param exchange           Exchange code (required)
     * @param interestRate       Risk-free interest rate (optional)
     * @param forwardPrice       Custom forward/synthetic futures price (optional)
     * @param underlyingSymbol   Custom underlying symbol (optional)
     * @param underlyingExchange Custom underlying exchange (optional)
     * @param expiryTime         Custom expiry time in HH:MM format (optional)
     * @return JsonObject response with option greeks
     */
    public JsonObject optiongreeks(String symbol, String exchange, Double interestRate,
                                    Double forwardPrice, String underlyingSymbol,
                                    String underlyingExchange, String expiryTime) {
        Map<String, Object> payload = createPayload();
        payload.put("symbol", symbol);
        payload.put("exchange", exchange);

        if (interestRate != null) payload.put("interest_rate", interestRate);
        if (forwardPrice != null) payload.put("forward_price", forwardPrice);
        if (underlyingSymbol != null) payload.put("underlying_symbol", underlyingSymbol);
        if (underlyingExchange != null) payload.put("underlying_exchange", underlyingExchange);
        if (expiryTime != null) payload.put("expiry_time", expiryTime);

        return makeRequest("optiongreeks", payload);
    }

    /**
     * Calculate option greeks with minimal parameters.
     */
    public JsonObject optiongreeks(String symbol, String exchange) {
        return optiongreeks(symbol, exchange, null, null, null, null, null);
    }

    /**
     * Place an options order based on strike offset.
     *
     * @param underlying        Underlying symbol (required)
     * @param exchange          Exchange code (required)
     * @param offset            Strike offset: ATM, ITM1-ITM50, OTM1-OTM50 (required)
     * @param optionType        CE or PE (required)
     * @param action            BUY or SELL (required)
     * @param quantity          Quantity (required)
     * @param strategy          Strategy name (default: Java)
     * @param expiryDate        Expiry date in DDMMMYY format (optional)
     * @param priceType         Price type (default: MARKET)
     * @param product           Product type (default: MIS)
     * @param price             Price for LIMIT orders (optional)
     * @param triggerPrice      Trigger price for SL orders (optional)
     * @param disclosedQuantity Disclosed quantity (optional)
     * @return JsonObject response with order details
     */
    public JsonObject optionsorder(String underlying, String exchange, String offset,
                                    String optionType, String action, int quantity,
                                    String strategy, String expiryDate, String priceType,
                                    String product, String price, String triggerPrice,
                                    String disclosedQuantity) {
        Map<String, Object> payload = createPayload();
        payload.put("underlying", underlying);
        payload.put("exchange", exchange);
        payload.put("offset", offset);
        payload.put("option_type", optionType);
        payload.put("action", action);
        payload.put("quantity", String.valueOf(quantity));
        payload.put("strategy", strategy != null ? strategy : "Java");
        payload.put("pricetype", priceType != null ? priceType : "MARKET");
        payload.put("product", product != null ? product : "MIS");

        if (expiryDate != null) payload.put("expiry_date", expiryDate);
        if (price != null) payload.put("price", price);
        if (triggerPrice != null) payload.put("trigger_price", triggerPrice);
        if (disclosedQuantity != null) payload.put("disclosed_quantity", disclosedQuantity);

        return makeRequest("optionsorder", payload);
    }

    /**
     * Place an options order with minimal parameters.
     */
    public JsonObject optionsorder(String underlying, String exchange, String offset,
                                    String optionType, String action, int quantity) {
        return optionsorder(underlying, exchange, offset, optionType, action, quantity,
                null, null, null, null, null, null, null);
    }

    /**
     * Place an options order with expiry date.
     */
    public JsonObject optionsorder(String underlying, String exchange, String offset,
                                    String optionType, String action, int quantity, String expiryDate) {
        return optionsorder(underlying, exchange, offset, optionType, action, quantity,
                null, expiryDate, null, null, null, null, null);
    }

    /**
     * Place an options order with expiry date and product.
     */
    public JsonObject optionsorder(String underlying, String exchange, String offset,
                                    String optionType, String action, int quantity,
                                    String expiryDate, String product) {
        return optionsorder(underlying, exchange, offset, optionType, action, quantity,
                null, expiryDate, null, product, null, null, null);
    }

    /**
     * Get option symbol based on strike offset.
     *
     * @param underlying Underlying symbol (required)
     * @param exchange   Exchange code (required)
     * @param offset     Strike offset: ATM, ITM1-ITM50, OTM1-OTM50 (required)
     * @param optionType CE or PE (required)
     * @param expiryDate Expiry date in DDMMMYY format (optional)
     * @return JsonObject response with symbol details
     */
    public JsonObject optionsymbol(String underlying, String exchange, String offset,
                                    String optionType, String expiryDate) {
        Map<String, Object> payload = createPayload();
        payload.put("underlying", underlying);
        payload.put("exchange", exchange);
        payload.put("offset", offset);
        payload.put("option_type", optionType);

        if (expiryDate != null) payload.put("expiry_date", expiryDate);

        return makeRequest("optionsymbol", payload);
    }

    /**
     * Get option symbol with minimal parameters.
     */
    public JsonObject optionsymbol(String underlying, String exchange, String offset, String optionType) {
        return optionsymbol(underlying, exchange, offset, optionType, null);
    }

    /**
     * Place multiple options orders (legs) as a strategy.
     *
     * @param strategy   Strategy name (required)
     * @param underlying Underlying symbol (required)
     * @param exchange   Exchange code (required)
     * @param legs       List of leg maps (1-20 legs) (required)
     * @param expiryDate Default expiry date in DDMMMYY format (optional)
     * @return JsonObject response with order results
     */
    public JsonObject optionsmultiorder(String strategy, String underlying, String exchange,
                                         List<Map<String, Object>> legs, String expiryDate) {
        Map<String, Object> payload = createPayload();
        payload.put("strategy", strategy);
        payload.put("underlying", underlying);
        payload.put("exchange", exchange);
        payload.put("legs", legs);

        if (expiryDate != null) payload.put("expiry_date", expiryDate);

        return makeRequest("optionsmultiorder", payload);
    }

    /**
     * Place multiple options orders without default expiry.
     */
    public JsonObject optionsmultiorder(String strategy, String underlying, String exchange,
                                         List<Map<String, Object>> legs) {
        return optionsmultiorder(strategy, underlying, exchange, legs, null);
    }

    /**
     * Get option chain for an underlying.
     *
     * @param underlying  Underlying symbol (required)
     * @param exchange    Exchange code (required)
     * @param expiryDate  Expiry date in DDMMMYY format (optional)
     * @param strikeCount Number of strikes above/below ATM, 1-100 (optional)
     * @return JsonObject response with option chain data
     */
    public JsonObject optionchain(String underlying, String exchange, String expiryDate, Integer strikeCount) {
        Map<String, Object> payload = createPayload();
        payload.put("underlying", underlying);
        payload.put("exchange", exchange);

        if (expiryDate != null) payload.put("expiry_date", expiryDate);
        if (strikeCount != null) payload.put("strike_count", strikeCount);

        return makeRequest("optionchain", payload);
    }

    /**
     * Get option chain with minimal parameters.
     */
    public JsonObject optionchain(String underlying, String exchange) {
        return optionchain(underlying, exchange, null, null);
    }
}
