import { useStore } from '@/store'
import { Resource, Analyse } from '@/store/resourse/resourceState'
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import jsSHA from 'jssha';
import Identicon from 'identicon.js';


interface O {
  'M+': number
  'd+': number
  'h+': number
  'm+': number
  's+': number
  'q+': number
  S: number
}
export const dateFormat = (date: string, format?: string) => {
  let dateObj = new Date(Date.parse(date));
  let fmt = format || 'yyyy-MM-dd hh:mm:ss';
  //author: meizz
  var o: O = {
    'M+': dateObj.getMonth() + 1, //月份
    'd+': dateObj.getDate(), //日
    'h+': dateObj.getHours(), //小时
    'm+': dateObj.getMinutes(), //分
    's+': dateObj.getSeconds(), //秒
    'q+': Math.floor((dateObj.getMonth() + 3) / 3), //季度
    S: dateObj.getMilliseconds() //毫秒
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (dateObj.getFullYear() + '').substr(4 - RegExp.$1.length));
  for (var k in o) if (new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k as keyof O].toString() : ('00' + o[k as keyof O].toString()).substr(('' + o[k as keyof O].toString()).length));
  return fmt;
};

export const uuid = (len?: number, radix?: number) => {
  const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
  const uuid = []
  let i
  radix = radix || chars.length
  if (len) {
    for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix];
  } else {
    let r
    uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
    uuid[14] = '4';
    for (i = 0; i < 36; i++) {
      if (!uuid[i]) {
        r = 0 | Math.random() * 16;
        uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
      }
    }
  }
  return uuid.join('');
}

export const imgBase64 = (name: string) => {
  let shaObj = new jsSHA('SHA-512', 'TEXT');
  shaObj.update(name);
  var hash = shaObj.getHash('HEX');
  let data = new Identicon(hash, 280).toString();
  return 'data:image/png;base64,' + data;
};

export const generateColorByText = (text: string) => {
  let tmp = ""
  for (let i = 0; i < text.length; i++) {
    tmp += text.charCodeAt(i).toString(16)
  }
  if (tmp.length > 6) {
    tmp = tmp.substring(tmp.length - 6, tmp.length)
  } else if (tmp.length > 3) {
    tmp = tmp.substring(tmp.length - 3, tmp.length)
  } else {
    tmp = 'aquamarine'
  }
  return '#' + tmp
}

export function getLastOrNextFewDateBy(date: string, day: number) {

  function getNextDate(date: string, day: number) {
    var dd = new Date(date);
    dd.setDate(dd.getDate() + day);
    var y = dd.getFullYear();
    var m = dd.getMonth() + 1 < 10 ? "0" + (dd.getMonth() + 1) : dd.getMonth() + 1;
    var d = dd.getDate() < 10 ? "0" + dd.getDate() : dd.getDate();
    return y + "-" + m + "-" + d;
  };


  const timeList = []
  if (day < 0) {
    for (let i = 0; i > day; i--) {
      timeList.push(getNextDate(date, i))
    }
  } else {
    for (let i = 0; i < day; i++) {
      timeList.push(getNextDate(date, i))
    }
  }
  return timeList
}

export function getFileSize(fileByte: number) {
  let fileSizeByte = fileByte;
  let fileSizeMsg = "";
  if (fileSizeByte < 1048576) fileSizeMsg = (fileSizeByte / 1024).toFixed(2) + " KB";
  else if (fileSizeByte == 1048576) fileSizeMsg = "1 MB";
  else if (fileSizeByte > 1048576 && fileSizeByte < 1073741824) fileSizeMsg = (fileSizeByte / (1024 * 1024)).toFixed(2) + " MB";
  else if (fileSizeByte > 1048576 && fileSizeByte == 1073741824) fileSizeMsg = "1 GB";
  else if (fileSizeByte > 1073741824 && fileSizeByte < 1099511627776) fileSizeMsg = (fileSizeByte / (1024 * 1024 * 1024)).toFixed(2) + " GB";
  else fileSizeMsg = "文件超过1 TB";
  return fileSizeMsg;
}


