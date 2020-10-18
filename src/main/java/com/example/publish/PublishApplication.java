package com.example.publish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;

@SpringBootApplication
public class PublishApplication implements ApplicationRunner{
	
	@Value("${person.name}")
    private String name;
	
	@Autowired
    private PublishApplication.PubsubOutboundGateway messagingGateway;
	
	public static void main(String[] args) {
		SpringApplication.run(PublishApplication.class, args);
	}
   
    @Bean
    @ServiceActivator(inputChannel = "pubsubOutputChannel")
    public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
            return new PubSubMessageHandler(pubsubTemplate, "testone");
    }
   
    @MessagingGateway(defaultRequestChannel = "pubsubOutputChannel")
    public interface PubsubOutboundGateway {

            void sendToPubsub(String text);
    }
   

    @Override
    public void run( ApplicationArguments args ) throws Exception
    {
    	messagingGateway.sendToPubsub(name);
        System.out.println( "Name: " + name );
    }
	
}
