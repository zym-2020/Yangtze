<template>
  <div class="page-copyrught">
    <div class="top">
      <el-row>
        <el-col :span="8">
          <div><strong>支撑服务</strong></div>
          <div class="border"></div>
          <div class="text">资源门户</div>
          <div class="text">一张图</div>
          <div class="text">分析中心</div>
        </el-col>
        <el-col :span="8">
          <div><strong>联系我们</strong></div>
          <div class="border"></div>
          <div class="text">地址：南京市广州路223号</div>
          <div class="text">南科院联系电话：025-85828808</div>
          <div class="text">邮箱：webmaster@nhri.cn</div>
          <div class="text">邮编：210029</div>
        </el-col>
        <el-col :span="8">
          <div><strong>关于我们</strong></div>
          <div class="border"></div>
          <div class="text">南京水利科学研究院</div>
          <img src="/resource/wx_code.jpg" alt="" />
        </el-col>
      </el-row>
      <div class="dividing-line one"></div>
      <div class="dividing-line two"></div>
    </div>

    <div class="bottom">
      ©水利部交通运输部国家能源局南京水利科学研究院&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;Copyright ©2022&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;苏ICP备05007122号
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
export default defineComponent({
  setup() {},
});
</script>

<style lang="scss" scoped>
.page-copyrught {
  height: 250px;
  background: #424242;
  width: 100%;
  position: relative;
  color: white;
  font-family: "Microsoft YaHei";
  .top {
    width: 800px;
    position: absolute;
    top: 30px;
    left: calc(50% - 400px);
    .el-col {
      text-align: center;
      .border {
        width: 25px;
        border-top: solid 3px #3a8ed3;
        margin-top: 18px;
        margin-left: calc(((800px / 3) - 25px) / 2);
        margin-bottom: 18px;
      }
      .text {
        height: 28px;
        line-height: 28px;
        font-size: 12px;
      }
      img {
        width: 61px;
        height: 72px;
      }
    }
    .dividing-line {
      position: absolute;
      top: 5px;
      height: 150px;
      width: 1px;
      border-right: solid 1px #cccccc;
    }
    .one {
      left: calc(800px / 3);
    }
    .two {
      left: calc(800px / 3 * 2);
    }
  }
  .bottom {
    width: 800px;
    text-align: center;
    position: absolute;
    left: calc(50% - 400px);
    top: 210px;
  }
}
</style>