package com.github.APoniatowski.JavaAPILearning;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContexts;
import com.github.APoniatowski.JavaAPILearning.util.UtilityClass;
import org.elasticsearch.client.RestClientBuilder;

@Configuration
public class ElasticsearchConfig {

  private static final String KEYSTORE_PASSWORD = "changeit";
  private static final String CERT_ALIAS = "elasticsearch";

  @Bean
  public RestHighLevelClient client() throws Exception {
    String keystorePath = UtilityClass.getDefaultKeystorePath();
    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(AuthScope.ANY,
        new UsernamePasswordCredentials("elastic", "changeme"));

    RestClientBuilder builder;

    if (UtilityClass.isCertificateValid(keystorePath, KEYSTORE_PASSWORD, CERT_ALIAS)) {
      builder = RestClient.builder(new HttpHost("localhost", 9200, "https"))
          .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
              .setDefaultCredentialsProvider(credentialsProvider));
    } else {
      builder = RestClient.builder(new HttpHost("localhost", 9200, "http"))
          .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
              .setDefaultCredentialsProvider(credentialsProvider));
    }

    return new RestHighLevelClient(builder);
  }
}
