<template>
  <div class="flush-filt">
    <div class="left">
      <div class="title">请选择基准年数据</div>
      <el-select v-model="value" placeholder="选择基准年数据" size="small">
        <el-option
          v-for="item in options"
          :key="item.valueId"
          :label="item.name"
          :value="item.valueId"
        />
      </el-select>
      <div class="title">请选择当前年数据</div>
      <div class="data-set">
        <el-scrollbar height="350px">
          <el-radio-group v-model="radio">
            <el-radio
              :label="item.valueId"
              v-for="(item, index) in options"
              :key="index"
              >{{ item.name }}</el-radio
            >
          </el-radio-group>
        </el-scrollbar>
      </div>
    </div>
    <div class="my-charts">
      <div ref="chart" ></div>
      <div ref="chart1" ></div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import * as echarts from "echarts";
export default defineComponent({
  props: {
    flushFiltValue: {
      type: Object,
    },
  },
  setup(props) {
    const chart = ref<HTMLElement>();
    const chart1 = ref<HTMLElement>();
    let myChart: echarts.ECharts;
    let myChart1: echarts.ECharts;
    const value = ref((props.flushFiltValue as any).dems[0].valueId);
    const radio = ref(
      (props.flushFiltValue as any).dems.length > 1
        ? (props.flushFiltValue as any).dems[1].valueId
        : (props.flushFiltValue as any).dems[0].valueId
    );
    const options = computed(() => {
      return (props.flushFiltValue as any).dems;
    });

    const getOption = () => {
      function getXAxis() {
        const temp: any[] = [];
        let max = -1;
        (props.flushFiltValue as any).data.forEach((item: any) => {
          if (item.list.length > max) {
            max = item.list.length;
          }
        });
        for (let i = 0; i < max; i++) {
          temp.push((i * 40).toString());
        }
        return temp;
      }
      function getSeries(valueId: string) {
        for (let i = 0; i < (props.flushFiltValue as any).data.length; i++) {
          if (valueId === (props.flushFiltValue as any).data[i].dem) {
            return (props.flushFiltValue as any).data[i].list;
          }
        }
      }
      const options: echarts.EChartsOption = {
        xAxis: {
          type: "category",
          name: "断面起点距",
          nameLocation: "middle",
          nameGap: 25,
          axisTick: {
            show: false,
          },

          data: getXAxis(),
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            data: getSeries(value.value),
            type: "line",
            areaStyle: {},
          },
          {
            data: getSeries(radio.value),
            type: "line",
            areaStyle: {},
          },
        ],
      };

      return options;
    };

    const getOption1 = () => {
      function getXAxis() {
        const temp: any[] = [];
        let max = -1;
        (props.flushFiltValue as any).data.forEach((item: any) => {
          if (item.list.length > max) {
            max = item.list.length;
          }
        });
        for (let i = 0; i < max; i++) {
          temp.push((i * 40).toString());
        }
        return temp;
      }
      function getSeries() {
        let arr1: string[] = [];
        let arr2: string[] = [];
        const result = [];
        (props.flushFiltValue as any).data.forEach((item: any) => {
          if (item.dem === radio.value) {
            arr1 = item.list;
          }
          if (item.dem === value.value) {
            arr2 = item.list;
          }
        });
        const min = arr1.length >= arr2.length ? arr2.length : arr1.length;
        if (arr1.length > arr2.length) {
          for (let i = 0; i < min; i++) {
            result.push(parseFloat(arr1[i]) - parseFloat(arr2[i]));
          }
          for (let i = min; i < arr1.length; i++) {
            result.push(parseFloat(arr1[i]));
          }
        } else if (arr1.length < arr2.length) {
          for (let i = 0; i < min; i++) {
            result.push(parseFloat(arr1[i]) - parseFloat(arr2[i]));
          }
          for (let i = min; i < arr1.length; i++) {
            result.push(0 - parseFloat(arr2[i]));
          }
        } else {
          for (let i = 0; i < min; i++) {
            result.push(parseFloat(arr1[i]) - parseFloat(arr2[i]));
          }
        }
        return result;
      }
      const options: echarts.EChartsOption = {
        xAxis: {
          type: "category",
          name: "断面起点距",
          nameLocation: "middle",
          nameGap: 25,
          axisTick: {
            show: false,
          },

          data: getXAxis(),
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            data: getSeries(),
            type: "line",
            areaStyle: {},
          },
        ],
      };

      return options;
    };

    const init = () => {
      const option = getOption();
      myChart = echarts.init(chart.value as HTMLElement, undefined, {
        height: 250,
        width: 500,
      });
      myChart.setOption(option);

      const option1 = getOption1();
      myChart1 = echarts.init(chart1.value as HTMLElement, undefined, {
        height: 250,
        width: 500,
      });
      myChart1.setOption(option1);
    };

    onMounted(() => {
      console.log(props.flushFiltValue);
      init();
    });

    return {
      options,
      value,
      radio,
      chart,
      chart1,
    };
  },
});
</script>

<style lang="scss" scoped>
.flush-filt {
  display: flex;
  .left {
    width: 150px;
    padding: 10px 10px;
    .el-select {
      margin: 10px 0;
    }
    .data-set {
      width: 150px;
      height: 350px;
      margin-top: 10px;
      background: #e2e3e5;
    }
  }

}
</style>