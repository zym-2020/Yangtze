<template>
  <div ref="SusChart" class="SusChart"></div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getSuspension } from "@/api/request";
import * as echarts from "echarts";
export default defineComponent({
  props: {
    visualId: {
      type: String,
    },
  },
  setup(props) {
    const SusChart = ref<HTMLElement>();
    let myChart: echarts.ECharts;

    let option: any = {};

    const initData = async (visualId: string) => {
      const suspensionData = await getSuspension(visualId);
      if (suspensionData != null) {
        if ((suspensionData as any).code == 0) {
          const particlesData = suspensionData.data;
          // 解析 json
          const dataLength = particlesData["data"].length;
          const times = new Array();
          const timeCategory = new Array();
          const stateCategory = new Array();

          // 制作时间轴数据及其副标题数据
          for (let state = new Array(), i = 0; i < dataLength; i++) {
            times[i] = particlesData["data"][i][3].replace(" ", "T");

            if (!timeCategory.includes(times[i])) {
              timeCategory.push(times[i]);
            }

            state[i] = particlesData["data"][i][2];
            if (!stateCategory.includes(state[i])) {
              stateCategory.push(state[i]);
            }
          }

          // 数据集生成函数
          function createData() {
            const data: any = new Object();
            for (const time of timeCategory) {
              data[time] = new Array();
            }

            return data;
          }

          // 根据悬浮质各级别分别生成数据集
          let data500 = createData();
          let data250 = createData();
          let data125 = createData();
          let data062 = createData();
          let data031 = createData();
          let data016 = createData();
          let data008 = createData();
          let data004 = createData();

          // 数据录入数据集
          // 单个数据集根据取样时间不同分割为不同属性,
          // 同一时间属性中为一个 object,
          // 具有 name 属性, 表示对应相对水深,
          // 具有 value 属性, 表示对应水深的颗粒级别百分比
          for (let i = 0; i < dataLength; i++) {
            let time = particlesData["data"][i][3].replace(" ", "T");
            data500[time].push({
              name: particlesData["data"][i][4],
              value: (
                particlesData["data"][i][5] - particlesData["data"][i][6]
              ).toFixed(2),
            });
            data250[time].push({
              name: particlesData["data"][i][4],
              value: (
                particlesData["data"][i][6] - particlesData["data"][i][7]
              ).toFixed(2),
            });
            data125[time].push({
              name: particlesData["data"][i][4],
              value: (
                particlesData["data"][i][7] - particlesData["data"][i][8]
              ).toFixed(2),
            });
            data062[time].push({
              name: particlesData["data"][i][4],
              value: (
                particlesData["data"][i][8] - particlesData["data"][i][9]
              ).toFixed(2),
            });
            data031[time].push({
              name: particlesData["data"][i][4],
              value: (
                particlesData["data"][i][9] - particlesData["data"][i][10]
              ).toFixed(2),
            });
            data016[time].push({
              name: particlesData["data"][i][4],
              value: (
                particlesData["data"][i][10] - particlesData["data"][i][11]
              ).toFixed(2),
            });
            data008[time].push({
              name: particlesData["data"][i][4],
              value: (
                particlesData["data"][i][11].toFixed(2) -
                particlesData["data"][i][12].toFixed(2)
              ).toFixed(2),
            });
            data004[time].push({
              name: particlesData["data"][i][4],
              value: particlesData["data"][i][12].toFixed(2),
            });
          }

          option = {
            baseOption: {
              // 题目
              title: [
                {
                  text: particlesData["table_name"],
                  left: "2%",
                  top: "1%",
                },
              ],
              // 时间轴
              timeline: {
                axisType: "category",
                autoPlay: true, // 自动播放
                playInterval: 1500, // 播放间隔时间
                data: timeCategory,
              },
              // 提示框
              tooltip: {
                trigger: "axis",
                axisPointer: {
                  type: "shadow",
                },
              },
              // 工具栏
              toolbox: {
                feature: {
                  saveAsImage: {}, // 另存为图像
                },
              },
              // 图例
              legend: {
                top: "15%",
              },
              calculable: true,
              // 格网位置
              grid: {
                top: "35%",
                bottom: "20%",
              },
              // x 轴-相对水深
              xAxis: [
                {
                  name: "相对水深(m)",
                  type: "category",
                  nameTextStyle: { align: "center", verticalAlign: "top" },
                  data: ["0.0", "0.2", "0.4", "0.6", "0.8", "1.0"],
                },
              ],
              // y 轴-各级别颗粒百分比
              yAxis: [
                {
                  name: "百分比(%)",
                  type: "value",
                },
              ],
              // 基础数据配置
              // 条形图
              // 堆叠形式
              series: [
                {
                  name: "0.004 粒径级(mm)",
                  type: "bar",
                  stack: "percent",
                  label: {
                    show: true,
                    position: "inside",
                  },
                },
                {
                  name: "0.008 粒径级(mm)",
                  type: "bar",
                  stack: "percent",
                  label: {
                    show: true,
                    position: "inside",
                  },
                },
                {
                  name: "0.016 粒径级(mm)",
                  type: "bar",
                  stack: "percent",
                  label: {
                    show: true,
                    position: "inside",
                  },
                },
                {
                  name: "0.031 粒径级(mm)",
                  type: "bar",
                  stack: "percent",
                  label: {
                    show: true,
                    position: "inside",
                  },
                },
                {
                  name: "0.062 粒径级(mm)",
                  type: "bar",
                  stack: "percent",
                  label: {
                    show: true,
                    position: "inside",
                  },
                },
                {
                  name: "0.126 粒径级(mm)",
                  type: "bar",
                  stack: "percent",
                  label: {
                    show: true,
                    position: "inside",
                  },
                },
                {
                  name: "0.250 粒径级(mm)",
                  type: "bar",
                  stack: "percent",
                  label: {
                    show: true,
                    position: "inside",
                  },
                },
                {
                  name: "0.500 粒径级(mm)",
                  type: "bar",
                  stack: "percent",
                  label: {
                    show: true,
                    position: "inside",
                  },
                },
              ],
            },
            // 各时间轴分数据
            options: [
              {
                title: {
                  subtext: `分析方法: 激光法    取样时间: ${timeCategory[0]}   潮流状态: ${stateCategory[0]}`,
                },
                series: [
                  { data: data004[timeCategory[0]] },
                  { data: data008[timeCategory[0]] },
                  { data: data016[timeCategory[0]] },
                  { data: data031[timeCategory[0]] },
                  { data: data062[timeCategory[0]] },
                  { data: data125[timeCategory[0]] },
                  { data: data250[timeCategory[0]] },
                  { data: data500[timeCategory[0]] },
                ],
              },
              {
                title: {
                  subtext: `分析方法: 激光法    取样时间: ${timeCategory[1]}   潮流状态: ${stateCategory[1]}`,
                },
                series: [
                  { data: data004[timeCategory[1]] },
                  { data: data008[timeCategory[1]] },
                  { data: data016[timeCategory[1]] },
                  { data: data031[timeCategory[1]] },
                  { data: data062[timeCategory[1]] },
                  { data: data125[timeCategory[1]] },
                  { data: data250[timeCategory[1]] },
                  { data: data500[timeCategory[1]] },
                ],
              },
              {
                title: {
                  subtext: `分析方法: 激光法    取样时间: ${timeCategory[2]}   潮流状态: ${stateCategory[2]}`,
                },
                series: [
                  { data: data004[timeCategory[2]] },
                  { data: data008[timeCategory[2]] },
                  { data: data016[timeCategory[2]] },
                  { data: data031[timeCategory[2]] },
                  { data: data062[timeCategory[2]] },
                  { data: data125[timeCategory[2]] },
                  { data: data250[timeCategory[2]] },
                  { data: data500[timeCategory[2]] },
                ],
              },
              {
                title: {
                  subtext: `分析方法: 激光法    取样时间: ${timeCategory[3]}   潮流状态: ${stateCategory[3]}`,
                },
                series: [
                  { data: data004[timeCategory[3]] },
                  { data: data008[timeCategory[3]] },
                  { data: data016[timeCategory[3]] },
                  { data: data031[timeCategory[3]] },
                  { data: data062[timeCategory[3]] },
                  { data: data125[timeCategory[3]] },
                  { data: data250[timeCategory[3]] },
                  { data: data500[timeCategory[3]] },
                ],
              },
            ],
          };
        }
      }
    };

    const refreshData = async (visualId: string) => {
      await initData(visualId);
      myChart.setOption(option);
    };

    onMounted(async () => {
      await initData(props.visualId as string);
      myChart = echarts.init(SusChart.value as HTMLElement);
      myChart.setOption(option);
    });

    return {
      SusChart,
      refreshData
    };
  },
});
</script>

<style lang="scss" scoped>
.SusChart {
  height: 400px;
  width: 900px;
}
</style>