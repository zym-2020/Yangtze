<template>
    <el-card id="data-one-card" shadow="hover">
        <div class="data-card-pic" :id="cardData[parseInt(cardID)].id + '-pic'"></div>
        <h2 class="data-card-title" :id="cardData[parseInt(cardID)].id + '-title'">{{ cardData[cardID].title }}</h2>
        <div class="data-card-content" :id="cardData[parseInt(cardID)].id + '-content'">{{ cardData[cardID].des }}</div>
        <el-button type="primary" class="data-card-button" :id="cardData[parseInt(cardID)].id + '-button'" :color="cardData[parseInt(cardID)].butColor">{{ cardData[parseInt(cardID)].butText }}</el-button>
    </el-card>
</template>

<script lang="ts">
export default {
    name: 'DataCard'
}
</script>

<script setup lang="ts">
import { ref } from 'vue';
defineProps({
  cardID: String
}); 

const cardData = ref(
    [
        {id:"data-one", title: "长江黄金水道矢量数据", des: "包括长江黄金水道等高线、等深线、深泓线等矢量数据。", butColor:"#ffef94de", butText:"查看数据"}, 
        {id:"data-two", title: "长江浓度场栅格时空数据", des: "包括不同时期的长江浓度场、遥感影像、冲於等栅格数据。", butColor:"#ffef94de", butText:"查看数据"}, 
        {id:"data-three", title: "三沙各垂线流速流向含沙量", des: "包括长江各段含沙量、潮位站潮位、悬移质颗粒等表格数据。", butColor:"#ffef94de", butText:"查看数据"},
        {id:"data-four", title: "工程方案一张图", des: "可交互的各工程及方案地图可视化及具体信息总览。", butColor:"#94ffa2de", butText:"进入查看"},
        {id:"data-five", title: "区域流场一张图", des: "基于可交互地图、应用GPU粒子模拟技术可视化的区域流场。", butColor:"#94ffa2de", butText:"进入查看"},
        {id:"data-six", title: "潮位实时监测一张图", des: "实时监测各潮位站数据，进行地图及图表可视化。", butColor:"#94ffa2de", butText:"进入查看"},
        {id:"data-seven", title: "冲淤等深线分析", des: "针对整个长江南京以下河段，任意区域冲於参数获得冲於等深线。", butColor:"#ffae61d2", butText:"进行分析"},
        {id:"data-eight", title: "河床坡度提取分析", des: "对于整个河段进行河床坡度范围设置，计算提取河床坡度。", butColor:"#ffae61d2", butText:"进行分析"},
        {id:"data-nine", title: "特定高程冲淤", des: "针对整个长江南京以下河段，设置特定高程域冲於参数生成冲於效果图。", butColor:"#ffae61d2", butText:"进行分析"},
    ]
);

</script>

<style lang="scss" scoped>
.el-card {
    height: 100%;
    background-color: #ffffffad;
    backdrop-filter: blur(3px);
    border-width: 0px;
    font-family: 'Microsoft YaHei';
    // text-align: center;

    /deep/.el-card__body {
        height: 100%;
        padding: 8px;

        h2.data-card-title {
            text-align: center !important;
            // font-size: 2vh;
            color: #000000;
            transition-duration: 0.5s;
        }
    }

    &:hover {
        background-color: #5bbcfdb2;

        h2.data-card-title {
            color: #d1f0ff;
            font-size: 3vh;
            transition-duration: 0.3s;
        }

        div.data-card-content {
            color: #bfeaff;
            font-weight: 600;
        }

        .data-card-button {
            border-width: 4px;
            font-weight: 800;
            &#data-one-button, &#data-two-button, &#data-three-button {
                background-image: linear-gradient(30deg, #fff894 0%, #ffff58 40%, #ffef14 100%);
            }
            &#data-four-button, &#data-five-button, &#data-six-button {
                background-image: linear-gradient(30deg, #fffcd2 0%, #9eff9e 40%, #17ff17 100%);
            }
            &#data-seven-button, &#data-eight-button, &#data-nine-button {
                background-image: linear-gradient(30deg, #fffcd2 0%, #ffc684 40%, #ff8d0b 100%);
            }
            
        }
    }

    div.data-card-pic {
        margin-top: 2vh;
        background-size: cover;
        background-repeat: no-repeat;
        height: 42%;

        &#data-one-pic {
            background-image: url("../../assets/homePage/vector-data.png");
        }
        &#data-two-pic {
            background-image: url("../../assets/homePage/raster-data.jpg");
        }
        &#data-three-pic {
            background-image: url("../../assets/homePage/table-data.jpg");
        }
        &#data-four-pic {
            background-image: url("../../assets/homePage/projects.png");
        }
        &#data-five-pic {
            background-image: url("../../assets/homePage/flow.png");
        }
        &#data-six-pic {
            background-image: url("../../assets/homePage/tide.png");
        }
        &#data-seven-pic {
            background-image: url("../../assets/homePage/flood-dirt.jpg");
        }
        &#data-eight-pic {
            background-image: url("../../assets/homePage/river-bed.jpg");
        }
        &#data-nine-pic {
            background-image: url("../../assets/homePage/ele-dirt.jpg");
        }
    }
    

    div.data-card-content {
        padding-top: 3vh;
        width: 100%;
        text-align: center;
        font-size: 2vh;
        font-weight: 500;
        transition: 0.3s;
    }

    .data-card-button {
        margin-top: 5vh;
        width: 32%;
        height: 10%;
        margin-left: 34%;
        border-radius: 25px;
        font-size: 2vh;
        font-weight: 600;
        border-width: 2px;
        &#data-one-button, &#data-two-button, &#data-three-button {
            background-image: linear-gradient(30deg, #fffbbf 0%, #ffffa6 40%, #fdee1b 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            -webkit-animation: hue 60s infinite linear;
        }
        &#data-four-button, &#data-five-button, &#data-six-button {
            background-image: linear-gradient(30deg, #fffcd2 0%, #d5ffd5 40%, #3aff3a 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            -webkit-animation: hue 60s infinite linear;
        }
        &#data-seven-button, &#data-eight-button, &#data-nine-button {
            background-image: linear-gradient(30deg, #fffcd2 0%, #ffe0bc 40%, #ffa33a 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            -webkit-animation: hue 60s infinite linear;
        }
        
    }

}
</style>