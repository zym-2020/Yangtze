import { VertexBufferHandle, IndexBufferHandle, TextureHandle, SceneResourceHandle, ShaderHandle } from './resource_handle';

export enum PixelFormat {
    Scratch_R8G8B8_RGB,
    Scratch_R8G8B8_SRGB,
    Scratch_R8G8B8A8_RGB,
    Scratch_R8G8B8A8_SRGB,
    Scratch_R32G32_FLOAT,
    Scratch_R32G32B32_FLOAT,
    Scratch_R32G32B32A32_FLOAT,
}

export enum TextureType {
    Scratch_Texture_Type_2D
}

export type SceneMemory = {
    handle: number, // byte size of memory data
    data:ArrayBuffer | null
}

export type SceneTexture = {
    // width: number,
    // height: number,
    // depth: number,
    // mipLevels: number,
    // arrayLayers: number,

    // format: number,
    // type: number,
    // data: ArrayBuffer | null;
    textureElement: HTMLImageElement | null;
}

export type SceneShader = {
    name: string,
    vertCode: string,
    fragCode: string
}

export class SceneBuffers {

    static destroy(handle: VertexBufferHandle | IndexBufferHandle | TextureHandle): void {
        // this may be not useful...
        
        // const sceneMemory = handle as unknown as SceneMemory;
        // if (sceneMemory) {
        //     if (sceneMemory.data) {
        //         sceneMemory.data = null;
        //     }
        // }
    }

    static isValid(handle: VertexBufferHandle | IndexBufferHandle | TextureHandle) {
        const sceneMemory = handle as unknown as SceneMemory;
        return sceneMemory.data !== null;
    }

    static alloc(size: number, data: ArrayBuffer) {
        const memory: SceneMemory = {handle: size, data: data};
        return memory;
    }

    static createVertexBuffer(memory: SceneMemory): VertexBufferHandle {
        return memory as unknown as VertexBufferHandle;
    }

    static createTexture(texture: SceneTexture) {
        return texture as unknown as TextureHandle;
    }

    static createScene(shader: SceneShader) {
        return shader as unknown as ShaderHandle;
    }

    static memoryFromHandle(handle: SceneResourceHandle) {
        return handle as unknown as SceneMemory;
    }

    static textureFromHandle(handle: TextureHandle) {
        return handle as unknown as SceneTexture;
    }

    static shaderFromHandle(handle: ShaderHandle) {
        return handle as unknown as SceneShader;
    }

}