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
          title: {
            padding: [10, 0, 0, 160],
            text: props.info.text,
            textStyle: {
              fontFamily: "Microsoft YaHei",
            },
          },
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "cross",
            },
          },
          legend: {
            data: props.info.legend,
            top: 10,
          },
          grid: {
            top: 50,
            right: 50 * (props.info.series.length - 1),
          },

          dataZoom: [
            {
              xAxisIndex: [0],
              filterMode: "filter",
              start: 50,
              end: 100,
            },
          ],
          xAxis: {
            name: "时间",
            type: "category",
            data: props.info.timeList,

            nameTextStyle: {
              width: 20,
              overflow: "none",
              textBorderType: "solid",
            },
          },
          yAxis: props.info.yAxis,
          series: props.info.series,
        };
      }
    };

    const refreshData = async () => {
      init();
      myChart.setOption(option);
    };

    onMounted(() => {
      init();
      myChart = echarts.init(chart.value as HTMLElement);
      myChart.setOption(option);
    });

    return {
      chart,
      refreshData,
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