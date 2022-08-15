<template>
  <div class="download-statistics">
    <div class="left">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column label="下载时间">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <el-icon><timer /></el-icon>
              <span style="margin-left: 10px">{{
                date(scope.row.download_time)
              }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="用户名">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <el-avatar :size="30" :src="avatar(scope.row.avatar)" />
              <div style="margin-left: 10px">{{ scope.row.name }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="下载数据类型" width="180">
          <template #default="scope">
            {{ type(scope.row.data_type) }}
          </template>
        </el-table-column>
      </el-table>
      <div class="page">
        <el-pagination
          small
          background
          layout="prev, pager, next"
          :total="total"
          :hide-on-single-page="true"
        />
      </div>
    </div>
    <!-- <div class="right">
      <div ref="chart" class="chart"></div>
    </div> -->
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import { dateFormat, getLastOrNextFewDateBy } from "@/utils/common";
import { pageQueryDownloadHistory } from "@/api/request";
import router from '@/router'
import * as echarts from "echarts";
export default defineComponent({

  setup() {
    const tableData = ref<any[]>([])
    const total = ref(0)
    const chart = ref<HTMLElement>();
    const chartInit = () => {
      const option = {
        title: {
          text: "10天内数据下载量柱状图",
          left: "center",
          top: 20,
        },

        legend: {},
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
        },
        yAxis: {
          type: "value",
          boundaryGap: [0, 0.01],
        },
        xAxis: {
          type: "category",
          data: getLastOrNextFewDateBy(new Date().toLocaleDateString(), -10),
        },
        series: [
          {
            name: "原始数据",
            type: "bar",
            data: [18203, 23489, 29034, 104970, 131744, 630230, 630230, 630230, 630230, 630230],
          },
          {
            name: "整合数据",
            type: "bar",
            data: [19325, 23438, 31000, 121594, 134141, 681807, 630230, 630230, 630230, 630230],
          },
          {
            name: "可视化数据",
            type: "bar",
            data: [19325, 23438, 31000, 121594, 134141, 681807, 630230, 630230, 630230, 630230],
          },
        ],
        dataZoom: [
          {
            type: "slider",
            realtime: true,
            start: 0,
            end: 50,
            xAxisIndex: [0],
          },
        ],
      };
      const myChart = echarts.init(chart.value as HTMLElement, undefined, {
        height: 300,
        width: 450,
      });
      myChart.setOption(option);
    };

    const initData = async () => {
      const data = await pageQueryDownloadHistory(10, 0, (router.currentRoute.value.params as any).id)
      if(data != null) {
        if((data as any).code === 0) {
          tableData.value = data.data.list
          total.value = data.data.total
        }
      }
    }

    const date = (time: string) => {
      return dateFormat(time);
    };
    const type = (type: string) => {
      if (type === "origin") {
        return "原始数据";
      } else if (type === "struct") {
        return "整合数据";
      } else if (type === "visual") {
        return "可视化数据";
      }
    };
    const avatar = (avatar: string) => {
      if (avatar === "" || avatar === undefined || avatar === null) {
        return "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png";
      } else {
        return "http://localhost:8002" + avatar;
      }
    };

    onMounted(async () => {
      await initData()
      // chartInit();
    });

    return {
      date,
      type,
      avatar,
      tableData,
      chart,
      total
    };
  },
});
</script>

<style lang="scss" scoped>
.download-statistics {
  display: flex;
  .left {
    width: 1250px;
    .page {
      margin-top: 40px;
      width: 100%;

      display: flex;
      justify-content: space-around;
    }
  }
}
</style>