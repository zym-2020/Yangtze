import { RouteLocationNormalized } from 'vue-router'
import { getProjectInfo, getFileInfoAndUserInfo, findFiles } from '@/api/request'
import { useStore } from '@/store'


const store = useStore()

export async function toIdPages(to: RouteLocationNormalized) {
    if (to.name === 'updateShare') {
        if (to.params.id != '' && to.params.id != null && to.params.id != undefined) {
            if (to.params.fileInfo === undefined) {
                const id = to.params.id
                const data = await getFileInfoAndUserInfo(id as string)
                const files = await findFiles(id as string)
                if (data != null && files != null) {
                    if ((data as any).code === 0 && (files as any).code === 0) {
                        to.params.fileInfo = data.data
                        to.params.files = files.data
                        return 1
                    } else {
                        return -1
                    }
                } else {
                    return 0
                }

            } else {
                const files = await findFiles(to.params.id as string)
                if (files != null) {
                    if ((files as any).code === 0) {
                        to.params.fileInfo = JSON.parse(to.params.fileInfo as string)
                        to.params.files = files.data
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
                if((to.params.projectInfo as any).creator != store.state.user.email && (to.params.projectInfo as any).isPublic === false) {
                    return -1
                }
                if ((to.params.projectInfo as any).creator === store.state.user.email) {
                    to.params.role = 'creator'
                } else {
                    to.params.role = 'member'
                }
                return 1
            } else {
                const data = await getProjectInfo(to.params.id as string)
                if (data != null) {
                    if ((data as any).code === 0) {
                        to.params.projectInfo = data.data
                        if((to.params.projectInfo as any).creator != store.state.user.email && (to.params.projectInfo as any).isPublic === false) {
                            return -1
                        }
                        if ((to.params.projectInfo as any).creator === store.state.user.email) {
                            to.params.role = 'creator'
                        } else {
                            to.params.role = 'member'
                        }
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