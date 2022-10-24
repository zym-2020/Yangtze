<template>
  <div class="layer-manage">
    <div class="bottom-resize"></div>
    <div class="layer-manage-body">
      <div class="input">
        <el-input v-model="serach" :suffix-icon="Search" />
      </div>
      <div class="content">
        <div class="scroll">
          <el-scrollbar>
            <div>
              <el-tree
                :data="treeData"
                :props="defaultProps"
                draggable
                :allow-drop="allowDrop"
                @node-drop="dragHandle"
              >
                <template #default="{ data }">
                  <div class="custom">
                    <el-checkbox
                      v-model="data.flag"
                      size="large"
                      @change="changeClick(data)"
                      >{{ data.label }}</el-checkbox
                    >
                    <div class="close" @click="closeClick(data.id)">
                      <el-icon><CircleClose /></el-icon>
                    </div>
                  </div>
                </template>
              </el-tree>
            </div>
          </el-scrollbar>
        </div>
      </div>
      <div class="bottom">
        <strong style="margin-left: 30px">图层管理</strong>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
type Tree = {
  id: string;
  label: string;
  visualType: string;
  visualId: string;
  flag: boolean;
  children: Tree[];
};
import { defineComponent, onMounted, ref } from "vue";
import { Search, Delete } from "@element-plus/icons-vue";
import { updateLayer } from "@/api/request";
import router from "@/router";
import { notice } from "@/utils/notice";
import type { DragEvents } from "element-plus/es/components/tree/src/model/useDragNode";

export default defineComponent({
  props: {
    layerList: {
      type: Array,
    },
  },
  emits: ["closeLayer", "hideLayer", "moveLayer"],
  setup(props, context) {
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const serach = ref("");
    const treeData = ref<Tree[]>([]);

    const addLayer = async (val: {
      id: string;
      name: string;
      visualType: string;
      visualId: string;
    }) => {
      for (let i = 0; i < treeData.value.length; i++) {
        if (treeData.value[i].id === val.id) {
          return;
        }
      }
      treeData.value.unshift({
        id: val.id,
        label: val.name,
        visualType: val.visualType,
        children: [],
        flag: true,
        visualId: val.visualId,
      });
      const list: string[] = [];
      treeData.value.forEach((item) => {
        list.push(item.id);
      });
      const data = await updateLayer(
        router.currentRoute.value.params.id as string,
        list
      );
    };

    const closeClick = async (id: string) => {
      for (let i = 0; i < treeData.value.length; i++) {
        if (treeData.value[i].id === id) {
          treeData.value.splice(i, 1);
          const list: string[] = [];
          treeData.value.forEach((item) => {
            list.push(item.id);
          });
          const data = await updateLayer(
            router.currentRoute.value.params.id as string,
            list
          );
          if (data != null && (data as any).code === 0) {
            notice("success", "成功", "已移除图层");
          }
          context.emit("closeLayer", id);
          return;
        }
      }
    };

    const delLayer = async (param: string) => {
      for (let i = 0; i < treeData.value.length; i++) {
        if (treeData.value[i].id === param) {
          treeData.value.splice(i, 1);
          const list: string[] = [];
          treeData.value.forEach((item) => {
            list.push(item.id);
          });
          await updateLayer(
            router.currentRoute.value.params.id as string,
            list
          );
          return;
        }
      }
    };

    const changeClick = (val: Tree) => {
      context.emit("hideLayer", { flag: val.flag, id: val.id });
    };

    const allowDrop = (draggingNode: any, dropNode: any, type: string) => {
      if (draggingNode.level === dropNode.level) {
        if (draggingNode.parent.id === dropNode.parent.id) {
          // 向上拖拽 || 向下拖拽
          return type === "prev" || type === "next";
        }
      } else {
        // 不同级进行处理
        return false;
      }
    };

    const dragHandle = async (
      node: any,
      dropNode: any,
      dropType: any,
      ev: DragEvents
    ) => {
      let target = "";
      console.log(dropType);
      if (dropType === "before") {
        for (let i = 0; i < treeData.value.length; i++) {
          if (treeData.value[i].id === node.data.id) {
            if (i != 0) {
              target = treeData.value[i + 1].id;
            }
            break;
          }
        }
      } else {
        target = dropNode.data.id;
      }
      context.emit("moveLayer", {
        drop: node.data.id,
        target: target,
      });
      const list: string[] = [];
      treeData.value.forEach((item) => {
        list.push(item.id);
      });
      await updateLayer(router.currentRoute.value.params.id as string, list);
    };

    const getLayers = () => {
      return treeData.value
    }

    onMounted(() => {
      props.layerList?.forEach((item: any) => {
        treeData.value.push({
          id: item.id,
          label: item.fileName,
          visualType: item.visualType,
          flag: true,
          children: [],
          visualId: item.visualId,
        });
      });
    });

    return {
      Search,
      serach,
      treeData,
      defaultProps,
      addLayer,
      Delete,
      closeClick,
      changeClick,
      delLayer,
      dragHandle,
      allowDrop,
      getLayers
    };
  },
});
</script>

<style lang="scss" scoped>
.layer-manage {
  position: relative;
  padding: 5px;
  .bottom-resize {
    height: 5px;
    position: absolute;
    top: 0;
    left: 0;
    cursor: row-resize;
    width: 100%;
  }
  .layer-manage-body {
    background: #f0f0f0;
    height: 100%;
    border-radius: 8px;
    .input {
      padding: 5px 10px;
    }
    .content {
      padding: 0 10px;
      height: calc(100% - 90px);
      .scroll {
        height: 100%;
        /deep/ .el-scrollbar__wrap {
          overflow-x: hidden;
        }
        /deep/.el-scrollbar__bar.is-horizontal {
          display: none;
        }
        .custom {
          position: relative;
          width: 100%;
          .el-checkbox {
            width: 100%;
            /deep/ .el-checkbox__label {
              width: calc(100% - 60px);
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
            }
          }

          .close {
            position: absolute;
            right: 10px;
            top: 13px;
          }
        }
        .el-tree {
          width: 100%;
          background: none;
          /deep/ .el-tree-node__content {
            height: 40px;
            width: 100%;
          }
          /deep/ .el-tree-node__label {
            width: 100%;
            overflow: hidden;
          }
        }
      }
    }
    .bottom {
      height: 40px;
      margin-top: 10px;
      border-bottom-left-radius: 8px;
      border-bottom-right-radius: 8px;
      background: rgba($color: #abadb3, $alpha: 0.5);
      line-height: 40px;
      font-size: 20px;
    }
  }
}
</style>