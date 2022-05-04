<template>
  <div class="folder-context-menu-main">
    <div class="context">重命名</div>
    <div class="context">删除</div>
    <div class="context">下载</div>
    <div class="context">权限管理</div>
    <div class="context">属性</div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
export default defineComponent({
  setup() {},
});
</script>

<style lang="scss" scoped>
.folder-context-menu-main {
  width: 300px;
  background: #e7e6e6;
  .context {
    height: 30px;
    line-height: 30px;
    padding-left: 10px;
    cursor: pointer;
    &:hover {
      background: rgba($color: #777777, $alpha: 0.5);
    }
  }
}
</style>