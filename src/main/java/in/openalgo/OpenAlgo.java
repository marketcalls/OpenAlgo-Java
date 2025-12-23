package in.openalgo;

/**
 * OpenAlgo Java SDK - Main API client class.
 *
 * Provides access to all OpenAlgo API endpoints for algorithmic trading:
 * - Order Management: Place, modify, cancel orders
 * - Market Data: Quotes, depth, historical data
 * - Account: Funds, positions, holdings
 * - Options: Greeks, option chains, options orders
 * - Utilities: Telegram alerts, market holidays, timings
 * - WebSocket Feed: Real-time LTP, quotes, and market depth
 *
 * Example usage:
 * <pre>
 * OpenAlgo client = new OpenAlgo("your-api-key");
 *
 * // Place an order
 * JsonObject response = client.placeorder("RELIANCE", "BUY", "NSE");
 *
 * // Get quotes
 * JsonObject quotes = client.quotes("RELIANCE", "NSE");
 *
 * // WebSocket streaming
 * client.connect();
 * List&lt;Map&lt;String, String&gt;&gt; instruments = new ArrayList&lt;&gt;();
 * instruments.add(Map.of("symbol", "RELIANCE", "exchange", "NSE"));
 * client.subscribeLtp(instruments, data -&gt; System.out.println(data));
 * </pre>
 *
 * @author OpenAlgo
 * @version 1.0.0
 */
public class OpenAlgo extends FeedApi {

    private static final String DEFAULT_HOST = "http://127.0.0.1:5000";
    private static final String DEFAULT_VERSION = "v1";
    private static final double DEFAULT_TIMEOUT = 120.0;
    private static final int DEFAULT_WS_PORT = 8765;

    /**
     * Creates a new OpenAlgo client with default settings.
     *
     * @param apiKey API key for authentication
     */
    public OpenAlgo(String apiKey) {
        this(apiKey, DEFAULT_HOST, DEFAULT_VERSION, DEFAULT_TIMEOUT, DEFAULT_WS_PORT, null);
    }

    /**
     * Creates a new OpenAlgo client with custom host.
     *
     * @param apiKey API key for authentication
     * @param host   Base host URL
     */
    public OpenAlgo(String apiKey, String host) {
        this(apiKey, host, DEFAULT_VERSION, DEFAULT_TIMEOUT, DEFAULT_WS_PORT, null);
    }

    /**
     * Creates a new OpenAlgo client with custom host and version.
     *
     * @param apiKey  API key for authentication
     * @param host    Base host URL
     * @param version API version
     */
    public OpenAlgo(String apiKey, String host, String version) {
        this(apiKey, host, version, DEFAULT_TIMEOUT, DEFAULT_WS_PORT, null);
    }

    /**
     * Creates a new OpenAlgo client with custom host, version, and timeout.
     *
     * @param apiKey  API key for authentication
     * @param host    Base host URL
     * @param version API version
     * @param timeout Request timeout in seconds
     */
    public OpenAlgo(String apiKey, String host, String version, double timeout) {
        this(apiKey, host, version, timeout, DEFAULT_WS_PORT, null);
    }

    /**
     * Creates a new OpenAlgo client with all custom settings.
     *
     * @param apiKey  API key for authentication
     * @param host    Base host URL (default: http://127.0.0.1:5000)
     * @param version API version (default: v1)
     * @param timeout Request timeout in seconds (default: 120)
     * @param wsPort  WebSocket port (default: 8765)
     * @param wsUrl   Custom WebSocket URL (optional)
     */
    public OpenAlgo(String apiKey, String host, String version, double timeout, int wsPort, String wsUrl) {
        super(
            apiKey,
            host != null ? host : DEFAULT_HOST,
            version != null ? version : DEFAULT_VERSION,
            timeout > 0 ? timeout : DEFAULT_TIMEOUT,
            wsPort > 0 ? wsPort : DEFAULT_WS_PORT,
            wsUrl
        );
    }

    /**
     * Builder class for creating OpenAlgo instances with custom configuration.
     */
    public static class Builder {
        private String apiKey;
        private String host = DEFAULT_HOST;
        private String version = DEFAULT_VERSION;
        private double timeout = DEFAULT_TIMEOUT;
        private int wsPort = DEFAULT_WS_PORT;
        private String wsUrl = null;

        /**
         * Creates a new Builder with the required API key.
         *
         * @param apiKey API key for authentication
         */
        public Builder(String apiKey) {
            this.apiKey = apiKey;
        }

        /**
         * Sets the base host URL.
         *
         * @param host Base host URL
         * @return Builder instance
         */
        public Builder host(String host) {
            this.host = host;
            return this;
        }

        /**
         * Sets the API version.
         *
         * @param version API version
         * @return Builder instance
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * Sets the request timeout.
         *
         * @param timeout Timeout in seconds
         * @return Builder instance
         */
        public Builder timeout(double timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Sets the WebSocket port.
         *
         * @param wsPort WebSocket port
         * @return Builder instance
         */
        public Builder wsPort(int wsPort) {
            this.wsPort = wsPort;
            return this;
        }

        /**
         * Sets a custom WebSocket URL.
         *
         * @param wsUrl WebSocket URL
         * @return Builder instance
         */
        public Builder wsUrl(String wsUrl) {
            this.wsUrl = wsUrl;
            return this;
        }

        /**
         * Builds the OpenAlgo instance.
         *
         * @return OpenAlgo instance
         */
        public OpenAlgo build() {
            return new OpenAlgo(apiKey, host, version, timeout, wsPort, wsUrl);
        }
    }
}
