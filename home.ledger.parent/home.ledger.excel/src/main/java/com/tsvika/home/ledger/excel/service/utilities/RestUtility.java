package com.tsvika.home.ledger.excel.service.utilities;

import com.tsvika.home.ledger.dto.*;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public@Component
class RestUtility implements IRestUtility {

    @Override
    public CategoryDisplayable postCategory(String url, CategoryForCreation item){
        HttpEntity<CategoryForCreation> requestEntity = gettHttpEntity(item);
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
    public LineDisplayable postLine(String url, LineForCreation item){
        HttpEntity<LineForCreation> requestEntity = gettHttpEntity(item);
        ResponseEntity<LineDisplayable> responseEntity = new RestTemplate().exchange(
                url,
                HttpMethod.POST,
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

    @Override
    public List<CategoryDisplayable> getCategories(String url){
        HttpEntity<?> requestEntity = gettHttpEntity(null);
        ResponseEntity<CategoryDisplayable[]> responseEntity = new RestTemplate().exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                CategoryDisplayable[].class
        );

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return Arrays.asList(responseEntity.getBody());
        }

        return null;
    }

    @Override
    public List<LineDisplayable> getLines(String url){
        HttpEntity<?> requestEntity = gettHttpEntity(null);
        ResponseEntity<LineDisplayable[]> responseEntity = new RestTemplate().exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                LineDisplayable[].class
        );

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return Arrays.asList(responseEntity.getBody());
        }

        return null;
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

    public <T> HttpEntity<T> gettHttpEntity(T item) {
        HttpHeaders requestHeaders = getHttpHeaders();

        //request entity is created with request body and headers
        return new HttpEntity<>(item, requestHeaders);
    }

    public HttpHeaders getHttpHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return requestHeaders;
    }
}
