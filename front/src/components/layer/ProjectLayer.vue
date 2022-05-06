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

    <!-- 
    <section-context-menu
      v-show="sectionContextMenuFlag"
      class="section-context-menu"
      :contextData="contextData"
      @sendSectionValue="sendSectionValue"
    ></section-context-menu>
    <section-contrast-context-menu
      v-show="sectionContrastContextMenuFlag"
      class="section-contrast-context-menu"
      :sectionContrastContextData="sectionContrastContextData"
      @sendSectionContrastValue="sendSectionContrastValue"
    >
    </section-contrast-context-menu>
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

    <el-dialog v-model="sectionContrastShow" width="700px" :modal="false">
      <section-contrast-show
        v-if="sectionContrastShow"
        :sectionContrastValue="sectionContrastValue"
      ></section-contrast-show>
    </el-dialog>

    <el-dialog v-model="openFlag" width="600px" :show-close="false">
      <open-project v-if="openFlag" @selectProjectId="selectProjectId" />
    </el-dialog> -->
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from "vue";
import { computedResource } from "@/utils/common";

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
  setup(props) {
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
    const open = () => {};
    const defaultProps = {
      children: "children",
      label: "label",
    };

    const contextmenuClick = () => {};

    return {
      open,
      defaultProps,
      contextmenuClick,
      resource
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