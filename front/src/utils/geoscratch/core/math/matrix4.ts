import { DEG2RAD } from './utils';
import { Matrix } from "three";
import { Vector3 } from "./vector3";

class Matrix4x4 {
    private elements: Float32Array;

    constructor(matrix?: Array<number>){
        if (matrix) {
            this.elements = new Float32Array(16);
            
            for (let i = 0 ; i < 16; i++)
                this.elements[i] = matrix[i];
        }
        else
            this.elements = Matrix4x4.identity();
    }

    setElementByF32ArrayBuffer(elements: Float32Array) {
        this.elements = new Float32Array(elements);
    }

    static setFromF32ArrayBuffer(elements: Float32Array) {
        
        const matrix = new Matrix4x4();
        matrix.setElementByF32ArrayBuffer(elements);

        return matrix;
    }

    getF32ArrayBuffer() {
        return this.elements;
    }

    static normalize(v: Array<number> | Float32Array) {
        const dst = new Float32Array(3);
        const length = Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        // make sure we don't divide by 0.
        if (length > 0.00001) {
          dst[0] = v[0] / length;
          dst[1] = v[1] / length;
          dst[2] = v[2] / length;
        }
        return dst;
    }

    multiply (b: Matrix4x4) {
        const b00 = b.elements[0 * 4 + 0];
        const b01 = b.elements[0 * 4 + 1];
        const b02 = b.elements[0 * 4 + 2];
        const b03 = b.elements[0 * 4 + 3];
        const b10 = b.elements[1 * 4 + 0];
        const b11 = b.elements[1 * 4 + 1];
        const b12 = b.elements[1 * 4 + 2];
        const b13 = b.elements[1 * 4 + 3];
        const b20 = b.elements[2 * 4 + 0];
        const b21 = b.elements[2 * 4 + 1];
        const b22 = b.elements[2 * 4 + 2];
        const b23 = b.elements[2 * 4 + 3];
        const b30 = b.elements[3 * 4 + 0];
        const b31 = b.elements[3 * 4 + 1];
        const b32 = b.elements[3 * 4 + 2];
        const b33 = b.elements[3 * 4 + 3];
        const a00 = this.elements[0 * 4 + 0];
        const a01 = this.elements[0 * 4 + 1];
        const a02 = this.elements[0 * 4 + 2];
        const a03 = this.elements[0 * 4 + 3];
        const a10 = this.elements[1 * 4 + 0];
        const a11 = this.elements[1 * 4 + 1];
        const a12 = this.elements[1 * 4 + 2];
        const a13 = this.elements[1 * 4 + 3];
        const a20 = this.elements[2 * 4 + 0];
        const a21 = this.elements[2 * 4 + 1];
        const a22 = this.elements[2 * 4 + 2];
        const a23 = this.elements[2 * 4 + 3];
        const a30 = this.elements[3 * 4 + 0];
        const a31 = this.elements[3 * 4 + 1];
        const a32 = this.elements[3 * 4 + 2];
        const a33 = this.elements[3 * 4 + 3];
        
        this.elements[ 0] = b00 * a00 + b01 * a10 + b02 * a20 + b03 * a30;
        this.elements[ 1] = b00 * a01 + b01 * a11 + b02 * a21 + b03 * a31;
        this.elements[ 2] = b00 * a02 + b01 * a12 + b02 * a22 + b03 * a32;
        this.elements[ 3] = b00 * a03 + b01 * a13 + b02 * a23 + b03 * a33;
        this.elements[ 4] = b10 * a00 + b11 * a10 + b12 * a20 + b13 * a30;
        this.elements[ 5] = b10 * a01 + b11 * a11 + b12 * a21 + b13 * a31;
        this.elements[ 6] = b10 * a02 + b11 * a12 + b12 * a22 + b13 * a32;
        this.elements[ 7] = b10 * a03 + b11 * a13 + b12 * a23 + b13 * a33;
        this.elements[ 8] = b20 * a00 + b21 * a10 + b22 * a20 + b23 * a30;
        this.elements[ 9] = b20 * a01 + b21 * a11 + b22 * a21 + b23 * a31;
        this.elements[10] = b20 * a02 + b21 * a12 + b22 * a22 + b23 * a32;
        this.elements[11] = b20 * a03 + b21 * a13 + b22 * a23 + b23 * a33;
        this.elements[12] = b30 * a00 + b31 * a10 + b32 * a20 + b33 * a30;
        this.elements[13] = b30 * a01 + b31 * a11 + b32 * a21 + b33 * a31;
        this.elements[14] = b30 * a02 + b31 * a12 + b32 * a22 + b33 * a32;
        this.elements[15] = b30 * a03 + b31 * a13 + b32 * a23 + b33 * a33;

        return this;
    }

