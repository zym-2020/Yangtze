<template>
  <!-- <div id="stats"></div> -->
  <div id="map"></div>
  <canvas id="deck" ref="deckMap"></canvas>
  <div id="layer-control" :class="{ active: controlActive}">
    <el-row id="layer-panel">
      <el-col id="layers-drop" :span="19">
        <el-collapse v-model="activeList" @change="LayerDropHandle">
          <el-collapse-item title="数据图层" name="layers">
            <el-checkbox-group v-model="showLayerList" @change="ToggleLayer">
              <el-checkbox label="AIS船舶" />
              <el-checkbox label="航标" />
              <el-checkbox label="停泊区" />
              <el-checkbox label="锚地" />
              <!-- <el-checkbox label="其他设施" /> -->
            </el-checkbox-group>
          </el-collapse-item>
        </el-collapse>
      </el-col>
      <el-col id="control-toggle" :span="5">
        <el-image 
          id="toggle-image"
          :src="toggleURL"
          fit="scale-down"
          @click="ToggleControl"
        />
      </el-col>
    </el-row>
  </div>
  <div id="ship-info" :class="{active: cardActive}">
    <el-card shadow="hover" >
      <template #header>
        <div class="card-header">
          <span class="ship-name">{{shipInfo.name}}</span>
          <br>
          <span class="ship-type">Cargo Ship</span>
          <el-icon id="hide-icon" size="1.6vw" @click="HideInfo()"><DArrowLeft /></el-icon>
        </div>
      </template>
      <img
          src="https://static.vesselfinder.net/images/cool-ship2@1.png"
          class="ship-image"
        />
        <el-descriptions
          direction="vertical"
          :column="4"
          size="large"
          border
        >
          <el-descriptions-item label="速度" label-align="center" align="center">{{shipInfo.speed}}</el-descriptions-item>
          <el-descriptions-item label="航向" label-align="center" align="center">{{shipInfo.rot}}</el-descriptions-item>
          <el-descriptions-item label="MMSI" :span="2" label-align="center" align="center">
            {{shipInfo.mmsi}}
          </el-descriptions-item>
          <!-- <el-descriptions-item label="吃水深度" :span="2" label-align="center" align="center">{{shipInfo.draught}}</el-descriptions-item> -->
          <!-- <el-descriptions-item label="登记时间" :span="2" label-align="center" align="center">
            {{shipInfo.registerTime}}
          </el-descriptions-item> -->
          <el-descriptions-item label="船长/宽" :span="1"  label-align="center" align="center">
            {{shipInfo.size}}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间" :span="3"  label-align="center" align="center">
            {{shipInfo.updateTime}}
          </el-descriptions-item>
        </el-descriptions>
    </el-card>
  </div>
  <div id="buoy-info" :style="buoyInfoStyle">
    <el-row :gutter="0" id="buoy-card">
      <el-col :span="8" :offset="0" id="buoy-img-box">
        <el-image 
          id="buoy-image"
          :src="buoyInfo.url"
          :preview-src-list="[buoyInfo.url]"
          :initial-index="0"
          fit="scale-down"
        />
      </el-col>
      <el-col :span="14" id="buoy-text">
        <el-descriptions
          direction="horizontal"
          :column="1"
          size="default"
          border
        >
          <el-descriptions-item label="名称" label-align="center" align="center">{{buoyInfo.name}}</el-descriptions-item>
          <el-descriptions-item label="颜色" label-align="center" align="center">{{buoyInfo.color}}</el-descriptions-item>
          <el-descriptions-item label="形状" label-align="center" align="center">
            {{buoyInfo.shape}}
          </el-descriptions-item>
          <el-descriptions-item label="所属航道" label-align="center" align="center">
            {{buoyInfo.belong}}
          </el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
    <el-row id="down-arrow">
      <el-col :span="24">
        <div class="down-arrow"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, Ref, ref } from 'vue';

export default defineComponent({
  name: 'App',
});
</script>

