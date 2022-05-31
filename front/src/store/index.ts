import { createStore } from 'vuex'
import { UserState } from './user/userState'
import { UserStore, store as user } from './user'
import { PermissionState } from './permission/permissionState'
import { PermissionStore, store as permission } from './permission'
import { ResourceStore, store as resource } from './resourse'
import { ResourceState } from './resourse/resourceState'
import { OtherStore, store as other } from './other'
import { OtherState } from './other/otherState'


export interface RootState {
  user: UserState,
  permission: PermissionState,

  resource: ResourceState,
  other: OtherState
}

export type Store = UserStore<Pick<RootState, 'user'>> & PermissionStore<Pick<RootState, 'permission'>> & ResourceStore<Pick<RootState, 'resource'>> & OtherStore<Pick<RootState, 'other'>>

export const store = createStore({
  modules: {
    user,
    permission,
    resource,
    other
  }
})

export function useStore(): Store {
  return store as Store
}
