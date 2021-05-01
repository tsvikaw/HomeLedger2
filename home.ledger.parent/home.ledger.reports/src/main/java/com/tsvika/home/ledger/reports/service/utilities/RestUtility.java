package com.tsvika.home.ledger.reports.service.utilities;

import com.tsvika.home.ledger.dto.*;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component

public class RestUtility implements IRestUtility {

    public static final String DESCENDANTS = "descendants";
    public static final String CHILDREN = "children";

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
    public List<String> getAccounts(String url){
        HttpEntity<?> requestEntity = gettHttpEntity(null);
        ResponseEntity<String[]> responseEntity = new RestTemplate().exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String[].class
        );

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return Arrays.asList(responseEntity.getBody());
        }

        return null;
    }

    @Override
    public List<CategoryDisplayable> getDecendentCategories(String url, String categoryId){
        HttpEntity<?> requestEntity = gettHttpEntity(null);
        ResponseEntity<CategoryDisplayable[]> responseEntity = new RestTemplate().exchange(
                String.format("%s/" + DESCENDANTS + "/%s", url, categoryId),
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
    public List<CategoryDisplayable> getChildrenCategories(String url, String categoryId){
        HttpEntity<?> requestEntity = gettHttpEntity(null);
        ResponseEntity<CategoryDisplayable[]> responseEntity = new RestTemplate().exchange(
                String.format("%s/" + CHILDREN + "/%s", url, categoryId),
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
    public LineDisplayableQueryResult getLinesByQuery(String url, LineQuery lineQuery){
        HttpEntity<LineQuery> requestEntity = gettHttpEntity(lineQuery);
        ResponseEntity<LineDisplayableQueryResult> responseEntity = new RestTemplate().exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                LineDisplayableQueryResult.class
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
