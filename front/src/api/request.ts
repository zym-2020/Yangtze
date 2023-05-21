import { get, post, del, patch } from "./axios";
import {
  RegisterJsonData,
  LoginJsonData,
  AddRecordJsonData,
  DeleteFilesOrFolders,
} from "./type/userType";

import { GenerateConfigJsonData } from "@/type";

import axios from "axios";

//========================User相关接口=================================
export async function login(jsonData: LoginJsonData) {
  return await post(`/user/login`, jsonData);
}

export async function getUserInfo() {
  return await get(`/user/getUserInfo`);
}

export async function register(jsonData: RegisterJsonData) {
  return await post(`/user/register`, jsonData);
}

export async function getUserByEmail() {
  return await get(`/user/getUserByEmail`);
}

export async function setUserInfo(formData: FormData) {
  return await patch(`/user/setUserInfo`, formData);
}

export async function getResourceCount() {
  return await get(`/user/getResourceCount`);
}

export async function getAllUserInfo(
  page: number,
  size: number,
  keyword: string
) {
  if (keyword === "") {
    return await get(`/user/getAllUserInfo/${page}/${size}`);
  } else {
    return await get(`/user/getAllUserInfo/${page}/${size}/${keyword}`);
  }
}

export async function resetPassword(jsonData: {
  id: string;
  password: string;
}) {
  return await patch(`/user/resetPassword`, jsonData);
}

export async function deleteUser(id: string) {
  return await del(`/user/delete/${id}`);
}

export async function batchDelete(ids: string[]) {
  return await del(`/user/batchDelete`, ids);
}

export async function adminAddUser(formData: FormData) {
  return await post(`/user/adminAddUser`, formData);
}
//========================share_file相关接口=================================

export async function offlineById(id: string) {
  return await patch(`/share/offlineById/${id}`);
}
export async function onlineById(id: string) {
  return await patch(`/share/onlineById/${id}`);
}

//========================uploadRecord相关接口=================================
export async function getRecords() {
  return await get(`/uploadRecord/getRecords`);
}

export async function delRecord(id: string) {
  return await del(`/uploadRecord/delRecord/${id}`);
}

export async function addRecord(jsonData: AddRecordJsonData) {
  return await post(`/uploadRecord/addRecord`, jsonData);
}

export async function delAll() {
  return await del(`/uploadRecord/delAll`);
}

//========================analyticDataSet相关接口=================================
export async function findDataByType(type: string) {
  return await get(`/analyticDataSet/findDataByType/${type}`);
}

export async function getAnalyticData(projectId: string) {
  return await get(`/analyticDataSet/getAnalyticData/${projectId}`);
}

export async function addDraw(jsonData: {
  geoJson: any;
  projectId: string;
  fileName: string;
  visualType: string;
}) {
  return await post(`/analyticDataSet/addDraw`, jsonData);
}

export async function delAnalyticData(id: string) {
  return await del(`/analyticDataSet/delAnalyticData/${id}`);
}

export async function addSection(jsonData: {
  projectId: string;
  sectionId: string;
  demId: string;
  fileName: string;
}) {
  return await post(`/analyticDataSet/addSection`, jsonData);
}

export async function addSectionCompare(jsonData: {
  projectId: string;
  sectionId: string;
  demList: string[];
  fileName: string;
}) {
  return await post(`/analyticDataSet/addSectionCompare`, jsonData);
}

export async function addSectionFlush(jsonData: {
  projectId: string;
  sectionId: string;
  benchmarkId: string;
  referId: string;
  fileName: string;
}) {
  return await post(`/analyticDataSet/addSectionFlush`, jsonData);
}

export async function addRegionFlush(jsonData: {
  projectId: string;
  regionId: string;
  benchmarkId: string;
  referId: string;
  fileName: string;
}) {
  return await post(`/analyticDataSet/addRegionFlush`, jsonData);
}

export async function addElevationFlush(jsonData: {
  projectId: string;
  benchmarkId: string;
  referId: string;
  fileName: string;
}) {
  return await post(`/analyticDataSet/addElevationFlush`, jsonData);
}

export async function addFlushContour(jsonData: {
  projectId: string;
  benchmarkId: string;
  referId: string;
  fileName: string;
}) {
  return await post(`/analyticDataSet/addFlushContour`, jsonData);
}

export async function addSlope(jsonData: {
  projectId: string;
  demId: string;
  fileName: string;
}) {
  return await post(`/analyticDataSet/addSlope`, jsonData);
}

export async function computeVolume(jsonData: {
  projectId: string;
  regionId: string;
  demId: string;
  deep: number;
  fileName: string;
}) {
  return await post(`/analyticDataSet/computeVolume`, jsonData);
}

export async function checkState(key: string) {
  return await get(`/analyticDataSet/checkState/${key}`);
}

