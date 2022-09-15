<template>
  <div class="SCChart" ref="SCChart"></div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import * as echarts from "echarts";
import { getSandContent } from "@/api/request";
export default defineComponent({
  props: {
    visualId: {
      type: String,
    },
  },
  setup(props) {
    const SCChart = ref<HTMLElement>();
    let myChart: echarts.ECharts;

    let option: any = {};

    const initData = async (visualId: string) => {
      const data = await getSandContent(visualId);
      if (data != null) {
        if ((data as any).code == 0) {
          const sandData = data.data;
          // 解析 json 文件
          let times = Array();
          let dataLength = sandData["data"].length;
          let sand_surface = Array();
          let sand_02 = Array();
          let sand_04 = Array();
          let sand_06 = Array();
          let sand_08 = Array();
          let sand_bottom = Array();
          let sand_average = Array();

          for (let i = 0; i < dataLength; i++) {
            times[i] = sandData["data"][i][0];
            sand_surface[i] = sandData["data"][i][2];
            sand_02[i] = sandData["data"][i][3];
            sand_04[i] = sandData["data"][i][4];
            sand_06[i] = sandData["data"][i][5];
            sand_08[i] = sandData["data"][i][6];
            sand_bottom[i] = sandData["data"][i][7];
            sand_average[i] = sandData["data"][i][8];
          }

          option = {
            title: [
              {
                text: sandData["table_name"],
                left: "1%",
                top: "1%",
              },
            ],
            tooltip: {
              trigger: "axis",
              axisPointer: {
                type: "cross",
                label: {
                  backgroundColor: "#6a7985",
                },
              },
            },
            toolbox: {
              feature: {
                saveAsImage: {},
              },
            },
            legend: {
              top: "8%",
              left: "3%",
            },
            grid: [
              {
                top: "30%",
                left: "6%",
                right: "6%",
              },
            ],
            xAxis: [
              {
                axisLabel: {
                  formatter: function (value: string) {
                    return value.split(" ")[0];
                  },
                  align: "left",
                },
                type: "category",
                boundaryGap: false,
                data: times,
              },
            ],
            yAxis: [
              {
                type: "value",
                name: "含沙量(kg/m^3)",
                nameTextStyle: { align: "left" },
              },
            ],
            dataZoom: [
              {
                type: "inside",
              },
              {
                type: "slider",
              },
            ],

            series: [
              {
                name: "水面含沙量",
                type: "line",
                stack: "sand",
                areaStyle: {},
                emphasis: {
                  focus: "series",
                },
                smooth: true,
                data: sand_surface,
              },
              {
                name: "0.2m 含沙量",
                type: "line",
                stack: "sand",
                areaStyle: {},
                emphasis: {
                  focus: "series",
                },
                smooth: true,
                data: sand_02,
              },
              {
                name: "0.4m 含沙量",
                type: "line",
                stack: "sand",
                areaStyle: {},
                emphasis: {
                  focus: "series",
                },
                smooth: true,
                data: sand_04,
              },
              {
                name: "0.6m 含沙量",
                type: "line",
                stack: "sand",
                areaStyle: {},
                emphasis: {
                  focus: "series",
                },
                smooth: true,
                data: sand_06,
              },
              {
                name: "0.8m 含沙量",
                type: "line",
                stack: "sand",
                areaStyle: {},
                emphasis: {
                  focus: "series",
                },
                smooth: true,
                data: sand_08,
              },
              {
                name: "水底含沙量",
                type: "line",
                stack: "sand",
                areaStyle: {},
                emphasis: {
                  focus: "series",
                },
                smooth: true,
                data: sand_bottom,
              },
              {
                name: "平均含沙量",
                type: "line",
                stack: "sand",
                areaStyle: {},
                emphasis: {
                  focus: "series",
                },
                smooth: true,
                data: sand_average,
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
      myChart = echarts.init(SCChart.value as HTMLElement);
      myChart.setOption(option);
    });

    return {
      SCChart,
      refreshData,
    };
  },
});
</script>

<style lang="scss" scoped>
.SCChart {
  width: 100%;
  height: 400px;
}
</style>