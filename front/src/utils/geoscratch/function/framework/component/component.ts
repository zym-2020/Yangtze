import { SpatialObject } from './../object/object';

import { CameraComponentRes } from '../../../resource/res_type/component/camera'
// /resource/res_type/component/camera';
import { MeshComponentRes } from '../../../resource/res_type/component/mesh';
import { TransformRes } from '../../../core/math/transform';

import { CameraComponent } from './camera/camera_component';
import { MeshComponent } from './mesh/mesh_compoent';
import { TransformComponent } from './transform/transform_component';


export type AnyComponentResType = CameraComponentRes | MeshComponentRes | TransformRes | undefined;
export type AnyComponentType = CameraComponent | MeshComponent | TransformComponent | undefined;

export abstract class Component {
    parentObject: SpatialObject;

    constructor(parentObject: SpatialObject) {
        this.parentObject = parentObject;
    }

    // public static async load(componentRes: AnyComponentResType, parentObject: SpatialObject){
    //     // async load ANY component
    //     return new Component(parentObject);
    // }

    setParentObject(sObject: SpatialObject) {
        this.parentObject = sObject;
    }

    abstract tick(): void;
    
    destroy() {
        // See You!
    }
}

// export interface ComponentInterface {
//     tick(): void;
//     load(componentRes: AnyComponentResType): void;
// }

// export interface ComponentConstructor {
//     new (componentRes: AnyComponentResType,  parentObject: SpatialObject): ComponentInterface;
// }

// export function loadComponent(componentConstructor: ComponentConstructor, componentRes: AnyComponentResType, parentObject: SpatialObject) {
//     return new componentConstructor(componentRes, parentObject);
// }