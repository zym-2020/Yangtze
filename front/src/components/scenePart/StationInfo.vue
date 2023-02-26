<template>
  <div class="station-info">
    <div class="name">
      <strong>{{ stationInfo.name }}</strong>
    </div>
    <div class="content" v-loading="loadingFlag">
      <div class="chart-main">
        <el-skeleton :rows="5" animated v-if="skeletonFlag" />
        <water-level-chart :info="info" v-else ref="waterLevelChart" />
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
    <div class="date-picker">
      <div>
        <el-date-picker
          v-model="timeValue"
          type="datetimerange"
          range-separator="至"
          format="YYYY-MM-DD HH 时"
          :disabled-date="disabledHandle"
        />
      </div>
      <div>
        <el-button type="primary" plain @click="btnClick">查询</el-button>
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

    const waterLevelChart = ref();
    const loadingFlag = ref(false);
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

    const timeValue = ref<[Date, Date]>([
      new Date(dateFormat(startDate.toString(), "yyyy-MM-dd hh") + ":00:00"),
      new Date(dateFormat(endDate.toString(), "yyyy-MM-dd hh") + ":00:00"),
    ]);

    const info = ref<WaterLevelChartType>({
      timeList: [],
      yAxis: [],
      series: [],
      legend: [],
    });

    const disabledHandle = (val: any) => {
      const temp = new Date();
      if (
        val > endDate ||
        val < temp.setTime(endDate.getTime() - 24 * 30 * 3600000)
      ) {
        return true;
      }
      return false;
    };

    const dataHandle = async () => {
      if (props.stationInfo) {
        info.value = {
          timeList: [],
          yAxis: [],
          series: [],
          legend: [],
        };
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
          dateFormat(timeValue.value[0].toString(), "yyyy-MM-dd hh") + ":00:00",
          dateFormat(timeValue.value[1].toString(), "yyyy-MM-dd hh") + ":00:00"
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

    const btnClick = async () => {
      loadingFlag.value = true;
      await dataHandle();
      waterLevelChart.value.refreshData();
      loadingFlag.value = false;
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
      timeValue,
      disabledHandle,
      loadingFlag,
      btnClick,
      waterLevelChart,
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
    height: 400px;
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
  .date-picker {
    margin-top: 20px;
    margin-left: 70px;
    height: 60px;
    display: flex;
    /deep/ .el-date-editor {
      width: 300px;
    }
    .el-button {
      margin-left: 40px;
    }
  }
}
</style> 