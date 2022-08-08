/* eslint-disable no-constant-condition */
import { SCurrentCameraType } from './../render/render_camera';
import { Matrix4x4 } from '../../core/math/matrix4';
import { FrameBuffer } from './../render/framebuffer';
import { SceneBuffers, SceneMemory, SceneTexture, PixelFormat, TextureType, SceneShader } from './../render/render_data';
import { MeshData, Vertex } from './../../resource/res_type/data/mesh_data';
import { assetManager } from '../../resource/asset_manager';
import { invalid_so_id } from './../framework/scene/scene';
import { Vector3 } from '../../core/math/vector3';
import { RenderScene } from './../render/render_scene';
import { Vector2 } from './../../core/math/vector2';
import { SceneAllocator } from './scene_allocator';
import { ComponentID, SpatialObjectTransformDesc, SpatialObjectDesc } from './scene_object';
import { Mutex } from './../../core/lock/mutex';
import { MaterialHandle, RenderMaterial } from './../render/render_material';
import { VertexBufferHandle, IndexBufferHandle, TextureHandle, ShaderHandle, SceneResourceHandle } from './../render/resource_handle';
import { MeshHandle, RenderMesh, PosNormalTangentTexVertex } from './../render/render_mesh';
import axios from "axios";

export const invalid_handle = 0;

export class SceneBuilder {
    constructor() {
        // this is just a tool class
    }

    static async loadMeshFromJSON(url: string, minBounds: Vector3, maxBounds: Vector3) {
        const ret = new MeshHandle();
        const meshData = await assetManager.loadAsset<MeshData>(url);

        const vertexPos: Array<number> = [];
        const vertexNormal: Array<number> = [];
        const vertexTangent: Array<number> = [];
        const vertexTexcoord: Array<number> = [];
        const vertexIndex: Array<number> = [];
        const meshVertices: Array<PosNormalTangentTexVertex> = [];
        for (const vertex of meshData.vertexBuffer) {
            const renderVertex: PosNormalTangentTexVertex = {
                x: vertex.px,
                y: vertex.py,
                z: vertex.pz,
                nx: vertex.nx,
                ny: vertex.ny,
                nz: vertex.nz,
                tx: vertex.tx,
                ty: vertex.ty,
                tz: vertex.tz,
                u: vertex.u,
                v: vertex.v
            }
            vertexPos.push(vertex.px);
            vertexPos.push(vertex.py);
            vertexPos.push(vertex.pz);
            vertexNormal.push(vertex.nx);
            vertexNormal.push(vertex.ny);
            vertexNormal.push(vertex.nz);
            vertexTangent.push(vertex.tx);
            vertexTangent.push(vertex.ty);
            vertexTangent.push(vertex.tz);
            vertexTexcoord.push(vertex.u);
            vertexTexcoord.push(vertex.v);

            meshVertices.push(renderVertex);

            minBounds.makeFloor(new Vector3(renderVertex.x, renderVertex.y, renderVertex.z));
            maxBounds.makeCeil(new Vector3(renderVertex.x, renderVertex.y, renderVertex.z));
            ret.boundingBox.merge(new Vector3(renderVertex.x, renderVertex.y, renderVertex.z))
        }

        for (const index of meshData.indexBuffer)
            vertexIndex.push(index);

        const vertexPosMem = SceneBuffers.alloc(vertexPos.length * 4, (new Float32Array(vertexPos)).buffer);
        const vertexNromalMem = SceneBuffers.alloc(vertexNormal.length * 4, (new Float32Array(vertexNormal)).buffer);
        const vertexTangentMem = SceneBuffers.alloc(vertexTangent.length * 4, (new Float32Array(vertexTangent)).buffer);
        const vertexTexcoordMem = SceneBuffers.alloc(vertexTexcoord.length * 4, (new Float32Array(vertexTexcoord)).buffer);
        const vertexIndexMem = SceneBuffers.alloc(vertexIndex.length * 4, Uint16Array.from(vertexIndex));

        const vertexPosBufferHandle = SceneBuffers.createVertexBuffer(vertexPosMem);
        const vertexNormalBufferHandle = SceneBuffers.createVertexBuffer(vertexNromalMem);
        const vertexTangentBufferHandle = SceneBuffers.createVertexBuffer(vertexTangentMem);
        const vertexTexcoordBufferHandle = SceneBuffers.createVertexBuffer(vertexTexcoordMem);
        const vertexIndexbufferHandle = SceneBuffers.createVertexBuffer(vertexIndexMem);

        ret.vertexPosHandle = vertexPosBufferHandle;
        ret.vertexNormalHandle = vertexNormalBufferHandle;
        ret.vertexTangentHandle = vertexTangentBufferHandle;
        ret.vertexTexcoordHandle = vertexTexcoordBufferHandle;
        ret.indexHandle = vertexIndexbufferHandle;

        return {ret, minBounds, maxBounds};
    }

