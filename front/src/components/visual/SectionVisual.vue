<template>
  <div class="section" ref="section"></div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getSection } from "@/api/request";
import * as echarts from "echarts";
export default defineComponent({
  props: {
    chartInfo: {
      type: Object,
    },
  },
  setup(props) {
    const section = ref<HTMLElement>();
    let myChart: echarts.ECharts;

    let option: any = {};

    const initData = async () => {
      const data = await getSection(props.chartInfo?.id as string);
      if (data != null && (data as any).code === 0) {
        const xdata: number[] = [];
        data.data.list.forEach((item: any, index: number) => {
          xdata.push(index * 40);
        });
        option = {
          title: {
            text: "地形剖面图",
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
            type: "category",
            axisLine: {
              onZero: false,
            },
            data: xdata,
          },
          yAxis: {
            type: "value",
            name: "高程(m)",
            splitLine: { show: false },
            interval: 5,
          },
          series: [
            {
              data: data.data.list,
              type: "line",
              connectNulls: true,
              symbol: "none",
              animation: false,
              smooth: true,
              emphasis: {
                disabled: true,
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
                    label: {
                      formatter: "海平面",
                      show: true,
                      position: "insideEndTop",
                    },
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
              markArea: {
                silent: true,
                data: [
                  [
                    {
                      yAxis: 0,
                      itemStyle: {
                        color: {
                          type: "linear",
                          x: 0,
                          y: 0,
                          x2: 0,
                          y2: 1,
                          colorStops: [
                            {
                              offset: 0,
                              color: "#b0d5df", // 0% 处的颜色
                            },

                            {
                              offset: 1,
                              color: "#11659a", // 100% 处的颜色
                            },
                          ],
                        },
                      },
                    },
                    {
                      yAxis: -30,
                    },
                  ],
                ],
              },
              lineStyle: {
                color: "#867e76",
                width: 1,
              },
              areaStyle: {
                origin: "start",
                opacity: 1,
                color: {
                  type: "linear",
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: "#e4dfd7", // 0% 处的颜色
                    },

                    {
                      offset: 1,
                      color: "#bbb5ac", // 100% 处的颜色
                    },
                  ],
                },
              },
            },
          ],
        };
      }
    };

    onMounted(async () => {
      await initData();
      myChart = echarts.init(section.value as HTMLElement);
      myChart.setOption(option);
    });

    return {
      section,
    };
  },
});
</script>

<style lang="scss" scoped>
.section {
  width: 900px;
  height: 400px;
}
</style>