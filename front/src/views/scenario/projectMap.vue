<template>
    <div id="map"></div>
    <el-card shadow="hover" id="total-num" :body-style="{ padding: '0 16px 0 16px' }">
        <h3 class="header-small">工程总数</h3>
        <p class="num-small">12</p>
    </el-card>
    <el-checkbox-group class="checkbox-group" id="info-vs" v-model="checkboxGroup1" size="large" @change="InfoChange">
        <el-checkbox-button class="check-button" id="info-checkbox" label="info" size='large'>
            <el-icon size="1.4vw"><InfoFilled /></el-icon>
        </el-checkbox-button>
        <el-checkbox-button class="check-button" id="compare-checkbox" label="vs" size='large'>
            <el-icon size="1.4vw"><Plus /></el-icon>
        </el-checkbox-button>
    </el-checkbox-group>
    <el-checkbox-group class="checkbox-group" id="project-plan" v-model="listGroup" size="large" @change="ChangeList">
        <el-checkbox-button class="check-button" id="project-checkbox" label="projects" size='large'>
            <p class="check-text">工程</p>
        </el-checkbox-button>
        <el-checkbox-button class="check-button" id="plan-checkbox" label="plans" size='large'>
            <p class="check-text">方案</p>
        </el-checkbox-button>
    </el-checkbox-group>
    <transition name="fade">
        <div class="project-list projects" :class="listInfo['projects'].pos" id='list-top' v-show="listInfo['projects'].seen">
            <h2 class="list-header">工程列表</h2>
            <el-scrollbar class="scroll-group">
                <el-button class="project-button" type="primary" plain v-for="project in projectList" :key="project.id">{{ project.name }}</el-button>
            </el-scrollbar>
        </div>
    </transition>
    <transition name="fade">
        <div class="project-list plans" :class="listInfo['plans'].pos" id="list-bot" v-show="listInfo['plans'].seen">
            <h2 class="list-header">方案列表</h2>
            <el-scrollbar class="scroll-group">
                <el-button class="plan-button" type="primary" plain v-for="plan in planList" :key="plan.id">{{ plan.name }}</el-button>
            </el-scrollbar>
        </div>
    </transition>
    <transition name="fade">
        <el-card class="detail-card" v-if="detailSeen">
            <template #header>
                <div class="card-header">
                    <span>{{ detailInfo.name }}</span>
                </div>
            </template>
            <div class="detail-info">
                <div class="two-col" id="detail-status">
                    <span>状态：</span>
                    <span class="status-text" :class="statusDic[detailInfo.status]">{{ detailInfo.status }}</span>
                </div>
                <div class="two-col" id="detail-money">
                    <span>总资金</span>
                    <span class="money-text">{{ detailInfo.money }}</span>
                </div>
                <div class="two-col two-row" id="detail-st">
                    <span>开工时间</span>
                    <span class="st-text">{{ detailInfo.startTime }}</span>
                </div>
                <div class="two-col" id="detail-dur">
                    <span>总工期:</span>
                    <span class="dur-text">{{ detailInfo.allDuration }}</span>
                </div>
                <div class="one-col" id="detail-brief">简述：{{ detailInfo.brief }}</div>
                <div class="one-col" id="detail-target">建设内容：{{ detailInfo.target }}</div>
            </div>
        </el-card>
    </transition>
</template>

<script lang="ts">
export default {
    name: 'ProjectMap', 
}
</script>

<script setup lang="ts">
import mapboxgl, { PointLike } from 'mapbox-gl';
import { onMounted } from 'vue-demi';
import { Ref, ref } from 'vue';
import 'mapbox-gl/dist/mapbox-gl.css';
import MapUtil from '../../utils/mapUtils';

const checkboxGroup1 = ref<Array<String>>([]);

const listGroup = ref(['projects', 'plans']);

const lists = ['projects', 'plans'];

const detailSeen = ref(false);

mapboxgl.accessToken =
  "pk.eyJ1Ijoiam9obm55dCIsImEiOiJja2xxNXplNjYwNnhzMm5uYTJtdHVlbTByIn0.f1GfZbFLWjiEayI6hb_Qvg";

