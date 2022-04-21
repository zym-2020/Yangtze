<template>
  <div>
    <div ref="chart" class="chart"></div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import * as echarts from "echarts";

interface SectionValue {
  value: [];
  name: string;
  id: string;
  selectDemId: string;
  selectDemName: string;
}

export default defineComponent({
  props: {
    sectionValue: {
      type: Object,
    },
  },
  setup(props) {
    const chart = ref<HTMLElement>();
    let myChart: echarts.ECharts;
    const init = () => {
      const xData: string[] = [];
      (props.sectionValue as SectionValue).value.forEach((item, index) => {
        xData.push((index * 40).toString());
      });
      const option: echarts.EChartsOption = {
        title: {
          text: (props.sectionValue as SectionValue).name,
        },
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
            data: (props.sectionValue as SectionValue).value,
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

<style lang="scss" scoped>
.chart {
  height: 300px;
  width: 600px;
}
</style>