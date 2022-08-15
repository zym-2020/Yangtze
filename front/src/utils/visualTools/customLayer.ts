import axios from 'axios';
import mapboxgl from "mapbox-gl";
import {Shader} from '../../utils/geoscratch/core/shader/shader';
import {tbvsSymbols, BillboardSymbolManager} from "../../utils/geoscratch/function/tbvs";
import { Matrix4x4 } from "../../utils/geoscratch/core/math/matrix4";
import {loadTexture} from "../../utils/geoscratch/resource/data/texture"
import { encodeFloatToDouble } from "../../utils/geoscratch/core/webglUtil/utils";
import * as three from 'three';


function degToRad(d: number): number {
    return d * Math.PI / 180;
}

class CustomLayer {
    id: string;
    type: string;
    renderingMode: string;

    constructor(id: string, type: string, renderingMode: string) {
        this.id = id;
        this.type = type;
        this.renderingMode = renderingMode;
    }
    

    onAdd(map: mapboxgl.Map, gl: WebGL2RenderingContext): void {
        console.log("custom layer on add");
    }

    render(gl: WebGL2RenderingContext, u_matrix: Array<number>): void {
        console.log("custom layer on render");
    }
}

class TBVSLayer extends CustomLayer {
    map: mapboxgl.Map | null;
    shader: Shader | null;
    texture: WebGLTexture | null;
    symbols: tbvsSymbols | null;

    framebuffer: Array<number>;
    pixelRatio: number;

    symblManager: BillboardSymbolManager;

    constructor(id: string, type: string, renderingMode: string, symbols: tbvsSymbols, manager: BillboardSymbolManager) {
      super(id, type, renderingMode);
      this.map = null;
      this.shader = null;
      this.texture = null;
      this.symbols = symbols;

      this.framebuffer = [];
      this.pixelRatio = window.devicePixelRatio;

      this.symblManager = manager;
    }

    async onAdd(map: mapboxgl.Map, gl: WebGL2RenderingContext) {
        this.map = map;
        this.framebuffer.push(gl.canvas.width);
        this.framebuffer.push(gl.canvas.height);

        // Get vertex shader source
        const vertexSource = await axios.get("http://172.21.212.10:8080/shaders/tbvs.vert")
        .then((response) => {
            return response.data;
        })
        // Get fragment shader source
        const fragmentSource = await axios.get("http://172.21.212.10:8080/shaders/tbvs.frag")
        .then((response) => {
            return response.data;
        })
        this.shader = new Shader(gl, vertexSource, fragmentSource);

        // Create a texture
        this.texture = loadTexture(gl, "http://172.21.212.10:8080/images/TBVS_88.png", 0);

        // this.symbols?.setup(gl, this.shader);

        this.symblManager.setup(gl, this.shader);
        
        gl.bindBuffer(gl.ARRAY_BUFFER, null);
        gl.bindVertexArray(null);
        gl.bindTexture(gl.TEXTURE_2D, null);

    }

    render(gl: WebGL2RenderingContext, matrix: Array<number>) {
        if (this.shader === null || this.map === null || this.symbols === null)
            return;

        gl.bindTexture(gl.TEXTURE_2D, this.texture);
        this.shader.use(gl);
        this.symblManager.use(gl);
        // this.symbols.use(gl);

        const center = this.map.getCenter();
        const mercatorCenter = mapboxgl.MercatorCoordinate.fromLngLat({lng:center.lng, lat:center.lat});
        const mercatorCenterX = encodeFloatToDouble(mercatorCenter.x);
        const mercatorCenterY = encodeFloatToDouble(mercatorCenter.y);

        const relativeToEyeMatrix = matrix.slice();
        relativeToEyeMatrix[12] += relativeToEyeMatrix[0] * mercatorCenter.x + relativeToEyeMatrix[4] * mercatorCenter.y;
        relativeToEyeMatrix[13] += relativeToEyeMatrix[1] * mercatorCenter.x + relativeToEyeMatrix[5] * mercatorCenter.y;
        relativeToEyeMatrix[14] += relativeToEyeMatrix[2] * mercatorCenter.x + relativeToEyeMatrix[6] * mercatorCenter.y;
        relativeToEyeMatrix[15] += relativeToEyeMatrix[3] * mercatorCenter.x + relativeToEyeMatrix[7] * mercatorCenter.y;

        const symbolPixel = 25.0;
        const radius = 0.0;
        let modelMatrix = Matrix4x4.identity();
        modelMatrix = Matrix4x4.xRotate(modelMatrix, radius);
        modelMatrix = Matrix4x4.yRotate(modelMatrix, radius);
        modelMatrix = Matrix4x4.zRotate(modelMatrix, radius);
        modelMatrix = Matrix4x4.scale(modelMatrix, this.pixelRatio * symbolPixel / this.framebuffer[0], this.pixelRatio * symbolPixel / this.framebuffer[1], 1.0);
        modelMatrix = Matrix4x4.translate(modelMatrix, 0, 0, 0);

        this.shader.setInt(gl, "u_maxInstanceNum", this.symblManager.getNumInstance());
        this.shader.setInt(gl, "symbolTexture", 0);
        this.shader.setFloat2(gl, "u_mercatorCenterHigh", mercatorCenterX[0], mercatorCenterY[0]);
        this.shader.setFloat2(gl, "u_mercatorCenterLow", mercatorCenterX[1], mercatorCenterY[1]);
        this.shader.setMat4(gl, "u_matrix", relativeToEyeMatrix);
        this.shader.setMat4(gl, "u_symbolMatrix", modelMatrix);
        this.shader.setFloat2(gl, "u_bufferSize", this.framebuffer[0], this.framebuffer[1]);


        const primitiveType = gl.TRIANGLES;
        // gl.drawArraysInstanced(primitiveType, 0, 63, this.symbols.getNumInstance()); // 218619
        gl.drawArraysInstanced(primitiveType, 0, 18, this.symblManager.getNumInstance());
        gl.bindVertexArray(null);
        gl.bindTexture(gl.TEXTURE_2D, null);
    }
}

