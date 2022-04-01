import { get, post, del, patch } from './axios'
import { RegisterJsonData, LoginJsonData } from './type/userType'

interface Result {
    code: number
    data: any
    msg: string,
    refreshToken: string | null
}

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
