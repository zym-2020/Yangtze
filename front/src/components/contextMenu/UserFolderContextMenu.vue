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
    <div class="context" v-if="!instance.folder" @click="unPackClick">
      解压（暂只支持.zip文件解压）
    </div>
    <div class="context" @click="move">移动</div>
    <div class="context" @click="compress">添加到压缩文件</div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted } from "vue";
import {
  deleteFilesOrFolders,
  getDownloadURL,
  unPack,
  compressFile,
} from "@/api/request";
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
    selectList: {
      type: Array,
    },
  },
  emits: ["delSuccess", "rename", "unPack", "move"],
  setup(props, context) {
    const store = useStore();
    const instance = computed(() => {
      return props.contextMenuInstance;
    });

    const renameClick = () => {
      context.emit("rename");
    };

    const fileList = computed(() => {
      const list: string[] = [];
      props.selectList?.forEach((item: any) => {
        if (!item.folder) {
          list.push(item.id);
        }
      });
      return list;
    });

    const folderList = computed(() => {
      const list: string[] = [];
      props.selectList?.forEach((item: any) => {
        if (item.folder) {
          list.push(item.id);
        }
      });
      return list;
    });

    const compressName = computed(() => {
      if (props.selectList?.length == 0) {
        return (props.contextMenuInstance as any).name + ".zip";
      } else {
        return (props.contextMenuInstance as any).parentName + ".zip";
      }
    });

    const deleteClick = async () => {
      if (props.selectList?.length === 0) {
        if ((props.contextMenuInstance as any).folder) {
          const folders: string[] = [];
          folders.push((props.contextMenuInstance as any).id);
          ElMessageBox.confirm("确定删除文件夹及文件夹以下内容", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
            .then(async () => {
              const data = await deleteFilesOrFolders({
                files: [],
                folders: folders,
              });
              if (data != null) {
                if ((data as any).code === 0) {
                  notice("success", "成功", "删除成功!");
                  context.emit("delSuccess", folders);
                } else {
                  notice("error", "失败", "删除失败!");
                }
              }
            })
            .catch(() => {});
        } else {
          const files: string[] = [];
          files.push((props.contextMenuInstance as any).id);
          const data = await deleteFilesOrFolders({
            files: files,
            folders: [],
          });
          if (data != null) {
            if ((data as any).code === 0) {
              notice("success", "成功", "删除成功!");
              context.emit("delSuccess", files);
            } else {
              notice("error", "失败", "删除失败!");
            }
          }
        }
      } else {
        if (folderList.value.length > 0) {
          ElMessageBox.confirm(
            "所选列表包含文件夹，确定删除文件夹及文件夹以下内容",
            "警告",
            {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
            }
          )
            .then(async () => {
              const data = await deleteFilesOrFolders({
                files: fileList.value,
                folders: folderList.value,
              });
              if (data != null) {
                if ((data as any).code === 0) {
                  notice("success", "成功", "删除成功!");
                  context.emit(
                    "delSuccess",
                    fileList.value.concat(folderList.value)
                  );
                } else {
                  notice("error", "失败", "删除失败!");
                }
              }
            })
            .catch(() => {});
        } else {
          const data = await deleteFilesOrFolders({
            files: fileList.value,
            folders: folderList.value,
          });
          if (data != null) {
            if ((data as any).code === 0) {
              notice("success", "成功", "删除成功!");
              context.emit(
                "delSuccess",
                fileList.value.concat(folderList.value)
              );
            } else {
              notice("error", "失败", "删除失败!");
            }
          }
        }
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

    const unPackClick = async () => {
      console.log(props.contextMenuInstance);
      const data = await unPack({
        id: (props.contextMenuInstance as any).id,
        parentId: (props.contextMenuInstance as any).parent_id,
        level: (props.contextMenuInstance as any).level,
      });
      if (data != null) {
        if ((data as any).code === 0) {
          context.emit("unPack");
        }
      }
    };

    const move = () => {
      context.emit("move");
    };

    const compress = async () => {
      const jsonData: { files: string[]; folders: string[]; level: number; parentId: string; compressName: string } = {
        files: [],
        folders: [],
        level: (props.contextMenuInstance as any).level,
        parentId: (props.contextMenuInstance as any).parent_id,
        compressName: compressName.value
      };
      if (props.selectList?.length === 0) {
        if ((props.contextMenuInstance as any).folder) {
          jsonData.folders.push((props.contextMenuInstance as any).id);
        } else {
          jsonData.files.push((props.contextMenuInstance as any).id);
        }
      } else {
        jsonData.files = fileList.value
        jsonData.folders = folderList.value
      }
      const data = await compressFile(jsonData)
      if(data != null) {
        if((data as any).code === 0) {
          notice("success", "成功", "压缩成功")
        }
      }
    };

    return {
      instance,
      renameClick,
      deleteClick,
      downloadClick,
      releaseClick,
      unPackClick,
      move,
      compress,
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