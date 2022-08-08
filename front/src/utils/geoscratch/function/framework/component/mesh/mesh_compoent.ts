import { sceneManager } from './../../../scene/scene_manager';
import { Matrix4x4 } from '../../../../core/math/matrix4';
import { TransformComponent } from './../transform/transform_component';
import { Transform } from '../../../../core/math/transform';
import { MaterialRes } from './../../../../resource/res_type/data/material';
import { assetManager } from './../../../../resource/asset_manager';
import { SpatialObject } from './../../object/object';
import { SpatialObjectComponentDesc, SpatialObjectDesc } from '../../../scene/scene_object';
import { MeshComponentRes } from "../../../../../geoscratch/resource/res_type/component/mesh";
import { Component } from '../component';

export class MeshComponent extends Component {
    rawMeshes: Array<SpatialObjectComponentDesc>;

    constructor(meshComponentRes: MeshComponentRes, parentObject: SpatialObject) {
        super(parentObject);
        this.rawMeshes = [];
    }

    public static async load(meshComponentRes: MeshComponentRes, parentObject: SpatialObject) {
        const mesh = new MeshComponent(meshComponentRes, parentObject);

        for (const subMeshRes of meshComponentRes.subMeshes) {   
            const subMesh= new SpatialObjectComponentDesc();
            
            subMesh.meshDesc.meshUrl = subMeshRes.meshData;
            subMesh.materialDesc.withMaterial = subMeshRes.material as unknown as boolean;

            if (subMesh.materialDesc.withMaterial) {
                const materialRes = await assetManager.loadAsset<MaterialRes>(subMeshRes.material);

                subMesh.materialDesc.baseColorTextureUrl = materialRes.baseColorTextureUrl;
                subMesh.materialDesc.metallicRoughnessTextureUrl = materialRes.metallicRoughnessTextureUrl;
                subMesh.materialDesc.normalTextureUrl = materialRes.normalTextureUrl;
                subMesh.materialDesc.occlusionTextureUrl = materialRes.occlusionTextureUrl;
                subMesh.materialDesc.emissiveTextureUrl = materialRes.emissiveTextureUrl;

                // TODO: need an independent shaderDesc!
                subMesh.materialDesc.shaderName = materialRes.shaderName;
                subMesh.materialDesc.vertSourceUrl = materialRes.vertSourceUrl;
                subMesh.materialDesc.fragSourceUrl = materialRes.fragSourceUrl;
            }

            subMesh.transformDesc.transformMatrix = Transform.setFromRes(subMeshRes.transform).getMatrix();
            mesh.rawMeshes.push(subMesh);
        }

        return mesh;
    }

    getRawMeshes() {
        return this.rawMeshes;
    }

    tick() {
        // Start Dash!
        const transformComponent = this.parentObject.tryGetComponent<TransformComponent>("TransformComponent");
        if (!transformComponent) return;

        const meshComponents: Array<SpatialObjectComponentDesc> = [];
        for (const meshComponent of this.rawMeshes) {
            const newMeshComDesc = new SpatialObjectComponentDesc();
            const objectTranformMatrix = Matrix4x4.setFromF32ArrayBuffer(meshComponent.transformDesc.transformMatrix);

            newMeshComDesc.meshDesc = meshComponent.meshDesc;
            newMeshComDesc.materialDesc = meshComponent.materialDesc;
            newMeshComDesc.transformDesc.transformMatrix = Matrix4x4.setFromF32ArrayBuffer(transformComponent.getMatrix()).multiply(objectTranformMatrix).getF32ArrayBuffer();
            
            meshComponents.push(newMeshComDesc);
        }
        sceneManager.addSceneObject(new SpatialObjectDesc(this.parentObject.getID(), meshComponents));
    }
    
    destroy() {
        // May be used...
    }
}