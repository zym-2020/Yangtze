<template>
  <div>
    <div ref="chart" class="chart"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import * as echarts from "echarts";
import { getLastOrNextFewDateBy } from "@/utils/common";
import { getDataGroup } from '@/api/request'
import router from '@/router'
export default defineComponent({
  props:['id'],
  setup(props) {
    const chart = ref<HTMLElement>();

    const chartInit = (dateList: any[], valueList: any[]) => {

      const option = {
        visualMap: [
          {
            show: false,
            type: "continuous",

            dimension: 0,
            min: 0,
            max: dateList.length - 1,
          },
        ],

        tooltip: {
          trigger: "axis",
        },
        xAxis: [
          {
            data: dateList,
          },
        ],
        yAxis: [{}],
        grid: [
          {
            top: 30,
            bottom: 35,
            left: 60,
            right: 30,
            borderColor: "#AAADB9",
          },
        ],
        series: [
          {
            type: "line",
            showSymbol: false,
            data: valueList,
          },
        ],
      };
      const myChart = echarts.init(chart.value as HTMLElement, undefined, {
        height: 302,
        width: 910,
      });
      myChart.setOption(option);
    };

    onMounted(async () => {
      //console.log(props.id)
      let values: any[];
      const data = await getDataGroup((props as any).id, -29)
      if(data != null) {
        if((data as any).code === 0) {
          values = data.data
        }
      }
      const nowDate = new Date()
      const dataList = getLastOrNextFewDateBy(nowDate.toLocaleDateString(), -30)
      const valueList:any[] = []
      dataList.forEach((item, index) => {
        for(let i = 0;i < values?.length;i++) {
          if(item === values[i].date) {
            valueList.push(values[i].sum)
            return
          }
        }
        valueList.push(0)
      })
      chartInit(dataList, valueList);
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