import { ActionTree, ActionContext } from 'vuex'
import { RootState } from '@/store'
import { Mutations } from './otherMutations'
import { OtherState } from './otherState'
import { useStore } from '../index'
import { getFileMd5, createFileChunk, handlePostFiles, checkStatus } from '@/utils/file'
import { getFileSize } from '@/utils/common'
import { notice } from '@/utils/notice'
import { getNoUpload, mergeFile, delRecord, addRecord } from '@/api/request'

type AugmentedActionContext = {
    ['commit']<K extends keyof Mutations>(key: K, payload: Parameters<Mutations[K]>[1]): ReturnType<Mutations[K]>
} & Omit<ActionContext<OtherState, RootState>, 'commit'>

export interface Actions {
    uploadFiles({ commit }: AugmentedActionContext, uploadInfo: { level: number, parentId: string }): void
    delUploadedItem({ commit }: AugmentedActionContext, index: number): void
}

export const otherActions: ActionTree<OtherState, RootState> & Actions = {
    uploadFiles({ commit }: AugmentedActionContext, uploadInfo: { level: number, parentId: string }) {
        function handle() {
            const store = useStore()
            if (store.state.other.waitList.length > 0) {
                const temp = store.state.other.waitList[0]
                commit("REMOVE_WAIT_ITEM", 0)
                commit("ADD_UPLOAD_LIST", { id: temp.id, name: temp.name, state: 0, progress: 0 })
                getFileMd5(temp.file, async (md5: string) => {
                    const fileChunk = createFileChunk(temp.file);
                    const chunkList = await getNoUpload({
                        MD5: md5,
                        total: fileChunk.length,
                        meta: {
                            name: temp.name,
                            total: fileChunk.length,
                            level: uploadInfo.level,
                            parentId: uploadInfo.parentId,
                            meta: "",
                            size: getFileSize(temp.file.size)
                        },
                    });
                    if (chunkList != null && (chunkList as any).code === 0) {
                        commit("SET_UPLOAD_ITEM", { id: temp.id, name: temp.name, state: 2, progress: 0 })
                        await handlePostFiles(chunkList.data, fileChunk, md5, temp.id, temp.name);
                        if (chunkList.data.length === 0) {
                            console.log(chunkList.data.length)
                            const key = await mergeFile(md5, temp.id);
                            if (key != null) {
                                if ((key as any).code === 0) {
                                    const result = await checkStatus(key.data, temp.id, handle)
                                } else {
                                    console.log((key as any).code)
                                    notice("error", "失败", "文件合并时出错，请重新上传");
                                    for (let i = 0; i < store.state.other.uploadList.length; i++) {
                                        if (store.state.other.uploadList[i].id === temp.id) {
                                            store.commit("REMOVE_UPLOAD_ITEM", i)
                                            store.commit("ADD_UPLOADED_ITEM", { id: temp.id, name: temp.name, state: -1 })
                                            store.commit("SET_UPLOAD_DOT_FLAG", true)
                                            await addRecord({id: temp.id, fileName: temp.name, state: -1})
                                            break
                                        }
                                    }
                                    handle()
                                }

                            }
                        } else {
                            for (let i = 0; i < store.state.other.uploadList.length; i++) {
                                if (store.state.other.uploadList[i].id === temp.id) {
                                    store.commit("REMOVE_UPLOAD_ITEM", i)
                                    store.commit("ADD_UPLOADED_ITEM", { id: temp.id, name: temp.name, state: -1 })
                                    store.commit("SET_UPLOAD_DOT_FLAG", true)
                                    await addRecord({id: temp.id, fileName: temp.name, state: -1})
                                    break
                                }
                            }
                            handle()
                        }
                    } else {
                        for (let i = 0; i < store.state.other.uploadList.length; i++) {
                            if (store.state.other.uploadList[i].id === temp.id) {
                                store.commit("REMOVE_UPLOAD_ITEM", i)
                                store.commit("ADD_UPLOADED_ITEM", { id: temp.id, name: temp.name, state: -1 })
                                store.commit("SET_UPLOAD_DOT_FLAG", true)
                                await addRecord({id: temp.id, fileName: temp.name, state: -1})
                                break
                            }
                        }
                        handle()
                    }
                })
            } else {
                commit("SET_UPLOAD_FLAG", false)
            }
        }
        for (let i = 0; i < 5; i++) {
            handle()
        }


    },

    async delUploadedItem({ commit }: AugmentedActionContext, index: number) {
        const store = useStore()
        const data = await delRecord(store.state.other.uploadedList[index].id);
        if (data != null && (data as any).code === 0) {
            commit("REMOVE_UPLOADED_ITEM", index)
            notice("success", "成功", "删除成功")
        }
    }
}