<template>
  <div class="admin-data-list">
    <div class="top">
      <!-- <div v-for="(item, index) in classList" :key="index" class="classify">
        <div>
          <strong>{{ item.label }}：</strong>
        </div>
        <div v-for="value in item.value" :key="value" class="classify-item">
          {{ value }}
        </div>
      </div> -->
    </div>
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <div v-else>
      <el-row :gutter="20" v-if="dataList.length > 0">
        <el-col :span="6" v-for="(item, index) in dataList" :key="index">
          <data-list-card :info="item" />
        </el-col>
      </el-row>
      <el-empty description="暂无数据" v-else />
      <div class="page">
        <el-pagination
          background
          v-model:current-page="currentPage"
          layout="total, prev, pager, next, jumper"
          :page-size="8"
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
import DataListCard from "@/components/admin/DataListCard.vue";
import { fuzzyQueryAdmin } from "@/api/request";
import NProgress from "nprogress";
export default defineComponent({
  components: { DataListCard },
  setup() {
    const classList = [
      {
        label: "地形数据",
        value: [
          "DEM",
          "边界",
          "等高线",
          "DWG",
          "高程点",
          "固定断面线",
          "深泓线",
        ],
      },
      {
        label: "工程数据",
        value: [
          "航标",
          "护岸工程",
          "码头工程",
          "水利工程",
          "整治工程",
          "桥梁工程",
        ],
      },
      {
        label: "物理模型",
        value: ["浓度场", "照片"],
      },
      {
        label: "水文数据",
        value: ["潮位", "断面输沙率", "含沙量", "含盐度", "流速流向", "悬移质"],
      },
      {
        label: "遥感影像",
        value: ["遥感影像"],
      },
    ];
    const skeletonFlag = ref(true);
    const dataList = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);

    const getData = async (
      property: string,
      titleKeyword: string,
      tags: string[],
      page: number,
      size: number,
      type: string,
      status: number
    ) => {
      NProgress.start();
      const data = await fuzzyQueryAdmin({
        property: property,
        flag: true,
        titleKeyword: titleKeyword,
        tags: tags,
        page: page,
        size: size,
        type: type,
        status: status,
      });
      if (data != null && (data as any).code === 0) {
        dataList.value = data.data.list;
        total.value = data.data.total;
      }
      NProgress.done();
    };

    const handleCurrentChange = async (param: number) => {
      await getData("update_time", "", [], param - 1, 8, "", 1);
    };

    onMounted(async () => {
      skeletonFlag.value = true;
      await getData("update_time", "", [], 0, 8, "", 1);
      skeletonFlag.value = false;
    });

    return {
      skeletonFlag,
      dataList,
      currentPage,
      handleCurrentChange,
      total,
      classList,
    };
  },
});
</script>

<style lang="scss" scoped>
.admin-data-list {
  margin: 20px;
  background: white;
  padding: 20px;
  .top {
    height: 60px;

    margin-bottom: 10px;
  }
  .page {
    display: flex;
    justify-content: center;
  }
}
</style>