<template>
  <div class="section-main">
    <div class="left">
      <el-radio-group v-model="value" @change="change">
        <el-radio :label="item.id" v-for="(item, index) in dems" :key="index">{{
          item.name
        }}</el-radio>
      </el-radio-group>
    </div>
    <div ref="chart" class="chart"></div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import * as echarts from "echarts";

interface SectionValue {
  value: { id: string; list: string[] }[];
  demLayers: any[];
}

export default defineComponent({
  props: {
    sectionValue: {
      type: Object,
    },
  },
  setup(props) {
    const chart = ref<HTMLElement>();
    let myChart: echarts.ECharts;
    const value = ref("");
    const index = ref(0);

    const dems = computed(() => {
      const arr: any[] = [];
      (props.sectionValue as SectionValue).demLayers.forEach((item) => {
        arr.push({
          id: item.id,
          name: item.name,
        });
      });
      return arr;
    });
    const getOption = () => {
      const xData: string[] = [];
      (props.sectionValue as SectionValue).value[index.value].list.forEach(
        (item, index) => {
          xData.push((index * 40).toString());
        }
      );
      const option: echarts.EChartsOption = {
        // title: {
        //   text: (props.sectionValue as SectionValue).name,
        // },
        tooltip: {},

        xAxis: {
          type: "category",
          name: "断面起点距",
          nameLocation: "middle",
          nameGap: 25,
          axisTick: {
            show: false,
          },
          data: xData,
        },
        yAxis: {
          type: "value",
          name: "断面高程值",
        },
        series: [
          {
            type: "line",
            data: (props.sectionValue as SectionValue).value[index.value].list,
            smooth: true,
          },
        ],
      };
      return option
    };
    const init = () => {
      const option = getOption()
      myChart = echarts.init(chart.value as HTMLElement, undefined, {
        height: 300,
        width: 600,
      });
      myChart.setOption(option);
    };

    const change = (val: string) => {
      for (
        let i = 0;
        i < (props.sectionValue as SectionValue).demLayers.length;
        i++
      ) {
        if (val === (props.sectionValue as SectionValue).demLayers[i].id) {
          index.value = i;
          break;
        }
      }
      const option = getOption()
      myChart.setOption(option, true)
    };

    onMounted(() => {
      console.log(props.sectionValue);
      value.value = (props.sectionValue as SectionValue).demLayers[
        index.value
      ].id;
      init();
    });

    return {
      chart,
      dems,
      value,
      change,
    };
  },
});
</script>

<style lang="scss" scoped>
.section-main {
  display: flex;
  .left {
    width: 100px;
    padding-top: 40px;
    .el-radio-group {
      /deep/ .el-radio {
        display: block;
      }
    }
  }
}
</style>