export async function rename(jsonData: { id: string; name: string }) {
  return await patch(`/analyticDataSet/rename`, jsonData);
}

export async function getUrl(id: string) {
  return await get(`/analyticDataSet/getUrl/${id}`);
}

export async function getAllModels() {
  return await get(`/analyticDataSet/getAllModels`);
}

export async function getModelConfig(id: string) {
  return await get(`/analyticDataSet/getModelConfig/${id}`);
}

export async function generateConfig(jsonData: GenerateConfigJsonData) {
  return await post(`/analyticDataSet/generateConfig`, jsonData);
}

export async function predict(jsonDta: { projectId: string; config: string }) {
  return await post(`/analyticDataSet/predict`, jsonDta);
}

//========================analyticParameter相关接口=================================
export async function findByType(type: string) {
  return await get(`/analyse/findByType/${type}`);
}

//========================船讯网相关接口=================================
export async function getAreaShip(key: string, scode: string, xy: string) {
  return axios.get("http://api.shipxy.com/apicall/GetAreaShip", {
    params: {
      k: key,
      enc: 1,
      scode: scode,
      xy: xy,
    },
  });
}

export async function QueryCode() {
  axios
    .get("http://localhost:8002/ship/QueryCode", {
      responseType: "arraybuffer",
    })
    .then((res) => {
      const dataView = new DataView(res.data);
      // console.log("223",res.data)
      // console.log("123", dataView.byteLength, dataView.getInt32(4))
      return dataView;
    });
}
//========================JsonRecord相关接口=================================

export async function QueryShpByName(name: string) {
  return await get(`/jsonRecord/QueryByName/${name}`);
}

//========================ShpCoordinates相关接口=================================

export async function QueryCoordinatesByName(name: string) {
  return await get(`/share/QueryCoordinatesByName/${name}`);
}

//========================TideStation相关接口=================================

export async function QueryHeightByName(name: string) {
  return await get(`/share/QueryHeightByName/${name}`);
}

//========================DataRelational相关接口=================================
export async function addRelational(jsonData: {
  dataListId: string;
  fileIdList: string[];
}) {
  return await post(`/relational/addRelational`, jsonData);
}

export async function updateRelational(jsonData: {
  dataListId: string;
  fileIdList: string[];
}) {
  return await patch(`/relational/updateRelational`, jsonData);
}

//========================DataList相关接口=================================

export async function findFiles(dataListId: string) {
  return await get(`/dataList/findFiles/${dataListId}`);
}
//浏览量增加
export async function addWatchCount(id: string) {
  return await patch(`/dataList/addWatchCount/${id}`);
}

export async function updateStatusById(id: string, status: number) {
  return await patch(`/dataList/updateStatusById/${id}/${status}`);
}

export async function deleteAsMember(jsonData: {
  id: string;
  size: number;
  page: number;
}) {
  return await del(`/dataList/deleteAsMember`, jsonData);
}

export async function deleteByAdmin(jsonData: {
  page: number;
  size: number;
  keyword: string;
  property: string;
  flag: boolean;
  id: string;
  type: string;
  status: number;
}) {
  return await del(`/dataList/deleteByAdmin`, jsonData);
}

export async function fuzzyQueryDataList(jsonData: {
  page: number;
  size: number;
  titleKeyword: string;
  property: string;
  flag: boolean;
  type: string;
}) {
  return await post(`/dataList/fuzzyQuery`, jsonData);
}

export async function getFileInfoAndUserInfo(id: string) {
  return await get(`/dataList/getFileInfoAndUserInfo/${id}`);
}

export async function fuzzyQueryAdmin(jsonData: {
  page: number;
  size: number;
  titleKeyword: string;
  property: string;
  flag: boolean;
  type: string;
  status: number;
}) {
  return await post(`/dataList/fuzzyQueryAdmin`, jsonData);
}

export async function addDataList(formData: FormData) {
  return await post(`/dataList/addDataList`, formData);
}

export async function updateDataList(formData: FormData) {
  return await patch(`/dataList/updateDataList`, formData);
}

export async function pageQueryByEmail(jsonData: {
  page: number;
  size: number;
  keyword: string;
  type: string;
  property: string;
}) {
  return await post(`/dataList/pageQueryByEmail`, jsonData);
}
export async function clearQueryDataList(jsonData: {
  tags: string[];
  type: string;
  location: string;
  startDate: string;
  endDate: string;
}) {
  return await post(`/dataList/clearQuery`, jsonData);
}

export async function getSimilarData(
  type: string,
  id: string,
  size: number,
  page: number
) {
  return await get(`/dataList/getSimilarData/${type}/${id}/${size}/${page}`);
}

