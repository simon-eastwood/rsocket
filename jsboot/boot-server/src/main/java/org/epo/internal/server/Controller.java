package org.epo.internal.server;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


import org.springframework.stereotype.Component;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.rsocket.RSocketRequester;

import org.springframework.kafka.annotation.KafkaListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

@Controller
class TestController {
	@Value(value = "${message.topic.name}")  
	private String topicName;
	
	@Autowired
	private KafkaTemplate<String, RTMessage> kafkaTemplate;

	// Fire And Forget
	@MessageMapping("test")
	public void test(RTMessage content) {
		System.err.println("invoked test call");

		kafkaTemplate.send(topicName, content);


	}

	// Stream
	

}

class Globals {

	public static long lastOffset = 0;
}

/* @Configuration
public class MyConfiguration {
	@Value(value = "${message.topic.name}")
	private String topicName;

	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value(value = "${spring.kafka.consumer.group-id}")
	private String consumerGroupId;

    @Bean
    public KafkaReceiver<String, String> kafkaReceiver() {
        Map<String, Object> props = new HashMap<>();
		consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        final ReceiverOptions<String, String> receiverOptions =
                ReceiverOptions.<String, string>create(props)
                               .subscription(Collections.singleton(consumer.getTopic()));
        return KafkaReceiver.create(receiverOptions);
    } 
} */

/* @Bean
public ApplicationRunner runner(KafkaReceiver<String, String> kafkaReceiver) {
        return args -> {
                kafkaReceiver.receive()
                          ...
                          .sunbscribe();
        };
} */


@Controller
class RealTimeUIController {
	
	private final Queue<RSocketRequester> connectedClients = new ConcurrentLinkedQueue<>();
	private final Queue<Subscription> subscriptions = new ConcurrentLinkedQueue<>();


	@Value(value = "${message.topic.name}")
	private String topicName;

	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value(value = "${spring.kafka.consumer.group-id}")
	private String consumerGroupId;


	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	// Request and Response
	@MessageMapping("getCurrentOffset")
	public Mono<String> getCurrentOffet() {
		System.err.println("invoked get current offset");
		System.out.println("Offset is " + Globals.lastOffset);
		//kafkaTemplate.send(topicName, content);

		return Mono.just("{\"reply\":"+ Globals.lastOffset + "}");
	}


 	@KafkaListener(topics = "${message.topic.name}", clientIdPrefix = "RT")
	public void listenForRTMessage(ConsumerRecord<String, RTMessage> cr,
									@Payload RTMessage msg) {
		
		Globals.lastOffset = cr.offset();
		System.out.println("Offset is " + Globals.lastOffset);
		System.out.println("Content is " + msg);

		this.subscriptions.forEach(s -> s.filterEvent(msg));
	} 
	

	@MessageMapping("getEvents")
	public Flux<RTMessage> streamEvents(String payload, RSocketRequester requester) {
		System.out.println("get Events invoked");
		 this.connectedClients.offer(requester);

		
		 Subscription sub;

		 /* if (this.subscriptions.size() < 1) { */
			sub = new Subscription (payload, payload, payload);
			this.subscriptions.offer(sub);  // TODO only add it if it doesnt already exist
	/* 	 } else {
			sub = this.subscriptions.peek();
		 } */

		

 	/* 	Map<String, Object> consumerProps = new HashMap<>();


	ReceiverOptions<Integer, String> receiverOptions =
    ReceiverOptions.<Integer, String>create(consumerProps)         
				   .subscription(Collections.singleton(topicName));
				   

				   Flux<ReceiverRecord<RTMessage>> inboundFlux =
    KafkaReceiver.create(receiverOptions)
                 .receive(); 
 */

		/* return inboundFlux.doOnEach(v -> System.err.println("got value from kafka") ).doOnNext( r -> r.receiverOffset().acknowledge() )
		.map(ReceiverRecord::value);  
		 */
		//return Flux.just(new RTMessage("type","id","etag"));
		Flux<RTMessage>  f = sub.getFilteredStream();
		f.subscribe(d -> System.out.println("Subscriber 1 "));
		return f;
	}
}

 
/* @Component
class MyBean {

	@KafkaListener(topics = "example", clientIdPrefix = "bytearray")
    public void listenAsByteArray(ConsumerRecord<String, byte[]> cr) {
		System.out.println("Offset is " + cr.offset());
		System.out.println("Content is " + cr.value());
    }


} */