    static multiply(a: Matrix4x4, b: Matrix4x4) {
        const dst = new Matrix4x4();
        
        const b00 = b.elements[0 * 4 + 0];
        const b01 = b.elements[0 * 4 + 1];
        const b02 = b.elements[0 * 4 + 2];
        const b03 = b.elements[0 * 4 + 3];
        const b10 = b.elements[1 * 4 + 0];
        const b11 = b.elements[1 * 4 + 1];
        const b12 = b.elements[1 * 4 + 2];
        const b13 = b.elements[1 * 4 + 3];
        const b20 = b.elements[2 * 4 + 0];
        const b21 = b.elements[2 * 4 + 1];
        const b22 = b.elements[2 * 4 + 2];
        const b23 = b.elements[2 * 4 + 3];
        const b30 = b.elements[3 * 4 + 0];
        const b31 = b.elements[3 * 4 + 1];
        const b32 = b.elements[3 * 4 + 2];
        const b33 = b.elements[3 * 4 + 3];
        const a00 = a.elements[0 * 4 + 0];
        const a01 = a.elements[0 * 4 + 1];
        const a02 = a.elements[0 * 4 + 2];
        const a03 = a.elements[0 * 4 + 3];
        const a10 = a.elements[1 * 4 + 0];
        const a11 = a.elements[1 * 4 + 1];
        const a12 = a.elements[1 * 4 + 2];
        const a13 = a.elements[1 * 4 + 3];
        const a20 = a.elements[2 * 4 + 0];
        const a21 = a.elements[2 * 4 + 1];
        const a22 = a.elements[2 * 4 + 2];
        const a23 = a.elements[2 * 4 + 3];
        const a30 = a.elements[3 * 4 + 0];
        const a31 = a.elements[3 * 4 + 1];
        const a32 = a.elements[3 * 4 + 2];
        const a33 = a.elements[3 * 4 + 3];
        dst.elements[ 0] = b00 * a00 + b01 * a10 + b02 * a20 + b03 * a30;
        dst.elements[ 1] = b00 * a01 + b01 * a11 + b02 * a21 + b03 * a31;
        dst.elements[ 2] = b00 * a02 + b01 * a12 + b02 * a22 + b03 * a32;
        dst.elements[ 3] = b00 * a03 + b01 * a13 + b02 * a23 + b03 * a33;
        dst.elements[ 4] = b10 * a00 + b11 * a10 + b12 * a20 + b13 * a30;
        dst.elements[ 5] = b10 * a01 + b11 * a11 + b12 * a21 + b13 * a31;
        dst.elements[ 6] = b10 * a02 + b11 * a12 + b12 * a22 + b13 * a32;
        dst.elements[ 7] = b10 * a03 + b11 * a13 + b12 * a23 + b13 * a33;
        dst.elements[ 8] = b20 * a00 + b21 * a10 + b22 * a20 + b23 * a30;
        dst.elements[ 9] = b20 * a01 + b21 * a11 + b22 * a21 + b23 * a31;
        dst.elements[10] = b20 * a02 + b21 * a12 + b22 * a22 + b23 * a32;
        dst.elements[11] = b20 * a03 + b21 * a13 + b22 * a23 + b23 * a33;
        dst.elements[12] = b30 * a00 + b31 * a10 + b32 * a20 + b33 * a30;
        dst.elements[13] = b30 * a01 + b31 * a11 + b32 * a21 + b33 * a31;
        dst.elements[14] = b30 * a02 + b31 * a12 + b32 * a22 + b33 * a32;
        dst.elements[15] = b30 * a03 + b31 * a13 + b32 * a23 + b33 * a33;

        return dst;
    }

