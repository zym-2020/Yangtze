<template>
  <div class="SChart" ref="SChart"></div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import * as echarts from "echarts";
import { getSalinity } from "@/api/request";
export default defineComponent({
  props: {
    visualId: {
      type: String,
    },
  },
  setup(props) {
    const SChart = ref<HTMLElement>();
    let myChart: echarts.ECharts;

    let option: any = {};

    const initData = async (visualId: string) => {
      const data = await getSalinity(visualId);
      if (data != null) {
        if ((data as any).code === 0) {
          console.log(data);
          const date = (data.data as any).date;
          const surface = (data.data as any).surface;
          const mid6 = (data.data as any).mid6;
          const underwater = (data.data as any).underwater;
          const average = (data.data as any).average;

          option = {
            tooltip: {
              trigger: "axis",
              axisPointer: {
                type: "cross",
                label: {
                  backgroundColor: "#6a7985",
                },
              },
            },
            legend: {
              data: ["水面", "0.6", "水底", "垂线平均"],
            },
            grid: {
              left: "3%",
              right: "4%",
              bottom: "3%",
              containLabel: true,
            },
            xAxis: [
              {
                type: "category",
                boundaryGap: false,
                data: date,
              },
            ],
            yAxis: [
              {
                type: "value",
              },
            ],
            series: [
              {
                name: "水面",
                type: "line",
                stack: "Total",
                areaStyle: {},
                emphasis: {
                  focus: "series",
                },
                data: surface,
              },
              {
                name: "0.6",
                type: "line",
                stack: "Total",
                areaStyle: {},
                emphasis: {
                  focus: "series",
                },
                data: mid6,
              },
              {
                name: "水底",
                type: "line",
                stack: "Total",
                areaStyle: {},
                emphasis: {
                  focus: "series",
                },
                data: underwater,
              },
              {
                name: "垂线平均",
                type: "line",
                stack: "Total",
                areaStyle: {},
                emphasis: {
                  focus: "series",
                },
                data: average,
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
      myChart = echarts.init(SChart.value as HTMLElement);
      myChart.setOption(option);
    });

    return {
      SChart,
      refreshData
    };
  },
});
</script>

<style lang="scss" scoped>
.SChart {
  height: 400px;
  width: 900px;
}
</style>