class TLayer extends CustomLayer {
    map: mapboxgl.Map | null;
    // shader: Shader | null;
    material: three.RawShaderMaterial | null;
    // mesh: three.Mesh | null;
    texture: three.Texture | null;
    geom: three.InstancedBufferGeometry | null;
    symbols: tbvsSymbols | null;

    frameSize: Array<number>;
    pixelRatio: number;

    camera: three.Camera | null;
    scene: three.Scene | null;
    renderer: three.WebGLRenderer | null;
    mesh: three.Mesh | null;

    rayCaster: three.Raycaster;
    pointer: three.Vector2;

    symblManager: BillboardSymbolManager;

    

    constructor(id: string, type: string, renderingMode: string, symbols: tbvsSymbols, manager: BillboardSymbolManager) {
        super(id, type, renderingMode);
        this.map = null;

        this.material = null;
        this.texture = null;
        this.geom = null;
        // this.mesh = null;

        this.symbols = symbols;
  
        this.frameSize = [];
        this.pixelRatio = window.devicePixelRatio;

        this.scene = null;
        this.camera = null;
        this.renderer = null;
        this.mesh = null;

        this.rayCaster = new three.Raycaster();
        this.pointer = new three.Vector2();

        this.symblManager = manager;
    }

    async onAdd(map: mapboxgl.Map, gl: WebGL2RenderingContext) {
        this.map = map;
        this.frameSize.push(gl.canvas.width);
        this.frameSize.push(gl.canvas.height);
        this.renderer = new three.WebGLRenderer({canvas: map.getCanvas(), context: gl, antialias: true});
        console.log("drawing context:", gl);
        this.scene = new three.Scene();
        this.camera = new three.PerspectiveCamera();

        this.texture = new three.TextureLoader().load('http://172.21.212.10:8080/textures/TBVS_88.png', (texture)=> {console.log("this is ", texture.image.width)});
        this.texture.flipY = false;
        console.log("texture? ", this.texture);

        this.geom = this.symbols?.Geometry4Three() as three.InstancedBufferGeometry;

        const uniforms = {
            symbolTexture: { value:this.texture }, 
            u_mercatorCenterHigh: {value: new three.Vector2()}, 
            u_mercatorCenterLow: {value: new three.Vector2()}, 
            u_matrix: {value: new three.Matrix4()}, 
            u_symbolMatrix: {value: new three.Matrix4()}
        };
        const vertexShader = await axios.get("http://172.21.212.10:8080/shaders/tbvs_three.vert")
                            .then((response) => {
                                return response.data;
                            });
        const fragmentShader = await axios.get("http://172.21.212.10:8080/shaders/tbvs_three.frag")
                            .then((response) => {
                                return response.data;
                            });

        this.material = new three.RawShaderMaterial({
            glslVersion: three.GLSL3,
            uniforms: uniforms, 
            vertexShader: vertexShader,
            fragmentShader:fragmentShader,
            vertexColors: true,
            side: three.DoubleSide
        });
        // console.log(this.material.vertexShader);


        this.mesh = new three.Mesh(this.geom, this.material);
    
        (this.scene as three.Scene).add(this.mesh);
        console.log(this.scene);
    
        // this.mesh = new three.Mesh(geom, this.material);
        this.renderer.autoClear = false;
    }

