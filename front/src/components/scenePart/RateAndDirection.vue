<template>
    <div ref="RDChart" class="RDChart"></div>
  </template>
  
  <script lang="ts">
  import * as echarts from "echarts";
  import { defineComponent, onMounted, ref } from "vue";
  import { getRateDirection } from "@/api/request";
  export default defineComponent({
    props:{
      getVeloName:{
        type:String
      }
  },
    setup(props) {

      const RDChart = ref<HTMLElement>();
      let myChart: echarts.ECharts;
  
      let option: any = {};
  
      const colors = ["#5470C6", "#91CC75", "#EE6666"];
  
      const initData = async () => {
        const data = await getRateDirection(
          props.getVeloName as string
        );
        const date = (data.data as any).date;
        console.log("wokuaile",date)
        const deep = (data.data as any).deep;
        const direction = (data.data as any).direction;
        const rate = (data.data as any).rate;
        option = {
          color: colors,
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "cross",
            },
          },
          grid: {
            right: "20%",
          },
          toolbox: {
            feature: {
              dataView: { show: true, readOnly: false },
              restore: { show: true },
              saveAsImage: { show: true },
            },
          },
          legend: {
            data: ["流速", "流向", "水深"],
          },
          xAxis: [
            {
              type: "category",
              axisTick: {
                alignWithLabel: true,
              },
              // prettier-ignore
              data: date,
            },
          ],
          yAxis: [
            {
              type: "value",
              name: "流速",
              position: "right",
              alignTicks: true,
              axisLine: {
                show: true,
                lineStyle: {
                  color: colors[0],
                },
              },
              axisLabel: {
                formatter: "{value} m/s",
              },
            },
            {
              type: "value",
              name: "流向",
              position: "right",
              alignTicks: true,
              offset: 80,
              axisLine: {
                show: true,
                lineStyle: {
                  color: colors[1],
                },
              },
              axisLabel: {
                formatter: "{value} °",
              },
            },
            {
              type: "value",
              name: "水深",
              position: "left",
              alignTicks: true,
              axisLine: {
                show: true,
                lineStyle: {
                  color: colors[2],
                },
              },
              axisLabel: {
                formatter: "{value} m",
              },
            },
          ],
          series: [
            {
              name: "流速",
              type: "bar",
              data: rate,
            },
            {
              name: "流向",
              type: "bar",
              yAxisIndex: 1,
              data: direction,
            },
            {
              name: "水深",
              type: "line",
              yAxisIndex: 2,
              data: deep,
            },
          ],
        };
      };
  
      onMounted(async () => {
        
        await initData();
        
        myChart = echarts.init(RDChart.value as HTMLElement);
        myChart.setOption(option);
      });
  
      return {
        RDChart,
        
      };
    },
  });
  </script>
  
  <style lang="scss" scoped>
  .RDChart {
    height: 400px;
    width: 950px;
    border: 1px solid #dcdfe6;
  border-radius: 8px;
  background: rgba($color: #d5e2fd, $alpha: 0.5);
  }
  </style>