package com.github.APoniatowski.JavaAPILearning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClient;

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
    if (System.getProperty("skipElasticsearchCheck") == null) {
      if (client == null) {
        System.out.println("Elasticsearch client is null.");
        return;
      }

      RestClient lowLevelClient = client.getLowLevelClient();
      if (lowLevelClient == null) {
        System.out.println("Elasticsearch low-level client is null.");
        return;
      }

      Response response = null;
      try {
        Request request = new Request("GET", "/");
        response = lowLevelClient.performRequest(request);
      } catch (IOException e) {
        System.out.println("Elasticsearch connection check failed: " + e.getMessage());
      }

      if (response != null && response.getStatusLine().getStatusCode() == 200) {
        System.out.println("Elasticsearch is reachable.");
      } else {
        System.out.println("Elasticsearch is not reachable or response is null.");
      }
    }
  }

  public static String getDefaultKeystorePath() {
    String os = System.getProperty("os.name").toLowerCase();
    String javaHome = System.getProperty("java.home");
    return os.contains("win") ? javaHome + "\\lib\\security\\cacerts" : javaHome + "/lib/security/cacerts";
  }
}
