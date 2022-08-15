import { Shader } from "../core/shader/shader";
import * as THREE from 'three';
import { PositionOptions } from "mapbox-gl";

class tbvsSymbol {
    symbolID: number | string;
    rowIndexStart: number;
    rowIndexLength: number;
    position: Array<number>;

    constructor (symbolID: number | string, rowIndexStart: number, rowIndexLength: number, position: Array<number>, ) {
        this.symbolID = symbolID;
        this.rowIndexStart = rowIndexStart;
        this.rowIndexLength = rowIndexLength;
        this.position = position;
    }

    addRowindexToMemory(rowIndices: Array<number>): Array<number> {
        let i = 0;
        while (i < this.rowIndexLength) {
            rowIndices.push(this.rowIndexStart + i);

            i++;
        }

        return rowIndices;
    }

    addPosition2DToMemory(positions: Array<number>): Array<number> {
        let i = 0;
        while (i < this.rowIndexLength) {
            positions.push(this.position[0]);
            positions.push(this.position[1]);
            positions.push(this.position[2]);
            positions.push(this.position[3]);

            i++;
        }

        return positions;
    }

    addPosition3DToMemory(positions: Array<number>): Array<number> {
        let i = 0;
        while (i < this.rowIndexLength) {
            positions.push(this.position[0]);
            positions.push(this.position[1]);
            positions.push(this.position[2]);

            i++;
        }

        return positions;
    }
}

class tbvsSymbols {
    symbols: Array<tbvsSymbol>;
    rowIndices: Array<number>;
    positions: Array<number>;
    vao: WebGLVertexArrayObject | null;
    vbo_row: WebGLBuffer | null;
    vbo_pos: WebGLBuffer | null;


    constructor(symbols: Array<tbvsSymbol>, type="2D") {
        this.symbols = symbols;
        this.rowIndices = [];
        this.positions = [];

        this.vao = null;
        this.vbo_row = null;
        this.vbo_pos = null;

        if (type === "2D")
            this.make2DSymbolsMemory();
        else if (type === "3D")
            this.make3DSymbolsMemory();
    }

    make2DSymbolsMemory(): void {
        let i = 0;
        while (i < this.symbols.length) {
            this.rowIndices = this.symbols[i].addRowindexToMemory(this.rowIndices);
            this.positions = this.symbols[i].addPosition2DToMemory(this.positions);
    
            i++;
        }
    }

    make3DSymbolsMemory(): void {
        let i = 0;
        while (i < this.symbols.length) {
            this.rowIndices = this.symbols[i].addRowindexToMemory(this.rowIndices);
            this.positions = this.symbols[i].addPosition3DToMemory(this.positions);
    
            i++;
        }
    }

    setup(gl: WebGL2RenderingContext, shader: Shader): void {

        this.vao = gl.createVertexArray();
        gl.bindVertexArray(this.vao);

        this.vbo_row = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, this.vbo_row);
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(this.rowIndices), gl.STATIC_DRAW);
        shader.setVertexBufferPointer_Instancing(gl, 0, 1, gl.FLOAT, false, 4 * 1, 0);

        this.vbo_pos = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, this.vbo_pos);
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(this.positions), gl.STATIC_DRAW);
        shader.setVertexBufferPointer_Instancing(gl, 1, 4, gl.FLOAT, false, 4 * 4, 0);

        gl.bindVertexArray(null);
    }

    Geometry4Three() {

        const posArray3 : Array<number> = [];
        for (let i = 0 ; i < this.positions.length * 3  / 2; ++i) {
            // if ( i % 4 === 3) posArray1.push(this.positions[i]);
            // posArray3.push(0.0);    
            // else posArray3.push(this.positions[i]);    
            // if ( i % 4 === 3) continue;
            posArray3.push(0);
        }

        const geom =  new THREE.InstancedBufferGeometry();
        const posAttrib = new THREE.InstancedBufferAttribute(new Float32Array(posArray3), 3, false, 1);
        const posExAttrib = new THREE.InstancedBufferAttribute(new Float32Array(this.positions), 4, false, 1);
        const rowAttrib = new THREE.InstancedBufferAttribute(new Float32Array(this.rowIndices), 1, false, 1);

        // geom.setAttribute('position', dummyAttrib);
        geom.setAttribute('position', posAttrib);
        geom.setAttribute('positionEx', posExAttrib);
        geom.setAttribute('blockIndex', rowAttrib);
        geom.setDrawRange(0, 63);
        geom.instanceCount = this.rowIndices.length;
        // geom.instanceCount =20;

        console.log(geom);
        return geom;
    }

    use(gl: WebGL2RenderingContext) {
        gl.bindVertexArray(this.vao);
    }

    getNumInstance() {
        return this.rowIndices.length;
    }

    isEmpty(): boolean {
        return !this.symbols.length;
    }
}

