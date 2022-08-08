import { sceneManager } from './../../../scene/scene_manager';
import { TransformComponent } from './../transform/transform_component';
import { SpatialObject } from './../../object/object';
import { CameraComponentRes, FirstPersonCameraParameter } from '../../../../resource/res_type/component/camera';
import { Component } from "../component";
import { Vector3 } from '../../../../core/math/vector3';
import { Matrix4x4 } from '../../../../core/math/matrix4';
import { DEG2RAD } from "../../../../core/math/utils";
import { SCurrentCameraType } from '../../../render/render_camera';

enum CameraMode {
    thirdPerson,
    firstPerson,
    free,
    invalid
}

export interface View {
    enabled: boolean,
    fullWidth: number,
    fullHeight: number,
    offsetX: number,
    offsetY: number,
    width: number,
    height: number
}

export class CameraComponent extends Component {
    parentObject: SpatialObject;
    cameraMode: CameraMode;

    target: Vector3;
    up: Vector3;
    near: number;
    far: number;
    fov: number;
    aspect: number;
    zoom: number;

    view: View | null;

    constructor(cameraComponentRes: CameraComponentRes, parentObject: SpatialObject) {
        super(parentObject);
        this.parentObject = parentObject;

        this.cameraMode = CameraMode.invalid;
        this.target = new Vector3();
        this.up = new Vector3(0, 1, 0);
        this.near = 0.1;
        this.far = 100;
        this.fov = 60;
        this.aspect = 1;
        this.zoom = 1;

        this.view = null;
    }

    public tick() {
        // TODO: need more camera mode tick function!

        switch (this.cameraMode) {
            case CameraMode.firstPerson:
                this.tickFirstPersonCamera();
                break;
        
            default:
                break;
        }
    }

    public tickFirstPersonCamera() {
        const parentTransform = this.parentObject.tryGetComponent<TransformComponent>("TransformComponent");
        if (!parentTransform)
            return;

        const viewMatrix = this.lookAt();
        if (!viewMatrix) return;
        const projMatrix = this.project();
        if (!projMatrix) return;
        sceneManager.setMainViewMatrix(Matrix4x4.setFromF32ArrayBuffer(viewMatrix), SCurrentCameraType.Motor);
        sceneManager.setMainProjMatrix(Matrix4x4.setFromF32ArrayBuffer(projMatrix), SCurrentCameraType.Motor);
    }

    public static async load(cameraComponentRes: CameraComponentRes, parentObject: SpatialObject) {
        switch (cameraComponentRes.parameters.typeName) {
            case "FirstPersonCameraParameter": {
                const params = cameraComponentRes.parameters as FirstPersonCameraParameter;
                const camera = new CameraComponent(cameraComponentRes, parentObject)
                
                camera.cameraMode = CameraMode.firstPerson;
                camera.target = new Vector3(params.target[0], params.target[1], params.target[2]);
                camera.near = params.near;
                camera.far = params.far;
                camera.fov = params.fov;
                return camera;
            }
            default:
                return new CameraComponent(cameraComponentRes, parentObject);
        }
    }

    public setAspect(aspect: number) {
        this.aspect = aspect;
    }

    public lookAt() {
        const transform = this.parentObject.tryGetComponent<TransformComponent>("TransformComponent");
        if (!transform) return;
        return Matrix4x4.lookAt(transform.getPosition(), this.target, this.up);
    }

    public project() {
		const near = this.near;
		let top = near * Math.tan( DEG2RAD * 0.5 * this.fov ) / this.zoom;
		let height = 2 * top;
		let width = this.aspect * height;
		let left = - 0.5 * width;
		const view = this.view;

		if ( view !== null && view.enabled ) {
			const fullWidth = view.fullWidth,
				fullHeight = view.fullHeight;

			left += view.offsetX * width / fullWidth;
			top -= view.offsetY * height / fullHeight;
			width *= view.width / fullWidth;
			height *= view.height / fullHeight;
		}

		return Matrix4x4.project(left, left + width, top, top - height, near, this.far);
	}

    public destroy() {
        // See you!
    }
    
}