    static cross(a: Array<number> | Float32Array, b: Array<number> | Float32Array) {
        const dst =  new Float32Array(3);
        dst[0] = a[1] * b[2] - a[2] * b[1];
        dst[1] = a[2] * b[0] - a[0] * b[2];
        dst[2] = a[0] * b[1] - a[1] * b[0];
        return dst;
    }

    static subtractVectors(a: Array<number> | Float32Array, b: Array<number> | Float32Array) {
        const dst = new Float32Array(3);
        dst[0] = a[0] - b[0];
        dst[1] = a[1] - b[1];
        dst[2] = a[2] - b[2];
        return dst;
    }

    static MultiplyVector(m: Array<number> | Float32Array, v: Array<number> | Float32Array): Float32Array {
        const dst = new Float32Array(4);

        const m00 = m[0 * 4 + 0];
        const m01 = m[0 * 4 + 1];
        const m02 = m[0 * 4 + 2];
        const m03 = m[0 * 4 + 3];
        const m10 = m[1 * 4 + 0];
        const m11 = m[1 * 4 + 1];
        const m12 = m[1 * 4 + 2];
        const m13 = m[1 * 4 + 3];
        const m20 = m[2 * 4 + 0];
        const m21 = m[2 * 4 + 1];
        const m22 = m[2 * 4 + 2];
        const m23 = m[2 * 4 + 3];
        const m30 = m[3 * 4 + 0];
        const m31 = m[3 * 4 + 1];
        const m32 = m[3 * 4 + 2];
        const m33 = m[3 * 4 + 3];
        dst[0] = m00 * v[0] + m10 * v[1] + m20 * v[2] + m30 * v[3]; 
        dst[1] = m01 * v[0] + m11 * v[1] + m21 * v[2] + m31 * v[3]; 
        dst[2] = m02 * v[0] + m12 * v[1] + m22 * v[2] + m32 * v[3]; 
        dst[3] = m03 * v[0] + m13 * v[1] + m23 * v[2] + m33 * v[3]; 

        return dst;
    }

    static identity(): Float32Array{
        const dst = new Float32Array(16);
    
        dst[ 0] = 1;
        dst[ 1] = 0;
        dst[ 2] = 0;
        dst[ 3] = 0;
        dst[ 4] = 0;
        dst[ 5] = 1;
        dst[ 6] = 0;
        dst[ 7] = 0;
        dst[ 8] = 0;
        dst[ 9] = 0;
        dst[10] = 1;
        dst[11] = 0;
        dst[12] = 0;
        dst[13] = 0;
        dst[14] = 0;
        dst[15] = 1;
    
        return dst;
    }

    copy() {
        return Matrix4x4.setFromF32ArrayBuffer(this.elements);
    }

    rotateX(angleInRadians: number) {
    //     const m10 = this.elements[4];
    //     const m11 = this.elements[5];
    //     const m12 = this.elements[6];
    //     const m13 = this.elements[7];
    //     const m20 = this.elements[8];
    //     const m21 = this.elements[9];
    //     const m22 = this.elements[10];
    //     const m23 = this.elements[11];
    //     const c = Math.cos(angleInRadians);
    //     const s = Math.sin(angleInRadians);

    //     this.elements[4]  = c * m10 + s * m20;
    //     this.elements[5]  = c * m11 + s * m21;
    //     this.elements[6]  = c * m12 + s * m22;
    //     this.elements[7]  = c * m13 + s * m23;
    //     this.elements[8]  = c * m20 - s * m10;
    //     this.elements[9]  = c * m21 - s * m11;
    //     this.elements[10] = c * m22 - s * m12;
    //     this.elements[11] = c * m23 - s * m13;
        const m10 = this.elements[1];
        const m11 = this.elements[5];
        const m12 = this.elements[9];
        const m13 = this.elements[13];
        const m20 = this.elements[2];
        const m21 = this.elements[6];
        const m22 = this.elements[10];
        const m23 = this.elements[14];
        const c = Math.cos(angleInRadians);
        const s = Math.sin(angleInRadians);

        this.elements[1]  = c * m10 + s * m20;
        this.elements[5]  = c * m11 + s * m21;
        this.elements[9]  = c * m12 + s * m22;
        this.elements[13]  = c * m13 + s * m23;
        this.elements[2]  = c * m20 - s * m10;
        this.elements[6]  = c * m21 - s * m11;
        this.elements[10] = c * m22 - s * m12;
        this.elements[14] = c * m23 - s * m13;

        return this;
    }

