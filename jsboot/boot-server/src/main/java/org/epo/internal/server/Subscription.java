package org.epo.internal.server;

import reactor.core.publisher.Flux;

public class Subscription {
	private String type;
	private String id;
	private String etag;

	private Flux  filteredStream;

	public Subscription (String type, String id, String eTag) {
		this.type = type;
		this.id = id;
		this.etag = eTag;

		this.filteredStream = Flux.empty();

	}

	public Flux<RTMessage> getFilteredStream() {
		return this.filteredStream;
	}

	public void filterEvent (RTMessage msg) {
		Flux.concat(this.filteredStream, Flux.just(msg));
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