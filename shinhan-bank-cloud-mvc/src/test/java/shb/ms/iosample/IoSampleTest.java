package shb.ms.iosample;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT, properties = {"server.port=8080"})
public class IoSampleTest {

    private static final String THREE_SECOND_URL = "http://localhost:8080/3second";
    private static final int LOOP_COUNT = 100;

    private final CountDownLatch count = new CountDownLatch(LOOP_COUNT);

    @Before
    public void setup() {
        // System.setProperty("reactor.netty.ioWorkerCount", "1");
    }

    @Test
    public void blocking2() throws InterruptedException {
        final AsyncRestTemplate restTemplate = new AsyncRestTemplate();

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < LOOP_COUNT; i++) {
            System.out.println("loop count : " + (i + 1));
            final ListenableFuture<ResponseEntity<String>> response =
                    restTemplate.exchange(THREE_SECOND_URL, HttpMethod.GET, HttpEntity.EMPTY, String.class);

            response.addCallback(result -> {
                count.countDown();
                System.out.println(result.getBody());
            }, ex -> {
                System.out.println(ex);
            });
        }


        count.await(50, TimeUnit.SECONDS);
        stopWatch.stop();

        System.out.println(stopWatch.getTotalTimeSeconds());
    }

}
