<template>
  <div class="top-tool">
    <div class="base-function">
      <el-button
        type="primary"
        text
        @click="dialogAddData = true"
        :disabled="flag"
        ><svg style="width: 16px; margin-right: 10px">
          <use xlink:href="#icon-tianjiayingyong"></use></svg
        >添加</el-button
      >
      <el-divider direction="vertical" />
      <el-button
        type="primary"
        text
        @click="dialogBasemap = true"
        :disabled="flag"
        ><svg style="width: 16px; margin-right: 10px">
          <use xlink:href="#icon-ditu"></use></svg
        >底图</el-button
      >
      <el-divider direction="vertical" />
      <el-button
        type="primary"
        text
        @click="dialogAnalyse = true"
        :disabled="flag"
        ><svg style="width: 16px; margin-right: 10px">
          <use xlink:href="#icon-shujuyanjiu"></use></svg
        >分析</el-button
      >
    </div>

    <div class="map-tool">
      <el-button
        type="primary"
        :text="state === 1 ? false : true"
        @click="sectionClick"
        :disabled="flag"
        ><svg style="width: 16px; margin-right: 10px">
          <use xlink:href="#icon-xian"></use></svg
        >绘制断面</el-button
      >
      <el-divider direction="vertical" />
      <el-button
        type="primary"
        :text="state === 2 ? false : true"
        @click="regionClick"
        :disabled="flag"
        ><svg style="width: 16px; margin-right: 10px">
          <use xlink:href="#icon-mian"></use></svg
        >绘制区域</el-button
      >
      <el-divider direction="vertical" />
      <el-button type="primary" text @click="textDrawClick" :disabled="flag"
        ><svg style="width: 16px; margin-right: 10px">
          <use xlink:href="#icon-duohangshuru"></use></svg
        >文本输入</el-button
      >
    </div>

    <div class="right">
      <el-dropdown trigger="click" @command="commandHandle">
        <el-icon><MoreFilled /></el-icon>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item v-if="publicFlag" command="public"
              >项目公开化</el-dropdown-item
            >
            <el-dropdown-item v-if="privateFlag" command="private"
              >项目私有化</el-dropdown-item
            >
            <el-dropdown-item command="copy">项目拷贝</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <el-dialog v-model="dialogAddData" width="1000px" title="添加数据">
      <add-data-dialog @returnData="returnData" v-if="dialogAddData" />
    </el-dialog>

    <el-dialog v-model="dialogAnalyse" width="1000px" title="分析算法">
      <analyse-dialog @analyse="analyse" v-if="dialogAnalyse" />
    </el-dialog>

    <el-dialog
      v-model="dialogBasemap"
      width="500px"
      title="选择底图"
      class="basemap"
    >
      <basemap-dialog @changeBasemap="changeBasemap" />
    </el-dialog>

    <el-dialog
      v-model="dialogTextDraw"
      width="500px"
      title="文本输入"
      class="text-draw"
    >
      <text-draw @createGeoJson="createGeoJson" />
    </el-dialog>
  </div>
</template>

