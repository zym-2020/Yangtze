import axios, { AxiosResponse } from "axios";
import mapboxgl from "mapbox-gl";

let baseStr = 'http://api.shipxy.com/apicall/GetAreaShip?k=9e42268de47b4f66a4f6c9f2a981fa0d&enc=1&scode=';

let reqStatusDic: {[key: number]: string} = {
    0:'成功', 1:'失败', 6:'Key 过期', 7:'key 被锁定', 9:'key 不存在', 12:'请求数据量过大，拒绝执行', 
    13:'服务器繁忙', 14:'请求来自非绑定域名', 15:'多船请求数量超过限制', 16:'区域船接口超出区域外', 
    29:'接口单位时间内请求频率太高，请稍后再尝试', 100:'接口单位时间内请求频率太高，请稍后再尝试'
}

let shipTypeDic: {[key: number]: string} = {

}

type Bbox = [number, number, number, number]; // left bottom right top

type singleShipData = {[key: string]: number|string};

type ShipData = { [key: number]: singleShipData };

let navStatusDic = ['在航', '锚泊', '失控', '操纵受限', '吃水受限', '靠泊', '搁浅', '捕捞作业', '靠帆船提供动力', '', '', '', '', '', '', '', '', '', '', '', '']

function BboxCheck(bbox:Bbox) {
    if(bbox[0]>=bbox[2] || bbox[1]>=bbox[3]) {
        return false;
    }
    return true;
}

function GenerateExtentStr(bbox: Bbox) {
    let extentStr = '&xy=';
    // 右上
    extentStr = extentStr + String(bbox[2]);
    extentStr = extentStr + ',';
    extentStr = extentStr + String(bbox[3]);
    extentStr = extentStr + '-';
    // 左上
    extentStr = extentStr + String(bbox[0]);
    extentStr = extentStr + ',';
    extentStr = extentStr + String(bbox[3]);
    extentStr = extentStr + '-';
    // 左下
    extentStr = extentStr + String(bbox[0]);
    extentStr = extentStr + ',';
    extentStr = extentStr + String(bbox[1]);
    extentStr = extentStr + '-';
    // 右下
    extentStr = extentStr + String(bbox[2]);
    extentStr = extentStr + ',';
    extentStr = extentStr + String(bbox[1]);

    return extentStr;
}

function GenerateReqStr(aPart: {extent: Bbox, sCode: number, extentStr: string}) {
    let reqStr = baseStr + String(aPart.sCode);
    reqStr = reqStr + aPart.extentStr;
    return reqStr;
}

export default class ShipProcessor {
    public fullExtent: Bbox; // full extent
    public reqParts: Array<{extent: Bbox, sCode: number, extentStr: string}>; // parts sliced for each request
    public shipData: ShipData; // ship data
    public geoJsonData: GeoJSON.FeatureCollection;


    constructor(extent: Bbox) {
        this.fullExtent = extent;
        this.reqParts = [];
        this.shipData = {};
        this.SliceExtent();
        this.geoJsonData = {
            type: 'FeatureCollection', 
            features: []
        };
    }

    // slice the full region into smaller ones
    private SliceExtent() {
        if(!BboxCheck(this.fullExtent)) { // bbox goes wrong
            console.log('Extent fault!! Please Check..');
            return;
        }
        let stY = this.fullExtent[3]; 
        let stX = this.fullExtent[0];
        for(stY ; stY >= this.fullExtent[1]; stY-=0.3) {
            stX = this.fullExtent[0];
            for(stX ; stX <= this.fullExtent[2]; stX+=0.3) {
                let endX = stX + 0.3;
                let endY = stY - 0.3;
                if(endX > this.fullExtent[2]) {
                    endX = this.fullExtent[2];
                }
                if(endY < this.fullExtent[1]) {
                    endY = this.fullExtent[1];
                }
                let aExtent = [Math.round(stX*1000000), Math.fround(endY*1000000), Math.fround(endX*1000000), Math.fround(stY*1000000)] as Bbox;
                this.reqParts.push({extent:aExtent, sCode:0, extentStr: GenerateExtentStr(aExtent)});
            }
        }
        console.log(this.reqParts);
    }

    // TODO: How to deal with the ship going out of the full region??
    private UpdateShipData(data: Array<singleShipData>) {
        for(let aShip of data) {
            let shipID = aShip['ShipID'] as number;
            this.shipData[shipID] = aShip;
        }
    }

    private UpdateGeoJson(data: Array<singleShipData>) {
        for(let aShip of data) {
            let lon = aShip['lon'] as number / 1000000;
            let lat = aShip['lat'] as number / 1000000;
            let aFeature: GeoJSON.Feature = {
                type: 'Feature', 
                geometry: {
                    type: 'Point', 
                    coordinates: [lon, lat]
                }, 
                properties: {}
            };
            delete aShip['lon'];
            delete aShip['lat'];
            aFeature.properties = aShip;
            this.geoJsonData.features.push(aFeature);
        }
    }

    // TODO: 1. Need to consider async? 
    //       2. request url only change once, consider saving it for rest request instead of generating it everytime
    public ReqShipData() {
        let that = this;
        let reqList: Promise<AxiosResponse<any, any>>[] = [];
        for(let aPart of this.reqParts) {
            let reqStr = GenerateReqStr(aPart);
            let axiosGet = axios.get(reqStr);
            // console.log(reqStr);
            reqList.push(axiosGet);
        }
        // console.log(reqList);
        let res = axios.all(reqList)
                .then(axios.spread(function(...args) {
                    Array.from([...args]).forEach((element, index) => {
                        let data = element.data;
                        if(data['status'] != 0) {
                            // console.log(data);
                            console.log(reqStatusDic[data['status']]);
                            return;
                        }
                        that.reqParts[index].sCode = data['scode'];
                        that.UpdateShipData(data.data);
                    })
                    return that.shipData;
                }));

        console.log('Request for SHIP DATA done.');
        return res;
    }

    public ReqGeoJsonData() {
        let that = this;
        let reqList: Promise<AxiosResponse<any, any>>[] = [];
        for(let aPart of this.reqParts) {
            let reqStr = GenerateReqStr(aPart);
            let axiosGet = axios.get(reqStr);
            // console.log(reqStr);
            reqList.push(axiosGet);
        }
        // console.log(reqList);
        let res = axios.all(reqList)
                .then(axios.spread(function(...args) {
                    Array.from([...args]).forEach((element, index) => {
                        let data = element.data;
                        if(data['status'] != 0) {
                            // console.log(data);
                            console.log(reqStatusDic[data['status']]);
                            return;
                        }
                        that.reqParts[index].sCode = data['scode'];
                        that.UpdateGeoJson(data.data);
                    })
                    return that.geoJsonData;
                }));

        console.log('Request for SHIP GeoJSON DATA done.');
        return res;
    }

    public Add2Map_GeoJSON(map: mapboxgl.Map) {
        map.loadImage(
            'http://172.21.212.10:8080/ship_icon.png', 
            (error, image) => {
                if(error) throw error;
                map.addImage('ship-marker', image as HTMLImageElement);
                map.addSource('ships', {
                    'type': 'geojson', 
                    'data': this.geoJsonData
                });
                map.addLayer({
                    'id': 'ships', 
                    'type': 'symbol', 
                    'source': 'ships', 
                    'layout': {
                        'icon-image': 'ship-marker', 
                        'icon-size': 0.5
                    }
                });
            }
        )
    }
}