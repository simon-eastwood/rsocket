import { TestClient } from './client';

window.onload = function init() {


    var client = new TestClient('ws://localhost:8080/rsocket');
    client.connect(() => {
        this.console.log('connected');

        // attached controller to offset form
        const offsetForm = document.getElementById('offsetForm');
        offsetForm.onsubmit = (f) => {
            console.log("submit done"); client.getOffset(
                (data) => { document.getElementById('latestOffset').innerHTML = JSON.stringify(data); }
            ); return false;
        };

        // attache controller to send form
        const sendForm = document.getElementById('sendForm');
        sendForm.onsubmit = (f) => {
            console.log("send done"); client.send(
                f.srcElement.elements['TypeToSend'].value,
                f.srcElement.elements['IdToSend'].value,
                f.srcElement.elements['eTagToSend'].value
            ); return false;
        };

        // attach controller to request stream form
        const streamForm = document.getElementById('streamForm');
        streamForm.onsubmit = (f) => {
            console.log("send done"); client.reqStream(
                f.srcElement.elements['TypeToSend'].value,
                f.srcElement.elements['IdToSend'].value,
                f.srcElement.elements['eTagToSend'].value,
                (data) => {
                    const list = document.getElementById('messagelist');
                    const line = document.createElement("LI");  
                    line.innerHTML = JSON.stringify(data);
                    list.appendChild(line);  
                }
            ); return false;
        };
    });
};


