<template>
  <div class="main">
    <div class="analyseHead">
      <span>河床演变分析</span>
      <span style="margin-left: 240px">当前操作：{{ title }}</span>
    </div>
    <div class="image">
      <el-dropdown @command="activeClick" trigger="click">
        <div :class="active === 1 ? 'img active' : 'img'" title="断面形态">
          <img src="/river_bed_img/TinEditingTinLineAdd32.png" alt="" />
          <!-- <img src="/river_bed_img/16_circle_blue.png" alt=""> -->
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="1-1">
              <img src="/river_bed_img/ElementLine16.png" />
              任意断面
            </el-dropdown-item>
            <el-dropdown-item command="1-2">
              <img src="/river_bed_img/EditingCreateCOGOAttributes16.png" />
              输入断面
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-dropdown @command="activeClick" trigger="click">
        <div :class="active === 2 ? 'img active' : 'img'" title="断面比较">
          <img src="/river_bed_img/3DAnalystInterpolateLine32.png" alt="" />
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="2-1">
              <img src="/river_bed_img/ElementLine16.png" />
              任意断面
            </el-dropdown-item>
            <el-dropdown-item command="2-2">
              <img src="/river_bed_img/EditingCreateCOGOAttributes16.png" />
              输入断面
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-dropdown @command="activeClick" trigger="click">
        <div :class="active === 3 ? 'img active' : 'img'" title="汊道断面对比">
          <img src="/river_bed_img/MapSendBehind32.png" alt="" />
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="3-1">
              <img src="/river_bed_img/CadastralPRImage3_16.png" />
              选择固定断面
            </el-dropdown-item>
            <el-dropdown-item command="3-2">
              <img
                src="/river_bed_img/3DAnalystInterpolateProfileGraphCreate16.png"
              />
              查看对比图
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-dropdown @command="activeClick" trigger="click">
        <div :class="active === 4 ? 'img active' : 'img'" title="断面面积冲淤">
          <img src="/river_bed_img/LayerServiceMap32.png" alt="" />
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="4-1">
              <img src="/river_bed_img/ElementLine16.png" />
              任意断面
            </el-dropdown-item>
            <el-dropdown-item command="4-2">
              <img src="/river_bed_img/EditingCreateCOGOAttributes16.png" />
              输入断面
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-dropdown @command="activeClick" trigger="click">
        <div :class="active === 5 ? 'img active' : 'img'" title="任意区域冲淤">
          <img src="/river_bed_img/3DAnalystInterpolatePolygon32.png" />
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="5-1">
              <img src="/river_bed_img/EditingRectangleTool16.png" />
              任意矩形
            </el-dropdown-item>
            <el-dropdown-item command="5-2">
              <img src="/river_bed_img/EditingCircleTool16.png" />
              任意圆形
            </el-dropdown-item>
            <el-dropdown-item command="5-3">
              <img src="/river_bed_img/EditingPolygonTool16.png" />
              任意多边形
            </el-dropdown-item>
            <el-dropdown-item command="5-4">
              <img
                src="/river_bed_img/EditingSketchPropertiesWindowShow16.png"
              />
              来自文本
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>

      <div
        :class="active === 6 ? 'img active' : 'img'"
        title="特定高程冲淤"
        @click="activeClick('6')"
      >
        <img src="/river_bed_img/RasterImageAnalysisPanSharpen32.png" />
      </div>
      <div
        :class="active === 7 ? 'img active' : 'img'"
        title="冲淤等深线"
        @click="activeClick('7')"
      >
        <img src="/river_bed_img/RasterImageAnalysisOrthoRectify32.png" />
      </div>
      <el-dropdown @command="activeClick" trigger="click">
        <div :class="active === 8 ? 'img active' : 'img'" title="河道容积计算">
          <img src="/river_bed_img/MapPackageMPKFile32.png" alt="" />
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="8-1">
              <img src="/river_bed_img/EditingRectangleTool16.png" />
              任意矩形
            </el-dropdown-item>
            <el-dropdown-item command="8-2">
              <img src="/river_bed_img/EditingCircleTool16.png" />
              任意圆形
            </el-dropdown-item>
            <el-dropdown-item command="8-3">
              <img src="/river_bed_img/EditingPolygonTool16.png" />
              任意多边形
            </el-dropdown-item>
            <el-dropdown-item command="8-4">
              <img
                src="/river_bed_img/EditingSketchPropertiesWindowShow16.png"
              />
              来自文本
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>

      <div
        :class="active === 9 ? 'img active' : 'img'"
        title="河床坡度提取"
        @click="activeClick('9')"
      >
        <img src="/river_bed_img/Surface32.png" alt="" />
      </div>
      <div
        :class="active === 10 ? 'img active' : 'img'"
        title="深泓线比较"
        @click="activeClick('10')"
      >
        <img src="/river_bed_img/SpatialAnalystContourTool32.png" alt="" />
      </div>
      <el-dropdown @command="activeClick" trigger="click">
        <div :class="active === 11 ? 'img active' : 'img'" title="等深线对比">
          <img src="/river_bed_img/CadastralCreateLineString32.png" alt="" />
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="11-1">
              <img src="/river_bed_img/ElementLine16.png" />
              等深线对比
            </el-dropdown-item>
            <el-dropdown-item command="11-2">
              <img src="/river_bed_img/EditingCreateCOGOAttributes16.png" />
              等高线对比
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>

      <div
        :class="active === 12 ? 'img active' : 'img'"
        title="边界分析"
        @click="activeClick('12')"
      >
        <img src="/river_bed_img/3DAnalystCreateSteepestPath32.png" alt="" />
      </div>
      <div class="img close" title="停止编辑" @click="activeClick('0')">
        <img src="/river_bed_img/GenericBlackDelete_B_16.png" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import DataSelect from "../components/DataSelect.vue";
