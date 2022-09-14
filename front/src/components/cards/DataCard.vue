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
          <el-col :span="18">
            <div class="top">
              <div class="text">{{ name }}</div>
              <slot name="creator"></slot>
              <slot name="status"></slot>
              <div v-show="watchs >= 20">
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
          <!-- <el-col :span="4" style="margin-top: 10px">
        <el-button @click="getDataSet()" style="margin-right: 20px">{{
          checkInData
        }}</el-button>
      </el-col> -->
          <el-col :span="2" style="margin-top: 10px">
            <div style="text-align: right">
              <el-tooltip content="公开" placement="left-start" effect="light">
                <el-icon :size="25" color="#00CD00"><Unlock /></el-icon>
              </el-tooltip>
            </div>
          </el-col>
        </el-row>
        <div v-show="shdes">
          <el-row style="margin-top: 10px">
            <el-col :span="5">
              <div class="block">
                <el-image
                  style="width: 180px; height: 160px"
                  :src="avatar"
                  fit="fill"
                />
              </div>
            </el-col>
            <el-col :span="19">
              <el-row>
                <div class="des">
                  {{ description }}
                </div>
              </el-row>
            </el-col>
          </el-row>
          <el-row>
            <el-col :offset="5">
              <div>
                <el-tag
                  v-for="(item, index) in tagList"
                  :key="index"
                  style="
                    margin-right: 8px;
                    background-color: rgba(21, 69, 153, 0.8);
                    color: #e5cab9;
                  "
                  effect="dark"
                  round
                >
                  {{ item }}
                </el-tag>
              </div>
            </el-col>
          </el-row>
        </div>
        <div v-show="shsta" style="width: 100%; height: 80%">
          <DownLoadSta2 :id="id"></DownLoadSta2>
        </div>
      </div>
      <el-divider style="color: #00bfff">
        <el-icon><star-filled /></el-icon>
      </el-divider>
      <el-row>
        <el-col :span="1">
          <div>
            <el-avatar :size="35" :src="userAvatar"></el-avatar>
          </div>
        </el-col>
        <el-col :span="4">
          <div style="margin-left: 10px; margin-top: 6px; font-size: 8px">
            <span style="color: #6495ed">{{ creator }}</span>
          </div>
        </el-col>
        <el-col :span="5">
          <div
            style="
              margin-left: 10px;
              margin-top: 6px;
              font-size: 8px;
              text-align: left;
            "
          >
            <strong>上传于：</strong>
            {{ updateTime }}
          </div>
        </el-col>
        <el-col :span="11" style="text-align: right">
          <el-button type="primary" plain @click="showDes">数据介绍</el-button>
          <el-button type="primary" plain @click="showSta">数据统计</el-button>
        </el-col>
        <el-col :span="2">
          <div style="margin-left: 10px; margin-top: 6px; text-align: right">
            <el-icon style="margin-right: 5px"><View /></el-icon>
            <span>{{ watchs }}</span>
          </div>
        </el-col>
        <el-col :span="1" style="margin-top: 6px; text-align: right">
          <el-icon style="margin-right: 5px"><Download /></el-icon>
          <span>{{ download }}</span>
        </el-col>
      </el-row>
    </div>
    <hr style="border-color: #d8d8d8" />
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref } from "vue";
import { imgBase64, generateColorByText, dateFormat } from "@/utils/common";
import BrowseStatistics from "../resourcePages/components/BrowseStatistics.vue";
import DownLoadSta2 from "../resourcePages/components/DownSta2.vue";

export default defineComponent({
  components: { BrowseStatistics, DownLoadSta2 },
  props: {
    fileInfo: {
      type: Object,
    },
  },
  emits: ["toDetail"],
  setup(props, context) {
    const shdes = ref(true);
    const shsta = ref(false);
    // const checkInData = ref("选中至资源集合");
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
          "http://localhost:8002/visual/getAvatar/" +
          (props.fileInfo as any).avatar
        );
      }
      return imgBase64(name.value);
    });
    const userAvatar = computed(() => {
      if ((props.fileInfo as any).avatar != "") {
        return (
          "http://localhost:8002/visual/getAvatar/" +
          (props.fileInfo as any).avatar
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
    const updateTime = computed(() => {
      return dateFormat(
        (props.fileInfo as any).updateTime,
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

    // const fits = ["fill", "contain", "cover", "none", "scale-down"];
    // const url =
    //   "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg";
    //监听父组件中的数组表格，注意对于数组要深度监听
    // watch(
    //   () => props.dataSelect,
    //   (newValue, oldValue) => {
    //     for (let i = 0; i < (props.dataSelect as any)?.length; i++) {
    //       //如果发现表格中包含该条目，则将该条目的背景色调为浅蓝色，证明已经选中
    //       if (
    //         (props.dataSelect as any[])[i].name == (props.fileInfo as any).name
    //       ) {
    //         checkInDataBoo.value = true;
    //         //break跳出了for循环，证明该条目已经在表格中
    //         break;
    //       } else {
    //         checkInDataBoo.value = false;
    //       }
    //     }
    //   },
    //   { immediate: true, deep: true }
    // );

    return {
      shdes,
      shsta,
      // checkInData,
      id,
      avatar,
      userAvatar,
      name,
      description,
      checkInDataBoo,
      updateTime,
      watchs,
      download,
      tagList,
      getColor,
      toDetail,
      showDes,
      showSta,
      // fits,
      // url,
      creator,
    };
  },
});
</script>

<style lang="scss" scoped>
.data-card {
  margin-bottom: 5px;

  .top {
    display: flex;
    position: relative;
    height: 40px;
    line-height: 40px;
    .text {
      //margin-left: 10px;
      font-size: 22px;
      color: #4fb5ea;
    }
  }
  .des {
    margin-top: 8px;
    font-size: 14px;
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
  padding: 10px 0;
  text-align: center;
  border-right: solid 1px var(--el-border-color);
  display: inline-block;
  width: 20%;
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