<script lang="ts" setup>
import axios from 'axios';
import mapboxgl from 'mapbox-gl';
import { Deck } from '@deck.gl/core/typed';
import { IconLayer, PolygonLayer } from '@deck.gl/layers/typed';
// import { GeoBoundingBox, TileLayer } from '@deck.gl/geo-layers/typed';
import { InfoStyle, PopupHelper, ViewState } from '../../utils/popupHelper';
import { MixLayer } from '../../utils/mixLayer';
// import {ScenegraphLayer} from '@deck.gl/mesh-layers/typed';
import 'mapbox-gl/dist/mapbox-gl.css';
import { prefix } from '../../../src/prefix'
// import Stats from 'three/examples/jsm/libs/stats.module';

// let stats: Stats;

// deck is on!
const deckMap = ref<HTMLCanvasElement | null>(null);
// const downArrow = ref(null);

// ship params type
// type ShipParams = {
//   pos: [number, number], 
//   rot: number, 
//   scale: [number, number]
// }

// type ShipGeojson = {
//   "geometry": {
//     "coordinates": [number, number], 
//     "type": "Point"
//   }, 
//   "id": number, 
//   "properties": {
//     "direction": number, "length": number, 
//     "mmsi": string, "name": string, 
//     "name_cn": string, "register_time": string, 
//     "title": string, "update_time": string, 
//     "velocity": number, "width": number
//   }, 
//   "type": "Feature"
// }

// initial view state
let viewState: ViewState = {
  latitude: 31.864162,
  longitude: 120.950697,
  zoom: 8,
  pitch: 0,
  bearing: 0
};

let zoom = 8;

// ship icon mapping param
const ICON_MAPPING = {
  shipMarker: {x: 0, y: 0, width: 512, height: 512},  
  parkMarker: {x: 0, y: 0, width: 256, height: 256, mask: false}, 
};

let buoyInfoStyle = ref<InfoStyle>({
  zIndex: -1, 
  left: '-20vw',
  width: '24vw',
  height: '11vw',
  bottom: '-20vh'
});

let infoCardStyles: {[key: string]: Ref<InfoStyle>} = {
  'buoy': buoyInfoStyle, 
};

const cardActive = ref(false);
const controlActive = ref(false);

let toggleURLList = [
  './layer.png', 
  './angle-double-left.png'
];
let toggleUrlIndex = 0;
let toggleURL = ref(toggleURLList[toggleUrlIndex]);
let ToggleControl = () => {
  toggleUrlIndex = 1 - toggleUrlIndex;
  toggleURL.value = toggleURLList[toggleUrlIndex];
  controlActive.value = !controlActive.value;
}

// let lastLayerList = ["AIS船舶", "航标", "停泊区", "锚地", "其他设施"];

const showLayerList = ref([
  "AIS船舶", "航标", "停泊区", "锚地", // "其他设施"
]);

// const layerId: {[key: string]: string} = {
//   '航标': 'buoy'
// }

const visibleControl: {[key: string]: boolean} = {
  'AIS船舶': true, 
  '航标': true, 
  '停泊区': true, 
  '锚地': true, 
  // "其他设施": true
};

const ToggleLayer = () => {
  for(let key in visibleControl) {
    if(showLayerList.value.includes(key)) {
      visibleControl[key] = true;
    }
    else {
      visibleControl[key] = false;
    }
  }
}

const activeList = ref(['layers']);
const LayerDropHandle = (val: string[]) => {
  console.log(val);
}



const shipInfo = ref({
  name: '', 
  speed: '10.2 km', 
  rot: '27.1°', 
  draught: '5.1 m', 
  status: '正在行驶中', 
  mmsi: '-', 
  size: '-', 
  updateTime: '-', 
  registerTime: '-'
});

const buoyInfo = ref({
  name: '#5黑浮', 
  color: '黑色', 
  shape: '柱形', 
  belong: '白茆沙水道', 
  url:  prefix + 'multiSource/img/1577929305273upload0.jpg'
});

function HideInfo(){
  cardActive.value = false;
}

function BuildTimeFromStamp(timeStamp: number) {
  const time = new Date(timeStamp);
  // console.log(time.toTimeString());
  const hourAndMin = time.toTimeString().split(' ')[0];
  return hourAndMin;
}

