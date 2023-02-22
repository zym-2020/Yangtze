<template>
  <div id="ship-info" :class="{ active: cardActive }">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span class="ship-name">{{ shipInfo.name }}</span>
          <br />
          <span class="ship-type">Cargo Ship</span>
          <el-icon id="hide-icon" size="1.6vw" @click="HideInfo()"
            ><DArrowLeft
          /></el-icon>
        </div>
      </template>
      <img
        src="https://static.vesselfinder.net/images/cool-ship2@1.png"
        class="ship-image"
      />
      <el-descriptions direction="vertical" :column="4" size="large" border>
        <el-descriptions-item
          label="速度"
          label-align="center"
          align="center"
          >{{ shipInfo.velocity + "km/h" }}</el-descriptions-item
        >
        <el-descriptions-item
          label="航向"
          label-align="center"
          align="center"
          >{{ shipInfo.direction + "°" }}</el-descriptions-item
        >
        <el-descriptions-item
          label="MMSI"
          :span="2"
          label-align="center"
          align="center"
        >
          {{ shipInfo.mmsi }}
        </el-descriptions-item>
        <!-- <el-descriptions-item label="吃水深度" :span="2" label-align="center" align="center">{{shipInfo.draught}}</el-descriptions-item> -->
        <!-- <el-descriptions-item label="登记时间" :span="2" label-align="center" align="center">
            {{shipInfo.registerTime}}
          </el-descriptions-item> -->
        <el-descriptions-item
          label="船长/宽"
          :span="1"
          label-align="center"
          align="center"
        >
          {{
            parseFloat(shipInfo.length).toFixed(1) +
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
          {{ shipInfo.update_time }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref } from "vue";
import { Ship } from "@/type";
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
  position: absolute;
  left: 0vw;
  width: 20vw;
  // height: 66vh;
  top: 16vh;
  z-index: 99;
  min-width: 300px;
  transform: translateX(-24vw);
  &.active {
    transform: translateX(0);
  }
  transition: transform 0.4s ease-in-out;

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
}
</style>