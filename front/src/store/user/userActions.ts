import { Mutations } from './userMutations'
import { ActionTree, ActionContext } from 'vuex'
import { UserState } from './userState'
import { RootState } from '@/store'
import { notice } from '@/utils/notice';
import { setToken, clear, clearRouter } from '@/utils/auth'
import { login, getUserInfoByToken } from '@/api/request'
import router from '@/router'

type AugmentedActionContext = {
    ['commit']<K extends keyof Mutations>(key: K, payload: Parameters<Mutations[K]>[1]): ReturnType<Mutations[K]>
} & Omit<ActionContext<UserState, RootState>, 'commit'>

export interface Actions {
    ['login']({ commit }: AugmentedActionContext, userInfo: { email: string, password: string }): void
    getUserInfo({ commit }: AugmentedActionContext): void
    logout({ commit }: AugmentedActionContext): void
}

export const userActions: ActionTree<UserState, RootState> & Actions = {
    async login({ commit }, userInfo) {
        let data = await login(userInfo) as any
        if (data != null) {
            if (data.code === -6) {
                notice('error', '登录失败', data.msg)
            } else if (data.code === -2) {
                notice('error', '登录失败', '邮箱不存在！')
            } else if (data.code === 0) {
                setToken(data.data)
                notice('success', '成功', '登录成功')
            } else {
                notice('error', '失败', '登录失败！')
            }
        }
    },
    async getUserInfo({ commit }) {
        let data = await getUserInfoByToken() as any
        if (data != null) {
            if (data.code === 0) {
                commit("SET_ID", data.data.id)
                commit("SET_EMAIL", data.data.email)
                commit("SET_NAME", data.data.name)
                commit("SET_ROLES", data.data.roles)
            } else {
                notice('error', '错误', '获取用户信息错误!')
            }
        }

    },

    logout({ commit }) {
        clear()
        commit("SET_ID", "")
        commit("SET_EMAIL", "")
        commit("SET_NAME", "")
        commit("SET_ROLES", [])
        clearRouter()
        
        router.push({ path: '/login' })
    }
}