    static async loadTexture(url: string) {
        const imageInfo = new Promise((resolve, reject) => {
            if (url !== "") {
                const image = new Image();

                image.onload = () => {
                    const textureData: SceneTexture = {
                        textureElement: image
                    }

                    resolve(textureData);
                }
                image.src = url;
            } else {
                const textureData: SceneTexture = {
                    textureElement: null
                }
                resolve(textureData);
            }
        }).then((res) => {
            return res as unknown as TextureHandle;
        });

        return imageInfo;
    }

    static async loadShader(name: string, vertexUrl: string, fragmentUrl: string) {
        const vertexCode = await axios.get(vertexUrl)
        .then((response) => {
            return response.data;
        });
        const fragmentCode = await axios.get(fragmentUrl)
        .then((response) => {
            return response.data;
        });

        const shader: SceneShader = {
            name: name,
            vertCode: vertexCode,
            fragCode: fragmentCode
        }

        return shader as unknown as ShaderHandle;
    }
}

export class SceneManager {
    scene: RenderScene;
    meshID_soID_map: Map<number, number>;

    private soDescs: Array<SpatialObjectDesc>;

    private mesh_handle_map: Map<string, MeshHandle>;
    private handle_mesh_map: Map<MeshHandle, string>;

    private vertex_handle_map: Map<string, VertexBufferHandle>;
    private handle_vertex_map: Map<VertexBufferHandle, string>;

    private index_handle_map: Map<string, IndexBufferHandle>;
    private handle_index_map: Map<IndexBufferHandle, string>;

    private texture_handle_map: Map<string, TextureHandle>;
    private handle_texture_map: Map<TextureHandle, string>;

    private shader_handle_map: Map<string, ShaderHandle>;
    private handle_shader_map: Map<ShaderHandle, string>;

    private maxMeshHandleCount = 1000;
    private releaseMeshHandleCount = 100;
    private maxMaterialHandleCount = 1000;
    private releaseMaterialHandleCount = 100;
    
    private meshHandlesToRelease: Array<MeshHandle>;
    private materialHandlesToRelease: Array<MaterialHandle>;
    private releaseHandleMutex: Mutex;
    private pickObjectMutex: Mutex;

    private componentTransformMap: Map<ComponentID, SpatialObjectTransformDesc>;

    private meshAllocator: SceneAllocator<RenderMesh>;
    private materialAllocator: SceneAllocator<RenderMaterial>;
    private instanceIDAllocator: SceneAllocator<ComponentID>;

    private windowSize: Vector2;

    constructor() {
        this.scene = new RenderScene();
        this.meshID_soID_map = new Map();
        this.soDescs = [];

        this.mesh_handle_map = new Map();
        this.handle_mesh_map = new Map();

        this.vertex_handle_map = new Map();
        this.handle_vertex_map = new Map();

        this.index_handle_map = new Map();
        this.handle_index_map = new Map();

        this.texture_handle_map = new Map();
        this.handle_texture_map = new Map();

        this.shader_handle_map = new Map();
        this.handle_shader_map = new Map();

        this.meshHandlesToRelease = [];
        this.materialHandlesToRelease = [];
        this.releaseHandleMutex = new Mutex();
        this.pickObjectMutex = new Mutex();

        this.componentTransformMap = new Map();

        this.meshAllocator = new SceneAllocator<RenderMesh>();
        this.materialAllocator = new SceneAllocator<RenderMaterial>();
        this.instanceIDAllocator = new SceneAllocator<ComponentID>();

        this.windowSize = new Vector2();
    }

