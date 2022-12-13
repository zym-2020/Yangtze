<template>
  <div>
    <div class="data-card">
      <div class="info">
        <div class="title">
          <div>
            <strong><span v-html="replaceHandle(name)"></span></strong>
          </div>
          <div class="hot">
            <el-tag type="danger" effect="dark" v-show="watchs >= 20">
              HOT
            </el-tag>
          </div>
        </div>
        <div class="des">
          <span v-html="replaceHandle(description)"></span>
        </div>
        <div class="creator">
          <span>创建者：</span>
          <span v-html="replaceHandle(userName)"></span>
        </div>
        <div class="time">
          <strong>上次更新于：</strong>
          {{ updateTime }}
        </div>
        <div class="watch-download">
          <el-icon style="margin-right: 5px"><View /></el-icon>
          <span>{{ watchs }}</span>
          <el-icon style="margin: 0 5px"><Download /></el-icon>
          <span>{{ download }}</span>
        </div>
        <div class="tag">
          <div class="tag-png" v-for="(item, index) in tagList" :key="index">
            {{ item }}
          </div>
        </div>
      </div>

      <div class="picture">
        <img :src="avatar" alt="" />
      </div>
    </div>
    <slot name="border"></slot>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
import { imgBase64, dateFormat } from "@/utils/common";

import { prefix } from "@/prefix";
export default defineComponent({
  props: {
    fileInfo: {
      type: Object,
    },
    keyword: {
      type: String,
    },
  },
  emits: ["toDetail"],
  setup(props, context) {
    const name = computed(() => {
      return (props.fileInfo as any).name;
    });
    const avatar = computed(() => {
      if (
        (props.fileInfo as any).avatar != "" &&
        (props.fileInfo as any).avatar != undefined &&
        (props.fileInfo as any).avatar != null
      ) {
        return prefix + "visual/getAvatar/" + (props.fileInfo as any).avatar;
      }

      return imgBase64(name.value);
    });

    const description = computed(() => {
      if ((props.fileInfo as any).description) {
        return (props.fileInfo as any).description;
      } else {
        return "该条目暂无简介";
      }
    });

    const updateTime = computed(() => {
      return dateFormat((props.fileInfo as any).updateTime, "yyyy-MM-dd hh:mm");
    });
    const watchs = computed(() => {
      return (props.fileInfo as any).watch;
    });

    const download = computed(() => {
      return (props.fileInfo as any).download;
    });
    const tagList = computed(() => {
      return (props.fileInfo as any).tags;
    });
    const userName = computed(() => {
      return (props.fileInfo as any).userName;
    });

    const toDetail = () => {
      context.emit("toDetail");
    };

    const replaceHandle = (currentStr: string) => {
      const res = new RegExp("(" + props.keyword + ")", "g");
      currentStr = currentStr.replace(
        res,
        "<span style='color:red;'>" + props.keyword + "</span>"
      );
      return currentStr;
    };

    return {
      avatar,
      name,
      description,
      updateTime,
      watchs,
      download,
      tagList,
      toDetail,
      replaceHandle,
      userName,
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
  // margin-bottom: 5px;
  width: 100%;
  position: relative;
  height: 300px;
  border-bottom: solid 1px #858689;

  .info {
    position: absolute;
    height: 250px;
    top: 25px;
    left: 20px;
    width: calc(100% - 460px);
    .title {
      // height: 30px;
      line-height: 30px;
      font-size: 23px;
      color: #0066cc;
      display: flex;
      max-width: calc(100% - 100px);
      .hot {
        display: flex;
        margin-left: 10px;
        margin-top: 5px;
        .el-tag {
          height: 20px;
        }
      }
    }
    .des {
      margin-top: 15px;
      font-size: 16px;
      line-height: 25px;
      text-indent: 4ch;
      display: -webkit-box;
      overflow: hidden;
      -webkit-line-clamp: 4;
      -webkit-box-orient: vertical;
    }
    .creator {
      position: absolute;
      bottom: 70px;
      left: 3px;
      font-size: 14px;
      color: #8c8c8c;
    }
    .time {
      position: absolute;
      bottom: 35px;
    }
    .watch-download {
      position: absolute;
      position: absolute;
      bottom: 35px;
      left: 275px;
    }
    .uploader {
      display: flex;
      position: absolute;
      bottom: 65px;
      .text {
        line-height: 30px;
        margin-left: 10px;
      }
    }
    .tag {
      position: absolute;
      bottom: 0px;
      display: flex;
      .tag-png {
        background: url("/png/label-blue-new.png");
        padding: 0 10px;
        background-size: 100% 100%;
        height: 30px;
        margin-right: 5px;
        color: white;
        text-align: center;
        // padding: 0 5px 0 10px;
        // background: #098ac3;
        // border-top-left-radius: 8px;
        // border-bottom-left-radius: 8px;
      }
      .triangle {
        width: 0;
        height: 0;
        border: 10px solid;
        border-color: transparent transparent transparent #098ac3;
      }
    }
  }
  .picture {
    position: absolute;
    right: 20px;
    top: 25px;
    height: 250px;
    width: 400px;
    img {
      height: 100%;
      width: 100%;
    }
  }
}
</style>