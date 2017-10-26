package de.audibene.core.domain.events.listener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import({TestService.class, TestSubjectProvider.class})
@SpringBootTest(classes = DomainEventAutoConfiguration.class)
public class TestApplicationTests {

    @Autowired
    private TestService service;

    @Test
    public void shouldRunSentAndReceiveDomainEvent() throws Exception {
        service.send();

        TestDomainEvent missed = service.getQueue().poll();
        assertThat(missed).isNull();

        TestDomainEvent received = service.getQueue().poll(1, SECONDS);
        assertThat(received).isNotNull();
    }

}
