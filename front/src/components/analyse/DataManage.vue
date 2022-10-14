<template>
  <div class="data-manage">
    <div class="data-manage-body">
      <div class="input">
        <el-input v-model="serach" :suffix-icon="Search" />
      </div>
      <div class="content">
        <div class="scroll">
          <el-scrollbar>
            <el-tree
              :data="treeData"
              :props="defaultProps"
              @node-contextmenu="rightClick"
              default-expand-all
            >
              <template #default="{ data }">
                <div class="custom">
                  <div class="icon">
                    <el-icon v-if="data.flag"><FolderOpened /></el-icon>
                    <el-icon v-else><Document /></el-icon>
                  </div>
                  <div class="text">
                    <strong v-if="data.flag">{{ data.label }}</strong>
                    <span v-else>{{ data.label }}</span>
                  </div>
                </div>
              </template>
            </el-tree>
          </el-scrollbar>
        </div>
      </div>
      <div class="bottom">
        <strong style="margin-left: 30px">数据管理</strong>
      </div>
    </div>
    <div v-show="showRightMenu">
      <ul class="right-menu" ref="menu">
        <li
          :class="!isLayerVisual ? 'menu-item disabled' : 'menu-item'"
          @click="operateLayer('add')"
        >
          <span>添加至图层</span>
        </li>
        <li :class="isLayerVisual ? 'menu-item disabled' : 'menu-item'">
          <span>可视化</span>
        </li>
        <li class="menu-item" @click="operateLayer('del')">
          <span>删除</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script lang="ts">
