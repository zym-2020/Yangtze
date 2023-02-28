<template #aa>
  <el-carousel
    height="93vh"
    direction="vertical"
    :autoplay="false"
    ref="carousel"
    trigger="click"
  >
    <el-carousel-item id="front-page">
      <div id="earth-part"></div>
      <div id="title">
        交通运输行业野外科学观测研究基地<br />深水航道水沙环境与工程安全平台
      </div>
      <div id="outline">
        基于南科院在长江黄金水道长期的科研与业务积累，本平台针对丰富的河道历史地形资料、水文测验数据以及物理模型和数值分析模型等建立统一数据标准并进行管理与共享。
        在数据基础上进一步结合地理信息系统，本平台提供包括水文分析、河床演变分析等一系列综合时空分析功能，也是集成了实时监控、数学模型、物理模型的一体化辅助实验平台。
      </div>
      <el-row id="links" justify="space-around">
        <el-col :span="6">
          <el-dropdown>
            <el-button
              type="primary"
              class="dropdown-button data"
              id="data"
              color="rgba(219, 219, 219, 0.2)"
              :dark="false"
            >
              资源门户<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item> 特色数据 1 </el-dropdown-item>
                <el-dropdown-item>特色数据 2</el-dropdown-item>
                <el-dropdown-item>特色数据 3</el-dropdown-item>
                <el-dropdown-item divided @click="RouteTo('/data/list')"
                  >探索更多数据</el-dropdown-item
                >
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-col>
        <el-col :span="6">
          <el-dropdown>
            <el-button
              type="primary"
              class="dropdown-button amap"
              id="amap"
              color="rgba(219, 219, 219, 0.2)"
              :dark="false"
            >
              一张图<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item> 工程方案一张图 </el-dropdown-item>
                <el-dropdown-item>流场可视化</el-dropdown-item>
                <el-dropdown-item>长江船舶</el-dropdown-item>
                <el-dropdown-item divided>更多一张图</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-col>
        <el-col :span="6">
          <el-dropdown>
            <el-button
              type="primary"
              class="dropdown-button analysis"
              id="analysis"
              color="rgba(219, 219, 219, 0.2)"
              :dark="false"
            >
              分析中心<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>进入分析中心</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-col>
      </el-row>
      <BottomInfo />
      <div id="usage-data">
        <el-divider content-position="center">数据<br />条目数量</el-divider>
        <span>112<br /></span>
        <el-divider content-position="center">一张图<br />浏览次数</el-divider>
        <span>84<br /></span>
        <el-divider content-position="center">分析<br />执行次数</el-divider>
        <span>61<br /></span>
      </div>
    </el-carousel-item>
    <el-carousel-item>
      <PartOutline partID="0" />
      <BottomInfo />
      <el-row id="special-data" justify="space-around">
        <el-col :span="7" id="data-one">
          <DataCard cardID="0" />
        </el-col>
        <el-col :span="7" id="data-two">
          <DataCard cardID="1" />
        </el-col>
        <el-col :span="7" id="data-three">
          <DataCard cardID="2" />
        </el-col>
      </el-row>
    </el-carousel-item>
    <el-carousel-item>
      <PartOutline partID="1" />
      <BottomInfo />
      <el-row id="special-data" justify="space-around">
        <el-col :span="7" id="data-one">
          <DataCard cardID="3" />
        </el-col>
        <el-col :span="7" id="data-two">
          <DataCard cardID="4" />
        </el-col>
        <el-col :span="7" id="data-three">
          <DataCard cardID="5" />
        </el-col>
      </el-row>
    </el-carousel-item>
    <el-carousel-item>
      <PartOutline partID="2" />
      <BottomInfo />
      <el-row id="special-data" justify="space-around">
        <el-col :span="7" id="data-one">
          <DataCard cardID="6" />
        </el-col>
        <el-col :span="7" id="data-two">
          <DataCard cardID="7" />
        </el-col>
        <el-col :span="7" id="data-three">
          <DataCard cardID="8" />
        </el-col>
      </el-row>
    </el-carousel-item>
  </el-carousel>
</template>

