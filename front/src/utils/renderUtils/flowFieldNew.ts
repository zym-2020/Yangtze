import axios from 'axios';
import type { AxiosResponse } from 'axios';
import { Shader } from './shader';

export type ShaderSrc = {
    [key:string]: {
        "vert": string, 
        "frag": string, 
        "tfVars"?: string[]
    }
}

export type FLowParams = {
    lineNum: number;
    segmentNum: number;
    fullLife: number;
    progressRate: number;
    speedFactor: number;
    dropRate: number;
    dropRateBump: number;
    fillWidth: number;
    aaWidth: number;
    content: string;
}

export type FlowLimit = {
    "constraints": {
        "max_drop_rate": number,
        "max_drop_rate_bump": number,
        "max_segment_num": number,
        "max_streamline_num": number,
        "max_texture_size": number
    }, 
    "flow_boundary": {
        "u_max": number,
        "u_min": number,
        "v_max": number,
        "v_min": number
    },
    "area_masks": string[], 
    "flow_fields": string[], 
    "valid_address": string[], 
}

export type ShaderURL = {
    [key: string]: {
        "vert": string, 
        "frag": string, 
        "tfVars"?: string[]
    }
}

export async function loadShaderSrc(shaderURL: ShaderURL) {
    const res: ShaderSrc = {};
    for(const key in shaderURL) {
        const vertexSource = await axios.get(shaderURL[key]["vert"])
        .then((response: AxiosResponse<string>) => {
            return response.data;
        });
        const fragmentSource = await axios.get(shaderURL[key]["frag"])
        .then((response: AxiosResponse<string>) => {
            return response.data;
        });
        res[key] = {
            "vert": vertexSource, 
            "frag": fragmentSource, 
            "tfVars": shaderURL[key]["tfVars"]
        }
    }
    return res;
}

// create random positions and velocities.
const rand = (min: number, max: number) => {
    if (max === undefined) {
        max = min;
        min = 0;
    }
    return Math.random() * (max - min) + min;
};

async function loadShader_url(gl: WebGL2RenderingContext, name: string, vertexUrl: string, fragmentUrl: string, transformFeedbackVaryings?: Array<string>) : Promise<Shader>{

    const vertexSource = await axios.get(vertexUrl)
    .then((response) => {
        return response.data;
    });
    const fragmentSource = await axios.get(fragmentUrl)
    .then((response) => {
        return response.data;
    });

    return new Shader(gl, name, [vertexSource, fragmentSource], transformFeedbackVaryings);
}

function makeShaderFromSrc(gl: WebGL2RenderingContext, shaderSrc: ShaderSrc) {
    const shaders: {[key: string]: Shader} = {};
    for(const key in shaderSrc) {
        const aShader = new Shader(
            gl, key, 
            [shaderSrc[key]["vert"], shaderSrc[key]["frag"]], 
            shaderSrc[key]["tfVars"]
        );
        shaders[key] = aShader;
    }
    return shaders;
}

function makeBufferBySource(gl: WebGL2RenderingContext, target: number, srcData: ArrayBuffer, usage: number): WebGLBuffer | null {
    const vbo = gl.createBuffer();
    if (vbo == null) {
        console.log("ERROR::Vertex Buffer cannot be created!");
        return vbo;
    }

    gl.bindBuffer(target, vbo);
    gl.bufferData(target, srcData, usage);
    gl.bindBuffer(target, null);
    return vbo;
}

function makeBufferBySize(gl: WebGL2RenderingContext, target: number, dataSize: number, usage: number): WebGLBuffer | null {
    const vbo = gl.createBuffer();
    if (vbo == null) {
        console.log("ERROR::Vertex Buffer cannot be created!");
        return vbo;
    }

    gl.bindBuffer(target, vbo);
    gl.bufferData(target, dataSize, usage);
    gl.bindBuffer(target, null);
    return vbo;
}

export function loadTexture(gl: WebGL2RenderingContext, url: string, interpolationType = gl.LINEAR) {
    const textureID = gl.createTexture();
    gl.bindTexture(gl.TEXTURE_2D, textureID);

    const image = new Image();
    image.src = url;
    image.onload = function() {
        gl.bindTexture(gl.TEXTURE_2D, textureID);
        gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);
        gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image);
        gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, false);

        if (isPowerOf2(image.width) && isPowerOf2(image.height)) {
            gl.generateMipmap(gl.TEXTURE_2D);
        } else {
            gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
            gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
            gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, interpolationType);
            gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, interpolationType);
        }
    };

    return textureID;

    function isPowerOf2(value: number) {
        return (value & (value - 1)) == 0;
    }
}

