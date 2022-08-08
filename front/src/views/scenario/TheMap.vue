<template>
  <div id="map"></div>
  <DropSelector :selectorId="selIndex" @changeCom='updateCom'/>
	<ChartContainer 
		:chartId="chartInfo[0].chartId" 
		:order="chartInfo[0].order" 
		:styleType="chartInfo[0].styleType"
    :notShown="chartInfo[0].notShown"
	/>
  <ChartContainer 
		:chartId="chartInfo[1].chartId" 
		:order="chartInfo[1].order" 
		:styleType="chartInfo[1].styleType"
    :notShown="chartInfo[1].notShown"
	/>
  <ChartContainer 
		:chartId="chartInfo[2].chartId" 
		:order="chartInfo[2].order" 
		:styleType="chartInfo[2].styleType"
    :notShown="chartInfo[2].notShown"
	/>
</template>

<script lang='ts'>
import DropSelector from '../../components/scenePart/dropSelector.vue';
import ChartContainer from '../../components/scenePart/chartContainer.vue';
export default {
  name: "TheMap",
  components: {
    DropSelector, ChartContainer
  }, 
};
</script>

<script setup lang='ts'>
import mapboxgl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import { onMounted } from "@vue/runtime-core";
import { ref } from "vue";

mapboxgl.accessToken =
  "pk.eyJ1Ijoiam9obm55dCIsImEiOiJja2xxNXplNjYwNnhzMm5uYTJtdHVlbTByIn0.f1GfZbFLWjiEayI6hb_Qvg";

const selIndex = ref('0');

const chartInfo = ref([
	{chartId: "4", order: "0", styleType: "4", notShown: true}, 
	{chartId: "5", order: "0", styleType: "5", notShown: true}, 
  {chartId: "6", order: "0", styleType: "6", notShown: true}
]);

let activeList: string[] = [];

const updateCom = function(updateList: string[]) {
  console.log(updateList);
  if(activeList.length < updateList.length) {
    for(let name of updateList) {
      if(activeList.indexOf(name) == -1) {
        console.log('增加了: '+name);
        chartInfo.value[nameKey[name]].notShown = false;
      }
    }
  }
  else if(activeList.length > updateList.length) {
    for(let name of activeList) {
      if(updateList.indexOf(name) == -1) {
        console.log('减少了: '+name);
        chartInfo.value[nameKey[name]].notShown = true;
      }
    }
  }                                                        
  activeList = updateList;
  
};

const nameKey:{[key: string]: number} = {
  '断面形态': 0, 
  '断面对比': 1, 
  '断面冲淤': 2, 
}

// function changeTest() {
//   chartInfo.value[0].notShown = !chartInfo.value[0].notShown;
//   console.log(chartInfo.value[0].notShown);
// }

onMounted(() => {
  const map = new mapboxgl.Map({
    container: "map",
    style: "mapbox://styles/johnnyt/ckp3ijl0l093w17pqe4bl8s9b",
    center: [115.951, 29.517],
    zoom: 12.89,
    pitch: 60,
  });
  // setInterval(changeTest, 2000);
});
</script>


<style lang='scss' scope>
body {
  overflow: hidden;
}

div#map {
  width: 100vw;
  height: 100vh;
  position: absolute;
  top: 0;
  left: 0;
  background: rgb(0, 155, 226);
}


</style>