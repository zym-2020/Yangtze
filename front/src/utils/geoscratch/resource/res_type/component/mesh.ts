import { TransformRes } from  '../../../core/math/transform';

export interface SubMeshRes {
    meshData: string,
    material: string,
    transform: TransformRes
}

export interface MeshComponentRes {
    subMeshes: Array<SubMeshRes>
}
