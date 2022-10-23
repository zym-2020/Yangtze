<template>
  <div class="section-flush" ref="sectionFlush"></div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getSectionFlush } from "@/api/request";
import * as echarts from "echarts";
export default defineComponent({
  props: {
    chartInfo: {
      type: Object,
    },
  },
  setup(props) {
    const sectionFlush = ref<HTMLElement>();
    let myChart: echarts.ECharts;

    let option: any = {};

    const initData = async () => {
      const data = await getSectionFlush(props.chartInfo?.id);
      if (data != null && (data as any).code === 0) {
        const benchmark = data.data.benchmark;
        const refer = data.data.refer;
        const flush = data.data.flush;
        const xdata: number[] = [];
        flush.forEach((item: any, index: number) => {
          xdata.push(index * 40);
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
              right: "50px",
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
          grid: [
            {
              show: true,
              borderColor: "#867e76",
              bottom: "55%",
            },
            {
              top: "55%",
              show: true,
              borderColor: "#867e76",
            },
          ],
          xAxis: [
            {
              data: xdata,
              type: "category",
              axisLine: {
                onZero: false,
              },
            },
            {
              data: xdata,
              type: "category",

              gridIndex: 1,
            },
          ],
          yAxis: [
            {
              type: "value",
              name: "高程(m)",
              splitLine: { show: false },
              interval: 5,
            },
            {
              type: "value",
              name: "高程(m)",
              splitLine: { show: false },
              interval: 2,
              gridIndex: 1,
            },
          ],
          visualMap: {
            top: "2%",
            left: "60%",
            orient: "horizontal",
            pieces: [
              {
                gt: 0,
                lte: 99,
                color: "#3f51b5",
                label: "冲",
              },
              {
                gt: -99,
                lte: 0,
                color: "#f44336",
                label: "淤",
              },
            ],
          },
          series: [
            {
              name: "1",
              data: benchmark,
              type: "line",
              connectNulls: true,
              symbol: "none",
              animation: false,
              smooth: true,
              lineStyle: {
                color: "#f44336",
              },
              areaStyle: {
                color: "#f44336",
                opacity: 0.3,
              },
            },
            {
              name: "2",
              data: refer,
              type: "line",
              connectNulls: true,
              symbol: "none",
              animation: false,
              smooth: true,
              lineStyle: {
                color: "#3f51b5",
              },
              areaStyle: {
                color: "#3f51b5",
                opacity: 0.3,
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
                ],
                silent: true,
              },
            },
            {
              xAxisIndex: 1,
              yAxisIndex: 1,
              data: flush,
              type: "line",
              connectNulls: true,
              symbol: "none",
              animation: false,
              smooth: true,
              silent: true,
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
                  { yAxis: 10 },
                  { yAxis: 8 },
                  { yAxis: 6 },
                  { yAxis: 4 },
                  { yAxis: 2 },
                  {
                    yAxis: 0,
                    lineStyle: {
                      type: "solid",
                    },
                  },
                  { yAxis: -10 },
                  { yAxis: -8 },
                  { yAxis: -6 },
                  { yAxis: -4 },
                  { yAxis: -2 },
                ],
              },
              areaStyle: {
                origin: 0,
              },
            },
          ],
        };
      }
    };

    onMounted(async () => {
      await initData();
      myChart = echarts.init(sectionFlush.value as HTMLElement);
      myChart.setOption(option);
    });

    return {
      sectionFlush,
    };
  },
});
</script>

<style lang="scss" scoped>
.section-flush {
  width: 900px;
  height: 700px;
}
</style>