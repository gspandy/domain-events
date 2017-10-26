package de.audibene.core.domain.events.azure;

import de.audibene.core.domain.events.azure.properties.AzureSubjectsProperties;
import de.audibene.core.domain.events.azure.subject.AzureSubjectProvider;
import de.audibene.core.domain.events.listener.DomainEventAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ImportAutoConfiguration(DomainEventAutoConfiguration.class)
public class AzureDomainEventAutoConfiguration {

    @Bean
    public AzureSubjectProvider azureSubjectProvider(
            final AzureSubjectsProperties properties,
            final Environment environment
    ) {
        return new AzureSubjectProvider(properties, environment);
    }

    @Bean
    @ConfigurationProperties("azure.servicebus")
    @ConditionalOnMissingBean(AzureSubjectsProperties.class)
    public AzureSubjectsProperties azureSubjectsProperties(){
        return new AzureSubjectsProperties();
    }
}
