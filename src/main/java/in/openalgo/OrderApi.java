package in.openalgo;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Order management API methods for OpenAlgo.
 */
public abstract class OrderApi extends BaseApi {

    protected OrderApi(String apiKey, String host, String version, double timeout, int wsPort, String wsUrl) {
        super(apiKey, host, version, timeout, wsPort, wsUrl);
    }

    /**
     * Place an order.
     *
     * @param symbol    Trading symbol (required)
     * @param action    BUY or SELL (required)
     * @param exchange  Exchange code (required)
     * @param priceType Price type: MARKET, LIMIT, SL, SL-M (default: MARKET)
     * @param product   Product type: MIS, CNC, NRML (default: MIS)
     * @param quantity  Quantity to trade (default: 1)
     * @param strategy  Strategy name (default: Java)
     * @param price     Price for LIMIT orders (optional)
     * @param triggerPrice Trigger price for SL orders (optional)
     * @param disclosedQuantity Disclosed quantity (optional)
     * @param target    Target price (optional)
     * @param stoploss  Stop loss price (optional)
     * @param trailingSl Trailing stop loss (optional)
     * @return JsonObject response with orderid
     */
    public JsonObject placeorder(String symbol, String action, String exchange,
                                  String priceType, String product, int quantity,
                                  String strategy, String price, String triggerPrice,
                                  String disclosedQuantity, String target, String stoploss,
                                  String trailingSl) {
        Map<String, Object> payload = createPayload();
        payload.put("symbol", symbol);
        payload.put("action", action);
        payload.put("exchange", exchange);
        payload.put("pricetype", priceType != null ? priceType : "MARKET");
        payload.put("product", product != null ? product : "MIS");
        payload.put("quantity", String.valueOf(quantity > 0 ? quantity : 1));
        payload.put("strategy", strategy != null ? strategy : "Java");

        if (price != null) payload.put("price", price);
        if (triggerPrice != null) payload.put("trigger_price", triggerPrice);
        if (disclosedQuantity != null) payload.put("disclosed_quantity", disclosedQuantity);
        if (target != null) payload.put("target", target);
        if (stoploss != null) payload.put("stoploss", stoploss);
        if (trailingSl != null) payload.put("trailing_sl", trailingSl);

        return makeRequest("placeorder", payload);
    }

    /**
     * Place an order with minimal parameters.
     */
    public JsonObject placeorder(String symbol, String action, String exchange) {
        return placeorder(symbol, action, exchange, null, null, 1, null, null, null, null, null, null, null);
    }

    /**
     * Place an order with quantity.
     */
    public JsonObject placeorder(String symbol, String action, String exchange, int quantity) {
        return placeorder(symbol, action, exchange, null, null, quantity, null, null, null, null, null, null, null);
    }

    /**
     * Place a smart order with position sizing.
     *
     * @param symbol       Trading symbol (required)
     * @param action       BUY or SELL (required)
     * @param exchange     Exchange code (required)
     * @param positionSize Target position size (required)
     * @param priceType    Price type (default: MARKET)
     * @param product      Product type (default: MIS)
     * @param quantity     Quantity to trade (default: 1)
     * @param strategy     Strategy name (default: Java)
     * @param price        Price for LIMIT orders (optional)
     * @param triggerPrice Trigger price for SL orders (optional)
     * @param disclosedQuantity Disclosed quantity (optional)
     * @param target       Target price (optional)
     * @param stoploss     Stop loss price (optional)
     * @param trailingSl   Trailing stop loss (optional)
     * @return JsonObject response
     */
    public JsonObject placesmartorder(String symbol, String action, String exchange,
                                       int positionSize, String priceType, String product,
                                       int quantity, String strategy, String price,
                                       String triggerPrice, String disclosedQuantity,
                                       String target, String stoploss, String trailingSl) {
        Map<String, Object> payload = createPayload();
        payload.put("symbol", symbol);
        payload.put("action", action);
        payload.put("exchange", exchange);
        payload.put("position_size", String.valueOf(positionSize));
        payload.put("pricetype", priceType != null ? priceType : "MARKET");
        payload.put("product", product != null ? product : "MIS");
        payload.put("quantity", String.valueOf(quantity > 0 ? quantity : 1));
        payload.put("strategy", strategy != null ? strategy : "Java");

        if (price != null) payload.put("price", price);
        if (triggerPrice != null) payload.put("trigger_price", triggerPrice);
        if (disclosedQuantity != null) payload.put("disclosed_quantity", disclosedQuantity);
        if (target != null) payload.put("target", target);
        if (stoploss != null) payload.put("stoploss", stoploss);
        if (trailingSl != null) payload.put("trailing_sl", trailingSl);

        return makeRequest("placesmartorder", payload);
    }

