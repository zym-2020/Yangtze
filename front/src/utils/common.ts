import { useStore } from '@/store'
import { Resource, Analyse } from '@/store/resourse/resourceState'
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import jsSHA from 'jssha';
import Identicon from 'identicon.js';

const store = useStore()

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
  console.log(tmp)
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
  type?: string;
  show?: boolean;
  id?: string
  selectDemId?: string
  selectDemName?: string
  selectDemIds?: string[]
  selectDemNames?: string[]
  nodeType?: string
}
export const computedResource = (result: Children[]) => {
  store.state.resource.layerDataList.forEach((item) => {
    result[0].children[0].children.push({
      label: item.name,
      type: item.type,
      show: item.show,
      children: [],
      id: item.id
    })
  });
  result[0].children[1].children.push({
    label: store.state.resource.analyse.anyArea.classify,
    children: []
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.area.classify,
    children: []
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.boundary.classify,
    children: []
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.branch.classify,
    children: []
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.deep.classify,
    children: []
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.deepContrast.classify,
    children: []
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.elev.classify,
    children: []
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.line.classify,
    children: []
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.section.classify,
    children: []
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.sectionContrast.classify,
    children: []
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.slope.classify,
    children: []
  })
  result[0].children[1].children.push({
    label: store.state.resource.analyse.volume.classify,
    children: []
  })

  store.state.resource.analyse.anyArea.analysisResultList.forEach(item => {
    result[0].children[1].children[0].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'anyArea'
    })
  })
  store.state.resource.analyse.area.analysisResultList.forEach(item => {
    result[0].children[1].children[1].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'area'
    })
  })
  store.state.resource.analyse.boundary.analysisResultList.forEach(item => {
    result[0].children[1].children[2].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'boundary'
    })
  })
  store.state.resource.analyse.branch.analysisResultList.forEach(item => {
    result[0].children[1].children[3].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'branch'
    })
  })
  store.state.resource.analyse.deep.analysisResultList.forEach(item => {
    result[0].children[1].children[4].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'deep'
    })
  })
  store.state.resource.analyse.deepContrast.analysisResultList.forEach(item => {
    result[0].children[1].children[5].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'deepContrast'
    })
  })
  store.state.resource.analyse.elev.analysisResultList.forEach(item => {
    result[0].children[1].children[6].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'elev'
    })
  })
  store.state.resource.analyse.line.analysisResultList.forEach(item => {
    result[0].children[1].children[7].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'line'
    })
  })
  store.state.resource.analyse.section.analysisResultList.forEach(item => {
    result[0].children[1].children[8].children.push({
      id: item.id,
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'section',
      selectDemId: item.selectDemId,
      selectDemName: item.selectDemName
    })
  })
  store.state.resource.analyse.sectionContrast.analysisResultList.forEach(item => {
    result[0].children[1].children[9].children.push({
      id: item.id,
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'sectionContrast',
      selectDemIds: item.selectDemIds,
      selectDemNames: item.selectDemNames
    })
  })
  store.state.resource.analyse.slope.analysisResultList.forEach(item => {
    result[0].children[1].children[10].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'slope'
    })
  })
  store.state.resource.analyse.volume.analysisResultList.forEach(item => {
    result[0].children[1].children[11].children.push({
      label: item.name,
      children: [],
      type: item.type,
      show: item.show,
      nodeType: 'volume'
    })
  })
}

