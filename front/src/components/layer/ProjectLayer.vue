<template>
  <div class="pro">
    <div class="head">项目管理</div>
    <div class="icon">
      <div class="left">
        <svg style="width: 20px; height: 20px" @click="open">
          <title>打开工程</title>
          <use xlink:href="#icon-wenjianjia"></use>
        </svg>
        <el-divider direction="vertical" />
        <svg style="width: 20px; height: 20px; margin-right: 100px">
          <title>移除工程</title>
          <use xlink:href="#icon-wenjianjiashanchu"></use>
        </svg>
      </div>

      <div class="right">
        <svg style="width: 20px; height: 20px">
          <title>创建工程</title>
          <use xlink:href="#icon-chuangjianrenwu"></use>
        </svg>
        <el-divider direction="vertical" />
        <svg style="width: 20px; height: 20px">
          <title>项目管理</title>
          <use xlink:href="#icon-liebiao-2"></use>
        </svg>
      </div>
    </div>
    <div class="detail-padding">
      <div class="detail">
        <el-tree
          :data="resource"
          :props="defaultProps"
          :highlight-current="true"
          :default-expand-all="true"
          @node-contextmenu="contextmenuClick"
        />
      </div>
    </div>

    <section-context-menu
      v-show="sectionContextMenuFlag"
      class="section-context-menu"
      :contextData="contextData"
      @sendSectionValue="sendSectionValue"
      :projectName="projectName"
    ></section-context-menu>
    <section-contrast-context-menu
      v-show="sectionContrastContextMenuFlag"
      class="section-contrast-context-menu"
      :sectionContrastContextData="sectionContrastContextData"
      @sendSectionContrastValue="sendSectionContrastValue"
      :projectName="projectName"
    >
    </section-contrast-context-menu>
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

    <!-- <el-dialog v-model="openFlag" width="600px" :show-close="false">
      <open-project v-if="openFlag" @selectProjectId="selectProjectId" />
    </el-dialog> -->
  </div>
</template>

<script lang="ts">
import { defineComponent, computed, ref, reactive } from "vue";
import { computedResource } from "@/utils/common";
import { notice } from "@/utils/notice";
import OpenProject from "@/components/projectDialog/OpenProject.vue";
import SectionContextMenu from "@/components/contextMenu/SectionContextMenu.vue";
import SectionContrastContextMenu from "@/components/contextMenu/SectionContrastContextMenu.vue";
import SectionShow from "@/components/projectDialog/SectionShow.vue";
import SectionContrastShow from "@/components/projectDialog/SectionContrastShow.vue";

