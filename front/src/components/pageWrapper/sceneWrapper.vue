/* eslint-disable no-undef */
<template>
  <div class="scene-wrapper">
      <div class="content-wrapper">
            <div class="overall-data-wrapper">
                <div class="clock">
                    <p class="time">{{ curTime.toLocaleTimeString() }}</p>
                </div>
                <ChartContainer 
                    :chartId="chartInfo[0].chartId" 
                    :order="chartInfo[0].order" 
                    :styleType="chartInfo[0].styleType"
                    :notShown="chartInfo[0].notShown"
                ></ChartContainer>
            </div>
            <ChartContainer 
                :chartId="chartInfo[1].chartId" 
                :order="chartInfo[1].order" 
                :styleType="chartInfo[1].styleType"
                :notShown="chartInfo[1].notShown"
            ></ChartContainer>
            <ChartContainer 
                :chartId="chartInfo[2].chartId" 
                :order="chartInfo[2].order" 
                :styleType="chartInfo[2].styleType"
                :notShown="chartInfo[2].notShown"
            ></ChartContainer>
            <CenterMap :mapId="mapIndex"/>
            <ChartContainer 
                :chartId="chartInfo[3].chartId" 
                :order="chartInfo[3].order" 
                :styleType="chartInfo[3].styleType"
                :notShown="chartInfo[3].notShown"
            ></ChartContainer>
            <DataTable :tableId='tableIndex'/>
            <StatBox :statIndex="statIdx"/>
      </div>
  </div>
</template>

<script lang='ts'>
import { onMounted } from '@vue/runtime-core';
import { ref } from 'vue';
import ChartContainer from '../scenePart/chartContainer.vue';
import CenterMap from '../scenePart/centerMap.vue';
import DataTable from '../scenePart/dataTable.vue';
import StatBox from '../scenePart/statBox.vue';

export default {
    name: 'SceneWrapper',
    components: {
        ChartContainer, 
        CenterMap, 
        DataTable, 
        StatBox
    }, 
    setup() {
        const curTime = ref(new Date());
        const updataTime = (): void => {
            curTime.value = new Date();
        }

        const statNames = ref([
            {index:'0', name:'A'}, 
            {index:'1', name:'B'}, 
            {index:'2', name:'C'}, 
            {index:'3', name:'D'}
        ]);
        
        const chartInfo = ref([
            {chartId: "0", order: "0", styleType: "1", notShown: false}, 
            {chartId: "1", order: "0", styleType: "2", notShown: false}, 
            {chartId: "2", order: "0", styleType: "2", notShown: false}, 
            {chartId: "3", order: "0", styleType: "3", notShown: false}, 
        ]);

        const mapIndex = ref('0');

        const tableIndex = ref('0');

        const statIdx = ref('0');

        onMounted(() => {
            setInterval(updataTime, 1000);
        });
        return {
            curTime, statNames, 
            chartInfo, mapIndex, tableIndex, statIdx
        }
    }
}
</script>

<style lang='scss' scoped>
$colorMap: ('0':#ADD5F7, '1':#123475, '2':#ADD5F7, '3':#123475);

div.scene-wrapper {
    height: 93.8vh;
    background: linear-gradient(50deg, #448bc5 0%, darken(#448bc5, 70%) 100%);
    background-size: 200% 200%;  
    animation: background 6s ease infinite;
    @keyframes background { 
        0% {
            background-position: 0% 50%;
        }
        50% {
            background-position: 100% 50%;
        }
        100% {
            background-position: 0% 50%;
        }
    }

    div.content-wrapper {
        position: absolute;
        height: 94%;
        width: 100%;
        top: 6%;
        background-color: transparent;
        display: flex;
        flex-flow: column wrap;
        justify-content: space-around;
        align-content: space-around;

        div {
            box-sizing: border-box;

            &.overall-data-wrapper {
                height: 27%;
                width: 23%;
                background-color: rgba(255, 255, 255, 0.3);
                border-radius: 0.6em;

                div.clock {
                    text-align: center;
                    position: absolute;
                    left: 15%;
                    top: -5%;
                    line-height: 1em;
                    p {
                        font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
                        color: rgba(255, 255, 255, 0.8);
                        letter-spacing: 0.05em;
                        font-size: 2em;
                        text-shadow: 
                            0 0.5px 0 #ccc, 
                            0 1px 0 #c9c9c9, 
                            0 1.5px 0 #bbb, 
                            0 2px 0 #b9b9b9, 
                            0 2.5px 0 #aaa, 
                            0 3px 0.5px rgba(0,0,0,.1), 
                            0 0 2.5px rgba(0,0,0,.1), 
                            0 0.5px 1.5px rgba(0,0,0,.3), 
                            0 1.5px 2.5px rgba(0,0,0,.2), 
                            0 2.5px 5px rgba(0,0,0,.25), 
                            0 5px 5px rgba(0,0,0,.2), 
                            0 10px 10px rgba(0,0,0,.15);
                    }

                    p {
                        width: 100%;
                        height: 100%;
                        line-height: 100px;
                        text-align: center;
                        vertical-align: middle;
                    }
                }
            }

        }
    }
}
</style>