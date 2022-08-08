import { Vector3 } from './vector3';

export class BoundingBox {
    min: Vector3;
    max: Vector3;

    constructor() {
        this.min = new Vector3(Number.MAX_SAFE_INTEGER, Number.MAX_SAFE_INTEGER, Number.MAX_SAFE_INTEGER);
        this.max = new Vector3(Number.MIN_SAFE_INTEGER, Number.MIN_SAFE_INTEGER, Number.MIN_SAFE_INTEGER);
    }

    merge(point: Vector3) {
        this.min.makeFloor(point);
        this.max.makeCeil(point);
    }
}
