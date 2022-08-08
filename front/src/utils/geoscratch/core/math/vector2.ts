export class Vector2 {
    x: number;
    y: number;

    constructor(...params: Array<number>) {
        if (params.length == 1) {
            this.x = params[0];
            this.y = params[0];
        } 
        else {
            this.x = params[0] || 0.0;
            this.y = params[1] || 0.0;
        }
    }

    add(v: Vector2) {
        this.x += v.x;
        this.y += v.y;
        
        return this;
    }

    subtract(v: Vector2) {
        this.x -= v.x;
        this.y -= v.y;

        return this;
    }

    normalize() {
        const length = Math.sqrt(this.x * this.x + this.y * this.y);

        // make sure we don't divide by 0.
        if (length > 0.00001) {
          this.x = this.x / length;
          this.y = this.y / length;
        }
        return this;
    }

    static subtraction(v1: Vector2, v2: Vector2) {
        return new Vector2(v1.x - v2.x, v1.y - v2.y);
    }
}