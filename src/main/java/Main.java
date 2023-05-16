import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    // сюда отправляем запрос
    public static final String URI = "https://api.nasa.gov/planetary/apod?" +
                                            "api_key=wGy5qb16TF7XeGYu6WGUu5sMCpm2XAbcWHT5Hb2Y";
    // сущность, которая будет преобразовывать ответ в объект NASA
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        // HTTP клиент, который будет отправлять запросы
        CloseableHttpClient httpClient =
                HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom()
                                .setConnectTimeout(5000) // максимальное время ожидание подключения к серверу
                                .setSocketTimeout(30000) // максимальное время ожидания получения данных
                                .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                                .build())
                        .build();

        // Отправление запроса и получение ответа
        CloseableHttpResponse response = httpClient.execute(new HttpGet(URI));

        // Преобразование ответа в java-object NasaObject
        NasaObject nasaObject = mapper.readValue(response.getEntity().getContent(), NasaObject.class);
        System.out.println(nasaObject);

        // Отправление запроса и получение ответа с картинкой
        CloseableHttpResponse pictureResponse = httpClient.execute(new HttpGet(nasaObject.getUrl()));

        // Формирование автоматического названия для файла
        String[] arr = nasaObject.getUrl().split("/");
        String fileName = arr[arr.length - 1];

        // Сохранение в файл
        HttpEntity entity = pictureResponse.getEntity();
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            entity.writeTo(fos);
        }

        httpClient.close();
    }
}