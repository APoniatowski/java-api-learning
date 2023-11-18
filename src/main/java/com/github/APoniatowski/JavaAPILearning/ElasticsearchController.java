package com.github.APoniatowski.JavaAPILearning;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.xcontent.XContentType;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/elasticsearch")
public class ElasticsearchController {

  @Autowired
  private RestHighLevelClient client;

  @PostMapping("/createIndex")
  public String createIndex(@RequestParam String indexName) throws IOException {
    CreateIndexRequest request = new CreateIndexRequest(indexName);
    CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
    return "Index created: " + createIndexResponse.index();
  }

  @PostMapping("/createDocument")
  public String createDocument(@RequestParam String indexName, @RequestBody Map<String, Object> document)
      throws IOException {
    IndexRequest request = new IndexRequest(indexName);
    request.source(document, XContentType.JSON);
    IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
    return "Document created with ID: " + indexResponse.getId();
  }

  @GetMapping("/getDocument")
  public Map<String, Object> getDocument(@RequestParam String indexName, @RequestParam String id) throws IOException {
    GetRequest getRequest = new GetRequest(indexName, id);
    GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
    return getResponse.getSource();
  }
}
