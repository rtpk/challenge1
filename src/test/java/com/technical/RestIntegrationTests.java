package com.technical;

import com.technical.rx.PathRx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestIntegrationTests  {

    @Autowired
    private PathRx pathRx;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskScheduler taskScheduler;


    @Test
    public void shouldReturnGivenDirs() throws InterruptedException {
        //Given
        restTemplate.getForEntity("http://localhost:8080/addFile?name=/root/test",ResponseEntity.class);
        restTemplate.getForEntity("http://localhost:8080/addFile?name=/root/test2",ResponseEntity.class);

        //When
        ResponseEntity responseEntity = restTemplate.getForEntity("http://localhost:8080/start",String.class);
        System.out.println(responseEntity.getBody());

        String result =  responseEntity.getBody().toString();
        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result).contains("/root/test","root/test2");

    }

    //TODO
    //websockety/rest i caly mechanizm dookola
    //testy integracyjny pokazujace wypychanie przez websockety foldery do klientow i zmiany na nich

    /*
    Step4: Create REST / Websocket endpoints to allow remotely watching folder(s) defined on server-side
    Use rest to initiate connection - initially you need to as about current folder structure
    Use websocket to send data to client (data means current folder structure and future changes in folde structure)
    Define simple rest endpot to allow remotely create folder / files for testing purposes
    Include full integration tests
    */

}