<template>
  <div class="main">
    <top-tool
      @returnFileList="returnFileList"
      @operateDraw="operateDraw"
      @analyse="analyse"
      @changeBasemap="changeBasemap"
      @createGeoJson="drawHandle"
    ></top-tool>
    <div class="body">
      <div class="left" ref="left">
        <data-manage
          class="top"
          ref="dataManage"
          @operateLayer="operateLayer"
        ></data-manage>
        <el-skeleton :rows="5" animated class="bottom" v-if="skeletonFlag" />
        <layer-manage
          class="bottom"
          ref="layerManage"
          :layerList="layerList"
          @closeLayer="closeLayer"
          @hideLayer="hideLayer"
          @moveLayer="moveLayer"
          v-else
        ></layer-manage>
        <div class="left-resize" ref="leftResize"></div>
      </div>
      <el-skeleton :rows="5" animated v-if="skeletonFlag" />
      <right-visual
        :layerList="layerList"
        ref="rightMap"
        @drawHandle="drawHandle"
        v-else
      ></right-visual>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, nextTick, onMounted, onUpdated } from "vue";
import { getLayersInfo } from "@/api/request";
import TopTool from "@/components/analyse/TopTool.vue";
import DataManage from "@/components/analyse/DataManage.vue";
import LayerManage from "@/components/analyse/LayerManage.vue";
import RightVisual from "@/components/analyse/RightVisual.vue";
import router from "@/router";
export default defineComponent({
  components: { TopTool, RightVisual, DataManage, LayerManage },
  setup() {
    const skeletonFlag = ref(true);
    const layerList = ref<
      { id: string; fileName: string; visualId: string; visualType: string }[]
    >([]);
    const leftResize = ref<HTMLElement>();
    const left = ref<HTMLElement>();
    const rightMap = ref();
    const dataManage = ref();
    const layerManage = ref();

    const dropSize = () => {
      const bodyHeight = document.body.scrollHeight;
      const bodyWidth = document.body.scrollWidth;
      (leftResize.value as HTMLElement).onmousedown = function () {
        document.onmousemove = function (e: any) {
          let clientX = e.clientX;
          if (clientX > bodyWidth * 0.4) {
            clientX = bodyWidth * 0.4;
          }
          if (clientX < bodyWidth * 0.2) {
            clientX = bodyWidth * 0.2;
          }
          (left.value as HTMLElement).style.width = clientX + "px";
          rightMap.value.mapResize();
        };
        document.onmouseup = function () {
          document.onmousemove = null;
          document.onmouseup = null;
        };
      };
      console.log(2);
      const bottomResize: HTMLElement = document.querySelector(
        ".bottom-resize"
      ) as HTMLElement;
      const bottom: HTMLElement = document.querySelector(
        ".layer-manage"
      ) as HTMLElement;
      const top: HTMLElement = document.querySelector(
        ".data-manage"
      ) as HTMLElement;
      bottomResize.onmousedown = function () {
        document.onmousemove = function (e: any) {
          let clientY = e.clientY;
          if (clientY > (bodyHeight - 50) * 0.7 + 50) {
            clientY = (bodyHeight - 50) * 0.7 + 50;
          }
          if (clientY < (bodyHeight - 50) * 0.5 + 50) {
            clientY = (bodyHeight - 50) * 0.5 + 50;
          }
          bottom.style.height = bodyHeight - clientY - 11 + "px";
          top.style.height = clientY - 120 + "px";
        };
        document.onmouseup = function () {
          document.onmousemove = null;
          document.onmouseup = null;
        };
      };
    };

    const returnFileList = (
      val: {
        fileId: string;
        fileName: string;
        dataListId: string;
        dataListName: string;
        visualType: string;
        visualId: string;
      }[]
    ) => {
      dataManage.value.addData(val);
    };

    const operateLayer = (val: {
      type: string;
      content: {
        id: string;
        name: string;
        visualType: string;
        visualId: string;
      };
    }) => {
      if (val.type === "add") {
        layerManage.value.addLayer(val.content);
        rightMap.value.addMapLayer(val.content);
      } else if (val.type === "del") {
        console.log(val, val.content);
        layerManage.value.delLayer(val.content.id);
        rightMap.value.removeLayer(val.content.id);
      } else if (val.type === "chart") {
        rightMap.value.addChart(val.content);
      }
    };

    const closeLayer = (val: string) => {
      rightMap.value.removeLayer(val);
    };

    const hideLayer = (val: { id: string; flag: boolean }) => {
      rightMap.value.changeLayerState(val);
    };

    const moveLayer = (val: { drop: string; target: string }) => {
      rightMap.value.moveLayer(val);
    };

    const operateDraw = (val: number) => {
      rightMap.value.operateDraw(val);
    };

    const drawHandle = async (val: { geoJson: any; visualType: string }) => {
      const param = await dataManage.value.addDrawData(val);
      layerManage.value.addLayer(param);
      rightMap.value.addMapLayer(param);
    };

    const analyse = async (val: { type: string; value: any }) => {
      await dataManage.value.addAnalyse(val);
    };

    const changeBasemap = (val: string) => {
      const layers = layerManage.value.getLayers();
      rightMap.value.changeBasemap(layers, val);
    };


    onUpdated(() => {
      dropSize();
    });

    onMounted(async () => {
      skeletonFlag.value = true;
      const data = await getLayersInfo(
        router.currentRoute.value.params.id as string
      );
      if (data != null && (data as any).code === 0) {
        layerList.value = data.data;
      }
      skeletonFlag.value = false;
    });

    return {
      leftResize,
      left,
      rightMap,
      returnFileList,
      dataManage,
      operateLayer,
      layerManage,
      closeLayer,
      hideLayer,
      operateDraw,
      drawHandle,
      analyse,
      skeletonFlag,
      layerList,
      moveLayer,
      changeBasemap,
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  height: calc(100vh - 60px);
  .body {
    height: calc(100% - 50px);
    display: flex;
    .left {
      width: 30%;
      height: 100%;
      position: relative;
      flex-shrink: 0;
      flex-grow: 0;
      .left-resize {
        position: absolute;
        width: 5px;
        height: 100%;
        top: 0;
        right: 0;
        cursor: col-resize;
      }
      .top {
        height: calc(60% - 10px);
      }
      .bottom {
        height: calc(40% - 10px);
      }
    }
    .right {
      width: 70%;
    }
  }
}
</style>