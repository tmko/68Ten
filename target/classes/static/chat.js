    function chat() {
      return {
        messages: [],
        input: '',
        loading: false,
        errored: false,
        controller: null,
        async send() {
          if (!this.input.trim() || this.loading || this.errored) return;
          const text = this.input;
          this.messages.push({ role: 'user', content: text });
          this.input = '';
          this.loading = true;
          this.controller = new AbortController();
          try {
            const res = await fetch('/v1/api/echo', {
              method: 'POST',
              headers: { 'Content-Type': 'text/plain' },
              body: text,
              signal: this.controller.signal,
            });
            if (res.status === 500) {
              this.errored = true;
              this.loading = false;
              this.messages.push({ role: 'ai', content: '[500 Internal Server Error — input locked]' });
              return;
            }
            const data = await res.text();
            this.messages.push({ role: 'ai', content: data || '(empty response)' });
          } catch (err) {
            if (err.name !== 'AbortError') {
              this.messages.push({ role: 'ai', content: '[error] ' + err.message });
            }
          } finally {
            this.loading = false;
          }
        }
      };
    }
