function terminal() {
  return {
    lines: [],
    client: null,
    maxLines: 500,
    timer: null,

    initTerminal() {
      const self = this;
      try {
        this.client = new StompJs.Client({
          brokerURL: 'ws://' + location.host + '/v1/websocket',

          onConnect: () => {
            self.client.subscribe('/topic/auditEvents', message => {
              self.lines.push(message.body);
              if (self.lines.length > self.maxLines) {
                self.lines.splice(0, self.lines.length - self.maxLines);
              }
              self.$nextTick(() => {
                const el = self.$refs.termBody;
                if (el) el.scrollTop = el.scrollHeight;
              });
            });

            self.timer = setInterval(() => {
              self.client.publish({
                destination: '/v1/api/audit',
                body: JSON.stringify({ timestamp: Date.now(), message: 'pull log' })
              });
            }, 3000);
          },

          onStompError: frame => {
            self.lines.push('[error] STOMP: ' + frame.headers.message);
          },

          onWebSocketClose: () => {
            self.lines.push('[info] disconnected — reconnecting...');
            if (self.timer) {
              clearInterval(self.timer);
              self.timer = null;
            }
          },

          reconnectDelay: 5000,
        });

        this.client.activate();
      } catch (err) {
        self.lines.push('[error] ' + err.message);
      }
    }
  };
}
