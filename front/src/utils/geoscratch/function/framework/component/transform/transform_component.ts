import { Vector3 } from "../../../../core/math/vector3";
import { Transform, TransformRes } from "../../../../core/math/transform";
import { Component } from "../component";
import { SpatialObject } from "../../object/object";

export class TransformComponent extends Component {
    transformBuffer: Array<Transform>;
    currentIndex: number;
    nextIndex: number;

    constructor(transform: TransformRes, parentObject: SpatialObject) {
        super(parentObject);

        this.transformBuffer = [];
        this.currentIndex = 0;
        this.nextIndex = 1;
    }

    public static async load(transformComponentRes: TransformRes, parentObject: SpatialObject) {
        const transform = new TransformComponent(transformComponentRes, parentObject);
        transform.transformBuffer = [Transform.setFromRes(transformComponentRes), Transform.setFromRes(transformComponentRes)];
        return transform;
    }

    getPosition() {
        return this.transformBuffer[this.currentIndex].position;
    }
    getScale() {
        return this.transformBuffer[this.currentIndex].scale;
    }
    getRotation() {
        return this.transformBuffer[this.currentIndex].rotation;
    }
    setPosition(newTranslation: Vector3) {
        this.transformBuffer[this.nextIndex].position = newTranslation;
    }
    setScale(newScale: Vector3) {
        this.transformBuffer[this.nextIndex].scale = newScale;
    }
    setRotation(newRotation: Vector3) {
        this.transformBuffer[this.nextIndex].rotation = newRotation;
    }
    getTransform() {
        return this.transformBuffer[this.currentIndex];
    }
    getMatrix() {
        return this.transformBuffer[this.currentIndex].getMatrix();
    }

    tick() {
        const temp = this.currentIndex; this.currentIndex = this.nextIndex; this.nextIndex = temp;
    }

}