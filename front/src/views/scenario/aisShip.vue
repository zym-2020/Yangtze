<template>
  <!-- <div id="stats"></div> -->
  <div id="map"></div>
  <canvas id="deck" ref="deckMap"></canvas>
  <div id="info" :class="{active: cardActive}">
    <el-card shadow="hover" >
      <template #header>
        <div class="card-header">
          <span class="ship-name">HANG HAI ZHI XIANG</span>
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
          <el-descriptions-item label="速度" label-align="center" align="center">{{cardInfo.speed}}</el-descriptions-item>
          <el-descriptions-item label="航线" label-align="center" align="center">{{cardInfo.rot}}</el-descriptions-item>
          <el-descriptions-item label="吃水深度" :span="2" label-align="center" align="center">{{cardInfo.draught}}</el-descriptions-item>
          <el-descriptions-item label="状态" :span="2" label-align="center" align="center">
            {{cardInfo.status}}
          </el-descriptions-item>
          <el-descriptions-item label="MMSI" :span="2" label-align="center" align="center">
            {{cardInfo.mmsi}}
          </el-descriptions-item>
          <el-descriptions-item label="船长/宽"  label-align="center" align="center">
            {{cardInfo.size}}
          </el-descriptions-item>
          <el-descriptions-item label="最大承重"  label-align="center" align="center">
            {{cardInfo.deadWeight}}
          </el-descriptions-item>
        </el-descriptions>
    </el-card>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';

export default defineComponent({
  name: 'aisShip',
});
</script>

<script lang="ts" setup>
import axios from 'axios';
import mapboxgl from 'mapbox-gl';
import { Deck } from '@deck.gl/core/typed';
import { IconLayer } from '@deck.gl/layers/typed';
// import {ScenegraphLayer} from '@deck.gl/mesh-layers/typed';
import 'mapbox-gl/dist/mapbox-gl.css';
import Stats from 'three/examples/jsm/libs/stats.module';

let stats: Stats;

// deck is on!
const deckMap = ref<HTMLCanvasElement | null>(null);

// deck & mapbox view state type -> for interactivity
type ViewState = {
  latitude: number,
  longitude: number,
  zoom: number,
  pitch: number,
  bearing: number
};

// ship params type
type ShipParams = {
  pos: [number, number], 
  rot: number, 
  scale: [number, number]
}

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
  marker: {x: 0, y: 0, width: 128, height: 128, mask: true}
};

// function downFile(data: string) {
//   var elementA = document.createElement('a');
  
//   elementA.setAttribute('href', 'data:text/plain;charset=utf-8,' + data);
//   elementA.setAttribute('download', + new Date() + ".json");
//   elementA.style.display = 'none';
//   document.body.appendChild(elementA);
//   elementA.click();
//   document.body.removeChild(elementA);
// }

const cardActive = ref(false);

const cardInfo = ref({
  name: '', 
  speed: '10.2 km', 
  rot: '27.1°', 
  draught: '5.1 m', 
  status: '正在行驶中', 
  mmsi: '-', 
  size: '-', 
  deadWeight: '-'
});

function HideInfo(){
  cardActive.value = false;
}

