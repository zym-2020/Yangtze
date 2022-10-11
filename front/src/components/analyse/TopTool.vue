<template>
  <div class="top-tool">
    <div class="base-function">
      <el-button type="primary" text @click="dialogAddData = true"
        ><svg style="width: 16px; margin-right: 10px">
          <use xlink:href="#icon-tianjiayingyong"></use></svg
        >添加</el-button
      >
      <el-divider direction="vertical" />
      <el-button type="primary" text
        ><svg style="width: 16px; margin-right: 10px">
          <use xlink:href="#icon-ditu"></use></svg
        >底图</el-button
      >
      <el-divider direction="vertical" />
      <el-button type="primary" text
        ><svg style="width: 16px; margin-right: 10px">
          <use xlink:href="#icon-shujuyanjiu"></use></svg
        >分析</el-button
      >
    </div>

    <el-dialog v-model="dialogAddData" width="1000px" title="添加数据">
      <add-data-dialog @returnData="returnData" v-if="dialogAddData" />
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import AddDataDialog from "./AddDataDialog.vue";
export default defineComponent({
  components: { AddDataDialog },
  emits: ["returnFileList"],
  setup(_, context) {
    const dialogAddData = ref(false);

    const returnData = (
      val: {
        fileId: string;
        fileName: string;
        dataListId: string;
        dataListName: string;
      }[]
    ) => {
      dialogAddData.value = false;
      context.emit("returnFileList", val);
    };

    return {
      dialogAddData,
      returnData,
    };
  },
});
</script>

<style lang="scss" scoped>
.top-tool {
  height: 50px;
  display: flex;
  background: #f6f7f8;
  border-bottom: solid 0.5px #dcdfe6;
  box-sizing: border-box;
  .base-function {
    margin-left: 70px;
    height: 100%;
    line-height: 50px;
  }
}
</style>
