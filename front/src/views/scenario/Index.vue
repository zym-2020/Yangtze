<template >
  <div class="data-similiar">
    <div class="bg"></div>
    <div class="main">
      <el-carousel :interval="5000" type="card" :autoplay="false">
        <el-carousel-item v-for="(item, index) in dataList" :key="index">
          <div class="card">
            <div class="image">
              <img :src="item.avatar" />
            </div>
            <div class="name">
              {{ item.name }}
            </div>
            <div class="des">
              {{ item.des }}
            </div>
            <div class="btn">
              <el-button
                type="primary"
                size="large"
                @click="clickHandle(item.path)"
                >查看详情</el-button
              >
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
    <page-copyright class="bottom" />
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import PageCopyright from "@/components/page/PageCopyright.vue";
import router from "@/router";
export default defineComponent({
  components: { PageCopyright },
  setup() {
    const dataList = [
      {
        path: "/scenario/scenarioOne",
        name: "潮位实时监测一张图",
        des: "对长江区域潮位站进行实时监测",
        avatar: "./tide2.png",
      },
      {
        path: "/scenario/scenarioTwo",
        name: "区域流场一张图",
        des: "对长江区域流场进行可视化",
        avatar: "./flow.png",
      },
      {
        path: "/scenario/scenarioThree",
        name: "AIS一张图",
        des: "展示长江区域航道现况",
        avatar: "./ship.png",
      },
      {
        path: "/scenario/scenarioFour",
        name: "工程方案一张图",
        des: "对长江区域已有工程以及现有方案进行描述",
        avatar: "./project.png",
      },
    ];

    const clickHandle = (path: string) => {
      router.push({
        path: path,
      });
    };

    return {
      dataList,
      clickHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
@keyframes ibannerbg {
  50% {
    transform: scale(1.2, 1.2);
  }
  100% {
    transform: scale(1, 1);
  }
}
.data-similiar {
  width: 100%;
  overflow: hidden;
  position: relative;
  height: calc(100% + 250px);
  .bg {
    height: calc(100% - 250px);
    background: url("/resource/analyse3.png");
    background-size: cover;
    animation: ibannerbg 30s linear infinite;
  }

  .main {
    width: 100%;
    height: 60%;
    position: absolute;
    top: calc(50% - 500px);

    .el-carousel {
      height: 100%;
      /deep/ .el-carousel__container {
        height: calc(100% - 30px);
      }

      // /deep/ .el-carousel__indicators {
      //   opacity: 0;
      // }

      .el-carousel__item {
        border-radius: 6px;
        .card {
          margin: 20px;

          width: calc(100% - 40px);
          height: calc(100% - 40px);
          .image {
            width: 100%;
            height: 80%;
            img {
              width: 100%;
              height: 100%;
            }
          }

          .name {
            font-size: 30px;
            font-weight: 800;
            margin-top: 20px;
          }
          .des {
            font-size: 20px;
            line-height: 50px;
            color: #8c8c8c;
            font-weight: 500;
          }
          .btn {
            position: absolute;
            bottom: 30px;
            right: 20px;
          }
        }
      }

      .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
      }

      .el-carousel__item:nth-child(2n + 1) {
        background-color: #d3dce6;
      }
    }
  }
  .bottom {
    position: absolute;
    bottom: 0px;
  }
}
</style>