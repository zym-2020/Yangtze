<template>
  <div class="admin-audit">
    <div class="head">
      <el-button-group>
        <el-button
          :type="active === 'dataList' ? 'success' : 'info'"
          @click="statusClick('dataList')"
          >数据条目</el-button
        >
        <el-button
          :type="active === 'visual' ? 'success' : 'info'"
          @click="statusClick('visual')"
          >可视化绑定</el-button
        >
      </el-button-group>
      <el-input
        v-model="input"
        :placeholder="active === 'dataList' ? '条目名/创建者' : '文件名/创建者'"
        @keydown.enter="searchHandle"
      />
      <el-button type="primary" @click="searchHandle"
        ><el-icon><Search /></el-icon>搜索</el-button
      >
    </div>
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <div v-else>
      <div v-if="active === 'dataList'">
        <div v-if="dataList.length > 0">
          <div v-for="(item, index) in dataList" :key="index">
            <audit-card
              :info="item"
              :keyword="keyword"
              @auditHandle="auditHandle"
            />
          </div>
          <div class="page">
            <el-pagination
              background
              v-model:current-page="currentPage"
              layout="total, prev, pager, next, jumper"
              :page-size="10"
              :total="total"
              :pager-count="5"
              @current-change="handleCurrentChange"
            ></el-pagination>
          </div>
        </div>
        <div v-else>
          <el-empty description="暂无数据" />
        </div>
      </div>
      <div v-else>
        <div v-if="fileVisualList.length > 0">
          <div v-for="(item, index) in fileVisualList" :key="index">
            <visual-audit-card
              :info="item"
              :keyword="keywordVisual"
              @auditHandleVisual="auditHandleVisual"
            />
          </div>
          <div class="page">
            <el-pagination
              background
              v-model:current-page="currentPageVisual"
              layout="total, prev, pager, next, jumper"
              :page-size="10"
              :total="totalVisual"
              :pager-count="5"
              @current-change="handleCurrentChangeVisual"
            ></el-pagination>
          </div>
        </div>
        <div v-else>
          <el-empty description="暂无数据" />
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import AuditCard from "@/components/admin/AuditCard.vue";
import VisualAuditCard from "@/components/admin/VisualAuditCard.vue";
import { fuzzyQueryAdmin, getVisualAuditFiles } from "@/api/request";
import { AuditFile } from "@/type";
export default defineComponent({
  components: { AuditCard, VisualAuditCard },
  setup() {
    const active = ref("dataList");
    const skeletonFlag = ref(true);
    const currentPage = ref(1);
    const currentPageVisual = ref(1);
    const total = ref(0);
    const totalVisual = ref(0);
    const input = ref("");
    const keyword = ref("");
    const keywordVisual = ref("");

    const dataList = ref<any[]>([]);
    const fileVisualList = ref<AuditFile[]>([]);

    let flag = false;

    const statusClick = async (param: string) => {
      active.value = param;
      if (!flag && param === "visual") {
        skeletonFlag.value = true;
        await getDataVisual(0, "");
        flag = true;
        skeletonFlag.value = false;
      }
      if (param === "dataList") {
        input.value = keyword.value;
      } else {
        input.value = keywordVisual.value;
      }
    };

    const getData = async (page: number, keyword: string) => {
      const data = await fuzzyQueryAdmin({
        page: page,
        size: 10,
        titleKeyword: keyword,
        property: "update_time",
        flag: false,
        type: "",
        status: 0,
      });
      if (data != null && (data as any).code === 0) {
        dataList.value = data.data.list;
        total.value = data.data.total;
      }
    };

    const getDataVisual = async (page: number, keyword: string) => {
      if (keyword === "") {
        keyword = "all";
      }
      const data = await getVisualAuditFiles(keyword, page, 10);
      if (data != null && (data as any).code === 0) {
        fileVisualList.value = data.data.list;
        totalVisual.value = data.data.total;
      }
    };

    const auditHandle = async () => {
      await getData(currentPage.value - 1, keyword.value);
      input.value = keyword.value;
    };

    const auditHandleVisual = async () => {
      await getDataVisual(currentPageVisual.value - 1, keywordVisual.value);
      input.value = keywordVisual.value;
    };

    const searchHandle = async () => {
      if (active.value === "dataList") {
        keyword.value = input.value;
        await getData(0, keyword.value);
        currentPage.value = 1;
      } else {
        keywordVisual.value = input.value;
        await getDataVisual(0, keywordVisual.value);
        currentPage.value = 1;
      }
    };

    const handleCurrentChange = async (val: number) => {
      await getData(val - 1, keyword.value);
      input.value = keyword.value;
    };

    const handleCurrentChangeVisual = async (val: number) => {
      await getDataVisual(val - 1, keywordVisual.value);
      input.value = keywordVisual.value;
    };

    onMounted(async () => {
      await getData(0, "");
      skeletonFlag.value = false;
    });

    return {
      active,
      skeletonFlag,
      currentPage,
      currentPageVisual,
      total,
      totalVisual,
      handleCurrentChange,
      handleCurrentChangeVisual,
      dataList,
      fileVisualList,
      input,
      keyword,
      keywordVisual,
      auditHandle,
      auditHandleVisual,
      searchHandle,
      statusClick,
    };
  },
});
</script>

<style lang="scss" scoped>
.admin-audit {
  .head {
    height: 50px;
    .el-input {
      margin-left: 30px;
      width: 200px;
      margin-right: 10px;
    }
    .el-icon {
      margin-right: 5px;
    }
  }
  margin: 20px;
  background: white;
  padding: 20px;
  .page {
    margin-top: 30px;
    display: flex;
    justify-content: center;
  }
}
</style>