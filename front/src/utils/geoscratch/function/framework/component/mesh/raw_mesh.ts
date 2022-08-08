enum PrimitiveType {
    point,
    line,
    triangle,
    quad
}

export interface baseVertexBuffer {
    vertexCount: number,
    positions: Array<number>,
    normals: Array<number>,
    tangents: Array<number>,
    uvs: Array<number>
}

export interface tbvsVertexBuffer extends baseVertexBuffer {
    blockIndex: Array<number>
}

export interface RawVertexBuffer {
    vertexCount: number,
    positions: Array<number>,
    normals: Array<number>,
    tangents: Array<number>,
    uvs: Array<number>
}

export interface RawIndexBuffer {
    primitiveType: PrimitiveType,
    primitiveCount: number,
    indices: Array<number>
}

export interface MaterialTexture {
    baseColor: string,
    metallicRoughness: string,
    normal: string
}

export interface SataticMeshData {
    vertexBuffer: RawVertexBuffer,
    indexBuffer: RawIndexBuffer,
    materialTexture: MaterialTexture
}

export type AnyVertexBuffer = RawVertexBuffer;
export type AnyMatrialTexture = MaterialTexture;

export class SubMeshComponent {
    name: string;
    vertexBuffer: AnyVertexBuffer | null;
    indexBuffer: RawIndexBuffer | null;
    materialTexture: AnyMatrialTexture | null;
    // materialShader: 

    constructor(name: string) {
        this.name = name;
        this.vertexBuffer = null;
        this.indexBuffer = null;
        this.materialTexture = null;
    }
}