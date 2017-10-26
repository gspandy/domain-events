package de.audibene.core.domain.events.listener;

import de.audibene.core.domain.events.listener.support.DomainEventListenerFactory;
import de.audibene.core.domain.events.subject.SubjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DomainEventAutoConfiguration {

    @Bean
    public DomainEventListenerFactory domainEventListenerFactory(
            final SubjectProvider subjectProvider,
            final Environment environment
    ) {
        return new DomainEventListenerFactory(subjectProvider, environment);
    }

}
