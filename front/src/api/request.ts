import { get, post, del, patch } from './axios'
import { LoginJsonData } from './type/userType'

export async function login(jsonData: LoginJsonData) {
    return await post(`/user/login`, jsonData)
}

export async function getUserInfoByToken() {
    return await get(`/user/getUserInfoByToken`)
}