interface Children {
  label: string;
  children: Children[];
  icon?: string;
  img?: string;
  type?: string;
  show?: boolean;
  id?: string
  selectDemId?: string
  selectDemName?: string
  selectDemIds?: string[]
  selectDemNames?: string[]
  nodeType?: string;
}
export const computedResource = (result: Children[]) => {
  const store = useStore()
  const getIcon = (type: string) => {
    if (type === "riverBed") {
      return "#icon-raster";
    } else if (type === "hydrology") {
      return "#icon-vector";
    } else if (type === 'satellite') {
      return "#icon-tiff"
    }
  };
  store.state.resource.layerDataList.forEach((item) => {
    result[0].children[0].children.push({
      label: item.name,
      type: item.type,
      show: item.show,
      children: [],
      id: item.id,
      icon: getIcon(item.type)
    })
  });
  result[0].children[1].children.push({
    label: store.state.resource.analyse.section.classify,
    children: [],
    img: '/river_bed_img/TinEditingTinLineAdd32.png'
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.sectionContrast.classify,
    children: [],
    img: "/river_bed_img/3DAnalystInterpolateLine32.png"
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.branch.classify,
    children: [],
    img: "/river_bed_img/MapSendBehind32.png"
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.area.classify,
    children: [],
    img: "/river_bed_img/LayerServiceMap32.png"
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.anyArea.classify,
    children: [],
    img: "/river_bed_img/3DAnalystInterpolatePolygon32.png"
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.elev.classify,
    children: [],
    img: "/river_bed_img/RasterImageAnalysisPanSharpen32.png"
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.deep.classify,
    children: [],
    img: "/river_bed_img/RasterImageAnalysisOrthoRectify32.png"
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.volume.classify,
    children: [],
    img: "/river_bed_img/MapPackageMPKFile32.png"
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.slope.classify,
    children: [],
    img: "/river_bed_img/Surface32.png"
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.line.classify,
    children: [],
    img: "/river_bed_img/SpatialAnalystContourTool32.png"
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.deepContrast.classify,
    children: [],
    img: "/river_bed_img/CadastralCreateLineString32.png"
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.boundary.classify,
    children: [],
    img: "/river_bed_img/3DAnalystCreateSteepestPath32.png"
  })

  store.state.resource.analyse.section.analysisResultList.forEach(item => {
    result[0].children[1].children[0].children.push({
      id: item.id,
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'section',
      selectDemId: item.selectDemId,
      selectDemName: item.selectDemName,
      img: "/river_bed_img/ElementLine16.png"
    })
  })
  store.state.resource.analyse.sectionContrast.analysisResultList.forEach(item => {
    result[0].children[1].children[1].children.push({
      id: item.id,
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'sectionContrast',
      selectDemIds: item.selectDemIds,
      selectDemNames: item.selectDemNames,
      img: "/river_bed_img/ElementLine16.png"
    })
  })
  store.state.resource.analyse.branch.analysisResultList.forEach(item => {
    result[0].children[1].children[2].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'branch',
      img: "/river_bed_img/ElementLine16.png"
    })
  })
  store.state.resource.analyse.area.analysisResultList.forEach(item => {
    result[0].children[1].children[3].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'area',
      img: "/river_bed_img/ElementLine16.png"
    })
  })
  store.state.resource.analyse.anyArea.analysisResultList.forEach(item => {
    result[0].children[1].children[4].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'anyArea',
      img: "/river_bed_img/ElementLine16.png"
    })
  })
  store.state.resource.analyse.elev.analysisResultList.forEach(item => {
    result[0].children[1].children[5].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'elev',
      img: "/river_bed_img/ElementLine16.png"
    })
  })
  store.state.resource.analyse.deep.analysisResultList.forEach(item => {
    result[0].children[1].children[6].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'deep',
      img: "/river_bed_img/ElementLine16.png"
    })
  })
  store.state.resource.analyse.volume.analysisResultList.forEach(item => {
    result[0].children[1].children[7].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'volume',
      img: "/river_bed_img/ElementLine16.png"
    })
  })
  store.state.resource.analyse.slope.analysisResultList.forEach(item => {
    result[0].children[1].children[8].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'slope',
      img: "/river_bed_img/ElementLine16.png"
    })
  })
  store.state.resource.analyse.line.analysisResultList.forEach(item => {
    result[0].children[1].children[9].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'line',
      img: "/river_bed_img/ElementLine16.png"
    })
  })
  store.state.resource.analyse.deepContrast.analysisResultList.forEach(item => {
    result[0].children[1].children[10].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'deepContrast',
      img: "/river_bed_img/ElementLine16.png"
    })
  })
  store.state.resource.analyse.boundary.analysisResultList.forEach(item => {
    result[0].children[1].children[11].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'boundary',
      img: "/river_bed_img/ElementLine16.png"
    })
  })

}

