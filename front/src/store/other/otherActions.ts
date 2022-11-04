import { ActionTree, ActionContext } from 'vuex'
import { RootState } from '@/store'
import { Mutations } from './otherMutations'
import { OtherState } from './otherState'
import { useStore } from '../index'
import { createFileChunk, handlePostFiles, checkStatus } from '@/utils/file'
import { uuid } from '@/utils/common'
import { mergeFile, delRecord, getRecords } from '@/api/request'



type AugmentedActionContext = {
    ['commit']<K extends keyof Mutations>(key: K, payload: Parameters<Mutations[K]>[1]): ReturnType<Mutations[K]>
} & Omit<ActionContext<OtherState, RootState>, 'commit'>

export interface Actions {
    uploadFiles({ commit }: AugmentedActionContext, uploadInfo: { parentId: string }): void
    delUploadedItem({ commit }: AugmentedActionContext, id: string): Promise<void>
    initUploadedList({ commit }: AugmentedActionContext): Promise<void>
}

export const otherActions: ActionTree<OtherState, RootState> & Actions = {
    uploadFiles({ commit }: AugmentedActionContext, uploadInfo: { parentId: string }) {
        const store = useStore()
        async function handle() {
            if (store.state.other.waitList.length > 0) {
                const temp = store.state.other.waitList[0]
                const id = uuid()
                const fileChunk = createFileChunk(temp.file);
                const total = fileChunk.length
                const tempPath = uuid()
                store.commit("REMOVE_WIAT_ITEM", 0)
                store.commit("ADD_UPLOADING", { id: id, name: temp.name, size: temp.size, state: 1, progress: 0 })
                await handlePostFiles(fileChunk, tempPath, id);

                if (fileChunk.length === 0) {
                    const key = await mergeFile({ key: tempPath, total: total, name: temp.name, folderId: uploadInfo.parentId });
                    if (key != null && (key as any).code === 0) {
                        await checkStatus(key.data, id)
                    }
                }
                await handle()
            } else {
                return
            }
        }

        for (let i = 0; i < 5 - Object.keys(store.state.other.uploading).length; i++) {
            handle()
        }


    },

    async delUploadedItem({ commit }: AugmentedActionContext, id: string) {
        const store = useStore()
        const data = await delRecord(id)
        if (data != null && (data as any).code === 0) {
            for (let i = 0; i < store.state.other.uploadedList.length; i++) {
                if (store.state.other.uploadedList[i].id === id) {
                    store.commit("REMOVE_UPLOADED_ITEM", i)
                    return
                }
            }
        }
    },

    async initUploadedList({ commit }: AugmentedActionContext) {
        const data = await getRecords()
        if (data != null && (data as any).code === 0) {
            commit("SET_UPLOADED_LIST", data.data)
        }
    }
}