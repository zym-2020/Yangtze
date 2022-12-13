<template>
  <div class="admin-data-list">
    <div class="top">
      <span>选择状态：</span>
      <el-select v-model="statusValue" @change="ChangeHandle">
        <el-option label="Online" value="1" />
        <el-option label="Offline" value="-1" />
      </el-select>
      <span>选择类型：</span>
      <el-select v-model="typeValue" @change="ChangeHandle">
        <el-option-group
          v-for="group in classList"
          :key="group.label"
          :label="group.label"
        >
          <el-option
            v-for="item in group.value"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-option-group>
      </el-select>
      <span>排序方式：</span>
      <el-select v-model="sortValue" @change="ChangeHandle">
        <el-option
          v-for="item in sortList"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button type="success" @click="searchHandle">搜索</el-button>
      <el-input
        v-model="input"
        placeholder="数据关键词"
        @keydown.enter="searchHandle"
      />
    </div>
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <div v-else>
      <el-row :gutter="20" v-if="dataList.length > 0">
        <el-col :span="6" v-for="(item, index) in dataList" :key="index">
          <data-list-card :info="item" @operateHandle="operateHandle" :keyword="keyword"/>
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
import {
  fuzzyQueryAdmin,
  updateStatusById,
  deleteByAdmin,
} from "@/api/request";
import NProgress from "nprogress";
import { notice } from "@/utils/notice";
export default defineComponent({
  components: { DataListCard },
  setup() {
    const classList = [
      {
        label: "所有类型",
        value: ["all"],
      },
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
    const sortList = [
      {
        label: "更新时间",
        value: "update_time",
      },
      {
        label: "下载量",
        value: "download",
      },
      {
        label: "浏览量",
        value: "watch",
      },
    ];
    const skeletonFlag = ref(true);
    const dataList = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);

    const typeValue = ref("all");
    const sortValue = ref("update_time");
    const statusValue = ref("1");
    const input = ref("");
    const keyword = ref("");

    const getData = async (
      property: string,
      titleKeyword: string,
      page: number,
      size: number,
      type: string,
      status: number
    ) => {
      NProgress.start();
      const data = await fuzzyQueryAdmin({
        property: property,
        flag: false,
        titleKeyword: titleKeyword,
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

    const ChangeHandle = async () => {
      await getData(
        sortValue.value,
        keyword.value,
        currentPage.value - 1,
        8,
        typeValue.value === "all" ? "" : typeValue.value,
        parseInt(statusValue.value)
      );
    };

    const handleCurrentChange = async (param: number) => {
      await getData(
        sortValue.value,
        keyword.value,
        param - 1,
        8,
        typeValue.value === "all" ? "" : typeValue.value,
        parseInt(statusValue.value)
      );
    };

    const operateHandle = async (val: { type: string; id: string }) => {
      if (val.type === "online") {
        await updateStatusById(val.id, 1);
        await getData(
          sortValue.value,
          keyword.value,
          currentPage.value - 1,
          8,
          typeValue.value === "all" ? "" : typeValue.value,
          parseInt(statusValue.value)
        );
        notice("success", "成功", "上线成功");
      } else if (val.type === "offline") {
        await updateStatusById(val.id, -1);
        await getData(
          sortValue.value,
          keyword.value,
          currentPage.value - 1,
          8,
          typeValue.value === "all" ? "" : typeValue.value,
          parseInt(statusValue.value)
        );
        notice("success", "成功", "下线成功");
      } else {
        const data = await deleteByAdmin({
          page: currentPage.value - 1,
          size: 8,
          keyword: keyword.value,
          property: sortValue.value,
          flag: false,
          id: val.id,
          type: typeValue.value === "all" ? "" : typeValue.value,
          status: parseInt(statusValue.value),
        });
        dataList.value = data.data.list;
        total.value = data.data.total;
        notice("success", "成功", "删除成功");
      }
    };

    const searchHandle = async () => {
      keyword.value = input.value;
      await getData(
        sortValue.value,
        keyword.value,
        currentPage.value - 1,
        8,
        typeValue.value === "all" ? "" : typeValue.value,
        parseInt(statusValue.value)
      );
    };



    onMounted(async () => {
      skeletonFlag.value = true;
      await getData("update_time", "", 0, 8, "", 1);
      skeletonFlag.value = false;
    });

    return {
      skeletonFlag,
      dataList,
      currentPage,
      handleCurrentChange,
      total,
      classList,
      sortList,
      operateHandle,
      typeValue,
      sortValue,
      statusValue,
      input,
      ChangeHandle,
      searchHandle,
      keyword
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
    position: relative;
    .el-select {
      width: 150px;
      margin-right: 50px;
    }
    .el-input {
      width: 300px;
      float: right;
      margin-right: 10px;
    }
    .el-button {
      float: right;
    }
  }
  .page {
    display: flex;
    justify-content: center;
  }
}
</style>