    rotateY(angleInRadians: number) {
        const m00 = this.elements[0];
        const m01 = this.elements[4];
        const m02 = this.elements[8];
        const m03 = this.elements[12];
        const m20 = this.elements[2];
        const m21 = this.elements[6];
        const m22 = this.elements[10];
        const m23 = this.elements[14];
        const c = Math.cos(angleInRadians);
        const s = Math.sin(angleInRadians);
    
        this.elements[ 0] = c * m00 - s * m20;
        this.elements[ 4] = c * m01 - s * m21;
        this.elements[ 8] = c * m02 - s * m22;
        this.elements[12] = c * m03 - s * m23;
        this.elements[ 2] = c * m20 + s * m00;
        this.elements[ 6] = c * m21 + s * m01;
        this.elements[10] = c * m22 + s * m02;
        this.elements[14] = c * m23 + s * m03;

        return this;
    }

    rotateZ(angleInRadians: number) {
        const m00 = this.elements[0];
        const m01 = this.elements[4];
        const m02 = this.elements[8];
        const m03 = this.elements[12];
        const m10 = this.elements[1];
        const m11 = this.elements[5];
        const m12 = this.elements[9];
        const m13 = this.elements[13];
        const c = Math.cos(angleInRadians);
        const s = Math.sin(angleInRadians);
    
        this.elements[ 0] = c * m00 + s * m10;
        this.elements[ 4] = c * m01 + s * m11;
        this.elements[ 8] = c * m02 + s * m12;
        this.elements[12] = c * m03 + s * m13;
        this.elements[ 1] = c * m10 - s * m00;
        this.elements[ 5] = c * m11 - s * m01;
        this.elements[ 9] = c * m12 - s * m02;
        this.elements[13] = c * m13 - s * m03;

        return this;
    }

    static xRotate(m: Array<number> | Float32Array, angleInRadians: number) {
        const dst = new Float32Array(16);

        const m10 = m[4];
        const m11 = m[5];
        const m12 = m[6];
        const m13 = m[7];
        const m20 = m[8];
        const m21 = m[9];
        const m22 = m[10];
        const m23 = m[11];
        const c = Math.cos(angleInRadians);
        const s = Math.sin(angleInRadians);

        dst[4]  = c * m10 + s * m20;
        dst[5]  = c * m11 + s * m21;
        dst[6]  = c * m12 + s * m22;
        dst[7]  = c * m13 + s * m23;
        dst[8]  = c * m20 - s * m10;
        dst[9]  = c * m21 - s * m11;
        dst[10] = c * m22 - s * m12;
        dst[11] = c * m23 - s * m13;

        if (m !== dst) {
            dst[ 0] = m[ 0];
            dst[ 1] = m[ 1];
            dst[ 2] = m[ 2];
            dst[ 3] = m[ 3];
            dst[12] = m[12];
            dst[13] = m[13];
            dst[14] = m[14];
            dst[15] = m[15];
        }

        return dst;
    }


    static yRotate(m: Array<number> | Float32Array, angleInRadians: number) {
        const dst = new Float32Array(16);
    
        const m00 = m[0 * 4 + 0];
        const m01 = m[0 * 4 + 1];
        const m02 = m[0 * 4 + 2];
        const m03 = m[0 * 4 + 3];
        const m20 = m[2 * 4 + 0];
        const m21 = m[2 * 4 + 1];
        const m22 = m[2 * 4 + 2];
        const m23 = m[2 * 4 + 3];
        const c = Math.cos(angleInRadians);
        const s = Math.sin(angleInRadians);
    
        dst[ 0] = c * m00 - s * m20;
        dst[ 1] = c * m01 - s * m21;
        dst[ 2] = c * m02 - s * m22;
        dst[ 3] = c * m03 - s * m23;
        dst[ 8] = c * m20 + s * m00;
        dst[ 9] = c * m21 + s * m01;
        dst[10] = c * m22 + s * m02;
        dst[11] = c * m23 + s * m03;
    
        if (m !== dst) {
          dst[ 4] = m[ 4];
          dst[ 5] = m[ 5];
          dst[ 6] = m[ 6];
          dst[ 7] = m[ 7];
          dst[12] = m[12];
          dst[13] = m[13];
          dst[14] = m[14];
          dst[15] = m[15];
        }
    
        return dst;
    }


