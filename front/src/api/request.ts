import { get, post, del, patch } from './axios'
import {
    RegisterJsonData, LoginJsonData, ProjectJsonData, NewShapeJsonData, AddFileJsonData, RenameJsonData, UnPackJsonData, UpdateParentIdAndLevelJsonData, CompressFileJsonData, GetFlushIdJsonData, ComputeContourJsonData,
    SetUserInfoWithoutAvatarJsonData, FuzzyQueryClassifyJsonData, DeleteShareFileByIdJsonDaya, UpdateStatusByIdJsonData, GetNoUploadJsonData, AddRecordJsonData, AddMessageJsonData, DeleteFilesOrFolders, GetProjectsJsonData, Layer, SectionJsonData, SectionContrastJsonData
} from './type/userType'
import { Resource, Analyse } from '@/store/resourse/resourceState'

import axios from 'axios'
import { JsonObject } from 'type-fest'
import { idID } from 'naive-ui'

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



//========================download相关接口=================================
export async function getDownloadURLTemp(id: string) {
    return await get(`/download/getDownloadURL/${id}`)
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
    return await get(`/admin/message/QueryByType/${property}`, { responseType: 'arraybuffer' })
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

export async function QueryByUserType(property: string) {
    return await get(`/admin/message/QueryByUserType/${property}`)
}

export async function offlineMessage(property: string, dataUploadTime: string) {
    return await get(`/admin/message/offlineMessage/${property}/${dataUploadTime}`)
}

export async function offlineUserMessage(property: string, dataUploadTime: string) {
    return await get(`/admin/message/offlineUserMessage/${property}/${dataUploadTime}`)
}

export async function QueryHistoryMessage() {
    return await get(`/admin/message/QueryHistoryMessage`)
}

export async function QueryAllHistoryMessage() {
    return await get(`/admin/message/QueryAllHistoryMessage`)
}

export async function showMessageDetails(property: string) {
    return await get(`/admin/message/showMessageDetails/${property}`)
}

export async function responseMessage(response: string, id: string) {
    return await get(`/admin/message/responseMessage/${response}/${id}`)
}

export async function CountReply() {
    return await get(`/admin/message/CountReply`)
}

export async function CountUserReply() {
    return await get(`/admin/message/CountUserReply`)
}

export async function ZipEntryPath(path: string) {
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
    axios.get("http://localhost:8002/ship/QueryCode", { responseType: 'arraybuffer' }).then((res) => {

        const dataView = new DataView(res.data)
        // console.log("223",res.data)
        // console.log("123", dataView.byteLength, dataView.getInt32(4))
        return dataView
    })

}
//========================JsonRecord相关接口=================================

export async function QueryShpByName(name: string) {
    return await get(`/jsonRecord/QueryByName/${name}`)

}


export async function QueryShpByNewName(name: string) {
    return await get(`/jsonRecord/QueryByNewName/${name}`)

}

//========================ShpCoordinates相关接口=================================

export async function QueryCoordinatesByName(name: string) {
    return await get(`/share/QueryCoordinatesByName/${name}`)

}

//========================TideStation相关接口=================================

export async function QueryHeightByName(name: string) {
    return await get(`/share/QueryHeightByName/${name}`)

}

//========================DataRelational相关接口=================================
export async function addRelational(jsonData: { dataListId: string, fileIdList: string[] }) {
    return await post(`/relational/addRelational`, jsonData)
}

export async function updateRelational(jsonData: { dataListId: string, fileIdList: string[] }) {
    return await patch(`/relational/updateRelational`, jsonData)
}


//========================DataList相关接口=================================

export async function findFiles(dataListId: string) {
    return await get(`/dataList/findFiles/${dataListId}`)
}
//浏览量增加
export async function addWatchCount(id: string) {
    return await patch(`/dataList/addWatchCount/${id}`)
}


export async function updateStatusById(id: string, status: number) {
    return await patch(`/dataList/updateStatusById/${id}/${status}`)
}

export async function deleteAsMember(jsonData: { id: string, size: number, page: number }) {
    return await del(`/dataList/deleteAsMember`, jsonData)
}

export async function deleteByAdmin(jsonData: { page: number, size: number, keyword: string, tags: string[], property: string, flag: boolean, id: string }) {
    return await del(`/dataList/deleteByAdmin`, jsonData)
}

export async function fuzzyQueryDataList(jsonData: { page: number, size: number, keyword: string, tags: string[], property: string, flag: boolean }) {
    return await post(`/dataList/fuzzyQuery`, jsonData)
}

export async function getFileInfoAndUserInfo(id: string) {
    return await get(`/dataList/getFileInfoAndUserInfo/${id}`)
}

export async function fuzzyQueryAdmin(jsonData: { page: number, size: number, keyword: string, tags: string[], property: string, flag: boolean }) {
    return await post(`/dataList/fuzzyQueryAdmin`, jsonData)
}

export async function addDataList(formData: FormData) {
    return await post(`/dataList/addDataList`, formData)
}

export async function updateDataList(formData: FormData) {
    return await patch(`/dataList/updateDataList`, formData)
}

export async function pageQueryByEmail(jsonData: { page: number, size: number, keyword: string }) {
    return await post(`/dataList/pageQueryByEmail`, jsonData)
}

//============================Visual相关接口====================================
//获取用户头像，项目头像，缩略图等
export async function getAvatar(filename: string) {
    return await get(`/visual/getAvatar/${filename}`)
}

//获取图片
export async function getPhoto(fileId: string) {
    return await get(`/visual/getPhoto/${fileId}`)
}

//地图png可视化，包括两部分，获取坐标、获取png资源
export async function getCoordinates(visualId: string) {
    return await get(`/visual/getCoordinates/${visualId}`)
}

export async function getPngResource(visualId: string) {
    return await get(`/visual/getPngResource/${visualId}`)
}


export async function getRateDirection(id: string) {
    return await get(`/visual/getRateDirection/${id}`)
}

export async function getSandContent(id: string) {
    return await get(`/visual/getSandContent/${id}`)
}

export async function getSuspension(id: string) {
    return await get(`/visual/getSuspension/${id}`)
}

export async function getSalinity(id: string) {
    return await get(`/visual/getSalinity/${id}`)
}

export async function getFlowSand_Z(id: string) {
    return await get(`/visual/getFlowSand_Z/${id}`)
}

//========================folder相关接口=================================
export async function addFolder(jsonData: { folderName: string, parentId: string }) {
    return await post(`/folder/addFolder`, jsonData)
}

//============================NewFile相关接口====================================

//获取下载的url
export async function getDownloadURL(id: string) {
    return await get(`/file/getDownloadURL/${id}`)
}

export async function findByFolderId(folderId: string) {
    return await get(`/file/findByFolderId/${folderId}`)
}

//========================downloadHistory相关接口=================================
export async function pageQueryDownloadHistory(size: number, page: number, id: string) {
    return await get(`/downloadHistory/pageQuery/${id}/${size}/${page}`)
}

//========================browseHistory相关接口=================================
export async function getDataGroup(dataId: string, number: number) {
    return await get(`/browseHistory/getDataGroup/${dataId}/${number}`)
}

export async function addBrowseHistory(dataListId: string) {
    return await post(`/browseHistory/addHistory/${dataListId}`)
}
