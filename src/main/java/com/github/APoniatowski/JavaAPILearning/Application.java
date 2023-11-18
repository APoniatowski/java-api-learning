package com.github.APoniatowski.JavaAPILearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class Application {

  private static RestHighLevelClient client;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Autowired
  public void setClient(RestHighLevelClient client) {
    Application.client = client;
  }

  @PostConstruct
  public void checkElasticsearchConnection() {
    try {
      Request request = new Request("GET", "/");
      Response response = client.getLowLevelClient().performRequest(request);
      if (response.getStatusLine().getStatusCode() == 200) {
        System.out.println("Elasticsearch is reachable.");
      } else {
        System.out.println("Elasticsearch is not reachable. Status Code: " + response.getStatusLine().getStatusCode());
      }
    } catch (IOException e) {
      System.out.println("Elasticsearch connection check failed: " + e.getMessage());
    }
  }

  public static String getDefaultKeystorePath() {
    String os = System.getProperty("os.name").toLowerCase();
    String javaHome = System.getProperty("java.home");

    String keystorePath;

    if (os.contains("win")) {
      keystorePath = javaHome + "\\lib\\security\\cacerts";
    } else {
      keystorePath = javaHome + "/lib/security/cacerts";
    }

    return keystorePath;
  }

}
