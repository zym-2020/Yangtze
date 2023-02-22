<template>
  <div id="anchor-info">
    <el-descriptions direction="horizontal" :column="2" size="default" border>
      <el-descriptions-item label="名称" label-align="center" align="center">{{
        anchorInfo == undefined ? "" : anchorInfo.mdmc
      }}</el-descriptions-item>
      <el-descriptions-item label="用途" label-align="center" align="center">{{
        anchorInfo == undefined ? "" : anchorInfo.yt
      }}</el-descriptions-item>
      <el-descriptions-item
        label="管理单位"
        :span="2"
        label-align="center"
        align="center"
        >{{
          anchorInfo == undefined ? "" : anchorInfo.gldw
        }}</el-descriptions-item
      >
      <el-descriptions-item
        label="水道名称"
        label-align="center"
        align="center"
        >{{
          anchorInfo == undefined ? "" : anchorInfo.sd_name
        }}</el-descriptions-item
      >
      <el-descriptions-item
        label="建成时间"
        label-align="center"
        align="center"
        >{{
          anchorInfo == undefined ? "" : anchorInfo.jcsj
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
    anchorInfo: {
      type: Object,
    },
  },
  setup(props) {
    const anchorInfo = computed(() => {
      return props.anchorInfo;
    });

    return {
      anchorInfo,
    };
  },
});
</script>


<style lang="scss">
#anchor-info {
  width: 24vw;
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
.down-arrow {
  width: 100%;
  display: flex;
  justify-content: center;
  .down-tri {
    width: 0px;
    height: 0px;
    border-top: 10px solid rgb(107, 176, 255);
    border-left: 10px solid transparent;
    border-right: 10px solid transparent;
  }
}
</style>