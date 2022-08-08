import { RenderMesh } from './render_mesh';
import { Vector3 } from '../../core/math/vector3';
import { Vector4 } from './../../core/math/vector4';
import { TextureHandle, ShaderHandle } from './resource_handle';

export class MaterialHandle {
    imageHandle0: TextureHandle;
    imageHandle1: TextureHandle;
    imageHandle2: TextureHandle;
    imageHandle3: TextureHandle;
    imageHandle4: TextureHandle;

    constructor() {
        this.imageHandle0 = new TextureHandle();
        this.imageHandle1 = new TextureHandle();
        this.imageHandle2 = new TextureHandle();
        this.imageHandle3 = new TextureHandle();
        this.imageHandle4 = new TextureHandle();
    }

    getHashValue() {
        return (((this.imageHandle0.getHashValue() ^ (this.imageHandle1.getHashValue() << 1)) ^
        (this.imageHandle2.getHashValue() << 1)) ^
       (this.imageHandle3.getHashValue() << 1)) ^
      (this.imageHandle4.getHashValue() << 1);
    }
}

export class RenderMaterial{
    blend: boolean;
    doubleSided: boolean;

    baseColorTexture: TextureHandle;
    baseColorFactor: Vector4;

    metallicRoughnessTexture: TextureHandle; // blue = metallic, green = roughness
    metallicFactor: number;
    roughnessFactor: number;

    normalTexture: TextureHandle;
    normalScale: number;

    occlusionTexture: TextureHandle;
    occlusionStrength: number;

    emissiveTexture: TextureHandle;
    emissiveFactor: Vector3;

    shader: ShaderHandle;

    guid: number;

    constructor() {
        this.blend = false;
        this.doubleSided = false;

        this.baseColorTexture = new TextureHandle();
        this.baseColorFactor = new Vector4(1.0);

        this.metallicRoughnessTexture = new TextureHandle();
        this.metallicFactor = 1.0;
        this.roughnessFactor = 1.0;

        this.normalTexture = new TextureHandle();
        this.normalScale = 1.0;

        this.occlusionTexture = new TextureHandle();
        this.occlusionStrength =1.0;

        this.emissiveTexture = new TextureHandle();
        this.emissiveFactor = new Vector3();

        this.shader = new ShaderHandle();

        this.guid = 0;
    }

    getHashValue(){
        return (((this.baseColorTexture.getHashValue() ^ (this.metallicRoughnessTexture.getHashValue() << 1)) ^
                 (this.normalTexture.getHashValue() << 1)) ^
                (this.occlusionTexture.getHashValue() << 1)) ^
               (this.emissiveTexture.getHashValue() << 1);
    }
}

export class MeshMaterialPack {
    mesh: RenderMesh;
    material: RenderMaterial;

    constructor(mesh?: RenderMesh, material?: RenderMaterial) {
        this.mesh = mesh || new RenderMesh();
        this.material = material || new RenderMaterial();
    }

    getHashValue() {
        return this.mesh.getHashValue() ^ (this.material.getHashValue() << 1);
    }
}
