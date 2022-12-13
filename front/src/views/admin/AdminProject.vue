<template>
  <div class="admin-project">
    <div class="head">
      <el-button-group>
        <el-button
          :type="active === 'public' ? 'success' : 'info'"
          @click="statusClick('public')"
          >公共</el-button
        >
        <el-button
          :type="active === 'private' ? 'success' : 'info'"
          @click="statusClick('private')"
          >私有</el-button
        >
      </el-button-group>
      <el-button type="success" @click="searchHandle" class="search"
        >搜索</el-button
      >
      <el-input
        v-model="input"
        placeholder="数据关键词"
        @keydown.enter="searchHandle"
      />
    </div>
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <div v-else>
      <el-row :gutter="20" v-if="projectList.length > 0">
        <el-col :span="6" v-for="(item, index) in projectList" :key="index">
          <project-card
            :info="item"
            @delete="deleteHandle"
            @update="updateHandle"
            :keyword="keyword"
          />
        </el-col>
      </el-row>
      <el-empty description="暂无数据" v-else />
      <div class="page">
        <el-pagination
          background
          v-model:current-page="currentPage"
          layout="total, prev, pager, next, jumper"
          :page-size="16"
          :total="total"
          :pager-count="5"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getAllByAdmin } from "@/api/request";
import ProjectCard from "@/components/admin/ProjectCard.vue";
import { notice } from "@/utils/notice";
import NProgress from "nprogress";
export default defineComponent({
  components: { ProjectCard },
  setup() {
    const skeletonFlag = ref(true);
    const projectList = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);

    const keyword = ref("");
    const input = ref("");
    const active = ref("public");
    const status = ref(1);

    const statusClick = async (param: string) => {
      if (param === "public") {
        status.value = 1;
      } else {
        status.value = -1;
      }
      if (param != active.value) {
        await getData("", 0, 16, status.value);
      }
      active.value = param;
    };

    const getData = async (
      keyword: string,
      page: number,
      size: number,
      status: number
    ) => {
      NProgress.start();
      const data = await getAllByAdmin({
        keyword: keyword,
        page: page,
        size: size,
        status: status,
      });
      NProgress.done();
      if (data != null && (data as any).code === 0) {
        projectList.value = data.data.list;
        total.value = data.data.total;
      }
    };

    const searchHandle = async () => {
      keyword.value = input.value;
      await getData(keyword.value, 0, 16, status.value);
      currentPage.value = 1
    };

    const deleteHandle = async () => {
      await getData(keyword.value, currentPage.value - 1, 16, status.value);
      notice("success", "成功", "删除成功");
    };

    const updateHandle = async () => {
      await getData(keyword.value, currentPage.value - 1, 16, status.value);
    };

    const handleCurrentChange = async (val: number) => {
      await getData(keyword.value, val - 1, 16, status.value);
    };

    onMounted(async () => {
      await getData("", 0, 16, status.value);
      skeletonFlag.value = false;
    });

    return {
      projectList,
      total,
      skeletonFlag,
      currentPage,
      handleCurrentChange,
      deleteHandle,
      updateHandle,
      active,
      statusClick,
      searchHandle,
      input,
      keyword
    };
  },
});
</script>


<style lang="scss" scoped>
.admin-project {
  padding: 20px;
  .head {
    padding: 25px;
    background: white;
    margin-bottom: 20px;
    position: relative;
    .el-input {
      float: right;
      width: 300px;
      margin-right: 10px;
    }
    .search {
      float: right;
    }
  }
  .page {
    display: flex;
    justify-content: center;
    margin-top: 25px;
  }
}
</style>