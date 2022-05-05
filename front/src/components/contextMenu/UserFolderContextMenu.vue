<template>
  <div class="folder-context-menu-main">
    <div class="context" @click="renameClick">重命名</div>
    <div class="context" @click="deleteClick">删除</div>
    <div class="context">下载</div>
    <div class="context">权限管理</div>
    <div class="context">属性</div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted } from "vue";
import { rename, deleteFile } from "@/api/request";
import { notice } from "@/utils/notice";
export default defineComponent({
  props: {
    contextMenuInstance: {
      type: Object,
    },
  },
  emits: ["delSuccess"],
  setup(props, context) {
    const renameClick = () => {
      console.log(props.contextMenuInstance);
    };

    const deleteClick = async () => {
      console.log(props.contextMenuInstance);
      if (!(props.contextMenuInstance as any).folder) {
        const data = await deleteFile((props.contextMenuInstance as any).id);
        if (data != null) {
          if ((data as any).code === 0) {
            notice("success", "成功", "删除成功!");
            context.emit("delSuccess");
          } else {
            notice("error", "失败", "删除失败!");
          }
        }
      }
    };

    return {
      renameClick,
      deleteClick,
    };
  },
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