    render(gl: WebGL2RenderingContext, matrix: number[]): void {
        if (this.texture === null || this.material === null || this.map === null || this.symbols === null || this.geom == null || this.scene == null || this.camera == null)
            return;

        if (this.texture!== null) {
            this.material.needsUpdate = true;
        }
        
        const center = this.map.getCenter();
        const mercatorCenter = mapboxgl.MercatorCoordinate.fromLngLat({lng:center.lng, lat:center.lat});
        const mercatorCenterX = encodeFloatToDouble(mercatorCenter.x);
        const mercatorCenterY = encodeFloatToDouble(mercatorCenter.y);

        // TODO: Change it into Threejs matrix, row-major or column-major??
        const relativeToEyeMatrix = matrix.slice();
        relativeToEyeMatrix[12] += relativeToEyeMatrix[0] * mercatorCenter.x + relativeToEyeMatrix[4] * mercatorCenter.y;
        relativeToEyeMatrix[13] += relativeToEyeMatrix[1] * mercatorCenter.x + relativeToEyeMatrix[5] * mercatorCenter.y;
        relativeToEyeMatrix[14] += relativeToEyeMatrix[2] * mercatorCenter.x + relativeToEyeMatrix[6] * mercatorCenter.y;
        relativeToEyeMatrix[15] += relativeToEyeMatrix[3] * mercatorCenter.x + relativeToEyeMatrix[7] * mercatorCenter.y;
        const relative2EyeMatrix = new three.Matrix4().fromArray(relativeToEyeMatrix);

        const symbolPixel = 20.0;
        const radius = 0.0;
        // TODO: Change it into Threejs matrix
        const rotationMatrix = new three.Matrix4().makeRotationX(radius).makeRotationY(radius).makeRotationZ(radius);
        const scaleMatrix = new three.Matrix4().makeScale(this.pixelRatio * symbolPixel / this.frameSize[0], this.pixelRatio * symbolPixel / this.frameSize[1], 1.0);
        const transformMatrix = new three.Matrix4().makeTranslation(0, 0, 0);
        
        const modelMatrix = rotationMatrix.multiply(scaleMatrix).multiply(transformMatrix);


        // console.log(this.material);

        ((this.scene.children[0] as three.Mesh).material as three.RawShaderMaterial).uniforms.u_mercatorCenterHigh.value.x = mercatorCenterX[0];
        ((this.scene.children[0] as three.Mesh).material as three.RawShaderMaterial).uniforms.u_mercatorCenterHigh.value.y = mercatorCenterY[0];

        ((this.scene.children[0] as three.Mesh).material as three.RawShaderMaterial).uniforms.u_mercatorCenterLow.value.x = mercatorCenterX[1];
        ((this.scene.children[0] as three.Mesh).material as three.RawShaderMaterial).uniforms.u_mercatorCenterLow.value.y = mercatorCenterY[1];

        ((this.scene.children[0] as three.Mesh).material as three.RawShaderMaterial).uniforms.u_matrix.value = relative2EyeMatrix;
        ((this.scene.children[0] as three.Mesh).material as three.RawShaderMaterial).uniforms.u_symbolMatrix.value = modelMatrix;

        ((this.scene.children[0] as three.Mesh).material as three.RawShaderMaterial).needsUpdate = true;

        // const mesh = new three.Mesh(this.geom, this.material);
    
        // (this.scene as three.Scene).add(mesh);
        
        this.camera.projectionMatrix = relative2EyeMatrix;

        this.rayCaster.setFromCamera( this.pointer, this.camera );
        const intersections = this.rayCaster.intersectObjects(this.scene.children);
        // console.log(this.rayCaster);
        if(intersections.length > 0) {
            const intersect = intersections[0].object as THREE.Mesh;
            (intersect.material as THREE.Material).opacity = 0.2;
            console.log(intersect);
        }

        this.renderer?.render(this.scene, this.camera);
        (this.renderer as three.WebGLRenderer).resetState();
        this.map.triggerRepaint();
    }

}


// parameters to ensure the model is georeferenced correctly on the map
const modelOrigin = [148.9819, -35.39847];
const modelAltitude = 0;
const modelRotate = [Math.PI / 2, 0, 0];
 
const modelAsMercatorCoordinate = mapboxgl.MercatorCoordinate.fromLngLat(
{lng: modelOrigin[0], lat: modelOrigin[1]},
modelAltitude
);
 