class BillboardSymbol {
    symbolID: number;
    position: Array<number>;

    constructor (symbolID: number, position: Array<number>, private rotation: number, private scale: [number, number]) {
        this.symbolID = symbolID;
        this.position = position;
    }

    addPosition2DToMemory(positions: Array<number>): Array<number> {
        let i = 0;
        positions.push(this.position[0]);
        positions.push(this.position[1]);
        positions.push(this.position[2]);
        positions.push(this.position[3]);

        return positions;
    }
}

class BillboardSymbolManager {
    deviceDataBuffer: Float32Array;
    vao: WebGLVertexArrayObject | null;
    vbo: WebGLBuffer | null;
    maxInstanceNum: number;
    instanceNum: number;


    constructor(maxInstanceNum: number) {
        this.vao = null;
        this.vbo = null;
        this.maxInstanceNum = maxInstanceNum;
        this.instanceNum = 0;
        this.deviceDataBuffer = new Float32Array(maxInstanceNum * 7);
    }

    setMemory(position: Array<number>, rotation: number, scale: [number, number]): void {

        // set position information of the marker
        this.deviceDataBuffer[this.maxInstanceNum * 0 + this.instanceNum * 4 + 0] = position[0];
        this.deviceDataBuffer[this.maxInstanceNum * 0 + this.instanceNum * 4 + 1] = position[1];
        this.deviceDataBuffer[this.maxInstanceNum * 0 + this.instanceNum * 4 + 2] = position[2];
        this.deviceDataBuffer[this.maxInstanceNum * 0 + this.instanceNum * 4 + 3] = position[3];

        // set rotation information of the marker
        this.deviceDataBuffer[this.maxInstanceNum * 4 + this.instanceNum * 1 + 0] = rotation;

        // set scale information of the marker
        this.deviceDataBuffer[this.maxInstanceNum * 5 + this.instanceNum * 2 + 0] = scale[1];
        this.deviceDataBuffer[this.maxInstanceNum * 5 + this.instanceNum * 2 + 1] = scale[0];

        this.instanceNum ++;
    }

    setup(gl: WebGL2RenderingContext, shader: Shader): void {

        this.vao = gl.createVertexArray();
        gl.bindVertexArray(this.vao);
        console.log(this.deviceDataBuffer);

        this.vbo = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, this.vbo);
        gl.bufferData(gl.ARRAY_BUFFER, this.deviceDataBuffer, gl.STATIC_DRAW);
        shader.setVertexBufferPointer_Instancing(gl, 0, 4, gl.FLOAT, false, 4 * 4, this.maxInstanceNum * 0 * 4);
        shader.setVertexBufferPointer_Instancing(gl, 1, 1, gl.FLOAT, false, 4 * 1, this.maxInstanceNum * 4 * 4);
        shader.setVertexBufferPointer_Instancing(gl, 2, 2, gl.FLOAT, false, 4 * 2, this.maxInstanceNum * 5 * 4);

        gl.bindVertexArray(null);
    }

    use(gl: WebGL2RenderingContext) {
        gl.bindVertexArray(this.vao);
    }

    getNumInstance() {
        return this.instanceNum;
    }

    isEmpty(): boolean {
        return !this.instanceNum;
    }
}

export {
    tbvsSymbol,
    tbvsSymbols,
    BillboardSymbolManager
};