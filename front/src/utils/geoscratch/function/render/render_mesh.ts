import { Matrix4x4 } from '../../core/math/matrix4';
import { BoundingBox } from './../../core/math/bounding_box';
import { VertexBufferHandle, IndexBufferHandle } from './resource_handle';

export class MeshHandle {
    vertexPosHandle: VertexBufferHandle;
    vertexNormalHandle: VertexBufferHandle;
    vertexTangentHandle: VertexBufferHandle;
    vertexTexcoordHandle: VertexBufferHandle;
    indexHandle: IndexBufferHandle;
    boundingBox: BoundingBox;

    constructor() {
        this.vertexPosHandle = new VertexBufferHandle;
        this.vertexNormalHandle = new VertexBufferHandle;
        this.vertexTangentHandle = new VertexBufferHandle;
        this.vertexTexcoordHandle = new VertexBufferHandle;
        this.indexHandle = new IndexBufferHandle;
        this.boundingBox = new BoundingBox;
    }

    getHashValue() {
        return this.vertexPosHandle.getHashValue() ^ (this.indexHandle.getHashValue() << 1);
    }
}

export type PosNormalTangentTexVertex = {
    x: number,
    y: number,
    z: number,
    nx: number,
    ny: number,
    nz: number,
    tx: number,
    ty: number,
    tz: number,
    u: number,
    v: number
}

export type VertexBinding = {
    index0: number,
    index1: number,
    index2: number,
    index3: number,
    weight0: number,
    weight1: number,
    weight2: number,
    weight3: number
}

export class RenderMesh {

    // per instance member
    instanceID: number;
    modelMatrix: Matrix4x4;
    jointMatrices: Array<Matrix4x4>;
    enableVertexBlending: boolean;

    // per asset member
    boundingBox: BoundingBox;
    guid: number;
    material: number;

    vertexPosBuffer: VertexBufferHandle;
    vertexNormalBuffer: VertexBufferHandle;
    vertexTangentBuffer: VertexBufferHandle;
    vertexTexcoordBuffer: VertexBufferHandle;
    indexBuffer: IndexBufferHandle;

    constructor() {
        this.instanceID = 0;
        this.modelMatrix = new Matrix4x4();
        this.jointMatrices = [];
        this.enableVertexBlending = false;

        this.boundingBox = new BoundingBox();
        this.guid = 0;
        this.material = 0;
        
        this.vertexPosBuffer = new VertexBufferHandle();
        this.vertexNormalBuffer = new VertexBufferHandle();
        this.vertexTangentBuffer = new VertexBufferHandle();
        this.vertexTexcoordBuffer = new VertexBufferHandle();
        this.indexBuffer = new IndexBufferHandle();
    }

    getHashValue() {
        return this.vertexPosBuffer.getHashValue() ^ (this.indexBuffer.getHashValue() << 1);
    }

}