<script lang="ts">
import DropDown from "../pagePart/dropdown.vue";
import BottomInfo from "../pagePart/bottomInfo.vue";
import DataCard from "../pagePart/dataCard.vue";
import PartOutline from "../pagePart/partOutline.vue";
export default {
  name: "HomePage",
  components: {
    DropDown,
    BottomInfo,
    DataCard,
    PartOutline,
  },
};
</script>

<script lang="ts" setup>
import { onActivated, onDeactivated, onMounted, ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

function RouteTo(addURL: string) {
  router.push(addURL);
}

const carousel = ref();

const scrollEvent = (delta: number) => {
  if (delta > 0) {
    carousel.value.next();
  } else {
    carousel.value.prev();
  }
};

//统一处理滚轮滚动事件
var throldHold = 200; //规定时间
var flag = true; //规定时间内是否可以触发
var timer: NodeJS.Timeout;
function wheel(e: any) {
  var delta = e.deltaY;

  if (flag && delta) {
    flag = false; //将开关设置为false，false期间内不能触发事件
    scrollEvent(delta); //首次可以触发
    timer = setTimeout(function () {
      clearInterval(timer);
      flag = true; //规定时间到大后，将开关设置为true
    }, throldHold);
  }
}

const handleWheel = (e: any) => {
  wheel(e);
};

onDeactivated(() => {
  window.removeEventListener("wheel", handleWheel);
});

onActivated(() => {
  window.addEventListener("wheel", handleWheel);
});

// onMounted(() => {
//   console.log(1);
//   // window.onwheel = (e) => {
//   //   wheel(e);
//   // };
//   window.addEventListener("wheel", handleWheel);
// });
</script>

<style lang="scss" scoped>
.el-carousel {
  margin-top: 0vh;

  .el-carousel__item h3 {
    color: #475669;
    opacity: 0.75;
    line-height: 93vh;
    line-height: 100%;
    margin: 0;
    text-align: center;
  }

  .el-carousel__item:first-child {
    background: radial-gradient(
        circle at 14% 96%,
        rgba(0, 29, 158, 0.5) 0%,
        rgba(0, 6, 75, 0.9) 36%,
        rgba(0, 20, 85, 0.9) 100%
      ),
      url("../../assets/homePage/ocean-bg.jpg");
    // background: url("../../assets/homePage/ocean-bg.jpg");

    div#earth-part {
      position: absolute;
      left: 0px;
      bottom: 0px;
      width: 675px;
      height: 443px;
      background-image: url("../../assets/homePage/earth_part_new.png");
      background-size: contain;
      z-index: 4;
    }

    div#title {
      position: absolute;
      // width: 48vw;
      height: 20vh;
      top: 10vh;
      right: 8vw;
      line-height: 10vh;
      background-image: linear-gradient(
        30deg,
        #ffffff 0%,
        #d1f0ff 40%,
        #39d6fd 100%
      );
      -webkit-background-clip: text;
      background-clip: text;
      -webkit-text-fill-color: transparent;
      -webkit-animation: hue 60s infinite linear;
      animation: hue 60s infinite linear;
      word-wrap: break-word;
      font-size: 6vh;
      text-align: right;
      color: white;
      font-weight: 600;
      font-family: "Microsoft YaHei";
    }

    div#outline {
      position: absolute;
      font-size: 3vh;
      width: 40vw;
      height: 40vh;
      right: 8vw;
      top: 36vh;
      color: rgb(219, 219, 219);
      font-weight: 600;
      font-family: "Microsoft YaHei";
    }

    div#usage-data {
      width: 6vw;
      height: 28vh;
      position: absolute;
      top: 0vh;
      left: 0vw;
      text-align: center;

      span {
        color: white;
        font-weight: 600;
        line-height: 40px;
        font-size: 18px;

        &:hover {
          background-image: linear-gradient(
            45deg,
            #c2ebff 0%,
            #99ddff 40%,
            #39d6fd 100%
          );
          -webkit-background-clip: text;
          background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;
          animation: hue 60s infinite linear;
          cursor: pointer;
        }
      }

      .el-divider {
        margin-top: 40px;
        border-width: 4px;
        border-color: #3576d8;
        cursor: default;
      }

      /deep/.el-divider__text {
        padding-left: 0px;
        padding-right: 0px;
        text-align: center;
        background-color: rgb(4, 12, 75);
        font-weight: 600;
        font-size: 14px;
        color: #e6e6e6;
        font-family: "Microsoft YaHei";
        cursor: default;
      }
    }
  }

  .el-carousel__item:nth-child(2) {
    background: linear-gradient(rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0.25)),
      url("../../assets/homePage/bg-mapbox.png");
    background-size: cover;

    .el-row#special-data {
      width: 72vw;
      height: 64vh;
      position: absolute;
      top: 14vh;
      left: 0vw;
      // background-color: #012063;

      .el-col {
        // background-color: #39d6fd;
        border-radius: 5px;
        border-color: #39d6fd;
      }
    }
  }

  .el-carousel__item:nth-child(3) {
    background: linear-gradient(rgba(0, 0, 0, 0.9), rgba(0, 0, 0, 0.45)),
      url("../../assets/homePage/amap-page.png");
    background-size: cover;

    .el-row#special-data {
      width: 72vw;
      height: 64vh;
      position: absolute;
      top: 14vh;
      left: 0vw;
      // background-color: #012063;

      .el-col {
        // background-color: #39d6fd;
        border-radius: 5px;
        border-color: #39d6fd;
      }
    }
  }

  .el-carousel__item:nth-child(4) {
    background: linear-gradient(rgba(0, 0, 0, 0.25), rgba(0, 0, 0, 0.05)),
      url("../../assets/homePage/abstract-flowing.png");
    background-size: cover;

    .el-row#special-data {
      width: 72vw;
      height: 64vh;
      position: absolute;
      top: 14vh;
      left: 0vw;
      // background-color: #012063;

      .el-col {
        // background-color: #39d6fd;
        border-radius: 5px;
        border-color: #39d6fd;
      }
    }
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
  }

  /deep/.el-carousel__button {
    width: 8px;
  }

  #links {
    position: absolute;
    top: 66vh;
    right: 8vw;
    height: 6vh;
    width: 40vw;
    // background-color: #153ab3;
  }
}

