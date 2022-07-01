export interface OtherState {
    dataSelect: {id: string, name: string},
    dataSelects: {id: string, name: string}[],
    editState: {type: string, flag: boolean, state: string}
    uploadList: {id: string, name: string, state: number, progress: number}[]
    waitList: {id: string, name: string, state: number, file: File}[]
    uploadedList: {id: string, name: string, state: number}[]
    uploadFlag: boolean
    uploadDotFlag: boolean
}

export const state: OtherState = {
    dataSelect: {id: '', name: ''},
    dataSelects: [],
    editState: {type: "", flag: false, state: ""},
    uploadList: [],
    waitList: [],
    uploadedList: [],
    uploadFlag: false,
    uploadDotFlag: false
}