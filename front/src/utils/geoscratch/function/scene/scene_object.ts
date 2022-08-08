import { Matrix4x4 } from "../../core/math/matrix4"

export class SpatialObjectMeshDesc {
    constructor() {
        this.meshUrl = "";
    }

    meshUrl: string;
}

export class SpatialObjectMaterialDesc {
    constructor() {
        this.baseColorTextureUrl = "";
        this.metallicRoughnessTextureUrl = "";
        this.normalTextureUrl = "";
        this.occlusionTextureUrl = "";
        this.emissiveTextureUrl = "";
        this.withMaterial = false;

        this.shaderName = "";
        this.vertSourceUrl = "";
        this.fragSourceUrl = "";
        this.withShader = false;
    }

    baseColorTextureUrl: string;
    metallicRoughnessTextureUrl: string;
    normalTextureUrl: string;
    occlusionTextureUrl: string;
    emissiveTextureUrl: string;
    withMaterial: boolean;

    shaderName: string;
    vertSourceUrl: string;
    fragSourceUrl: string;
    withShader: boolean;
}

export class SpatialObjectTransformDesc {
    transformMatrix: Float32Array;

    constructor() {
        this.transformMatrix = new Float32Array(16);
    }
}

export class SpatialObjectComponentDesc {
    constructor() {
        this.meshDesc = new SpatialObjectMeshDesc();
        this.materialDesc = new SpatialObjectMaterialDesc();
        this.transformDesc = new SpatialObjectTransformDesc();
    }

    meshDesc: SpatialObjectMeshDesc;
    materialDesc: SpatialObjectMaterialDesc;
    transformDesc: SpatialObjectTransformDesc;
}

const invalid_so_id = Number.MAX_SAFE_INTEGER;
const invalid_component_id = Number.MAX_SAFE_INTEGER;

export class ComponentID {
    soID: number;
    componentID: number;

    constructor(soID?: number, componentID?: number) {
        this.soID = soID || invalid_so_id;
        this.componentID = componentID || invalid_component_id;
    }

    getHashValue() {
        return this.soID ^ (this.componentID << 1);
    }

    isValid() {
        return (this.soID !== invalid_so_id) && (this.componentID != invalid_component_id);
    }
}

export class SpatialObjectDesc {
    private soID: number;
    private components: Array<SpatialObjectComponentDesc>;

    constructor(soID?: number, components?: Array<SpatialObjectComponentDesc>) {
        this.soID = soID || 0;
        this.components = components || [];
    }

    getID() {
        return this.soID;
    }

    getComponents() {
        return this.components;
    }

    copy(soDesc: SpatialObjectDesc) {
        this.soID = soDesc.soID;
        this.components = soDesc.components;
        return this;
    }
}