    static zRotate(m: Array<number> | Float32Array, angleInRadians: number) {
        const dst = new Float32Array(16);
    
        const m00 = m[0 * 4 + 0];
        const m01 = m[0 * 4 + 1];
        const m02 = m[0 * 4 + 2];
        const m03 = m[0 * 4 + 3];
        const m10 = m[1 * 4 + 0];
        const m11 = m[1 * 4 + 1];
        const m12 = m[1 * 4 + 2];
        const m13 = m[1 * 4 + 3];
        const c = Math.cos(angleInRadians);
        const s = Math.sin(angleInRadians);
    
        dst[ 0] = c * m00 + s * m10;
        dst[ 1] = c * m01 + s * m11;
        dst[ 2] = c * m02 + s * m12;
        dst[ 3] = c * m03 + s * m13;
        dst[ 4] = c * m10 - s * m00;
        dst[ 5] = c * m11 - s * m01;
        dst[ 6] = c * m12 - s * m02;
        dst[ 7] = c * m13 - s * m03;
    
        if (m !== dst) {
          dst[ 8] = m[ 8];
          dst[ 9] = m[ 9];
          dst[10] = m[10];
          dst[11] = m[11];
          dst[12] = m[12];
          dst[13] = m[13];
          dst[14] = m[14];
          dst[15] = m[15];
        }
    
        return dst;
    }

    static scale(m: Array<number> | Float32Array, sx: number, sy: number, sz: number) {
        const dst = new Float32Array(16);
    
        dst[ 0] = sx * m[0 * 4 + 0];
        dst[ 1] = sx * m[0 * 4 + 1];
        dst[ 2] = sx * m[0 * 4 + 2];
        dst[ 3] = sx * m[0 * 4 + 3];
        dst[ 4] = sy * m[1 * 4 + 0];
        dst[ 5] = sy * m[1 * 4 + 1];
        dst[ 6] = sy * m[1 * 4 + 2];
        dst[ 7] = sy * m[1 * 4 + 3];
        dst[ 8] = sz * m[2 * 4 + 0];
        dst[ 9] = sz * m[2 * 4 + 1];
        dst[10] = sz * m[2 * 4 + 2];
        dst[11] = sz * m[2 * 4 + 3];
    
        if (m !== dst) {
          dst[12] = m[12];
          dst[13] = m[13];
          dst[14] = m[14];
          dst[15] = m[15];
        }
    
        return dst;
    }

    static translate(m: Array<number> | Float32Array,  tx: number, ty: number, tz: number) {
        const dst = new Float32Array(16);
        
        dst[ 0] = m[0];
        dst[ 1] = m[1];
        dst[ 2] = m[2];
        dst[ 3] = m[3];
        dst[ 4] = m[4];
        dst[ 5] = m[5];
        dst[ 6] = m[6];
        dst[ 7] = m[7];
        dst[ 8] = m[8];
        dst[ 9] = m[9];
        dst[10] = m[10];
        dst[11] = m[11];
        dst[12] = tx;
        dst[13] = ty;
        dst[14] = tz;
        dst[15] = m[15];

        return dst;
    }

    static lookAt(cameraPosition: Vector3, target: Vector3, up: Vector3) {
        const zAxis = Vector3.subtraction(cameraPosition, target).normalize();
        const xAxis = Vector3.crossProduct(up, zAxis).normalize();
        const yAxis = Vector3.crossProduct(zAxis, xAxis).normalize();

        const translation = new Matrix4x4([
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            -cameraPosition.x, -cameraPosition.y, -cameraPosition.z, 1.0
        ]);

        const dst = new Matrix4x4([
            xAxis.x, yAxis.x, zAxis.x, 0.0,
            xAxis.y, yAxis.y, zAxis.y, 0.0,
            xAxis.z, yAxis.z, zAxis.z, 0.0,
            0.0, 0.0, 0.0, 1.0
        ]);

        return this.multiply(dst, translation).elements;
    }


