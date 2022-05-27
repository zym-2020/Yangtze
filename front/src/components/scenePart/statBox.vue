<template>
  <div class="stat-data-wrapper">
    <a>
      <div class="drop" @click="dropFunc">
        <span>{{ dropText }}</span>
      </div>
    </a>
    <div class="down" :class="{ opened: flag }">
      <ul>
        <a v-for="choice in statNames" :key="choice.index">
          <li @click="chooseOpt(choice)">{{ choice.name }}</li>
        </a>
      </ul>
    </div>
    <h2 class="stat-title">涨落潮统计</h2>
    <div class="stats-container">
      <div class="single-stat" v-for="stat in tideSta" :key="stat.index">
        <h2>{{ stat.name }}</h2>
        <span class="static">水位：</span
        ><span class="dyn" :part="stat.index">{{ stat.val }}</span>
        <span class="static">时刻：</span
        ><span class="dyn" :part="stat.index">{{ stat.time }}</span>
      </div>
    </div>
  </div>
</template>

<script lang='ts'>
export default {
  name: "SingleStat",
};
</script>

<script setup lang='ts'>
import { Prop, ref } from "vue";
import { tideStat } from '../../frontData';

interface Props {
  statIndex: string;
}

const props = defineProps<Props>();

const statId = ref(props.statIndex);

const statNames = ref([
  {index:'0', name:'A'}, 
  {index:'1', name:'B'}, 
  {index:'2', name:'C'}, 
  {index:'3', name:'D'}
]);

const tideSta = ref([
  {name:'高潮', val:'', time:'', index:'0'}, 
  {name:'低潮', val:'', time:'', index:'1'}, 
  {name:'高低潮', val:'', time:'', index:'2'}, 
  {name:'低高潮', val:'', time:'', index:'3'}, 
]);

let flag = ref(false);

function dropFunc() {
    flag.value = !flag.value;
}

const tideData = tideStat[+statId.value];

const dropText = ref('选择测站');
function chooseOpt(val: any) {
    console.log(val.name);
    dropText.value = val.name as string;
    tideSta.value = tideData[val.name as string];
    flag.value = !flag.value;
}


</script>

<style lang='scss' scoped>
$colorMap: ('0':#ADD5F7, '1':#123475, '2':#ADD5F7, '3':#123475);

div.stat-data-wrapper {
  background-color: rgba(255, 255, 255, 0.2);
  height: 45%;
  width: 23%;
  border-radius: 0.6em;

  div.drop {
    margin-top: 4%;
    margin-left: 3%;
    display: inline-block;
    width: 5em;
    height: 1.2em;
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
    transition: 0.5s;

    span::selection {
      background: rgba(255, 255, 255, 0.8);
      color: rgb(142, 194, 255);
    }
    &:active {
      position: relative;
      top: 2px;
    }
    &:hover {
      background: #2955a7;
      color: white;
    }

    span {
      padding-left: 0.2em;
      font-weight: bold;
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
    position: relative;
    transition: 0.5s;
    transform: scale(0) translateX(15em) translateY(-3em);
    z-index: 2000;

    &.opened {
      transform: scale(1) translateX(0) translateY(0);
    }

    ul {
      padding-left: 4px;
    }
    a {
      text-decoration: none;
    }
    li {
      list-style-type: none;
      margin-bottom: 6px;
      padding: 4px 20px;
      padding-right: 18px;
      color: white;
      border-radius: 6px;
      cursor: pointer;
      transition: 0.5s;
    }
    li:active {
      position: relative;
      top: 2px;
    }
    li:hover {
      background: rgba(255, 255, 255, 0.8);
      color: rgb(0, 28, 59);
    }
  }

  h2.stat-title {
    font-size: 2.2em;
    position: absolute;
    right: 1.2%;
    bottom: 37%; 
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
        bottom: 35%; 
        text-align: center;
        margin-block-start: 0em;
        margin-block-end: 0em;
      }
      span {
        line-height: 1em;
        font-size: 1.8em;
        font-family: Impact, Haettenschweiler, "Arial Narrow Bold", sans-serif;
        transition: ease-in-out 0.5s;
        width: 40%;
        text-align: left;
        margin-block-start: 0em;
        margin-block-end: 0em;
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
</style>