

  function terminal() {
    return {
      lines: [],
      ws: null,
      maxLines: 500,


      initTerminal() {
        const self = this;
        try {
          this.ws = new WebSocket('ws://' + location.host + '/v1/websocket');
          this.ws.onmessage = (e) => {
            self.lines.push(e.data);
            if (self.lines.length > self.maxLines) {
              self.lines.splice(0, self.lines.length - self.maxLines);
            }
            self.$nextTick(() => {
              const el = self.$refs.termBody;
              if (el) el.scrollTop = el.scrollHeight;
            });
          };
          this.ws.onerror = () => {
            self.lines.push('[error] WebSocket connection failed — retrying in 5s');
            setTimeout(() => self.initTerminal(), 5000);
          };
          this.ws.onclose = () => {
            self.lines.push('[info] WebSocket closed — reconnecting in 5s');
            setTimeout(() => self.initTerminal(), 5000);
          };
        } catch (err) {
          self.lines.push('[error] ' + err.message);
        }
      }


    }; // end return
  } // end function




function terminal() {
  return {
    lines: [],
    ws: null,
    stompClient: null,
    maxLines: 500,


    initTerminal() {
      const self = this;
      try {
        this.ws = new WebSocket('ws://' + location.host + '/v1/websocket');
        this.stompClient = new StompJs.Client({ brokerURL: 'ws://' + location.host + '/gs-guide-websocket' });

        this.stompClient.onConnect = (frame) => {
          setConnected(true);
          console.log('Connected: ' + frame);
          stompClient.subscribe('/topic/auditEvents', (event) => { JSON.parse(event.body).content; });
        };


        this.ws.onmessage = (e) => {
          self.lines.push(e.data);
          if (self.lines.length > self.maxLines) {
            self.lines.splice(0, self.lines.length - self.maxLines);
          }
          self.$nextTick(() => {
            const el = self.$refs.termBody;
            if (el) el.scrollTop = el.scrollHeight;
          });
        };


        this.stompClient.onWebSocketError = (error) => {
          console.error('Error with websocket', error);
        };

        this.stompClient.onStompError = (frame) => {
          console.error('Broker reported error: ' + frame.headers['message']);
          console.error('Additional details: ' + frame.body);
        };



      } catch (err) {
        self.lines.push('[error] ' + err.message);
      }
    }


  }; // end return
} // end function