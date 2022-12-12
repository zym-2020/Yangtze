export interface OtherState {
    uploading: { [id: string]: { name: string, size: string, state: number, progress: number } }
    waitList: { name: string, file: File, size: string }[]
    uploadedList: { id: string, name: string, size: string, time: Date }[]

}

export const state: OtherState = {
    uploading: {},
    waitList: [],
    uploadedList: []

}