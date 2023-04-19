<template>
  <div :class="active ? 'bridge-info active' : 'bridge-info'">
    <div class="title">
      <strong>桥梁信息</strong>
      <div class="close">
        <el-button type="info" link @click="closeClick"
          ><el-icon><CircleClose /></el-icon
        ></el-button>
      </div>
    </div>
    <div class="content">
      <div class="bridge-name">
        {{ bridgeInfo == undefined ? "" : bridgeInfo["qlmc"] }}
      </div>
      <div class="info">
        <el-scrollbar>
          <el-descriptions border :column="1">
            <el-descriptions-item
              label="桥区水域范围"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined ? "" : bridgeInfo["syfw"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥涵标"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["qhb"] != undefined
                  ? bridgeInfo["qhb"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥区水域水上航路航法"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["hlhf"] != undefined
                  ? bridgeInfo["hlhf"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="航评批复文件"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["hppfwj"] != undefined
                  ? bridgeInfo["hppfwj"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="海事管理机构"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["hsgljg"] != undefined
                  ? bridgeInfo["hsgljg"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥梁属性"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["qlsx"] != undefined
                  ? bridgeInfo["qlsx"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="设计通航代表船型、船队"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["sjthdbcx"] != undefined
                  ? bridgeInfo["sjthdbcx"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥柱灯"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["qzd"] != undefined
                  ? bridgeInfo["qzd"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="区段通航代表船型、船队"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["qdthdbcx"] != undefined
                  ? bridgeInfo["qdthdbcx"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥梁主动防撞设施"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["qlzdfzss"] != undefined
                  ? bridgeInfo["qlzdfzss"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥梁运行管理单位"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["qlyxgldw"] != undefined
                  ? bridgeInfo["qlyxgldw"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="通车时间"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["tcsj"] != undefined
                  ? bridgeInfo["tcsj"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥梁被动防撞设施"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["qlbdfzss"] != undefined
                  ? bridgeInfo["qlbdfzss"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="日均断面流量"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["rjdmll"] != undefined
                  ? bridgeInfo["rjdmll"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥区视频设施"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["qqspss"] != undefined
                  ? bridgeInfo["qqspss"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="已建立的桥区水域水上交通安全管理制度"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["glzd"] != undefined
                  ? bridgeInfo["glzd"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥区水域内临河设施"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["lhss"] != undefined
                  ? bridgeInfo["lhss"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="主通航孔编号"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["zthkbh"] != undefined
                  ? bridgeInfo["zthkbh"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥区水域内锚地、停泊区"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["tbq"] != undefined
                  ? bridgeInfo["tbq"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥区水域内跨河设施"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["khss"] != undefined
                  ? bridgeInfo["khss"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥区水上航标布置"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["hbbz"] != undefined
                  ? bridgeInfo["hbbz"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="小轮通航孔编号"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["xlthkbh"] != undefined
                  ? bridgeInfo["xlthkbh"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="航道养护单位"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["yhdw"] != undefined
                  ? bridgeInfo["yhdw"]
                  : ""
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="主通航孔桥墩防船撞能力"
              label-align="center"
              align="center"
              >{{
                bridgeInfo == undefined
                  ? ""
                  : bridgeInfo["fznl"] != undefined
                  ? bridgeInfo["fznl"]
                  : ""
              }}</el-descriptions-item
            >
          </el-descriptions>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, PropType, ref } from "vue";
import { Bridge } from "@/type";
export default defineComponent({
  props: {
    bridgeInfo: {
      type: Object as PropType<Bridge>,
    },
  },
  setup(props) {
    const active = ref(false);

    const bridgeInfo = computed(() => {
      return props.bridgeInfo;
    });

    const popupClick = () => {
      active.value = true;
    };

    const closeClick = () => {
      active.value = false;
    };

    return {
      active,
      popupClick,
      bridgeInfo,
      closeClick,
    };
  },
});
</script>

<style lang="scss" scoped>
.bridge-info {
  position: absolute;
  left: 0vw;
  width: 360px;
  height: 74vh;
  top: 100px;
  z-index: 99;
  border-top-left-radius: 6px;
  border-bottom-left-radius: 6px;
  transform: translateX(-400px);
  background: white;
  &.active {
    transform: translateX(0);
  }
  transition: transform 0.4s ease-in-out;
  padding: 20px;

  .title {
    color: #1e88e5;
    margin-bottom: 10px;
    height: 20px;
    position: relative;
    .close {
      position: absolute;
      right: 0px;
      top: 0px;
    }
  }
  .content {
    width: 340px;
    padding: 10px;
    border: solid 1px #c8c8c8;
    .bridge-name {
      margin-bottom: 10px;
      font-weight: 400;
    }
    .info {
      text-align: center;
      height: calc(74vh - 100px);
      .el-scrollbar {
        height: 100%;
      }
    }
  }
}
</style>