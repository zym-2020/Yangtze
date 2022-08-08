import { Vector3 } from '../../core/math/vector3';
import { Transform } from '../../core/math/transform';
import { Matrix4x4 } from '../../core/math/matrix4';
import { Shader } from '../../core/shader/shader';
import { sceneManager } from './../scene/scene_manager';
import { isPowerOf2 } from '../../resource/data/texture';
import { SceneMemory, SceneTexture, SceneShader } from './render_data';
import { RenderScene } from './render_scene';


export class Render {
    VAO: WebGLVertexArrayObject | null = null;
    VBO: WebGLBuffer | null = null;
    EBO: WebGLBuffer | null = null;
    textures_pixels_ID_map: Map<HTMLImageElement, WebGLTexture> = new Map(); 
    shader_name_program_map: Map<string, Shader> = new Map();
    shader: Shader | null = null;
    drawingContext: WebGL2RenderingContext;

    radius = 0.0;

    constructor(canvas: HTMLCanvasElement) {
        const drawingContext = canvas.getContext("webgl2");
        if (drawingContext === null)
            console.log("ERROR: This canvas does not support WebGL2!")

        this.drawingContext = drawingContext as WebGL2RenderingContext;
    }

    createDataBuffer() {
        // PosBuffer:       9MB = 9,437,184 Byte,
        // NormalBuffer:    9MB,
        // TangentBuffer:   9MB,
        // TexcoordBuffer:  6MB = 6,291,456 Byte
        this.VAO = this.drawingContext.createVertexArray();
        this.drawingContext.bindVertexArray(this.VAO);

        this.VBO = this.drawingContext.createBuffer();
        this.drawingContext.bindBuffer(this.drawingContext.ARRAY_BUFFER, this.VBO);
        this.drawingContext.bufferData(this.drawingContext.ARRAY_BUFFER, 9437184 * 3 + 6291456, this.drawingContext.DYNAMIC_DRAW);

        this.EBO = this.drawingContext.createBuffer();
        this.drawingContext.bindBuffer(this.drawingContext.ELEMENT_ARRAY_BUFFER, this.EBO);
        this.drawingContext.bufferData(this.drawingContext.ELEMENT_ARRAY_BUFFER, 9437184, this.drawingContext.DYNAMIC_DRAW);
        
        this.drawingContext.bindBuffer(this.drawingContext.ARRAY_BUFFER, null);
        this.drawingContext.bindVertexArray(null);
    }

    initialize() {
        this.createDataBuffer();
        this.setMesh(sceneManager.scene);
        this.setMaterial(sceneManager.scene);
    }

    tick() {

        resizeCanvasToDisplaySize(this.drawingContext.canvas, window.devicePixelRatio);

        this.drawingContext.viewport(0, 0, this.drawingContext.canvas.width, this.drawingContext.canvas.height);

        this.drawingContext.enable(this.drawingContext.DEPTH_TEST);
        this.drawingContext.enable(this.drawingContext.CULL_FACE);

        const grey = 58.0 / 255.0;
        this.drawingContext.clearColor(grey, grey, grey, 1.0);
        this.drawingContext.clear(this.drawingContext.COLOR_BUFFER_BIT | this.drawingContext.DEPTH_BUFFER_BIT);
        
        // this.useShader("CubeShader");
        this.drawingContext.bindVertexArray(this.VAO);
        this.drawingContext.bindBuffer(this.drawingContext.ELEMENT_ARRAY_BUFFER, this.EBO);


        let verticesOffset = 0;
        const meshesToDraw = sceneManager.scene.getMeshes()
        const materialsToUse = sceneManager.scene.getMaterials()
        
        for (let i = 0; i < meshesToDraw.length; i++) {
            const mesh = sceneManager.getRenderMeshByMeshGuid(meshesToDraw[i]);
            const material = sceneManager.getRenderMaterialByMaterialGuid(materialsToUse[i]);
            const shader = (material?.shader as unknown as SceneShader).name;
            this.useShader(shader);

            let textureIndex = 0;
            this.useTexture((material!.baseColorTexture as unknown as SceneTexture).textureElement, textureIndex++);
            this.useTexture((material!.metallicRoughnessTexture as unknown as SceneTexture).textureElement, textureIndex++);
            this.useTexture((material!.normalTexture as unknown as SceneTexture).textureElement, textureIndex++);
            this.useTexture((material!.occlusionTexture as unknown as SceneTexture).textureElement, textureIndex++);
            this.useTexture((material!.emissiveTexture as unknown as SceneTexture).textureElement, textureIndex);

            const model = (mesh!.modelMatrix).copy().multiply((new Matrix4x4()).rotateX(this.radius * i).rotateY(this.radius * i).rotateZ(this.radius * i));
            // const model = (mesh!.modelMatrix).copy().rotateX(this.radius * i).rotateY(this.radius * i).rotateZ(this.radius * i);
            const view = sceneManager.scene.camera.viewMatrices[0].getF32ArrayBuffer();
            const project = sceneManager.scene.camera.getPersProjMatrix();

            this.shader?.setMat4(this.drawingContext, "u_model", model.getF32ArrayBuffer());
            this.shader?.setMat4(this.drawingContext, "u_view", view);
            this.shader?.setMat4(this.drawingContext, "u_projection", project!);

            const verticesNum = (new Float32Array((mesh!.indexBuffer as unknown as SceneMemory).data!)).length;
            this.drawingContext.drawElements(this.drawingContext.TRIANGLES, verticesNum, this.drawingContext.UNSIGNED_SHORT, verticesOffset);
            verticesOffset += verticesNum;
        }

        this.radius += 0.001;
    }

