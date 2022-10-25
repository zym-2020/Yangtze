<template>
  <div>
    <div ref="chart" class="chart"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import * as echarts from "echarts";
import { getLastOrNextFewDateBy } from "@/utils/common";
import { getDataGroup, QueryHeightByName } from "@/api/request";
import router from "@/router";
export default defineComponent({
  props: {
    getName: {
      type: String,
    },
  },
  setup(props) {
    const chart = ref<HTMLElement>();
    const dataList = ref<any[]>();
    const nameList = ref<any[]>();
    const tableName = ref("徐六泾站潮位观测成果表");
    (tableName.value as any) = props.getName;
    nameList.value = [
      "6.3日",
      "6.4日",
      "6.5日",
      "6.6日",
      "6.7日",
      "6.8日",
      "6.9日",
      "6.10日",
      "6.11日",
      "6.12日",
      "6.13日",
      "6.14日",
    ];
    const chartInit = async () => {
      const datass = await QueryHeightByName(tableName.value);
      dataList.value = datass.data[0].height;
      const realSeries = ref<any[]>([]);
      for (let i = 0; i < (dataList.value as any[]).length; i++) {
        (realSeries.value as any[]).push({
          name: (nameList.value as any[])[i],
          type: "line",
          stack: "Total",
          data: (dataList.value as any[])[i],
        });
      }
      const option = {
        title: {
          text: datass.data[0].name,
        },
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
            data: [
              "0",
              "1",
              "2",
              "3",
              "4",
              "5",
              "6",
              "7",
              "8",
              "9",
              "10",
              "11",
              "12",
              "13",
              "14",
              "15",
              "16",
              "17",
              "18",
              "19",
              "20",
              "21",
              "22",
              "23",
            ],
          },
        ],
        yAxis: [
          {
            type: "value",
            data: ["-1.0", "-0.5", "0", "0.5", "1", "1.5", "2", "2.5", "3.0"],
          },
        ],
        series: realSeries.value,
      };
      const myChart = echarts.init(chart.value as HTMLElement, undefined, {
        height: 400,
        width: 950,
      });
      myChart.setOption(option);
    };

    onMounted(() => {
      chartInit();
    });

    return {
      chart,
    };
  },
});
</script>

<style lang="scss" scoped>
.chart {
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  background: rgba($color: #d5e2fd, $alpha: 0.5);
}
</style>