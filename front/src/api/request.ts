import { get, post, del, patch } from './axios'
import {
    RegisterJsonData, LoginJsonData, ProjectJsonData, NewShapeJsonData, AddFileJsonData, RenameJsonData, UnPackJsonData, UpdateParentIdAndLevelJsonData, CompressFileJsonData, GetFlushIdJsonData, ComputeContourJsonData,
    SetUserInfoWithoutAvatarJsonData, FuzzyQueryClassifyJsonData, DeleteShareFileByIdJsonDaya, UpdateStatusByIdJsonData, GetNoUploadJsonData, AddRecordJsonData, AddMessageJsonData, DeleteFilesOrFolders, GetProjectsJsonData, Layer, SectionJsonData, SectionContrastJsonData 
} from './type/userType'
import { Resource, Analyse } from '@/store/resourse/resourceState'

import axios from 'axios'
import { JsonObject } from 'type-fest'

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

export async function addProjectWithoutAvatar(jsonData: ProjectJsonData) {
    return await post(`/project/addProjectWithoutAvatar`, jsonData)
}

export async function addProjectWithAvatar(formData: FormData) {
    return await post(`/project/addProjectWithAvatar`, formData)
}

export async function getProjectsByEmail(email: string) {
    return await get(`/project/getProjectsByEmail/${email}`)
}

export async function addLayers(jsonData: { id: string, name: string, type: string, demSlopeId?: string }[], projectId: string) {
    return await patch(`/project/addLayers/${projectId}`, jsonData)
}

export async function addSection(layer: Layer, projectId: string) {
    return await post(`/project/addSection/${projectId}`, layer)
}

export async function getSectionValue(projectId: string, sectionId: string, valueIds: string[]) {
    return await post(`/project/getSectionValue/${projectId}/${sectionId}`, valueIds)
}

export async function delLayer(projectId: string, layerId: string) {
    return await del(`/project/delLayer/${projectId}/${layerId}`)
}


export async function addSectionContrast(projectId: string, layer: Layer) {
    return await post(`/project/addSectionContrast/${projectId}`, layer)
}

export async function getSectionContrast(projectId: string, layerId: string) {
    return await get(`/project/getSectionContrast/${projectId}/${layerId}`)
}

export async function getAll(jsonData: { page: number, size: number, keyWord: string }) {
    return await post(`/project/getAll`, jsonData)
}

export async function getProjectInfo(projectId: string) {
    return await get(`/project/getProjectInfo/${projectId}`)
}

export async function checkLayerState(projectId: string, sectionId: string) {
    return await get(`/project/checkState/${projectId}/${sectionId}`)
}

export async function getFlushId(jsonData: GetFlushIdJsonData) {
    return await post(`/project/getFlushId`, jsonData)
}

export async function computeContour(jsonData: ComputeContourJsonData) {
    return await post(`/project/computeContour`, jsonData)
}

export async function sortLayer(projectId: string, layers: any[]) {
    return await post(`/project/sortLayer/${projectId}`, layers)
}

export async function addRegion(projectId: string, demId: string, jsonArray: any[]) {
    return await post(`/project/addRegion/${projectId}/${demId}`, jsonArray)
}

export async function getRegionLayer(projectId: string, layerId: string) {
    return await get(`/project/getRegionLayer/${projectId}/${layerId}`)
}

