package com.technical;

import com.technical.rx.PathRx;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import rx.subjects.ReplaySubject;

import java.io.IOException;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created by Robert Piotrowski on 08/12/2016.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WebsocketsTests {

    @Autowired
    private PathRx pathRx;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldSubscribeOneToPathRx() throws InterruptedException {
        //Given
        assertThat(pathRx.getSubscriberQueue().size()).isEqualTo(0);

        //When
        HttpStatus httpStatus = restTemplate.getForEntity("http://localhost:8080/start",ResponseEntity.class).getStatusCode();

        //Then
        assertThat(pathRx.getSubscriberQueue().size()).isEqualTo(1);
    }


    @Test
    public void shouldPathRxEmitOneElement() throws InterruptedException, IOException {
        //Given
        HttpStatus httpStatus = restTemplate.getForEntity("http://localhost:8080/start",ResponseEntity.class).getStatusCode();
        ReplaySubject<WatchEvent<?>> replaySubject = ReplaySubject.create();
        pathRx.watch().subscribe(replaySubject);

        //When
        restTemplate.getForEntity("http://localhost:8080/addFile?name=/root/test",ResponseEntity.class);

        //Then
        final WatchEvent<?> event = replaySubject.toBlocking().first();
        Assertions.assertThat(event.context().toString()).isEqualTo("test");
    }



    @Test
    public void shouldConnectWebsocket() { //full ws client? ask
        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());

        SockJsClient sockJsClient = new SockJsClient(transports);
       // sockJsClient.doHandshake(new MyWebSocketHandler(), "ws://example.com:8080/sockjs");
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
