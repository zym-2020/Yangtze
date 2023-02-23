<template>
  <div id="buoy-info">
    <el-row :gutter="0" id="buoy-card">
      <el-col :span="8" :offset="0" id="buoy-img-box">
        <el-image
          id="buoy-image"
          :src="
            buoyInfo == undefined
              ? ''
              : buoyInfo.hbphoto == ''
              ? '/nophoto.png'
              : prefix + 'multiSource/img/' + buoyInfo.hbphoto
          "
          :preview-src-list="[
            buoyInfo == undefined
              ? ''
              : buoyInfo.hbphoto == ''
              ? '/nophoto.png'
              : prefix + 'multiSource/img/' + buoyInfo.hbphoto,
          ]"
          :initial-index="0"
          fit="scale-down"
        />
      </el-col>
      <el-col :span="14" id="buoy-text">
        <el-descriptions
          direction="horizontal"
          :column="1"
          size="default"
          border
        >
          <el-descriptions-item
            label="名称"
            label-align="center"
            align="center"
            >{{
              buoyInfo == undefined ? "" : buoyInfo.hbmc
            }}</el-descriptions-item
          >
          <el-descriptions-item
            label="颜色"
            label-align="center"
            align="center"
            >{{
              buoyInfo == undefined ? "" : buoyInfo.bsys
            }}</el-descriptions-item
          >
          <el-descriptions-item
            label="形状"
            label-align="center"
            align="center"
          >
            {{ buoyInfo == undefined ? "" : buoyInfo.hbxz }}
          </el-descriptions-item>
          <el-descriptions-item
            label="所属航道"
            label-align="center"
            align="center"
          >
            {{ buoyInfo == undefined ? "" : buoyInfo.sshd }}
          </el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
    <el-row id="down-arrow">
      <el-col :span="24">
        <div class="down-arrow"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
import { prefix } from "@/prefix";
export default defineComponent({
  props: {
    buoyInfo: {
      type: Object,
    },
  },
  setup(props) {
    const buoyInfo = computed(() => {
      return props.buoyInfo;
    });

    return {
      buoyInfo,
      prefix,
    };
  },
});
</script>

<style lang="scss">
#buoy-info {
  min-width: 300px;

  width: 24vw;
  height: 11vw;
  // transition: 0.1s ease-in-out;
  // transition: z-index 0s ease-in-out;

  .el-row#buoy-card {
    align-items: center;
    height: 100%;
    // background-color: antiquewhite;

    #buoy-image {
      // margin-top: 4px;
      height: 100%;
      // padding-left: 2px;
      // border-width: 2px;
      // border-color: rgb(159, 180, 218);
      border: 1px solid rgb(159, 180, 218);
      box-sizing: border-box;
      border-top-left-radius: 0.2vw;
      border-bottom-left-radius: 0.2vw;
      background-color: rgb(218, 237, 255);
    }

    .el-col {
      height: 100%;

      &#buoy-text {
        .el-descriptions {
          height: 100%;
          .el-descriptions__body {
            height: 100%;

            .el-descriptions__table {
              height: 100%;
              border-top-right-radius: 5vw;

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

      &#buoy-img-box {
        display: flex;
        justify-content: center;
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