    /**
     * Place a smart order with minimal parameters.
     */
    public JsonObject placesmartorder(String symbol, String action, String exchange, int positionSize) {
        return placesmartorder(symbol, action, exchange, positionSize, null, null, 1, null, null, null, null, null, null, null);
    }

    /**
     * Place a basket of orders.
     *
     * @param orders   List of order maps (required)
     * @param strategy Strategy name (default: Java)
     * @return JsonObject response with results array
     */
    public JsonObject basketorder(List<Map<String, Object>> orders, String strategy) {
        Map<String, Object> payload = createPayload();
        payload.put("strategy", strategy != null ? strategy : "Java");
        payload.put("orders", orders);
        return makeRequest("basketorder", payload);
    }

    /**
     * Place a basket of orders.
     */
    public JsonObject basketorder(List<Map<String, Object>> orders) {
        return basketorder(orders, null);
    }

    /**
     * Place a split order (large order split into smaller chunks).
     *
     * @param symbol    Trading symbol (required)
     * @param action    BUY or SELL (required)
     * @param exchange  Exchange code (required)
     * @param quantity  Total quantity (required)
     * @param splitsize Size of each split order (required)
     * @param priceType Price type (default: MARKET)
     * @param product   Product type (default: MIS)
     * @param strategy  Strategy name (default: Java)
     * @param price     Price for LIMIT orders (optional)
     * @param triggerPrice Trigger price (optional)
     * @param disclosedQuantity Disclosed quantity (optional)
     * @return JsonObject response with results array
     */
    public JsonObject splitorder(String symbol, String action, String exchange,
                                  int quantity, int splitsize, String priceType,
                                  String product, String strategy, String price,
                                  String triggerPrice, String disclosedQuantity) {
        Map<String, Object> payload = createPayload();
        payload.put("symbol", symbol);
        payload.put("action", action);
        payload.put("exchange", exchange);
        payload.put("quantity", String.valueOf(quantity));
        payload.put("splitsize", String.valueOf(splitsize));
        payload.put("pricetype", priceType != null ? priceType : "MARKET");
        payload.put("product", product != null ? product : "MIS");
        payload.put("strategy", strategy != null ? strategy : "Java");

        if (price != null) payload.put("price", price);
        if (triggerPrice != null) payload.put("trigger_price", triggerPrice);
        if (disclosedQuantity != null) payload.put("disclosed_quantity", disclosedQuantity);

        return makeRequest("splitorder", payload);
    }

    /**
     * Place a split order with minimal parameters.
     */
    public JsonObject splitorder(String symbol, String action, String exchange, int quantity, int splitsize) {
        return splitorder(symbol, action, exchange, quantity, splitsize, null, null, null, null, null, null);
    }

    /**
     * Get order status.
     *
     * @param orderId  Order ID (required)
     * @param strategy Strategy name (default: Java)
     * @return JsonObject response with order details
     */
    public JsonObject orderstatus(String orderId, String strategy) {
        Map<String, Object> payload = createPayload();
        payload.put("order_id", orderId);
        payload.put("strategy", strategy != null ? strategy : "Java");
        return makeRequest("orderstatus", payload);
    }

    /**
     * Get order status.
     */
    public JsonObject orderstatus(String orderId) {
        return orderstatus(orderId, null);
    }

