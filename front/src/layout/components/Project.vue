<template>
  <div class="pro">
    <div class="head" title="hah">项目管理</div>
    <div class="icon">
      <svg style="width: 20px; height: 20px" @click="open">
        <title>打开工程</title>
        <use xlink:href="#icon-wenjianjia"></use>
      </svg>
      <el-divider direction="vertical" />
      <svg style="width: 20px; height: 20px; margin-right: 100px">
        <title>移除工程</title>
        <use xlink:href="#icon-wenjianjiashanchu"></use>
      </svg>
      <el-divider direction="vertical" />
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
    <div class="detail">
      <div v-if="flag == false">
        <el-empty description="请添加项目！" />
      </div>
      <div v-else>
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
    ></section-context-menu>
    <el-dialog
      v-model="sectionShow"
      width="700px"
      :modal="false"
      :draggable="true"
    >
      <section-show
        :sectionValue="sectionValue"
        v-if="sectionShow"
      ></section-show>
    </el-dialog>

    <el-dialog v-model="openFlag" width="600px">
      <template #title>
        <svg style="width: 20px; height: 20px">
          <use xlink:href="#icon-service"></use>
        </svg>
        请选择要添加的工程
      </template>
      <open-project v-if="openFlag" @selectProjectId="selectProjectId" />
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, reactive, ref } from "vue";
import { getCurrentProjectId, getCurrentProjectName } from "@/utils/project";
import { getResult } from "@/api/request";
import OpenProject from "@/components/projectDialog/OpenProject.vue";
import { ResourceState } from "@/store/resourse/resourceState";
import { useStore } from "@/store";
import { computedResource } from "@/utils/common";
import SectionContextMenu from "@/components/contextMenu/SectionContextMenu.vue";
import SectionShow from "@/components/projectDialog/SectionShow.vue";
import { notice } from "@/utils/notice";

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
}
export default defineComponent({
  components: {
    OpenProject,
    SectionContextMenu,
    SectionShow,
  },

  setup() {
    const store = useStore();
    const flag = ref(false);
    const openFlag = ref(false);
    const sectionContextMenuFlag = ref(false);
    const selectId = ref("");
    const contextData = ref({});
    const sectionShow = ref(false);
    const sectionValue = reactive({
      name: "",
      id: "",
      value: [],
      selectDemId: "",
      selectDemName: "",
    });
    const open = () => {
      openFlag.value = true;
    };
    const defaultProps = {
      children: "children",
      label: "label",
    };

    const resource = computed(() => {
      const result: Children[] = [
        {
          label: getCurrentProjectName() as string,
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

    const selectProjectId = async () => {
      if (
        getCurrentProjectId() != null &&
        selectId.value != getCurrentProjectId()
      ) {
        selectId.value = getCurrentProjectId() as string;
        let data = await getResult(selectId.value);
        if (data != null) {
          let temp: ResourceState = JSON.parse(data.data);
          classify(temp);
          flag.value = true;
        }
      }
      openFlag.value = false;
    };

    const classify = (projectResult: ResourceState) => {
      store.commit("INIT", undefined);
      projectResult.layerDataList.forEach((item) => {
        store.commit("ADD_BASE_DATA", item);
      });
      store.commit("SET_ANALYSE", projectResult.analyse);
    };

    const contextmenuClick = (event: any, data: any) => {
      sectionContextMenuFlag.value = false;
      if (data.id != undefined) {
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
      }
    };
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

    onMounted(async () => {
      if (getCurrentProjectId() != null) {
        selectId.value = getCurrentProjectId() as string;
        let data = await getResult(selectId.value);
        if (data != null) {
          let temp: ResourceState = JSON.parse(data.data);
          classify(temp);
        }
        flag.value = true;
      } else {
        flag.value = false;
      }
    });
    return {
      open,
      flag,
      openFlag,
      sectionShow,
      sectionValue,
      selectProjectId,
      contextmenuClick,
      sendSectionValue,
      contextData,
      sectionContextMenuFlag,
      defaultProps,
      resource,
    };
  },
});
</script>


<style lang="scss" scoped>
.pro {
  width: 300px;
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
    padding-left: 10px;
    padding-top: 5px;
    .el-divider {
      margin-bottom: 8px;
    }
    svg {
      cursor: pointer;
    }
  }
  .detail {
    border: solid 0.5px;
    margin: 5px;
    height: calc(100% - 65px);
    background: white;
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
.section-context-menu {
  position: absolute;
  z-index: 99;
  top: 0;
}
</style>