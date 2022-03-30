import { createStore } from 'vuex'
import { UserState } from './user/userState'
import { UserStore, store as user } from './user'
import { PermissionState } from './permission/permissionState'
import { PermissionStore, store as permission } from './permission'


export interface RootState {
  user: UserState,
  permission: PermissionState
}

export type Store = UserStore<Pick<RootState, 'user'>> & PermissionStore<Pick<RootState, 'permission'>>

export const store = createStore({
  modules: {
    user,
    permission
  }
})

export function useStore(): Store {
  return store as Store
}
