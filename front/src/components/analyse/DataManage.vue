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
import {
  defineComponent,
  ref,
  onMounted,
  computed,
  onBeforeUnmount,
} from "vue";
import { Search } from "@element-plus/icons-vue";
import {
  addProjectData,
  getData,
  delData,
  getAnalyticData,
  addDraw,
  delAnalyticData,
  addSection,
  addSectionCompare,
  addSectionFlush,
  checkState,
} from "@/api/request";
import { notice } from "@/utils/notice";
export default defineComponent({
  emits: ["operateLayer"],
  setup(_, context) {
    let sectionTimeout: any;
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

    const sectionName = computed(() => {
      if (treeData.value[treeData.value.length - 1].id != "") {
        return "断面形态0";
      } else {
        let max: number = 0;
        treeData.value[treeData.value.length - 1].children.forEach((item) => {
          if (
            item.label.substring(0, 5) === "断面形态" &&
            parseInt(item.label.substring(5)) != NaN
          ) {
            const number = parseInt(item.label.substring(5));
            if (number > max) {
              max = number;
            }
          }
        });
        if (max === 0) {
          return "断面形态0";
        } else {
          return "断面形态" + (max + 1);
        }
      }
    });

    const regionName = computed(() => {
      if (treeData.value[treeData.value.length - 1].id != "") {
        return "区域形态0";
      } else {
        let max: number = 0;
        treeData.value[treeData.value.length - 1].children.forEach((item) => {
          if (
            item.label.substring(0, 5) === "区域形态" &&
            parseInt(item.label.substring(5)) != NaN
          ) {
            const number = parseInt(item.label.substring(5));
            if (number > max) {
              max = number;
            }
          }
        });
        if (max === 0) {
          return "区域形态0";
        } else {
          return "区域形态" + (max + 1);
        }
      }
    });

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
          treeData.value.splice(treeData.value.length - 1, 0, {
            id: item.dataListId,
            label: item.dataListName,
            flag: true,
            children: [],
          });
          treeData.value[treeData.value.length - 2].children.push({
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
      if (jsonData.list.length > 0) {
        await addProjectData(jsonData);
      }
    };

    const addDrawData = async (param: { geoJson: any; visualType: string }) => {
      const fileName = JSON.parse(
        JSON.stringify(
          param.visualType === "geoJsonLine"
            ? sectionName.value
            : regionName.value
        )
      );
      const jsonData: {
        projectId: string;
        geoJson: any;
        fileName: string;
        visualType: string;
      } = {
        projectId: router.currentRoute.value.params.id as string,
        geoJson: param.geoJson,
        fileName: fileName,
        visualType: param.visualType,
      };
      const data = await addDraw(jsonData);
      if (data != null && (data as any).code === 0) {
        if (treeData.value[treeData.value.length - 1].id === "") {
          treeData.value[treeData.value.length - 1].children.push({
            id: data.data,
            label: fileName,
            flag: false,
            children: [],
            visualType: param.visualType,
            visualId: "",
          });
        } else {
          treeData.value.push({
            id: "",
            label: "分析数据集",
            flag: true,
            children: [],
          });
          treeData.value[treeData.value.length - 1].children.push({
            id: data.data,
            label: fileName,
            flag: false,
            children: [],
            visualType: param.visualType,
            visualId: "",
          });
        }
        return {
          id: data.data,
          name: fileName,
          visualType: param.visualType,
          visualId: "",
        };
      }
    };

    const addAnalyse = async (param: { type: string; value: any }) => {
      if (param.type === "section") {
        addData([param.value.dem]);
        context.emit("operateLayer", {
          content: {
            id: param.value.dem.fileId,
            name: param.value.dem.fileName,
            visualType: param.value.dem.visualType,
            visualId: param.value.dem.visualId,
          },
          type: "add",
        });
        const data = await addSection(
          router.currentRoute.value.params.id as string,
          param.value.section as string,
          param.value.dem.fileId
        );
        if (data != null && (data as any).code === 0) {
          await checkStateHandle(param.type, data.data, "断面形态");
        }
      } else if (param.type === "sectionCompare") {
        addData(param.value.demList);
        const demList: string[] = [];
        param.value.demList.forEach((item: any) => {
          demList.push(item.fileId);
          context.emit("operateLayer", {
            content: {
              id: item.fileId,
              name: item.fileName,
              visualType: item.visualType,
              visualId: item.visualId,
            },
            type: "add",
          });
        });
        const data = await addSectionCompare(
          router.currentRoute.value.params.id as string,
          param.value.section as string,
          demList
        );
        if (data != null && (data as any).code === 0) {
          await checkStateHandle(param.type, data.data, "断面比较");
        }
      } else if (param.type === "sectionFlush") {
        console.log(param);
        addData([param.value.benchmarkDem]);
        context.emit("operateLayer", {
          content: {
            id: param.value.benchmarkDem.fileId,
            name: param.value.benchmarkDem.fileName,
            visualType: param.value.benchmarkDem.visualType,
            visualId: param.value.benchmarkDem.visualId,
          },
          type: "add",
        });
        addData([param.value.referDem]);
        context.emit("operateLayer", {
          content: {
            id: param.value.referDem.fileId,
            name: param.value.referDem.fileName,
            visualType: param.value.referDem.visualType,
            visualId: param.value.referDem.visualId,
          },
          type: "add",
        });
        const data = await addSectionFlush(
          router.currentRoute.value.params.id as string,
          param.value.section,
          param.value.benchmarkDem.fileId,
          param.value.referDem.fileId
        );
        if (data != null && (data as any).code === 0) {
          await checkStateHandle(param.type, data.data, "断面冲淤");
        }
      }
    };

    const checkStateHandle = async (
      type: string,
      key: string,
      text: string
    ) => {
      const data = await checkState(key);
      if (data != null && (data as any).code === 0) {
        if (treeData.value[treeData.value.length - 1].id != "") {
          treeData.value.push({
            id: "",
            label: "分析数据集",
            children: [],
            flag: true,
          });
        }
        treeData.value[treeData.value.length - 1].children.push({
          id: data.data.id,
          label: data.data.fileName,
          flag: false,
          children: [],
          visualType: data.data.visualType,
          visualId: data.data.visualId,
        });
        notice("success", "成功", text + "计算成功！");
      } else if (data != null && (data as any).code === -1) {
        sectionTimeout = setTimeout(async () => {
          await checkStateHandle(type, key, text);
        }, 2000);
      } else {
        notice("error", "错误", text + "计算失败！");
      }
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
        let data;
        if (parentId.value === "") {
          data = await delAnalyticData(selectedData.value?.id as string);
        } else {
          data = await delData(
            router.currentRoute.value.params.id as string,
            parentId.value,
            selectedData.value?.id as string
          );
        }
        if (data != null && (data as any).code === 0) {
          for (let i = 0; i < treeData.value.length; i++) {
            if (treeData.value[i].id === parentId.value) {
              if (
                treeData.value[i].children.length === 0 ||
                treeData.value[i].children.length === 1
              ) {
                treeData.value.splice(i, 1);
                notice("success", "成功", "数据删除成功!");
                return;
              } else {
                for (let j = 0; j < treeData.value[i].children.length; j++) {
                  if (
                    treeData.value[i].children[j].id === selectedData.value?.id
                  ) {
                    treeData.value[i].children.splice(j, 1);
                    notice("success", "成功", "数据删除成功!");
                    return;
                  }
                }
              }
            }
          }
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
        selectedData.value?.visualType === "rasterTile" ||
        selectedData.value?.visualType === "geoJsonLine" ||
        selectedData.value?.visualType === "geoJsonPolygon"
      ) {
        return true;
      } else {
        return false;
      }
    });

    onMounted(async () => {
      const a = parseInt("dasdsa");
      console.log(a);
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
      const analyticData = await getAnalyticData(
        router.currentRoute.value.params.id as string
      );
      if (analyticData != null && (data as any).code === 0) {
        if (analyticData.data.length > 0) {
          treeData.value.push({
            id: "",
            label: "分析数据集",
            flag: true,
            children: [],
          });
          analyticData.data.forEach((item: any) => {
            treeData.value[treeData.value.length - 1].children.push({
              id: item.id,
              label: item.fileName,
              flag: false,
              children: [],
              visualType: item.visualType,
              visualId: item.visualId,
            });
          });
        }
      }
    });

    onBeforeUnmount(() => {
      clearTimeout(sectionTimeout);
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
      addDrawData,
      addAnalyse,
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