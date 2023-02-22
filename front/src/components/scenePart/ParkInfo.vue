<template>
  <div id="park-info">
    <el-descriptions direction="horizontal" :column="2" size="default" border>
      <el-descriptions-item label="名称" label-align="center" align="center">{{
        parkInfo == undefined ? "" : parkInfo.mc
      }}</el-descriptions-item>
      <el-descriptions-item label="用途" label-align="center" align="center">{{
        parkInfo == undefined ? "" : parkInfo.yt
      }}</el-descriptions-item>
      <el-descriptions-item
        label="管理单位"
        label-align="center"
        align="center"
        >{{ parkInfo == undefined ? "" : parkInfo.gldw }}</el-descriptions-item
      >
      <el-descriptions-item
        label="水道名称"
        label-align="center"
        align="center"
        >{{
          parkInfo == undefined ? "" : parkInfo.sd_name
        }}</el-descriptions-item
      >
    </el-descriptions>
    <div class="down-arrow">
      <div class="down-tri"></div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
export default defineComponent({
  props: {
    parkInfo: {
      type: Object,
    },
  },
  setup(props) {
    const parkInfo = computed(() => {
      return props.parkInfo;
    });

    return {
      parkInfo,
    };
  },
});
</script>

<style lang="scss">
#park-info {
  width: 26vw;
  height: 8vw;

  .el-descriptions {
    height: 100%;
    .el-descriptions__body {
      height: 100%;

      .el-descriptions__table {
        height: 100%;

        .el-descriptions__label {
          font-size: 0.75vw;
          font-weight: 600;
          color: rgb(0, 9, 39);
          font-family: "Microsoft YaHei";
          background-color: rgba(200, 214, 240, 0.5);
          border-color: rgb(159, 180, 218);
          border-width: 1px;
          // border-bottom-width: 0;
        }
        .el-descriptions__content {
          border-width: 1px;
          // border-top-width: 0;
          border-color: rgb(159, 180, 218);
          font-weight: 600;
          font-size: 0.8vw;
          transition: 0.3s ease-in-out;
          &:hover,
          &:focus {
            font-weight: 600;
            font-size: 0.9vw;
            color: rgb(96, 136, 211);
          }
        }

        tr {
          height: 25%;
        }
      }
    }
  }
}
</style>