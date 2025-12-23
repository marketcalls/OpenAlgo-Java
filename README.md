# OpenAlgo Java SDK

OpenAlgo Java SDK for algorithmic trading - Java client library for OpenAlgo API. Supports order placement, market data, options trading, and real-time WebSocket streaming.

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>in.openalgo</groupId>
    <artifactId>openalgo</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

Add the following to your `build.gradle`:

```groovy
implementation 'in.openalgo:openalgo:1.0.0'
```

## Compatibility

| Java Version | Support |
|--------------|---------|
| Java 11 | LTS (Long Term Support) |
| Java 17 | LTS (Long Term Support) |
| Java 21 | LTS (Long Term Support) - Recommended |

## Get the OpenAlgo apikey

Make Sure that your OpenAlgo Application is running. Login to OpenAlgo Application with valid credentials and get the OpenAlgo apikey

For detailed function parameters refer to the [API Documentation](https://docs.openalgo.in/api-documentation/v1)

## Getting Started with OpenAlgo Java SDK

First, import the `OpenAlgo` class and initialize it with your API key:

```java
import in.openalgo.OpenAlgo;
import com.google.gson.JsonObject;

// Replace 'your_api_key_here' with your actual API key
// Specify the host URL with your hosted domain or ngrok domain.
// If running locally in windows then use the default host value.
OpenAlgo client = new OpenAlgo("your_api_key_here");

// Or with custom host
OpenAlgo client = new OpenAlgo("your_api_key_here", "http://127.0.0.1:5000");
```

## Examples

Please refer to the documentation on [order constants](https://docs.openalgo.in/api-documentation/v1/order-constants), and consult the API reference for details on optional parameters

---

# API Reference

## PlaceOrder Example

To place a new market order:

```java
JsonObject response = client.placeorder(
    "YESBANK",   // symbol
    "BUY",       // action
    "NSE",       // exchange
    "MARKET",    // priceType
    "CNC",       // product
    1,           // quantity
    "Java",      // strategy
    null,        // price
    null,        // triggerPrice
    null,        // disclosedQuantity
    null,        // target
    null,        // stoploss
    null         // trailingSl
);
System.out.println("Status: " + response.get("status").getAsString());
System.out.println("OrderId: " + response.get("orderid").getAsString());
```

**Place Market Order Response**

```json
{
  "mode": "analyze",
  "orderid": "25122301278383",
  "status": "success"
}
```

To place a new limit order:

```java
JsonObject response = client.placeorder(
    "YESBANK",   // symbol
    "BUY",       // action
    "NSE",       // exchange
    "LIMIT",     // priceType
    "CNC",       // product
    1,           // quantity
    "Java",      // strategy
    "16",        // price
    "0",         // triggerPrice
    "0",         // disclosedQuantity
    null,        // target
    null,        // stoploss
    null         // trailingSl
);
System.out.println("Status: " + response.get("status").getAsString());
System.out.println("OrderId: " + response.get("orderid").getAsString());
```

**Place Limit Order Response**

```json
{
  "status": "success",
  "orderid": "250408001003813"
}
```

## PlaceSmartOrder Example

To place a smart order considering the current position size:

```java
JsonObject response = client.placesmartorder(
    "TATAMOTORS", // symbol
    "SELL",       // action
    "NSE",        // exchange
    5,            // positionSize
    "MARKET",     // priceType
    "MIS",        // product
    1,            // quantity
    "Java",       // strategy
    null,         // price
    null,         // triggerPrice
    null,         // disclosedQuantity
    null,         // target
    null,         // stoploss
    null          // trailingSl
);
System.out.println("Status: " + response.get("status").getAsString());
System.out.println("OrderId: " + response.get("orderid").getAsString());
```

**Place Smart Market Order Response**

```json
{
  "status": "success",
  "orderid": "250408000997543"
}
```

## BasketOrder Example

To place a new basket order:

```java
List<Map<String, Object>> orders = new ArrayList<>();
orders.add(Map.of(
    "symbol", "BHEL",
    "exchange", "NSE",
    "action", "BUY",
    "quantity", 1,
    "pricetype", "MARKET",
    "product", "MIS"
));
orders.add(Map.of(
    "symbol", "ZOMATO",
    "exchange", "NSE",
    "action", "SELL",
    "quantity", 1,
    "pricetype", "MARKET",
    "product", "MIS"
));

JsonObject response = client.basketorder(orders, "Java");
System.out.println("Status: " + response.get("status").getAsString());
```

**Basket Order Response**

```json
{
  "status": "success",
  "results": [
    {"orderid": "250408000999544", "status": "success", "symbol": "BHEL"},
    {"orderid": "250408000997545", "status": "success", "symbol": "ZOMATO"}
  ]
}
```

## SplitOrder Example

To place a new split order:

```java
JsonObject response = client.splitorder(
    "YESBANK",  // symbol
    "SELL",     // action
    "NSE",      // exchange
    105,        // quantity
    20,         // splitsize
    "MARKET",   // priceType
    "MIS",      // product
    "Java",     // strategy
    null,       // price
    null,       // triggerPrice
    null        // disclosedQuantity
);
System.out.println("Status: " + response.get("status").getAsString());
```

**SplitOrder Response**

```json
{
  "status": "success",
  "split_size": "20",
  "total_quantity": 105,
  "results": [
    {"order_num": 1, "orderid": "250408001021467", "quantity": 20, "status": "success"},
    {"order_num": 2, "orderid": "250408001021459", "quantity": 20, "status": "success"},
    {"order_num": 3, "orderid": "250408001021466", "quantity": 20, "status": "success"},
    {"order_num": 4, "orderid": "250408001021470", "quantity": 20, "status": "success"},
    {"order_num": 5, "orderid": "250408001021471", "quantity": 20, "status": "success"},
    {"order_num": 6, "orderid": "250408001021472", "quantity": 5, "status": "success"}
  ]
}
```

## ModifyOrder Example

To modify an existing order:

```java
JsonObject response = client.modifyorder(
    "250408001002736",  // orderId
    "YESBANK",          // symbol
    "BUY",              // action
    "NSE",              // exchange
    "CNC",              // product
    1,                  // quantity
    "16.5"              // price
);
System.out.println("Status: " + response.get("status").getAsString());
System.out.println("OrderId: " + response.get("orderid").getAsString());
```

**Modify Order Response**

```json
{
  "status": "success",
  "orderid": "250408001002736"
}
```

## CancelOrder Example

To cancel an existing order:

```java
JsonObject response = client.cancelorder("250408001002736", "Java");
System.out.println("Status: " + response.get("status").getAsString());
System.out.println("OrderId: " + response.get("orderid").getAsString());
```

**CancelOrder Response**

```json
{
  "status": "success",
  "orderid": "250408001002736"
}
```

## CancelAllOrder Example

To cancel all open orders and trigger pending orders:

```java
JsonObject response = client.cancelallorder("Java");
System.out.println("Status: " + response.get("status").getAsString());
```

**CancelAllOrder Response**

```json
{
  "status": "success",
  "message": "Canceled 5 orders. Failed to cancel 0 orders.",
  "canceled_orders": ["250408001042620", "250408001042667", "250408001042642"]
}
```

## ClosePosition Example

To close all open positions across various exchanges:

```java
JsonObject response = client.closeposition("Java");
System.out.println("Status: " + response.get("status").getAsString());
```

**ClosePosition Response**

```json
{
  "status": "success",
  "message": "All Open Positions Squared Off"
}
```

## OrderStatus Example

To get the current order status:

```java
JsonObject response = client.orderstatus("250828000185002", "Java");
System.out.println("Status: " + response.get("status").getAsString());
if (response.has("data")) {
    JsonObject data = response.getAsJsonObject("data");
    System.out.println("Order Status: " + data.get("order_status").getAsString());
    System.out.println("Symbol: " + data.get("symbol").getAsString());
}
```

**OrderStatus Response**

```json
{
  "status": "success",
  "data": {
    "action": "BUY",
    "exchange": "NSE",
    "order_status": "complete",
    "orderid": "250828000185002",
    "price": 18.95,
    "quantity": 1,
    "symbol": "YESBANK",
    "timestamp": "28-Aug-2025 09:59:10"
  }
}
```

## OpenPosition Example

To get the current open position:

```java
JsonObject response = client.openposition("YESBANK", "NSE", "MIS", "Java");
System.out.println("Status: " + response.get("status").getAsString());
System.out.println("Quantity: " + response.get("quantity").getAsInt());
```

**OpenPosition Response**

```json
{
  "status": "success",
  "quantity": -10
}
```

## Quotes Example

```java
JsonObject response = client.quotes("RELIANCE", "NSE");
System.out.println("Status: " + response.get("status").getAsString());
if (response.has("data")) {
    JsonObject data = response.getAsJsonObject("data");
    System.out.println("Open: " + data.get("open").getAsDouble());
    System.out.println("High: " + data.get("high").getAsDouble());
    System.out.println("Low: " + data.get("low").getAsDouble());
    System.out.println("LTP: " + data.get("ltp").getAsDouble());
    System.out.println("Volume: " + data.get("volume").getAsLong());
}
```

**Quotes Response**

```json
{
  "status": "success",
  "data": {
    "ask": 1575.4,
    "bid": 0.0,
    "high": 1577.5,
    "low": 1565.3,
    "ltp": 1575.4,
    "oi": 179211500,
    "open": 1573.5,
    "prev_close": 1565.1,
    "volume": 10184852
  }
}
```

## Depth Example

```java
JsonObject response = client.depth("SBIN", "NSE");
System.out.println("Status: " + response.get("status").getAsString());
```

**Depth Response**

```json
{
  "status": "success",
  "data": {
    "ltp": 827.45,
    "open": 825.00,
    "high": 829.35,
    "low": 824.55,
    "volume": 9362799,
    "totalbuyqty": 591351,
    "totalsellqty": 835701,
    "bids": [
      {"price": 827.40, "quantity": 886},
      {"price": 827.35, "quantity": 212}
    ],
    "asks": [
      {"price": 827.45, "quantity": 767},
      {"price": 827.50, "quantity": 115}
    ]
  }
}
```

## History Example

```java
JsonObject response = client.history("SBIN", "NSE", "5m", "2025-12-20", "2025-12-22");
System.out.println("Status: " + response.get("status").getAsString());
if (response.has("data")) {
    JsonArray data = response.getAsJsonArray("data");
    System.out.println("Total Candles: " + data.size());
    // Print first candle
    JsonObject candle = data.get(0).getAsJsonObject();
    System.out.println("Open: " + candle.get("open").getAsDouble());
    System.out.println("High: " + candle.get("high").getAsDouble());
    System.out.println("Low: " + candle.get("low").getAsDouble());
    System.out.println("Close: " + candle.get("close").getAsDouble());
    System.out.println("Volume: " + candle.get("volume").getAsLong());
}
```

**History Response**

```json
{
  "status": "success",
  "data": [
    {
      "close": 981.5,
      "high": 982.0,
      "low": 980.0,
      "open": 981.1,
      "timestamp": 1766375100,
      "volume": 131984
    },
    {
      "close": 981.75,
      "high": 982.6,
      "low": 981.15,
      "open": 981.5,
      "timestamp": 1766375400,
      "volume": 122471
    }
  ]
}
```

## Intervals Example

```java
JsonObject response = client.intervals();
System.out.println("Status: " + response.get("status").getAsString());
```

**Intervals Response**

```json
{
  "status": "success",
  "data": {
    "days": ["D"],
    "hours": ["1h"],
    "minutes": ["1m", "3m", "5m", "10m", "15m", "30m"]
  }
}
```

## Symbol Example

```java
JsonObject response = client.symbol("NIFTY30DEC25FUT", "NFO");
System.out.println("Status: " + response.get("status").getAsString());
```

**Symbol Response**

```json
{
  "status": "success",
  "data": {
    "symbol": "NIFTY30DEC25FUT",
    "name": "NIFTY",
    "exchange": "NFO",
    "instrumenttype": "FUT",
    "lotsize": 75,
    "expiry": "30-DEC-25",
    "freeze_qty": 1800
  }
}
```

## Search Example

```java
JsonObject response = client.search("NIFTY 26000 DEC CE", "NFO");
System.out.println("Status: " + response.get("status").getAsString());
```

**Search Response**

```json
{
  "status": "success",
  "message": "Found 7 matching symbols",
  "data": [
    {
      "symbol": "NIFTY30DEC2526000CE",
      "exchange": "NFO",
      "expiry": "30-DEC-25",
      "lotsize": 75
    }
  ]
}
```

## OptionsOrder Example

To place ATM options order:

```java
JsonObject response = client.optionsorder(
    "NIFTY",      // underlying
    "NSE_INDEX",  // exchange
    "ATM",        // offset
    "CE",         // optionType
    "BUY",        // action
    75            // quantity
);
System.out.println("Status: " + response.get("status").getAsString());
System.out.println("OrderId: " + response.get("orderid").getAsString());
```

**OptionsOrder Response**

```json
{
  "status": "success",
  "orderid": "25102800000006",
  "symbol": "NIFTY30DEC2526200CE",
  "exchange": "NFO",
  "underlying": "NIFTY",
  "underlying_ltp": 26215.55
}
```

## OptionChain Example

```java
JsonObject response = client.optionchain("NIFTY", "NSE_INDEX", "30DEC25", 10);
System.out.println("Status: " + response.get("status").getAsString());
System.out.println("Underlying: " + response.get("underlying").getAsString());
System.out.println("ATM Strike: " + response.get("atm_strike").getAsInt());
```

**OptionChain Response**

```json
{
  "status": "success",
  "underlying": "NIFTY",
  "underlying_ltp": 26215.55,
  "expiry_date": "30DEC25",
  "atm_strike": 26200,
  "chain": [
    {
      "strike": 26100,
      "ce": {"symbol": "NIFTY30DEC2526100CE", "label": "ITM2", "ltp": 490},
      "pe": {"symbol": "NIFTY30DEC2526100PE", "label": "OTM2", "ltp": 193}
    }
  ]
}
```

## OptionGreeks Example

```java
JsonObject response = client.optiongreeks("NIFTY25NOV2526000CE", "NFO");
System.out.println("Status: " + response.get("status").getAsString());
```

**OptionGreeks Response**

```json
{
  "status": "success",
  "symbol": "NIFTY25NOV2526000CE",
  "spot_price": 25966.05,
  "option_price": 435,
  "implied_volatility": 15.6,
  "days_to_expiry": 28.51,
  "greeks": {
    "delta": 0.4967,
    "gamma": 0.000352,
    "theta": -7.919,
    "vega": 28.9489
  }
}
```

## Funds Example

```java
JsonObject response = client.funds();
System.out.println("Status: " + response.get("status").getAsString());
if (response.has("data")) {
    JsonObject data = response.getAsJsonObject("data");
    System.out.println("Available Cash: " + data.get("availablecash").getAsString());
}
```

**Funds Response**

```json
{
  "status": "success",
  "data": {
    "availablecash": "320.66",
    "collateral": "0.00",
    "m2mrealized": "3.27",
    "m2munrealized": "-7.88",
    "utiliseddebits": "679.34"
  }
}
```

## OrderBook Example

```java
JsonObject response = client.orderbook();
System.out.println("Status: " + response.get("status").getAsString());
```

**OrderBook Response**

```json
{
  "status": "success",
  "data": {
    "orders": [
      {
        "action": "BUY",
        "exchange": "NSE",
        "order_status": "complete",
        "orderid": "250408000989443",
        "symbol": "RELIANCE"
      }
    ],
    "statistics": {
      "total_buy_orders": 2,
      "total_completed_orders": 1
    }
  }
}
```

## TradeBook Example

```java
JsonObject response = client.tradebook();
System.out.println("Status: " + response.get("status").getAsString());
```

**TradeBook Response**

```json
{
  "status": "success",
  "data": [
    {
      "action": "BUY",
      "average_price": "1180.1",
      "exchange": "NSE",
      "orderid": "250408000989443",
      "symbol": "RELIANCE",
      "trade_value": "1180.1"
    }
  ]
}
```

## PositionBook Example

```java
JsonObject response = client.positionbook();
System.out.println("Status: " + response.get("status").getAsString());
```

**PositionBook Response**

```json
{
  "status": "success",
  "data": [
    {
      "symbol": "NHPC",
      "exchange": "NSE",
      "quantity": -1,
      "ltp": 83.72,
      "pnl": 0.02
    }
  ]
}
```

## Holdings Example

```java
JsonObject response = client.holdings();
System.out.println("Status: " + response.get("status").getAsString());
```

**Holdings Response**

```json
{
  "status": "success",
  "data": {
    "holdings": [
      {
        "symbol": "RELIANCE",
        "exchange": "NSE",
        "quantity": 1,
        "pnl": -149,
        "pnlpercent": -11.1
      }
    ],
    "statistics": {
      "totalholdingvalue": 1768,
      "totalprofitandloss": -233.15,
      "totalpnlpercentage": -11.65
    }
  }
}
```

## Telegram Alert Example

```java
JsonObject response = client.telegram("<openalgo_loginid>", "NIFTY crossed 26000!", 5);
System.out.println("Status: " + response.get("status").getAsString());
```

**Telegram Alert Response**

```json
{
  "status": "success",
  "message": "Notification sent successfully"
}
```

## Holidays Example

```java
JsonObject response = client.holidays(2025);
System.out.println("Status: " + response.get("status").getAsString());
```

**Holidays Response**

```json
{
  "status": "success",
  "data": [
    {"date": "2025-02-26", "description": "Maha Shivaratri", "holiday_type": "TRADING_HOLIDAY"},
    {"date": "2025-03-14", "description": "Holi", "holiday_type": "TRADING_HOLIDAY"}
  ]
}
```

## Timings Example

```java
JsonObject response = client.timings("2025-12-19");
System.out.println("Status: " + response.get("status").getAsString());
```

**Timings Response**

```json
{
  "status": "success",
  "data": [
    {"exchange": "NSE", "start_time": 1734584100000, "end_time": 1734606600000},
    {"exchange": "BSE", "start_time": 1734584100000, "end_time": 1734606600000}
  ]
}
```

## Analyzer Status Example

```java
JsonObject response = client.analyzerstatus();
System.out.println("Status: " + response.get("status").getAsString());
```

**Analyzer Status Response**

```json
{
  "status": "success",
  "data": {
    "analyze_mode": true,
    "mode": "analyze",
    "total_logs": 2
  }
}
```

## Analyzer Toggle Example

```java
// Switch to analyze mode (simulated responses)
JsonObject response = client.analyzertoggle(true);
System.out.println("Status: " + response.get("status").getAsString());
```

**Analyzer Toggle Response**

```json
{
  "status": "success",
  "data": {
    "analyze_mode": true,
    "mode": "analyze",
    "message": "Analyzer mode switched to analyze",
    "total_logs": 2
  }
}
```

---

# WebSocket Streaming

## LTP Data (Streaming WebSocket)

```java
import in.openalgo.OpenAlgo;
import java.util.*;

// Initialize OpenAlgo client with WebSocket URL
OpenAlgo client = new OpenAlgo.Builder("your_api_key")
    .host("http://127.0.0.1:5000")
    .wsUrl("ws://127.0.0.1:8765")
    .build();

// Define instruments to subscribe for LTP
List<Map<String, String>> instruments = new ArrayList<>();
instruments.add(Map.of("exchange", "MCX", "symbol", "CRUDEOIL16JAN26FUT"));

// Connect and subscribe
client.connect();
client.subscribeLtp(instruments, data -> {
    System.out.println("LTP Update: " + data);
});

// Wait for data
Thread.sleep(3000);

// Get cached LTP data
Map<String, Object> ltpData = client.getLtp("MCX", "CRUDEOIL16JAN26FUT");
System.out.println("LTP Data: " + ltpData);

// Unsubscribe and disconnect
client.unsubscribeLtp(instruments);
client.disconnect();
```

**LTP Response**

```json
{
  "ltp": {
    "MCX": {
      "CRUDEOIL16JAN26FUT": {
        "ltp": 5218.0,
        "timestamp": 1703328453123
      }
    }
  }
}
```

## Quotes (Streaming WebSocket)

```java
// Subscribe to quote stream
client.connect();
client.subscribeQuote(instruments, data -> {
    System.out.println("Quote Update: " + data);
});

// Get cached Quote data
Map<String, Object> quoteData = client.getQuotes("MCX", "CRUDEOIL16JAN26FUT");
```

**Quote Response**

```json
{
  "quote": {
    "MCX": {
      "CRUDEOIL16JAN26FUT": {
        "open": 5124.0,
        "high": 5246.0,
        "low": 5114.0,
        "ltp": 5218.0,
        "volume": 14537,
        "timestamp": 1703328453123
      }
    }
  }
}
```

## Depth (Streaming WebSocket)

```java
// Subscribe to depth stream
client.connect();
client.subscribeDepth(instruments, data -> {
    System.out.println("Depth Update: " + data);
});

// Get cached Depth data
Map<String, Object> depthData = client.getDepth("MCX", "CRUDEOIL16JAN26FUT");
```

**Depth Response**

```json
{
  "depth": {
    "MCX": {
      "CRUDEOIL16JAN26FUT": {
        "ltp": 5218.0,
        "timestamp": 1703328453123,
        "depth": {
          "buy": [
            {"price": 5217.0, "quantity": 2, "orders": 2},
            {"price": 5216.0, "quantity": 16, "orders": 8}
          ],
          "sell": [
            {"price": 5218.0, "quantity": 5, "orders": 3},
            {"price": 5219.0, "quantity": 13, "orders": 7}
          ]
        }
      }
    }
  }
}
```

---

## Error Handling

```java
JsonObject response = client.placeorder("INVALID", "BUY", "NSE");

if (!"success".equals(response.get("status").getAsString())) {
    System.out.println("Error: " + response.get("message").getAsString());
}
```

---

## Complete API Reference

### Order Management

| Method | Description |
|--------|-------------|
| `placeorder()` | Place a new order |
| `placesmartorder()` | Place a smart order with position sizing |
| `modifyorder()` | Modify an existing order |
| `cancelorder()` | Cancel a specific order |
| `cancelallorder()` | Cancel all open orders |
| `closeposition()` | Close all open positions |
| `orderstatus()` | Get status of a specific order |
| `openposition()` | Get current open position quantity |

### Basket & Split Orders

| Method | Description |
|--------|-------------|
| `basketorder()` | Place multiple orders in a single request |
| `splitorder()` | Split large order into smaller chunks |

### Options Trading

| Method | Description |
|--------|-------------|
| `optionsorder()` | Place ATM/ITM/OTM option order |
| `optionsmultiorder()` | Place multi-leg option strategy |
| `optionsymbol()` | Get option symbol by offset |
| `optionchain()` | Get full option chain data |
| `optiongreeks()` | Calculate option Greeks |
| `syntheticfuture()` | Calculate synthetic future price |
| `expiry()` | Get expiry dates for symbol |

### Market Data

| Method | Description |
|--------|-------------|
| `quotes()` | Get real-time quotes for a symbol |
| `multiquotes()` | Get quotes for multiple symbols |
| `depth()` | Get market depth (order book) |
| `history()` | Get historical OHLCV data |
| `intervals()` | Get supported time intervals |

### Symbol & Search

| Method | Description |
|--------|-------------|
| `symbol()` | Get symbol details |
| `search()` | Search for symbols |
| `instruments()` | Download all instruments |

### Account & Portfolio

| Method | Description |
|--------|-------------|
| `funds()` | Get funds and margin details |
| `margin()` | Calculate margin requirements |
| `orderbook()` | Get order book |
| `tradebook()` | Get trade book |
| `positionbook()` | Get position book |
| `holdings()` | Get stock holdings |

### Utilities

| Method | Description |
|--------|-------------|
| `holidays()` | Get trading holidays for a year |
| `timings()` | Get exchange timings for a date |
| `telegram()` | Send Telegram alert message |
| `analyzerstatus()` | Get analyzer mode status |
| `analyzertoggle()` | Toggle analyze/live mode |

### WebSocket Streaming

| Method | Description |
|--------|-------------|
| `connect()` | Connect to WebSocket server |
| `disconnect()` | Disconnect from WebSocket |
| `subscribeLtp()` | Subscribe to LTP updates |
| `unsubscribeLtp()` | Unsubscribe from LTP |
| `subscribeQuote()` | Subscribe to Quote updates |
| `unsubscribeQuote()` | Unsubscribe from Quote |
| `subscribeDepth()` | Subscribe to Depth updates |
| `unsubscribeDepth()` | Unsubscribe from Depth |
| `getLtp()` | Get cached LTP data |
| `getQuotes()` | Get cached Quote data |
| `getDepth()` | Get cached Depth data |

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Links

- [OpenAlgo Platform](https://openalgo.in/)
- [API Documentation](https://docs.openalgo.in/api-documentation/v1)
- [Order Constants](https://docs.openalgo.in/api-documentation/v1/order-constants)
