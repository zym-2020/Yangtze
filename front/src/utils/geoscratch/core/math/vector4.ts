export class Vector4 {
    x: number;
    y: number;
    z: number;
    w: number;

    constructor(...params: Array<number>) {
        if (params.length == 1) {
            this.x = params[0];
            this.y = params[0];
            this.z = params[0];
            this.w = params[0];
        } 
        else {
            this.x = params[0] || 0.0;
            this.y = params[1] || 0.0;
            this.z = params[2] || 0.0;
            this.w = params[3] || 0.0;
        }
    }

    add(v: Vector4) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        this.w += v.w;
        
        return this;
    }

    normalize() {
        const length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);

        // make sure we don't divide by 0.
        if (length > 0.00001) {
          this.x = this.x / length;
          this.y = this.y / length;
          this.z = this.z / length;
          this.w = this.w / length;
        }
        return this;
    }

    subtract(v: Vector4) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        this.w -= v.w;

        return this;
    }

    static subtraction(v1: Vector4, v2: Vector4) {
        return new Vector4(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z, v1.w - v2.w);
    }

    makeFloor(cmp: Vector4) {
        if (cmp.x < this.x) this.x = cmp.x;
        if (cmp.y < this.y) this.y = cmp.y;
        if (cmp.z < this.z) this.z = cmp.z;
        if (cmp.w < this.w) this.w = cmp.w;
    }

    makeCeil(cmp: Vector4) {
        if (cmp.x > this.x) this.x = cmp.x;
        if (cmp.y > this.y) this.y = cmp.y;
        if (cmp.z > this.z) this.z = cmp.z;
        if (cmp.w > this.w) this.w = cmp.w;
    }

}