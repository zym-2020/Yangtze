<template>
  <div 
    :index='chartOptId' 
    ref="chartDom" 
    class="chart-container" 
    :class="{unShown: noShown}"
    :styleId="styleId">
  </div>
</template>

<script lang="ts">
export default {
    name:'chartContainer'
}
</script>

<script setup lang='ts'>
import * as echarts from 'echarts';
import { onMounted, ref, watch } from 'vue';
import { frontData } from '../../frontData';

type EChartsOption = echarts.EChartsOption;

interface Props {
    chartId: string, 
    order: string, 
    styleType: string, 
    notShown: boolean
}

const props = defineProps<Props>();

const chartOptId = ref(props.chartId);
const styleId = ref(props.styleType);
const chartDom = ref<HTMLElement>();

let noShown = ref(props.notShown);

watch(()=>props.notShown, (oldShown, newShown)=> {
    noShown.value = !newShown;
    console.log(noShown.value);
});

onMounted(()=> {
    // console.log(chartDom.value);
    let chart = echarts.init(chartDom.value as HTMLElement)
    let chartConfig = frontData['charts'][+(chartOptId.value as string)];
    chart.setOption(chartConfig['chartOpt'] as EChartsOption)
    if(chartConfig['dynamicFunc'] !== undefined) {
        setInterval(
            function() {
                (chartConfig['dynamicFunc'] as ((chart: echarts.ECharts)=>void))(chart)
            }, 3000
        );
    }
    // TODO: window.onsize doesn't work on components
    window.onresize = function() {
        chart.resize();
    };
});


</script>

<style  lang='scss' scope>
$orders: 1, 2, 3, 4, 5, 6, 7, 8;

div.chart-container {
    border-radius: 0.4em;
    transition: transform 1s ease-in-out;

    &[styleId='1'] {
        height: 100%;
        width: 100%;
        div {
            canvas {
                border-radius: 0.6em;
            }
        }
    }
    &[styleId='2'] {
        height: 35%;
        width: 23%;
        background-color: rgba(255, 255, 255, 0.2);
    }
    &[styleId='3'] {
        height: 35%;
        width: 52%;
        background-color: rgba(255, 255, 255, 0.2);
    }
    &[styleId='4'] {
        position: absolute;
        right: 1vw;
        top: 22vh;
        height: 36vh;
        width: 28vw;
        border-radius: 0.8em;
        background-color: rgba(255, 255, 255, 0.3);
        backdrop-filter: blur(3px);
        box-shadow: 0.3em 0.3em 0.3em rgba(26, 26, 26, 0.6);

        &.unShown {
            transform: translateX(30vw);
            transition: transform .5s ease-in-out;   
        }
    }

    &[styleId='5'] {
        position: absolute;
        left: 1vw;
        top: 11vh;
        height: 40vh;
        width: 28vw;
        background-color: rgba(255, 255, 255, 0.3);
        backdrop-filter: blur(3px);
        box-shadow: 0.3em 0.3em 0.3em rgba(26, 26, 26, 0.6);

        &.unShown {
            transform: translateX(-30vw);
            transition: transform .5s ease-in-out;   
        }
    }

    &[styleId='6'] {
        position: absolute;
        left: 1vw;
        bottom: 4vh;
        height: 40vh;
        width: 28vw;
        background-color: rgba(255, 255, 255, 0.3);
        backdrop-filter: blur(3px);
        box-shadow: 0.3em 0.3em 0.3em rgba(26, 26, 26, 0.6);

        &.unShown {
            transform: translateX(-30vw);
            transition: transform .5s ease-in-out;   
        }
    }

    @each $order in $orders {
        &[order='#{$order}'] {
            order: $order;
        }
    }

    div {
        canvas {
            border-radius: 0.4em;
        }
    }
}

</style>