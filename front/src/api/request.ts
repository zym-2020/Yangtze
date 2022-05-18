import { get, post, del, patch } from './axios'
import { RegisterJsonData, LoginJsonData, ProjectJsonData, NewShapeJsonData, SectionJsonData, SectionContrastJsonData, MergeFileJsonData, AddFileJsonData, RenameJsonData, SetUserInfoWithoutAvatarJsonData, AddShareFileJsonData } from './type/userType'
import { ResourceState } from '@/store/resourse/resourceState'


//========================User相关接口=================================
export async function login(jsonData: LoginJsonData) {
    return await post(`/user/login`, jsonData)
}

export async function getUserInfoByToken() {
    return await get(`/user/getUserInfoByToken`)
}

export async function register(jsonData: RegisterJsonData) {
    return await post(`/user/register`, jsonData)
}

export async function getUserByEmail() {
    return await get(`/user/getUserByEmail`)
}

export async function setUserInfo(formData: FormData) {
    return await patch(`/user/setUserInfo`, formData)
}

export async function setUserInfoWithoutAvatar(jsonData: SetUserInfoWithoutAvatarJsonData) {
    return await patch(`/user/setUserInfoWithoutAvatar`, jsonData)
}

//========================VectorTile相关接口=================================


//========================Project相关接口=================================
export async function addProject(formData: FormData) {
    return await post(`/project/addProject`, formData)
}

export async function addProjectWithoutAvatar(jsonData: ProjectJsonData) {
    return await post(`/project/addProjectWithoutAvatar`, jsonData)
}

export async function getProjectsByEmail() {
    return await get(`/project/getProjectsByEmail`)
}

export async function getResult(projectId: string) {
    return await get(`/project/getResult/${projectId}`)
}

export async function setResult(jsonData: ResourceState, id: string) {
    return await patch(`/project/setResult/${id}`, jsonData)
}

export async function section(jsonData: SectionJsonData) {
    return await post(`/project/section`, jsonData)
}

export async function getSectionValue(projectName: string, sectionName: string, DEMName: string, DEMId: string) {
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

export async function getProjects(size: number, page: number) {
    return await get(`/project/getProjects/${size}/${page}`)
}

export async function findProjectById(projectId: string) {
    return await get(`/project/findProjectById/${projectId}`)
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

//========================file相关接口=================================
export async function getNoUpload(md5: string, total: number) {
    return await get(`/file/getNoUpload/${md5}/${total}`)
}

export async function mergeFile(jsonData: MergeFileJsonData) {
    return await post(`/file/mergeFile`, jsonData)
}

export async function checkMergeState(key: string) {
    return await get(`/file/checkMergeState/${key}`)
}

export async function findByLevel(level: number) {
    return await get(`/file/findByLevel/${level}`)
}

export async function findByParentId(parentId: string) {
    return await get(`/file/findByParentId/${parentId}`)
}

export async function addFile(jsonData: AddFileJsonData) {
    return await post(`/file/addFile`, jsonData)
}

export async function rename(jsonData: RenameJsonData) {
    return await patch(`/file/rename`, jsonData)
}

export async function deleteFile(id: string) {
    return await del(`/file/deleteFile/${id}`)
}

//========================share_file相关接口=================================
export async function addShareFile(jsonData: AddShareFileJsonData) {
    return await post(`/share/addShareFile`, jsonData)
}

export async function pageQueryOrderByDownload(page: number, size: number) {
    return await get(`/share/pageQueryOrderByDownload/${page}/${size}`)
}

export async function getFileInfoAndMeta(id: string) {
    return await get(`/share/getFileInfoAndMeta/${id}`)
}

export async function addWatchCount(id: string) {
    return await patch(`/share/addWatchCount/${id}`)
}

//========================fileMeta相关接口=================================
export async function getFileMetaById(id: string) {
    return await get(`/fileMeta/getFileMetaById/${id}`)
}