const projectList = ref([
    {name:'白茆小沙整治工程', id:0, lyrName:'bmxszzproject', flySet: {zoom:12.71, center:[121.054,31.751]}, status: '进行中', money: '20.62亿', startTime: '2016-10-9', allDuration: '14个月', brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'}, 
    {name:'横港沙整治工程', id:1, lyrName:'hgszzproject', flySet: {zoom:12.71, center:[120.705,32.029]}, status: '未开始', money: '12.4亿', startTime: '2012-10-9', allDuration: '18个月', brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'}, 
    {name:'通州沙潜堤左侧延长工程', id:2, lyrName:'tzszdycproject', flySet: {zoom:12.8, center:[120.836,31.967]}, status: '已完成', money: '850万', startTime: '2011-10-9', allDuration: '20个月', brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'}, 
    {name:'西水道一期工程', id:3, lyrName:'xsd1qproject', flySet: {zoom:11.65, center:[120.851,31.917]}, status: '进行中', money: '4.32亿', startTime: '2018-10-9', allDuration: '32个月', brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'}, 
    {name:'西水道二期工程', id:4, lyrName:'xsd2qproject', flySet: {zoom:12.38, center:[120.811,31.930]}, status: '未开始', money: '2.43亿', startTime: '2020-10-9', allDuration: '30个月', brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'}, 
    {name:'西水道三期工程', id:5, lyrName:'xsd3qproject', flySet: {zoom:11.5, center:[120.830,31.902]}, status: '进行中', money: '1.52亿', startTime: '2017-10-9', allDuration: '36个月', brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'}, 
    {name:'新开沙整治工程', id:6, lyrName:'xkszzproject', flySet: {zoom:12.55, center:[120.951,31.848]}, status: '未开始', money: '2.47亿', startTime: '2012-10-9', allDuration: '48个月', brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'}, 
]);

const planList = ref([
    {name:'9新福中&福北方案', id:0, lyrName:'fzfbplan', flySet: {zoom:11.1, center:[120.531,32.035]}, status: '未开始', money: '1.22亿', startTime: '2014-7-9', allDuration: '22个月', brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'}, 
    {name:'护漕港边滩推荐方案', id:1, lyrName:'hcgbtplan', flySet: {zoom:13.13, center:[120.494,32.018]}, status: '未开始', money: '3.40亿', startTime: '2018-10-9', allDuration: '12个月', brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'}, 
    {name:'护滩方案', id:2, lyrName:'htplan', flySet: {zoom:11.14, center:[120.627,31.991]}, status: '未开始', money: '20.62亿', startTime: '2012-4-9', allDuration: '14个月', brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'}, 
    {name:'黄铁沙一期推荐方案', id:3, lyrName:'hts1qplan', flySet: {zoom:12.85, center:[120.860,31.793]}, status: '未开始', money: '2.68亿', startTime: '2013-2-9', allDuration: '8个月', brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'}, 
    {name:'双涧沙施工方案', id:4, lyrName:'szsplan', flySet: {zoom:13.00, center:[120.469,32.047]}, status: '未开始', money: '7.62亿', startTime: '2013-8-12', allDuration: '10个月', brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'}, 
    {name:'通白初设施工方案', id:5, lyrName:'tbcsplan', flySet: {zoom:10.61, center:[121.031,31.860]}, status: '未开始', money: '15.68亿', startTime: '2014-12-3', allDuration: '16个月', brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'}, 
]);

const layerIDList = [
    'bmxszzproject', 'hgszzproject', 'tzszdycproject', 'xsd1qproject', 'xsd2qproject', 'xsd3qproject', 'xkszzproject', 
    'fzfbplan', 'hcgbtplan', 'htplan', 'hts1qplan', 'szsplan', 'tbcsplan'];

const listInfo: Ref<{[key: string]: {pos: string, seen: boolean}}> = ref({
    'projects': {pos: 'top', seen:true}, 
    'plans': {pos: 'bot', seen:true},
});

const detailInfo = ref({
    name: '通州沙潜堤左侧延长工程', 
    status: '已完成', 
    money: '20.62亿', 
    startTime: '2014-10-9', 
    allDuration: '32个月', 
    brief: '该工程是国务院部署实施的150项重大水利工程之一，也是今年重点推进的55项重大水利工程之一', 
    target: '全面提升湘西州府吉首市城市防洪能力和供水保障能力，改善吉首市农业产业灌溉条件，防洪能力由10年一遇提高到50年一遇，同时还将进一步丰富旅游资源，改善峒河流域生态环境'
});

function ChangeList() {
    let len = listGroup.value.length;
    if(len == 0) {
        listInfo.value.projects.seen = false;
        listInfo.value.plans.seen = false;
    }
    else if(len == 1) {
        let seenOne = listGroup.value[0];
        let otherOne = lists[ 1 - lists.indexOf(seenOne)];
        listInfo.value[seenOne].seen = true;
        listInfo.value[seenOne].pos = 'top';
        listInfo.value[otherOne].seen = false;
        listInfo.value[otherOne].pos = 'bot';
    }
    else {
        listInfo.value.projects.seen = true;
        listInfo.value.plans.seen = true;
    }
}

const statusDic = {'未开始': 'red-stat', '进行中': 'yellow-stat', '已完成': 'green-stat'}

function InfoChange() {
    if(checkboxGroup1.value.indexOf('info') >= 0) {
        detailSeen.value = true;
    }
    else {
        detailSeen.value = false;
    }
}

function GetLyrIndex(lyrId: string, infoList: Array<{lyrName: string}>) {
    let len = infoList.length;
    for(let i=0; i< len; i++) {
        if(infoList[i].lyrName == lyrId) {
            return i;
        }
    }
}

onMounted(()=> {
    const map = new mapboxgl.Map({
        container: "map",
        style: "mapbox://styles/johnnyt/cl4wa5e28003n14l8ykdpchb4",
        center: [120.790, 31.890],
        zoom: 10,
        pitch: 0,
    });
    let mapUtil = new MapUtil(map);

    let ShowDetail = async function(id: number, listId: number) {
        let list = projectList.value;
        if(listId == 1) {
            list = planList.value;
        }

        let lyrName = list[id].lyrName;
        let flySet = list[id].flySet;
        detailInfo.value = {name: list[id].name, status: list[id].status, money: list[id].money, startTime: list[id].startTime, allDuration: list[id].allDuration, brief: list[id].brief, target:list[id].target};
        mapUtil.Fly2Pos(flySet.zoom, flySet.center as [number, number]);
        await new Promise(r => setTimeout(r, 500));
        if(checkboxGroup1.value.indexOf('info') < 0) {
            checkboxGroup1.value.push('info');
            detailSeen.value = true;
        }
        mapUtil.LineBlink(lyrName);
        // console.log('0');
    }

    map.on('load', () => {
        let projctButtons = document.getElementsByClassName('project-button') as HTMLCollectionOf<HTMLButtonElement>;
        for(let i = 0; i < projctButtons.length ; i++) {
            projctButtons[i].onclick = function() {ShowDetail(i, 0);};
        }

        let planButtons = document.getElementsByClassName('plan-button') as HTMLCollectionOf<HTMLButtonElement>;
        for(let i = 0; i < planButtons.length ; i++) {
            planButtons[i].onclick = function() {ShowDetail(i, 1);};
        }

        map.on('click', layerIDList, (e)=> {
            const bbox: [PointLike, PointLike] = [
                [e.point.x - 5, e.point.y - 5],
                [e.point.x + 5, e.point.y + 5]
            ];
            
            const selectedFeatures = map.queryRenderedFeatures(bbox, {
                layers: layerIDList
            });
            console.log(selectedFeatures);

            if(selectedFeatures.length > 0) {
                let listID = 0;
                let lyrId = selectedFeatures[0].sourceLayer;
                let strType = lyrId.substr(lyrId.length-4, 4);
                if(strType == 'Plan') listID = 1;
                let list = projectList.value;
                if(listID == 1) {
                    list = planList.value;
                }
                console.log(lyrId, list[0]);
                let lyrIndex = GetLyrIndex(lyrId.toLowerCase(), list);
                ShowDetail(lyrIndex as number, listID);
            }
        });
        // console.log('cursor', map.getCanvas().style.cursor);

        map.on('mouseenter', layerIDList, (e)=> {
            map.getCanvas().style.cursor = 'pointer';
        });
        map.on('mouseleave', layerIDList, (e)=> {
            map.getCanvas().style.cursor = '';
        });
    });


})


</script>

<style lang='scss' scoped>
div#map {
  width: 100vw;
  height: 100vh;
  position: absolute;
  top: 0;
  left: 0;
  background: rgb(0, 155, 226);
}

#total-num {
    position: absolute;
    padding-top: 8px;
    padding-bottom: 8px;
    top: 8%;
    left: 0.5%;
    text-align: center;
    backdrop-filter: blur(5px);
    background-color: rgba(1, 44, 99, 0.6);
    border-radius: 8px;
    color: white;

    .header-small {
        font-size: 1.5em;
        margin: 0px;
        font-weight: 400;
    }

    .num-small {
        font-size: 1.8em;
        font-weight: 400;
        margin-top: 8px;
        margin-bottom: 0;
        font-family: fantasy;
        letter-spacing: 0.1em;

        &:hover {
            color: rgb(128, 196, 252);
            cursor: pointer;
        }
    }
}

.checkbox-group {
    position: absolute;

    &#info-vs {
        top: 24%;
        left: 0.5%;
    }

    &#project-plan {
        top: 8%;
        right: 0.5%;

        p {
            font-size: 1.4em;
            margin-top: 0px;
            margin-bottom: 0px;
            font-weight: 700;
        }
    }

}

h2 {
    text-align: center;
    color: white;
}

.project-list {
    width: 20%;
    position: absolute;
    top: 16%;
    right: 0.5%;
    height: 36%;
    border-radius: 5px;
    border: 1px solid rgb(178, 219, 255);
    background-color: rgba(1, 44, 99, 0.6);
    backdrop-filter: blur(5px);
    // transform: translateX(110%);
    transition: 0.5s ease-in-out;

    &.bot {
        top: 56%;
    }

    h2.list-header {
        margin-bottom: 2px;
        margin-top: 4px;
        font-size: 1.6em;
    }

    .scroll-group {
        width: 100%;
        height: 84%;

        .project-button, .plan-button {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 50px;
            width: 96%;
            margin: 10px;
            text-align: center;
            border-radius: 4px;
            color: rgba(1, 44, 99, 0.8);

            &:hover {
                color: rgba(255, 255, 255, 1);
            }

            font-size: 1.2em;
            font-weight: 800;
        }
    }
}

.fade-enter-active {
    transition: opacity 0.5s ease;
}

.fade-leave-active {
    transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}

.detail-card {
    position: absolute;
    top: 36%;
    left: 0.5%;
    width: 20vw;
    height: 28vw;
    background-color: rgba(1, 44, 99, 0.6);
    backdrop-filter: blur(5px);
    color: white;
    border-radius: 10px;
    border-color: rgb(178, 219, 255);

    :deep().el-card__header {
        padding: 10px 5px;
        font-size: 1.2em;
        font-weight: 600;
        border-color: rgb(178, 219, 255); 

        .card-header {
            text-align: center;
            font-size: 1.4em;
        }
    }

    :deep().el-card__body {
        padding: 0;

        div.detail-info {
            position: relative;
            width: 20vw;
            height: 25vw;
            // background-color: red;
            display: flex;
            flex-flow: row wrap;
            justify-content: center;
            align-content: center;
            row-gap: 12px;
            column-gap: 8px;
            color: rgba(1, 44, 99, 0.8);

            div.two-col {
                width: 9vw;
                height: 4vw;

                display: flex;
                flex-flow: row wrap;
                align-items: center;
                justify-content: center;

                &.two-row {
                    align-content: center;
                    gap: 8px;
                }


                // box-sizing: border-box;
                // border: 1px solid rgba(219, 241, 255, 0.8);
                border-radius: 10px;
                background-color: rgba(229, 242, 253, 0.95);

                font-size: 1.4em;
                font-weight: 600;

                span {
                    &.red-stat {
                        color: red;
                    }
                    &.yellow-stat {
                        color: #ff8725;
                    }
                    &.green-stat {
                        color: green;
                    }
                    &.money-text {
                        color: rgb(241, 184, 37);
                        font-size: 1.4em;
                    }
                    &.st-text {
                        color: rgba(60, 103, 160, 0.8);
                    }
                    &.dur-text {
                        color: rgba(60, 103, 160, 0.8);
                    }
                }
            }

            div.one-col {
                width: 18.5vw;
                height: 8vw;
                padding: 6px;
                
                display: flex;
                align-content: center;
                align-items: center;
                &#detail-brief {
                    height: 5vw;
                }

                border-radius: 12px;
                box-sizing: border-box;
                //border: 1px solid rgba(196, 195, 195, 0.8);
                background-color: rgba(229, 242, 253, 0.95);

                font-size: 1.2em;
                font-weight: 600;
            }

        }
    }
}

</style>