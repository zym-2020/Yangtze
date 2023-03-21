<template>
  <div id="ship-info">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span class="ship-name">{{
            shipInfo == undefined ? "" : shipInfo.name
          }}</span>
          <br />
          <span class="ship-type">Cargo Ship</span>
        </div>
      </template>

      <el-descriptions direction="vertical" :column="4" size="large" border>
        <el-descriptions-item
          label="速度"
          label-align="center"
          align="center"
          >{{
            shipInfo == undefined ? "" : shipInfo.velocity + "km/h"
          }}</el-descriptions-item
        >
        <el-descriptions-item
          label="航向"
          label-align="center"
          align="center"
          >{{
            shipInfo == undefined ? "" : shipInfo.direction + "°"
          }}</el-descriptions-item
        >
        <el-descriptions-item
          label="MMSI"
          :span="2"
          label-align="center"
          align="center"
        >
          {{ shipInfo == undefined ? "" : shipInfo.mmsi }}
        </el-descriptions-item>
        <el-descriptions-item
          label="船长/宽"
          :span="1"
          label-align="center"
          align="center"
        >
          {{
            shipInfo == undefined
              ? ""
              : parseFloat(shipInfo.length).toFixed(1) +
                " / " +
                parseFloat(shipInfo.width).toFixed(1)
          }}
        </el-descriptions-item>
        <el-descriptions-item
          label="更新时间"
          :span="3"
          label-align="center"
          align="center"
        >
          {{ shipInfo == undefined ? "" : shipInfo.update_time }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
    <el-row id="down-arrow">
      <el-col :span="24">
        <div class="down-arrow"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref } from "vue";
export default defineComponent({
  props: {
    shipInfo: {
      type: Object,
    },
  },
  setup(props) {
    const cardActive = ref(false);
    const shipInfo = computed(() => {
      return props.shipInfo;
    });

    const HideInfo = () => {
      cardActive.value = false;
    };

    const extend = () => {
      cardActive.value = true;
    };

    return {
      shipInfo,
      HideInfo,
      cardActive,
      extend,
    };
  },
});
</script>

<style lang="scss" scoped>
#ship-info {
  width: 20vw;
  height: 285px;

  z-index: 99;
  min-width: 300px;

  /deep/ .el-card {
    height: 100%;
    backdrop-filter: blur(2px);
    background-color: rgba(240, 240, 240, 0.8);

    .el-card__header {
      background-color: rgb(159, 180, 218);
    }

    &:focus,
    &:hover {
      box-shadow: 0 0 8px rgb(88, 88, 88);
    }

    .card-header {
      .ship-name {
        font-size: 1vw;
        font-weight: 600;
        margin-bottom: 20px;
      }
      .ship-type {
        font-size: 0.8vw;
        font-weight: 500;
        top: 10px;
        color: rgb(54, 54, 54);
      }

      .el-icon {
        position: absolute;
        right: 1vw;
        top: 2.8vh;
        transition: 0.4s ease-in-out;

        &:hover,
        &:focus {
          cursor: pointer;
          transform: translateX(-0.3vw);
        }
      }
    }

    .el-card__body {
      padding: 0px;

      .ship-image {
        width: 100%;
        display: block;
      }

      .el-descriptions {
        margin-top: 5px;
        .el-descriptions__label {
          font-size: 0.9vw;
          font-weight: 600;
          color: rgb(0, 9, 39);
          font-family: "Microsoft YaHei";
          background-color: rgba(200, 214, 240, 0.5);
          border-color: rgb(159, 180, 218);
          border-width: 2px;
          border-bottom-width: 0;
        }
        .el-descriptions__content {
          border-width: 2px;
          border-top-width: 0;
          border-color: rgb(159, 180, 218);
          font-size: 0.8vw;
          transition: 0.3s ease-in-out;
          &:hover,
          &:focus {
            font-weight: 600;
            font-size: 0.9vw;
            color: rgb(96, 136, 211);
          }
        }
      }
    }
  }

  .el-row#down-arrow {
    width: 100%;
    .el-col {
      display: flex;
      justify-content: center;
      .down-arrow {
        width: 0px;
        height: 0px;
        border-top: 10px solid rgb(107, 176, 255);
        border-left: 10px solid transparent;
        border-right: 10px solid transparent;
      }
    }
  }
}
</style>