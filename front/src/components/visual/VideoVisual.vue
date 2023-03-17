<template>
  <div class="video-visual">
    <el-row
      :gutter="20"
      justify="center"
      style="margin-bottom: 20px"
      v-if="urls.length > 1"
    >
      <el-col :span="8" style="text-align: center">
        <el-card
          class="video"
          shadow="always"
          style="margin-top: 30px; margin-bottom: 10px"
          @click="changeHandle('left')"
        >
          <div class="body">
            <div class="text" style="margin-left: 20px">
              <strong>{{ urls[leftIndex].fileName }}</strong>
            </div>
            <div class="icon icon-left">
              <el-icon><ArrowLeftBold /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" style="text-align: center">
        <el-card
          shadow="always"
          style="
            margin-top: 10px;
            margin-bottom: 10px;
            background-color: lightgrey;
          "
        >
          <div class="text">
            <strong>{{ urls[centerIndex].fileName }}</strong>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" style="text-align: center">
        <el-card
          class="video"
          shadow="always"
          style="margin-top: 30px; margin-bottom: 10px"
          @click="changeHandle('right')"
        >
          <div class="body">
            <div class="text">
              <strong>{{ urls[rightIndex].fileName }}</strong>
            </div>
            <div class="icon icon-right">
              <el-icon><ArrowRightBold /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <video
      ref="player"
      controls
      :src="urls[centerIndex].url"
      autoplay
      v-else
    ></video>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from "vue";

export default defineComponent({
  props: {
    urls: {
      type: Array,
    },
  },
  setup(props) {
    const player = ref();
    const urls = ref<{ url: string; fileName: string }[]>([]);
    const skeletonFlag = ref(true);

    const centerIndex = ref(0);

    const leftIndex = computed(() => {
      return (centerIndex.value - 1 + urls.value.length) % urls.value.length;
    });
    const rightIndex = computed(() => {
      return (centerIndex.value + 1) % urls.value.length;
    });

    const changeHandle = (handle: string) => {
      if (handle === "left") {
        centerIndex.value =
          (centerIndex.value - 1 + urls.value.length) % urls.value.length;
      } else {
        centerIndex.value = (centerIndex.value + 1) % urls.value.length;
      }
    };

    onMounted(() => {
      console.log(props.urls);
      urls.value = props.urls as { url: string; fileName: string }[];
      skeletonFlag.value = false;
    });

    return {
      urls,
      centerIndex,
      leftIndex,
      rightIndex,
      player,
      changeHandle,
      skeletonFlag,
    };
  },
});
</script>

<style lang="scss" scoped>
.video-visual {
  height: 100%;
  width: 100%;
  .el-card {
    cursor: pointer;
  }
  .body {
    width: 100%;
    position: relative;
    .icon {
      position: absolute;
      top: 2px;
      height: 20px;
      width: 20px;
    }
    .icon-right {
      right: 0px;
    }
    .icon-left {
      left: 0px;
    }
  }
  .text {
    width: calc(100% - 20px);
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
  }

  video {
    height: 500px;
    width: 100%;
    object-fit: cover;
  }
}
</style>