    initialize() {
        this.setSceneOnce();
    }

    tick(frameBuffer: FrameBuffer) {
        // Editor Mode used, but this tick is not needed in Version0
    }

    memoryFromHandle(handle: SceneResourceHandle) {
        return SceneBuffers.memoryFromHandle(handle);
    }

    textureFromHandle(handle: TextureHandle) {
        return SceneBuffers.textureFromHandle(handle);
    }

    addSceneObject(soDesc: SpatialObjectDesc) {
        this.soDescs.push(soDesc);
    }

    setWindowSize(windowSize: Vector2) {
        this.windowSize = windowSize;
    }

    setSceneOnce() {
        if (this.scene && this.scene.loaded == false) {
            // TODO: need some default texture handles!
            // This will not be needed during Verison0.0

            this.scene.camera.lookAt(new Vector3(0.0, 0.0, 3.0), new Vector3(0.0, 0.0, 0.0), new Vector3(0.0, 1.0, 0.0))
            this.scene.camera.zFar = 1000.0;
            this.scene.camera.zNear = 0.1;
            this.scene.camera.setAspect(this.windowSize.x / this.windowSize.y);

            this.scene.loaded = true;
        }
    }

    async syncSceneObjects() {
        this.scene.lock();
        this.scene.clear();

        // TODO: require release handles function!

        this.pickObjectMutex.lock();
        this.instanceIDAllocator.clear();
        this.componentTransformMap.clear();
        this.meshID_soID_map.clear();

        let materialIndex= 0;
        // console.log(this.soDescs);
        for (const soDesc of this.soDescs) {
            for (let i = 0 ; i < soDesc.getComponents().length; i++) {
                const component =  soDesc.getComponents()[i];
                const soID = soDesc.getID();
                const componentIndex = i;

                const componentID = new ComponentID(soID, componentIndex);
                this.componentTransformMap.set(componentID, component.transformDesc);

                // Add mesh to scene
                const mesh = new RenderMesh();
                // console.log(component.transformDesc.transformMatrix);
                mesh.modelMatrix = Matrix4x4.setFromF32ArrayBuffer(component.transformDesc.transformMatrix);
                mesh.material = materialIndex++;
                mesh.instanceID = this.instanceIDAllocator.allocGuid(componentID);

                // load by json
                const meshHandle = await this.getOrCreateMeshHandle(component.meshDesc.meshUrl);
                mesh.vertexPosBuffer = meshHandle.vertexPosHandle;
                mesh.vertexNormalBuffer = meshHandle.vertexNormalHandle;
                mesh.vertexTangentBuffer = meshHandle.vertexTangentHandle;
                mesh.vertexTexcoordBuffer = meshHandle.vertexTexcoordHandle;
                mesh.indexBuffer = meshHandle.indexHandle;

                mesh.guid = this.meshAllocator.allocGuid(mesh);
                this.scene.addMesh(mesh.guid);
                this.meshID_soID_map.set(mesh.instanceID, soDesc.getID());
                
                // Add material to scene
                let textureFile0: string;
                let textureFile1: string;
                let textureFile2: string;
                let textureFile3: string;
                let textureFile4: string;
                let shaderName: string;
                let shaderVertSource: string;
                let shaderFragSource: string;

                if (component.materialDesc.withMaterial) {
                    textureFile0 = component.materialDesc.baseColorTextureUrl;
                    textureFile1 = component.materialDesc.metallicRoughnessTextureUrl;
                    textureFile2 = component.materialDesc.normalTextureUrl;
                    textureFile3 = component.materialDesc.occlusionTextureUrl;
                    textureFile4 = component.materialDesc.emissiveTextureUrl;

                    shaderName = component.materialDesc.shaderName;
                    shaderVertSource = component.materialDesc.vertSourceUrl;
                    shaderFragSource = component.materialDesc.fragSourceUrl;
                
                    // TODO: need an independent shaderDesc!

                }
                else {
                    // TODO: Need to add default texture
                    textureFile0 = "";
                    textureFile1 = "";
                    textureFile2 = "";
                    textureFile3 = "";
                    textureFile4 = "";

                    shaderName = "";
                    shaderVertSource = "";
                    shaderFragSource = "";
                }

                const texture0 = await this.getOrCreateImageHandle(textureFile0);
                const texture1 = await this.getOrCreateImageHandle(textureFile1);
                const texture2 = await this.getOrCreateImageHandle(textureFile2);
                const texture3 = await this.getOrCreateImageHandle(textureFile3);
                const texture4 = await this.getOrCreateImageHandle(textureFile4);
                const shader = await this.getOrCreateShaderHandle(shaderName, shaderVertSource, shaderFragSource);
                const material = new RenderMaterial();
                material.baseColorTexture = texture0;
                material.metallicRoughnessTexture = texture1;
                material.normalTexture = texture2;
                material.occlusionTexture = texture3;
                material.emissiveTexture = texture4;
                material.shader = shader;
                material.guid = this.materialAllocator.allocGuid(material);
                this.scene.addMaterial(material.guid);
            }
        }
        
        this.soDescs = [];
        this.pickObjectMutex.unlock();
        this.scene.unlock();
    }

