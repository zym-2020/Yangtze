<template>
  <div>
    <div ref="chart" class="chart"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import * as echarts from "echarts";
export default defineComponent({
  props: {
    sectionContrastValue: {
      type: Object,
    },
  },

  setup(props) {
    const chart = ref<HTMLElement>();
    let myChart: echarts.ECharts;
    const init = () => {
      const xData: string[] = [];
      for (let i = 0; i < 272; i++) {
        xData.push((i * 40).toString());
      }
      const option: echarts.EChartsOption = {
        // title: {
        //   text: (props.sectionContrastValue as any).name,
        // },
        tooltip: {},

        xAxis: {
          type: "category",
          name: "断面起点距",
          nameLocation: "middle",
          nameGap: 25,
          axisTick: {
            show: false,
          },
          data: xData,
        },
        yAxis: {
          type: "value",
          name: "断面高程值",
        },
        series: [
          {
            type: "line",
            data: (props.sectionContrastValue as any).value["199801_dem"],
            smooth: true,
          },
          {
            type: "line",
            data: (props.sectionContrastValue as any).value["200408_dem"],
            smooth: true,
          },
          {
            type: "line",
            data: (props.sectionContrastValue as any).value["200602_dem"],
            smooth: true,
          },
        ],
      };
      myChart = echarts.init(chart.value as HTMLElement, undefined, {
        height: 300,
        width: 700,
      });
      myChart.setOption(option);
    };
    onMounted(() => {
      init();
    });
    return {
      chart,
    };
  },
});
</script>

<style scoped>
.chart {
  height: 300px;
  width: 600px;
}
</style>