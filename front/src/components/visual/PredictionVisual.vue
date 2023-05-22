<template>
  <div class="prediction-visual" ref="predictionVisual"></div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from "vue";
import * as echarts from "echarts";
import { getPrediction } from "@/api/request";
export default defineComponent({
  props: {
    id: {
      type: String,
    },
  },
  setup(props) {
    const predictionVisual = ref<HTMLElement>();
    let myChart: echarts.ECharts;

    let option: any = {};

    const initData = async (id: string) => {
      const data = await getPrediction(id);
      if (data !== null && (data as any).code === 0) {
        const predictionList = data.data;
        option = {
          xAxis: {
            name: "步长",
            type: "category",
          },
          yAxis: {
            name: "水位",
            type: "value",
          },
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "cross",
            },
          },
          dataZoom: [
            {
              xAxisIndex: [0],
              filterMode: "filter",
              start: 50,
              end: 100,
            },
          ],
          series: [
            {
              data: predictionList,
              type: "line",
              smooth: true,
            },
          ],
        };
      }
    };

    const refreshData = async (id: string) => {
      await initData(id);
      myChart.setOption(option);
    };

    onMounted(async () => {
      await initData(props.id as string);
      myChart = echarts.init(predictionVisual.value as HTMLElement);
      myChart.setOption(option);
    });

    return { predictionVisual, refreshData };
  },
});
</script>

<style lang="scss" scoped>
.prediction-visual {
  width: 100%;
  height: 400px;
}
</style>