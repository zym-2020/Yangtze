<template>
  <div class="usage-wrapper" :pos="position">
    <div class="image-wrapper" :id="'index'+sequence" :pos="position"></div>
    <div class="text-wrapper" :pos="position">
      <h2>{{usageText[sequence].title}}</h2>
      <p>
        {{usageText[sequence].descript}}
      </p>
      <a class="button arrow">Learn More</a>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  name: "usageScene", 
  props : {
      seq: String, 
      pos: String
  }, 
  setup(props) {
    // 接受父组件传来的参数seq
    const sequence = ref(props.seq);
    const position = ref(props.pos);
    const usageText = ref({
      '1': {title:'Search', descript:'Search and geocoding is tied to everything we build — maps, navigation, AR — and underlies every app that helps humans explore their world.'}, 
      '2': {title:'Atlas', descript:'With Atlas, you can self-host Mapbox maps and geocoding APIs, Streets, Satellite, and Terrain tilesets, and Mapbox Studio on your network, behind a firewall, or even air-gapped. '}, 
      '3': {title:'Data', descript:'Our data is powered by hundreds of data sources, and a distributed global users base of more than half a billion monthly active users.'}
    })
    return { sequence, position, usageText };
  }
}
  
</script>

<style lang='scss' scope>
$indexs : 'index1', 'index2', 'index3', 'index4', 'index5', 'index6';

div.usage-wrapper {
  width: 70%;
  height: 32em;
  margin-left: 6%;
  margin-right: 24%;
  &[pos='right'] {
    margin-left: 24%;
    margin-right: 6%;
  }
  margin-top: 2%;
  border-radius: 5%;
  display: flex;
  justify-content: space-between;

  div {
    &.image-wrapper {
      width: 55%;
      background-color: rgb(40, 40, 40);

      // 添加对应编号的图片 ！！注意文件命名规则！！
      @each $index in $indexs {
        &[id=#{$index}] {
            background-image: url('../assets/carousel' + $index + '.png'); // 需要改路径
            background-size : cover;
        }
      }

      &[pos='right'] {
        order: 2;
      }
    }

    &.text-wrapper {
      width: 40%;
      background-color: transparent;
      display: flex;
      flex-direction: column;
      gap: 2em;

      h2 {
        margin-top: 2.8em;
        color: rgb(226, 226, 226);
        font-size: 3em;
      }

      p {
        color: rgba(226, 226, 226, 0.75);
        font-size: 1.5em;
        line-height: 1.5em;
      }

      a {
        &.button {
          margin-left: 60%;
          font-size: 1.2em;
          display: flex;
          align-items: center;
          text-align: center;
          justify-content: center;
          border-radius: 1.1em 1.1em 1.1em 1.1em;
          border-style: solid;
          border-width: 1px;
          border-color: #cde4ff;
        }

        &.arrow {
          color: #cde4ff;
          background-color: transparent;
          width: 32%;
          height: 8%;

          &::after {
            display: inline-block;
            content: "\0279E"; // arrow right unicode
            -webkit-transition: transform 0.3s ease-out;
            -moz-transition: transform 0.3s ease-out;
            -ms-transition: transform 0.3s ease-out;
            -o-transition: transform 0.3s ease-out;
            transition: transform 0.3s ease-out;
          }

          &:hover {
            color: rgb(55, 150, 251);
            border-color: rgb(55, 150, 251);
            cursor: pointer;

            &::after {
                -webkit-transform: translateX(4px);
                -moz-transform: translateX(4px);
                -ms-transform: translateX(4px);
                -o-transform: translateX(4px);
                transform: translateX(4px);
            }
          }
        }        
      }

      &[pos='right'] {
        order: 1;
      }
    }
  }
}



</style>