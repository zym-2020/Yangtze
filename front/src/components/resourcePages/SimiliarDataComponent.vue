<template >
  <div id="box">
    <div id="search">
      <el-image style="width: 100%; height: 100%" :src="avatar" fit="fit" />
    </div>
    <div>
      <el-row>
        <div id="data">{{ name }}</div>
        <div id="free">免费</div>
      </el-row>
    </div>
    <div id="clock">
      <div id="text">发布: {{ provider }}</div>
      <div id="detail">
        <el-button id="button1" @click="toDetail()">访问数据</el-button>
      </div>
    </div>
    <div id="data-size">
      <div>数据时间: {{ timeStamp }}</div>
    </div>
    <div id="data-tag">
      <div style="display: inline">数据标签:</div>
      <div
        v-for="(item, index) in tagList"
        :key="index"
        style="display: inline"
      >
        {{ item }},
      </div>
    </div>
  </div>
</template>
  
  <script lang="ts">
import { defineComponent, computed } from "vue";
import { dateFormat } from "@/utils/common";
import router from "@/router";
import { prefix } from '@/prefix'
export default defineComponent({
  props: {
    similarInfo: Object,
  },
  setup(props) {
    const name = computed(() => {
      return (props.similarInfo as any).name;
    });
    const provider = computed(() => {
      return (props.similarInfo as any).provider;
    });
    const tagList = computed(() => {
      return (props.similarInfo as any).tags;
    });
    const timeStamp = computed(() => {
      return dateFormat(
        (props.similarInfo as any).time_stamp,
        "yyyy年MM月dd日hh时"
      );
    });
    const avatar = computed(() => {
      if ((props.similarInfo as any).avatar != "") {
        return (
          prefix + "visual/getAvatar/" +
          (props.similarInfo as any).avatar
        );
      } else {
        return "https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg";
      }
    });
    const toDetail = () => {
      router.push({
        name: "shareFile",
        params: {
          id: props.similarInfo?.id,
          fileInfo: JSON.stringify(props.similarInfo),
        },
      });
    };
    return {
      name,
      provider,
      tagList,
      toDetail,
      timeStamp,
      avatar,
    };
  },
});
</script>
  
  <style lang="scss" scoped>
div#box {
  width: 410px;
  height: 364px;
  top: 50px;
  left: 20px;
  //background-color: rgba(33, 37, 41, 0.6);
  background-color: white;
  position: absolute;
  z-index: 20;
  color: white;
  div#search {
    top: 10px;
    position: absolute;
    width: 390px;
    height: 220px;
    left: 10px;
    text-align: center;
    display: inline;
    //background-color: green;
  }

  div#data {
    position: relative;
    top: 235px;
    left: 20px;
    text-align: center;
    font-weight: bolder;
    font-size: 19px;
    color: black;
  }
  div#free {
    position: absolute;
    top: 235px;
    left: 360px;
    text-align: center;

    //font-weight:bolder;
    color: rgb(219, 133, 21);
  }
  div#clock {
    position: relative;
    top: 245px;
    left: 20px;
    //text-align: center;
    //font-weight:bolder;
    font-size: 8px;
    div#text {
      text-align: left;
      display: inline;
      left: 20px;
      color: #707070;
    }
    div#detail {
      position: relative;
      left: 150px;
      display: inline;
      /deep/.el-button {
        width: 50px;
        height: 20px;
        > span {
          font-size: 4px;
        }
      }
    }
  }
  div#data-size {
    position: relative;
    top: 250px;
    left: 20px;
    font-size: 8px;
    color: #707070;
  }
  div#data-tag {
    position: relative;
    top: 260px;
    left: 20px;
    font-size: 8px;
    color: #707070;
  }
}
</style>