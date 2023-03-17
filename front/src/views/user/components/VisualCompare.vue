<template>
  <div class="visual-compare">
    <div class="head">可视化对比</div>
    <div class="old">
      <el-skeleton :rows="5" animated v-if="oldSkeletonFlag" />
      <div style="width: 100%" v-if="oldMapVisualFlag">
        <map-visual
          :shpArray="oldShpArray"
          :movePngArray="oldMovePngArray"
          :pngArray="oldPngArray"
          :rasterTileArray="oldRasterTileArray"
        />
      </div>
      <div style="width: 100%" v-if="oldExcelVisualFlag">
        <excel-visual
          :tableNameList="oldTableNameList"
          :sandContentList="oldSandContentList"
          :suspensionList="oldSuspensionList"
          :rateDirectionList="oldRateDirectionList"
          :salinityList="oldSalinityList"
          :flowSandZList="oldFlowSandZList"
        />
      </div>
      <div v-if="oldPhotoVisualFlag">
        <photo-visual :photoList="oldPhotoList" />
      </div>
      <div v-if="oldVideoVisualFlag">
        <video-visual :urls="oldVideoURLs" />
      </div>
    </div>
    <div class="divider">
      <el-divider content-position="left"
        >上方为被更换数据，下方为更换数据</el-divider
      >
    </div>
    <div class="now">
      <el-skeleton :rows="5" animated v-if="nowSkeletonFlag" />
      <div style="width: 100%" v-if="nowMapVisualFlag">
        <map-visual
          :shpArray="nowShpArray"
          :movePngArray="nowMovePngArray"
          :pngArray="nowPngArray"
          :rasterTileArray="nowRasterTileArray"
        />
      </div>
      <div style="width: 100%" v-if="nowExcelVisualFlag">
        <excel-visual
          :tableNameList="nowTableNameList"
          :sandContentList="nowSandContentList"
          :suspensionList="nowSuspensionList"
          :rateDirectionList="nowRateDirectionList"
          :salinityList="nowSalinityList"
          :flowSandZList="nowFlowSandZList"
        />
      </div>
      <div v-if="nowPhotoVisualFlag">
        <photo-visual :photoList="photoList" />
      </div>
      <div v-if="nowVideoVisualFlag">
        <video-visual :urls="nowVideoURLs" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import MapVisual from "@/components/visual/MapVisual.vue";
