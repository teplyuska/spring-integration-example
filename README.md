# spring-integration-example

Attempt of building IntegrationFlowFactory to easily build integration flows for passing events between application contexts.
The application context can with it's own config choose how to handle messages without knowing much about how the message was passed between the application contexts.

See: spring.integration.example.integration.IntegrationFlowFactory

For rabbit configuration see: spring.integration.example.integration.config.RabbitConfig

For consumer app see namespace: spring.integration.example.app.consumer

For publisher app see namespace: spring.integration.example.app.publisher
Publisher app also starts rest endpoint to publish messages.
