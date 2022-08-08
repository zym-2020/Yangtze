import { TransformRes } from "../../../core/math/transform";

export interface ComponentDefinitionRes {
    typeName: string,
    component: string
}

export interface ObjectDefinitionRes {
    components: Array<string>
}

export interface ObjectInstanceRes {
    name: string,
    transform: TransformRes,
    definition: string,
    instanceComponents: Array<string>
}