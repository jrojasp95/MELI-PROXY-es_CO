/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ejemplo_proxy;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.Query;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

/**
 *
 * @author Jair
 */
@Service
@Slf4j
public class RequestControlConditions {

    private ElasticsearchOperations elasticsearchRestTemplate;
    private final RestHighLevelClient client;

    public RequestControlConditions(RestHighLevelClient client) {
        this.client = client;
    }

    
    public boolean MaxRequestOneFieldFilter(String field,String clientIp, long MaxLimit) {

        CountRequest countRequest = new CountRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //searchSourceBuilder.query(QueryBuilders.termQuery("client.ip", "192.168.31.155"));
        searchSourceBuilder.query(QueryBuilders.boolQuery()
            .must(QueryBuilders.termQuery(field,clientIp))
            .must(QueryBuilders.rangeQuery("@timestamp").gte("now-1d")));
        countRequest.indices("apm-7.15.0-transaction-*");
        countRequest.source(searchSourceBuilder);
       long total=0;
        
                
        try {
          CountResponse   response = client.count(countRequest, RequestOptions.DEFAULT);
          total = response.getCount();
        } catch (IOException ex) {
            Logger.getLogger(RequestControlConditions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total<MaxLimit;
    }
    
    
    public boolean MaxRequestTwoFieldFilter(String field1,String value1,String field2,String value2, long MaxLimit) {

        CountRequest countRequest = new CountRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //searchSourceBuilder.query(QueryBuilders.termQuery("client.ip", "192.168.31.155"));
        searchSourceBuilder.query(QueryBuilders.boolQuery()
            .must(QueryBuilders.termQuery(field1,value1))
            .must(QueryBuilders.termQuery(field2,value2))
            .must(QueryBuilders.rangeQuery("@timestamp").gte("now-1d")));
        countRequest.indices("apm-7.15.0-transaction-*");
        countRequest.source(searchSourceBuilder);
       long total=0;
        
                
        try {
          CountResponse   response = client.count(countRequest, RequestOptions.DEFAULT);
          total = response.getCount();
        } catch (IOException ex) {
            Logger.getLogger(RequestControlConditions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total<MaxLimit;
    }

}
