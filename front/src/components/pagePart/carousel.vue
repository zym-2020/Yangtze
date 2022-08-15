<template>
  <div class="carousel-wrapper" @mouseenter="focusOn" @mouseleave="focusOff">
    <div class="carousel">
      <div
        v-for="item in carousels"
        :key="item.index"
        class="items"
        :class="item.pos"
        :id="'index' + item.index"
      >
        <!-- <p>{{ item.index }}</p> -->
      </div>
    </div>
    <div class="control-wrapper">
      <div class="left-control" @click="turnLeft"></div>
      <div class="right-control" @click="turnRight"></div>
    </div>
  </div>
</template>

<script lang='ts'>
import { ref } from "vue";

export default {
  name: "carousel-gallery",
  setup() {
    // dom info list
    const carousels = ref([
      { pos: "main-pos", index: "1" },
      { pos: "right-pos", index: "2" },
      { pos: "back-pos", index: "3" },
      { pos: "back-pos", index: "4" },
      { pos: "back-pos", index: "5" },
      { pos: "left-pos", index: "6" },
    ]);
    
    // carousel roll forward
    const rollForward = function (): void {
      let len = carousels.value.length;
      let temp = carousels.value[0].pos;
      carousels.value[0].pos = carousels.value[len - 1].pos;
      for (let i = len - 1; i > 1; i--) {
        carousels.value[i].pos = carousels.value[i - 1].pos;
      }
      carousels.value[1].pos = temp;
    };
    // carousel roll backward
    const rollBackward = function (): void {
      let len = carousels.value.length;
      let temp = carousels.value[len - 1].pos;
      carousels.value[len - 1].pos = carousels.value[0].pos;
      for (let i = 0; i < len - 2; i++) {
        carousels.value[i].pos = carousels.value[i + 1].pos;
      }
      carousels.value[len - 2].pos = temp;
    };

    // auto rolling
    let rolling = setInterval(rollForward, 1500);

    // stop rolling
    const focusOn = function (): void {
      clearInterval(rolling);
    };
    // start rolling again
    const focusOff = function (): void {
      rolling = setInterval(rollForward, 1500);
    };

    const turnLeft = rollBackward;
    const turnRight = rollForward;
    // onMounted(() => {
    //     setInterval(rollForward, 1500)
    // });
    return {
      carousels,
      focusOn,
      focusOff,
      turnLeft,
      turnRight,
    };
  },
};
</script>

<style lang='scss' scoped>
$carouselBg: "../../assets/home/carousel";
$indexs: "index1", "index2", "index3", "index4", "index5", "index6";

div.carousel-wrapper {
  width: 48em;
  height: 32em;
  margin: 0 auto;
  top: 2vh;
  position: relative;
  background-color: transparent;

  div.carousel {
    width: 100%;
  }

  div {
    &.items {
      width: 100%;
      height: 85%;
      position: absolute;
      border-radius: 5%;
      background: rgb(189, 189, 189);
      display: inline-block;
      -webkit-transition: all 0.3s ease-in-out;
      -moz-transition: all 0.3s ease-in-out;
      transition: all 0.3s ease-in-out;
      p {
        color: rgb(72, 173, 255);
        font-weight: bold;
        font-size: 5em;
        text-align: center;
        margin-top: 1.175em;
      }
    }

    // 添加对应编号的图片 ！！注意文件命名规则！！
    @each $index in $indexs {
      &[id="#{$index}"] {
        background-image: url($carouselBg + $index + ".png"); // 需要改路径
        background-size: cover;
      }
    }

    &.main-pos {
      z-index: 2000;
      box-shadow: 0 0 8em rgb(0, 0, 0);
    }
    &.left-pos {
      opacity: 0.3;
      margin-left: -40.5em !important;
      z-index: 1000;
      -webkit-transform: scale(0.78);
      -moz-transform: scale(0.78);
      transform: scale(0.78);
    }
    &.back-pos {
      margin-left: 2em !important;
      opacity: 0.05;
      -webkit-transform: scale(0.5);
      -moz-transform: scale(0.5);
      transform: scale(0.5);
    }
    &.right-pos {
      opacity: 0.3;
      margin-left: 40.5em !important;
      z-index: 1000;
      -webkit-transform: scale(0.78);
      -moz-transform: scale(0.78);
      transform: scale(0.78);
      overflow: hidden;
    }
  }
}

div.control-wrapper {
  height: 8%;
  padding-top: 27.8em;
  background: transparent;
  z-index: 2500;

  div {
    position: absolute;
    box-sizing: border-box;
    height: 1.5em;
    width: 1.5em;
    border-style: solid;
    border-color: rgba(238, 238, 238, 0.85);
    border-width: 0px 3px 3px 0px;
    transition: 150ms ease-in-out;

    &:hover {
      height: 1.8em;
      width: 1.8em;
      border-bottom-width: 5px;
      border-right-width: 5px;
      cursor: pointer;
      border-color: rgb(255, 255, 255);
    }

    &.left-control {
      left: 45%;
      transform: rotate(135deg);
    }

    &.right-control {
      right: 45%;
      transform: rotate(-45deg);
    }
  }
}
</style>