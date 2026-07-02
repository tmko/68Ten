function terminal() {
  return {
    lines: [],
    client: null,
    maxLines: 500,
    timer: null,
    lastTimeStamp: Date.now(),
9    paused: true,
    showScrollButton: true,

    clearLines() {
      this.lines = [];
      this.paused = true;
      this.showScrollButton = true;
      if (this.timer) {
        clearInterval(this.timer);
        this.timer = null;
      }
    },

    pauseWebSocket() {
      this.paused = true;
      this.showScrollButton = true;
      if (this.timer) {
        clearInterval(this.timer);
        this.timer = null;
      }
    },

    resumeWebSocket() {
      this.paused = false;
      this.showScrollButton = false;
      const self = this;
      if (this.timer) clearInterval(this.timer);
      this.timer = setInterval(() => {
        self.client.publish({
          destination: '/v1/api/audit',
          body: JSON.stringify({ timestamp: self.lastTimeStamp, message: 'pull log' })
        });
      }, 3000);
      this.$nextTick(() => {
        const el = this.$refs.termBody;
        if (el) el.scrollTop = el.scrollHeight;
      });
    },

    scrollToBottom() {
      const el = this.$refs.termBody;
      if (el) el.scrollTop = el.scrollHeight;
    },

    initTerminal() {
      const self = this;
      this.paused = true;
      this.showScrollButton = true;
      try {
        this.client = new StompJs.Client({
          brokerURL: 'ws://' + location.host + '/v1/websocket',

          onConnect: () => {
            self.client.subscribe('/topic/auditEvents', message => {
                  try {
                    const data = JSON.parse(message.body);
                    if (Array.isArray(data)) {
                      data.forEach(item => {
                        if (item.message) self.lines.push(item.message);
                        if (item.timestamp) self.lastTimeStamp = item.timestamp
                      });

                      if (self.lines.length > self.maxLines) {
                        self.lines.splice(0, self.lines.length - self.maxLines);
                      }

                      if (!self.paused) {
                        self.$nextTick(() => {
                          const el = self.$refs.termBody;
                          if (el) el.scrollTop = el.scrollHeight;
                        });
                      }
                    }
                  } catch (e) {
                    console.error("Failed to parse audit message", e, message);
                  }
            });

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