    setMesh(scene: RenderScene) {
        this.drawingContext.bindVertexArray(this.VAO);
        this.drawingContext.bindBuffer(this.drawingContext.ARRAY_BUFFER, this.VBO);

        const meshes = scene.getMeshes();
        let mesh3Offset = 0;
        let mesh2Offset = 0;
        let meshIOffset = 0;
        for (const meshID of meshes) {
            const mesh = sceneManager.getRenderMeshByMeshGuid(meshID);
            if (!mesh) return;

            this.drawingContext.bufferSubData(this.drawingContext.ARRAY_BUFFER, 9437184 * 0 + mesh3Offset, new Float32Array((mesh.vertexPosBuffer as unknown as SceneMemory).data!));
            this.drawingContext.bufferSubData(this.drawingContext.ARRAY_BUFFER, 9437184 * 1 + mesh3Offset, new Float32Array((mesh.vertexNormalBuffer as unknown as SceneMemory).data!));
            this.drawingContext.bufferSubData(this.drawingContext.ARRAY_BUFFER, 9437184 * 2 + mesh3Offset, new Float32Array((mesh.vertexTangentBuffer as unknown as SceneMemory).data!));
            this.drawingContext.bufferSubData(this.drawingContext.ARRAY_BUFFER, 9437184 * 3 + mesh2Offset, new Float32Array((mesh.vertexTexcoordBuffer as unknown as SceneMemory).data!));

            this.drawingContext.vertexAttribPointer(0, 3, this.drawingContext.FLOAT, false, 3 * 4, 9437184 * 0);
            this.drawingContext.enableVertexAttribArray(0);
            this.drawingContext.vertexAttribPointer(1, 3, this.drawingContext.FLOAT, false, 3 * 4, 9437184 * 1);
            this.drawingContext.enableVertexAttribArray(1);
            this.drawingContext.vertexAttribPointer(2, 3, this.drawingContext.FLOAT, false, 3 * 4, 9437184 * 2);
            this.drawingContext.enableVertexAttribArray(2);
            this.drawingContext.vertexAttribPointer(3, 2, this.drawingContext.FLOAT, false, 2 * 4, 9437184 * 3);
            this.drawingContext.enableVertexAttribArray(3);

            this.drawingContext.bindBuffer(this.drawingContext.ELEMENT_ARRAY_BUFFER, this.EBO);
            this.drawingContext.bufferSubData(this.drawingContext.ELEMENT_ARRAY_BUFFER, meshIOffset, new Uint16Array((mesh.indexBuffer as unknown as SceneMemory).data!));

            mesh3Offset += (mesh.vertexPosBuffer as unknown as SceneMemory).data!.byteLength;
            mesh2Offset += (mesh.vertexTexcoordBuffer as unknown as SceneMemory).data!.byteLength;
            meshIOffset += (mesh.indexBuffer as unknown as SceneMemory).data!.byteLength;
        }
        this.drawingContext.bindBuffer(this.drawingContext.ARRAY_BUFFER, null);
        this.drawingContext.bindVertexArray(null);
    }

