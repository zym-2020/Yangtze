<template>
  <div class="pro">
    <div class="head" title="hah">项目管理</div>
    <div class="icon">
      <svg style="width: 20px; height: 20px" @click="open">
        <title>打开工程</title>
        <use xlink:href="#icon-wenjianjia"></use>
      </svg>
      <el-divider direction="vertical" />
      <svg style="width: 20px; height: 20px; margin-right: 100px">
        <title>移除工程</title>
        <use xlink:href="#icon-wenjianjiashanchu"></use>
      </svg>
      <el-divider direction="vertical" />
      <svg style="width: 20px; height: 20px">
        <title>创建工程</title>
        <use xlink:href="#icon-chuangjianrenwu"></use>
      </svg>
      <el-divider direction="vertical" />
      <svg style="width: 20px; height: 20px">
        <title>项目管理</title>
        <use xlink:href="#icon-liebiao-2"></use>
      </svg>
    </div>
    <div class="detail"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
export default defineComponent({
  setup() {
    const open = () => {
      console.log("haha");
    };
    return {
      open,
    };
  },
});
</script>


<style lang="scss" scoped>
.pro {
  width: 300px;
  border-left: solid 0.5px #dcdfe6;
  background: #f0f0f0;
  .head {
    background: rgba($color: #c8d5e3, $alpha: 0.4);
    height: 30px;
    line-height: 30px;
    padding-left: 10px;
  }
  .icon {
    height: 20px;
    line-height: 20px;
    padding-left: 10px;
    padding-top: 5px;
    .el-divider {
      margin-bottom: 8px;
    }
    svg {
      cursor: pointer;
    }
  }
  .detail {
    border: solid 0.5px;
    margin: 5px;
    height: calc(100% - 65px);
    background: white;
  }
}
</style>