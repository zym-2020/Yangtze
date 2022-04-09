export function setCurrentProjectId(projectId: string) {
    localStorage.setItem("currentProjectId", projectId)
}

export function getCurrentProjectId(): string | null {
    return localStorage.getItem("currentProjectId")
}

export function setCurrentProjectName(projectName: string) {
    localStorage.setItem("currentProjectName", projectName)
}

export function getCurrentProjectName(): string | null {
    return localStorage.getItem("currentProjectName")
}