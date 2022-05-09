<template>
  <div class="left-tools">
    <div class="title">分析工具</div>
    <div class="body">
      <div class="tools">
        <el-scrollbar>
          <el-tree :data="analyzeList" :props="defaultProps" node-key="id" :default-expanded-keys="treeExpandData">
            <template #default="{ node, data }">
              <span class="custom-tree-node">
                <div class="img">
                  <img :src="data.icon" alt="" width="18" height="18" />
                </div>
                <div>{{ node.label }}</div>
              </span>
            </template>
          </el-tree>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
interface Tree {
  id: string;
  label: string;
  children?: Tree[];
  icon?: string;
}
import { defineComponent, ref } from "vue";
export default defineComponent({
  setup() {
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const treeExpandData = ref(["1", "2"]);
    const analyzeList = ref<Tree[]>([
      {
        id: "1",
        label: "河床演变",
        icon: "/river_bed_img/3DAnalystCreateSteepestPath32.png",
        children: [
          {
            id: "1-1",
            label: "断面形态",
            icon: "/river_bed_img/TinEditingTinLineAdd32.png",
            children: [
              {
                id: "1-1-1",
                label: "任意断面",
                icon: "/river_bed_img/ElementLine16.png",
              },
              {
                id: "1-1-2",
                label: "输入断面",
                icon: "/river_bed_img/EditingCreateCOGOAttributes16.png",
              },
            ],
          },
          {
            id: "1-2",
            label: "断面比较",
            icon: "/river_bed_img/3DAnalystInterpolateLine32.png",
            children: [
              {
                id: "1-2-1",
                label: "任意断面",
                icon: "/river_bed_img/ElementLine16.png",
              },
              {
                id: "1-2-2",
                label: "输入断面",
                icon: "/river_bed_img/EditingCreateCOGOAttributes16.png",
              },
            ],
          },
          {
            id: "1-3",
            label: "汊道断面对比",
            icon: "/river_bed_img/MapSendBehind32.png",
            children: [
              {
                id: "1-3-1",
                label: "选择固定断面",
                icon: "/river_bed_img/CadastralPRImage3_16.png",
              },
              {
                id: "1-3-2",
                label: "查看对比图",
                icon: "/river_bed_img/3DAnalystInterpolateProfileGraphCreate16.png",
              },
            ],
          },
          {
            id: "1-4",
            label: "断面面积冲淤",
            icon: "/river_bed_img/LayerServiceMap32.png",
            children: [
              {
                id: "1-4-1",
                label: "任意断面",
                icon: "/river_bed_img/ElementLine16.png",
              },
              {
                id: "1-4-2",
                label: "输入断面",
                icon: "/river_bed_img/EditingCreateCOGOAttributes16.png",
              },
            ],
          },
          {
            id: "1-5",
            label: "任意区域冲淤",
            icon: "/river_bed_img/3DAnalystInterpolatePolygon32.png",
            children: [
              {
                id: "1-5-1",
                label: "任意矩形",
                icon: "/river_bed_img/EditingRectangleTool16.png",
              },
              {
                id: "1-5-2",
                label: "任意圆形",
                icon: "/river_bed_img/EditingCircleTool16.png",
              },
              {
                id: "1-5-3",
                label: "任意多边形",
                icon: "/river_bed_img/EditingPolygonTool16.png",
              },
              {
                id: "1-5-6",
                label: "来自文本",
                icon: "/river_bed_img/EditingSketchPropertiesWindowShow16.png",
              },
            ],
          },
          {
            id: "1-6",
            label: "特定高程冲淤",
            icon: "/river_bed_img/RasterImageAnalysisPanSharpen32.png",
            children: [],
          },
          {
            id: "1-7",
            label: "冲淤等深线",
            icon: "/river_bed_img/RasterImageAnalysisOrthoRectify32.png",
            children: [],
          },
          {
            id: "1-8",
            label: "河道容积计算",
            icon: "/river_bed_img/MapPackageMPKFile32.png",
            children: [
              {
                id: "1-8-1",
                label: "任意矩形",
                icon: "/river_bed_img/EditingRectangleTool16.png",
              },
              {
                id: "1-8-2",
                label: "任意圆形",
                icon: "/river_bed_img/EditingCircleTool16.png",
              },
              {
                id: "1-8-3",
                label: "任意多边形",
                icon: "/river_bed_img/EditingPolygonTool16.png",
              },
              {
                id: "1-8-4",
                label: "来自文本",
                icon: "/river_bed_img/EditingSketchPropertiesWindowShow16.png",
              },
            ],
          },
          {
            id: "1-9",
            label: "河床坡度提取",
            icon: "/river_bed_img/Surface32.png",
            children: [],
          },
          {
            id: "1-10",
            label: "深泓线比较",
            icon: "/river_bed_img/SpatialAnalystContourTool32.png",
            children: [],
          },
          {
            id: "1-11",
            label: "等深线对比",
            icon: "/river_bed_img/CadastralCreateLineString32.png",
            children: [
              {
                id: "1-11-1",
                label: "等深线对比",
                icon: "/river_bed_img/ElementLine16.png",
              },
              {
                id: "1-11-2",
                label: "等高线对比",
                icon: "/river_bed_img/EditingCreateCOGOAttributes16.png",
              },
            ],
          },
          {
            id: "1-12",
            label: "边界分析",
            icon: "/river_bed_img/3DAnalystCreateSteepestPath32.png",
            children: [],
          },
        ],
      },
      {
        id: "2",
        label: "水文分析",
        icon: "/river_bed_img/3DAnalystCreateSteepestPath32.png",
        children: [],
      },
    ]);
    return {
      analyzeList,
      defaultProps,
      treeExpandData
    };
  },
});
</script>

<style lang="scss" scoped>
.left-tools {
  height: calc(100vh - 60px);
  border-right: solid 1px;
  background: #e0e5eb;
  .title {
    padding-left: 10px;
    height: 30px;
    line-height: 30px;
  }
  .body {
    height: calc(100% - 30px);
    .tools {
      border: solid 1px;
      height: calc(100% - 10px);
      width: 290px;
      margin-left: 4px;
      background: white;
      .el-scrollbar {
        height: 100%;
        .custom-tree-node {
          display: flex;
          .img {
            margin-right: 5px;
            img {
              margin-top: 2px;
            }
          }
        }
      }
    }
  }
}
</style>