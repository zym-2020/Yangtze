<template >
  <div>
    <el-descriptions :column="4" size="small" direction="vertical" border>
      <el-descriptions-item label="资源名称">{{name}}</el-descriptions-item>
      <el-descriptions-item label="资源时间">{{updateTime}}</el-descriptions-item>
      <el-descriptions-item label="空间范围">{{range}}</el-descriptions-item>
      <el-descriptions-item label="数据类型"> {{type}} </el-descriptions-item>
      <el-descriptions-item label="数据标签">
        <div
          v-for="(item, index) in tagList"
          :key="index"
          style="display: inline"
        >
          <el-tag size="small" style="margin-right: 3px">{{item}}</el-tag>
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
import { dateFormat } from "@/utils/common";
import router from "@/router";
export default defineComponent({
  components: {},
  props: {
    fileInfo: {
      type: Object,
    },
  },
  setup(props) {
    const size = ref("");
    const name = computed(() => {
      return (props.fileInfo as any).name;
    });
    const updateTime = computed(() => {
      return dateFormat(
        (props.fileInfo as any).updateTime,
        "yyyy年MM月dd日hh时"
      );
    });
    const tagList = computed(() => {
      return (props.fileInfo as any).tags;
    });
    const type = computed(() => {
      return (props.fileInfo as any).type;
    });
    const range = computed(() => {
      return (props.fileInfo as any).range;
    });
    const toDetail = () => {
      router.push({
        name: "shareFile",
        params: {
          id: props.fileInfo?.id,
          fileInfo: JSON.stringify(props.fileInfo),
        },
      });
    };
    return {
      size,
      toDetail,
      name,
      updateTime,
      tagList,
      type,
      range
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