export async function checkAddRegion(key: string) {
    return await get(`/project/checkAddRegion/${key}`)
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

export async function checkMergeStateTemp(key: string) {
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

export async function renameTemp(jsonData: RenameJsonData) {
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

export async function getFilePath(path: string) {
    return await get(`/file/getFilePath/${path}`)
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

export async function addWatchCountTemp(id: string) {
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

export async function updateStatusByIdTemp(jsonData: UpdateStatusByIdJsonData) {
    return await patch(`/share/updateStatusById`, jsonData)
}

export async function offlineById(id: string) {
    return await patch(`/share/offlineById/${id}`)
}

export async function examineById(id: string) {
    return await patch(`/share/examineById/${id}`)
}

export async function onlineById(id: string) {
    return await patch(`/share/onlineById/${id}`)
}

export async function deleteShareFileAsMember(id: string, page: number, size: number) {
    return await del(`/share/deleteShareFileAsMember/${id}/${page}/${size}`)
}

export async function getShareFileById(id: string) {
    return await patch(`/share/getShareFileById/${id}`)
}

export async function getOtherTags(id: string) {
    return await patch(`/share/getOtherTags/${id}`)
}


export async function getShpByCoordinates(arrayString: string) {
    return await get(`/share/getShpByCoordinates/${arrayString}`)
}



//========================fileMeta相关接口=================================

export async function getFileMetaAndUserInfo(id: string, email: string) {
    return await get(`/fileMeta/getFileMetaAndUserInfo/${id}/${email}`)
}

//========================download相关接口=================================
export async function getDownloadURLTemp(id: string) {
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
    return await get(`/admin/message/QueryByType/${property}`,{responseType:'arraybuffer'})
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


export async function QueryByUserEmail() {
    return await get(`/admin/message/QueryByUserEmail`)
}

export async function QueryByUserType(property:string) {
    return await get(`/admin/message/QueryByUserType/${property}`)
}

export async function offlineMessage(property:string,dataUploadTime:string) {
    return await get(`/admin/message/offlineMessage/${property}/${dataUploadTime}`)
}

export async function offlineUserMessage(property:string,dataUploadTime:string) {
    return await get(`/admin/message/offlineUserMessage/${property}/${dataUploadTime}`)
}

export async function QueryHistoryMessage() {
    return await get(`/admin/message/QueryHistoryMessage`)
}

export async function QueryAllHistoryMessage() {
    return await get(`/admin/message/QueryAllHistoryMessage`)
}

export async function showMessageDetails(property:string) {
    return await get(`/admin/message/showMessageDetails/${property}`)
}

export async function responseMessage(response:string,id:string) {
    return await get(`/admin/message/responseMessage/${response}/${id}`)
}

export async function CountReply() {
    return await get(`/admin/message/CountReply`)
}

export async function CountUserReply() {
    return await get(`/admin/message/CountUserReply`)
}

export async function ZipEntryPath(path:string){
    return await get(`/admin/message/ZipEntryPath/${path}`)
}

//========================analyticDataSet相关接口=================================
export async function findDataByType(type: string) {
    return await get(`/analyticDataSet/findDataByType/${type}`)
}


//========================船讯网相关接口=================================
export async function getAreaShip(key: string, scode: string, xy: string) {
    return axios.get("http://api.shipxy.com/apicall/GetAreaShip", {
        params: {
            k: key,
            enc: 1,
            scode: scode,
            xy: xy
        }
    })
}

export async function QueryCode() {
    axios.get("http://localhost:8002/ship/QueryCode", { responseType: 'arraybuffer'}).then((res) => {
        
        const dataView = new DataView(res.data)
        // console.log("223",res.data)
        // console.log("123", dataView.byteLength, dataView.getInt32(4))
        return dataView
    })

}
//========================JsonRecord相关接口=================================

export async function QueryShpByName(name:string){
    return await get(`/jsonRecord/QueryByName/${name}`)

}


export async function QueryShpByNewName(name:string){
    return await get(`/jsonRecord/QueryByNewName/${name}`)

}

//========================ShpCoordinates相关接口=================================

export async function QueryCoordinatesByName(name:string){
    return await get(`/share/QueryCoordinatesByName/${name}`)

}

//========================TideStation相关接口=================================

export async function QueryHeightByName(name:string){
    return await get(`/share/QueryHeightByName/${name}`)

}

//========================DataList相关接口=================================

export async function pagequery(dataListId:string,size:number,page:number){
    return await axios.get(`http://172.21.213.244:8002/downloadHistory/pageQuery/${dataListId}/${size}/${page}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        return res.data
    })

}



export async function findFiles(dataListId:string){
    return await axios.get(`http://172.21.213.244:8002/dataList/findFiles/${dataListId}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log("wenjian",res.data)
        return res.data
    })

}
//浏览量增加
export async function addWatchCount(id:string){
    return await axios.patch(`http://172.21.213.244:8002/dataList/addWatchCount/${id}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log("wenjian",res.data)
        return res.data
    })

}



export async function updateStatusById(id:string,status:number){
    return await axios.patch(`http://172.21.213.244:8002/dataList/updateStatusById/${id}/${status}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log("update",res.data)
        return res.data
    })

}
//========================admin接口========
export async function deleteByAdmin(formData:JsonObject){
    return await axios.delete(`http://172.21.213.244:8002/dataList/deleteByAdmin/`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log("delete",res.data)
        return res.data
    })

}


export async function deleteAsMember(formData:JsonObject){
    return await axios.post(`http://172.21.213.244:8002/dataList/deleteAsMember/`,formData,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log("wenjian",res.data)
        return res.data
    })

}

export async function fuzzyQueryAdmin(formData:JsonObject){
    return await axios.post(`http://172.21.213.244:8002/dataList/fuzzyQueryAdmin/`,formData,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        return res.data
    })

}

//========================DataRelational相关接口=================================
export async function addRelational(formData:any){
    return await axios.post(`http://172.21.213.244:8002/relational/addRelational/`,formData,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log("接口",res.data)
        return res.data
    })

}

