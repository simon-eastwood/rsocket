import {
    RSocketClient,
    JsonSerializer,
    IdentitySerializer,
    BufferEncoder,
    UTF8Encoder
} from 'rsocket-core';
import RSocketWebSocketClient from 'rsocket-websocket-client';
import {RoutingMetadataSerializer} from './metadata'

const CustomEncoders = {
    data: UTF8Encoder,
    dataMimeType: UTF8Encoder,
    message: UTF8Encoder,
    metadata: BufferEncoder,
    metadataMimeType: UTF8Encoder,
    resumeToken: UTF8Encoder,
};

export class TestClient {

    constructor(url) {
        this.client = new RSocketClient({
            serializers: {
                data: JsonSerializer,
                metadata: new RoutingMetadataSerializer(),
            },
            setup: {
                // ms btw sending keepalive to server
                keepAlive: 10000,
                // ms timeout if no keepalive response
                lifetime: 20000,
                dataMimeType: 'application/json',
                metadataMimeType: RoutingMetadataSerializer.MIME_TYPE,
            },
            transport: new RSocketWebSocketClient({url: url}, CustomEncoders)
        });
    }

    connect(cb) {
        return this.client.connect().subscribe({
            onComplete: s => {
                this.socket = s;
        
                
       
  
                cb();
            },
            onError: error => console.error(error),
            onSubscribe: cancel => { this.cancel = cancel}
        });
    }

    disconnect() {
        this.cancel();
    }

    getOffset(cb) {
        console.log ("getoffset called");
        this.socket.requestResponse({
            data: {},
            metadata: 'getCurrentOffset',
        }).subscribe({
            onComplete: p => {console.log('received: ' + JSON.stringify(p)); cb(p);},
            onError: error => {console.error(error); cb(error);},
            onSubscribe: data => console.log('subscribed: ' + data)
        });   
    }

    send(t, i, e) {
        console.log (t);
        this.socket.fireAndForget({
            data: {type: t, id: i, etag: e},
            metadata: 'test',
        });
    }

    reqStream(type, id, etag, cb) {
        console.log('requesting server response');
            this.socket.requestStream({
                    data: 'input string',
                     metadata: 'getEvents'
                }).subscribe({
                    onError: error => {
                        console.error(error);
                        console.dir(error);
                    },
                    onNext: msg => {
                        const data = msg.data;
                        console.log ("stream provided next value");
                        console.log (data);
                        cb(data);
                       
                    },
                    onSubscribe: sub => {
                        console.log ("subscribed Now setting up backpressure control ");
                        console.log (sub);
                        
                        window.setInterval(() => {
                            console.log ("requesting next 10 ");
                            sub.request(10);
                        }, 1000);
                    },

                });          
    }

}