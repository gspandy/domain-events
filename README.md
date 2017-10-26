# domain-events

## Purpose
Spring boot extensions to manage event listeners over abstract async messaging systems.

## Usage (azure as example)

### TL;DR: see domain-events-sample project

* Add maven dependency to existing project
    ```xml
    <dependency>
        <groupId>de.audibene.core</groupId>
        <artifactId>domain-events-azure</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency> 
    ```
    
* Create custom event type 
    ```java
    @JsonTypeName("test")
    @Topic(name = "my-topic-name")
    public class TestDomainEvent implements DomainEvent {
        // fields, getters and setters
    }
    ```

* Publish event

    ```java
    @Component
    public class TestPublisher {
        @Autowire
        private ApplicationEvenPublisher publisher;
        
        public void produce(){
            // some logic
            publisher.publishEvent(new TestDomainEvent());
        }
        
    }
    ```

* Create custom handler 
    ```java
    @Component
    public class TestHandler {
        @DomainEventListner(group = "my-group-name")
        public void handle(TestDOmainEvent event) {
            // some logic
        }
    }
    ```

* Profit
