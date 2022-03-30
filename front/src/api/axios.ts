import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';

import { ElNotification } from 'element-plus'
import { setToken, getToken } from '@/utils/auth';
import { useStore } from '@/store';


const store = useStore()

const axiosInstance: AxiosInstance = axios.create({
    baseURL: 'http://localhost:8080/Yangtze/',
    timeout: 200000
})

axiosInstance.interceptors.response.use(
    (response: AxiosResponse) => {
        if (response.data.code === -4 || response.data.code === -5) {
            store.dispatch("logout", undefined)
            return response.data
        }
        if (response.data.refreshToken != null && response.data.refreshToken != '') {
            setToken(response.data.refreshToken)
        }
        return response.data
    },
    (err: any) => {
        ElNotification({
            title: '错误',
            type: 'error',
            message: '请求错误'
        })
        console.log(err)
    }
)

axiosInstance.interceptors.request.use(
    (config: AxiosRequestConfig) => {
        const token = getToken()
        config.headers = {
            Authorization: `Bearer ${token}`
        }
        return config
    }
)


export const get = (url: string, params?: any) => {
    return axiosInstance({
        url: url,
        params: params,
        method: 'get'
    })
}

export const post = (url: string, data?: any) => {
    return axiosInstance({
        url: url,
        data: data,
        method: 'post'
    })
}

export const del = (url: string, data?: any) => {
    return axiosInstance({
        url: url,
        data: data,
        method: 'delete'
    })
}

export const patch = (url: string, data?: any) => {
    return axiosInstance({
        url: url,
        data: data,
        method: 'patch'
    })
}
