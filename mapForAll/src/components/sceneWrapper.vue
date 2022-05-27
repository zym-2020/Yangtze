/* eslint-disable no-undef */
<template>
  <div class="scene-wrapper">
      <div class="content-wrapper">
            <div class="overall-data-wrapper">
                <div class="clock">
                    <p class="time">{{ curTime.toLocaleTimeString() }}</p>
                </div>
                <ChartContainer 
                    :chartId="chartInfo[0].chartId" 
                    :order="chartInfo[0].order" 
                    :styleType="chartInfo[0].styleType"
                ></ChartContainer>
            </div>
            <ChartContainer 
                :chartId="chartInfo[1].chartId" 
                :order="chartInfo[1].order" 
                :styleType="chartInfo[1].styleType"
            ></ChartContainer>
            <ChartContainer 
                :chartId="chartInfo[2].chartId" 
                :order="chartInfo[2].order" 
                :styleType="chartInfo[2].styleType"
            ></ChartContainer>
            <CenterMap :mapId="mapIndex"/>
            <ChartContainer 
                :chartId="chartInfo[3].chartId" 
                :order="chartInfo[3].order" 
                :styleType="chartInfo[3].styleType"
            ></ChartContainer>
            <DataTable :tableId='tableIndex'/>
            <div class="stat-data-wrapper">
                <a href='#'>
                    <div class='drop' @click="dropFunc">
                        <span>{{ dropText }}</span>
                    </div>
                </a>
                <div class='down' :class="{opened:flag}">
                    <ul>
                        <a href='#' v-for="choice in statNames" :key="choice.index">
                            <li  @click="chooseOpt(choice)">{{ choice.name }}</li>
                        </a>
                    </ul>
                </div>
                <h2 class="stat-title">涨落潮统计</h2>
                <div class="stats-container">
                    <div class="single-stat" v-for="stat in tideStat" :key="stat.index">
                        <h2>{{ stat.name }}</h2>
                        <span class="static">水位：</span><span class="dyn" :part="stat.index">{{ stat.val }}</span>
                        <span class="static">时刻：</span><span class="dyn" :part="stat.index">{{ stat.time }}</span>
                    </div>
                </div>
            </div>
      </div>
  </div>
</template>

<script lang='ts'>
import { onMounted } from '@vue/runtime-core';
import { ref } from 'vue';
import ChartContainer from './chartContainer.vue';
import CenterMap from './centerMap.vue';
import DataTable from './dataTable.vue';