// transformation parameters to position, rotate and scale the 3D model onto the map
const modelTransform = {
translateX: modelAsMercatorCoordinate.x,
translateY: modelAsMercatorCoordinate.y,
translateZ: modelAsMercatorCoordinate.z,
rotateX: modelRotate[0],
rotateY: modelRotate[1],
rotateZ: modelRotate[2],
/* Since the 3D model is in real world meters, a scale transform needs to be
* applied since the CustomLayerInterface expects units in MercatorCoordinates.
*/
scale: modelAsMercatorCoordinate.meterInMercatorCoordinateUnits()
};

class mLayer extends CustomLayer {
    map: mapboxgl.Map | null;
    // shader: Shader | null;
    material: three.RawShaderMaterial | null;
    // mesh: three.Mesh | null;
    texture: three.Texture | null;
    geom: three.BufferGeometry | null;
    symbols: tbvsSymbols | null;

    frameSize: Array<number>;
    pixelRatio: number;

    camera: three.Camera | null;
    scene: three.Scene | null;
    renderer: three.WebGLRenderer | null;
    mesh: three.Mesh | null;


    constructor(id: string, type: string, renderingMode: string, symbols: tbvsSymbols) {
        super(id, type, renderingMode);
        this.map = null;

        this.material = null;
        this.texture = null;
        this.geom = null;
        // this.mesh = null;

        this.symbols = symbols;
  
        this.frameSize = [];
        this.pixelRatio = window.devicePixelRatio;

        this.scene = null;
        this.camera = null;
        this.renderer = null;
        this.mesh = null;
    }

    async onAdd(map: mapboxgl.Map, gl: WebGL2RenderingContext) {
        this.camera = new three.Camera();
        this.scene = new three.Scene();
        
        // create two three.js lights to illuminate the model
        const directionalLight = new three.DirectionalLight(0xffffff);
        directionalLight.position.set(0, -70, 100).normalize();
        this.scene.add(directionalLight);
        
        const directionalLight2 = new three.DirectionalLight(0xffffff);
        directionalLight2.position.set(0, 70, 100).normalize();
        this.scene.add(directionalLight2);
        
        this.geom = new three.BoxGeometry( 10, 10, 10 );
        // const material = new three.MeshBasicMaterial( { color: 0x00ff00 } );
        
        this.material = new three.RawShaderMaterial( {
            glslVersion: three.GLSL3,
            uniforms: {
            },
            vertexShader: await axios.get("http://172.21.212.10:8080/shaders/tbvs_three.vert")
                                .then((response) => {
                                    return response.data;
                                }), 
            fragmentShader: await axios.get("http://172.21.212.10:8080/shaders/tbvs_three.frag")
                                .then((response) => {
                                    return response.data;
                                }),
            side: three.BackSide
        } );
        const cube = new three.Mesh(this.geom, this.material);

        // use the three.js GLTF loader to add the 3D model to the three.js scene
        // const loader = new GLTFLoader();
        // loader.load(
        // 'https://docs.mapbox.com/mapbox-gl-js/assets/34M_17/34M_17.gltf',
        //     (gltf) => {
        //         this.scene!.add(gltf.scene);
        //         console.log("loaded!!!");
        //     }
        // );
        this.scene.add(cube);
        this.map = map;
        
        // use the Mapbox GL JS map canvas for three.js
        this.renderer = new three.WebGLRenderer({
        canvas: map.getCanvas(),
        context: gl,
        antialias: true
        });
        
        this.renderer.autoClear = false;
    }

    render(gl: WebGL2RenderingContext, matrix: number[]) {
        if (this.material === null || this.map === null || this.geom == null || this.scene == null || this.camera == null)
            return;

        const rotationX = new three.Matrix4().makeRotationAxis(
        new three.Vector3(1, 0, 0),
            modelTransform.rotateX
        );
        const rotationY = new three.Matrix4().makeRotationAxis(
            new three.Vector3(0, 1, 0),
            modelTransform.rotateY
        );
        const rotationZ = new three.Matrix4().makeRotationAxis(
                new three.Vector3(0, 0, 1),
                modelTransform.rotateZ
        );
        
        const m = new three.Matrix4().fromArray(matrix);
        const l = new three.Matrix4()
        .makeTranslation(
            modelTransform.translateX,
            modelTransform.translateY,
            modelTransform.translateZ!
        )
        .scale(
            new three.Vector3(
                modelTransform.scale,
                -modelTransform.scale,
                modelTransform.scale
            )
        )
        .multiply(rotationX)
        .multiply(rotationY)
        .multiply(rotationZ);
        
        this.camera!.projectionMatrix = m.multiply(l);
        this.renderer!.resetState();
        this.renderer!.render(this.scene!, this.camera!);
        this.map!.triggerRepaint();
    }
}

export{
    CustomLayer,
    TBVSLayer, 
    TLayer,
    mLayer
};