export async function getHot(size: number) {
  return await get(`/dataList/getHot/${size}`);
}

export async function getDownloadURLDataList(id: string) {
  return await get(`/dataList/getDownloadURL/${id}`);
}

//============================Visual相关接口====================================
//获取用户头像，项目头像，缩略图等
export async function getAvatar(filename: string) {
  return await get(`/visual/getAvatar/${filename}`);
}

//获取图片
export async function getPhoto(fileId: string) {
  return await get(`/visual/getPhoto/${fileId}`);
}

//地图png可视化，包括两部分，获取坐标、获取png资源
export async function getCoordinates(visualId: string) {
  return await get(`/visual/getCoordinates/${visualId}`);
}

export async function getPngResource(visualId: string) {
  return await get(`/visual/getPngResource/${visualId}`);
}

export async function getContent(visualId: string) {
  return await get(`/visual/getContent/${visualId}`);
}

export async function getRateDirection(id: string) {
  return await get(`/visual/getRateDirection/${id}`);
}

export async function getSandContent(id: string) {
  return await get(`/visual/getSandContent/${id}`);
}

export async function getSuspension(id: string) {
  return await get(`/visual/getSuspension/${id}`);
}

export async function getSalinity(id: string) {
  return await get(`/visual/getSalinity/${id}`);
}

export async function getFlowSand_Z(id: string) {
  return await get(`/visual/getFlowSand_Z/${id}`);
}

export async function getGeoJson(fileId: string) {
  return await get(`/visual/getGeoJson/${fileId}`);
}

export async function getAnalyticGeoJson(fileId: string) {
  return await get(`/visual/getAnalyticGeoJson/${fileId}`);
}

export async function getSection(fileId: string) {
  return await get(`/visual/getSection/${fileId}`);
}

export async function getSectionContrast(fileId: string) {
  return await get(`/visual/getSectionContrast/${fileId}`);
}

export async function getSectionFlush(fileId: string) {
  return await get(`/visual/getSectionFlush/${fileId}`);
}

export async function getVolume(fileId: string) {
  return await get(`/visual/getVolume/${fileId}`);
}

export async function uploadParts(
  uid: string,
  number: number,
  formData: FormData
) {
  return await post(`/visual/uploadParts/${uid}/${number}`, formData);
}

export async function mergeParts(
  uid: string,
  total: number,
  type: string,
  name: string
) {
  return await post(`/visual/mergeParts/${uid}/${total}/${type}/${name}`);
}

//========================folder相关接口=================================
export async function addFolder(jsonData: {
  folderName: string;
  parentId: string;
}) {
  return await post(`/folder/addFolder`, jsonData);
}

//============================NewFile相关接口====================================

//获取下载的url
export async function getDownloadURL(id: string) {
  return await get(`/file/getDownloadURL/${id}`);
}

export async function findByFolderId(folderId: string) {
  return await get(`/file/findByFolderId/${folderId}`);
}

export async function uploadFile(formData: FormData) {
  return await post(`/file/uploadFile`, formData);
}

export async function mergeFile(jsonData: {
  key: string;
  total: number;
  name: string;
  folderId: string;
}) {
  return await post(`/file/mergeFile`, jsonData);
}

export async function checkMergeStateTemp(key: string) {
  return await get(`/file/checkMergeState/${key}`);
}
export async function deleteFilesOrFolders(jsonData: DeleteFilesOrFolders) {
  return await del(`/file/deleteFilesOrFolders`, jsonData);
}

export async function bindVisualData(jsonData: {
  id: string;
  fileName: string;
  type: string;
  srid: string;
  coordinates: number[][];
  view: {
    zoom: number;
    center: number[];
  } | null;
}) {
  return await post(`/file/bindVisualData`, jsonData);
}

export async function cancelVisualBind(id: string) {
  return await del(`/file/cancelVisualBind/${id}`);
}

export async function getVisualAuditFiles(
  keyword: string,
  page: number,
  size: number
) {
  return await get(`/file/getVisualAuditFiles/${page}/${size}/${keyword}`);
}

export async function changeFileVisualState(id: string, state: number) {
  return await post(`/file/changeFileVisualState/${id}/${state}`);
}

export async function getView(visualId: string) {
  return await get(`/file/getView/${visualId}`);
}

//========================downloadHistory相关接口=================================
export async function pageQueryDownloadHistory(
  size: number,
  page: number,
  id: string
) {
  return await get(`/downloadHistory/pageQuery/${id}/${size}/${page}`);
}

//========================browseHistory相关接口=================================
export async function getDataGroup(dataId: string, number: number) {
  return await get(`/browseHistory/getDataGroup/${dataId}/${number}`);
}

export async function addBrowseHistory(dataListId: string) {
  return await post(`/browseHistory/addHistory/${dataListId}`);
}