import ExcelVisual from "@/components/visual/ExcelVisual.vue";
import PhotoVisual from "@/components/visual/PhotoVisual.vue";
import { getView, getCoordinates } from "@/api/request";
import { prefix } from "@/prefix";
import VideoVisual from "@/components/visual/VideoVisual.vue";
export default defineComponent({
  components: {
    MapVisual,
    ExcelVisual,
    PhotoVisual,
    VideoVisual,
  },
  props: {
    compareInfo: {
      type: Object,
    },
  },
  setup(props) {
    const oldSkeletonFlag = ref(true);
    const oldMapVisualFlag = ref(false);
    const oldExcelVisualFlag = ref(false);
    const oldPhotoVisualFlag = ref(false);
    const oldVideoVisualFlag = ref(false);
    const oldShpArray = ref<
      {
        visualId: string;
        type: "line" | "fill" | "circle";
        view: { zoom: number; center: number[] };
      }[]
    >([]);
    const oldMovePngArray = ref<
      {
        visualId: string;
        coordinates: number[][];
        view: { zoom: number; center: number[] };
      }[]
    >([]);
    const oldPngArray = ref<
      {
        visualId: string;
        coordinates: number[][];
        view: { zoom: number; center: number[] };
      }[]
    >([]);
    const oldRasterTileArray = ref<
      { visualId: string; view: { zoom: number; center: number[] } }[]
    >([]);
    const oldPhotoList = ref<string[]>([]);
    const oldVideoURLs = ref<{ url: string; fileName: string }[]>([]);
    const oldTableNameList = ref<string[]>([]);
    const oldSandContentList = ref<string[]>([]);
    const oldSuspensionList = ref<string[]>([]);
    const oldRateDirectionList = ref<string[]>([]);
    const oldSalinityList = ref<string[]>([]);
    const oldFlowSandZList = ref<string[]>([]);

    const nowSkeletonFlag = ref(true);
    const nowMapVisualFlag = ref(false);
    const nowExcelVisualFlag = ref(false);
    const nowPhotoVisualFlag = ref(false);
    const nowVideoVisualFlag = ref(false);
    const nowShpArray = ref<
      {
        visualId: string;
        type: "line" | "fill" | "circle";
        view: { zoom: number; center: number[] };
      }[]
    >([]);
    const nowMovePngArray = ref<
      {
        visualId: string;
        coordinates: number[][];
        view: { zoom: number; center: number[] };
      }[]
    >([]);
    const nowPngArray = ref<
      {
        visualId: string;
        coordinates: number[][];
        view: { zoom: number; center: number[] };
      }[]
    >([]);
    const nowRasterTileArray = ref<
      { visualId: string; view: { zoom: number; center: number[] } }[]
    >([]);
    const nowPhotoList = ref<string[]>([]);
    const nowVideoURLs = ref<{ url: string; fileName: string }[]>([]);
    const nowTableNameList = ref<string[]>([]);
    const nowSandContentList = ref<string[]>([]);
    const nowSuspensionList = ref<string[]>([]);
    const nowRateDirectionList = ref<string[]>([]);
    const nowSalinityList = ref<string[]>([]);
    const nowFlowSandZList = ref<string[]>([]);

    const initVisual = async () => {
      //获取file文件的可视化方法
      let oldMapFlag = false;
      let nowMapFlag = false;
      let oldPhotoFlag = false;
      let oldExcelFlag = false;
      let nowExcelFlag = false;
      let nowPhotoFlag = false;
      let oldVideoFlag = false;
      let nowVideoFlag = false;
      if (props.compareInfo) {
        const oldView = await getView(props.compareInfo.oldVisualId);
        const nowView = await getView(props.compareInfo.visualId);
        if (
          props.compareInfo.visualType === "lineVectorTile3D" ||
          props.compareInfo.visualType === "lineVectorTile"
        ) {
          nowShpArray.value.push({
            visualId: props.compareInfo.visualId,
            type: "line",
            view: JSON.parse(nowView.data),
          });
          nowMapFlag = true;
        }
        if (
          props.compareInfo.visualType === "pointVectorTile" ||
          props.compareInfo.visualType === "pointVectorTile3D"
        ) {
          nowShpArray.value.push({
            visualId: props.compareInfo.visualId,
            type: "circle",
            view: JSON.parse(nowView.data),
          });
          nowMapFlag = true;
        }
        if (
          props.compareInfo.visualType === "polygonVectorTile" ||
          props.compareInfo.visualType === "polygonVectorTile3D"
        ) {
          nowShpArray.value.push({
            visualId: props.compareInfo.visualId,
            type: "fill",
            view: JSON.parse(nowView.data),
          });
          nowMapFlag = true;
        }
        if (props.compareInfo.visualType == "rasterTile") {
          nowRasterTileArray.value.push({
            visualId: props.compareInfo.visualId,
            view: JSON.parse(nowView.data),
          });
          nowMapFlag = true;
        }
        if (props.compareInfo.visualType == "png") {
          const coordinates = await getCoordinates(props.compareInfo.visualId);
          if (coordinates != null && (coordinates as any).code === 0) {
            nowPngArray.value.push({
              visualId: props.compareInfo.visualId,
              coordinates: coordinates.data,
              view: JSON.parse(nowView.data),
            });
          }
          nowMapFlag = true;
        }
        if (props.compareInfo.visualType == "movePng") {
          const coordinates = await getCoordinates(props.compareInfo.visualId);
          if (coordinates != null && (coordinates as any).code === 0) {
            nowMovePngArray.value.push({
              visualId: props.compareInfo.visualId,
              coordinates: coordinates.data,
              view: JSON.parse(nowView.data),
            });
          }
          nowMapFlag = true;
        }
        if (props.compareInfo.visualType === "photo") {
          nowPhotoList.value.push(
            `${prefix}visual/getPhoto/${props.compareInfo.id}`
          );
          nowPhotoFlag = true;
        }
        if (props.compareInfo.visualType === "video") {
          nowVideoURLs.value.push({
            url: `${prefix}visual/video/${props.compareInfo.id}`,
            fileName: props.compareInfo.fileName,
          });
          nowVideoFlag = true;
        }
        if (props.compareInfo.visualType === "sandContent") {
          nowSandContentList.value.push(props.compareInfo.visualId);
          nowTableNameList.value.push(props.compareInfo.fileName);
          nowExcelFlag = true;
        }
        if (props.compareInfo.visualType === "suspension") {
          nowSuspensionList.value.push(props.compareInfo.visualId);
          nowTableNameList.value.push(props.compareInfo.fileName);
          nowExcelFlag = true;
        }
        if (props.compareInfo.visualType === "rateDirection") {
          nowRateDirectionList.value.push(props.compareInfo.visualId);
          nowTableNameList.value.push(props.compareInfo.fileName);
          nowExcelFlag = true;
        }
        if (props.compareInfo.visualType === "salinity") {
          nowSalinityList.value.push(props.compareInfo.visualId);
          nowTableNameList.value.push(props.compareInfo.fileName);
          nowExcelFlag = true;
        }
        if (props.compareInfo.visualType === "flowSand_Z") {
          nowFlowSandZList.value.push(props.compareInfo.visualId);
          nowTableNameList.value.push(props.compareInfo.fileName);
          nowExcelFlag = true;
        }

        //==================================================================

        if (
          props.compareInfo.oldVisualType === "lineVectorTile3D" ||
          props.compareInfo.oldVisualType === "lineVectorTile"
        ) {
          oldShpArray.value.push({
            visualId: props.compareInfo.oldVisualId,
            type: "line",
            view: JSON.parse(oldView.data),
          });
          oldMapFlag = true;
        }
        if (
          props.compareInfo.oldVisualType === "pointVectorTile" ||
          props.compareInfo.oldVisualType === "pointVectorTile3D"
        ) {
          oldShpArray.value.push({
            visualId: props.compareInfo.oldVisualId,
            type: "circle",
            view: JSON.parse(oldView.data),
          });
          oldMapFlag = true;
        }
        if (
          props.compareInfo.oldVisualType === "polygonVectorTile" ||
          props.compareInfo.oldVisualType === "polygonVectorTile3D"
        ) {
          oldShpArray.value.push({
            visualId: props.compareInfo.oldVisualId,
            type: "fill",
            view: JSON.parse(oldView.data),
          });
          oldMapFlag = true;
        }
        if (props.compareInfo.oldVisualType == "rasterTile") {
          oldRasterTileArray.value.push({
            visualId: props.compareInfo.oldVisualId,
            view: JSON.parse(oldView.data),
          });
          oldMapFlag = true;
        }
        if (props.compareInfo.oldVisualType == "png") {
          const coordinates = await getCoordinates(
            props.compareInfo.oldVisualId
          );
          if (coordinates != null && (coordinates as any).code === 0) {
            oldPngArray.value.push({
              visualId: props.compareInfo.oldVisualId,
              coordinates: coordinates.data,
              view: JSON.parse(oldView.data),
            });
          }
          oldMapFlag = true;
        }
        if (props.compareInfo.oldVisualType == "movePng") {
          const coordinates = await getCoordinates(
            props.compareInfo.oldVisualId
          );
          if (coordinates != null && (coordinates as any).code === 0) {
            oldMovePngArray.value.push({
              visualId: props.compareInfo.oldVisualId,
              coordinates: coordinates.data,
              view: JSON.parse(oldView.data),
            });
          }
          oldMapFlag = true;
        }
        if (props.compareInfo.oldVisualType === "photo") {
          oldPhotoList.value.push(
            `${prefix}visual/getPhoto/${props.compareInfo.id}`
          );
          oldPhotoFlag = true;
        }
        if (props.compareInfo.oldVisualType === "video") {
          oldVideoURLs.value.push({
            url: `${prefix}visual/video/${props.compareInfo.id}`,
            fileName: props.compareInfo.fileName,
          });
          oldVideoFlag = true;
        }
        if (props.compareInfo.oldVisualType === "sandContent") {
          oldSandContentList.value.push(props.compareInfo.oldVisualId);
          oldTableNameList.value.push(props.compareInfo.fileName);
          oldExcelFlag = true;
        }
        if (props.compareInfo.oldVisualType === "suspension") {
          oldSuspensionList.value.push(props.compareInfo.oldVisualId);
          oldTableNameList.value.push(props.compareInfo.fileName);
          oldExcelFlag = true;
        }
        if (props.compareInfo.oldVisualType === "rateDirection") {
          oldRateDirectionList.value.push(props.compareInfo.oldVisualId);
          oldTableNameList.value.push(props.compareInfo.fileName);
          oldExcelFlag = true;
        }
        if (props.compareInfo.oldVisualType === "salinity") {
          oldSalinityList.value.push(props.compareInfo.oldVisualId);
          oldTableNameList.value.push(props.compareInfo.fileName);
          oldExcelFlag = true;
        }
        if (props.compareInfo.oldVisualType === "flowSand_Z") {
          oldFlowSandZList.value.push(props.compareInfo.oldVisualId);
          oldTableNameList.value.push(props.compareInfo.fileName);
          oldExcelFlag = true;
        }
      }
      nowMapVisualFlag.value = nowMapFlag;
      nowExcelVisualFlag.value = nowExcelFlag;
      nowPhotoVisualFlag.value = nowPhotoFlag;
      nowVideoVisualFlag.value = nowVideoFlag;
      oldMapVisualFlag.value = oldMapFlag;
      oldExcelVisualFlag.value = oldExcelFlag;
      oldPhotoVisualFlag.value = oldPhotoFlag;
      oldVideoVisualFlag.value = oldVideoFlag;

      nowSkeletonFlag.value = false;
      oldSkeletonFlag.value = false;
    };

    onMounted(() => {
      initVisual();
    });

    return {
      oldSkeletonFlag,
      oldMapVisualFlag,
      oldExcelVisualFlag,
      oldPhotoVisualFlag,
      oldVideoVisualFlag,
      oldShpArray,
      oldMovePngArray,
      oldPngArray,
      oldRasterTileArray,
      oldTableNameList,
      oldSandContentList,
      oldSuspensionList,
      oldRateDirectionList,
      oldSalinityList,
      oldFlowSandZList,
      oldPhotoList,
      oldVideoURLs,
      nowSkeletonFlag,
      nowMapVisualFlag,
      nowExcelVisualFlag,
      nowPhotoVisualFlag,
      nowVideoVisualFlag,
      nowShpArray,
      nowMovePngArray,
      nowPngArray,
      nowRasterTileArray,
      nowTableNameList,
      nowSandContentList,
      nowSuspensionList,
      nowRateDirectionList,
      nowSalinityList,
      nowFlowSandZList,
      nowPhotoList,
      nowVideoURLs,
    };
  },
});
</script>

<style lang="scss" scoped>
.visual-compare {
  width: 100%;
  .head {
    height: 50px;
    background: black;
    color: white;
    font-size: 20px;
    font-weight: 400;
    line-height: 50px;
    padding-left: 30px;
  }
  .divider {
    padding: 0 20px;
  }
}
</style>