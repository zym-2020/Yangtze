<template>
  <div class="folder-context-menu-main">
    <div class="context" @click="renameClick">重命名</div>
    <div class="context" @click="deleteClick">删除</div>
    <div class="context" v-if="!instance.folder" @click="downloadClick">
      下载
    </div>
    <div class="context" v-if="!instance.folder" @click="releaseClick">
      共享此文件
    </div>
    <div class="context" v-if="!instance.folder">解压</div>

    <div class="context">属性</div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted } from "vue";
import { deleteFile, deleteFolder, getDownloadURL } from "@/api/request";
import { notice } from "@/utils/notice";
import { ElMessageBox } from "element-plus";
import { useStore } from "@/store";
import { decrypt } from "@/utils/auth";
import router from "@/router";
export default defineComponent({
  props: {
    contextMenuInstance: {
      type: Object,
    },
  },
  emits: ["delSuccess", "rename"],
  setup(props, context) {
    const store = useStore();
    const instance = computed(() => {
      return props.contextMenuInstance;
    });

    const renameClick = () => {
      context.emit("rename");
    };

    const deleteClick = async () => {
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
      } else {
        ElMessageBox.confirm("确定删除文件夹及文件夹以下内容", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(async () => {
            const data = await deleteFolder(
              (props.contextMenuInstance as any).id
            );
            if (data != null) {
              if ((data as any).code === 0) {
                notice("success", "成功", "删除成功!");
                context.emit("delSuccess");
              } else {
                notice("error", "失败", "删除失败!");
              }
            }
          })
          .catch(() => {});
      }
    };

    const downloadClick = async () => {
      const data = await getDownloadURL((props.contextMenuInstance as any).id);
      if (data != null) {
        if ((data as any).code === 0) {
          window.location.href =
            "http://172.21.213.177:8002/download/downloadLocalFile/" +
            decrypt(data.data, store.state.user.id);
        } else {
          notice("error", "错误", (data as any).msg);
        }
      }
    };

    const releaseClick = () => {
      router.push({
        name: "share",
        params: {
          originFileAddress: (props.contextMenuInstance as any).address,
          originFileName: (props.contextMenuInstance as any).name,
        },
      });
    };

    return {
      instance,
      renameClick,
      deleteClick,
      downloadClick,
      releaseClick,
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