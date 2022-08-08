export class SceneResourceHandle {
    handle: number;

    constructor() {
        this.handle = 0;
    }

    getHashValue() {
        return this.handle;
    }

}

export class VertexBufferHandle extends SceneResourceHandle {}
export class IndexBufferHandle extends SceneResourceHandle {}
export class TextureHandle extends SceneResourceHandle {}
export class DynamicVertexBufferHandle extends SceneResourceHandle {}
export class ShaderHandle extends SceneResourceHandle {}