type TextureOffset = {
    offsetX: number;
    offsetY: number;
}

export class FlowFieldManagerNew {
    private simulationVAO: WebGLVertexArrayObject | null;
    private renderVAO: WebGLVertexArrayObject | null;
    public fieldSequence: Array<WebGLTexture | null>;
    public maskSequence: Array<WebGLTexture | null>;
    public validSequence: Array<WebGLTexture | null>;

    private XFBO: WebGLTransformFeedback | null;

    private simulationBuffer: WebGLBuffer | null;
    private lifeBuffer: WebGLBuffer | null;
    private xfSimulationBuffer: WebGLBuffer | null;
    private xfLifeBuffer: WebGLBuffer | null;

    private poolTextureBuffer: WebGLTexture | null;

    private UBO: WebGLBuffer|null;

    private updateShader: Shader | null;
    private drawShader: Shader | null;
    private poolShader: Shader | null;
    private textureShader: Shader | null;

    private uboMapBuffer: Float32Array;
    private particleMapBuffer : Float32Array | null;

    flowBoundary: Array<number>;

    private maxBlockSize: number;
    private maxBlockColumn: number;
    private textureOffsetArray: Array<TextureOffset>;

    // Temporary render variable
    private beginBlock = -1.0;
    private streamline = 0.0;
    private segmentNum = 0.0;
    private vaTextureInfo: WebGLTexture = 0; 
    private ffTextureInfo: Array<WebGLTexture> = []; 
    private maskTextureInfo: Array<WebGLTexture> = []; 

    constructor(
        public flowLimits: FlowLimit, public flowParams: FLowParams, 
        private shaderSrc: ShaderSrc,
    ) {
        this.fieldSequence = []; // store all the flow textures
        this.maskSequence = []; // store all the mask textures
        this.validSequence = [];
        this.simulationVAO = null;
        this.renderVAO = null;
        this.simulationBuffer = null;
        this.lifeBuffer = null;
        this.xfSimulationBuffer = null;
        this.xfLifeBuffer = null;
        this.poolTextureBuffer = null;
        this.XFBO = null;
        this.UBO = null;
        this.updateShader = null;
        this.drawShader = null;
        this.poolShader = null;
        this.textureShader = null;
        this.uboMapBuffer = new Float32Array(12);  // uniform block!
        this.particleMapBuffer = null;

        this.flowBoundary = []; // boundary 4 flow u&v

        this.maxBlockSize = 0.0;
        this.maxBlockColumn = 0.0;
        this.textureOffsetArray = [];
    }

    // static async Create(gl: WebGL2RenderingContext, flowLimits: FlowLimit, flowParams: FLowParams) {
    //     const ffManager = new FlowFieldManager(flowLimits, flowParams);
    //     await ffManager.Prepare(gl);

    //     return ffManager;
    // }

