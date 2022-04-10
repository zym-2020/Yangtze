<template>
  <div class="tool">
    <div class="dragHead">工具栏</div>
    <div>
      <svg style="width: 20px; height: 20px">
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
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
export default defineComponent({
  setup() {},
});
</script>

<style lang="scss" scoped>
.tool {
  width: 124px;
  border: solid 1px #888888;
  padding: 1px;
  .dragHead {
    background: #a0a0a0;
    height: 20px;
    &:hover {
      cursor: move;
    }
  }
  background: #fafafa;
  svg {
    margin-right: 10px;
    margin-top: 3px;
  }
}
</style>