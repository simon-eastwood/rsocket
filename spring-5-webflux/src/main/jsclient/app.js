
import './node_modules/rsocket-core/build/index.js';
import './node_modules/rsocket-websocket-client/build/index.js';

  
  // Create an instance of a client
  const client = new RSocketClient({
	// send/receive objects instead of strings/buffers
	serializers: JsonSerializers,
	setup: {
	  // ms btw sending keepalive to server
	  keepAlive: 60000, 
	  // ms timeout if no keepalive response
	  lifetime: 180000, 
	  // format of `data`
	  dataMimeType: 'application/json', 
	  // format of `metadata`
	  metadataMimeType: 'application/json', 
	},
	transport: new RSocketWebSocketClient({url: 'wss://...'}),
  });
  
  // Open the connection
  client.connect().subscribe({
	onComplete: socket => {
	  // socket provides the rsocket interactions fire/forget, request/response,
	  // request/stream, etc as well as methods to close the socket.
  
	  socket.fireAndForget({
		data: {some: {json: {value: 1}}},
		metadata: {another: {json: {value: true}}},
	  });
	},
	onError: error => console.error(error),
	onSubscribe: cancel => {/* call cancel() to abort */}
  });