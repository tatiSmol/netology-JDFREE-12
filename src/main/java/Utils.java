import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class Utils {
    private static final CloseableHttpClient httpClient =
            HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)
                            .setSocketTimeout(30000)
                            .setRedirectsEnabled(false)
                            .build())
                    .build();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static NasaObject nasaObject;

    private static void connect(String url) throws IOException {
        CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
        nasaObject = mapper.readValue(response.getEntity().getContent(), NasaObject.class);
    }

    public static String getPhoto(String url) throws IOException {
        connect(url);
        return nasaObject.getUrl();
    }

    public static String getDescription(String url) throws IOException {
        connect(url);
        return nasaObject.getExplanation();
    }


}