export default {
    name: 'SceneWrapper',
    components: {
        ChartContainer, 
        CenterMap, 
        DataTable
    }, 
    setup() {
        const curTime = ref(new Date());
        const updataTime = (): void => {
            curTime.value = new Date();
        }

        const statNames = ref([
            {index:'0', name:'A'}, 
            {index:'1', name:'B'}, 
            {index:'2', name:'C'}, 
            {index:'3', name:'D'}
        ]);

        let flag = ref(false);

        function dropFunc() {
            flag.value = !flag.value;
        }

        const dropText = ref('选择测站');
        function chooseOpt(val: any) {
            console.log(val.name);
            dropText.value = val.name as string;
            tideStat.value = tideStatData[val.name as string]
            flag.value = !flag.value;
        }

        let tideStat = ref([
            {name:'高潮', val:'', time:'', index:'0'}, 
            {name:'低潮', val:'', time:'', index:'1'}, 
            {name:'高低潮', val:'', time:'', index:'2'}, 
            {name:'低高潮', val:'', time:'', index:'3'}, 
        ]);

        type statData = {
            [key: string]: {name: string; val: string; time: string; index: string;}[]
        }

        let tideStatData: statData= {
            'A':[
                {name:'高潮', val:'2.65', time:'23:00', index:'0'}, 
                {name:'低潮', val:'1.32', time:'19:00', index:'1'}, 
                {name:'高低潮', val:'1.39', time:'7:00', index:'2'}, 
                {name:'低高潮', val:'1.89', time:'10:00', index:'3'}
            ], 
            B:[
                {name:'高潮', val:'2.26', time:'22:00', index:'0'}, 
                {name:'低潮', val:'0.62', time:'16:00', index:'1'}, 
                {name:'高低潮', val:'0.69', time:'5:00', index:'2'}, 
                {name:'低高潮', val:'1.40', time:'9:00', index:'3'}
            ], 
            C:[
                {name:'高潮', val:'1.95', time:'20:00', index:'0'}, 
                {name:'低潮', val:'0.17', time:'13:00', index:'1'}, 
                {name:'高低潮', val:'0.18', time:'2:00', index:'2'}, 
                {name:'低高潮', val:'1.18', time:'6:00', index:'3'}
            ], 
            D:[
                {name:'高潮', val:'4.00', time:'3:00', index:'0'}, 
                {name:'低潮', val:'0.71', time:'23:00', index:'1'}, 
                {name:'高低潮', val:'0.80', time:'12:00', index:'2'}, 
                {name:'低高潮', val:'3.35', time:'15:00', index:'3'}
            ]
        };
        
        const chartInfo = ref([
            {chartId: "0", order: "0", styleType: "1"}, 
            {chartId: "1", order: "0", styleType: "2"}, 
            {chartId: "2", order: "0", styleType: "2"}, 
            {chartId: "3", order: "0", styleType: "3"}, 
        ]);

        const mapIndex = ref('0');

        const tableIndex = ref('0');

        onMounted(() => {
            setInterval(updataTime, 1000);
        });
        return {
            curTime, statNames, 
            flag, dropFunc, chooseOpt, dropText, 
            tideStat, chartInfo, mapIndex, tableIndex
        }
    }
}
</script>

