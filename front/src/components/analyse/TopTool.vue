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

    <div class="map-tool">
      <el-button
        type="primary"
        :text="state === 1 ? false : true"
        @click="sectionClick"
        ><svg style="width: 16px; margin-right: 10px">
          <use xlink:href="#icon-tianjiayingyong"></use></svg
        >绘制断面</el-button
      >
      <el-divider direction="vertical" />
      <el-button
        type="primary"
        :text="state === 2 ? false : true"
        @click="regionClick"
        ><svg style="width: 16px; margin-right: 10px">
          <use xlink:href="#icon-ditu"></use></svg
        >绘制区域</el-button
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
  emits: ["returnFileList", "operateDraw"],
  setup(_, context) {
    const dialogAddData = ref(false);
    const state = ref(0);

    const returnData = (
      val: {
        fileId: string;
        fileName: string;
        dataListId: string;
        dataListName: string;
        visualType: string;
        visualId: string;
      }[]
    ) => {
      dialogAddData.value = false;
      context.emit("returnFileList", val);
    };

    const sectionClick = () => {
      if (state.value === 1) {
        state.value = 0;
      } else {
        state.value = 1;
      }
      context.emit("operateDraw", state.value);
    };

    const regionClick = () => {
      if (state.value === 2) {
        state.value = 0;
      } else {
        state.value = 2;
      }
      context.emit("operateDraw", state.value);
    };

    return {
      state,
      dialogAddData,
      returnData,
      sectionClick,
      regionClick,
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
  position: relative;
  .base-function {
    margin-left: 70px;
    height: 100%;
    line-height: 50px;
  }
  .map-tool {
    height: 100%;
    position: absolute;
    left: 35%;
    line-height: 50px;
    .el-button {
      border: solid 1px rgba($color: #000000, $alpha: 0);
      box-sizing: border-box;
    }
  }
}
</style>
