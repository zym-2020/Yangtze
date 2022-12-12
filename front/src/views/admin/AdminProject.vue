<template>
  <div class="admin-project">
    <div class="head">1</div>
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <div v-else>
      <el-row :gutter="20" v-if="projectList.length > 0">
        <el-col :span="6" v-for="(item, index) in projectList" :key="index">
          <project-card :info="item" />
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
export default defineComponent({
  components: { ProjectCard },
  setup() {
    const skeletonFlag = ref(true);
    const projectList = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);

    const keyword = ref("");

    const getData = async (keyword: string, page: number, size: number) => {
      const data = await getAllByAdmin({
        keyword: keyword,
        page: page,
        size: size,
      });
      if (data != null && (data as any).code === 0) {
        projectList.value = data.data.list;
        total.value = data.data.total;
      }
    };

    const handleCurrentChange = async (val: number) => {
      await getData(keyword.value, val - 1, 16);
    };

    onMounted(async () => {
      await getData("", 0, 16);
      skeletonFlag.value = false;
    });

    return {
      projectList,
      total,
      skeletonFlag,
      currentPage,
      handleCurrentChange,
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
  }
  .page {
    display: flex;
    justify-content: center;
    margin-top: 25px;
  }
}
</style>