.dropdown-button {
  height: 6vh;
  width: 10vw;
  font-size: 3vh;
  font-weight: 600;
  color: rgb(219, 219, 219);
  background-image: linear-gradient(
    45deg,
    #ffffff 0%,
    #9edcfc 40%,
    #39d6fd 100%
  );
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  -webkit-animation: hue 60s infinite linear;
  animation: hue 60s infinite linear;

  &.data {
    background-image: linear-gradient(
      30deg,
      #fffef0 0%,
      #9edcfc 10%,
      #fff352 100%
    );
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-color: transparent;
    -webkit-animation: hue 60s infinite linear;
    animation: hue 60s infinite linear;
  }

  &.amap {
    background-image: linear-gradient(
      45deg,
      #fffef0 0%,
      #9edcfc 10%,
      #52ff52 100%
    );
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-color: transparent;
    -webkit-animation: hue 60s infinite linear;
    animation: hue 60s infinite linear;
  }

  &.analysis {
    background-image: linear-gradient(
      45deg,
      #fffef0 0%,
      #9edcfc 10%,
      #ff9252 100%
    );
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-color: transparent;
    -webkit-animation: hue 60s infinite linear;
    animation: hue 60s infinite linear;
  }
}

ul.el-dropdown-menu {
  background-image: linear-gradient(
    30deg,
    #6ea9f5 0%,
    #2662bb 40%,
    #012063 100%
  );
  font-size: 2vh;
  width: 10vw;
  border-color: #39d6fd;

  /deep/.el-dropdown-menu__item {
    font-size: 2vh;
    font-family: "Microsoft YaHei";
    color: rgb(224, 253, 255);
    font-weight: 600;
  }

  /deep/.el-dropdown-menu__item:focus {
    background-color: rgb(7, 18, 80);
    color: #f3f3f3;
  }
}
</style>