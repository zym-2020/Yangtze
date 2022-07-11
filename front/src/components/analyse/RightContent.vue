<template>
  <div class="right-content">
    <div class="title">图层管理</div>
    <div class="tools">
      <div class="tool" @click="addFlag = true">
        <svg>
          <title>添加数据</title>
          <use xlink:href="#icon-wenjianjiawenjian"></use>
        </svg>
      </div>
      <el-divider direction="vertical" />
      <div class="tool" @click="createFlag = true">
        <svg>
          <title>创建项目</title>
          <use xlink:href="#icon-chuangjianrenwu"></use>
        </svg>
      </div>
      <el-divider direction="vertical" />
      <div class="tool" @click="manageFlag = true">
        <svg>
          <title>项目列表</title>
          <use xlink:href="#icon-liebiao-2"></use>
        </svg>
      </div>
      <el-divider direction="vertical" />
      <div class="tool" @click="toolClick">
        <svg>
          <title>工具</title>
          <use xlink:href="#icon-gongjuxiang"></use>
        </svg>
      </div>
    </div>
    <div class="tree">
      <el-scrollbar>
        <div v-if="view">
          <el-tree
            :data="treeData"
            :props="defaultProps"
            :default-expand-all="true"
            draggable
            :allow-drop="allowDrop"
            @node-drop="sortLayer"
            @node-contextmenu="layerContextClick"
            v-if="treeData[0].children.length > 0"
          >
            <template #default="{ node, data }">
              <div class="custom-tree-node">
                <span>{{ node.label }}</span>
                <div class="icon" v-if="data.type != 'project'">
                  <svg
                    style="width: 20px; height: 20px"
                    v-if="data.show"
                    @click.stop="hideLayer(data)"
                  >
                    <use xlink:href="#icon-view1"></use>
                  </svg>
                  <svg
                    style="width: 20px; height: 20px"
                    v-else
                    @click.stop="showLayer(data)"
                  >
                    <use xlink:href="#icon-hide"></use>
                  </svg>
                </div>
              </div>
            </template>
          </el-tree>
          <div v-else>
            <el-empty description="暂无图层信息！" />
          </div>
        </div>
        <div v-else>
          <el-tree
            :data="dataView"
            :props="defaultProps"
            :default-expand-all="true"
          >
            <template #default="{ node, data }">
              <div class="custom-tree-node">
                <div class="data-icon" v-if="data.children == undefined">
                  <svg
                    style="width: 20px; height: 20px"
                    v-if="data.icon != undefined"
                  >
                    <use :xlink:href="data.icon"></use>
                  </svg>
                  <img :src="data.img" alt="" height="18" width="18" v-else />
                </div>
                <div class="label">
                  <div>{{ node.label }}</div>
                  <div v-if="data.type === 'classify'" class="classify">
                    {{ data.children.length }}
                  </div>
                </div>
              </div>
            </template>
          </el-tree>
        </div>
      </el-scrollbar>
    </div>
    <div class="footer">
      <div @click="change">
        <svg>
          <use xlink:href="#icon-qiehuangongsi"></use>
        </svg>
      </div>
      <div>当前视图：图层视图</div>
    </div>

    <el-dialog v-model="createFlag" width="600px" :show-close="false">
      <create-project @createProject="createProject"></create-project>
    </el-dialog>

    <el-dialog v-model="addFlag" width="600px" :show-close="false">
      <add-data
        v-if="addFlag"
        @returnData="returnData"
        :layers="treeData[0].children"
      />
    </el-dialog>

    <el-dialog v-model="manageFlag" width="600px" :show-close="false">
      <project-manage v-if="manageFlag" @selectProjectId="selectProjectId" />
    </el-dialog>

    <layer-context-menu
      class="layer-context-menu"
      v-show="layerContextMenuFlag"
      :contextItem="contextItem"
      @delLayer="delContextLayer"
      @showChirt="showResult"
    ></layer-context-menu>

    <el-dialog v-model="sectionShow" width="700px" :modal="false">
      <section-show
        :sectionValue="sectionValue"
        v-if="sectionShow"
      ></section-show>
    </el-dialog>

    <el-dialog v-model="sectionContrastShow" width="700px" :modal="false">
      <section-contrast-show
        v-if="sectionContrastShow"
        :sectionContrastValue="sectionContrastValue"
      ></section-contrast-show>
    </el-dialog>

    <el-dialog v-model="areaElevationShow" width="700px" :modal="false">
      <area-elevation-show
        v-if="areaElevationShow"
        :areaElevationValue="areaElevationValue"
      ></area-elevation-show>
    </el-dialog>

    <el-dialog v-model="flushFiltShow" width="700px" :modal="false">
      <flush-filt-show
        v-if="flushFiltShow"
        :flushFiltValue="flushFiltValue"
      ></flush-filt-show>
    </el-dialog>
  </div>
