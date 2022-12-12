import { Transform } from "../../core/math/transform";
import { Vector3 } from "../../core/math/vector3";
import { Shader } from "../../core/shader/shader";
import axios from "axios";
import { assetManager } from "../asset_manager";
import { prefix } from '@/prefix'

const cubeVertices = Array<number>(
    // Back face
    -0.5, -0.5, -0.5,  0.0, 0.0, // Bottom-left
     0.5,  0.5, -0.5,  1.0, 1.0, // top-right
     0.5, -0.5, -0.5,  1.0, 0.0, // bottom-right         
     0.5,  0.5, -0.5,  1.0, 1.0, // top-right
    -0.5, -0.5, -0.5,  0.0, 0.0, // bottom-left
    -0.5,  0.5, -0.5,  0.0, 1.0, // top-left
    // Front face
    -0.5, -0.5,  0.5,  0.0, 0.0, // bottom-left
     0.5, -0.5,  0.5,  1.0, 0.0, // bottom-right
     0.5,  0.5,  0.5,  1.0, 1.0, // top-right
     0.5,  0.5,  0.5,  1.0, 1.0, // top-right
    -0.5,  0.5,  0.5,  0.0, 1.0, // top-left
    -0.5, -0.5,  0.5,  0.0, 0.0, // bottom-left
    // Left face
    -0.5,  0.5,  0.5,  1.0, 0.0, // top-right
    -0.5,  0.5, -0.5,  1.0, 1.0, // top-left
    -0.5, -0.5, -0.5,  0.0, 1.0, // bottom-left
    -0.5, -0.5, -0.5,  0.0, 1.0, // bottom-left
    -0.5, -0.5,  0.5,  0.0, 0.0, // bottom-right
    -0.5,  0.5,  0.5,  1.0, 0.0, // top-right
    // Right face
     0.5,  0.5,  0.5,  1.0, 0.0, // top-left
     0.5, -0.5, -0.5,  0.0, 1.0, // bottom-right
     0.5,  0.5, -0.5,  1.0, 1.0, // top-right         
     0.5, -0.5, -0.5,  0.0, 1.0, // bottom-right
     0.5,  0.5,  0.5,  1.0, 0.0, // top-left
     0.5, -0.5,  0.5,  0.0, 0.0, // bottom-left     
    // Bottom face
    -0.5, -0.5, -0.5,  0.0, 1.0, // top-right
     0.5, -0.5, -0.5,  1.0, 1.0, // top-left
     0.5, -0.5,  0.5,  1.0, 0.0, // bottom-left
     0.5, -0.5,  0.5,  1.0, 0.0, // bottom-left
    -0.5, -0.5,  0.5,  0.0, 0.0, // bottom-right
    -0.5, -0.5, -0.5,  0.0, 1.0, // top-right
    // Top face
    -0.5,  0.5, -0.5,  0.0, 1.0, // top-left
     0.5,  0.5,  0.5,  1.0, 0.0, // bottom-right
     0.5,  0.5, -0.5,  1.0, 1.0, // top-right     
     0.5,  0.5,  0.5,  1.0, 0.0, // bottom-right
    -0.5,  0.5, -0.5,  0.0, 1.0, // top-left
    -0.5,  0.5,  0.5,  0.0, 0.0  // bottom-left  
);

export class CubeMesh {
    vao: WebGLVertexArrayObject | null;
    vbo_pos: WebGLBuffer | null;
    vbo_uv: WebGLBuffer | null;
    vbo_normal: WebGLBuffer | null;
    ebo: WebGLBuffer | null;

    vertexCount: number;

    transform: Transform;

    constructor(position: Vector3, scale: Vector3, rotation: Vector3) {
        this.vao = null;
        this.vbo_pos = null;
        this.vbo_uv = null;
        this.vbo_normal = null;
        this.ebo = null;


        this.vertexCount = 0

        this.transform = new Transform(position, scale, rotation);
    }

    async setup(gl: WebGL2RenderingContext, shader: Shader) {
        const meshData = await axios.get(prefix + "json/cube.json")
            .then((response) => {
                return response.data;
            });
        this.vao = gl.createVertexArray();
        gl.bindVertexArray(this.vao);

        this.vbo_pos = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, this.vbo_pos);
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(meshData.vertices), gl.STATIC_DRAW);
        shader.setVertexBufferPointer(gl, 0, 3, gl.FLOAT, false, 3 * 4, 0 * 4);

        this.vbo_normal = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, this.vbo_normal);
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(meshData.normals), gl.STATIC_DRAW);
        shader.setVertexBufferPointer(gl, 1, 3, gl.FLOAT, false, 3 * 4, 0 * 4);
        this.vertexCount = meshData.vertices.length;

        this.vbo_uv = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, this.vbo_uv);
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(meshData.texcoords), gl.STATIC_DRAW);
        shader.setVertexBufferPointer(gl, 3, 2, gl.FLOAT, false, 2 * 4, 0 * 4);

        if (meshData.indices.length !== 0) {

            this.ebo = gl.createBuffer();
            gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, this.ebo);
            gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(meshData.indices), gl.STATIC_DRAW);

            this.vertexCount = meshData.indices.length;
        }

        gl.bindVertexArray(null);
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, null);
    }

    use(gl: WebGL2RenderingContext) {
        gl.bindVertexArray(this.vao);
    }

    destroy(gl: WebGL2RenderingContext) {
        gl.deleteBuffer(this.vbo_pos);
        gl.deleteBuffer(this.vbo_normal);
        gl.deleteBuffer(this.vbo_uv);
        gl.deleteBuffer(this.ebo);
        gl.deleteVertexArray(this.vao);
    }   

    // tick(gl: WebGL2RenderingContext) {
    //     gl.
    // }

    getNumVertices() {
        return this.vertexCount;
    }
}