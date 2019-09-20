package org.epo.internal.server;

import reactor.core.publisher.Flux;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;

import java.util.regex.Pattern;

public class Subscription {

	private Pattern typeRE;
	private Pattern idRE;
	private Pattern etagRE;

	private Flux  filteredStream;
	private EmitterProcessor<RTMessage> emitterProcessor;
	private FluxSink<RTMessage> sink;

	public Subscription (RTMessage filter) {
		this.typeRE = Pattern.compile (filter.getType()); 
		this.idRE = Pattern.compile (filter.getId());  
		this.etagRE =  Pattern.compile (filter.getEtag());  

		this.emitterProcessor = EmitterProcessor.create();
		this.sink = emitterProcessor.sink(); 
		this.filteredStream = this.emitterProcessor.publish().autoConnect();  // TODO: detect when the last subscriber has disconnected

	}

	public Flux<RTMessage> getFilteredStream() {
		return this.filteredStream;
	}

	public void filterEvent (RTMessage msg) {

		// check if it matches my filter
		if (this.typeRE.matcher(msg.getType()).matches() &&
				this.idRE.matcher(msg.getId()).matches() &&
					this.etagRE.matcher(msg.getEtag()).matches() ) {
			
			System.out.println ("Appending msg to stream"+ msg.getType());
			this.sink.next(msg);
		}
	}


}