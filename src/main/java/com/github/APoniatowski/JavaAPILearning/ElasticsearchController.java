package com.github.APoniatowski.JavaAPILearning;

import com.github.APoniatowski.JavaAPILearning.util.UtilityClass;

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
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/elasticsearch")
public class ElasticsearchController {

  @Autowired
  private RestHighLevelClient client;

  @PostMapping("/createIndex")
  public ResponseEntity<String> createIndex(@RequestParam String indexName) {
    try {
      CreateIndexRequest request = new CreateIndexRequest(indexName);
      CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
      return ResponseEntity.ok("Index created: " + createIndexResponse.index());
    } catch (IOException e) {
      UtilityClass.logError("Error creating index: " + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating index");
    }
  }

  @PostMapping("/createDocument")
  public ResponseEntity<String> createDocument(@RequestParam String indexName,
      @RequestBody Map<String, Object> document) {
    try {
      IndexRequest request = new IndexRequest(indexName);
      request.source(document, XContentType.JSON);
      IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
      return ResponseEntity.ok("Document created with ID: " + indexResponse.getId());
    } catch (IOException e) {
      UtilityClass.logError("Error creating document: " + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating document");
    }
  }

  @GetMapping("/getDocument")
  public ResponseEntity<Map<String, Object>> getDocument(@RequestParam String indexName, @RequestParam String id) {
    try {
      GetRequest getRequest = new GetRequest(indexName, id);
      GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
      return ResponseEntity.ok(getResponse.getSource());
    } catch (IOException e) {
      UtilityClass.logError("Error getting document: " + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}
