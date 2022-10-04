import { RouteLocationNormalized } from 'vue-router'
import { getProjectInfo, getFileInfoAndUserInfo } from '@/api/request'
import { useStore } from '@/store'
import axios from "axios"


const store = useStore()

export async function toIdPages(to: RouteLocationNormalized) {
    if (to.name === 'updateShare') {
        if (to.params.id != '' && to.params.id != null && to.params.id != undefined) {
            if (to.params.fileInfo === undefined) {
                const id = to.params.id
                const data = await getFileInfoAndUserInfo(id as string)
                if (data != null) {
                    if ((data as any).code === 0) {
                        to.params.fileInfo = data.data
                        return 1
                    } else {
                        return -1
                    }
                } else {
                    return 0
                }
            } else {
                to.params.fileInfo = JSON.parse(to.params.fileInfo as string)
                return 1
            }
        } else {
            return -1
        }

    } else if (to.name === 'shareFile') {
        if (to.params.id != '' && to.params.id != null && to.params.id != undefined) {
            if (to.params.fileInfo === undefined) {
                const id = to.params.id
                const data = await getFileInfoAndUserInfo(id as string)
                if (data != null) {
                    if ((data as any).code === 0) {
                        to.params.fileInfo = data.data
                        return 1
                    } else {
                        return -1
                    }
                }
                return 0
            } else {
                to.params.fileInfo = JSON.parse(to.params.fileInfo as string)
                return 1
            }

        } else {
            return -1
        }
    } else if (to.name === 'project') {
        if (to.params.id != '' && to.params.id != null && to.params.id != null) {
            if (to.params.projectInfo != undefined && to.params.projectInfo != null) {
                to.params.projectInfo = JSON.parse(to.params.projectInfo as string)
                return 1
            } else {
                const data = await getProjectInfo(to.params.id as string)
                if (data != null) {
                    if ((data as any).code === 0) {
                        to.params.projectInfo = data.data
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