<template>
  <div>
    <div class="head">数据预览</div>
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <div style="width: 950px; height: 400px" v-if="mapVisualFlag">
      <map-visual
        :shpArray="shpArray"
        :movePngArray="movePngArray"
        :pngArray="pngArray"
        :rasterTileArray="rasterTileArray"
      />
    </div>
    <div style="width: 950px" v-if="excelVisualFlag">
      <excel-visual
        :tableNameList="tableNameList"
        :sandContentList="sandContentList"
        :suspensionList="suspensionList"
        :rateDirectionList="rateDirectionList"
        :salinityList="salinityList"
        :flowSandZList="flowSandZList"
      />
    </div>
  </div>
</template>

<script lang="ts">
type FileInfo = {
  id: string;
  fileName: string;
  size: string;
  uploader: string;
  visualType: string;
  visualId: string;
  folderId: string;
};
import { defineComponent, ref, PropType, onMounted } from "vue";
import MapVisual from "@/components/visual/MapVisual.vue";
import ExcelVisual from "@/components/visual/ExcelVisual.vue";
import { getCoordinates } from "@/api/request";
export default defineComponent({
  props: {
    fileInfo: {
      type: Object as PropType<FileInfo>,
    },
  },
  components: {
    MapVisual,
    ExcelVisual,
  },
  setup(props) {
    const skeletonFlag = ref(true);
    const mapVisualFlag = ref(false);
    const excelVisualFlag = ref(false);

    const shpArray = ref<
      { visualId: string; type: "line" | "fill" | "circle" }[]
    >([]);
    const movePngArray = ref<{ visualId: string; coordinates: number[][] }[]>(
      []
    );
    const pngArray = ref<{ visualId: string; coordinates: number[][] }[]>([]);
    const rasterTileArray = ref<string[]>([]);
    const tableNameList = ref<string[]>([]);
    const sandContentList = ref<string[]>([]);
    const suspensionList = ref<string[]>([]);
    const rateDirectionList = ref<string[]>([]);
    const salinityList = ref<string[]>([]);
    const flowSandZList = ref<string[]>([]);

    const initVisual = async () => {
      //获取file文件的可视化方法
      let MapFlag = false;
      let photoFlag = false;
      let excelFlag = false;
      if (props.fileInfo) {
        if (
          props.fileInfo.visualType === "lineVectorTile3D" ||
          props.fileInfo.visualType === "lineVectorTile"
        ) {
          shpArray.value.push({
            visualId: props.fileInfo.visualId,
            type: "line",
          });
          MapFlag = true;
        }
        if (
          props.fileInfo.visualType === "pointVectorTile" ||
          props.fileInfo.visualType === "pointVectorTile3D"
        ) {
          shpArray.value.push({
            visualId: props.fileInfo.visualId,
            type: "circle",
          });
          MapFlag = true;
        }
        if (
          props.fileInfo.visualType === "polygonVectorTile" ||
          props.fileInfo.visualType === "polygonVectorTile3D"
        ) {
          shpArray.value.push({
            visualId: props.fileInfo.visualId,
            type: "fill",
          });
          MapFlag = true;
        }
        if (props.fileInfo.visualType == "rasterTile") {
          rasterTileArray.value.push(props.fileInfo.visualId);
          MapFlag = true;
        }
        if (props.fileInfo.visualType == "png") {
          const coordinates = await getCoordinates(props.fileInfo.visualId);
          if (coordinates != null && (coordinates as any).code === 0) {
            pngArray.value.push({
              visualId: props.fileInfo.visualId,
              coordinates: coordinates.data,
            });
          }
          MapFlag = true;
        }
        if (props.fileInfo.visualType == "movePng") {
          const coordinates = await getCoordinates(props.fileInfo.visualId);
          if (coordinates != null && (coordinates as any).code === 0) {
            movePngArray.value.push({
              visualId: props.fileInfo.visualId,
              coordinates: coordinates.data,
            });
          }
          MapFlag = true;
        }
        // if (props.fileInfo.visualType === "photo") {
        //   photoList.value.push(
        //     `${prefix}visual/getPhoto/${fileList.value[i].id}`
        //   );
        //   photoFlag = true;
        // }
        if (props.fileInfo.visualType === "sandContent") {
          sandContentList.value.push(props.fileInfo.visualId);
          tableNameList.value.push(props.fileInfo.fileName);
          excelFlag = true;
        }
        if (props.fileInfo.visualType === "suspension") {
          suspensionList.value.push(props.fileInfo.visualId);
          tableNameList.value.push(props.fileInfo.fileName);
          excelFlag = true;
        }
        if (props.fileInfo.visualType === "rateDirection") {
          rateDirectionList.value.push(props.fileInfo.visualId);
          tableNameList.value.push(props.fileInfo.fileName);
          excelFlag = true;
        }
        if (props.fileInfo.visualType === "salinity") {
          salinityList.value.push(props.fileInfo.visualId);
          tableNameList.value.push(props.fileInfo.fileName);
          excelFlag = true;
        }
        if (props.fileInfo.visualType === "flowSand_Z") {
          flowSandZList.value.push(props.fileInfo.visualId);
          tableNameList.value.push(props.fileInfo.fileName);
          excelFlag = true;
        }
      }
      mapVisualFlag.value = MapFlag;
      excelVisualFlag.value = excelFlag;
      skeletonFlag.value = false;
    };

    onMounted(async () => {
      await initVisual();
    });

    return {
      skeletonFlag,
      mapVisualFlag,
      excelVisualFlag,
      shpArray,
      movePngArray,
      pngArray,
      rasterTileArray,
      tableNameList,
      sandContentList,
      suspensionList,
      rateDirectionList,
      salinityList,
      flowSandZList,
    };
  },
});
</script>

<style lang="scss" scoped>
.head {
  height: 50px;
  background: black;
  color: white;
  font-size: 20px;
  font-weight: 400;
  line-height: 50px;
  padding-left: 30px;
}
</style>