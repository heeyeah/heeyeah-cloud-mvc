package shb.ms.iosample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class IoSampleController {
    private final AtomicInteger atomicInteger = new AtomicInteger();


    @GetMapping("/3second")
    public String threeSecond() throws InterruptedException {
        Thread.sleep(3000);
        return "success - " + this.atomicInteger.incrementAndGet();
    }
}