</template>

<script lang="ts">
interface Tree {
  name: string;
  children: Tree[];
  id: string;
  type: string;
  geoJson?: any;

  tableName?: string;
  vectorType?: string;
  show?: boolean;
  sections?: {
    id: string;
    sectionId: string;
    state: number;
  }[];
}

import {
  computed,
  defineComponent,
  onMounted,
  ref,
  watch,
  reactive,
} from "vue";
import CreateProject from "@/components/tools/CreateProject.vue";
import AddData from "@/components/tools/AddData.vue";
import ProjectManage from "@/components/tools/ProjectManage.vue";
import router from "@/router";
import { computedDataView } from "@/components/tools/ts/leftToolTreeData";
import SectionShow from "@/components/projectDialog/SectionShow.vue";
import { getSectionValue, delLayer } from "@/api/request";
import LayerContextMenu from "@/components/contextMenu/LayerContextMenu.vue";
import { notice } from "@/utils/notice";
import sectionContrastShow from "@/components/projectDialog/SectionContrastShow.vue";
import areaElevationShow from "@/components/projectDialog/AreaElevationShow.vue";
import flushFiltShow from "@/components/projectDialog/FlushFiltShow.vue";
export default defineComponent({
  emits: ["setVisible", "setLayers", "toolClick", "deleteLayer"],
  components: {
    CreateProject,
    AddData,
    ProjectManage,
    SectionShow,
    LayerContextMenu,
    sectionContrastShow,
    areaElevationShow,
    flushFiltShow,
  },
  setup(_, context) {
    const createFlag = ref(false);
    const addFlag = ref(false);
    const manageFlag = ref(false);
    const view = ref(true);
    const sectionShow = ref(false);
    const sectionContrastShow = ref(false);
    const layerContextMenuFlag = ref(false);
    const contextItem = ref<any>(null);
    const areaElevationShow = ref(false);
    const flushFiltShow = ref(false);

    const sectionValue = reactive({
      value: [],
      demLayers: [],
    });
    const sectionContrastValue = reactive({
      dems: [] as any[],
      data: [],
    });
    const areaElevationValue = reactive({
      data: [],
      dems: [],
    });
    const flushFiltValue = reactive({
      data: [],
      dems: [],
    });

    const defaultProps = {
      children: "children",
      label: "name",
    };

    const treeData = ref<Tree[]>([
      {
        name: "",
        id: "",
        type: "project",
        children: [],
      },
    ]);
    const sortLayers = ref<string[]>([]);

    const dataView = computed(() => {
      const result = [
        {
          name: (router.currentRoute.value.params.projectInfo as any)
            .projectName,
          children: computedDataView(treeData.value[0].children),
        },
      ];
      return result;
    });

    const init = () => {
      treeData.value[0].name = (
        router.currentRoute.value.params.projectInfo as any
      ).projectName;
      treeData.value[0].id = (
        router.currentRoute.value.params.projectInfo as any
      ).id;
      treeData.value[0].children = (
        router.currentRoute.value.params.projectInfo as any
      ).layers;

      treeData.value[0].children.forEach((item) => {
        item.show = true;
      });

      sortLayers.value = (
        router.currentRoute.value.params.projectInfo as any
      ).sortLayers;
    };

    const listClick = () => {
      if (router.currentRoute.value.params.id === "62ba63044bf8161735590782") {
        router.push({
          name: "project",
          params: { id: "62b9b176890c8501bf5dd77f" },
        });
      } else {
        router.push({
          name: "project",
          params: { id: "62ba63044bf8161735590782" },
        });
      }
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
    const sortLayer = (draggingNode: any, dropNode: any, type: string) => {
      //   console.log(draggingNode, dropNode, type);
    };

    const hideLayer = (data: any) => {
      data.show = false;
      context.emit("setVisible", { id: data.id, visibility: false });
    };

    const showLayer = (data: any) => {
      data.show = true;
      context.emit("setVisible", { id: data.id, visibility: true });
    };

    const createProject = (val: any) => {
      createFlag.value = false;
      router.push({
        name: "project",
        params: {
          id: val.id,
          projectInfo: JSON.stringify(val),
        },
      });
    };

    const returnData = (val: any[]) => {
      addFlag.value = false;
      const tempArr: any[] = [];
      val.forEach((item) => {
        item.show = true;
        treeData.value[0].children.push(item);
        sortLayers.value.push(item.id);
        tempArr.push({ id: item.id, type: item.type, isAdd: true });
      });
      context.emit("setLayers", tempArr);
    };

    const selectProjectId = () => {
      manageFlag.value = false;
    };

    const change = () => {
      view.value = !view.value;
    };

    const toolClick = () => {
      context.emit("toolClick");
    };

    const addLayer = (layer: any) => {
      treeData.value[0].children.push(layer);
      sortLayers.value.push(layer.id);
    };

    const delContextLayer = async (val: string) => {
      const data = await delLayer(
        router.currentRoute.value.params.id as string,
        val
      );
      if (data != null) {
        if ((data as any).code === 0) {
          let index = -1;
          for (let i = 0; i < treeData.value[0].children.length; i++) {
            if (treeData.value[0].children[i].id === val) {
              index = i;
              console.log("index", index);
            }
          }
          console.log(index);
          if (index != -1) {
            treeData.value[0].children.splice(index, 1);
            console.log(treeData.value);
          }
          notice("success", "成功", "删除成功");
          context.emit("deleteLayer", val);
        }
      }
    };

    const updateLayer = (layer: any) => {
      for (let i = 0; i < treeData.value[0].children.length; i++) {
        if (treeData.value[0].children[i].id === layer.id) {
          console.log("layer", layer);
          layer.sections.forEach((item: any) => {
            item.state = 1;
          });
          treeData.value[0].children[i].sections = layer.sections;
          console.log("treeData", treeData.value[i]);
        }
      }
    };

    const showResult = async (params: { layer: any; type: string }) => {
      const valueIds: any[] = [];
      const dems: any[] = [];
      params.layer.sections.forEach((item: any) => {
        if (item.state === 1) {
          valueIds.push(item.id);
          treeData.value[0].children.forEach((layer) => {
            if (layer.id === item.sectionId) {
              dems.push({
                demId: layer.id,
                valueId: item.id,
                name: layer.name,
              });
            }
          });
        }
      });
      const data = await getSectionValue(
        router.currentRoute.value.params.id as string,
        params.layer.id,
        valueIds
      );
      if (params.type === "section") {
        if (data != null) {
          if ((data as any).code === 0) {
            sectionValue.value = data.data;
            sectionValue.demLayers = dems as any;
            sectionShow.value = true;
          }
        }
      } else if (params.type === "sectionContrast") {
        if (data != null) {
          if ((data as any).code === 0) {
            sectionContrastValue.dems = dems;
            sectionContrastValue.data = data.data;
            sectionContrastShow.value = true;
          }
        }
      } else if (params.type === "area-elevation") {
        if (data != null) {
          if ((data as any).code === 0) {
            areaElevationValue.data = data.data;
            areaElevationValue.dems = dems as any;
            areaElevationShow.value = true;
          }
        }
      } else if (params.type === "flush-silt") {
        if (data != null) {
          if ((data as any).code === 0) {
            flushFiltValue.data = data.data
            flushFiltValue.dems = dems as any
            flushFiltShow.value = true
          }
        }
      }
    };

    const layerContextClick = (event: any, data: any) => {
      layerContextMenuFlag.value = false;
      if (data.type != "project") {
        contextItem.value = data;
        layerContextMenuFlag.value = true;
        const menu: any = document.querySelector(".layer-context-menu");
        const content = document.querySelector(".right") as HTMLElement;
        menu.style.left = event.clientX - content.offsetLeft - 20 + "px";
        menu.style.top = event.clientY - content.offsetTop - 60 + "px";
        console.log(event);
        function closeMenu() {
          layerContextMenuFlag.value = false;
          document.removeEventListener("click", closeMenu);
        }
        document.addEventListener("click", closeMenu);
      }
    };

    watch(
      () => {
        return router.currentRoute.value.path;
      },
      (newVal) => {
        if (router.currentRoute.value.name === "project") {
          init();
        }
      }
    );

    onMounted(() => {
      console.log("init");
      init();
    });

    return {
      addFlag,
      createFlag,
      manageFlag,
      defaultProps,
      treeData,
      sortLayer,
      listClick,
      sortLayers,
      allowDrop,
      hideLayer,
      showLayer,
      createProject,
      returnData,
      selectProjectId,
      view,
      change,
      dataView,
      toolClick,
      addLayer,
      sectionShow,
      sectionValue,
      showResult,
      updateLayer,
      layerContextClick,
      layerContextMenuFlag,
      contextItem,
      delContextLayer,
      sectionContrastShow,
      sectionContrastValue,
      areaElevationValue,
      areaElevationShow,
      flushFiltShow,
      flushFiltValue,
    };
  },
});
</script>

<style lang="scss" scoped>
.right-content {
  height: 100%;
  position: relative;
  .title {
    padding: 0 10px;
    color: white;
    height: 30px;
    line-height: 30px;
    border-bottom: solid 1px rgba($color: #f5f4ee, $alpha: 0.5);
    box-sizing: border-box;
  }
  .tools {
    height: 30px;
    padding: 0 10px;
    display: flex;
    .tool {
      width: 30px;
      cursor: pointer;
      border-radius: 6px;
      svg {
        height: 20px;
        width: 20px;
        margin-top: 5px;
        margin-left: 5px;
      }

      &:hover {
        background: rgba($color: #f5dd4f, $alpha: 0.5);
      }
    }
    .el-divider {
      margin-top: 7px;
    }
  }
  .tree {
    height: calc(100% - 90px);
    .el-scrollbar {
      height: 100%;
      padding: 0 15px;
    }
    .el-tree {
      background: none;
      color: white;
      /deep/ .el-tree-node {
        margin-bottom: 5px;
        .is-leaf + .el-checkbox .el-checkbox__inner {
          display: inline-block;
        }
        .el-checkbox .el-checkbox__inner {
          display: none;
        }
      }
      /deep/ .el-tree-node__content:hover {
        background: rgba($color: #4a9de7, $alpha: 0.5);
        color: white;
      }
      /deep/ .el-tree-node:focus > .el-tree-node__content {
        background-color: #4a9de7 !important;
        color: white !important;
      }
      .custom-tree-node {
        width: 100%;
        display: flex;
        position: relative;
        .icon {
          position: absolute;
          right: 5px;
        }
        .data-icon {
          margin-right: 5px;
        }
        .label {
          display: flex;
          .classify {
            margin-left: 5px;
            margin-top: 2px;
            background: #d7d6d6;
            height: 18px;
            width: 18px;
            text-align: center;
            border-radius: 50%;
            line-height: 18px;
            color: black;
          }
        }
      }
    }
  }
  .footer {
    padding: 0 10px;
    color: white;
    height: 30px;
    line-height: 30px;
    border-top: solid 1px rgba($color: #f5f4ee, $alpha: 0.5);
    box-sizing: border-box;
    display: flex;
    svg {
      width: 20px;
      height: 20px;
      margin-top: 5px;
      margin-right: 10px;
      cursor: pointer;
    }
  }

  /deep/.el-dialog {
    .el-dialog__header {
      padding: 0;
    }
    .el-dialog__body {
      padding: 0;
    }
  }
  .layer-context-menu {
    position: absolute;
    z-index: 100;
  }
}
</style>