    setMaterial(scene: RenderScene) {
        const materials = scene.getMaterials();
        for (const materialID of materials) {

            const material = sceneManager.getRenderMaterialByMaterialGuid(materialID);
            if (!material) return;
            const image0 = (material.baseColorTexture as unknown as SceneTexture).textureElement; this.setTexture(image0);
            const image1 = (material.metallicRoughnessTexture as unknown as SceneTexture).textureElement; this.setTexture(image1);
            const image2 = (material.normalTexture as unknown as SceneTexture).textureElement; this.setTexture(image2);
            const image3 = (material.occlusionTexture as unknown as SceneTexture).textureElement; this.setTexture(image3);
            const image4 = (material.emissiveTexture as unknown as SceneTexture).textureElement; this.setTexture(image4);

            const shaderName = (material.shader as unknown as SceneShader).name;
            const vertCode = (material.shader as unknown as SceneShader).vertCode;
            const fragCode = (material.shader as unknown as SceneShader).fragCode;
            this.setShaderProgram(shaderName, vertCode, fragCode);
        }
    }

    setShaderProgram(name: string, vertCode: string, fragCode: string) {
        const findIt = this.shader_name_program_map.get(name);
        if (findIt) return findIt;

        const shader = new Shader(this.drawingContext, vertCode, fragCode, name);
        this.shader_name_program_map.set(name, shader);
    }

    setTexture(image: HTMLImageElement | null) {
        if (!image) return;
        const findIt = this.textures_pixels_ID_map.get(image);
        if (findIt) return findIt;
        const textureID = this.drawingContext.createTexture();
        if (!textureID) return;

        this.drawingContext.bindTexture(this.drawingContext.TEXTURE_2D, textureID);
        this.drawingContext.texImage2D(this.drawingContext.TEXTURE_2D, 0, this.drawingContext.RGBA, this.drawingContext.RGBA, this.drawingContext.UNSIGNED_BYTE, image);

        if (isPowerOf2(image.width) && isPowerOf2(image.height)) {
            this.drawingContext.generateMipmap(this.drawingContext.TEXTURE_2D);
        } else {
            this.drawingContext.texParameteri(this.drawingContext.TEXTURE_2D, this.drawingContext.TEXTURE_WRAP_S, this.drawingContext.CLAMP_TO_EDGE);
            this.drawingContext.texParameteri(this.drawingContext.TEXTURE_2D, this.drawingContext.TEXTURE_WRAP_T, this.drawingContext.CLAMP_TO_EDGE);
            this.drawingContext.texParameteri(this.drawingContext.TEXTURE_2D, this.drawingContext.TEXTURE_MIN_FILTER, this.drawingContext.LINEAR);
            this.drawingContext.texParameteri(this.drawingContext.TEXTURE_2D, this.drawingContext.TEXTURE_MAG_FILTER, this.drawingContext.LINEAR);
        }

        this.textures_pixels_ID_map.set(image, textureID);
    }

    useShader(shaderName: string) {
        const shader = this.shader_name_program_map.get(shaderName);
        if (!shader) return;
        this.shader = shader;
        shader.use(this.drawingContext);
    }

    unuseShader() {
        this.drawingContext.useProgram(null);
    }

    useTexture(image: HTMLImageElement | null, index = 0) {
        if (!image) return;
        const findIt = this.textures_pixels_ID_map.get(image);
        if (!findIt) return;
        
        this.drawingContext.activeTexture(this.drawingContext.TEXTURE0 + index);
        this.drawingContext.bindTexture(this.drawingContext.TEXTURE_2D, findIt);
        this.shader?.setInt(this.drawingContext, "texture" + index, index);
    }

}

function resizeCanvasToDisplaySize(canvas: HTMLCanvasElement, multiplier: number): boolean {
    multiplier = multiplier || 1;
    const width  = canvas.clientWidth  * multiplier | 0;
    const height = canvas.clientHeight * multiplier | 0;
    if (canvas.width !== width ||  canvas.height !== height) {
    canvas.width  = width;
    canvas.height = height;
    return true;
    }
    return false;
}