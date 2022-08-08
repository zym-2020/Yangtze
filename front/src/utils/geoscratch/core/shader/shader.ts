/* eslint-disable @typescript-eslint/no-non-null-assertion */
export class Shader {
    name: string;
    vsModule: WebGLShader | null;
    fsModule: WebGLShader | null;
    shaderProgram: WebGLProgram | null;

    constructor(gl: WebGL2RenderingContext, vertexShaderCode: string, fragmentShaderCode: string, name?: string) {
        this.name = name || "";
        this.vsModule = this.createShader(gl, gl.VERTEX_SHADER, vertexShaderCode);
        this.fsModule = this.createShader(gl, gl.FRAGMENT_SHADER, fragmentShaderCode);

        this.shaderProgram = this.createProgram(gl, this.vsModule!, this.fsModule!);
    }

    createShader(gl: WebGL2RenderingContext, type: number, source: string): WebGLShader | null {
        const shader = gl.createShader(type);
        if (!shader) return null;

        gl.shaderSource(shader, source);
        gl.compileShader(shader);

        if (gl.getShaderParameter(shader, gl.COMPILE_STATUS))
            return shader;
    
        console.log(gl.getShaderInfoLog(shader));
        gl.deleteShader(shader);
        return null;
    }

    createProgram(gl: WebGL2RenderingContext, vertexShader: WebGLShader, fragmentShader: WebGLShader): WebGLProgram | null {
        const program = gl.createProgram();
        if (!program) return null;

        gl.attachShader(program, vertexShader);
        gl.attachShader(program, fragmentShader);
        gl.linkProgram(program);

        if (gl.getProgramParameter(program, gl.LINK_STATUS))
            return program;
        
        console.log(gl.getProgramInfoLog(program));
        gl.deleteProgram(program);
        return null;
    }

    use(gl: WebGL2RenderingContext) {
        gl.useProgram(this.shaderProgram);
    }

    setVertexBufferPointer(gl: WebGL2RenderingContext, layout: number, size: number, type: number, normalize: boolean, stride: number, offset: number) {
        gl.enableVertexAttribArray(layout);
        gl.vertexAttribPointer(layout, size, type, normalize, stride, offset);
    }

    setVertexBufferPointer_Instancing(gl: WebGL2RenderingContext, layout: number, size: number, type: number, normalize: boolean, stride: number, offset: number, divisor=1) {
        gl.enableVertexAttribArray(layout);
        gl.vertexAttribPointer(layout, size, type, normalize, stride, offset);
        gl.vertexAttribDivisor(layout, divisor);
    }

    breakVertexBufferLink(gl: WebGL2RenderingContext, layout: number) {
        gl.disableVertexAttribArray(layout);
    }

    setFloat(gl: WebGL2RenderingContext, name: string, value: number) {
        const uniformLocation = gl.getUniformLocation(this.shaderProgram!, name);
        gl.uniform1f(uniformLocation, value);
    }

    setInt(gl: WebGL2RenderingContext, name: string, value: number) {
        const uniformLocation = gl.getUniformLocation(this.shaderProgram!, name);
        gl.uniform1i(uniformLocation, value);
    }

    setFloat2(gl: WebGL2RenderingContext, name: string, value1: number, value2: number) {
        const uniformLocation = gl.getUniformLocation(this.shaderProgram!, name);
        gl.uniform2f(uniformLocation, value1, value2);
    }

    setFloat3(gl: WebGL2RenderingContext, name: string, value1: number, value2: number, value3: number) {
        const uniformLocation = gl.getUniformLocation(this.shaderProgram!, name);
        gl.uniform3f(uniformLocation, value1, value2, value3);
    }

    setFloat4(gl: WebGL2RenderingContext, name: string, value1: number, value2: number, value3: number, value4: number) {
        const uniformLocation = gl.getUniformLocation(this.shaderProgram!, name);
        gl.uniform4f(uniformLocation, value1, value2, value3, value4);
    }

    setMat4(gl: WebGL2RenderingContext, name: string, matrix: number[] | Float32Array) {
        const uniformLocation = gl.getUniformLocation(this.shaderProgram!, name);
        gl.uniformMatrix4fv(uniformLocation, false, matrix);
    }
}