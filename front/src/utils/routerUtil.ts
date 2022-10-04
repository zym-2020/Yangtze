import { RouteLocationNormalized } from 'vue-router'
import { getFileInfoAndMeta, getFileMetaAndUserInfo, getProjectInfo, getFileInfoAndMetaAndUserInfo } from '@/api/request'
import { useStore } from '@/store'
import { reduce } from 'lodash'
import axios from "axios"


const store = useStore()

export async function toIdPages(to: RouteLocationNormalized) {
    if (to.name === 'updateShare') {
        if (to.params.id != '' && to.params.id != null && to.params.id != undefined) {
            const id=to.params.id
            const data = await axios.get(`http://172.21.213.244:8002/dataList/getFileInfoAndUserInfo/${id}`,
            {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiYXZhdGFyIjoiIiwiZXhwIjoxNjY1MTk0NjQwLCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.DhIC267e5dRCBklRCVlUxDuD-tGNe3iNFlHCcchnjWwo-IiDHIUiffe9XnV9V7dwemKnSI4hNKALwt1tvVa0lg'}}).then((res)=>{
                console.log("updateShare",res.data)
            return res.data
            })
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
            return -1
        }

    } else if (to.name === 'shareFile') {
        if (to.params.id != '' && to.params.id != null && to.params.id != undefined) {
            const id=to.params.id
            const data = await axios.get(`http://172.21.213.244:8002/dataList/getFileInfoAndUserInfo/${id}`,
            {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiYXZhdGFyIjoiIiwiZXhwIjoxNjY1MTk0NjQwLCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.DhIC267e5dRCBklRCVlUxDuD-tGNe3iNFlHCcchnjWwo-IiDHIUiffe9XnV9V7dwemKnSI4hNKALwt1tvVa0lg'}}).then((res)=>{
                console.log("detail",res.data)
            return res.data
            })
            if (data != null) {
                if ((data as any).code === 0) {
                    if (store.state.user.email === data.data.creator) {
                        to.params.fileInfo = data.data
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