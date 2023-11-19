package com.github.APoniatowski.JavaAPILearning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.IndicesClient;

import java.io.IOException;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ElasticsearchController.class)
public class ElasticsearchControllerTests {

  @MockBean
  private RestHighLevelClient restHighLevelClient;
  private IndicesClient mockIndicesClient;
  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  public void setUp() throws IOException {
    mockIndicesClient = mock(IndicesClient.class);
    when(restHighLevelClient.indices()).thenReturn(mockIndicesClient);
  }

  @Test
  public void testCreateDocument() throws Exception {
    IndexResponse indexResponse = mock(IndexResponse.class);
    when(indexResponse.getId()).thenReturn("1");
    when(restHighLevelClient.index(any(IndexRequest.class), any(RequestOptions.class))).thenReturn(indexResponse);

    mockMvc.perform(post("/elasticsearch/createDocument")
        .param("indexName", "testindex")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"field1\": \"value1\", \"field2\": \"value2\"}"))
        .andExpect(status().isOk());
  }

  @Test
  public void testCreateIndex() throws Exception {
    CreateIndexResponse createIndexResponse = mock(CreateIndexResponse.class);
    when(createIndexResponse.index()).thenReturn("testIndex");
    when(mockIndicesClient.create(any(CreateIndexRequest.class), any(RequestOptions.class)))
        .thenReturn(createIndexResponse);

    mockMvc.perform(post("/elasticsearch/createIndex")
        .param("indexName", "testindex"))
        .andExpect(status().isOk());
  }

  @Test
  public void testGetDocument() throws Exception {
    GetResponse getResponse = mock(GetResponse.class);
    when(getResponse.getSource()).thenReturn(Map.of("field1", "value1"));
    when(restHighLevelClient.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

    mockMvc.perform(get("/elasticsearch/getDocument")
        .param("indexName", "testindex")
        .param("id", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.field1").value("value1"));
  }
}