export const computedLayers = () => {
  const store = useStore()
  let result: any[] = []
  store.state.resource.analyse.section.analysisResultList.forEach(item => {
    if (item.show) {
      result.push({
        label: item.name,
        children: [],
        id: item.id,
        type: item.type,
        tableName: item.tableName,
        vectorType: item.vectorType,
        geoJson: item.geoJson,
      })
    }
  })
  store.state.resource.analyse.sectionContrast.analysisResultList.forEach(item => {
    if (item.show) {
      result.push({
        label: item.name,
        children: [],
        id: item.id,
        type: item.type,
        tableName: item.tableName,
        vectorType: item.vectorType,
        geoJson: item.geoJson
      })
    }
  })
  store.state.resource.analyse.branch.analysisResultList.forEach(item => {
    if (item.show) {
      result.push({
        label: item.name,
        children: [],
        id: item.id,
        type: item.type,
        tableName: item.tableName,
        vectorType: item.vectorType,
        geoJson: item.geoJson
      })
    }
  })
  store.state.resource.analyse.area.analysisResultList.forEach(item => {
    if (item.show) {
      result.push({
        label: item.name,
        children: [],
        id: item.id,
        type: item.type,
        tableName: item.tableName,
        vectorType: item.vectorType,
        geoJson: item.geoJson
      })
    }
  })
  store.state.resource.analyse.anyArea.analysisResultList.forEach(item => {
    if (item.show) {
      result.push({
        label: item.name,
        children: [],
        id: item.id,
        type: item.type,
        tableName: item.tableName,
        vectorType: item.vectorType,
        geoJson: item.geoJson
      })
    }
  })
  store.state.resource.analyse.elev.analysisResultList.forEach(item => {
    if (item.show) {
      result.push({
        label: item.name,
        children: [],
        id: item.id,
        type: item.type,
        tableName: item.tableName,
        vectorType: item.vectorType,
        geoJson: item.geoJson
      })
    }
  })
  store.state.resource.analyse.deep.analysisResultList.forEach(item => {
    if (item.show) {
      result.push({
        label: item.name,
        children: [],
        id: item.id,
        type: item.type,
        tableName: item.tableName,
        vectorType: item.vectorType,
        geoJson: item.geoJson
      })
    }
  })
  store.state.resource.analyse.volume.analysisResultList.forEach(item => {
    if (item.show) {
      result.push({
        label: item.name,
        children: [],
        id: item.id,
        type: item.type,
        tableName: item.tableName,
        vectorType: item.vectorType,
        geoJson: item.geoJson
      })
    }
  })
  store.state.resource.analyse.slope.analysisResultList.forEach(item => {
    if (item.show) {
      result.push({
        label: item.name,
        children: [],
        id: item.id,
        type: item.type,
        tableName: item.tableName,
        vectorType: item.vectorType,
        geoJson: item.geoJson
      })
    }
  })
  store.state.resource.analyse.line.analysisResultList.forEach(item => {
    if (item.show) {
      result.push({
        label: item.name,
        children: [],
        id: item.id,
        type: item.type,
        tableName: item.tableName,
        vectorType: item.vectorType,
        geoJson: item.geoJson
      })
    }
  })
  store.state.resource.analyse.deepContrast.analysisResultList.forEach(item => {
    if (item.show) {
      result.push({
        label: item.name,
        children: [],
        id: item.id,
        type: item.type,
        tableName: item.tableName,
        vectorType: item.vectorType,
        geoJson: item.geoJson
      })
    }
  })
  store.state.resource.analyse.boundary.analysisResultList.forEach(item => {
    if (item.show) {
      result.push({
        label: item.name,
        children: [],
        id: item.id,
        type: item.type,
        tableName: item.tableName,
        vectorType: item.vectorType,
        geoJson: item.geoJson
      })
    }
  })
  store.state.resource.layerDataList.forEach(item => {
    if (item.show) {
      result.push({
        label: item.name,
        children: [],
        id: item.id,
        type: item.type,
        tableName: item.tableName,
        vectorType: item.vectorType,
        geoJson: item.geoJson
      })
    }
  })

  if (store.state.resource.tempLayer.length > 0) {
    const temp: any[] = []
    store.state.resource.tempLayer.forEach(item => {
      for (let i = 0; i < result.length; i++) {
        if (item === result[i].id) {
          temp.push(result[i])
          break
        }
      }
    })
    result.forEach((item: any) => {
      let flag = true
      const length = temp.length
      for (let i = 0; i < length; i++) {
        if (item.id === temp[i].id) {
          flag = false
          break
        }
      }
      if (flag) {
        temp.unshift({
          label: item.label,
          children: [],
          id: item.id,
          type: item.type,
          tableName: item.tableName,
          vectorType: item.vectorType,
          geoJson: item.geoJson
        })
      }
    })
    result = temp
  }
  return result
}


export const mergeResource = () => {
  const store = useStore()

  const arr: Resource[] = JSON.parse(JSON.stringify(store.state.resource.layerDataList))
  store.state.resource.analyse.anyArea.analysisResultList.forEach(item => {
    arr.push(item)
  })
  store.state.resource.analyse.area.analysisResultList.forEach(item => {
    arr.push(item)
  })
  store.state.resource.analyse.boundary.analysisResultList.forEach(item => {
    arr.push(item)
  })
  store.state.resource.analyse.branch.analysisResultList.forEach(item => {
    arr.push(item)
  })
  store.state.resource.analyse.deep.analysisResultList.forEach(item => {
    arr.push(item)
  })
  store.state.resource.analyse.deepContrast.analysisResultList.forEach(item => {
    arr.push(item)
  })
  store.state.resource.analyse.elev.analysisResultList.forEach(item => {
    arr.push(item)
  })
  store.state.resource.analyse.line.analysisResultList.forEach(item => {
    arr.push(item)
  })
  store.state.resource.analyse.section.analysisResultList.forEach(item => {
    arr.push(item)
  })
  store.state.resource.analyse.sectionContrast.analysisResultList.forEach(item => {
    arr.push(item)
  })
  store.state.resource.analyse.slope.analysisResultList.forEach(item => {
    arr.push(item)
  })
  store.state.resource.analyse.volume.analysisResultList.forEach(item => {
    arr.push(item)
  })
  return arr
}
