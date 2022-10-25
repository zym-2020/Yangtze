<template>
  <div class="FSZChart" ref="FSZChart"></div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import * as echarts from "echarts";
import { getFlowSand_Z } from "@/api/request";
export default defineComponent({
  props: {
    visualId: {
      type: String,
    },
  },
  setup(props) {
    const FSZChart = ref<HTMLElement>();
    let myChart: echarts.ECharts;

    let option: any = {};
    const colors = ["#5470C6", "#91CC75", "#EE6666"];

    const initData = async (visualId: string) => {
      const data = await getFlowSand_Z(visualId);
      if (data != null) {
        if ((data as any).code === 0) {
          const date = (data.data as any).date;
          const flow = (data.data as any).flow;
          const sand = (data.data as any).sand;
          option = {
            color: colors,
            tooltip: {
              trigger: "axis",
              axisPointer: {
                type: "cross",
              },
            },
            grid: {
              right: "20%",
            },
            legend: {
              data: ["流量", "输沙率"],
            },
            xAxis: [
              {
                type: "category",
                axisTick: {
                  alignWithLabel: true,
                },
                // prettier-ignore
                data: date,
              },
            ],
            yAxis: [
              {
                type: "value",
                name: "流量",
                position: "left",
                alignTicks: true,
                axisLine: {
                  show: true,
                  lineStyle: {
                    color: colors[0],
                  },
                },
                axisLabel: {
                  formatter: "{value} m3/s",
                },
              },
              {
                type: "value",
                name: "输沙率",
                position: "right",
                alignTicks: true,
                // offset: 80,
                axisLine: {
                  show: true,
                  lineStyle: {
                    color: colors[1],
                  },
                },
                axisLabel: {
                  formatter: "{value} kg/s",
                },
              },
            ],
            series: [
              {
                name: "流量",
                type: "line",
                data: flow,
              },
              {
                name: "输沙率",
                type: "line",
                yAxisIndex: 1,
                data: sand,
              },
            ],
          };
        }
      }
    };

    const refreshData = async (visualId: string) => {
      await initData(visualId);
      myChart.setOption(option);
    };

    onMounted(async () => {
      await initData(props.visualId as string);
      myChart = echarts.init(FSZChart.value as HTMLElement);
      myChart.setOption(option);
    });

    return {
      FSZChart,
      refreshData
    };
  },
});
</script>

<style lang="scss" scoped>
.FSZChart {
  width: 900px;
  height: 400px;
}
</style>