interface Children {
  id?: string;
  label: string;
  children: Children[];
  type?: string;
  classify?: string;
  show?: boolean;
  classifyCount?: number;
  selectDemId?: string;
  selectDemName?: string;
  selectDemIds?: string[];
  selectDemNames?: string[];
  nodeType?: string;
}
export default defineComponent({
  props: {
    projectName: {
      type: String,
    },
  },
  components: {
    OpenProject,
    SectionContextMenu,
    SectionShow,
    SectionContrastContextMenu,
    SectionContrastShow,
  },

  setup(props) {
    const projectName = computed(() => {
      return props.projectName
    })
    const flag = ref(false);
    const openFlag = ref(false);
    const sectionContextMenuFlag = ref(false);
    const sectionContrastContextMenuFlag = ref(false);
    const selectId = ref("");
    const sectionShow = ref(false);
    const sectionContrastShow = ref(false);
    const contextData = ref({});
    const sectionContrastContextData = ref({});

    const sectionContrastValue = reactive({
      id: "",
      name: "",
      value: [],
    });

    const sectionValue = reactive({
      name: "",
      id: "",
      value: [],
      selectDemId: "",
      selectDemName: "",
    });
    const resource = computed(() => {
      const result: Children[] = [
        {
          label: props.projectName as string,
          children: [
            {
              label: "基础数据",
              children: [],
            },
            {
              label: "分析结果",
              children: [],
            },
          ],
        },
      ];
      computedResource(result);
      return result;
    });
    const open = () => {
      openFlag.value = true;
    };
    const defaultProps = {
      children: "children",
      label: "label",
    };

    // const selectProjectId = async () => {
    //   if (
    //     getCurrentProjectId() != null &&
    //     selectId.value != getCurrentProjectId()
    //   ) {
    //     selectId.value = getCurrentProjectId() as string;
    //     let data = await getResult(selectId.value);
    //     if (data != null) {
    //       let temp: ResourceState = JSON.parse(data.data);
    //       classify(temp);
    //       flag.value = true;
    //     }
    //   }
    //   openFlag.value = false;
    // };

    const sendSectionValue = (val: { code: number; data: []; msg: [] }) => {
      sectionValue.name = (contextData.value as any).label;
      sectionValue.id = (contextData.value as any).id;
      sectionValue.selectDemId = (contextData.value as any).selectDemId;
      sectionValue.selectDemName = (contextData.value as any).selectDemName;
      if (val.code === 100) {
        notice("warning", "计算中", "断面计算中，请稍后！");
        sectionValue.value = [];
        sectionShow.value = false;
      } else if (val.code === 0) {
        sectionValue.value = val.data;
        sectionShow.value = true;
      } else {
        notice("error", "错误", "断面计算错误");
        sectionValue.value = [];
        sectionShow.value = false;
      }
    };

    const sendSectionContrastValue = (val: {
      code: number;
      data: [];
      msg: string;
    }) => {
      sectionContrastValue.id = (sectionContrastContextData.value as any).id;
      sectionContrastValue.name = (
        sectionContrastContextData.value as any
      ).label;
      if (val.code === 100) {
        notice("warning", "计算中", "断面计算中，请稍后！");
        sectionContrastValue.value = [];
        sectionContrastShow.value = false;
      } else if (val.code === 0) {
        sectionContrastValue.value = val.data;
        sectionContrastShow.value = true;
      } else {
        notice("error", "错误", "断面计算错误");
        sectionContrastValue.value = [];
        sectionContrastShow.value = false;
      }
    };

    const contextmenuClick = (event: any, data: any) => {
      sectionContextMenuFlag.value = false;
      sectionContrastContextMenuFlag.value = false;
      if (data.nodeType === "section") {
        contextData.value = data;
        sectionContextMenuFlag.value = true;
        const menu: any = document.querySelector(".section-context-menu");
        const pro = document.querySelector(".pro") as HTMLElement;
        menu.style.left = event.clientX - pro.offsetLeft + "px";
        menu.style.top = event.clientY - pro.offsetTop + "px";
        function closeMenu() {
          sectionContextMenuFlag.value = false;
          document.removeEventListener("click", closeMenu);
        }
        document.addEventListener("click", closeMenu);
      } else if (data.nodeType === "sectionContrast") {
        sectionContrastContextData.value = data;
        sectionContrastContextMenuFlag.value = true;
        const menu: any = document.querySelector(
          ".section-contrast-context-menu"
        );
        const pro = document.querySelector(".pro") as HTMLElement;
        menu.style.left = event.clientX - pro.offsetLeft + "px";
        menu.style.top = event.clientY - pro.offsetTop + "px";
        function closeMenu() {
          sectionContrastContextMenuFlag.value = false;
          document.removeEventListener("click", closeMenu);
        }
        document.addEventListener("click", closeMenu);
      }
    };

    return {
      projectName,
      open,
      contextData,
      sectionContrastContextData,
      sectionShow,
      sectionContrastShow,
      defaultProps,
      contextmenuClick,
      resource,
      sendSectionValue,
      sendSectionContrastValue,
      sectionContextMenuFlag,
      sectionContrastContextMenuFlag,
      sectionValue,
      sectionContrastValue
      // selectProjectId
    };
  },
});
</script>

<style lang="scss" scoped>
.pro {
  //   width: 100%;
  border-left: solid 0.5px #7080a5;
  background: #f0f0f0;
  position: relative;
  .head {
    background: rgba($color: #c8d5e3, $alpha: 0.4);
    height: 30px;
    line-height: 30px;
    padding-left: 10px;
  }
  .icon {
    height: 20px;
    line-height: 20px;
    padding-top: 5px;
    display: flex;
    position: relative;
    .left {
      margin-left: 10px;
    }
    .right {
      position: absolute;
      right: 10px;
    }
    .el-divider {
      margin-bottom: 8px;
    }
    svg {
      cursor: pointer;
    }
  }
  .detail-padding {
    padding: 5px;
    .detail {
      background: white;
      border: solid 0.5px;
      height: calc(100vh - 130px);
    }
  }

  /deep/.el-dialog {
    .el-dialog__header {
      padding: 0;
    }
    .el-dialog__body {
      padding: 0;
    }
    // .el-dialog__headerbtn {
    //   height: 40px;
    //   width: 40px;
    // }
  }
}
.section-context-menu,
.section-contrast-context-menu {
  position: absolute;
  z-index: 99;
  top: 0;
}
</style>