    addReleaseMeshHandle(meshHandle: MeshHandle) {
        this.releaseHandleMutex.lock();
        let findIt = false;
        for (const handle of this.meshHandlesToRelease) {
            if (handle === meshHandle)
                findIt = true;
        }
        if (!findIt)
            this.meshHandlesToRelease.push(meshHandle);

        this.releaseHandleMutex.unlock();
    }

    addReleaseTextureHandle(materialHandle: MaterialHandle) {
        this.releaseHandleMutex.lock();
        let findIt = false;
        for (const handle of this.materialHandlesToRelease) {
            if (handle === materialHandle)
                findIt = true;
        }
        if (!findIt)
            this.materialHandlesToRelease.push(materialHandle);
        
        this.releaseHandleMutex.unlock();
    }

    async getOrCreateMeshHandle(url: string) {
        const findIt = this.mesh_handle_map.get(url);
        if (findIt)
            return findIt;

        const meshLoaded = await SceneBuilder.loadMeshFromJSON(url, this.scene.minBounds, this.scene.maxBounds);
        this.scene.minBounds = meshLoaded.minBounds;
        this.scene.maxBounds = meshLoaded.maxBounds;

        this.handle_mesh_map.set(meshLoaded.ret, url);
        this.mesh_handle_map.set(url, meshLoaded.ret);

        return meshLoaded.ret;
    }

    async getOrCreateImageHandle(url: string) {
        const findIt = this.texture_handle_map.get(url);
        if (findIt) 
            return findIt;
        
        const textureHandle = await SceneBuilder.loadTexture(url);

        this.handle_texture_map.set(textureHandle, url);
        this.texture_handle_map.set(url, textureHandle);

        return textureHandle;
    }

    async getOrCreateShaderHandle(name: string, vertSource: string, fragSource: string) {
        const findIt = this.shader_handle_map.get(name);
        if (findIt)
            return findIt;
        
        const shaderHandle = await SceneBuilder.loadShader(name, vertSource, fragSource);

        this.handle_shader_map.set(shaderHandle, name);
        this.shader_handle_map.set(name, shaderHandle);

        return shaderHandle;
    }

    getSObjectIDByMeshID(meshID: number) {
        const findIt = this.meshID_soID_map.get(meshID);
        if (findIt)
            return findIt;
        else
            return invalid_so_id;
    }

    getSelectedComponentByNodeID(nodeID: number) {
        this.pickObjectMutex.lock();

        const componentID = this.instanceIDAllocator.getGuidRelatedElement(nodeID)
        if (componentID) {
            //
        }
        this.pickObjectMutex.unlock();
        return componentID;
    }

    getTransformDescByComponentID(componentID: ComponentID) {
        this.pickObjectMutex.lock();

        const findTransform = this.componentTransformMap.get(componentID);
        if (findTransform) {
            this.pickObjectMutex.unlock();
            return findTransform.transformMatrix;
        }

        this.pickObjectMutex.unlock();
        return null;
    }

    getRenderMeshByMeshGuid(meshID: number) {
        return this.meshAllocator.getGuidRelatedElement(meshID);
    }

