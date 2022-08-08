// code and idea are from "https://www.gofun4.top/JS%E4%B8%AD%E7%9A%84%E4%BA%92%E6%96%A5%E9%94%81.html"

export interface IMutex {
    lock: () => Promise<void>;
    unlock: () => void;
}

export class Mutex implements IMutex {
    // eslint-disable-next-line @typescript-eslint/ban-types
    private _queue: Array<Function>;
    private _maxConcurrency: number;

    constructor() {
        this._queue = [];
        this._maxConcurrency = 1;
    }

    async lock() {
      if (this._maxConcurrency <= 0) {
        await new Promise<void>(resolve => this._queue.push(resolve))
      }
      this._maxConcurrency--
    }
    unlock() {
      this._maxConcurrency++;
      this._queue.shift()?.();
    }
}
