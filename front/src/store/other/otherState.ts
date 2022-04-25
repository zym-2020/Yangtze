export interface OtherState {
    dataSelect: {id: string, name: string},
    dataSelects: {id: string, name: string}[]
}

export const state: OtherState = {
    dataSelect: {id: '', name: ''},
    dataSelects: []
}