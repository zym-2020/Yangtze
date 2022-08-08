export interface Vertex {
    px: number;
    py: number;
    pz: number;
    nx: number;
    ny: number;
    nz: number;
    tx: number;
    ty: number;
    tz: number;
    u: number;
    v: number;
}

export interface MeshData {
    vertexBuffer: Array<Vertex>,
    indexBuffer: Array<number>
}