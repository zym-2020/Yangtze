<template>
  <div class="scenario-card">
    <div>
      <div class="image">
        <img :src="info.img" />
      </div>
    </div>
    <div class="text">
      <div class="title">
        <strong>{{ info.title }}</strong>
      </div>
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
@keyframes move {
  0% {
    transform: scale(1);
  }

  100% {
    transform: scale(1.1);
  }
}
.scenario-card {
  height: 360px;
  width: 550px;
  background: #f4f4f5;
  border-radius: 4px;
  padding: 20px;
  cursor: pointer;
  .image {
    width: 500px;
    height: 300px;
    // padding-top: 20px;
    margin: 0 auto 10px;
    overflow: hidden;
    img {
      width: 100%;
      height: 300px;
    }
  }

  .text {
    text-align: center;
    font-size: 20px;
  }

  &:hover {
    img {
      animation: move 0.3s linear forwards;
    }
  }
}
</style>