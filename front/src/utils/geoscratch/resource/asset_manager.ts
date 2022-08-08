// import { AnyComponentResType, loadComponent, AnyComponentType } from './../function/framework/component/component';
import { SpatialObject } from './../function/framework/object/object';
import { Texture, isPowerOf2 } from "./data/texture";
import { Shader } from './data/shader';
import axios from "axios";
import { ObjectDefinitionRes, ComponentDefinitionRes } from "./res_type/common/object";

import { CameraComponent } from '../function/framework/component/camera/camera_component';
import { CameraComponentRes } from './res_type/component/camera';
import { TransformComponent } from '../function/framework/component/transform/transform_component';
import { TransformRes } from '../core/math/transform';
import { MeshComponent } from '../function/framework/component/mesh/mesh_compoent';
import { MeshComponentRes } from './res_type/component/mesh';
import { AnyComponentType, AnyComponentResType, Component } from '../function/framework/component/component';
import { defineAsyncComponent } from 'vue';


export class ShaderManager {
    shaders: Map<string, Shader>;

    constructor() {
        this.shaders = new Map();
    }

    async loadShader_url(gl: WebGL2RenderingContext, name: string, vertexUrl: string, fragmentUrl: string) {
        if (this.shaders.has(name)) return;

        const vertexSource = await axios.get(vertexUrl)
        .then((response) => {
            return response.data;
        });
        const fragmentSource = await axios.get(fragmentUrl)
        .then((response) => {
            return response.data;
        });

        const shader = new Shader(gl, name, vertexSource, fragmentSource);
        this.shaders.set(name, shader);
    }

    deleteShader(gl: WebGL2RenderingContext, name: string) {
        const shader = this.shaders.get(name);
        if (!shader) return false;
        gl.deleteShader(shader)

        this.shaders.delete(name);
        return true;
    }

    getShader(name: string) {
        const shader = this.shaders.get(name);
        if (!shader) {
            console.warn("No shader named", name);
            return null;
        }
        else
            return shader;
    }
}

export class TextureManager {
    textures: Map<string, Texture>;

    constructor() {
        this.textures = new Map();
    }

    loadTexture(gl: WebGL2RenderingContext, url: string) {
        if (this.textures.has(url)) return;

        const textureID = gl.createTexture();
        gl.bindTexture(gl.TEXTURE_2D, textureID);
    
        const image = new Image();
        image.src = url;
        image.onload = function() {
            gl.bindTexture(gl.TEXTURE_2D, textureID);
            gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image);

            if (isPowerOf2(image.width) && isPowerOf2(image.height)) {
                gl.generateMipmap(gl.TEXTURE_2D);
            } else {
                gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
                gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
                gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
                gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
            }
        };

        if (textureID === null) return;
        const texture = new Texture(url, textureID);
        this.textures.set(url, texture);
    }

    deleteTexture(gl: WebGL2RenderingContext, url: string) {
        const texture = this.textures.get(url);
        if (!texture) return false;
        gl.deleteTexture(texture)

        this.textures.delete(url);
        return true;
    }

    getTexture(url: string) {
        const texture = this.textures.get(url);
        if (!texture) {
            console.warn("No texture from the url:", url);
            return null;
        }
        else
            return texture;
    }
}

export class MeshManager {
    textures: Map<string, Texture>;

    constructor() {
        this.textures = new Map();
    }

    loadTexture(gl: WebGL2RenderingContext, url: string) {
        if (this.textures.has(url)) return;

        const textureID = gl.createTexture();
        gl.bindTexture(gl.TEXTURE_2D, textureID);
    
        const image = new Image();
        image.src = url;
        image.onload = function() {
            gl.bindTexture(gl.TEXTURE_2D, textureID);
            gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image);

            if (isPowerOf2(image.width) && isPowerOf2(image.height)) {
                gl.generateMipmap(gl.TEXTURE_2D);
            } else {
                gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
                gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
                gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
                gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
            }
        };

        if (textureID === null) return;
        const texture = new Texture(url, textureID);
        this.textures.set(url, texture);
    }

    deleteTexture(gl: WebGL2RenderingContext, url: string) {
        const texture = this.textures.get(url);
        if (!texture) return false;
        gl.deleteTexture(texture)

        this.textures.delete(url);
        return true;
    }

    getTexture(url: string) {
        const texture = this.textures.get(url);
        if (!texture) {
            console.warn("No texture from the url:", url);
            return null;
        }
        else
            return texture;
    }
}

export class AssetManager {
    textures: TextureManager;
    shaders: ShaderManager;

    constructor() {
        this.textures = new TextureManager();
        this.shaders = new ShaderManager();
    }

    async loadAsset<ComponentResType>(url: string) {
        const asset = await axios.get(url)
            .then((response) => {
                return (response.data as ComponentResType);
        });

        return asset;
    }
    
    public async loaderFunc(componentType: string, componentResUrl: string, sObject: SpatialObject) {
        let componentRes: AnyComponentResType;
        let component: AnyComponentType;
        switch (componentType) {
            case "CameraComponent":
                componentRes = await this.loadAsset<CameraComponentRes>(componentResUrl);
                component = await CameraComponent.load(componentRes, sObject);
                break;

            case "MeshComponent":
                componentRes = await this.loadAsset<MeshComponentRes>(componentResUrl);
                component = await MeshComponent.load(componentRes, sObject);
                break;

            case "TransformComponent":
                componentRes = await this.loadAsset<TransformRes>(componentResUrl);
                component = await TransformComponent.load(componentRes, sObject);
                break;

            default: 
                component = undefined;
                break;
        }

        return component;
    }

    async loadAssetFromJSON(gl: WebGL2RenderingContext, url: string) {
        const assets = await axios.get(url)
            .then((response) => {
                return response.data;
            });
        
        if (assets.material.textures.length) {
            for (let i = 0; i < assets.material.textures.length; i++) {
                this.textures.loadTexture(gl, assets.material.textures[i]);
            }
        }

        if (assets.material.shader) {
            if (assets.material.shader.sourceType === "url")
                await this.shaders.loadShader_url(gl, assets.material.shader.name, assets.material.shader.vertSource, assets.material.shader.fragSource);
        }
    }
}

export const assetManager = new AssetManager();