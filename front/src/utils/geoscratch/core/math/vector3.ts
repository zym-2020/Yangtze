class Vector3 {
    x: number;
    y: number;
    z: number;

    constructor(...params: Array<number>) {
        if (params.length == 1) {
            this.x = params[0];
            this.y = params[0];
            this.z = params[0];
        } 
        else {
            this.x = params[0] || 0.0;
            this.y = params[1] || 0.0;
            this.z = params[2] || 0.0;
        }
    }

    add(v: Vector3) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        
        return this;
    }

    multiplyNum(num: number) {
        this.x *= num;
        this.y *= num;
        this.z *= num;

        return this;
    }

    normalize() {
        const length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);

        // make sure we don't divide by 0.
        if (length > 0.00001) {
          this.x = this.x / length;
          this.y = this.y / length;
          this.z = this.z / length;
        }
        return this;
    }

    subtract(v: Vector3) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;

        return this;
    }

    static subtraction(v1: Vector3, v2: Vector3) {
        return new Vector3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    static crossProduct(v1: Vector3, v2: Vector3) {
        return new Vector3(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z, v1.x * v2.y - v1.y * v2.x);
    }

    makeFloor(cmp: Vector3) {
        if (cmp.x < this.x) this.x = cmp.x;
        if (cmp.y < this.y) this.y = cmp.y;
        if (cmp.z < this.z) this.z = cmp.z;
    }

    makeCeil(cmp: Vector3) {
        if (cmp.x > this.x) this.x = cmp.x;
        if (cmp.y > this.y) this.y = cmp.y;
        if (cmp.z > this.z) this.z = cmp.z;
    }

}

export {
    Vector3,
}