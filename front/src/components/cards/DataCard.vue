<template>
  <div>
    <div
      class="data-card"
      :style="{
        backgroundColor: checkInDataBoo == true ? 'rgba(30,144,255,0.1)' : '',
      }"
    >
      <div @click="toDetail">
        <el-row>
          <el-col :span="23">
            <div class="top">
              <div class="text">{{ name }}</div>
              <slot name="creator"></slot>
              <slot name="status"></slot>
              <div
                v-show="watchs >= 20"
                style="float: right; margin-left: auto"
              >
                <el-tag
                  style="margin-top: 10px; margin-left: 5px"
                  type="danger"
                  effect="dark"
                  round
                >
                  HOT
                </el-tag>
              </div>
            </div>
          </el-col>
          <el-col :span="1" style="margin-top: 10px">
            <div style="text-align: right">
              <el-tooltip content="公开" placement="left-start" effect="light">
                <el-icon :size="25" color="#00CD00" style="margin-right: 10px"
                  ><Unlock
                /></el-icon>
              </el-tooltip>
            </div>
          </el-col>
        </el-row>
        <div v-show="shdes">
          <el-row style="margin-top: 10px">
            <el-col :span="6">
              <div class="block">
                <el-image
                  style="width: 250px; height: 152px"
                  :src="avatar"
                  fit="fill"
                />
              </div>
            </el-col>
            <el-col :span="18">
              <el-row>
                <div class="des">
                  {{ description }}
                </div>
              </el-row>
            </el-col>
          </el-row>
          <el-row>
            <el-col :offset="6" :span="16">
              <div style="position: relative; display: inline">
                <el-tag
                  v-for="(item, index) in tagList"
                  :key="index"
                  size="large"
                  style="
                    margin-right: 8px;
                    background-color: rgba(21, 69, 153, 0.8);
                    color: white;
                  "
                  effect="dark"
                  round
                >
                  {{ item }}
                </el-tag>
              </div>
              <div
                style="
                  position: relative;
                  display: inline;
                  margin-right: 30px;
                  float: right;
                "
              >
                <el-card shadow="always"> 已认证为可视化数据 </el-card>
              </div>
            </el-col>
          </el-row>
        </div>
        <div v-if="shsta" style="width: 100%; height: 300px">
          <DownLoadSta2 :id="id"></DownLoadSta2>
        </div>
      </div>
      <el-divider style="color: #00bfff">
        <el-icon><star-filled /></el-icon>
      </el-divider>
      <el-row>
        <el-col :span="1">
          <div style="left: 10px; position: relative">
            <el-avatar :size="42" :src="userAvatar"></el-avatar>
          </div>
        </el-col>
        <el-col :span="4">
          <div style="margin-left: 20px; margin-top: 10px; font-size: 8px">
            <span style="color: #6495ed">{{ creator }}</span>
          </div>
        </el-col>
        <el-col :span="5">
          <div
            style="
              margin-left: 10px;
              margin-top: 8px;
              font-size: 8px;
              text-align: left;
            "
          >
            <strong>数据上传于：</strong>
            {{ createTime }}
          </div>
        </el-col>
        <el-col :span="5">
          <div
            style="
              margin-left: 10px;
              margin-top: 8px;
              font-size: 8px;
              text-align: left;
            "
          >
            <strong>数据时间：</strong>
            {{ timeStamp }}
          </div>
        </el-col>
        <el-col
          :span="6"
          style="text-align: right; position: relative; right: 50px"
        >
          <el-button color="#4682B4" plain @click="showDes">数据介绍</el-button>
          <el-button color="#4682B4" plain @click="showSta">数据统计</el-button>
        </el-col>
        <el-col :span="2">
          <div
            style="
              margin-left: 10px;
              margin-top: 6px;
              text-align: right;
              right: 15px;
              position: relative;
            "
          >
            <el-icon style="margin-right: 5px"><View /></el-icon>
            <span>{{ watchs }}</span>
          </div>
        </el-col>
        <el-col
          :span="1"
          style="
            margin-top: 6px;
            text-align: right;
            right: 15px;
            position: relative;
          "
        >
          <el-icon style="margin-right: 5px"><Download /></el-icon>
          <span>{{ download }}</span>
        </el-col>
      </el-row>
    </div>
    <slot name="border"></slot>
  </div>
  <div>
    <hr style="border-color: #d8d8d8" />
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import { imgBase64, generateColorByText, dateFormat } from "@/utils/common";
import BrowseStatistics from "../resourcePages/components/BrowseStatistics.vue";
import DownLoadSta2 from "../resourcePages/components/DownSta2.vue";
import { prefix } from '@/prefix'
export default defineComponent({
  components: { BrowseStatistics, DownLoadSta2 },
  props: {
    fileInfo: {
      type: Object,
    },
  },
  emits: ["toDetail", "getDataSet"],
  setup(props, context) {
    const shdes = ref(true);
    const shsta = ref(false);
    const checkInDataBoo = ref(false);
    const name = computed(() => {
      return (props.fileInfo as any).name;
    });
    const avatar = computed(() => {
      if (
        (props.fileInfo as any).avatar != "" &&
        (props.fileInfo as any).avatar != undefined &&
        (props.fileInfo as any).avatar != null
      ) {
        return (
          prefix + "visual/getAvatar/" +
          (props.fileInfo as any).avatar
        );
      }

      return imgBase64(name.value);
    });
    const userAvatar = computed(() => {
      if ((props.fileInfo as any).userAvatar != "") {
        return (
          prefix + "visual/getAvatar/" +
          (props.fileInfo as any).userAvatar
        );
      } else {
        return "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png";
      }
    });
    const description = computed(() => {
      if ((props.fileInfo as any).description) {
        return (props.fileInfo as any).description;
      } else {
        return "该条目暂无简介";
      }
    });
    const creator = computed(() => {
      return (props.fileInfo as any).userName;
    });

    const id = computed(() => {
      return (props.fileInfo as any).id;
    });
    const timeStamp = computed(() => {
      return dateFormat(
        (props.fileInfo as any).timeStamp,
        "yyyy年MM月dd日hh时"
      );
    });
    const createTime = computed(() => {
      return dateFormat(
        (props.fileInfo as any).createTime,
        "yyyy年MM月dd日hh时"
      );
    });
    const watchs = computed(() => {
      return (props.fileInfo as any).watch;
    });

    const showDes = () => {
      shdes.value = true;
      shsta.value = false;
    };
    const showSta = () => {
      shdes.value = false;
      shsta.value = true;
    };
    const download = computed(() => {
      return (props.fileInfo as any).download;
    });
    const tagList = computed(() => {
      return (props.fileInfo as any).tags;
    });

    const getColor = (text: string) => {
      return generateColorByText(text);
    };

    const toDetail = () => {
      context.emit("toDetail");
    };
    return {
      shdes,
      shsta,
      id,
      avatar,
      userAvatar,
      name,
      description,
      checkInDataBoo,
      timeStamp,
      createTime,
      watchs,
      download,
      tagList,
      getColor,
      toDetail,
      showDes,
      showSta,
      creator,
    };
  },
});
</script>