<script lang="ts">
import router from "@/router";
import { notice } from "@/utils/notice";
import { computed, defineComponent, ref } from "vue";
import AddDataDialog from "./AddDataDialog.vue";
import AnalyseDialog from "./AnalyseDialog.vue";
import BasemapDialog from "./BasemapDialog.vue";
import TextDraw from "./TextDraw.vue";
import { MoreFilled } from "@element-plus/icons-vue";
import { updatePublicState } from "@/api/request";
export default defineComponent({
  components: { AddDataDialog, AnalyseDialog, BasemapDialog, TextDraw },
  emits: [
    "returnFileList",
    "operateDraw",
    "analyse",
    "changeBasemap",
    "createGeoJson",
  ],
  setup(_, context) {
    const dialogAddData = ref(false);
    const dialogAnalyse = ref(false);
    const dialogBasemap = ref(false);
    const dialogTextDraw = ref(false);
    const state = ref(0);
    const isPublic = ref<boolean>((router.currentRoute.value.params.projectInfo as any).isPublic)

    const flag = computed(() => {
      if (router.currentRoute.value.params.role === "creator") {
        return false;
      } else {
        return true;
      }
    });

    const publicFlag = computed(() => {
      if (router.currentRoute.value.params.role === "creator") {
        if (isPublic.value) {
          return false;
        } else {
          return true;
        }
      } else {
        false;
      }
    });

    const privateFlag = computed(() => {
      if (router.currentRoute.value.params.role === "creator") {
        if (isPublic.value) {
          return true;
        } else {
          return false;
        }
      } else {
        false;
      }
    });

    const returnData = (
      val: {
        fileId: string;
        fileName: string;
        dataListId: string;
        dataListName: string;
        visualType: string;
        visualId: string;
      }[]
    ) => {
      dialogAddData.value = false;
      context.emit("returnFileList", val);
    };

    const sectionClick = () => {
      if (state.value === 1) {
        state.value = 0;
      } else {
        state.value = 1;
      }
      context.emit("operateDraw", state.value);
    };

    const regionClick = () => {
      if (state.value === 2) {
        state.value = 0;
      } else {
        state.value = 2;
      }
      context.emit("operateDraw", state.value);
    };

    const analyse = (val: { type: string; value: any }) => {
      context.emit("analyse", val);
      dialogAnalyse.value = false;
    };

    const changeBasemap = (val: string) => {
      if (state.value != 0) {
        notice("warning", "警告", "切换底图前请先关闭断面/区域的绘制");
        dialogBasemap.value = false;
        return;
      }
      context.emit("changeBasemap", val);
      dialogBasemap.value = false;
    };

    const textDrawClick = () => {
      state.value = 0;
      context.emit("operateDraw", state.value);
      dialogTextDraw.value = true;
    };

    const createGeoJson = (val: { geoJson: any; visualType: string }) => {
      context.emit("createGeoJson", val);
      dialogTextDraw.value = false;
    };

    const commandHandle = async (val: string) => {
      if (val === "public" || val === "private") {
        if (router.currentRoute.value.params.role === "creator") {
          let state: boolean = val === "public" ? true : false;
          const data = await updatePublicState({
            projectId: router.currentRoute.value.params.id as string,
            state: state,
          });
          if (data != null && (data as any).code === 0) {
            notice("success", "成功", "项目权限更新成功");
            isPublic.value = state
            console.log((router.currentRoute.value.params.projectInfo as any))
          }
        }
      }
    };

    return {
      state,
      dialogAddData,
      returnData,
      sectionClick,
      regionClick,
      dialogAnalyse,
      analyse,
      dialogBasemap,
      changeBasemap,
      textDrawClick,
      dialogTextDraw,
      createGeoJson,
      flag,
      MoreFilled,
      publicFlag,
      privateFlag,
      commandHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.top-tool {
  height: 50px;
  display: flex;
  background: #f6f7f8;
  border-bottom: solid 0.5px #dcdfe6;
  box-sizing: border-box;
  position: relative;
  .base-function {
    margin-left: 70px;
    height: 100%;
    line-height: 50px;
  }
  .map-tool {
    height: 100%;
    position: absolute;
    left: 35%;
    line-height: 50px;
    .el-button {
      border: solid 1px rgba($color: #000000, $alpha: 0);
      box-sizing: border-box;
    }
  }
  .right {
    height: 100%;
    right: 30px;
    position: absolute;
    cursor: pointer;
    .el-dropdown {
      margin-top: 18px;
    }
  }
  /deep/ .basemap {
    .el-dialog__header {
      background: #f6f7f8;
      margin: 0px;
    }
    .el-dialog__body {
      padding: 0px;
    }
  }
  /deep/ .text-draw {
    .el-dialog__body {
      padding-top: 0px;
    }
  }
}
</style>
