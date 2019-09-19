package org.epo.internal.server;

public class RTMessage {
	private String type;
	private String id;
	private String etag;

	public RTMessage() {
		;
	}

	public RTMessage (String type, String id, String eTag) {
		this.type = type;
		this.id = id;
		this.etag = eTag;
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