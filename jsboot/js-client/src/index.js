import {TestClient} from './client';

window.onload = function init() {
    var client = new TestClient('ws://localhost:8080/rsocket');
    client.connect(() => {
        this.console.log('connected');
    });
};


