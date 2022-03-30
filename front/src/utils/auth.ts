
export function getToken(): string | null {
    return localStorage.getItem("zymtoken")
}

export function setToken(token: string): void {
    localStorage.setItem("zymtoken", token)
}

export function clear(): void {
    localStorage.clear()
}