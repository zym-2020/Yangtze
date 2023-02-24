<template>
  <div class="water-level-info">
    <div class="chart" ref="chart"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import * as echarts from "echarts";
export default defineComponent({
  props: {
    info: {
      type: Object,
    },
  },
  setup(props) {
    const chart = ref<HTMLElement>();
    let myChart: echarts.ECharts;

    let option: any = {};

    const init = () => {
      if (props.info) {
        option = {
          xAxis: {
            type: "category",
            data: props.info.timeList,
          },
          yAxis: props.info.yAxis,
          series: props.info.series,
        };
      }
    };

    onMounted(() => {
      init();
      myChart = echarts.init(chart.value as HTMLElement);
      myChart.setOption(option);
    });

    return {
      chart,
    };
  },
});
</script>

<style lang="scss" scoped>
.water-level-info {
  width: 100%;
  height: 100%;
  .chart {
    height: 100%;
    width: 100%;
  }
}
</style>