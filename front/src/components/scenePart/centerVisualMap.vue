<template>
  <div class="scene-map-wrapper">
    <div id="map"></div>
  </div>
</template>

<script lang="ts">
export default {
  name: "centerVisualMap",
};
</script>

<script setup lang='ts'>
import mapboxgl from "mapbox-gl";
import mapBoxGl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import { onMounted } from "@vue/runtime-core";
import { ref, watch } from "vue";
import { useStore } from "@/store";
import { QueryShpByName, getCoordinates } from "@/api/request";
import router from "@/router";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
interface Props {
  mapId: string;
  shpVisualList: any[];
}
const store = useStore();
const props = defineProps<Props>();
const map = ref<mapBoxGl.Map>();
const pngName = ref("Yang");
const mapIndex = ref(props.mapId);
const mapName = ref("");
const flag = true;
const movePngFlag =ref(false)
const getCoordinate = async (id: string) => {
  const coor = [];
  const data = await getCoordinates(id);
  for (let i = 0; i < data.data.length; i++) {
    coor.push(data.data[i]);
  }
  return coor;
};

//避免跳转到其他页面时去调用后台的接口

// const data = await QueryShpByName(
//   (router.currentRoute.value.params.fileInfo as any).name
// );
// mapName.value = data.data[0].shpPinYinName;
// let names = mapName.value;
// if (names[0] >= "0" && names[0] <= "9") names = "a" + names;

// init mapbox...

// const popup = new mapboxgl.Popup({ closeOnClick: false })
//   .setLngLat([121.18, 31.723])
//   .setHTML("<h1>Hello World!</h1>")
//   .addTo(map.value);

// 待完善的栅格显示
// map.value?.addSource(layer.id, {
//   type: "raster",
//   tiles: [
//     `http://localhost:8002/analyticDataSet/getSlope/${layer.demSlopeId}/{x}/{y}/{z}`,
//   ],
// });
// map.value?.addLayer({
//   id: layer.id,
//   source: layer.id,
//   type: "raster",
// });
//console.log("http://localhost:8002/vector/" +names+" /{x}/{y}/{z}")

const addLayer = (layer: any) => {
 
  //矢量
  if (layer.type == "lineVectorTile" || layer.type == "lineVectorTile3D") {
    console.log("jj",layer.type)
    map.value?.addSource(layer.id, {
      type: "vector",
      tiles: [
        "http://172.21.213.244:8002/visual/getVectorTiles/" +
          layer.id +
          "/{x}/{y}/{z}",
      ],
    });

    map.value?.addLayer({
      id: layer.id,
      source: layer.id,
      type: "line",
      "source-layer": layer.id,
      layout: {
        "line-join": "round",
        "line-cap": "round",
      },
      paint: {
        "line-color": "#0000CD",
        "line-width": 5,
      },
    });
  } else if (
    layer.type == "pointVectorTile" ||
    layer.type == "pointVectorTile3D"
  ) {
    map.value?.addSource(layer.id, {
      type: "vector",
      tiles: [
        "http://172.21.213.244:8002/visual/getVectorTiles/" +
          layer.id +
          "/{x}/{y}/{z}",
      ],
    });

    map.value?.addLayer({
      id: layer.id,
      source: layer.id,
      type:'circle',
      "source-layer": layer.id,
      layout: {
      },
      paint: {
        'circle-radius':5,
        'circle-color' :"#0000CD"
      },
    });
  } else if (
    layer.type == "polygonVectorTile" ||
    layer.type == "polygonVectorTile3D"
  ) {
    map.value?.addSource(layer.id, {
      
      type:"vector",
      tiles: [
        "http://172.21.213.244:8002/visual/getVectorTiles/" +
          layer.id +
          "/{x}/{y}/{z}",
      ],
    });

    map.value?.addLayer({
      id: layer.id,
      source: layer.id,
      type: "line",
      "source-layer": layer.id,
      layout: {
        "line-join": "round",
        "line-cap": "round",
      },
      paint: {
        "line-color": "#0000CD",
        "line-width": 5,
      },
    });


    //栅格
  } else if (layer.type == "rasterTile") {
    map.value?.addSource(layer.id, {
      type: "raster",
      tiles: [
        `http://172.21.213.244:8002/visual/getRaster/${layer.id}/{x}/{y}/{z}`,
      ],
    });
    map.value?.addLayer({
      id: layer.id,
      source: layer.id,
      type: "raster",
    });
    //png
  } else if (layer.type == "png") {
    map.value?.addSource(layer.id, {
      type: "image",
      url: `http://172.21.213.244:8002/visual/getPngResource/${layer.id}`,
      coordinates: getCoordinate(layer.id) as unknown as any[],
    });
    map.value?.addLayer({
      id: layer.id,
      source: layer.id,
      type: "raster",
    });
    //变化栅格
  } else if (layer.type == "movePng") {
    movePngFlag.value=true
    map.value?.addSource(layer.id, {
      type: "image",
      url: `http://172.21.213.244:8002/visual/getPngResource/${layer.id}`,
      coordinates: getCoordinate(layer.id) as unknown as any[],
    });
    map.value?.addLayer({
      id: layer.id,
      source: layer.id,
      type: "raster",
    });
  }
};
const addAllLayers = (layers: any[]) => {
  layers.forEach((layer) => {
    addLayer(layer);
  });
  //如果是动态展示栅格的话就轮播展示
  if(movePngFlag.value==true){
  let count=0
  handle(layers,count)
  }
};
//
const handle=(layers:any[],count:number)=>{
  setTimeout(()=>{
    map.value?.moveLayer(layers[count]);
    count=(count +1)%layers.length
    handle(layers,count)
    
  })
}

onMounted(async () => {
  console.log("HHHHH");
  //仍需要更改
  //mapboxgl.accessToken =
  //"pk.eyJ1IjoiMTY2NTE2OTkzNzYiLCJhIjoiY2ttMDh5amJpMHE2dzJ3cTd5eWZsMGQxZyJ9.XErH3kSOuRC_OWXWCpDLkQ";
  map.value = new mapboxgl.Map({
    container: "map",
    style: "mapbox://styles/16651699376/ckmpu8kuk0h8r17msqpz351vf",
    center: [121.18, 31.723],
    zoom: 8,
    accessToken:
      "pk.eyJ1IjoiMTY2NTE2OTkzNzYiLCJhIjoiY2ttMDh5amJpMHE2dzJ3cTd5eWZsMGQxZyJ9.XErH3kSOuRC_OWXWCpDLkQ",
  });

  map.value.on("load", () => {
    addAllLayers(props.shpVisualList);
  });

  map.value?.addControl(new mapboxgl.NavigationControl());
});
</script>

<style lang='scss'>
div.scene-map-wrapper {
  cursor: pointer;
  // background-color: transparent;
  width: 52%;
  height: 63%;

  div#map {
    position: absolute;
    top: 0;
    bottom: 0;

    width: 100%;
    height: 100%;
    // background: rgba(255, 255, 255, 0.2);
  }
}
</style>