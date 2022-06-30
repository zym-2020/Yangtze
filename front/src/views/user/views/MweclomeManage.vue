<template>
  <div class="grid-container">
    <!-- <div class="item1" > -->
    <el-row class="item1">
      <el-col :span="6" :offset="9">
        <div ref="chart" class="chart" style="float: center"></div>
      </el-col>
    </el-row>
    <div class="item2">
      <h1 class="h1intro">admin ,您好！</h1>
      <h1 class="h1intro">
        当您收到这份通知，说明您已经注册了该系统的会员，我们确保您能在平台开启一段美妙的数据探索之旅！
      </h1>
      <h1 class="h1intro">泛在数据共享平台着力于数据共享与水利数据可视化。</h1>
      <h1 class="h1intro">
        所以您需要做的第一件事可以是上传您自己的数据，抑或是在数据共享平台下载数据。
      </h1>
      <!-- <div style="width: 900px; margin-left: 350px">
        <el-carousel indicator-position="outside" type="card" >
          <el-carousel-item v-for="url in urls" :key="url">
            <div class="demo-image__lazy">
              <el-image :src="url" lazy />
            </div>
          </el-carousel-item>
        </el-carousel>
      </div> -->

      <h1 class="h1intro">如果您是新用户，那么下面提供一些使用该平台的建议</h1>
      <ul>
        <li>
          当上传数据至平台共享时，请考虑到数据的完备性，所有数据需要经过管理员审核，审核成功后可上传至共享平台
        </li>
        <li>
          当从共享平台下载数据时，请考虑到数据的完备性，下载数据无需要经过管理员审核，可直接进行下载
        </li>
        <li>
          上传数据时，请考虑到添加数据描述元文件，当下载的数据具有质量问题时，请及时与管理员联络
        </li>
      </ul>
      <h1 class="h1intro">
        同时，请及时关注消息中心与邮箱，我们将发送一些必要的通知。
      </h1>
      <hr style="border-color: #d8d8d8; margin: 10px" />

      <h1 class="h1intro">
        现在您可以尽情的探索系统内的功能并肆意的上传与下载数据！在一张图中领略数据之美。
      </h1>

      <h1 class="h1intro">---泛在数据共享平台开发团队</h1>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { imgBase64 } from "@/utils/common";
import * as echarts from "echarts";
export default defineComponent({
  setup() {
    const datatype = ref("地质类型");
    const avatar = ref("");
    const supportTime = ref("2022-5-18");
    const checkTime = ref("2022-5-19");
    const n = ref(1);
    const name = ref("冲淤数据");
    const chart = ref<HTMLElement>();
    const chartInit = () => {
      const option = {
        graphic: {
          elements: [
            {
              type: "text",
              left: "center",
              top: "center",
              style: {
                text: "NHRI",
                fontSize: 80,
                fontWeight: "bold",

                lineDash: [0, 200],
                lineDashOffset: 0,
                fill: "transparent",
                stroke: "#000",
                lineWidth: 1,
              },
              keyframeAnimation: {
                duration: 3000,
                loop: true,
                keyframes: [
                  {
                    percent: 0.7,
                    style: {
                      fill: "transparent",
                      lineDashOffset: 200,
                      lineDash: [200, 0],
                    },
                  },
                  {
                    // Stop for a while.
                    percent: 0.8,
                    style: {
                      fill: "transparent",
                    },
                  },
                  {
                    percent: 1,
                    style: {
                      fill: "#66CCFF",
                    },
                  },
                ],
              },
            },
          ],
        },
      };
      const myChart = echarts.init(chart.value as HTMLElement, undefined, {
        height: 180,
        width: 250,
      });
      myChart.setOption(option);
    };
    const urls = [
      "https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg",
      "https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg",
      "https://fuss10.elemecdn.com/0/6f/e35ff375812e6b0020b6b4e8f9583jpeg.jpeg",
      "https://fuss10.elemecdn.com/9/bb/e27858e973f5d7d3904835f46abbdjpeg.jpeg",
      "https://fuss10.elemecdn.com/d/e6/c4d93a3805b3ce3f323f7974e6f78jpeg.jpeg",
      "https://fuss10.elemecdn.com/3/28/bbf893f792f03a54408b3b7a7ebf0jpeg.jpeg",
      "https://fuss10.elemecdn.com/2/11/6535bcfb26e4c79b48ddde44f4b6fjpeg.jpeg",
    ];

    onMounted(() => {
      avatar.value = imgBase64("哈哈");
      chartInit();
    });
    // function showinfo(){
    //        showinfos=false
    //        return showinfos
    // }
    return {
      avatar,
      datatype,
      supportTime,
      checkTime,
      n,
      name,
      urls,
      chart,

      chartInit,
      // showinfos,
      // showinfo
    };
  },
});
</script>

<style lang="scss" scoped>
li {
  margin: 10px;
  font-style: italic;
  font-weight: bold;
}
.item1 {
  grid-area: header;
}
.item2 {
  grid-area: footer;
}
// .item3 { grid-area: main; }
// .item4 { grid-area: right; }
// .item5 { grid-area: footer; }
.h1intro {
  font-family: Microsoft YaHei;
  color: rgba(248, 242, 239, 0.918);
}
.grid-container {
  display: grid;
  background: url("E:/水科院/Yangtze/front/src/assets/background.jpg") 0 0/ 100%
    100% no-repeat;
  background-size: cover;
  position: relative;
  grid-template-areas:
    "header header header"
    "header header header"
    "footer footer footer"
    "footer footer footer"
    "footer footer footer"
    "footer footer footer";
  grid-gap: 10px;
  //background-color: #2196F3;
  padding: 10px;
}

.demo-image__lazy {
  height: 400px;
  overflow-y: auto;
}
.demo-image__lazy .el-image {
  display: block;
  min-height: 200px;
  margin-bottom: 10px;
}
.demo-image__lazy .el-image:last-child {
  margin-bottom: 0;
}
</style>