import { Matrix4x4 } from "./matrix4";
import { Vector3 } from "./vector3";

class Transform {
    position: Vector3;
    scale: Vector3;
    rotation: Vector3;

    constructor(translation?: Vector3, scale?: Vector3, rotation?: Vector3) {
        this.position = translation || new Vector3(0.0);
        this.scale = scale || new Vector3(1.0);
        this.rotation = rotation || new Vector3(0.0);
    }

    static setFromRes(transformRes: TransformRes): Transform {
        return new Transform(new Vector3(transformRes.position[0], transformRes.position[1], transformRes.position[2]), 
                             new Vector3(transformRes.scale[0], transformRes.scale[1], transformRes.scale[2]),
                             new Vector3(transformRes.rotation[0], transformRes.rotation[1], transformRes.rotation[2]));
    }

    getMatrix(): Float32Array {
        let temp = Matrix4x4.identity();

        temp = Matrix4x4.scale(temp, this.scale.x, this.scale.y, this.scale.z);
        temp = Matrix4x4.xRotate(temp, this.rotation.x);
        temp = Matrix4x4.yRotate(temp, this.rotation.y);
        temp = Matrix4x4.zRotate(temp, this.rotation.z);
        temp = Matrix4x4.translate(temp, this.position.x, this.position.y, this.position.z);

        return temp;
    } 
}

export interface TransformRes {
    position: Array<number>;
    scale: Array<number>;
    rotation: Array<number>;
}

export {
    Transform,
};