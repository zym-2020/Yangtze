import { RenderMaterial } from './render_material';
import { RenderMesh } from './render_mesh';
import { Mutex } from './../../core/lock/mutex';
import { SCamera } from './render_camera';
import { Vector3 } from '../../core/math/vector3';

export class RenderScene {
    loaded: boolean;
    minBounds: Vector3;
    maxBounds: Vector3;
    center: Vector3;
    camera: SCamera;

    private sceneMutex: Mutex;
    private meshes: Array<number>; // meshID
    private materials: Array<number>; // materialID

    constructor() {
        this.loaded = false;
        this.minBounds = new Vector3();
        this.maxBounds = new Vector3();
        this.center = new Vector3();
        this.camera = new SCamera();

        this.sceneMutex = new Mutex();
        this.meshes = [];
        this.materials = [];
    }

    lock() {
        this.sceneMutex.lock();
    }

    unlock() {
        this.sceneMutex.unlock();
    }

    getMeshes() {
        return this.meshes;
    }

    getMaterials() {
        return this.materials;
    }

    addMesh(meshID: number) {
        this.meshes.push(meshID);
    }

    addMaterial(materialID: number) {
        this.materials.push(materialID);
    }

    clearMeshes() {
        this.meshes = [];
    }

    clearMaterials() {
        this.materials = [];
    }

    clear() {
        this.clearMeshes();
        this.clearMaterials();
    }
}   
