import { RouteLocationNormalized } from 'vue-router'
import { getFileInfoAndMeta, getFileMetaAndUserInfo, findProjectById, getFileInfoAndMetaAndUserInfo } from '@/api/request'
import { useStore } from '@/store'


const store = useStore()

export async function toIdPages(to: RouteLocationNormalized) {
    if (to.name === 'updateShare') {
        if (to.params.id != '' && to.params.id != null && to.params.id != undefined) {
            const data = await getFileInfoAndMeta(to.params.id as string)
            if (data != null) {
                if ((data as any).code === 0) {
                    to.params.fileInfo = data.data.fileInfo
                    to.params.fileMeta = data.data.fileMeta
                    return 1
                } else {
                    return -1
                }
            } else {
                return 0
            }
        } else {
            return -1
        }

    } else if (to.name === 'shareFile') {
        if (to.params.id != '' && to.params.id != null && to.params.id != undefined) {
            const data = await getFileInfoAndMetaAndUserInfo(to.params.id as string)
            if (data != null) {
                if ((data as any).code === 0) {
                    if (data.data.fileInfo.status === 1 || (data.data.fileInfo.status === -1 && store.state.user.email === data.data.fileInfo.creator)) {
                        to.params.fileInfo = data.data.fileInfo
                        to.params.fileMeta = data.data.fileMeta
                        return 1
                    } else {
                        return -1
                    }

                } else {
                    return -1
                }
            }
            return 0
        } else {
            return -1
        }
    } else if (to.name === 'project') {
        if (to.params.id != '' && to.params.id != null && to.params.id != null) {
            if (to.params.result != undefined && to.params.result != null) {
                return 1
            } else {
                const data = await findProjectById(to.params.id as string)
                if (data != null) {
                    if ((data as any).code === 0) {
                        to.params.name = data.data.project_name
                        to.params.result = data.data.result
                        return 1
                    } else {
                        return -1
                    }
                } else {
                    return 0
                }
            }
        } else {
            return -1
        }
    }
    else {
        return 1
    }
}