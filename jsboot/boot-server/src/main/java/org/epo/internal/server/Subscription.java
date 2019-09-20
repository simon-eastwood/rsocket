package org.epo.internal.server;

import reactor.core.publisher.Flux;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;

public class Subscription {
	private String type;
	private String id;
	private String etag;

	private Flux  filteredStream;

	private EmitterProcessor<RTMessage> emitterProcessor;
	private FluxSink<RTMessage> sink;

	public Subscription (String type, String id, String eTag) {
		this.type = type;
		this.id = id;
		this.etag = eTag;

		this.emitterProcessor = EmitterProcessor.create();
		this.sink = emitterProcessor.sink(); 
		this.filteredStream = this.emitterProcessor.publish().autoConnect();

	}

	public Flux<RTMessage> getFilteredStream() {
		return this.filteredStream;
	}

	public void filterEvent (RTMessage msg) {
		System.out.println ("Appending msg to stream"+ msg.getType());
		this.sink.next(msg);
	}

	public String getType() {
		return this.type;
	}

	public String getId() {
		return this.id;
	}
	
	public String getEtag() {
		return this.etag;
	}

	public void setType (String type) {
		this.type = type;
	}

	public void setId (String id) {
		this.id = id;
	}

	public void setEtag (String etag) {
		this.etag = etag;
	}

}