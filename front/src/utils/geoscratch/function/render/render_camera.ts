import { Mutex } from './../../core/lock/mutex';
import { DEG2RAD, clamp } from '../../core/math/utils';
import { Vector2 } from './../../core/math/vector2';
import { Matrix4x4 } from '../../core/math/matrix4';
import { Vector3 } from '../../core/math/vector3';
export enum SCurrentCameraType {
    Editor,
    Motor
}

export class SCamera {
    private aspect: number;
    private fovX: number;
    private fovY: number;

    position: Vector3;
    target: Vector3;
    up: Vector3;
    rotation: Vector3;
    invRotation: Vector3;

    zNear: number;
    zFar: number;

    exposure: number;
    
    X: Vector3;
    Y: Vector3;
    Z: Vector3;

    MIN_FOV: number;
    MAX_FOV: number;
    upAxis: Vector3;

    viewMatrixMutex: Mutex;
    projMatrixMutex: Mutex;
    MAIN_VIEW_MATRIX_INDEX: number;
    viewMatrices: Array<Matrix4x4>;
    projectMatrix: Matrix4x4 | null = null;

    zoomLevel= 1;

    currentCameraType: SCurrentCameraType;

    constructor() {
        this.aspect = 0;
        this.fovX = 89.0;
        this.fovY = 0;

        this.position = new Vector3();
        this.target = new Vector3();
        this.up = new Vector3();
        this.rotation = new Vector3();
        this.invRotation = new Vector3();
        this.zNear = 0.1;
        this.zFar = 1000.0;

        this.exposure = 1.0;

        this.X = new Vector3(1.0, 0.0, 0.0);
        this.Y = new Vector3(0.0, 1.0, 0.0);
        this.Z = new Vector3(0.0, 0.0, 1.0);

        this.MIN_FOV = 10.0;
        this.MAX_FOV = 89.0;
        this.upAxis = this.Y;

        this.viewMatrixMutex = new Mutex();
        this.projMatrixMutex = new Mutex();
        this.MAIN_VIEW_MATRIX_INDEX = 0;
        this.viewMatrices= [];

        this.currentCameraType = SCurrentCameraType.Editor;
    }

    setCurrentCameraType(currentCameraType: SCurrentCameraType) {
        // may need a locker
        this.currentCameraType = currentCameraType;
    }

    move(delta: Vector3) {
        this.position.add(delta);
    }

    rotate(delta: Vector2) {
        // delta = new Vector2(DEG2RAD * delta.x, DEG2RAD * delta.y);
        // need to learn quaternion
    }

    zoom(offset: number) {
        this.fovX = clamp(this.fovX - offset, this.MIN_FOV, this.MAX_FOV);
    }

    lookAt(position: Vector3, target: Vector3, up: Vector3){
        this.position = position;
        this.target = target;
        this.up = up;
    }

    getPosition() {
        return this.position;
    }

    getRotation() {
        return this.rotation;
    }

    getViewMatrix() {
        this.viewMatrixMutex.lock();

        let viewMatrix: Float32Array;
        switch (this.currentCameraType) {
            case SCurrentCameraType.Editor:
                viewMatrix = Matrix4x4.lookAt(this.position, this.target, this.up);
                break;
            case SCurrentCameraType.Motor:
                viewMatrix = Matrix4x4.lookAt(this.position, this.target, this.up);
                break;
        
            default:
                viewMatrix = (new Matrix4x4()).getF32ArrayBuffer();
                break;
        }

        this.viewMatrixMutex.unlock();
        return viewMatrix;
    }

    getPersProjMatrix() {
        // console.log(this.fovY, this.aspect);
        return Matrix4x4.perspective(this.fovY, this.aspect, this.zNear, this.zFar);


		const near = this.zNear;
		// const top = near * Math.tan( DEG2RAD * 0.5 * this.fovY) / this.zoomLevel;
		const top = near * Math.tan( DEG2RAD * 0.5 * 60) / this.zoomLevel;
		const height = 2 * top;
		const width = this.aspect * height;
		const left = - 0.5 * width;

		return Matrix4x4.project(left, left + width, top, top - height, near, this.zFar);
    }

    setAspect(aspect: number) {
        this.aspect = aspect;
        this.fovY = Math.atan(Math.tan(DEG2RAD * this.fovX * 0.5) / this.aspect) * 2.0;
    }

    setFovX(fovx: number) {
        this.fovX = fovx;
    }

    getFov() {
        return new Vector2(this.fovX, this.fovY);
    }

    setMainProjectMatrix(projectMatrix: Matrix4x4, type: SCurrentCameraType) {
        this.projMatrixMutex.lock()

        this.currentCameraType = type;
        this.projectMatrix = projectMatrix;

        this.projMatrixMutex.unlock()
    }

    setMainViewMatrix(viewMatrix: Matrix4x4, type: SCurrentCameraType) {
        this.viewMatrixMutex.lock()

        this.currentCameraType = type;
        this.viewMatrices[this.MAIN_VIEW_MATRIX_INDEX] = viewMatrix;

        // TODO: update this.position!
        // Code below may be wrong!
        const matrix = viewMatrix.getF32ArrayBuffer();
        const s = new Vector3(matrix[0 * 4 + 0], matrix[0 * 4 + 1], matrix[0 * 4 + 2]);
        const u = new Vector3(matrix[1 * 4 + 0], matrix[1 * 4 + 1], matrix[1 * 4 + 2]);
        const f = new Vector3(-matrix[2 * 4 + 0], -matrix[2 * 4 + 1], -matrix[2 * 4 + 2]);

        this.position = s.multiplyNum(-matrix[0 * 4 + 3]).add(u.multiplyNum(-matrix[1 * 4 + 3])).add(f.multiplyNum(matrix[2 * 4 + 3]));

        this.viewMatrixMutex.unlock()
    }
}