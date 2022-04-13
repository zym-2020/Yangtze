<template>
  <div class="tool">
    <div class="dragHead">工具栏</div>
    <div>
      <svg style="width: 20px; height: 20px" @click="openAddData">
        <title>添加数据</title>
        <use xlink:href="#icon-tianjiafutu"></use>
      </svg>
      <svg style="width: 20px; height: 20px">
        <title>全图</title>
        <use xlink:href="#icon-quanqiu"></use>
      </svg>
      <svg style="width: 20px; height: 20px">
        <title>选择</title>
        <use xlink:href="#icon-xuanze"></use>
      </svg>
      <svg style="width: 20px; height: 20px">
        <title>清除要素选择</title>
        <use xlink:href="#icon-cachubianjiewaiwapian"></use>
      </svg>
    </div>

    <el-dialog v-model="addDataFlag" :width="600">
      <add-data @returnData="returnData" v-if="addDataFlag"></add-data>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import AddData from "./toolDialog/AddData.vue";
export default defineComponent({
  components: {
    AddData,
  },
  setup() {
    const addDataFlag = ref(false);
    const openAddData = () => {
      addDataFlag.value = true;
    };
    const returnData = () => {
      addDataFlag.value = false;
    };
    return {
      addDataFlag,
      openAddData,
      returnData
    };
  },
});
</script>

<style lang="scss" scoped>
.tool {
  width: 124px;
  border: solid 1px #888888;
  padding: 1px;
  .dragHead {
    background: #a0a0a0;
    height: 25px;
    line-height: 25px;
    &:hover {
      cursor: move;
    }
  }
  background: #fafafa;
  svg {
    margin-right: 10px;
    margin-top: 3px;
  }
  /deep/.el-dialog {
    .el-dialog__header {
      padding: 0;
    }
    .el-dialog__body {
      padding: 0;
    }
    .el-dialog__headerbtn {
      height: 40px;
      width: 40px;
    }
  }
}
</style>