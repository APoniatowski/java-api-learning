package com.github.APoniatowski.JavaAPILearning;

import com.github.APoniatowski.JavaAPILearning.util.UtilityClass;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

  private static final String KEYSTORE_PASSWORD = "changeit";
  private static final String CERT_ALIAS = "elasticsearch";

  @Bean
  public RestHighLevelClient client() {
    RestHighLevelClient client = null;
    try {
      String keystorePath = UtilityClass.getDefaultKeystorePath();
      CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
      credentialsProvider.setCredentials(AuthScope.ANY,
          new UsernamePasswordCredentials("elastic", "changeme")); // TODO Maybe add functionality to check ENV vars

      RestClientBuilder builder;

      try {
        if (UtilityClass.isCertificateValid(keystorePath, KEYSTORE_PASSWORD, CERT_ALIAS)) {
          builder = RestClient.builder(new HttpHost("localhost", 9200, "https"))
              .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                  .setDefaultCredentialsProvider(credentialsProvider));
        } else {
          builder = RestClient.builder(new HttpHost("localhost", 9200, "http"))
              .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                  .setDefaultCredentialsProvider(credentialsProvider));
        }
      } catch (Exception e) {
        UtilityClass.logError("Error validating certificate: " + e.getMessage(), e);
        builder = RestClient.builder(new HttpHost("localhost", 9200, "http"))
            .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                .setDefaultCredentialsProvider(credentialsProvider));
      }

      client = new RestHighLevelClient(builder);
    } catch (Exception e) {
      UtilityClass.logError("Error creating Elasticsearch client: " + e.getMessage(), e);
    }
    return client;
  }

}
