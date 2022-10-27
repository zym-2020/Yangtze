<template>
  <div class="scenario-card">
    <div class="image">
      <img :src="info.img" />
    </div>
    <div class="text">
      <div class="title">
        <strong>{{ info.title }}</strong>
      </div>
      <div class="creator">发布：{{ info.creator }}</div>
      <div class="time">时间：{{ info.time }}</div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
export default defineComponent({
  props: {
    cardInfo: {
      type: Object,
    },
  },
  setup(props) {
    const info = computed(() => {
      return props.cardInfo;
    });

    return {
      info,
    };
  },
});
</script>

<style lang="scss" scoped>
.scenario-card {
  width: 400px;
  .image {
    height: 220px;
    img {
      height: 100%;
      width: 100%;
    }
  }
  .text {
    margin: 20px 15px 0px;
    .title {
      height: 30px;
      line-height: 30px;
    }
    .creator,
    .time {
      height: 25px;
      line-height: 25px;
    }
  }
}
</style>