    async Prepare(gl: WebGL2RenderingContext) {
        // Get boundaries of flow speed
        this.flowBoundary[0] = this.flowLimits["flow_boundary"]["u_min"];
        this.flowBoundary[1] = this.flowLimits["flow_boundary"]["v_min"];
        this.flowBoundary[2] = this.flowLimits["flow_boundary"]["u_max"];
        this.flowBoundary[3] = this.flowLimits["flow_boundary"]["v_max"];

        // Set uniform buffer object data (something will not change)
        this.uboMapBuffer[8] = this.flowBoundary[0];
        this.uboMapBuffer[9] = this.flowBoundary[1];
        this.uboMapBuffer[10] = this.flowBoundary[2];
        this.uboMapBuffer[11] = this.flowBoundary[3];

        // Prepare descriptive variables
        const MAX_TEXTURE_SIZE = this.flowLimits["constraints"]["max_texture_size"];
        const MAX_STREAMLINE_NUM = this.flowLimits["constraints"]["max_streamline_num"];
        const MAX_SEGMENT_NUM = this.flowLimits["constraints"]["max_segment_num"];

        // Load textures of flow fields
        for (const url of this.flowLimits["flow_fields"]) {
            this.fieldSequence.push(loadTexture(gl, url, gl.NEAREST));
        }

        // Load textures of area masks
        for (const url of this.flowLimits["area_masks"]) {
            this.maskSequence.push(loadTexture(gl, url, gl.NEAREST));
        }

        // Load textures of valid address
        for (const url of this.flowLimits["valid_address"]) {
            this.validSequence.push(loadTexture(gl, url, gl.NEAREST));
        }

        this.maxBlockSize = Math.ceil(Math.sqrt(MAX_STREAMLINE_NUM));  // block num in a row/col
        this.maxBlockColumn =  Math.floor(MAX_TEXTURE_SIZE / this.maxBlockSize); // column num of a block
        for (let i = 0; i < MAX_SEGMENT_NUM; i++) { // get the particle position in the texture
            const offset: TextureOffset = {
                offsetX: (i % this.maxBlockColumn) * this.maxBlockSize,
                offsetY: Math.floor(i / this.maxBlockColumn) * this.maxBlockSize
            };

            this.textureOffsetArray.push(offset);
        }

        // Set data of particle block used to fill simulation buffer and particle pool texture
        this.particleMapBuffer = new Float32Array(this.maxBlockSize * this.maxBlockSize * 3).fill(0);
        for (let i = 0; i < MAX_STREAMLINE_NUM; i++) {
            this.particleMapBuffer[i * 3 + 0] = rand(0, 1.0);
            this.particleMapBuffer[i * 3 + 1] = rand(0, 1.0);
            this.particleMapBuffer[i * 3 + 2] = rand(0, 0);
        }
        console.log(this.particleMapBuffer);

        // Set coundown for particles
        const particleCountdownArray = new Float32Array(MAX_STREAMLINE_NUM);
        for (let i = 0; i < MAX_STREAMLINE_NUM; i++) {
            particleCountdownArray[i] = Math.floor(rand(this.flowParams.segmentNum, this.flowParams.fullLife));
        }

        // Set Buffer used to simulation
        this.simulationBuffer = makeBufferBySource(gl, gl.ARRAY_BUFFER, this.particleMapBuffer.slice(0, MAX_STREAMLINE_NUM * 3), gl.DYNAMIC_DRAW);
        this.xfSimulationBuffer = makeBufferBySource(gl, gl.TRANSFORM_FEEDBACK_BUFFER, this.particleMapBuffer.slice(0, MAX_STREAMLINE_NUM * 3), gl.DYNAMIC_DRAW);
        this.lifeBuffer = makeBufferBySource(gl, gl.ARRAY_BUFFER, particleCountdownArray, gl.DYNAMIC_DRAW);
        this.xfLifeBuffer = makeBufferBySource(gl, gl.TRANSFORM_FEEDBACK_BUFFER, particleCountdownArray, gl.DYNAMIC_DRAW);

        // Make uniform buffer object
        this.UBO = gl.createBuffer();
        gl.bindBuffer(gl.UNIFORM_BUFFER, this.UBO);
        gl.bufferData(gl.UNIFORM_BUFFER, 48, gl.DYNAMIC_DRAW);
        gl.bindBuffer(gl.UNIFORM_BUFFER, null);


        // Set particle pool
        this.poolTextureBuffer = gl.createTexture();
        gl.bindTexture(gl.TEXTURE_2D, this.poolTextureBuffer);
        gl.texStorage2D(gl.TEXTURE_2D, 1, gl.RGB32F, MAX_TEXTURE_SIZE, MAX_TEXTURE_SIZE);
        gl.pixelStorei(gl.UNPACK_ALIGNMENT, 1);
        for (let i = 0; i < MAX_SEGMENT_NUM; i++) {
            // init each block(particle line status)
            gl.texSubImage2D(
                gl.TEXTURE_2D, 0, 
                this.textureOffsetArray[i].offsetX, this.textureOffsetArray[i].offsetY, 
                this.maxBlockSize, this.maxBlockSize, gl.RGB, gl.FLOAT, this.particleMapBuffer
            );
        }
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST);
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST);

        // Set Vertex Array Object
        this.simulationVAO = gl.createVertexArray();
        gl.bindVertexArray(this.simulationVAO);
        gl.bindBuffer(gl.ARRAY_BUFFER, this.simulationBuffer);
        gl.vertexAttribPointer(0, 3, gl.FLOAT, false, 3 * 4, 0);
        gl.enableVertexAttribArray(0);
        gl.bindBuffer(gl.ARRAY_BUFFER, this.lifeBuffer);
        gl.vertexAttribPointer(1, 1, gl.FLOAT, false, 1 * 4, 0);
        gl.enableVertexAttribArray(1);
        gl.bindVertexArray(null);
        gl.bindBuffer(gl.ARRAY_BUFFER, null);

        this.renderVAO = gl.createVertexArray();
        gl.bindVertexArray(this.renderVAO);
        gl.bindBuffer(gl.ARRAY_BUFFER, this.lifeBuffer);
        gl.vertexAttribPointer(0, 1, gl.FLOAT, false, 1 * 4, 0);
        gl.enableVertexAttribArray(0);
        gl.vertexAttribDivisor(0, 1);
        gl.bindVertexArray(null);
        gl.bindBuffer(gl.ARRAY_BUFFER, null);

        // Set Transform Feedback Object
        this.XFBO = gl.createTransformFeedback();
        gl.bindTransformFeedback(gl.TRANSFORM_FEEDBACK, this.XFBO);
        gl.bindBuffer(gl.TRANSFORM_FEEDBACK_BUFFER, this.xfSimulationBuffer);
        gl.bindBufferRange(gl.TRANSFORM_FEEDBACK_BUFFER, 0, this.xfSimulationBuffer, 0, MAX_STREAMLINE_NUM * 12);
        gl.bindBuffer(gl.TRANSFORM_FEEDBACK_BUFFER, this.xfLifeBuffer);
        gl.bindBufferRange(gl.TRANSFORM_FEEDBACK_BUFFER, 1, this.xfLifeBuffer, 0, MAX_STREAMLINE_NUM * 4);
        gl.bindTransformFeedback(gl.TRANSFORM_FEEDBACK, null);
        gl.bindBuffer(gl.TRANSFORM_FEEDBACK_BUFFER, null);

        // Build Shaders
        const shaders = makeShaderFromSrc(gl, this.shaderSrc);
        this.updateShader = shaders["update"];
        this.drawShader = shaders["draw"];
        this.poolShader = shaders["poolDebug"];
        this.textureShader = shaders["textureDebug"];
        // this.updateShader = await loadShader_url(gl, "update", "http://localhost:8080/shaders/update.vert", "http://localhost:8080/shaders/update.frag", ['newPosition', 'aliveTime']);
        // this.drawShader = await loadShader_url(gl, "draw", "http://localhost:8080/shaders/ribbonParticle.vert", "http://localhost:8080/shaders/ribbonParticle.frag");
        // this.poolShader = await loadShader_url(gl, "textureDebug", "http://localhost:8080/shaders/showPool.vert", "http://localhost:8080/shaders/showPool.frag");
        // this.textureShader = await loadShader_url(gl, "textureDebug", "http://localhost:8080/shaders/texture.vert", "http://localhost:8080/shaders/texture.frag");    
        return true;
    }

    getFieldTexture(index: number) {
        if (index < 0 || index >= this.fieldSequence.length)
            return null;
        
        return this.fieldSequence[index];
    }

    getMaskTexture(index: number) {
        if (index < 0 || index >= this.maskSequence.length)
            return null;
        
        return this.maskSequence[index];
    }
    getValidTexture(progressRate: number) {
        const progress = progressRate * (this.fieldSequence.length - 1.0);
        const fractionalPart = progress - Math.floor(progress);

        return fractionalPart < 0.5 ? this.validSequence[Math.floor(progress)] : this.validSequence[Math.ceil(progress)];
    }

    getFieldTextures(progressRate: number) {
        const progress = progressRate * (this.fieldSequence.length - 1.0);

        return [this.fieldSequence[Math.floor(progress)], this.fieldSequence[Math.ceil(progress)]];
    }

    getMaskTextures(progressRate: number) {
        const progress = progressRate * (this.maskSequence.length - 1.0);

        return [this.maskSequence[Math.floor(progress)], this.maskSequence[Math.ceil(progress)]];
    }

    getProgressBetweenTexture(progressRate: number) {
        const progress = progressRate * (this.fieldSequence.length - 1.0);

        return progress - Math.floor(progress);
    }

    bindUBO(gl: WebGL2RenderingContext, bindingPointIndex: number) {
        gl.bindBuffer(gl.UNIFORM_BUFFER, this.UBO);
        gl.bufferSubData(gl.UNIFORM_BUFFER, 0, this.uboMapBuffer);
        gl.bindBufferBase(gl.UNIFORM_BUFFER, bindingPointIndex, this.UBO);
    }

    step(stepSize: number) {
        this.flowParams.progressRate = (this.flowParams.progressRate + stepSize) - Math.floor(this.flowParams.progressRate + stepSize);
    }

    tickLogicCount() {
        this.step(0.002);
        this.beginBlock = (this.beginBlock + 1) % this.flowLimits["constraints"]["max_segment_num"];
        // console.log(this.beginBlock);

        this.uboMapBuffer[0] = this.getProgressBetweenTexture(this.flowParams.progressRate);
        this.uboMapBuffer[1] = this.flowParams.segmentNum;
        this.uboMapBuffer[2] = this.flowParams.segmentNum * 3;
        this.uboMapBuffer[3] = this.flowParams.dropRate;
        this.uboMapBuffer[4] = this.flowParams.dropRateBump;
        this.uboMapBuffer[5] = this.flowParams.speedFactor * 0.015 * 100;
    }
    tickLogic(deltaTime: number) {
        this.step(deltaTime * 0.02);
        this.beginBlock = (this.beginBlock + 1) % this.flowLimits.constraints["max_segment_num"];

        this.uboMapBuffer[0] = this.getProgressBetweenTexture(this.flowParams.progressRate);
        this.uboMapBuffer[1] = this.flowParams.segmentNum;
        this.uboMapBuffer[2] = this.flowParams.segmentNum * 3;
        this.uboMapBuffer[3] = this.flowParams.dropRate;
        this.uboMapBuffer[4] = this.flowParams.dropRateBump;
        this.uboMapBuffer[5] = this.flowParams.speedFactor * deltaTime * 100;
    }

    tickRender(gl: WebGL2RenderingContext) {
        if(this.updateShader === null || this.drawShader === null) {
            return;
        }
        if(this.poolShader === null || this.textureShader === null) {
            return;
        }

        this.vaTextureInfo = this.getValidTexture(this.flowParams.progressRate) as WebGLTexture;
        this.ffTextureInfo = this.getFieldTextures(this.flowParams.progressRate) as WebGLTexture[];
        this.maskTextureInfo = this.getMaskTextures(this.flowParams.progressRate) as WebGLTexture[];
        this.streamline = this.flowParams.lineNum;
        this.segmentNum = this.flowParams.segmentNum;
        this.bindUBO(gl, 0);

        // gl.clearColor(0.0, 0.0, 0.0, 1.0);
        // gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
        gl.disable(gl.BLEND);

        // Pass 1 - Operation 1: Simulation
        gl.activeTexture(gl.TEXTURE0);
        gl.bindTexture(gl.TEXTURE_2D, this.ffTextureInfo[0]);
        gl.activeTexture(gl.TEXTURE1);
        gl.bindTexture(gl.TEXTURE_2D, this.maskTextureInfo[0]);
        gl.activeTexture(gl.TEXTURE2);
        gl.bindTexture(gl.TEXTURE_2D, this.ffTextureInfo[1]);
        gl.activeTexture(gl.TEXTURE3);
        gl.bindTexture(gl.TEXTURE_2D, this.maskTextureInfo[1]);
        gl.activeTexture(gl.TEXTURE4);
        gl.bindTexture(gl.TEXTURE_2D, this.vaTextureInfo);
        this.updateShader.use(gl);
        this.updateShader.setInt(gl, "flowField1", 0);
        this.updateShader.setInt(gl, "mask1", 1);
        this.updateShader.setInt(gl, "flowField2", 2);
        this.updateShader.setInt(gl, "mask2", 3);
        this.updateShader.setInt(gl, "validAddress", 4);
        this.updateShader.setFloat(gl, "randomSeed", Math.random());
        this.updateShader.setUniformBlock(gl, "FlowFieldUniforms", 0);
        this.updateShader.setFloat2(gl, "boundary", gl.canvas.width, gl.canvas.height);

        gl.enable(gl.RASTERIZER_DISCARD); // prevent generating primitives
        gl.bindVertexArray(this.simulationVAO);
        gl.bindTransformFeedback(gl.TRANSFORM_FEEDBACK, this.XFBO);

        gl.beginTransformFeedback(gl.POINTS);
        gl.drawArrays(gl.POINTS, 0, this.streamline);
        gl.endTransformFeedback();

        gl.bindVertexArray(null);
        gl.bindTransformFeedback(gl.TRANSFORM_FEEDBACK, null);
        gl.disable(gl.RASTERIZER_DISCARD);

        // Pass 1 - Operation 2: Update particle pool
        gl.bindBuffer(gl.TRANSFORM_FEEDBACK_BUFFER, this.xfLifeBuffer);
        gl.bindBuffer(gl.COPY_WRITE_BUFFER, this.lifeBuffer);
        gl.copyBufferSubData(gl.TRANSFORM_FEEDBACK_BUFFER, gl.COPY_WRITE_BUFFER, 0, 0, this.streamline * 1 * 4);

        gl.bindBuffer(gl.TRANSFORM_FEEDBACK_BUFFER, this.xfSimulationBuffer);
        gl.bindBuffer(gl.COPY_WRITE_BUFFER, this.simulationBuffer);
        gl.copyBufferSubData(gl.TRANSFORM_FEEDBACK_BUFFER, gl.COPY_WRITE_BUFFER, 0, 0, this.streamline * 3 * 4);
        gl.getBufferSubData(gl.TRANSFORM_FEEDBACK_BUFFER, 0, this.particleMapBuffer!, 0, this.streamline * 3);
        gl.bindBuffer(gl.TRANSFORM_FEEDBACK_BUFFER, null);
        // console.log(this.particleMapBuffer);

        gl.bindTexture(gl.TEXTURE_2D, this.poolTextureBuffer);
        gl.pixelStorei(gl.UNPACK_ALIGNMENT, 1);
        // console.log(this.beginBlock);
        gl.texSubImage2D(gl.TEXTURE_2D, 0, this.textureOffsetArray[this.beginBlock].offsetX, this.textureOffsetArray[this.beginBlock].offsetY, this.maxBlockSize, this.maxBlockSize, gl.RGB, gl.FLOAT, this.particleMapBuffer);

        // Pass 2 - Operation 1: Rendering
        gl.viewport(0, 0, gl.canvas.width, gl.canvas.height);
        gl.enable(gl.BLEND);
        gl.blendFunc(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA);

        gl.bindVertexArray(this.renderVAO);
        this.drawShader.use(gl);
        gl.activeTexture(gl.TEXTURE0);
        gl.bindTexture(gl.TEXTURE_2D, this.poolTextureBuffer);
        this.drawShader.setInt(gl, "particlePool", 0);
        this.drawShader.setInt(gl, "blockNum", this.flowLimits["constraints"]["max_segment_num"]);
        this.drawShader.setInt(gl, "beginBlock", this.beginBlock);
        this.drawShader.setInt(gl, "blockSize", this.maxBlockSize);
        this.drawShader.setFloat(gl, "fillWidth", this.flowParams.fillWidth);
        this.drawShader.setFloat(gl, "aaWidth", this.flowParams.aaWidth);
        this.drawShader.setFloat2(gl, "viewport", gl.canvas.width, gl.canvas.height);
        this.drawShader.setUniformBlock(gl, "FlowFieldUniforms", 0);
        gl.drawArraysInstanced(gl.TRIANGLE_STRIP, 0, (this.segmentNum - 1) * 2, this.streamline);

        gl.disable(gl.BLEND);

        gl.bindVertexArray(null);
        gl.bindTexture(gl.TEXTURE_2D, null);

        // Debug
        // Show particle pool
        if (this.flowParams.content == "particle pool") {

            gl.enable(gl.BLEND);
            gl.blendFunc(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA);
            this.poolShader.use(gl);
            this.poolShader.setFloat2(gl, "viewport", window.innerWidth, window.innerHeight);
            this.poolShader.setInt(gl, "textureBuffer", 0);
            gl.activeTexture(gl.TEXTURE0);
            gl.bindTexture(gl.TEXTURE_2D, this.poolTextureBuffer);
            gl.drawArraysInstanced(gl.TRIANGLE_STRIP, 0, 4, 1);
            gl.disable(gl.BLEND);
        }
        // Show flow fields
        if (this.flowParams.content == "flow field") {

            this.textureShader.use(gl);
            gl.activeTexture(gl.TEXTURE0);
            gl.bindTexture(gl.TEXTURE_2D, this.ffTextureInfo[0]);
            gl.activeTexture(gl.TEXTURE1);
            gl.bindTexture(gl.TEXTURE_2D, this.ffTextureInfo[1]);
            this.textureShader.setInt(gl, "texture1", 0);
            this.textureShader.setInt(gl, "texture2", 1);
            this.textureShader.setFloat(gl, "progress", this.getProgressBetweenTexture(this.beginBlock));
            this.textureShader.setFloat2(gl, "viewport", window.innerWidth, window.innerHeight);
            gl.drawArraysInstanced(gl.TRIANGLE_STRIP, 0, 4, 1);
        }
    }
}