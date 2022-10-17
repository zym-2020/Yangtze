<template>
  <div class="main">
    <top-tool
      @returnFileList="returnFileList"
      @operateDraw="operateDraw"
    ></top-tool>
    <div class="body">
      <div class="left" ref="left">
        <data-manage
          class="top"
          ref="dataManage"
          @operateLayer="operateLayer"
        ></data-manage>
        <layer-manage
          class="bottom"
          ref="layerManage"
          @closeLayer="closeLayer"
          @hideLayer="hideLayer"
        ></layer-manage>
        <div class="left-resize" ref="leftResize"></div>
      </div>
      <right-visual ref="rightMap" @drawHandle="drawHandle"></right-visual>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, nextTick } from "vue";
import TopTool from "@/components/analyse/TopTool.vue";
import DataManage from "@/components/analyse/DataManage.vue";
import LayerManage from "@/components/analyse/LayerManage.vue";
import RightVisual from "@/components/analyse/RightVisual.vue";
export default defineComponent({
  components: { TopTool, RightVisual, DataManage, LayerManage },
  setup() {
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
      }
    };

    const closeLayer = (val: string) => {
      rightMap.value.removeLayer(val);
    };

    const hideLayer = (val: { id: string; flag: boolean }) => {
      rightMap.value.changeLayerState(val);
    };

    const operateDraw = (val: number) => {
      rightMap.value.operateDraw(val);
    };

    const drawHandle = async (val: { geoJson: any; visualType: string }) => {
      const param = await dataManage.value.addDrawData(val);
      layerManage.value.addLayer(param);
      rightMap.value.addMapLayer(param);
    };

    nextTick(() => {
      dropSize();
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
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  height: 100%;
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