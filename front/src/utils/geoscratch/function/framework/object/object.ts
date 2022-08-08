import { AnyComponentType } from './../component/component';
import { CameraComponent } from './../component/camera/camera_component';
import { TransformComponent } from './../component/transform/transform_component';
import { ObjectInstanceRes, ComponentDefinitionRes, ObjectDefinitionRes } from './../../../resource/res_type/common/object';
import { Component } from "../component/component";
import { Transform } from "../../../core/math/transform";
import { assetManager } from '../../../resource/asset_manager';


type TypeNameSet = Set<string>;

export class SpatialObject {
    id: number;
    name: string | undefined;
    defUrl: string | undefined;
    components: Array<Component>;
    componentTypeNames: Array<string>;
    

    constructor(id:number) {
        this.id = id;
        this.components = [];
        this.componentTypeNames = [];

    }

    tick() {
        // Start Dash!
        for (const component of this.components) {
            component.tick();
        }
    }

    public hasComponent(typeName: string) {
        for(const componentTypeName of this.componentTypeNames)
            if (componentTypeName === typeName)
                return true;
        return false;
    }

    public static async load(id: number, objectInstanceRes: ObjectInstanceRes) {
        const object = new SpatialObject(id);

        object.setName(objectInstanceRes.name);

        // Load transform component
        const transformComponent = await TransformComponent.load(objectInstanceRes.transform, object);
        // const transformComponent = new TransformComponent(objectInstanceRes.transform, object);
        object.components.push(transformComponent);
        object.componentTypeNames.push("TransformComponent");

        // Load object instance component
        // Do not pay too much attetion on this at Ver.0
        const instanceComponentTypeSet = new Set<string>() as TypeNameSet;

        // Load object definition components
        object.defUrl = objectInstanceRes.definition;
        const definitionRes = await assetManager.loadAsset<ObjectDefinitionRes>(object.defUrl);

        if (await object.loadComponents(definitionRes.components, instanceComponentTypeSet))
            return object;

        return null;
    }

    async loadComponents(components: Array<string>, outInstanceComponentTypeSet: TypeNameSet) {
        let definitionRes : ComponentDefinitionRes;

        for(const definitionUrl of components) {
            definitionRes = await assetManager.loadAsset<ComponentDefinitionRes>(definitionUrl);
            if (await this.loadComponentDefinition(definitionRes, false, outInstanceComponentTypeSet))
            return true;
        }
        return false;
    }

    async loadComponentDefinition(componentDefinitionRes: ComponentDefinitionRes, isInstanceComponent: boolean, outInstanceComponentTypeSet: TypeNameSet) {
        if (isInstanceComponent || !this.hasComponent(componentDefinitionRes.typeName)) {
            const component = await assetManager.loaderFunc(componentDefinitionRes.typeName, componentDefinitionRes.component, this);

            if (component) {
                this.components.push(component);
                this.componentTypeNames.push(componentDefinitionRes.typeName);
                component.setParentObject(this);
                outInstanceComponentTypeSet.add(componentDefinitionRes.typeName);
            }
            else 
                return false;
        }

        return true;
    }

    setName(name: string) {
        this.name = name;
    }

    getName() {
        return this.name;
    }

    tryGetComponent<ComponentType extends AnyComponentType>(componentTypeName: string) {
        for (let i = 0; i < this.components.length; i++) {
            if (this.componentTypeNames[i] === componentTypeName)
                return this.components[i] as ComponentType;
        }
        return null;
    }

    getID() {
        return this.id;
    }

    destroy() {
        // 
    }
}