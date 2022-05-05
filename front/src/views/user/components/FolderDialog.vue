<template>
  <div class="folder-main">
    文件名:
    <el-input v-model="input" ref="inputFocus" />
    <el-button
      type="primary"
      plain
      size="small"
      style="margin-left: 130px"
      @click="commit"
      >确定</el-button
    >
  </div>
</template>

<script lang="ts">
import { defineComponent, nextTick, onMounted, ref } from "vue";
import { notice } from "@/utils/notice";
export default defineComponent({
  emits: ["createFolder"],
  props: {
    tableData: {
      type: Array,
    },
  },
  setup(props, context) {
    const input = ref("");
    const inputFocus = ref<HTMLElement>();
    const commit = () => {
      let flag = true;
      props.tableData?.forEach((item) => {
        if ((item as any).name === input.value) {
          flag = false;
        }
      });
      if (flag) {
        context.emit("createFolder", input.value);
      } else {
        notice("warning", "警告", "文件重名!");
      }
    };
    nextTick(() => {
      inputFocus.value?.focus();
    });

    return {
      input,
      commit,
      inputFocus,
    };
  },
});
</script>

<style lang="scss" scoped>
.folder-main {
  width: 180px;
  padding: 10px;
  background: #f0f0f0;
  .el-input {
    width: 100%;
    margin: 10px 0px;
  }
}
</style>