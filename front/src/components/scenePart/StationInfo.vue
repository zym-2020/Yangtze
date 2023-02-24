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
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import WaterLevelChart from "@/components/visual/WaterLevelChart.vue";
import { WaterLevelChartType } from "@/type";
import { getWaterLevelByStationAndTime } from "@/api/request";
import { dateFormat } from "@/utils/common";
import { Station } from "@/type";
export default defineComponent({
  components: { WaterLevelChart },
  props: {
    stationInfo: {
      type: Object,
    },
  },
  setup(props) {
    const skeletonFlag = ref(true);
    const stationInfo = computed(() => {
      return props.stationInfo;
    });

    const startDate = new Date();
    const endDate = new Date();
    startDate.setTime(endDate.getTime() - 24 * 3600000);

    const info = ref<WaterLevelChartType>({
      timeList: [],
      yAxis: [],
      series: [],
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
        (props.stationInfo as Station).keys_cn.forEach((key, index) => {
          info.value.yAxis.push({
            name: key,
            type: "value",
          });
          info.value.series.push({
            data: [],
            type: "line",
            smooth: true,
            yAxisIndex: index,
          });
        });
        const data = await getWaterLevelByStationAndTime(
          type,
          station,
          startTime.value,
          endTime.value
        );
        if (data != null && (data as any).code === 0) {
          (data.data as any).forEach((item: any) => {
            info.value.timeList.push(item.time);
            (props.stationInfo as Station).keys.forEach((key, index) => {
              info.value.series[index].data.push(item[key]);
            });
          });
        }
        console.log(info.value);
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
      width: 600px;
    }
  }
}
</style> 