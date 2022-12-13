<template>
  <div class="admin-audit">
    <div class="head">
      <el-input
        v-model="input"
        placeholder="条目名/创建者"
        @keydown.enter="searchHandle"
      />
      <el-button type="primary" @click="searchHandle"
        ><el-icon><Search /></el-icon>搜索</el-button
      >
    </div>
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <div v-else>
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
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import AuditCard from "@/components/admin/AuditCard.vue";
import { fuzzyQueryAdmin } from "@/api/request";
export default defineComponent({
  components: { AuditCard },
  setup() {
    const skeletonFlag = ref(true);
    const currentPage = ref(1);
    const total = ref(0);
    const input = ref("");
    const keyword = ref("");

    const dataList = ref<any[]>([]);

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

    const auditHandle = async () => {
      await getData(currentPage.value - 1, keyword.value);
      input.value = keyword.value;
    };

    const searchHandle = async () => {
      keyword.value = input.value;
      await getData(currentPage.value - 1, keyword.value);
      currentPage.value = 1
    };

    const handleCurrentChange = async (val: number) => {
      await getData(val - 1, keyword.value);
      input.value = keyword.value;
    };

    onMounted(async () => {
      await getData(0, "");
      skeletonFlag.value = false;
    });

    return {
      skeletonFlag,
      currentPage,
      total,
      handleCurrentChange,
      dataList,
      input,
      keyword,
      auditHandle,
      searchHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.admin-audit {
  .head {
    height: 50px;
    .el-input {
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