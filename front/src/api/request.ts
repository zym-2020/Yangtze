import { get, post, del, patch } from './axios'
import {
    RegisterJsonData, LoginJsonData, ProjectJsonData, NewShapeJsonData, SectionJsonData, SectionContrastJsonData, AddFileJsonData, RenameJsonData, UnPackJsonData, UpdateParentIdAndLevelJsonData, CompressFileJsonData,
    SetUserInfoWithoutAvatarJsonData, FuzzyQueryClassifyJsonData, DeleteShareFileByIdJsonDaya, UpdateStatusByIdJsonData, GetNoUploadJsonData, AddRecordJsonData, AddMessageJsonData, DeleteFilesOrFolders
} from './type/userType'
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
export async function getNoUpload(jsonData: GetNoUploadJsonData) {
    return await post(`/file/getNoUpload`, jsonData)
}

export async function mergeFile(md5: string, uid: string) {
    return await post(`/file/mergeFile/${md5}/${uid}`)
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

export async function deleteFilesOrFolders(jsonData: DeleteFilesOrFolders) {
    return await del(`/file/deleteFilesOrFolders`, jsonData)
}

export async function unPack(jsonData: UnPackJsonData) {
    return await post(`/file/unPack`, jsonData)
}

export async function getTree() {
    return await get(`/file/getTree`)
}

export async function updateParentIdAndLevel(jsonData: UpdateParentIdAndLevelJsonData) {
    return await post(`/file/updateParentIdAndLevel`, jsonData)
}

export async function compressFile(jsonData: CompressFileJsonData) {
    return await post(`/file/compressFile`, jsonData)
}

//========================share_file相关接口=================================
export async function addShareFile(formData: FormData) {
    return await post(`/share/addShareFile`, formData)
}

export async function pageQueryByAdmin(jsonData: FuzzyQueryClassifyJsonData) {
    return await post(`/share/pageQueryByAdmin`, jsonData)
}

export async function getFileInfoAndMeta(id: string) {
    return await get(`/share/getFileInfoAndMeta/${id}`)
}

export async function getFileInfoAndMetaAndUserInfo(id: string) {
    return await get(`/share/getFileInfoAndMetaAndUserInfo/${id}`)
}

export async function addWatchCount(id: string) {
    return await patch(`/share/addWatchCount/${id}`)
}

export async function fuzzyQuery(jsonData: FuzzyQueryClassifyJsonData) {
    return await post(`/share/fuzzyQuery`, jsonData)
}

export async function fuzzyQueryClassify(jsonData: FuzzyQueryClassifyJsonData) {
    return await post(`/share/fuzzyQueryClassify`, jsonData)
}

export async function pageQueryByEmail(page: number, size: number) {
    return await get(`/share/pageQueryByEmail/${page}/${size}`)
}

//这里懒得写了，字段太多
export async function updateShareFileNoAvatar(jsonData: any) {
    return await patch(`/share/updateShareFileNoAvatar`, jsonData)
}

//这个也是一样，懒得写了
export async function updateShareFile(formData: FormData) {
    return await patch(`/share/updateShareFile`, formData)
}

export async function deleteShareFileById(jsonData: DeleteShareFileByIdJsonDaya) {
    return await del(`/share/deleteShareFileById`, jsonData)
}

export async function updateStatusById(jsonData: UpdateStatusByIdJsonData) {
    return await patch(`/share/updateStatusById`, jsonData)
}

export async function offlineById(id: string) {
    return await patch(`/share/offlineById/${id}`)
}

export async function deleteShareFileAsMember(id: string, page: number, size: number) {
    return await del(`/share/deleteShareFileAsMember/${id}/${page}/${size}`)
}

//========================fileMeta相关接口=================================

export async function getFileMetaAndUserInfo(id: string, email: string) {
    return await get(`/fileMeta/getFileMetaAndUserInfo/${id}/${email}`)
}


//========================download相关接口=================================
export async function getDownloadURL(id: string) {
    return await get(`/download/getDownloadURL/${id}`)
}

//========================downloadHistory相关接口=================================
export async function pageQueryDownloadHistory(size: number, page: number, id: string) {
    return await get(`/downloadHistory/pageQuery/${id}/${size}/${page}`)
}

//========================browseHistory相关接口=================================
export async function getDataGroup(dataId: string, number: number) {
    return await get(`/browseHistory/getDataGroup/${dataId}/${number}`)
}

//========================uploadRecord相关接口=================================
export async function getRecords(number: number) {
    return await get(`/uploadRecord/getRecords/${number}`)
}

export async function delRecord(id: string) {
    return await del(`/uploadRecord/delRecord/${id}`)
}

export async function addRecord(jsonData: AddRecordJsonData) {
    return await post(`/uploadRecord/addRecord`, jsonData)
}
//========================messagebox相关接口=================================
export async function pageQuerys(property: string, flag: boolean, page: number, size: number) {
    return await get(`/admin/message/pageQuerys/${property}/${flag}/${page}/${size}`)
}

export async function QueryByType(property: string) {
    return await get(`/admin/message/QueryByType/${property}`)
}

export async function QueryByUser(property: string, type: string) {
    return await get(`/admin/message/QueryByUser/${property}/${type}`)
}

export async function QueryByReceiver(property: string) {
    return await get(`/admin/message/QueryByReceiver/${property}`)
}

export async function addMessage(jsonData: AddMessageJsonData) {
    return await post(`/admin/message/addMessage`, jsonData)
}

export async function QueryByTime(property: string) {
    return await get(`/admin/message/QueryByTime/${property}`)
}