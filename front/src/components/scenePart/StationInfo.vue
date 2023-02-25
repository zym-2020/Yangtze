<template>
  <div class="station-info">
    <div class="name">
      <strong>{{ stationInfo.name }}</strong>
    </div>
    <div class="content">
      <div class="chart-main">
        <el-skeleton :rows="5" animated v-if="skeletonFlag" />
        <water-level-chart :info="info" v-else />
      </div>
      <div class="table">
        <el-table :data="tableData" border style="width: 100%" height="380">
          <el-table-column label="时间" width="110" align="center">
            <template #default="scope">
              <span>{{ scope.row.time.substring(5, 16) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            :prop="item"
            :label="keys_cn[index]"
            v-for="(item, index) in keys"
            :key="index"
          />
        </el-table>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import WaterLevelChart from "@/components/visual/WaterLevelChart.vue";
import { WaterLevelChartType } from "@/type";
import { getWaterLevelByStationAndTime } from "@/api/request";
import { dateFormat } from "@/utils/common";
import { Station, WaterLevel } from "@/type";
export default defineComponent({
  components: { WaterLevelChart },
  props: {
    stationInfo: {
      type: Object,
    },
  },
  setup(props) {
    const colors = ["#5470C6", "#91CC75", "#EE6666", "#E5DB4B"];
    const skeletonFlag = ref(true);
    const tableData = ref<WaterLevel[]>([]);
    const stationInfo = computed(() => {
      return props.stationInfo;
    });
    const keys = computed(() => {
      const res = props.stationInfo?.keys;
      return res;
    });
    const keys_cn = computed(() => {
      const res = props.stationInfo?.keys_cn;
      return res;
    });

    const startDate = new Date();
    const endDate = new Date();
    startDate.setTime(endDate.getTime() - 24 * 3600000);

    const info = ref<WaterLevelChartType>({
      timeList: [],
      yAxis: [],
      series: [],
      legend: [],
    });
    const startTime = ref(
      dateFormat(startDate.toString(), "yyyy-MM-dd hh") + ":00:00"
    );
    const endTime = ref(
      dateFormat(endDate.toString(), "yyyy-MM-dd hh") + ":00:00"
    );

    const dataHandle = async () => {
      if (props.stationInfo) {
        const station = props.stationInfo.name;
        const type = props.stationInfo.type;
        let start = 50;
        (props.stationInfo as Station).keys_cn.forEach((key, index) => {
          if (index >= 2) {
            info.value.yAxis.push({
              alignTicks: true,
              axisLine: {
                show: true,
                lineStyle: {
                  color: colors[index],
                },
              },

              type: "value",
              offset: start,
            });
            start += 50;
          } else {
            info.value.yAxis.push({
              alignTicks: true,
              type: "value",
              axisLine: {
                show: true,
                lineStyle: {
                  color: colors[index],
                },
              },
            });
          }
          info.value.legend.push(key);
          info.value.series.push({
            name: key,
            data: [],
            type: "line",
            smooth: true,
            yAxisIndex: index,
            itemStyle: {
              normal: {
                color: colors[index],
                lineStyle: {
                  color: colors[index],
                },
              },
            },
          });
        });
        const data = await getWaterLevelByStationAndTime(
          type,
          station,
          startTime.value,
          endTime.value
        );
        if (data != null && (data as any).code === 0) {
          tableData.value = data.data;
          (data.data as any).forEach((item: any) => {
            info.value.timeList.push(item.time.substring(10, 16));
            (props.stationInfo as Station).keys.forEach((key, index) => {
              info.value.series[index].data.push(item[key]);
            });
          });
        }
      }
    };

    onMounted(async () => {
      skeletonFlag.value = true;
      await dataHandle();
      skeletonFlag.value = false;
    });

    return {
      stationInfo,
      info,
      skeletonFlag,
      tableData,
      keys,
      keys_cn,
    };
  },
});
</script>


<style lang="scss" scoped>
.station-info {
  width: 100%;
  .name {
    padding: 10px;
    text-align: center;
    color: white;
    background: #006399;
    font-size: 20px;
  }
  .content {
    height: 500px;
    display: flex;
    .chart-main {
      height: 400px;
      width: 700px;
    }
    .table {
      padding: 10px;
      width: 530px;
    }
  }
}
</style> 