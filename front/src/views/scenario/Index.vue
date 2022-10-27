<template>
  <div class="main">
    <page-header :pageTitle="'一张图'">
      <template #search> </template>
    </page-header>
    <div class="container" ref="container">
      <div class="right" ref="right">
        <div></div>
        <div class="btn" @click="btnClick">
          <el-icon v-if="flag"><ArrowLeftBold /></el-icon>
          <el-icon v-else><ArrowRightBold /></el-icon>
        </div>
        <div class="body"></div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import PageHeader from "@/components/page/PageHeader.vue";
import ScenarioCard from "@/components/cards/ScenarioCard.vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
export default defineComponent({
  components: { PageHeader, ScenarioCard },
  setup() {
    const search = ref("");
    const container = ref<HTMLElement>();
    const flag = ref(true);
    const right = ref<HTMLElement>();
    let map: mapBoxGl.Map;

    const initMap = () => {
      map = new mapBoxGl.Map({
        container: container.value as HTMLElement,
        accessToken:
          "pk.eyJ1Ijoiam9obm55dCIsImEiOiJja2xxNXplNjYwNnhzMm5uYTJtdHVlbTByIn0.f1GfZbFLWjiEayI6hb_Qvg",
        style: "mapbox://styles/johnnyt/cl9pb20ze00bo14mebyl7p9as",
        center: [121.18, 31.723],
        zoom: 8,
      });
      map.on("load", () => {
        map.loadImage("/png/潮位站.png", (err, image) => {
          if (err) throw err;
          map.addImage("tide", image as HTMLImageElement | ImageBitmap);
          map.addSource("tide", {
            type: "vector",
            tiles: ["http://localhost:8002/visual/scenario/tide/{x}/{y}/{z}"],
          });
          map.addLayer({
            id: "tide",
            source: "tide",
            type: "symbol",
            "source-layer": "tide",
            layout: {
              "icon-image": "tide",
              "icon-size": 0.2,
            },
          });
        });
      });
    };

    const btnClick = () => {
      if (!flag.value) {
        (right.value as HTMLElement).style.width = 30 + "px";
      } else {
        (right.value as HTMLElement).style.width = 700 + "px";
      }
      flag.value = !flag.value;
    };

    const searchClick = () => {};

    onMounted(() => {
      initMap();
    });

    return {
      search,
      searchClick,
      container,
      btnClick,
      right,
      flag,
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  height: calc(100vh - 63px);
  position: relative;
  .search {
    .el-input {
      width: 500px;
      margin-left: 50px;
      margin-right: 20px;
    }
  }
  .container {
    height: calc(100% - 105px);
    margin-top: 5px;
    position: relative;
    .right {
      position: absolute;
      height: 100%;
      top: 0;
      right: 0;
      display: flex;
      width: 30px;
      z-index: 99;
      transition: 1s;
      .btn {
        width: 30px;
        background: rgb(231, 231, 231);
        height: 100px;
        margin-top: calc(50vh - 89px - 50px);
        border-top-left-radius: 8px;
        border-bottom-left-radius: 8px;
        cursor: pointer;
        .el-icon {
          height: 100px;
          width: 30px;
          font-size: 20px;
        }
      }
      .body {
        background: rgba($color: #000000, $alpha: 0.3);
        width: calc(100% - 30px);
      }
    }
  }
}
</style>