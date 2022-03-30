import { Mutations } from './userMutations'
import { ActionTree, ActionContext } from 'vuex'
import { UserState } from './userState'
import { RootState } from '@/store'
import { ElNotification } from 'element-plus'
import { setToken, clear } from '@/utils/auth'
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
        try {
            let data = await login(userInfo)
            setToken(data.data)
            ElNotification({
                type: 'success',
                title: '成功',
                message: '登录成功'
            })
        } catch {
            ElNotification({
                type: 'error',
                title: '失败',
                message: '登录失败'
            })
        }
    },
    async getUserInfo({ commit }) {
        let data = await getUserInfoByToken()
        commit("SET_EMAIL", data.data.email)
        commit("SET_NAME", data.data.name)
        commit("SET_ROLES", data.data.roles)
    },

    logout({ commit }) {
        clear()
        commit("SET_EMAIL", "")
        commit("SET_NAME", "")
        commit("SET_ROLES", [])
        router.push({ path: '/login' })
    }
}