//========================new project相关接口=================================
export async function getAll(jsonData: {
  page: number;
  size: number;
  keyword: string;
}) {
  return await post(`/project/getAll`, jsonData);
}

export async function getProjectInfo(projectId: string) {
  return await get(`/project/getProjectInfo/${projectId}`);
}

export async function addProjectData(jsonData: {
  projectId: string;
  list: { fileId: string; dataListId: string }[];
}) {
  return await post(`/project/addData`, jsonData);
}

export async function getData(projectId: string) {
  return await get(`/project/getData/${projectId}`);
}

export async function delData(
  projectId: string,
  dataListId: string,
  fileId: string
) {
  return await del(`/project/delData/${projectId}/${dataListId}/${fileId}`);
}

export async function updateLayer(projectId: string, list: string[]) {
  return await post(`/project/updateLayer/${projectId}`, list);
}

export async function getLayersInfo(projectId: string) {
  return await get(`/project/getLayersInfo/${projectId}`);
}

export async function updateBasemap(jsonData: {
  projectId: string;
  url: string;
}) {
  return await patch(`/project/updateBasemap`, jsonData);
}

export async function updatePublicState(jsonData: {
  projectId: string;
  state: boolean;
}) {
  return await patch(`/project/updatePublicState`, jsonData);
}

export async function addProject(formData: FormData) {
  return await post(`/project/addProject`, formData);
}

export async function getAllByEmail(
  page: number,
  size: number,
  status: number
) {
  return await get(`/project/getAllByEmail/${page}/${size}/${status}`);
}

export async function getCount() {
  return await get(`/project/getCount`);
}

export async function updateProjectInfo(formData: FormData) {
  return await patch(`/project/updateProjectInfo`, formData);
}

export async function deleteProject(projectId: string) {
  return await del(`/project/deleteProject/${projectId}`);
}

export async function getAllByAdmin(jsonData: {
  keyword: string;
  page: number;
  size: number;
  status: number;
}) {
  return await post(`/project/getAllByAdmin`, jsonData);
}

export async function copyProject(formData: FormData) {
  return await post(`/project/copyProject`, formData);
}

// specialData相关接口
export async function addSpecialRecord(id: string) {
  return await post(`/special/addSpecialRecord/${id}`);
}

export async function delSpecialRecord(id: string) {
  return await del(`/special/delSpecialRecord/${id}`);
}

export async function getAllSpecialData() {
  return await get(`/special/getAllSpecialData`);
}

export async function getIdAndDataListName(size: number, start: number) {
  return await get(`/special/getIdAndDataListName/${size}/${start}`);
}

// 多源数据相关接口
export async function getBuoyByBox(
  top: number,
  right: number,
  bottom: number,
  left: number
) {
  return await get(
    `/multiSource/getBuoyByBox/${top}/${right}/${bottom}/${left}`
  );
}

export async function getShipInfoByBoxAndTime(
  top: number,
  right: number,
  bottom: number,
  left: number,
  startTime: string,
  endTime: string
) {
  return await get(
    `/multiSource/getShipInfoByBoxAndTime/${top}/${right}/${bottom}/${left}/${startTime}/${endTime}`,
    undefined,
    true
  );
}

export async function queryBoxShip(
  top: number,
  right: number,
  bottom: number,
  left: number
) {
  return await get(
    `/multiSource/queryBoxShip/${top}/${right}/${bottom}/${left}`
  );
}

export async function getAnchorInfoByBox(
  top: number,
  right: number,
  bottom: number,
  left: number
) {
  return await get(
    `/multiSource/getAnchorInfoByBox/${top}/${right}/${bottom}/${left}`
  );
}

export async function getParkInfoByBox(
  top: number,
  right: number,
  bottom: number,
  left: number
) {
  return await get(
    `/multiSource/getParkInfoByBox/${top}/${right}/${bottom}/${left}`
  );
}

export async function getAllBridgeInfo() {
  return await get(`/multiSource/getAllBridgeInfo`);
}

export async function getMeteorology() {
  return await get(`/multiSource/getMeteorology`);
}

export async function getStationByBox(
  top: number,
  right: number,
  bottom: number,
  left: number
) {
  return await get(
    `/multiSource/getStationByBox/${top}/${right}/${bottom}/${left}`
  );
}

export async function getWaterLevelByStationAndTime(
  type: string,
  station: string,
  startTime: string,
  endTime: string
) {
  return await get(
    `/multiSource/getWaterLevelByStationAndTime/${type}/${station}/${startTime}/${endTime}`
  );
}

export async function pageList(
  type: string,
  page: number,
  size: number,
  keyword: string
) {
  return await get(`/multiSource/pageList`, {
    type: type,
    page: page,
    size: size,
    keyword: keyword,
  });
}
