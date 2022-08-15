<template>
  <div class="top-content">
    <div>
      <img
        src="/river_bed_img/TinEditingTinLineAdd32.png"
        alt=""
        class="image"
      />
    </div>
    <el-divider direction="vertical" />
    <div>
      <img src="/river_bed_img/TinEditingTinLineAdd32.png" alt="" class="image"/>
    </div>
    <el-divider direction="vertical" />
    <div>
      <img src="/river_bed_img/TinEditingTinLineAdd32.png" alt="" class="image"/>
    </div>
    <el-divider direction="vertical" />
    <div>
      <img src="/river_bed_img/TinEditingTinLineAdd32.png" alt="" class="image"/>
    </div>
    <el-divider direction="vertical" />
    <div>
      <img src="/river_bed_img/TinEditingTinLineAdd32.png" alt="" class="image"/>
    </div>
    <el-divider direction="vertical" />
    <div>
      <img src="/river_bed_img/TinEditingTinLineAdd32.png" alt="" class="image"/>
    </div>
    <el-divider direction="vertical" />
    <div>
      <img src="/river_bed_img/TinEditingTinLineAdd32.png" alt="" class="image"/>
    </div>
    <el-divider direction="vertical" />
    <div>
      <img src="/river_bed_img/TinEditingTinLineAdd32.png" alt="" />
    </div>
    <el-divider direction="vertical" />
    <div>
      <img src="/river_bed_img/TinEditingTinLineAdd32.png" alt="" />
    </div>
    <el-divider direction="vertical" />
    <div>
      <img src="/river_bed_img/TinEditingTinLineAdd32.png" alt="" />
    </div>
    <el-divider direction="vertical" />
    <div>
      <img src="/river_bed_img/TinEditingTinLineAdd32.png" alt="" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
export default defineComponent({
  setup() {},
});
</script>


<style lang="scss" scoped>
@-webkit-keyframes scale-up-center {
  0% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
  }
  100% {
    -webkit-transform: scale(1);
    transform: scale(1);
  }
}
@keyframes scale-up-center {
  0% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
  }
  100% {
    -webkit-transform: scale(1);
    transform: scale(1);
  }
}
.top-content {
  height: 100%;
  border-radius: 6px;
  background: rgba($color: #000000, $alpha: 0.5);
  display: flex;
  cursor: pointer;
  .el-divider {
    margin-top: 17px;
  }
  .image {
    &:hover {
      width: 50px;
      height: 50px;
      transition: all 10s;
    }
  }
}
</style>