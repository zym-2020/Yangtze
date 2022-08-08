import { sceneManager } from './../../scene/scene_manager';
import { ObjectInstanceRes } from './../../../resource/res_type/common/object';
import { SceneRes } from './../../../resource/res_type/common/scene';
import { assetManager } from '../../../resource/asset_manager';
import { SpatialObject } from "../object/object";

export const invalid_so_id = Number.MAX_SAFE_INTEGER;
export class Scene {
    
    private resUrl: string;
    private nextObjectID: number;
    private sObjects: Map<number, SpatialObject>;

    constructor(url: string) {
        this.resUrl = url;
        this.nextObjectID = 0;
        this.sObjects = new Map();
    }

    public static async load(sceneResUrl: string) {
        const scene = new Scene(sceneResUrl);
        const sceneRes  = await assetManager.loadAsset<SceneRes>(sceneResUrl);

        for (const objectInstanceRes of sceneRes.objects) {
            await scene.createObject(objectInstanceRes);
        }

        return scene;
    }

    public save() {
        // save to JSON
        // need to be added
    }

    public getSceneResUrl() {
        return this.resUrl;
    }

    public getAllSObjects() {
        return this.sObjects;
    }

    public getSObjectByID(id: number) {
        return this.sObjects.get(id);
    }

    public deleteSObjectByID(id: number) {
        if (this.sObjects.has(id)) {
            this.sObjects.get(id)?.destroy();
            this.sObjects.delete(id);
        }
    }

    public async createObject(objectInstanceRes: ObjectInstanceRes) {
        let soID = invalid_so_id;
        const sObject = await SpatialObject.load(this.nextObjectID, objectInstanceRes);

        if (sObject) {
            soID = this.nextObjectID;
            this.sObjects.set(soID, sObject);
            ++ this.nextObjectID;
        }
        else 
            console.log("loading object " + objectInstanceRes.name + " failed!");
        
        return soID;
    }

    public async tickAll() {
        for (const sObjectPair of this.sObjects) {
            if (sObjectPair[1])
                sObjectPair[1].tick();
        }
        await sceneManager.syncSceneObjects();
    }

    public clear() {
        for (const sObjectPair of this.sObjects) {
            this.deleteSObjectByID(sObjectPair[0]);
        }
    }
}