import { get, post, del, patch } from './axios'
import { RegisterJsonData, LoginJsonData, ProjectJsonData, ProjectJsonBean } from './type/userType'


//========================User相关接口=================================
export async function login(jsonData: LoginJsonData) {
    return await post(`/user/login`, jsonData)
}

export async function getUserInfoByToken() {
    return await get(`/user/getUserInfoByToken`)
}

export async function register(jsonData: RegisterJsonData){
    return await post(`/user/register`, jsonData)
}



//========================VectorTile相关接口=================================


//========================Project相关接口=================================
export async function addProject(jsonData: ProjectJsonData) {
    return await post(`/project/addProject`, jsonData)
}

export async function getProjectId() {
    return await get(`/project/getProjectId`)
}

export async function getResult(projectId: string) {
    return await get(`/project/getResult/${projectId}`)
}

export async function setResult(jsonData: ProjectJsonBean, id: number) {
    return await patch(`/project/setResult/${id}`, jsonData)
}

//========================vectorRelationship相关接口=================================
export async function vectorPageQuery(size: number, start: number) {
    return await get(`/vectorRelationship/pageQuery/${size}/${start}`)
}

//========================rasterRelationship相关接口=================================
export async function rasterPageQuery(size: number, start: number) {
    return await get(`/rasterRelationship/pageQuery/${size}/${start}`)
}