onMounted(async () => {
  console.log("Mounted...");
  let container = document.getElementById( 'stats' ) as HTMLElement; 
  // stats = new (Stats as any)();
  // container.appendChild( stats.dom );

  const requestFromBack = 0;
  if(requestFromBack) {
    // rquest for ship data in binary format
    const shipData = 
      await axios.get(
              'http://172.21.212.254:8002/ship/QueryCode', // back end api
              { responseType: 'arraybuffer' }
            ).then((res) => {
                  const dataView = new DataView(res.data)
                  return dataView; // in binary form
                });
    // console.log('data', shipData);

    const shipNum = shipData.byteLength / 25; // binary data for one ship is 25 bytes long

    const latStart = shipNum * 4;
    const lonStart = shipNum * 8;
    const rotStart = shipNum * 12;
    const lenStart = shipNum * 16;
    const widStart = shipNum * 20;

    const ships: ShipParams[] = [];

    for (let i = 0; i < shipNum; i++) { // resolve data and store in a simple list
      const lon = shipData.getInt32(lonStart + i * 4) / 100000;
      const lat = shipData.getInt32(latStart + i * 4) / 100000;
      const rot = shipData.getInt32(rotStart + i *4) / 1000;
      const len = shipData.getInt32(lenStart + i *4) / 500;
      const wid = shipData.getInt32(widStart + i *4) / 500;
      // console.log(lon, lat);
      ships.push({pos: [lon, lat], rot, scale: [len, wid]});
    }

    // downFile(ships.toString())
  }

  // request local json data(downloaded and resolve from back end request)
  const shipJSONData = await axios.get(
    'http://172.21.212.10:8080/data/shipData.json',
    {responseType: 'json'}
  ).then((res) => {
    const data = res.data;
    return data["ships"] as ShipParams[];
  });
  console.log(shipJSONData);


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

  // cancel right-click context-menu showing up
  (deckMap.value as HTMLCanvasElement).oncontextmenu = (e) => {
    e.preventDefault();
  }

  const renderLayer = () => {
    const iconLayer = new IconLayer({
      id: 'icon', 
      data: shipJSONData, 
      // data: ships, 
      pickable: true, 
      // iconAtlas: 'http://172.21.212.10:8080/ship.png', // icon image url
      // iconMapping: ICON_MAPPING, 
      getIcon: d => ({
        url: 'http://172.21.212.10:8080/ship-from-above-cut.png', 
        width: 512, 
        height: 512
      }), // static marker, return only a strrin
      getPosition: d => d.pos, // deck.gl call back function for retrieve ship positions from [data]
      sizeScale: 1, 
      getSize: (d) => {
        // console.log(zoom);
        if(zoom < 7) {
          if(d.scale[0] < 0.3) return 0;
          return zoom * 1.4;
        }
        if(zoom < 10) {
          if(d.scale[0] < 0.3) return 0;
          return 25;
        }
        if(zoom < 11.5) {
          if(d.scale[0] < 0.3) return 0;
          return Math.log10(d.scale[0] + 1) * zoom * 18;
        }
        if(zoom > 13) {
          return Math.log10(d.scale[0] + 1) * zoom * 36
        }
        return Math.log10(d.scale[0] + 1) * zoom * 48;
      }, 
      sizeUnits: 'pixels', 
      onClick: (info, event) => {
        console.log('Clicked:', info, event);
        cardActive.value = true;
        cardInfo.value.rot = info.object.rot + '°';
        cardInfo.value.size = (info.object.scale[0]*100).toFixed(2) + ' / ' + (info.object.scale[1]*100).toFixed(2) + ' m'
      },
      getAngle: d => d.rot, // make it rotate
      sizeMaxPixels: 100,
      updateTriggers: {
        getSize: [zoom]
      }
    });
    // set new deck props
    deck.setProps(
      {
        layers: [iconLayer],  // [gltfLayer],  // add this layer
        // getTooltip: ({object}) => object && (object.pos[0].toString()+', '+ object.pos[1].toString()), // set interactive callback function
      }
    );
    // stats.update();
  }

  let isHovering = false;

  // init deck ~
  const deck = new Deck({
    canvas: 'deck', // decl requires a full-screen canvas
    width: "100%", 
    height: "100%", 
    initialViewState: viewState, 
    controller: true, 
    onHover: ({object}) => (isHovering = Boolean(object)), 
    getCursor: ({isDragging}) => (isDragging ? 'grabbing' : (isHovering ? 'pointer' : 'grab')), 
    onViewStateChange: ( {viewState} ) => {
      map.jumpTo({
        center: [viewState.longitude, viewState.latitude], 
        zoom: viewState.zoom, 
        bearing: viewState.bearing, 
        pitch: viewState.pitch
      });
      zoom = viewState.zoom;
      // console.log(zoom);
      renderLayer();
      // iconLayer.updateState({props: {}});
    }
  });

  // creae icon layer for ships
  

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

#info {
  position: absolute;
  left: 0vw;
  width: 20vw;
  height: 66vh;
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

</style>
