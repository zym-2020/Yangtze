<template >
  <div>
    <el-descriptions :column="4" size="small" direction="vertical" border>
      <el-descriptions-item label="资源名称">kooriookami</el-descriptions-item>
      <el-descriptions-item label="资源大小">18100000000</el-descriptions-item>
      <el-descriptions-item label="空间范围">Suzhou</el-descriptions-item>
      <el-descriptions-item label="数据类型"> 水文数据 </el-descriptions-item>
      <el-descriptions-item label="数据标签">
        <div
          v-for="(item, index) in [1, 2, 3, 4]"
          :key="index"
          style="display: inline"
        >
          <el-tag size="small" style="margin-right: 3px">school</el-tag>
        </div>
      </el-descriptions-item>
      <el-descriptions-item label="数据详情">
        <el-button @click="toDetail()">数据详情</el-button>
      </el-descriptions-item>
      <el-descriptions-item label="数据下载">
        <el-button>数据下载</el-button>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>



<script lang="ts">
import { defineComponent, computed, ref } from "vue";
import router from "@/router";
export default defineComponent({
  components: {},
  props: {
    fileInfo: {
      type: Object,
    },
    id: {
      type: String,
    },
  },
  setup(props) {
    const size = ref("");
    const toDetail = () => {
      router.push({
        name: "shareFile",
        params: {
          id: props.id,
          fileInfo: JSON.stringify(props.fileInfo),
        },
      });
    };
    return {
      size,
      toDetail,
      //blockMargin,
    };
  },
});
</script>

<style lang="scss" scoped>
/deep/.el-descriptions__body {
  padding-top: 5px;
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 5px;
}
</style>