<style lang='scss'>
$colorMap: ('0':#ADD5F7, '1':#123475, '2':#ADD5F7, '3':#123475);

div.scene-wrapper {
    height: 100vh;
    background: linear-gradient(50deg, #448bc5 0%, darken(#448bc5, 70%) 100%);
    background-size: 200% 200%;  
    animation: background 6s ease infinite;
    @keyframes background { 
        0% {
            background-position: 0% 50%;
        }
        50% {
            background-position: 100% 50%;
        }
        100% {
            background-position: 0% 50%;
        }
    }

    div.content-wrapper {
        position: absolute;
        height: 94%;
        width: 100%;
        top: 6%;
        background-color: transparent;
        display: flex;
        flex-flow: column wrap;
        justify-content: space-around;
        align-content: space-around;

        div {
            box-sizing: border-box;

            &.overall-data-wrapper {
                height: 27%;
                width: 23%;
                background-color: rgba(255, 255, 255, 0.3);
                border-radius: 0.6em;

                div.clock {
                    text-align: center;
                    position: absolute;
                    left: 15%;
                    top: -2%;
                    line-height: 1em;
                    p {
                        font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
                        color: rgba(255, 255, 255, 0.8);
                        letter-spacing: 0.05em;
                        font-size: 2em;
                        text-shadow: 
                            0 0.5px 0 #ccc, 
                            0 1px 0 #c9c9c9, 
                            0 1.5px 0 #bbb, 
                            0 2px 0 #b9b9b9, 
                            0 2.5px 0 #aaa, 
                            0 3px 0.5px rgba(0,0,0,.1), 
                            0 0 2.5px rgba(0,0,0,.1), 
                            0 0.5px 1.5px rgba(0,0,0,.3), 
                            0 1.5px 2.5px rgba(0,0,0,.2), 
                            0 2.5px 5px rgba(0,0,0,.25), 
                            0 5px 5px rgba(0,0,0,.2), 
                            0 10px 10px rgba(0,0,0,.15);
                    }

                    p {
                        width: 100%;
                        height: 100%;
                        line-height: 100px;
                        text-align: center;
                        vertical-align: middle;
                    }
                }
            }

            &.stat-data-wrapper {
                background-color: rgba(255, 255, 255, 0.2);
                height: 45%;
                width: 23%;
                border-radius: 0.6em;

                div.drop {
                    margin-top: 4%;
                    margin-left: 3%;
                    display: inline-block;
                    width: 7em;
                    height: 2em;
                    line-height: 1.2em;
                    padding: 0.2em 1.2em;
                    background: rgba(0, 11, 68, 0.6);
                    color: white;
                    font-family: Tahoma;
                    font-size: 1.2em;
                    text-align: center;
                    border: 2px solid rgba(0, 11, 68, 0.8);
                    border-radius: 2em;
                    cursor: default;
                    transition: .5s;

                    span::selection {
                        background:rgba(255, 255, 255, 0.8);
                        color:rgb(142, 194, 255);
                    }
                    &:active {
                        position:relative;
                        top:2px;
                    }
                    &:hover {
                        background:#2955a7;
                        color:white;
                    }

                    span {
                        padding-left:0.2em;
                        font-weight:bold;
                    }
                }
                div.down {
                    margin-top: 3%;
                    display: inline-block;
                    padding: 4px 8px 6px 4px;
                    background: rgba(0, 11, 68, 0.8);
                    font-family: Tahoma;
                    font-size: 1.2em;
                    border: 2px solid rgba(0, 11, 68, 0.8);
                    border-radius: 8px;
                    vertical-align: top;
                    position:relative;
                    transition: 0.5s;
                    transform:scale(0) translateX(15em) translateY(-3em);
                    z-index: 2000;

                    &.opened {
                        transform:scale(1) translateX(0) translateY(0);
                    }

                    ul {
                        padding-left:4px;
                    }
                    a {
                        text-decoration:none;
                    }
                    li {
                        list-style-type:none; 
                        margin-bottom:6px;
                        padding:4px 20px;
                        padding-right:18px;  
                        color:white;
                        border-radius:6px;
                        cursor:pointer;
                        transition:.5s;  
                    }
                    li:active {
                        position:relative;
                        top:2px;
                    }
                    li:hover {
                        background:rgba(255, 255, 255, 0.8);
                        color:rgb(0, 28, 59);
                    }
                }

                h2.stat-title {
                    font-size: 2.2em;
                    position: absolute;
                    right: 1.2%;
                    bottom: 40.2%;
                    color: rgba(255, 255, 255, 0.9);
                }

                div.stats-container {
                    width: 22%;
                    height: 36%;
                    position: absolute;
                    right: 1em;
                    bottom: 1em;
                    background-color: transparent;
                    display: flex;
                    flex-flow: row wrap;
                    justify-content: space-around;
                    align-content: space-around;

                    div.single-stat {
                        width: 46%;
                        height: 46%;
                        background-color: rgba(221, 230, 255, 0.3);
                        border-radius: 1em;
                        display: flex;
                        flex-flow: row wrap;
                        align-items: flex-start;
                        align-content: space-around;
                        gap: 0em 0em;

                        h2 {
                            padding-top: 0.1em;
                            width: 100%;
                            font-size: 1.5em;
                            color: #003ab8;
                            text-align: center;
                        }
                        span {
                            line-height: 1em;
                            font-size: 1.8em;
                            font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
                            transition: ease-in-out 0.5s;
                            width: 40%;
                            text-align: left;
                            &.static {
                                width: 50%;
                                margin-left: 5%;
                                text-align: right;
                                color: aliceblue;
                                font-size: 1.5em;
                            }
                            &.dyn {
                                animation: ease-in-out 2s;
                            }
                            @each $index, $colorP in $colorMap {
                                &[part="#{$index}"] {
                                    color: $colorP;
                                }
                            }
                        }
                        
                    }
                }
            }
        }
    }
}
</style>