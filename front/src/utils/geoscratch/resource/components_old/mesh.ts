import { Transform  } from "../../core/math/transform";


export class SubMeshRes {
    fileRef: string;
    transform: Transform;
    materialRef: string;

    constructor(fileRef?: string, transform?: Transform, materialRef?: string) {
        this.fileRef = fileRef || "";
        this.transform = transform || new Transform();
        this.materialRef = materialRef || "";
    }
}

export class MeshComponentRes {
    subMeshes: Array<SubMeshRes>;

    constructor(subMeshes: Array<SubMeshRes>) {
        this.subMeshes = subMeshes;
    }
}