<style lang="scss" scoped>
/deep/.el-card__body {
  height: 25px;
  padding: 5px;
  font-size: 15px;
}
.data-card {
  margin-bottom: 5px;

  .top {
    display: flex;
    position: relative;
    height: 40px;
    line-height: 40px;
    .text {
      margin-left: 20px;
      margin-top: 5px;
      font-size: 25px;
      color: rgb(5, 59, 88);
    }
  }
  .des {
    margin-top: 8px;
    margin-left: 10px;
    font-size: 15px;
    display: flex;
    line-height: 30px;
    justify-content: center;
    //align-items: center;
  }
  .bottom {
    .bottom-top {
      display: flex;
      margin-top: 10px;
      .watch,
      .download {
        margin-left: 20px;
        position: relative;
        .el-icon {
          position: absolute;
          top: 2px;
        }
        span {
          margin-left: 20px;
        }
      }
    }
    .bottom-bottom {
      margin-top: 10px;
      .el-tag {
        margin-right: 10px;
      }
    }
  }
}

.block {
  margin-left: 5px;
  padding: 10px 0;
  text-align: center;
  border-right: solid 1px var(--el-border-color);
  display: inline-block;
  width: 25%;
  box-sizing: border-box;
  vertical-align: top;
}
.block:last-child {
  border-right: none;
}

.pos {
  height: 100%;
  .el-row {
    height: 100%;
    display: flex;
    flex-wrap: wrap;
    .el-col {
      height: 100%;
    }
  }
}
</style>