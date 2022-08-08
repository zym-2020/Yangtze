import { Texture } from "./texture";

class Vertex {
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

    constructor(px: number, py: number, pz?: number, nx?: number, ny?: number, nz?: number, tx?: number, ty?: number, tz?: number, u?: number, v?: number) {
        this.px = Math.fround(px);
        this.py = Math.fround(py);
        this.pz = pz || Math.fround(0.0);
        this.nx = nx || Math.fround(0.0);
        this.ny = ny || Math.fround(0.0);
        this.nz = nz || Math.fround(0.0);
        this.tx = tx || Math.fround(0.0);
        this.ty = ty || Math.fround(0.0);
        this.tz = tz || Math.fround(0.0);
        this.u = u || Math.fround(0.0);
        this.v = v || Math.fround(0.0);
    }
}

class MeshData {
    vertex_buffer: Array<Vertex>;
    index_buffer: Array<number>;
    texture_buffer: Array<Texture>

    constructor(vertices: Array<Vertex>, indices?: Array<number>) {
        this.vertex_buffer = vertices;
        this.index_buffer = indices || [];
        this.texture_buffer = [];
    }
}

export default {
    Vertex,
    MeshData,
}