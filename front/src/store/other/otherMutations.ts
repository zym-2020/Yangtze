import { MutationTree } from 'vuex'
import { OtherState } from './otherState'

export type Mutations<S = OtherState> = {

    ADD_UPLOADING(state: S, uploadItem: { id: string, name: string, state: number, size: string, progress: number }): void
    REMOVE_UPLOADING(state: S, id: string): void
    UPDATE_UPLOADING(state: S, param: { id: string, value: { name: string, state: number, size: string, progress: number } }): void

    SET_WAIT_LIST(state: S, waitList: { name: string, file: File, size: string }[]): void
    REMOVE_WIAT_ITEM(state: S, index: number): void
    ADD_WAIT_ITEM(state: S, waitItem: { name: string, file: File, size: string }): void

    SET_UPLOADED_LIST(state: S, uploadedList: { id: string, fileName: string, uploader: string, uploadTime: Date, size: string }[]): void
    ADD_UPLOADED_ITEM(state: S, uploadedItem: { id: string, name: string, time: Date, size: string }): void
    REMOVE_UPLOADED_ITEM(state: S, index: number): void

}

export const otherMutations: MutationTree<OtherState> & Mutations = {

    ADD_UPLOADING(state: OtherState, uploadItem: { id: string, name: string, state: number, size: string, progress: number }) {
        state.uploading[uploadItem.id] = {
            name: uploadItem.name,
            state: uploadItem.state,
            size: uploadItem.size,
            progress: uploadItem.progress
        }
    },
    REMOVE_UPLOADING(state: OtherState, id: string) {
        delete state.uploading[id]
    },

    UPDATE_UPLOADING(state: OtherState, param: { id: string, value: { name: string, state: number, size: string, progress: number } }) {
        state.uploading[param.id] = param.value
    },

    SET_WAIT_LIST(state: OtherState, waitList: { name: string, file: File, size: string }[]) {
        state.waitList = waitList
    },
    REMOVE_WIAT_ITEM(state: OtherState, index: number) {
        state.waitList.splice(index, 1)
    },
    ADD_WAIT_ITEM(state: OtherState, waitItem: { name: string, file: File, size: string }) {
        state.waitList.push(waitItem)
    },
    SET_UPLOADED_LIST(state: OtherState, uploadedList: { id: string, fileName: string, uploader: string, uploadTime: Date, size: string }[]) {
        uploadedList.forEach(item => {
            state.uploadedList.push({
                id: item.id,
                name: item.fileName,
                size: item.size,
                time: item.uploadTime
            })
        })
    },
    ADD_UPLOADED_ITEM(state: OtherState, uploadedItem: { id: string, name: string, time: Date, size: string }) {
        state.uploadedList.unshift(uploadedItem)
    },
    REMOVE_UPLOADED_ITEM(state: OtherState, index: number) {
        state.uploadedList.splice(index, 1)
    }
}