    getRenderMaterialByMaterialGuid(materialID: number) {
        return this.materialAllocator.getGuidRelatedElement(materialID);
    }

    setMainViewMatrix(viewMatrix: Matrix4x4, type: SCurrentCameraType) {
        if (this.scene && this.scene.camera) {
            this.scene.camera.setMainViewMatrix(viewMatrix, type);
        }
    }

    setMainProjMatrix(projMatrix: Matrix4x4, type: SCurrentCameraType) {
        if (this.scene && this.scene.camera) {
            this.scene.camera.setMainProjectMatrix(projMatrix, type);
        }
    }

    setFOV(fov: number) {
        if (this.scene && this.scene.camera) {
            this.scene.camera.setFovX(fov);
        }
    }

    getFOV() {
        if (this.scene && this.scene.camera) {
            return this.scene.camera.getFov();
        }

        return new Vector2();
    }

    releasemeshHandles() {
        this.releaseHandleMutex.lock();

        if (this.meshHandlesToRelease.length < this.maxMeshHandleCount)
            return;
        
        let releaseCount = 0;
        while (true) {
            if (this.meshHandlesToRelease.length === 0)
                break;
            const meshHandle = this.meshHandlesToRelease.shift();
            if (!meshHandle) break;

            const mesh = new RenderMesh();
            mesh.vertexPosBuffer = meshHandle.vertexPosHandle;
            mesh.vertexNormalBuffer = meshHandle.vertexNormalHandle;
            mesh.vertexTangentBuffer = meshHandle.vertexTangentHandle;
            mesh.vertexTexcoordBuffer = meshHandle.vertexTexcoordHandle;
            mesh.indexBuffer = meshHandle.indexHandle;
            this.meshAllocator.freeElement(mesh);

            const findMeshFile = this.handle_mesh_map.get(meshHandle);
            if (findMeshFile) {
                this.mesh_handle_map.delete(findMeshFile);
                this.handle_mesh_map.delete(meshHandle);
            }
            SceneBuffers.destroy(meshHandle.vertexPosHandle);
            SceneBuffers.destroy(meshHandle.vertexNormalHandle);
            SceneBuffers.destroy(meshHandle.vertexTangentHandle);
            SceneBuffers.destroy(meshHandle.vertexTexcoordHandle);
            SceneBuffers.destroy(meshHandle.indexHandle);

            if (++releaseCount >= this.releaseMeshHandleCount)
                break;
        }

        this.releaseHandleMutex.unlock();
    }

    releaseMatrialHandles() {
        this.releaseHandleMutex.lock();

        if (this.materialHandlesToRelease.length < this.maxMaterialHandleCount)
            return;
        
        let releaseCount = 0;
        while (true) {
            if (this.materialHandlesToRelease.length === 0)
                break;
            const materialHandle = this.materialHandlesToRelease.shift();
            if (!materialHandle)
                break;
            
            const material = new RenderMaterial();
            material.baseColorTexture = materialHandle.imageHandle0;
            material.metallicRoughnessTexture = materialHandle.imageHandle1;
            material.normalTexture = materialHandle.imageHandle2;
            material.occlusionTexture = materialHandle.imageHandle3;
            material.emissiveTexture = materialHandle.imageHandle4;
            this.materialAllocator.freeElement(material);

            this.releaseTextureHandle(materialHandle.imageHandle0);
            this.releaseTextureHandle(materialHandle.imageHandle1);
            this.releaseTextureHandle(materialHandle.imageHandle2);
            this.releaseTextureHandle(materialHandle.imageHandle3);
            this.releaseTextureHandle(materialHandle.imageHandle4);

            if (++releaseCount >= this.releaseMeshHandleCount)
                break;
        }

        this.releaseHandleMutex.unlock();
    }

    releaseTextureHandle(textureHandle: TextureHandle) {
        const findIt = this.handle_texture_map.get(textureHandle);
        if (findIt) {
            this.texture_handle_map.delete(findIt);
            this.handle_texture_map.delete(textureHandle);

            SceneBuffers.destroy(textureHandle);
        }
    }
}

export const sceneManager = new SceneManager();
