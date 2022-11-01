<template>
  <div class="basemap-main">
    <el-scrollbar height="400px">
      <div
        :class="selectNum === 0 ? 'map selected-map' : 'map'"
        @click="clickHandle(0)"
      >
        <div class="image" style="background: white"></div>
        <div class="text">无底图</div>
      </div>
      <div
        :class="selectNum === index + 1 ? 'map selected-map' : 'map'"
        v-for="(item, index) in mapListOnline"
        :key="index"
        @click="clickHandle(index + 1)"
      >
        <div class="image">
          <img :src="item.src" alt="" />
        </div>
        <div class="text">{{ item.text }}</div>
      </div>
    </el-scrollbar>
    <div class="btn">
      <el-button type="primary" plain @click="btnClick">确定</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { prefix } from '@/prefix'
export default defineComponent({
  emits: ["changeBasemap"],
  setup(_, contenxt) {
    const mapListOnline: { src: string; text: string; url: string }[] = [
      {
        src: "/basemap/basemap1.png",
        text: "中国底图灰色版（OSM 在线底图）",
        url: "mapbox://styles/johnnyt/cl9miecpn001t14rspop38nyk",
      },
      {
        src: "/basemap/basemap2.png",
        text: "中国底图蓝黑版（OSM 在线底图）",
        url: "mapbox://styles/johnnyt/cl9mkdnso000q15rlcoxiaab0",
      },
      {
        src: "/basemap/basemap3.png",
        text: "OSM-影像（OSM 在线底图）",
        url: "mapbox://styles/johnnyt/cl9mpbzya000c14l9n2p2wbxl",
      },
      {
        src: "/basemap/basemap4.png",
        text: "天地图（CGCS2000 天地图在线底图）",
        url: `${prefix}visual/getTianDiTu`,
      },
      {
        src: "/basemap/basemap5.png",
        text: "天地图-影像（CGCS2000 天地图在线底图）",
        url: `${prefix}visual/getTianDiTuImage`,
      },
      {
        src: "https://www.arcgis.com/sharing/rest/content/items/74e992f4fce3450aaf57a9a0df0007c3/info/thumbnail/cn_canvas.jpg?f=json",
        text: "中国底图灰色版（OSM 离线底图）",
        url: "",
      },
      {
        src: "https://www.arcgis.com/sharing/rest/content/items/24f2862ddba74de88bb5e99328b7db29/info/thumbnail/thumbnail1629179208957.png?f=json",
        text: "天地图（CGCS2000 天地图离线底图）",
        url: "",
      },
    ];
    const selectNum = ref(1);

    const clickHandle = (param: number) => {
      selectNum.value = param;
    };

    const btnClick = () => {
      let url = "";
      if (selectNum.value != 0) {
        url = mapListOnline[selectNum.value - 1].url;
      }
      contenxt.emit("changeBasemap", url);
    };



    return {
      mapListOnline,
      selectNum,
      clickHandle,
      btnClick,
    };
  },
});
</script>


<style lang="scss" scoped>
.basemap-main {
  width: 100%;
  .map {
    height: 80px;
    display: flex;
    cursor: pointer;
    border-left: solid 4px white;
    &:hover {
      background: #f3f3f3;
      border-left: solid 4px #f3f3f3;
    }
    .image {
      border-radius: 8px;
      width: 70px;
      height: 70px;
      margin: 5px 10px;
      img {
        height: 100%;
        width: 100%;
        border-radius: 8px;
      }
    }
    .text {
      line-height: 80px;
    }
  }
  .selected-map {
    background: #e2f1fb;
    border-left: solid 4px #0079c1;
    box-sizing: border-box;
    &:hover {
      background: #e2f1fb;
      border-left: solid 4px #0079c1;
    }
  }
  .btn {
    margin-top: 15px;
    text-align: center;
    height: 50px;
  }
}
</style>