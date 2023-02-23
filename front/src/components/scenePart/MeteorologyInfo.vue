<template>
  <div class="meteorology-info">
    <div class="title">
      {{ meteorologyInfo == undefined ? "" : meteorologyInfo.title }}
    </div>
    <div class="content">
      <div class="headline">
        {{ meteorologyInfo == undefined ? "" : meteorologyInfo.headline }}
      </div>
      <div class="effective">
        发布时间:
        {{ meteorologyInfo == undefined ? "" : meteorologyInfo.effective }}
      </div>
      <div class="description">
        {{ meteorologyInfo == undefined ? "" : meteorologyInfo.description }}
      </div>
    </div>
    <div class="bottom">
      <div class="down-arrow"></div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
export default defineComponent({
  props: {
    meteorologyInfo: {
      type: Object,
    },
  },
  setup(props) {
    const meteorologyInfo = computed(() => {
      return props.meteorologyInfo;
    });

    return {
      meteorologyInfo,
    };
  },
});
</script>

<style lang="scss" scoped>
.meteorology-info {
  width: 400px;
  height: 230px;
  .title {
    padding: 10px;
    color: white;
    background: #4989c6;
    font-size: 14px;
    height: 20px;
  }
  .content {
    padding: 10px;
    height: 160px;
    font-size: 14px;
    background: white;
    .headline {
      margin-bottom: 15px;
    }
    .effective {
      margin-bottom: 15px;
    }
    .description {
      line-height: 25px;
    }
  }
  .bottom {
    height: 10px;
    position: relative;
    .down-arrow {
      position: absolute;
      width: 0px;
      height: 0px;
      top: 0;
      left: 190px;
    //   background: red;
      border-top: 10px solid white;
      border-bottom: 10px solid transparent;
      border-left: 10px solid transparent;
      border-right: 10px solid transparent;
    }
  }
}
</style>