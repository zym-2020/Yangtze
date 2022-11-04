import { createStore } from 'vuex'
import { UserState } from './user/userState'
import { UserStore, store as user } from './user'
import { PermissionState } from './permission/permissionState'
import { PermissionStore, store as permission } from './permission'

import { OtherStore, store as other } from './other'
import { OtherState } from './other/otherState'


export interface RootState {
  user: UserState,
  permission: PermissionState,


  other: OtherState
}

export type Store = UserStore<Pick<RootState, 'user'>> & PermissionStore<Pick<RootState, 'permission'>> & OtherStore<Pick<RootState, 'other'>>

export const store = createStore({
  modules: {
    user,
    permission,
    other
  }
})

export function useStore(): Store {
  return store as Store
}