//============================Visual相关接口====================================
//获取用户头像，项目头像，缩略图等
export async function getAvatar(filename:string){
    return await axios.get(`http://172.21.213.244:8002/visual/getAvatar/${filename}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })

}

//已切片栅格可视化
export async function getRaster(visualId:string){
    return await axios.get(`http://172.21.213.244:8002/visual/getAvatar/${visualId}/{x}/{y}/{z}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })

}

//pg入库的矢量文件可视化
export async function getVectorTiles(visualId:string){
    return await axios.get(`http://172.21.213.244:8002/visual/getVectorTiles/${visualId}/{x}/{y}/{z}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })

}

//获取图片
export async function getPhoto(fileId:string){
    return await axios.get(`http://172.21.213.244:8002/visual/getPhoto/${fileId}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log("图片",res.data)
        return res.data
    })

}

//地图png可视化，包括两部分，获取坐标、获取png资源
export async function getCoordinates(visualId:string){
    return await axios.get(`http://172.21.213.244:8002/visual/getCoordinates/${visualId}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })

}

export async function getPngResource(visualId:string){
    return await axios.get(`http://172.21.213.244:8002/visual/getPngResource/${visualId}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })

}

//方便离线插入数据
export async function addVisualFile(visualFile:JsonObject){
    return await axios.post(`http://172.21.213.244:8002/visual/addVisualFile/`,visualFile,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })

}

export async function getRateDirection(id:string){
    return await axios.get(`http://172.21.213.244:8002/visual/getRateDirection/${id}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })

}

export async function getSandContent(id:string){
    return await axios.get(`http://172.21.213.244:8002/visual/getSandContent/${id}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })

}


//============================NewFile相关接口====================================
//上传文件
export async function uploadFile(formData:JsonObject){
    return await axios.post(`http://172.21.213.244:8002/file/uploadFile/`,formData,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })

}

//合并文件
/////////////
export async function mergeFileTemp(formData:JsonObject){
    return await axios.post(`http://172.21.213.244:8002/file/mergeFile/`,formData,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })
}

//
export async function checkMergeState(key:string){
    return await axios.get(`http://172.21.213.244:8002/file/checkMergeState/${key}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })
}

//重命名文件
export async function rename(jsonData:JsonObject){
    return await axios.patch(`http://172.21.213.244:8002/file/rename`,jsonData,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })
}

//获取下载的url
export async function getDownloadURL(id:string){
    return await axios.get(`http://172.21.213.244:8002/file/getDownloadURL/{id}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })
}
//加密下载，下载条目下的文件
export async function downLoadInList(userId:string,id:string,dataListId:string){
    return await axios.get(`http://172.21.213.244:8002/file/downLoadInList/${userId}/${id}/${dataListId}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })
}
//单文件下载
export async function downloadLocalFile(userId:string,id:string){
    return await axios.get(`http://172.21.213.244:8002/file/downloadLocalFile/${userId}/${id}`,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw'}}).then((res)=>{
        console.log(res.data)
        return res.data
    })
}