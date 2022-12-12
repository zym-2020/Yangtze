<template>
  <div class="download-statistics">
    <div class="left">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column label="下载时间">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <el-icon><timer /></el-icon>
              <span style="margin-left: 10px">{{
                date(scope.row.downloadTime)
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
        <el-table-column label="文件名">
          <template #default="scope">
            {{ scope.row.fileName }}
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
import { defineComponent, onMounted, ref } from "vue";
import { dateFormat } from "@/utils/common";
import { pageQueryDownloadHistory } from "@/api/request";
import router from "@/router";
import { prefix } from '@/prefix'
export default defineComponent({
  setup() {
    const tableData = ref<any[]>([]);
    const total = ref(0);


    const initData = async () => {
      const data = await pageQueryDownloadHistory(
        10,
        0,
        (router.currentRoute.value.params as any).id
      );
      if (data != null) {
        if ((data as any).code === 0) {
          tableData.value = data.data.list;
          total.value = data.data.total;
        }
      }
    };

    const date = (time: string) => {
      return dateFormat(time);
    };

    const avatar = (avatar: string) => {
      if (avatar === "") {
        return "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png";
      } else {
        return prefix + "visual/getAvatar/" + avatar;
      }
    };

    onMounted(async () => {
      await initData();
      // chartInit();
    });

    return {
      date,
      avatar,
      tableData,
      total,
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