onMounted(async () => {
  console.log("Mounted...");
  // let container = document.getElementById( 'stats' ) as HTMLElement; 
  // stats = new (Stats as any)();
  // container.appendChild( stats.dom );

  // const shipGeoData = await axios.get(
  //   'http://172.21.212.10:8090/ais.geojson',
  //   {responseType: 'json'}
  // ).then((res) => {
  //   const data = res.data;
  //   return data["features"] as ShipGeojson[];
  // });
  // console.log(shipGeoData);


  // init mapbpox base map as a background
  const map = new mapboxgl.Map({
    accessToken: 'pk.eyJ1IjoieWNzb2t1IiwiYSI6ImNsMWVsdnpxNDBzcDgzYnA0MDJrcW1hOXQifQ.-5KUoc4jAJbAcBEWgbMGSA', 
    container: 'map', 
    interactive: false, 
    style: 'mapbox://styles/mapbox/dark-v10', 
    zoom: viewState.zoom, 
    center: [viewState.longitude, viewState.latitude], 
    pitch: viewState.pitch, 
    bearing: viewState.bearing
  });

  map.on('load', () => {
    map.addSource('bg', {
      type: 'raster', 
      tiles: [prefix + 'visual/getRaster/3884904c-7fc6-4811-b3a1-588853da8942/{x}/{y}/{z}']
    });
    map.addLayer({
      id: 'bg', 
      type: 'raster', 
      source: 'bg', 
      paint: {
        'raster-opacity': 0.75, 
        'raster-contrast': -0.4, 
        'raster-hue-rotate': 50, 
        'raster-saturation': -0.2
      }
    });
  });

  // cancel right-click context-menu showing up
  (deckMap.value as HTMLCanvasElement).oncontextmenu = (e) => {
    e.preventDefault();
  }

  if(deckMap.value == null) {
    return;
  }

  let mapHeight = deckMap.value.clientHeight;
  let mapWidth = deckMap.value.clientWidth;
  console.log(mapWidth, mapHeight);
  let b = map.unproject([mapWidth, 0]);
  let d = map.unproject([0, mapHeight]);

  let downArrow = document.getElementById('down-arrow');
  let buoyCardSize = [0.0, 0.0]
  if(downArrow) {
    buoyCardSize = [downArrow.clientWidth, downArrow.clientHeight];
    buoyInfoStyle.value["width"] = buoyCardSize[0] + 'px';
    buoyInfoStyle.value["height"] = parseFloat(buoyInfoStyle.value["height"].split('vw')[0]) / 100.0 * deckMap.value.clientWidth + 'px';
    // console.log(buoyCardSize);
  }

  let popupHelper = new PopupHelper(infoCardStyles, map);
  // console.log(infoCardStyles);

  // const demLayer = new TileLayer({
  //   data: prefix + 'visual/getRaster/3884904c-7fc6-4811-b3a1-588853da8942/{x}/{y}/{z}', 
  //   tileSize: 256, 
  //   renderSubLayers: props => {
  //     let { bbox } = props.tile;
  //     bbox = bbox as GeoBoundingBox;
  //     return new BitmapLayer( props, {
  //       data: null, 
  //       image: props.data, 
  //       bounds: [bbox.west, bbox.south, bbox.east, bbox.north]
  //     });
  //   }
  // });

  const stTime = new Date('2022-12-07 00:00:00 GMT+8').getTime();
  const endTime = new Date('2022-12-07 14:30:00 GMT+8').getTime();
  // console.log(new Date(stTime).toTimeString());
  const timePeriod = endTime - stTime;
  const timeInterval = 30 * 60 * 1000;
  const changeTime = timePeriod / timeInterval;
  const shipTimeInterval = ['00:00', '00:20'];
  let curTime = 0;

  let vis = true;

  const renderLayer = async () => {

    let parkMixData = await axios.get(
      prefix + 'multiSource/getParkInfoByBox/'
      + b.lat + '/' + b.lng + '/' + d.lat + '/' + d.lng,
      {responseType: 'json'}
    ).then((res) => {
      return res.data;
    });
    // console.log(parkMixData);

    // const otherLayer = new PolygonLayer({
    //   id:'other', 
    //   data: prefix + "multiSource/getOtherInfoBox/" + 
    //           b.lat + '/' + b.lng + '/' + d.lat + '/' + d.lng, 
    //   onDataLoad: (val, ctx) => {
    //     console.log(val);
    //   }, 
    //   getPolygon: d => {
    //     const pgPts = d.qyfw.points;
    //     let lng_ext = (pgPts[0][0] > pgPts[1][0])?[pgPts[1][0], pgPts[0][0]]:[pgPts[0][0], pgPts[1][0]];
    //     let lat_ext = (pgPts[0][1] > pgPts[1][1])?[pgPts[1][1], pgPts[0][1]]:[pgPts[0][1], pgPts[1][1]];

    //     return [
    //       [lng_ext[0], lat_ext[1]], 
    //       [lng_ext[0], lat_ext[0]], 
    //       [lng_ext[1], lat_ext[0]], 
    //       [lng_ext[1], lat_ext[1]], 
    //       [lng_ext[0], lat_ext[1]], 
    //     ];
    //   }, 
    //   getFillColor: [23, 233, 13, 20], 
    //   getLineWidth: 8, 
    //   pickable: true,
    //   stroked: true,
    //   filled: true,
    //   visible: visibleControl["其他设施"] && zoom > 11
    // });

    const anchorLayer = new PolygonLayer({
      id: 'anchor', 
      data: prefix + "multiSource/getAnchorInfoByBox/" + 
              b.lat + '/' + b.lng + '/' + d.lat + '/' + d.lng, 
      // onDataLoad: (val, ctx) => {
      //   console.log(val);
      // }, 
      getPolygon: d => {
        const pgPts = d.qyfw.points;
        if(pgPts[pgPts.length-1] !== pgPts[0]) {
          pgPts.push(pgPts[0]);
        }
        return pgPts;
      }, 
      getFillColor: [233, 13, 13, 120], 
      getLineWidth: 8, 
      pickable: true,
      stroked: true,
      filled: true,
      visible: visibleControl['锚地'] && zoom > 11
    });

    const parkLayer = new MixLayer({
      id: "park", 
      data: parkMixData, 
      visible:  visibleControl['停泊区'] && zoom > 11, 
      // updateTriggers: {
      //   getSize: [zoom]
      // }
    });

    const shipLayer = new IconLayer({
      id: 'ship', 
      // data: shipJSONData, 
      // data: shipGeoData, 
      data: prefix + "multiSource/getShipInfoByBoxAndTime/" + 
              b.lat + '/' + b.lng + '/' + d.lat + '/' + d.lng + '/' +
              shipTimeInterval[0] + '/' + shipTimeInterval[1], 
      // onDataLoad: (val, ctx) => {
      //   console.log(val);
      // }, 
      pickable: true, 
      iconAtlas: './ship-from-above-cut.png', // icon image url
      iconMapping: ICON_MAPPING,
      getIcon: d => 'shipMarker',  
      getPosition: d => [d["lon"], d["lat"]], // deck.gl call back function for retrieve ship positions from [data]
      sizeScale: 1, 
      getSize: (d) => {
        if(zoom < 10) {
          if(d["length"] < 200) return 0;
          return zoom * 2.0;
        }
        if(zoom < 12) {
          if(d["length"] < 120) return 0;
          return Math.log2(d["length"]) * zoom / 2.2;
        }
        if(zoom < 13) {
          if(d["length"] < 60) return 0;
          if(d["length"] == 0) 
            return zoom * 3.6;
          return Math.log2(d["length"]) * zoom / 1.8;
        }
        if(d["length"] == 0) 
            return zoom * 48;
        return Math.log10(d["length"]) * zoom * 48;
      }, 
      sizeUnits: 'pixels', 
      billboard: false, 
      visible: visibleControl['AIS船舶'], 
      onClick: (info, event) => {
        // console.log('Clicked:', info, event);
        cardActive.value = true;
        shipInfo.value.rot = info.object["direction"] + '°';
        shipInfo.value.size = parseFloat(info.object["length"]).toFixed(1) + ' / ' + parseFloat(info.object["width"]).toFixed(1) + ' m';
        shipInfo.value.mmsi = ((info.object["mmsi"]) == '')?'-':info.object["mmsi"];
        shipInfo.value.name = info.object["name"];
        shipInfo.value.speed = info.object["velocity"] + 'km/h';
        shipInfo.value.updateTime = info.object["update_time"];
        shipInfo.value.registerTime = info.object["register_time"];
      },
      getAngle: d => d["direction"], // make it rotate
      sizeMaxPixels: 100,
      // updateTriggers: {
      //   getSize: [zoom], 
      // }
    });
    let layerList = [];
    const buoyLayer = new IconLayer({
      id: 'buoy', 
      // data: shipJSONData, 
      data: prefix + "multiSource/getBuoyByBox/" + 
            b.lat + '/' + b.lng + '/' + d.lat + '/' + d.lng, 
      // onDataLoad: (val, ctx) => {
      //   console.log(val);
      // }, 
      pickable: true, 
      getIcon: d => ({
        url: prefix + 'multiSource/img/' + d.hbtlpz, 
        width: 50, 
        height: 50
      }), // static marker, return only a strrin
      // getPosition: d => d.pos, // deck.gl call back function for retrieve ship positions from [data]
      getPosition: (d) =>{
        return [d.jdwz_84jd, d.jdwz_84wd];
      },  // deck.gl call back function for retrieve ship positions from [data]
      sizeScale: 1, 
      visible: visibleControl['航标'] && zoom > 12, 
      getSize: () => {
        if(zoom < 13) {
          return zoom * 2.4;
        }
        return zoom * 4;
      }, 
      sizeUnits: 'pixels', 
      sizeMaxPixels: 100,
      onClick: (info, event) => {
        // console.log('Clicked:', info, event);
        // buoyInfoStyle.value.zIndex = 5;
        // if(info.devicePixel) {
        //   buoyInfoStyle.value.left = info.devicePixel[0] - buoyCardSize[0] / 2.0 + 12.0 + 'px';
        //   buoyInfoStyle.value.bottom = info.devicePixel[1] + 12 + 'px';
        // }
        buoyInfo.value.name = info.object.hbmc;
        if(info.object.bsys[info.object.bsys.length-1] !== '色') {
          buoyInfo.value.color = info.object.bsys + '色';
        }
        else {
          buoyInfo.value.color = info.object.bsys;
        }
        buoyInfo.value.shape = info.object.hbxz;
        buoyInfo.value.belong = info.object.sshd;
        buoyInfo.value.url = prefix + 'multiSource/img/' + info.object.hbphoto;
        // console.log(buoyCardSize);
        // console.log(buoyInfoStyle);
      },
      updateTriggers: {
        getSize: [zoom]
      }
    });
    // layerList.push(otherLayer);
    layerList.push(parkLayer);
    layerList.push(anchorLayer);
    layerList.push(buoyLayer);
    layerList.push(shipLayer);
    
    // set new deck props
    deck.setProps(
      {
        layers: layerList,  // [gltfLayer],  // add this layer
        // getTooltip: ({object}) => object && (object.pos[0].toString()+', '+ object.pos[1].toString()), // set interactive callback function
      }
    );
  }

  let isHovering = false;

  const UpdateShipTime = () => {
    const begTime = BuildTimeFromStamp(stTime + curTime * timeInterval);
    const endTime = BuildTimeFromStamp(stTime + (curTime + 1) * timeInterval);
    shipTimeInterval[0] = begTime;
    shipTimeInterval[1] = endTime;
    curTime = (curTime + 1) % changeTime;
    // console.log(shipTimeInterval);
    renderLayer();
  }
  // setInterval(UpdateShipTime, 2000);


  // init deck ~
  const deck = new Deck({
    canvas: 'deck', // decl requires a full-screen canvas
    width: "100%", 
    height: "100%", 
    initialViewState: viewState, 
    controller: true, 
    onHover: ({object}) => (isHovering = Boolean(object)), 
    getCursor: ({isDragging}) => (isDragging ? 'grabbing' : (isHovering ? 'pointer' : 'grab')),
    onClick: (info, event) => {
      // console.log('click', info, event);
      popupHelper.handleClick(info, event);
      // vis = false;
    }, 
    onViewStateChange: ( {viewState} ) => {
      map.jumpTo({
        center: [viewState.longitude, viewState.latitude], 
        zoom: viewState.zoom, 
        bearing: viewState.bearing, 
        pitch: viewState.pitch
      });
      // if(deck != null) {
      //   console.log(deck.getViewports())
      // }
      if(deckMap.value !== null) {
        let a = map.unproject([0, 0]);
        b = map.unproject([deckMap.value.clientWidth, 0]);
        let c = map.unproject([deckMap.value.clientWidth, deckMap.value.clientHeight]);
        d = map.unproject([0, deckMap.value.clientHeight]);
        let lat_min = Math.min(a.lat, b.lat, c.lat, d.lat);
        let lat_max = Math.max(a.lat, b.lat, c.lat, d.lat);
        let lng_min = Math.min(a.lng, b.lng, c.lng, d.lng);
        let lng_max = Math.max(a.lng, b.lng, c.lng, d.lng);

        b.lng = lng_max;
        b.lat = lat_max;
        d.lng = lng_min;
        d.lat = lat_min;
        // console.log(b, d);  
      }
      popupHelper.handleViewChange(viewState as ViewState);
      zoom = viewState.zoom;
      // console.log(zoom);
      renderLayer();
      // iconLayer.updateState({props: {}});
    }, 
    onLoad: () => {
      setInterval(UpdateShipTime, 2000);
    }
  });


  // setInterval(() => {
  //   zoom += 1;
  // }, 100);

  // const gltfLayer = new ScenegraphLayer({
  //   id: 'scenegraph-layer',
  //   data: ships,
  //   pickable: true,
  //   scenegraph: ' http://172.21.212.10:8081/boat.glb',
  //   getPosition: d => d.pos,
  //   getOrientation: d => [0, d.rot, 90],
  //   sizeScale: 0.1,
  //   sizeUnits: 'meters', 
  //   _lighting: 'pbr'
  // });
});


