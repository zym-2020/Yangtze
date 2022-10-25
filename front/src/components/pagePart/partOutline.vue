<template>
    <div>
        <div class="part-bg" :id="partData[parseInt(partID)].id + '-bg'"></div>
        <div class="data-title" :id="partData[parseInt(partID)].id + '-title'">{{ partData[parseInt(partID)].title }}</div>
        <div id="divider"></div>
        <el-button type="primary" class="data-button" :id="partData[parseInt(partID)].id + '-button'" :color="partData[parseInt(partID)].butColor">{{ partData[parseInt(partID)].but }}</el-button>
        <div class="data-content" :id="partData[parseInt(partID)].id + '-content'">{{ partData[parseInt(partID)].cont }}</div>
    </div>
</template>

<script lang="ts">
export default {
    name: 'PartOutline'
}
</script>

<script setup lang="ts">
import { ref } from 'vue';
defineProps({
  partID: String
});

const partData = ref(
    [
        {id:"data", title: "资源门户", but: "更多数据", cont:"综合的黄金水道时空数据库，支撑相关数据的分类存储、快速检索、变更告知和制图输出。", butColor:"#ffef94de"}, 
        {id:"amap", title: "一张图", but: "查看更多", cont:"综合的黄金水道时空数据库，支撑相关数据的分类存储、快速检索、变更告知和制图输出。", butColor:"#94ffa2de"}, 
        {id:"analysis", title: "分析中心", but: "进入中心", cont:"综合的黄金水道时空数据库，支撑相关数据的分类存储、快速检索、变更告知和制图输出。", butColor:"#ffae61d2"}
    ]
);

</script>

<style lang="scss" scoped>
div.part-bg {
    position: absolute;
    right: 2vw;
    top: 20vh;
    background-size: cover;
    opacity: 0.3;

    &#data-bg {
        width: 435px;
        height: 488px;
        background-image: url("../../assets/homePage/big-data.png");
    }

    &#amap-bg {
        right: 3vw;
        top: 26vh;
        width: 478px;
        height: 356px;
        background-image: url("../../assets/homePage/amap-bg.png");
    }

    &#analysis-bg {
        width: 458px;
        height: 448px;
        right: 3vw;
        opacity: 0.3;
        background-image: url("../../assets/homePage/geoAnalysis.png");
    }
}

div.data-title {
    position: absolute;
    right: 7vw;
    top: 20vh;
    font-size: 6vh;
    font-weight: 600;
    

    &#data-title {
        background-image: linear-gradient(30deg, #fffef0 0%, #9edcfc 10%, #fff352 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        -webkit-animation: hue 60s infinite linear;
    }

    &#amap-title {
        background-image: linear-gradient(30deg, #fffef0 0%, #9edcfc 10%, #52ff52 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        -webkit-animation: hue 60s infinite linear;
    }

    &#analysis-title {
        background-image: linear-gradient(30deg, #fffef0 0%, #9edcfc 10%, #ff9252 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        -webkit-animation: hue 60s infinite linear;
    }
}

div#divider {
    position: absolute;
    width: 280px;
    height: 40px;
    top: 32vh;
    right: 7vw;
    background-image: url("../../assets/homePage/flow-simple-small.png");
    background-size: cover;
    opacity: 0.75;
}

div.data-content {
    position: absolute;
    right: 5vw;
    top: 40vh;
    width: 18vw;
    height: 20vh;
    font-size: 3vh;
    font-weight: 600;

    &#data-content {
        color: rgb(255, 233, 110);
    }

    &#amap-content {
        color: rgb(151, 255, 173);
    }

    &#analysis-content {
        color: rgb(255, 188, 125);
    }
}

.data-button {
    position: absolute;
    top: 60vh;
    right: 7vw;
    width: 6vw;
    height: 5vh;
    font-size: 2vh;
    font-weight: 600;
    font-family: 'Microsoft YaHei';
    color: #94ffa2de;
    color: #ffae61d2;
    color: #012063;
    // transition-duration: 0.2s;

    &#data-button {
        background-image: linear-gradient(30deg, #fffcd2 0%, #fdfdd7 40%, #fff23a 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        -webkit-animation: hue 60s infinite linear;
    }

    &#amap-button {
        background-image: linear-gradient(30deg, #fffcd2 0%, #d5ffd5 40%, #3aff3a 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        -webkit-animation: hue 60s infinite linear;
    }

    &#analysis-button {
        background-image: linear-gradient(30deg, #fffcd2 0%, #ffe0bc 40%, #ffa33a 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        -webkit-animation: hue 60s infinite linear;
    }


    &:hover {
        // background-image: linear-gradient(30deg, #fffab4 0%, #fafa8c 40%, #fcec11 100%);
        border-width: 2px;
        // transition-duration: 0.2s;
    }
}
</style>