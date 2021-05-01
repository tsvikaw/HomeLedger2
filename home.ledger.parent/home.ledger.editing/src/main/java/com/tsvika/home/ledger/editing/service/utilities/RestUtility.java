package com.tsvika.home.ledger.editing.service.utilities;

import com.tsvika.home.ledger.dto.*;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component

public class RestUtility implements IRestUtility {

    private <T> HttpEntity<T> gettHttpEntity(T item) {
        HttpHeaders requestHeaders = getHttpHeaders();

        //request entity is created with request body and headers
        return new HttpEntity<>(item, requestHeaders);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return requestHeaders;
    }

    @Override
    public CategoryDisplayable createCategory(String url, CategoryForCreation categoryForCreation) {
        HttpEntity<CategoryForCreation> requestEntity = gettHttpEntity(categoryForCreation);
        ResponseEntity<CategoryDisplayable> responseEntity = new RestTemplate().exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                CategoryDisplayable.class
        );

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return responseEntity.getBody();
        }

        return null;
    }

    @Override
    public CategoryDisplayable updateCategory(String url, String id, CategoryForUpdate categoryForUpdate) {
        HttpEntity<CategoryForUpdate> requestEntity = gettHttpEntity(categoryForUpdate);
        ResponseEntity<CategoryDisplayable> responseEntity = new RestTemplate().exchange(
                url + "/" + id,
                HttpMethod.PUT,
                requestEntity,
                CategoryDisplayable.class
        );

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return responseEntity.getBody();
        }

        return null;
    }

    @Override
    public boolean deleteCategory(String url, String id) {
        HttpEntity<String> requestEntity = gettHttpEntity(id);
        ResponseEntity responseEntity = new RestTemplate().exchange(
                url + "/" + id,
                HttpMethod.DELETE,
                requestEntity,
                void.class
        );

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return true;
        }

        return false;
    }

    @Override
    public LineDisplayable getLine(String url, String id){
        HttpEntity<?> requestEntity = gettHttpEntity(null);
        ResponseEntity<LineDisplayable> responseEntity = new RestTemplate().exchange(
                url +"/" + id,
                HttpMethod.GET,
                requestEntity,
                LineDisplayable.class
        );

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return responseEntity.getBody();
        }

        return null;
    }

    @Override
    public LineDisplayable putLine(String url, LineForUpdate item){
        HttpEntity<LineForUpdate> requestEntity = gettHttpEntity(item);
        ResponseEntity<LineDisplayable> responseEntity = new RestTemplate().exchange(
                url + "/" + item.getId(),
                HttpMethod.PUT,
                requestEntity,
                LineDisplayable.class
        );

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return responseEntity.getBody();
        }

        return null;
    }
}
