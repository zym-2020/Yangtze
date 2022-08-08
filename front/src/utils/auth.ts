import { resetRouter } from "@/router"
import { useStore } from "@/store"
import router from '@/router'



export function getToken(): string | null {
    return localStorage.getItem("zymtoken")
}

export function setToken(token: string): void {
    localStorage.setItem("zymtoken", token)
}

export function clear(): void {
    localStorage.clear()
}

export function hasPermission(roles: string[], routeRoles: string[]): boolean {
    for(let i = 0;i < roles.length;i++) {
        for(let j = 0;j < routeRoles.length;j++) {
            if(roles[i] === routeRoles[j]) {
                return true
            }
        }
    }
    return false
}


//清空路由
export function clearRouter() {
    const store = useStore()
    store.dispatch("clearRouters", undefined)
    resetRouter()
}

export function decrypt(ciphertext: string, userId: string) {
    let result = ""
    const key = "abcdef0123456789".split('')
    for(let i = 0;i < ciphertext.length; i = i + 2) {
        const a = key.findIndex(item => {
            return item === ciphertext[i]
        })
        const b = key.findIndex(item => {
            return item === ciphertext[i + 1]
        })
        result += String.fromCharCode(16 * b + a - userId.charCodeAt(i / 2))
    }
    return result
    
}