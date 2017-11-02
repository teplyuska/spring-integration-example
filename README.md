# spring-integration-example

Attempt of building IntegrationFlowFactory to easily build integration flows for passing events between application contexts.
The application context can with it's own config choose how to handle messages without knowing much about how the message was passed between the application contexts.

See: spring.integration.example.integration.IntegrationFlowFactory

For rabbit configuration see: spring.integration.example.integration.config.RabbitConfig

For consumer app see namespace: spring.integration.example.app.consumer

For publisher app see namespace: spring.integration.example.app.publisher  
Publisher app also starts rest endpoint to publish messages.

See:
https://stackoverflow.com/questions/47076296/optimise-consuming-messages-from-rabbitmq-using-spring-integration  
for Adapter vs Gateway fix.

Creating a scenario with multiple applications acting as Publishers, multiple applications acting as Consumers and probably many acting as both.
One would probably share the PublisherApplicationConfig between all Publishing application contexts.

The important part is where the Consuming application have to choose what events it wants.
ConsumerApplicationConfig is an example of such an choice. Each Consuming application will most likely have it's own way of handling events.