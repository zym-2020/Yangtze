<template>
  <div
    :index="chartOptId"
    ref="chartDom"
    class="chart-container"
    :styleId="styleId"
  ></div>
</template>

<script lang="ts">
export default {
  name: "chartContainer",
};
</script>

<script setup lang='ts'>
import * as echarts from "echarts";
import { onMounted, ref } from "vue";
import { frontData } from "../../frontData";

type EChartsOption = echarts.EChartsOption;

interface Props {
  chartId: string;
  order: string;
  styleType: string;
}

const props = defineProps<Props>();

const chartOptId = ref(props.chartId);
const styleId = ref(props.styleType);
const chartDom = ref<HTMLElement>();

onMounted(() => {
  // console.log(chartDom.value);
  let chart = echarts.init(chartDom.value as HTMLElement);
  let chartConfig = frontData["charts"][+(chartOptId.value as string)];
  chart.setOption(chartConfig["chartOpt"] as EChartsOption);
  if (chartConfig["dynamicFunc"] !== undefined) {
    setInterval(function () {
      (chartConfig["dynamicFunc"] as (chart: echarts.ECharts) => void)(chart);
    }, 3000);
  }
  // TODO: window.onsize doesn't work on components
  window.onresize = function () {
    chart.resize();
  };
});
</script>

<style  lang='scss' scope>
$orders: 1, 2, 3, 4, 5, 6, 7, 8;

div.chart-container {
  border-radius: 0.4em;

  &[styleId="1"] {
    height: 100%;
    width: 100%;
    div {
      canvas {
        border-radius: 0.6em;
      }
    }
  }
  &[styleId="2"] {
    height: 35%;
    width: 23%;
    background-color: rgba(255, 255, 255, 0.2);
  }
  &[styleId="3"] {
    height: 35%;
    width: 52%;
    background-color: rgba(255, 255, 255, 0.2);
  }
  &[styleId="4"] {
    position: absolute;
    right: 1vw;
    top: 23vh;
    height: 30vh;
    width: 28vw;
    background-color: rgba(26, 26, 26, 0.6);
  }

  @each $order in $orders {
    &[order="#{$order}"] {
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