    /**
     * Get open position for a symbol.
     *
     * @param symbol   Trading symbol (required)
     * @param exchange Exchange code (required)
     * @param product  Product type (required)
     * @param strategy Strategy name (default: Java)
     * @return JsonObject response with position quantity
     */
    public JsonObject openposition(String symbol, String exchange, String product, String strategy) {
        Map<String, Object> payload = createPayload();
        payload.put("symbol", symbol);
        payload.put("exchange", exchange);
        payload.put("product", product);
        payload.put("strategy", strategy != null ? strategy : "Java");
        return makeRequest("openposition", payload);
    }

    /**
     * Get open position for a symbol.
     */
    public JsonObject openposition(String symbol, String exchange, String product) {
        return openposition(symbol, exchange, product, null);
    }

    /**
     * Modify an existing order.
     *
     * @param orderId   Order ID (required)
     * @param symbol    Trading symbol (required)
     * @param action    BUY or SELL (required)
     * @param exchange  Exchange code (required)
     * @param product   Product type (required)
     * @param quantity  New quantity (required)
     * @param price     New price (required)
     * @param priceType Price type (default: LIMIT)
     * @param strategy  Strategy name (default: Java)
     * @param disclosedQuantity Disclosed quantity (default: 0)
     * @param triggerPrice Trigger price (default: 0)
     * @return JsonObject response
     */
    public JsonObject modifyorder(String orderId, String symbol, String action, String exchange,
                                   String product, int quantity, String price, String priceType,
                                   String strategy, String disclosedQuantity, String triggerPrice) {
        Map<String, Object> payload = createPayload();
        payload.put("order_id", orderId);
        payload.put("symbol", symbol);
        payload.put("action", action);
        payload.put("exchange", exchange);
        payload.put("product", product);
        payload.put("quantity", String.valueOf(quantity));
        payload.put("price", price);
        payload.put("pricetype", priceType != null ? priceType : "LIMIT");
        payload.put("strategy", strategy != null ? strategy : "Java");
        payload.put("disclosed_quantity", disclosedQuantity != null ? disclosedQuantity : "0");
        payload.put("trigger_price", triggerPrice != null ? triggerPrice : "0");
        return makeRequest("modifyorder", payload);
    }

    /**
     * Modify an existing order with minimal parameters.
     */
    public JsonObject modifyorder(String orderId, String symbol, String action, String exchange,
                                   String product, int quantity, String price) {
        return modifyorder(orderId, symbol, action, exchange, product, quantity, price, null, null, null, null);
    }

    /**
     * Cancel an order.
     *
     * @param orderId  Order ID (required)
     * @param strategy Strategy name (default: Java)
     * @return JsonObject response
     */
    public JsonObject cancelorder(String orderId, String strategy) {
        Map<String, Object> payload = createPayload();
        payload.put("order_id", orderId);
        payload.put("strategy", strategy != null ? strategy : "Java");
        return makeRequest("cancelorder", payload);
    }

    /**
     * Cancel an order.
     */
    public JsonObject cancelorder(String orderId) {
        return cancelorder(orderId, null);
    }

    /**
     * Close all positions for a strategy.
     *
     * @param strategy Strategy name (default: Java)
     * @return JsonObject response
     */
    public JsonObject closeposition(String strategy) {
        Map<String, Object> payload = createPayload();
        payload.put("strategy", strategy != null ? strategy : "Java");
        return makeRequest("closeposition", payload);
    }

    /**
     * Close all positions.
     */
    public JsonObject closeposition() {
        return closeposition(null);
    }

    /**
     * Cancel all open orders for a strategy.
     *
     * @param strategy Strategy name (default: Java)
     * @return JsonObject response
     */
    public JsonObject cancelallorder(String strategy) {
        Map<String, Object> payload = createPayload();
        payload.put("strategy", strategy != null ? strategy : "Java");
        return makeRequest("cancelallorder", payload);
    }

    /**
     * Cancel all open orders.
     */
    public JsonObject cancelallorder() {
        return cancelallorder(null);
    }
}
