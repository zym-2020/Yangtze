<template>
  <div class="sectionContrast" ref="sectionContrast"></div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getSectionContrast } from "@/api/request";
import * as echarts from "echarts";
export default defineComponent({
  props: {
    chartInfo: {
      type: Object,
    },
  },
  setup(props) {
    const sectionContrast = ref<HTMLElement>();
    let myChart: echarts.ECharts;

    let option: any = {};

    const initData = async () => {
      const data = await getSectionContrast(props.chartInfo?.id);
      if (data != null && (data as any).code === 0) {
        const series: any[] = [];
        const color = ["#f44336", "#185abd", "#c5f6c5"];
        const xdata: number[] = [];
        data.data[0].forEach((item: any, index: number) => {
          xdata.push(index * 40)
        })
        data.data.forEach((item: any[], index: number) => {
          if (index != data.data.length - 1) {
            series.push({
              name: index,
              data: item,
              type: "line",
              connectNulls: true,
              symbol: "none",
              animation: false,
              smooth: true,
              lineStyle: {
                color: color[index],
              },
            });
          } else {
            series.push({
              name: index,
              data: item,
              type: "line",
              connectNulls: true,
              symbol: "none",
              animation: false,
              smooth: true,
              lineStyle: {
                color: color[index],
              },
              markLine: {
                symbol: ["none", "none"],
                animation: false,
                lineStyle: {
                  color: "#21373d",
                  width: 1,
                  type: "dotted",
                },
                label: { show: false },
                data: [
                  {
                    yAxis: 0,
                    lineStyle: {
                      type: "solid",
                      width: 0.7,
                    },
                  },
                  {
                    yAxis: -5,
                  },
                  {
                    yAxis: -10,
                  },
                  {
                    yAxis: -15,
                  },
                  {
                    yAxis: -20,
                  },
                  {
                    yAxis: -25,
                  },
                  {
                    yAxis: -30,
                  },
                  {
                    yAxis: -35,
                  },
                ],
                silent: true,
              },
            });
          }
        });

        option = {
          title: {
            text: "地形剖面对比图",
            left: "5%",
          },
          // 提示框
          tooltip: {
            trigger: "axis",
          },
          // 工具栏
          toolbox: {
            right: "50px",
            feature: {
              dataZoom: {
                yAxisIndex: "none",
                brushStyle: {
                  color: "#869d9d",
                  opacity: 0.4,
                },
              },
              restore: {},
              saveAsImage: {
                pixelRatio: 2,
              },
            },
          },
          grid: {
            show: true,
            borderColor: "#867e76",
          },
          xAxis: {
            data: xdata,
            type: "category",
            axisLine: {
              onZero: false,
            },
          },
          yAxis: {
            type: "value",
            name: "高程(m)",
            splitLine: { show: false },
            interval: 5,
          },
          series: series,
        };
      }
    };

    onMounted(async () => {
      await initData();
      myChart = echarts.init(sectionContrast.value as HTMLElement);
      myChart.setOption(option);
    });

    return {
      sectionContrast,
    };
  },
});
</script>

<style lang="scss" scoped>
.sectionContrast {
  height: 400px;
  width: 900px;
}
</style>