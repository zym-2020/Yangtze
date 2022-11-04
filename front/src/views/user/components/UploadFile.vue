<template>
  <div class="upload-file">
    <div class="left">
      <div class="upload">
        <div class="cloud">
          <svg>
            <use xlink:href="#icon-cloudupload-fill"></use>
          </svg>
        </div>
        <div class="text">上传文件</div>
        <div class="btn">
          <el-button type="warning">浏览</el-button>
        </div>
      </div>
    </div>
    <div class="right">
      <div class="animation">
        <img src="/gif/upload.gif" alt="" />
      </div>
      <div class="card"></div>
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
.upload-file {
  height: 100%;
  padding: 0 15vw;
  display: flex;
  background: #ffffff;
  .left {
    width: 35%;
    padding-top: 10%;
    .upload {
      box-sizing: border-box;
      width: calc(100% - 10px);
      height: 80%;
      border: 1px dashed #000000;
      border-radius: 20px;
      .cloud {
        margin: 15% auto 0px;
        width: 30%;
        svg {
          width: 100%;
        }
      }
      .text {
        margin: 12% auto 0px;
        width: 35%;
        height: 58px;
        font-family: "Inter";
        font-style: normal;
        font-weight: 400;
        font-size: 24px;
        line-height: 29px;
        text-align: center;

        color: #000000;
      }
      .btn {
        text-align: center;
        margin-top: 20%;
        .el-button {
          height: 17%;
          width: 35%;
          box-shadow: 0px 4px 4px #cc7500;
          font-family: "Inter";
          font-style: normal;
          font-weight: 500;
          font-size: 20px;
          color: #000000;
        }
      }
    }
  }
  .right {
    width: 70%;
    .animation {
      width: 50%;
      margin: 0 auto;
      img {
        width: 100%;
      }
    }
    .card {
      height: 40%;
      padding: 0 30px;
    }
  }
}
</style>