export const mergeResource = () => {
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

export const watchAnalyse = (map: mapBoxGl.Map, newVal: Analyse, oldVal: Analyse, addLayer: (resource: Resource) => void, delLayer: (type: string, id: string, show: boolean) => void, isLoaded: boolean) => {
  const sectionAdd = getAddArr(newVal.section.analysisResultList, oldVal.section.analysisResultList)
  const sectionDel = getDelArr(newVal.section.analysisResultList, oldVal.section.analysisResultList)
  console.log("map.sectiondel", sectionDel);
  sectionDel.forEach(item => {
    if (isLoaded) {
      delLayer(item.type, item.id as string, item.show as boolean)
    } else {
      map.on('load', () => {
        delLayer(item.type, item.id as string, item.show as boolean)
      })
    }

  })
  sectionAdd.forEach(item => {
    if (item.show && map.getLayer(item.type + item.id) === undefined) {
      if (isLoaded) {
        addLayer(item)
      } else {
        map.on('load', () => {
          addLayer(item)
        })
      }
    }
  })


  const sectionContrastAdd = getAddArr(newVal.sectionContrast.analysisResultList, oldVal.sectionContrast.analysisResultList)
  const sectionContrastDel = getDelArr(newVal.sectionContrast.analysisResultList, oldVal.sectionContrast.analysisResultList)
  sectionContrastAdd.forEach(item => {
    if (item.show && map.getLayer(item.type + item.id?.toString()) === undefined) {
      addLayer(item)
      // if (isLoaded) {
      //   addLayer(item)
      // } else {
      //   map.once('styledata', () => {
      //     addLayer(item)
      //   })
      // }
    }
  })
  sectionContrastDel.forEach(item => {
    delLayer(item.type, item.id as string, item.show as boolean)
  })

  const anyAreaAdd = getAddArr(newVal.anyArea.analysisResultList, oldVal.anyArea.analysisResultList)
  const anyAreaDel = getDelArr(newVal.anyArea.analysisResultList, oldVal.anyArea.analysisResultList)
  anyAreaAdd.forEach(item => {
    if (item.show && map.getLayer(item.type + item.id?.toString()) === undefined) {
      addLayer(item)
      // if (isLoaded) {
      //   addLayer(item)
      // } else {
      //   map.once('load', () => {
      //     addLayer(item)
      //   })
      // }
    }
  })
  anyAreaDel.forEach(item => {
    delLayer(item.type, item.id as string, item.show as boolean)
  })

  const areaAdd = getAddArr(newVal.area.analysisResultList, oldVal.area.analysisResultList)
  const areaDel = getDelArr(newVal.area.analysisResultList, oldVal.area.analysisResultList)
  areaAdd.forEach(item => {
    if (item.show && map.getLayer(item.type + item.id?.toString()) === undefined) {
      addLayer(item)
      // if (isLoaded) {
      //   addLayer(item)
      // } else {
      //   map.once('load', () => {
      //     addLayer(item)
      //   })
      // }
    }
  })
  areaDel.forEach(item => {
    delLayer(item.type, item.id as string, item.show as boolean)
  })

  const boundaryAdd = getAddArr(newVal.boundary.analysisResultList, oldVal.boundary.analysisResultList)
  const boundaryDel = getDelArr(newVal.boundary.analysisResultList, oldVal.boundary.analysisResultList)
  boundaryAdd.forEach(item => {
    if (item.show && map.getLayer(item.type + item.id?.toString()) === undefined) {
      addLayer(item)
      // if (isLoaded) {
      //   addLayer(item)
      // } else {
      //   map.once('load', () => {
      //     addLayer(item)
      //   })
      // }
    }
  })
  boundaryDel.forEach(item => {
    delLayer(item.type, item.id as string, item.show as boolean)
  })

  const branchAdd = getAddArr(newVal.branch.analysisResultList, oldVal.branch.analysisResultList)
  const branchDel = getDelArr(newVal.branch.analysisResultList, oldVal.branch.analysisResultList)
  branchAdd.forEach(item => {
    if (item.show && map.getLayer(item.type + item.id?.toString()) === undefined) {
      addLayer(item)
      // if (isLoaded) {
      //   addLayer(item)
      // } else {
      //   map.once('load', () => {
      //     addLayer(item)
      //   })
      // }
    }
  })
  branchDel.forEach(item => {
    delLayer(item.type, item.id as string, item.show as boolean)
  })

  const deepAdd = getAddArr(newVal.deep.analysisResultList, oldVal.deep.analysisResultList)
  const deepDel = getDelArr(newVal.deep.analysisResultList, oldVal.deep.analysisResultList)
  deepAdd.forEach(item => {
    if (item.show && map.getLayer(item.type + item.id?.toString()) === undefined) {
      addLayer(item)
      // if (isLoaded) {
      //   addLayer(item)
      // } else {
      //   map.once('load', () => {
      //     addLayer(item)
      //   })
      // }
    }
  })
  deepDel.forEach(item => {
    delLayer(item.type, item.id as string, item.show as boolean)
  })

  const deepContrastAdd = getAddArr(newVal.deepContrast.analysisResultList, oldVal.deepContrast.analysisResultList)
  const deepContrastDel = getDelArr(newVal.deepContrast.analysisResultList, oldVal.deepContrast.analysisResultList)
  deepContrastAdd.forEach(item => {
    if (item.show && map.getLayer(item.type + item.id?.toString()) === undefined) {
      addLayer(item)
      // if (isLoaded) {
      //   addLayer(item)
      // } else {
      //   map.once('load', () => {
      //     addLayer(item)
      //   })
      // }
    }
  })
  deepContrastDel.forEach(item => {
    delLayer(item.type, item.id as string, item.show as boolean)
  })

  const elevAdd = getAddArr(newVal.elev.analysisResultList, oldVal.elev.analysisResultList)
  const elevDel = getDelArr(newVal.elev.analysisResultList, oldVal.elev.analysisResultList)
  elevAdd.forEach(item => {
    if (item.show && map.getLayer(item.type + item.id?.toString()) === undefined) {
      addLayer(item)
      // if (isLoaded) {
      //   addLayer(item)
      // } else {
      //   map.once('load', () => {
      //     addLayer(item)
      //   })
      // }
    }
  })
  elevDel.forEach(item => {
    delLayer(item.type, item.id as string, item.show as boolean)
  })

  const lineAdd = getAddArr(newVal.line.analysisResultList, oldVal.line.analysisResultList)
  const lineDel = getDelArr(newVal.line.analysisResultList, oldVal.line.analysisResultList)
  lineAdd.forEach(item => {
    if (item.show && map.getLayer(item.type + item.id?.toString()) === undefined) {
      addLayer(item)
      // if (isLoaded) {
      //   addLayer(item)
      // } else {
      //   map.once('load', () => {
      //     addLayer(item)
      //   })
      // }
    }
  })
  lineDel.forEach(item => {
    delLayer(item.type, item.id as string, item.show as boolean)
  })

  const slopeAdd = getAddArr(newVal.slope.analysisResultList, oldVal.slope.analysisResultList)
  const slopeDel = getDelArr(newVal.slope.analysisResultList, oldVal.slope.analysisResultList)
  slopeAdd.forEach(item => {
    if (item.show && map.getLayer(item.type + item.id?.toString()) === undefined) {
      addLayer(item)
      // if (isLoaded) {
      //   addLayer(item)
      // } else {
      //   map.once('load', () => {
      //     addLayer(item)
      //   })
      // }
    }
  })
  slopeDel.forEach(item => {
    delLayer(item.type, item.id as string, item.show as boolean)
  })

  const volumeAdd = getAddArr(newVal.volume.analysisResultList, oldVal.volume.analysisResultList)
  const volumeDel = getDelArr(newVal.volume.analysisResultList, oldVal.volume.analysisResultList)
  volumeAdd.forEach(item => {
    if (item.show && map.getLayer(item.type + item.id?.toString()) === undefined) {
      addLayer(item)
      // if (isLoaded) {
      //   addLayer(item)
      // } else {
      //   map.once('load', () => {
      //     addLayer(item)
      //   })
      // }
    }
  })
  volumeDel.forEach(item => {
    delLayer(item.type, item.id as string, item.show as boolean)
  })
}
const getAddArr = (newArr: Resource[], oldRrr: Resource[]) => {
  const add: Resource[] = newArr.filter((item) => {
    let flag = true;
    for (let i = 0; i < oldRrr.length; i++) {
      if (item.id === oldRrr[i].id && item.type === oldRrr[i].type) {
        flag = false;
        break;
      }
    }
    if (flag) return item;
  });
  return add
}
const getDelArr = (newArr: Resource[], oldRrr: Resource[]) => {
  const del: Resource[] = oldRrr.filter((item) => {
    let flag = true;
    for (let i = 0; i < newArr.length; i++) {
      if (item.id === newArr[i].id && item.type === newArr[i].type) {
        flag = false;
        break;
      }
    }
    if (flag) return item;
  });
  return del
}