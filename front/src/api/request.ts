import { get, post, del, patch } from './axios'
import { RegisterJsonData, LoginJsonData, ProjectJsonData } from './type/userType'


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