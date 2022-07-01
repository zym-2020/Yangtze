import { MutationTree } from 'vuex'
import { OtherState } from './otherState'

export type Mutations<S = OtherState> = {
    SET_DATA_SELECT(state: S, dataSelect: { id: string, name: string }): void
    SET_DATA_SELECTS(state: S, dataSelects: { id: string, name: string }[]): void

    SET_EDIT_STATE(state: S, jsonParam: {type: string, flag: boolean, state: string}): void

    SET_UPLOAD_LIST(state: S, uploadList: { id: string, name: string, state: number, progress: number }[]): void
    ADD_UPLOAD_LIST(state: S, upload: { id: string, name: string, state: number, progress: number }): void
    SET_UPLOAD_ITEM(state: S, uploadItem: { id: string, name: string, state: number, progress: number }): void
    REMOVE_UPLOAD_ITEM(state: S, index: number): void

    SET_WAIT_LIST(state: S, waitList: { id: string, name: string, state: number, file: File }[]):void
    ADD_WAIT_ITEM(state: S, waitItem: { id: string, name: string, state: number, file: File }): void
    SET_WAIT_ITEM(state: S, waitItem: { id: string, name: string, state: number, file: File }): void
    REMOVE_WAIT_ITEM(state: S, index: number): void

    SET_UPLOADED_LIST(state: S, uploadedList: {id: string, name: string, state: number}[]): void
    ADD_UPLOADED_ITEM(state: S, uploadedItem: {id: string, name: string, state: number}): void
    SET_UPLOADED_ITEM(state: S, uploadedItem: {id: string, name: string, state: number}): void
    REMOVE_UPLOADED_ITEM(state: S, index: number): void


    SET_UPLOAD_FLAG(state: S, flag: boolean): void

    SET_UPLOAD_DOT_FLAG(state: S, flag: boolean): void
}

export const otherMutations: MutationTree<OtherState> & Mutations = {
    SET_DATA_SELECT(state: OtherState, dataSelect: { id: string, name: string }) {
        state.dataSelect = dataSelect
    },
    SET_DATA_SELECTS(state: OtherState, dataSelects: { id: string, name: string }[]) {
        state.dataSelects = dataSelects
    },
    SET_EDIT_STATE(state: OtherState, jsonParam: {type: string, flag: boolean, state: string}) {
        state.editState.flag = jsonParam.flag
        state.editState.state = jsonParam.state
        state.editState.type = jsonParam.type
    },
    SET_UPLOAD_LIST(state: OtherState, uploadList: { id: string, name: string, state: number, progress: number }[]) {
        state.uploadList = uploadList
    },
    ADD_UPLOAD_LIST(state: OtherState, upload: { id: string, name: string, state: number, progress: number }) {
        state.uploadList.push(upload)
    },
    SET_UPLOAD_ITEM(state: OtherState, uploadItem: { id: string, name: string, state: number, progress: number }) {
        for (let i = 0; i < state.uploadList.length; i++) {
            if (state.uploadList[i].id === uploadItem.id) {
                state.uploadList[i] = uploadItem
                return
            }
        }
    },
    REMOVE_UPLOAD_ITEM(state: OtherState, index: number) {
        state.uploadList.splice(index, 1)
    },
    SET_WAIT_LIST(state: OtherState, waitList: { id: string, name: string, state: number, file: File }[]) {
        state.waitList = waitList
    },
    SET_WAIT_ITEM(state: OtherState, waitItem: { id: string, name: string, state: number, file: File }) {
        for (let i = 0; i < state.waitList.length; i++) {
            if (state.waitList[i].id === waitItem.id) {
                state.waitList[i] = waitItem
                return
            }
        }
    },
    ADD_WAIT_ITEM(state: OtherState, waitItem: { id: string, name: string, state: number, file: File }) {
        state.waitList.push(waitItem)
    },
    REMOVE_WAIT_ITEM(state: OtherState, index: number) {
        state.waitList.splice(index, 1)
    },
    SET_UPLOADED_LIST(state: OtherState, uploadedList: {id: string, name: string, state: number}[]) {
        state.uploadedList = uploadedList
    },
    ADD_UPLOADED_ITEM(state: OtherState, uploadedItem: {id: string, name: string, state: number}) {
        state.uploadedList.unshift(uploadedItem)
    },
    SET_UPLOADED_ITEM(state: OtherState, uploadedItem: {id: string, name: string, state: number}) {
        for(let i = 0; i < state.uploadedList.length; i++) {
            if(state.uploadedList[i].id === uploadedItem.id) {
                state.uploadedList[i] = uploadedItem
                return
            }
        }
    },
    REMOVE_UPLOADED_ITEM(state: OtherState, index: number) {
        state.uploadedList.splice(index, 1)
    },
    SET_UPLOAD_FLAG(state: OtherState, flag: boolean) {
        state.uploadFlag = flag
    },

    SET_UPLOAD_DOT_FLAG(state: OtherState, flag: boolean) {
        state.uploadDotFlag = flag
    }
}