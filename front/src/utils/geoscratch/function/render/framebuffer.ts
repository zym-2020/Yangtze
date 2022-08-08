import { RenderScene } from './render_scene';

export enum RenderPath {
    Forward,
    Deferred,
    Clustered
}

export class FrameBuffer {
    scene: RenderScene | undefined;
    renderPath =  RenderPath.Forward;
    logicalFrameIndex = 0;
}
