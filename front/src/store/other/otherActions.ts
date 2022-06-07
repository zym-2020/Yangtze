import { ActionTree, ActionContext } from 'vuex'
import { RootState } from '@/store'
import { Mutations } from './otherMutations'
import { OtherState } from './otherState'
import { useStore } from '../index'
import { getFileMd5, createFileChunk, handlePostFiles, checkStatus } from '@/utils/file'
import { getFileSize } from '@/utils/common'
import { notice } from '@/utils/notice'
import { getNoUpload, mergeFile, delRecord } from '@/api/request'

type AugmentedActionContext = {
    ['commit']<K extends keyof Mutations>(key: K, payload: Parameters<Mutations[K]>[1]): ReturnType<Mutations[K]>
} & Omit<ActionContext<OtherState, RootState>, 'commit'>

export interface Actions {
    uploadFiles({ commit }: AugmentedActionContext, uploadInfo: { level: number, parentId: string }): void
    delUploadedItem({ commit }: AugmentedActionContext, index: number): void
}

export const otherActions: ActionTree<OtherState, RootState> & Actions = {
    async uploadFiles({ commit }: AugmentedActionContext, uploadInfo: { level: number, parentId: string }) {
        const store = useStore()
        commit("SET_UPLOAD_FLAG", true)
        while (store.state.other.waitList.length > 0) {
            console.log(123456)
            if (store.state.other.uploadList.length < 5) {
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
                            const key = await mergeFile(md5, temp.id);
                            if (key != null && (key as any).code === 0) {
                                await checkStatus(key.data, temp.id)

                            } else {
                                notice("error", "失败", "文件合并时出错，请重新上传");
                            }
                        }
                    } else {
                        commit("SET_UPLOAD_ITEM", { id: temp.id, name: temp.name, state: -1, progress: 0 })
                        notice(
                            "warning",
                            "警告",
                            "上传初始化失败，请检查文件及相关描述是否出错"
                        );
                    }
                })
            }
        }
        commit("SET_UPLOAD_FLAG", false)
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