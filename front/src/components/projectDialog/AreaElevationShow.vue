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
    areaElevationValue: {
      type: Object,
    },
  },
  setup(props) {
    const chart = ref<HTMLElement>();
    let myChart: echarts.ECharts;

    const getSeries = () => {
      const result: any[] = [];
      (props.areaElevationValue as any).dems.forEach((item: any) => {
        (props.areaElevationValue as any).data.forEach((d: any) => {
          if (d.dem === item.valueId) {
            const list: number[] = [];
            let count = 0;
            d.list.forEach((l: string) => {
              list.push(Math.abs(parseFloat(l) * 40) + count);
              count = Math.abs(parseFloat(l) * 40) + count;
            });
            result.push({
              type: "line",
              data: list,
              smooth: true,
            });
          }
        });
      });

      return result;
    };

    const init = () => {
      const xData: string[] = [];
      let max = -1;
      (props.areaElevationValue as any).data.forEach((item: any) => {
        if (item.list.length > max) {
          max = item.list.length;
        }
      });
      for (let i = 0; i < max; i++) {
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
        series: getSeries(),
        // series: [
        //   {
        //     type: "line",
        //     data: (props.sectionContrastValue as any).value["199801_dem"],
        //     smooth: true,
        //   },
        //   {
        //     type: "line",
        //     data: (props.sectionContrastValue as any).value["200408_dem"],
        //     smooth: true,
        //   },
        //   {
        //     type: "line",
        //     data: (props.sectionContrastValue as any).value["200602_dem"],
        //     smooth: true,
        //   },
        // ],
      };
      myChart = echarts.init(chart.value as HTMLElement, undefined, {
        height: 300,
        width: 700,
      });
      myChart.setOption(option);
    };

    onMounted(() => {
      console.log(props.areaElevationValue);
      init();
    });

    return {
      chart,
    };
  },
});
</script>

<style lang="scss" scoped>
</style>