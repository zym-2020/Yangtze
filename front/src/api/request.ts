import { get, post, del, patch } from './axios'
import { RegisterJsonData, LoginJsonData, ProjectJsonData, NewShapeJsonData, SectionJsonData, SectionContrastJsonData } from './type/userType'
import { ResourceState } from '@/store/resourse/resourceState'


//========================User相关接口=================================
export async function login(jsonData: LoginJsonData) {
    return await post(`/user/login`, jsonData)
}

export async function getUserInfoByToken() {
    return await get(`/user/getUserInfoByToken`)
}

export async function register(jsonData: RegisterJsonData){
    return await post(`/user/register`, jsonData)
}



//========================VectorTile相关接口=================================


//========================Project相关接口=================================
export async function addProject(jsonData: ProjectJsonData) {
    return await post(`/project/addProject`, jsonData)
}

export async function getProjectId() {
    return await get(`/project/getProjectId`)
}

export async function getResult(projectId: string) {
    return await get(`/project/getResult/${projectId}`)
}

export async function setResult(jsonData: ResourceState, id: number) {
    return await patch(`/project/setResult/${id}`, jsonData)
}

export async function section(jsonData: SectionJsonData) {
    return await post(`/project/section`, jsonData)
}

export async function getSectionValue(projectName: string, sectionName: string, DEMName: string, DEMId: number) {
    return await get(`/project/getSectionValue/${projectName}/${sectionName}/${DEMName}/${DEMId}`)
}

export async function delSection(projectName: string, sectionName: string, DEMName: string) {
    return await del(`/project/delSection/${projectName}/${sectionName}/${DEMName}`)
}

export async function saveSectionContrastValue(jsonData: SectionContrastJsonData) {
    return await post(`/project/saveSectionContrastValue`, jsonData)
}

export async function getSectionContrastValue(projectName: string, sectionName: string) {
    return await get(`/project/getSectionContrastValue/${projectName}/${sectionName}`)
}

//========================vectorRelationship相关接口=================================
export async function vectorPageQuery(size: number, start: number) {
    return await get(`/vectorRelationship/pageQuery/${size}/${start}`)
}

export async function newShape(jsonData: NewShapeJsonData) {
    return await post(`/vectorRelationship/newShape`, jsonData)
}

export async function checkState(uuid: string) {
    return await get(`/vectorRelationship/checkState/${uuid}`)
}

//========================rasterRelationship相关接口=================================
export async function rasterPageQuery(size: number, start: number) {
    return await get(`/rasterRelationship/pageQuery/${size}/${start}`)
}