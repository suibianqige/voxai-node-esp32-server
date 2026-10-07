package com.voxai.dialogue.llm.tool.function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxai.ai.llm.tool.ToolCallStringResultConverter;
import com.voxai.ai.tool.ToolsGlobalRegistry;
import com.voxai.ai.tool.session.ToolSession;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 天气查询工具。
 * 使用 Open-Meteo（https://open-meteo.com）免费 API，无需申请 key：
 * 1. 地理编码接口：城市名 -> 经纬度
 * 2. 天气预报接口：经纬度 -> 当前天气
 * 返回结构化数据给 LLM，由 LLM 组织成口语化回答。
 */
@Component
public class WeatherFunction implements ToolsGlobalRegistry.GlobalFunction {
    private static final String TOOL_NAME = "query_weather";

    private static final String GEOCODING_URL = "https://geocoding-api.open-meteo.com/v1/search?name=%s&count=1&language=zh&format=json";
    private static final String FORECAST_URL = "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m&timezone=auto";

    /** WMO 天气代码 -> 中文描述 */
    private static final Map<Integer, String> WMO_CODES = Map.ofEntries(
            Map.entry(0, "晴"),
            Map.entry(1, "基本晴"),
            Map.entry(2, "多云"),
            Map.entry(3, "阴"),
            Map.entry(45, "雾"),
            Map.entry(48, "雾凇"),
            Map.entry(51, "小毛毛雨"),
            Map.entry(53, "毛毛雨"),
            Map.entry(55, "大毛毛雨"),
            Map.entry(56, "冻毛毛雨"),
            Map.entry(57, "强冻毛毛雨"),
            Map.entry(61, "小雨"),
            Map.entry(63, "中雨"),
            Map.entry(65, "大雨"),
            Map.entry(66, "冻雨"),
            Map.entry(67, "强冻雨"),
            Map.entry(71, "小雪"),
            Map.entry(73, "中雪"),
            Map.entry(75, "大雪"),
            Map.entry(77, "雪粒"),
            Map.entry(80, "小阵雨"),
            Map.entry(81, "阵雨"),
            Map.entry(82, "强阵雨"),
            Map.entry(85, "小阵雪"),
            Map.entry(86, "大阵雪"),
            Map.entry(95, "雷阵雨"),
            Map.entry(96, "雷阵雨伴小冰雹"),
            Map.entry(99, "雷阵雨伴大冰雹")
    );

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    ToolCallback toolCallback = FunctionToolCallback
            .builder(TOOL_NAME, (Map<String, String> params, org.springframework.ai.chat.model.ToolContext toolContext) -> {
                String city = params.get("city");
                if (city == null || city.isBlank()) {
                    return "查询失败：请告诉我要查哪个城市的天气";
                }
                try {
                    return queryWeather(city.trim());
                } catch (Exception e) {
                    return "查询失败：" + e.getMessage();
                }
            })
            .description("查询指定城市的当前天气（气温、体感温度、湿度、风力、天气现象）。当用户询问天气相关问题时调用。")
            .inputSchema("""
                        {
                            "type": "object",
                            "properties": {
                                "city": {
                                    "type": "string",
                                    "description": "城市名称，如：北京、上海、深圳"
                                }
                            },
                            "required": ["city"]
                        }
                    """)
            .inputType(Map.class)
            .toolCallResultConverter(ToolCallStringResultConverter.INSTANCE)
            .build();

    /**
     * 查询指定城市当前天气，返回给 LLM 的结构化中文描述。
     */
    private String queryWeather(String city) throws Exception {
        // 1. 城市名 -> 经纬度
        String geoUrl = String.format(GEOCODING_URL, URLEncoder.encode(city, StandardCharsets.UTF_8));
        JsonNode geoRoot = httpGetJson(geoUrl);
        JsonNode results = geoRoot.path("results");
        if (!results.isArray() || results.isEmpty()) {
            return "查询失败：找不到城市「" + city + "」，请确认城市名称";
        }
        JsonNode location = results.get(0);
        String latitude = location.path("latitude").asText();
        String longitude = location.path("longitude").asText();
        String resolvedName = location.path("name").asText(city);
        String admin = location.path("admin1").asText("");

        // 2. 经纬度 -> 当前天气
        String forecastUrl = String.format(FORECAST_URL, latitude, longitude);
        JsonNode weatherRoot = httpGetJson(forecastUrl);
        JsonNode current = weatherRoot.path("current");
        if (current.isMissingNode()) {
            return "查询失败：天气服务未返回数据";
        }

        double temperature = current.path("temperature_2m").asDouble();
        double apparent = current.path("apparent_temperature").asDouble();
        int humidity = current.path("relative_humidity_2m").asInt();
        double windSpeed = current.path("wind_speed_10m").asDouble();
        String condition = WMO_CODES.getOrDefault(current.path("weather_code").asInt(), "未知天气");

        return String.format(
                "%s%s当前天气：%s，气温 %.1f 摄氏度，体感 %.1f 摄氏度，相对湿度 %d%%，风速 %.1f 公里/小时。请根据这些数据用口语自然地告诉用户。",
                resolvedName, admin.isEmpty() ? "" : "（" + admin + "）",
                condition, temperature, apparent, humidity, windSpeed);
    }

    private JsonNode httpGetJson(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("User-Agent", "voxai-weather-tool")
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IllegalStateException("天气服务返回 HTTP " + response.statusCode());
        }
        return objectMapper.readTree(response.body());
    }

    @Override
    public ToolCallback getFunctionCallTool(ToolSession toolSession) {
        return toolCallback;
    }

    @Override
    public String getToolName() {
        return TOOL_NAME;
    }

    @Override
    public String getToolDescription() {
        return "天气查询";
    }
}
