<template>
  <div class="volume" ref="volume"></div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import * as echarts from "echarts";
import { getVolume } from "@/api/request";
import "echarts-gl";
export default defineComponent({
  props: {
    chartInfo: {
      type: Object,
    },
  },
  setup(props) {
    const volume = ref<HTMLElement>();
    let myChart: echarts.ECharts;

    let option: any = {};

    const initData = async () => {
      const data = await getVolume(props.chartInfo?.id as string);
      if (data != null && (data as any).code === 0) {
        const value = data.data.data;
        const min = data.data.min;
        const max = data.data.max;
        option = {
          visualMap: {
            show: false,
            max: max,
            min: min,
            inRange: {
              color: ["#3949ab", "#26c6da", "#ffee58", "#ff7043", "#d50000"],
            },
          },
          xAxis3D: {
            type: "value",
          },
          yAxis3D: {
            type: "value",
          },
          zAxis3D: {
            name: "高程",
            type: "value",
            max: (max * 1.1).toFixed(0),
            min: (min * 1.1).toFixed(0),
          },
          grid3D: {
            axisPointer: { show: false },
          },
          series: [
            {
              type: "bar3D",
              data: value,
              shading: "color",
              silent: true,
              animation: false,
            },
          ],
        };
      }
    };

    onMounted(async () => {
      await initData();
      console.log(1)
      myChart = echarts.init(volume.value as HTMLElement);
      myChart.setOption(option);
    });
    return {
      volume,
    };
  },
});
</script>

<style lang="scss" scoped>
.volume {
  width: 900px;
  height: 400px;
}
</style>