</script>

<style lang="scss">
body {
  margin: 0;
  overflow: hidden;
}

#map {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
}

#deck {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
}

#layer-control {
  position: absolute;
  width: 15vw;
  height: 5vh;
  left: -12vw;
  top: 10vh;
  z-index: 8;
  background-color: rgba(242, 247, 255, 0.6);
  backdrop-filter: blur(2px);
  border-top-right-radius: 6px;
  border-bottom-right-radius: 6px;

  transition: 0.3s ease-in-out;

  &.active {
    left: 0vw;
  }

  .el-row {
    height: 5vh;

    .el-col {
      &#layers-drop {
        height: 5vh;
        .el-collapse {
          height: 5vh;
          border-width: 0px;

          .el-collapse-item {
            height: 5vh;

            &.is-active {
              .el-collapse-item__wrap {
                height: 20vh;
                padding-left: 12px;
                transition: 0.3s ease-in-out;
                background-color: rgba(246, 251, 255, 0.95);
                backdrop-filter: blur(1em);
              }
            }

            div {
              height: 5vh;
              border-width: 0px;
              transition: 0.3s ease-in-out;


              &.el-collapse-item__header {
                font-size: 1vw;
                font-weight: 600;
                padding-left: 12px;
              }

              &.el-collapse-item__content {
                height: 20vh;

                .el-checkbox-group {
                  height: 20vh;
                  display: flex;
                  flex-flow: column;
                  justify-content: space-around;
                  .el-checkbox {
                    .el-checkbox__label {
                      font-size: 1vw;
                      color: rgb(136, 136, 136);
                    }

                    &.is-checked {
                      .el-checkbox__label {
                        color: rgb(4, 49, 88);
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      &#control-toggle {
        // background-color:antiquewhite;
        height: 5vh;
        padding: 12px 18px;
        &:hover {
          cursor: pointer;
        }
      }
    }
  }

  
}

#ship-info {
  position: absolute;
  left: 0vw;
  width: 20vw;
  // height: 66vh;
  top: 16vh;
  z-index: 20;
  min-width: 300px;
  transform: translateX(-24vw);
  &.active {
    transform: translateX(0);
  }
  transition: transform 0.4s ease-in-out;

  .el-card {
    height: 100%;
    backdrop-filter: blur(2px);
    background-color: rgba(240, 240, 240, 0.8);

    .el-card__header {
      background-color: rgb(159, 180, 218);
    }

    &:focus, &:hover {
      box-shadow: 0 0 8px rgb(88, 88, 88);
    }

    .card-header {
      .ship-name {
        font-size: 1vw;
        font-weight: 600;
        margin-bottom: 20px;
      }
      .ship-type {
        font-size: 0.8vw;
        font-weight: 500;
        top: 10px;
        color: rgb(54, 54, 54);
      }

      .el-icon {
        position: absolute;
        right: 1vw;
        top: 2.8vh;
        transition: 0.4s ease-in-out ;

        &:hover, &:focus {
          cursor: pointer;
          transform: translateX(-0.3vw);
        }
      }
    }

    .el-card__body {
      padding: 0px;

      .ship-image {
        width: 100%;
        display: block;
      }

      .el-descriptions {
        margin-top: 5px;
        .el-descriptions__label {
          font-size: 0.9vw;
          font-weight: 600;
          color: rgb(0, 9, 39);
          font-family: 'Microsoft YaHei';
          background-color: rgba(200, 214, 240, 0.5);
          border-color: rgb(159, 180, 218);
          border-width: 2px;
          border-bottom-width: 0;
        }
        .el-descriptions__content {
          border-width: 2px;
          border-top-width: 0;
          border-color: rgb(159, 180, 218);
          font-size: 0.8vw;
          transition: 0.3s ease-in-out;
          &:hover, &:focus {
            font-weight: 600;
            font-size: 0.9vw;
            color: rgb(96, 136, 211);
          }
        }
      }
    }
  }
}

#buoy-info {
  position: absolute;
  min-width: 300px;
  transform-style: preserve-3d;
  // transition: 0.1s ease-in-out;
  // transition: z-index 0s ease-in-out;

  .el-row#buoy-card {
    align-items: center;
    height: 100%;
    // background-color: antiquewhite;

    #buoy-image {
      // margin-top: 4px;
      height: 100%;
      // padding-left: 2px;
      // border-width: 2px;
      // border-color: rgb(159, 180, 218);
      border: 1px solid rgb(159, 180, 218);
      box-sizing: border-box;
      border-top-left-radius: 0.2vw;
      border-bottom-left-radius: 0.2vw;
      background-color: rgb(218, 237, 255);
    }

    .el-col {
      height: 100%;
      
      &#buoy-text {
        .el-descriptions {
          height: 100%;
          .el-descriptions__body {
            height: 100%;

            .el-descriptions__table {
              height: 100%;
              border-top-right-radius: 5vw;

              .el-descriptions__label {
                font-size: 0.75vw;
                font-weight: 600;
                color: rgb(0, 9, 39);
                font-family: 'Microsoft YaHei';
                background-color: rgba(200, 214, 240, 0.5);
                border-color: rgb(159, 180, 218);
                border-width: 1px;
                // border-bottom-width: 0;
              }
              .el-descriptions__content {
                border-width: 1px;
                // border-top-width: 0;
                border-color: rgb(159, 180, 218);
                font-weight: 600;
                font-size: 0.8vw;
                transition: 0.3s ease-in-out;
                &:hover, &:focus {
                  font-weight: 600;
                  font-size: 0.9vw;
                  color: rgb(96, 136, 211);
                }
              }

              tr {
                height: 25%;

              }
            }
          }

          
        }
      }

      &#buoy-img-box {
        display: flex;
        justify-content: center;
      }
    }
  }

  .el-row#down-arrow {
    width: 92%;
    .el-col {
      display: flex;
      justify-content: center;
      .down-arrow {
        width: 0px;
        height: 0px;
        border-top: 10px solid rgb(107, 176, 255);
        border-left: 10px solid transparent;
        border-right: 10px solid transparent;
      }
    }
    
  }
}

</style>