import mapBoxGl from "mapbox-gl";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import { MapUtils } from '../components'

export default defineComponent({
  emits: ["riverBed"],
  components: {
    DataSelect,
  },
  props: {
    map: {
      type: mapBoxGl.Map,
    },
  },
  setup(props, context) {
    const mapUtils = new MapUtils()
    const active = ref(0);
    const title = computed(() => {
      switch (active.value) {
        case 1:
          return "断面形态";
        case 2:
          return "断面比较";
        case 3:
          return "汊道断面对比";
        case 4:
          return "断面面积冲淤";
        case 5:
          return "任意区域冲淤";
        case 6:
          return "特定高程冲淤";
        case 7:
          return "冲淤等深线";
        case 8:
          return "河道容积计算";
        case 9:
          return "河床坡度提取";
        case 10:
          return "深泓线比较";
        case 11:
          return "等深线对比";
        case 12:
          return "边界分析";
        default:
          return "无";
      }
    });
    const activeClick = (num: string) => {
      if (num === "1-1" || num === "1-2") {
        active.value = 1;
        if (num === "1-1") {
          mapUtils.section_1(props.map, Draw)
        }
      } else if (num === "2-1" || num === "2-2") {
        active.value = 2;
      } else if (num === "3-1" || num === "3-2") {
        active.value = 3;
      } else if (num === "4-1" || num === "4-2") {
        active.value = 4;
      } else if (
        num === "5-1" ||
        num === "5-2" ||
        num === "5-3" ||
        num === "5-4"
      ) {
        active.value = 5;
      } else if (num === "6") {
        active.value = 6;
      } else if (num === "7") {
        active.value = 7;
      } else if (
        num === "8-1" ||
        num === "8-2" ||
        num === "8-3" ||
        num === "8-4"
      ) {
        active.value = 8;
      } else if (num === "9") {
        active.value = 9;
      } else if (num === "10") {
        active.value = 10;
      } else if (num === "11-1" || num === "11-2") {
        active.value = 11;
      } else if (num === "12") {
        active.value = 12;
      } else if (num === "0") {
        active.value = 0;
      }
      context.emit("riverBed", active.value);
    };

    const Draw = new MapboxDraw({
      controls: {
        combine_features: false,
        uncombine_features: false,
        trash: false
      },
    });

    return {
      active,
      title,
      activeClick,
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  border: solid 1px #888888;
  padding: 1px;
  background: #fafafa;
  .analyseHead {
    background: #a0a0a0;
    line-height: 25px;
    height: 25px;
    &:hover {
      cursor: move;
    }
  }
  .image {
    display: flex;
    .img {
      padding: 5px;

      &:hover {
        cursor: pointer;
      }
    }
    .active {
      // box-shadow: inset 0 2px 2px 2px rgba($color: #67cca6, $alpha: 0.5);
      background: rgba($color: #67cca6, $alpha: 0.4);
    }
    .close {
      padding-top: 13px;
    }
  }
}
</style>