    // static lookAt(cameraPosition: Array<number> | Float32Array, target: Array<number> | Float32Array, up: Array<number> | Float32Array) {
    //     const zAxis = this.normalize(
    //         this.subtractVectors(cameraPosition, target));
    //         const xAxis = this.normalize(this.cross(up, zAxis));
    //         const yAxis = this.normalize(this.cross(zAxis, xAxis));
    
    //     const translation = [
    //       1.0, 0.0, 0.0, 0.0,
    //       0.0, 1.0, 0.0, 0.0,
    //       0.0, 0.0, 1.0, 0.0,
    //       -cameraPosition[0], -cameraPosition[1], -cameraPosition[2], 1.0
    //     ];
    
    //     let dst: Array<number> | Float32Array = [
    //       xAxis[0], yAxis[0], zAxis[0], 0.0,
    //       xAxis[1], yAxis[1], zAxis[1], 0.0,
    //       xAxis[2], yAxis[2], zAxis[2], 0.0,
    //       0.0, 0.0, 0.0, 1.0
    //     ];
    
    //     dst = this.multiply(dst, translation);
    
    //     return dst;
    // }
    
    static perspective(fovInRadians: number, aspect: number, near: number, far: number) {
        const dst = new Float32Array(16);
        const f = Math.tan(Math.PI * 0.5 - 0.5 * fovInRadians);
        const rangeInv = 1.0 / (near - far);
    
        dst[ 0] = f / aspect;
        dst[ 1] = 0;
        dst[ 2] = 0;
        dst[ 3] = 0;
        dst[ 4] = 0;
        dst[ 5] = f;
        dst[ 6] = 0;
        dst[ 7] = 0;
        dst[ 8] = 0;
        dst[ 9] = 0;
        dst[10] = (near + far) * rangeInv;
        dst[11] = -1;
        dst[12] = 0;
        dst[13] = 0;
        dst[14] = near * far * rangeInv * 2;
        dst[15] = 0;
    
        return dst;
    }

    makePerspective( left: number, right: number, top: number, bottom: number, near: number, far: number ) {
        
        const te = this.elements;

        const x = 2 * near / ( right - left );
        const y = 2 * near / ( top - bottom );
    
        const a = ( right + left ) / ( right - left );
        const b = ( top + bottom ) / ( top - bottom );
		const c = - ( far + near ) / ( far - near );
		const d = - 2 * far * near / ( far - near );
    
        te[ 0 ] = x;	te[ 4 ] = 0;	te[ 8 ] = a;	te[ 12 ] = 0;
        te[ 1 ] = 0;	te[ 5 ] = y;	te[ 9 ] = b;	te[ 13 ] = 0;
        te[ 2 ] = 0;	te[ 6 ] = 0;	te[ 10 ] = c;	te[ 14 ] = d;
        te[ 3 ] = 0;	te[ 7 ] = 0;	te[ 11 ] = - 1;	te[ 15 ] = 0;
    
        return this;
    }

    static project( left: number, right: number, top: number, bottom: number, near: number, far: number ) {
        
        const te = (new Matrix4x4()).elements;

        const x = 2 * near / ( right - left );
        const y = 2 * near / ( top - bottom );
    
        const a = ( right + left ) / ( right - left );
        const b = ( top + bottom ) / ( top - bottom );
		const c = - ( far + near ) / ( far - near );
		const d = - 2 * far * near / ( far - near );
    
        te[ 0 ] = x;	te[ 4 ] = 0;	te[ 8 ] = a;	te[ 12 ] = 0;
        te[ 1 ] = 0;	te[ 5 ] = y;	te[ 9 ] = b;	te[ 13 ] = 0;
        te[ 2 ] = 0;	te[ 6 ] = 0;	te[ 10 ] = c;	te[ 14 ] = d;
        te[ 3 ] = 0;	te[ 7 ] = 0;	te[ 11 ] = - 1;	te[ 15 ] = 0;
    
        return te;
    }

    transpose() {
        const te = this.elements;

        return new Matrix4x4(
            [
                te[0], te[4], te[8], te[12],
                te[1], te[5], te[9], te[13],
                te[2], te[6], te[10], te[14],
                te[3], te[7], te[11], te[15]
        ]);
    }
}

export {
    Matrix4x4,
};
