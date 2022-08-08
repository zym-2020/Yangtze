export interface CameraParameter {
    fov: number,
    typeName: string
}

export interface FirstPersonCameraParameter extends CameraParameter{
    position: Array<number>,
    target: Array<number>,
    near: number,
    far: number
}

export interface ThirdPersonCameraParameter extends CameraParameter {
    horizontalOffset: number,
    verticalOffset: number,
    pitch: number,
    yaw: number
}

export type AnyCameraParameter = 
    FirstPersonCameraParameter |
    ThirdPersonCameraParameter;

export interface CameraComponentRes {
    parameters: AnyCameraParameter
}