type Tree = {
  id: string;
  flag: boolean;
  label: string;
  children: Tree[];
  visualType?: string;
  visualId?: string;
};
import router from "@/router";
import { defineComponent, ref, onMounted, computed } from "vue";
import { Search } from "@element-plus/icons-vue";
import { addProjectData, getData, delData } from "@/api/request";
import { notice } from "@/utils/notice";
export default defineComponent({
  emits: ["operateLayer"],
  setup(_, context) {
    const serach = ref("");

    const defaultProps = {
      children: "children",
      label: "label",
    };
    const treeData = ref<Tree[]>([]);
    const showRightMenu = ref(false);
    const menu = ref<HTMLElement>();
    const selectedData = ref<Tree>();
    const parentId = ref("");

    const addData = async (
      param: {
        fileId: string;
        fileName: string;
        dataListId: string;
        dataListName: string;
        visualId: string;
        visualType: string;
      }[]
    ) => {
      const jsonData: {
        projectId: string;
        list: { fileId: string; dataListId: string }[];
      } = {
        projectId: router.currentRoute.value.params.id as string,
        list: [],
      };
      param.forEach((item) => {
        let flag1 = true;
        for (let i = 0; i < treeData.value.length; i++) {
          if (treeData.value[i].id === item.dataListId) {
            let flag = true;
            for (let j = 0; j < treeData.value[i].children.length; j++) {
              if (treeData.value[i].children[j].id === item.fileId) {
                flag = false;
                break;
              }
            }
            if (flag) {
              treeData.value[i].children.push({
                id: item.fileId,
                label: item.fileName,
                flag: false,
                children: [],
                visualType: item.visualType,
                visualId: item.visualId,
              });
              jsonData.list.push({
                fileId: item.fileId,
                dataListId: item.dataListId,
              });
            }
            flag1 = false;
            break;
          }
        }
        if (flag1) {
          treeData.value.push({
            id: item.dataListId,
            label: item.dataListName,
            flag: true,
            children: [],
          });
          treeData.value[treeData.value.length - 1].children.push({
            id: item.fileId,
            label: item.fileName,
            flag: false,
            children: [],
            visualType: item.visualType,
            visualId: item.visualId,
          });
          jsonData.list.push({
            fileId: item.fileId,
            dataListId: item.dataListId,
          });
        }
      });
      await addProjectData(jsonData);
    };

    const operateLayer = async (keyword: string) => {
      context.emit("operateLayer", {
        content: {
          id: selectedData.value?.id,
          name: selectedData.value?.label,
          visualType: selectedData.value?.visualType,
          visualId: selectedData.value?.visualId,
        },
        type: keyword,
      });
      if (keyword === "del") {
        const data = await delData(
          router.currentRoute.value.params.id as string,
          parentId.value,
          selectedData.value?.id as string
        );
        if (data != null && (data as any).code === 0) {
          for (let i = 0; i < treeData.value.length; i++) {
            if (treeData.value[i].id === parentId.value) {
              if (
                treeData.value[i].children.length === 0 ||
                treeData.value[i].children.length === 1
              ) {
                treeData.value.splice(i, 1);
                return;
              } else {
                for (let j = 0; j < treeData.value[i].children.length; j++) {
                  if (
                    treeData.value[i].children[j].id === selectedData.value?.id
                  ) {
                    treeData.value[i].children.splice(j, 1);
                    return;
                  }
                }
              }
            }
          }
          notice("success", "成功", "数据删除成功!");
        }
      }
    };

    const rightClick = (event: any, data: Tree, node: any, obj: any) => {
      function closeRightMenu() {
        showRightMenu.value = false;
        document.removeEventListener("click", closeRightMenu);
      }
      if (!data.flag && menu.value) {
        showRightMenu.value = false;
        showRightMenu.value = true;
        selectedData.value = data;
        parentId.value = node.parent.data.id;
        menu.value.style.left = event.clientX - 15 + "px";
        menu.value.style.top = event.clientY - 125 + "px";
        document.addEventListener("click", closeRightMenu);
      }
    };

    const isLayerVisual = computed(() => {
      if (
        selectedData.value?.visualType === "polygonVectorTile3D" ||
        selectedData.value?.visualType === "pointVectorTile3D" ||
        selectedData.value?.visualType === "pointVectorTile" ||
        selectedData.value?.visualType === "lineVectorTile3D" ||
        selectedData.value?.visualType === "polygonVectorTile" ||
        selectedData.value?.visualType === "lineVectorTile" ||
        selectedData.value?.visualType === "png" ||
        selectedData.value?.visualType === "movePng" ||
        selectedData.value?.visualType === "rasterTile"
      ) {
        return true;
      } else {
        return false;
      }
    });

    onMounted(async () => {
      const data = await getData(router.currentRoute.value.params.id as string);
      if (data != null && (data as any).code === 0) {
        (data.data as any[]).forEach((item) => {
          let flag = true;
          for (let i = 0; i < treeData.value.length; i++) {
            if (treeData.value[i].id === item.dataListId) {
              treeData.value[i].children.push({
                id: item.fileId,
                label: item.fileName,
                flag: false,
                children: [],
                visualType: item.visualType,
                visualId: item.visualId,
              });
              flag = false;
            }
          }
          if (flag) {
            treeData.value.push({
              id: item.dataListId,
              label: item.dataListName,
              flag: true,
              children: [],
            });
            treeData.value[treeData.value.length - 1].children.push({
              id: item.fileId,
              label: item.fileName,
              flag: false,
              children: [],
              visualType: item.visualType,
              visualId: item.visualId,
            });
          }
        });
      }
    });

    return {
      serach,
      Search,
      addData,
      operateLayer,
      defaultProps,
      rightClick,
      showRightMenu,
      menu,
      treeData,
      isLayerVisual,
    };
  },
});
</script>


<style lang="scss" scoped>
.data-manage {
  padding: 5px;
  .data-manage-body {
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
        .el-tree {
          width: 100%;
          background: none;
          /deep/ .el-tree-node__content {
            height: 40px;
            width: 100%;
          }
          /deep/ .el-tree-node__children {
            .el-tree-node__content {
              width: calc(100% - 18px);
              overflow: hidden;
            }
          }
          /deep/ .el-tree-node__label {
            width: 100%;
            overflow: hidden;
          }
        }
        .custom {
          display: flex;
          .icon {
            width: 15px;
            margin-top: 12px;
            margin-right: 5px;
          }
          .text {
            line-height: 40px;
            width: calc(100% - 20px);
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
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
  .right-menu {
    z-index: 1;
    position: absolute;
    width: 100px;
    position: absolute;
    border-radius: 5px;
    border: 1px solid #ccc;
    background-color: white;
    cursor: pointer;
    li:hover {
      background-color: #edf6ff;
      color: #606266;
    }
    .menu-item {
      line-height: 25px;
      height: 25px;
      text-align: left;
      margin: 10px 0px;
      font-size: 14px;
      color: #606266;
      span {
        margin-left: 5px;
      }
    }
    .disabled {
      background: white;
      color: #c7d0dc;
      &:hover {
        background: white;
        color: #c7d0dc;
      }
    }
  }
}
</style>