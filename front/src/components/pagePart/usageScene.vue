<template>
  <div class="usage-wrapper" :pos="position">
    <div class="image-wrapper" :id="'index'+sequence" :pos="position"></div>
    <div class="text-wrapper" :pos="position">
      <h2>{{usageText[sequence].title}}</h2>
      <p>
        {{usageText[sequence].descript}}
      </p>
      <a class="button arrow">点击进入</a>
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
      '1': {title:'资源门户', descript:'包罗用户所上传的所有共享资源，资源门户以资源共享与资源预览功能为核心的同时，又以资源描述与资源分类为支持进行资源索引。对资源进行灵活整合与统一组织。'}, 
      '2': {title:'一张图', descript:'以底图数据和同类相关信息资源整合为基础，以统一的技术标准整合各类信息资源，实现了部门内部信息资源的集成管理和共享服务，与完整的资源展示和交互功能。'}, 
      '3': {title:'分析中心', descript:'综合研究分析平台以数据和模型分析库及分析功能为驱动，以中心数据库为中心，结合本地数据库中的缓存数据，支撑应用层的各种分析操作。'}
    });
    return { sequence, position, usageText };
  }
}
  
</script>

<style lang='scss' scoped>
$carouselBg: "../../assets/home/usage";
$indexs : 'index1', 'index2', 'index3';

div.usage-wrapper {
  width: 75%;
  height: 32em;
  margin-left: 5%;
  margin-right: 20%;
  &[pos='right'] {
    margin-left: 20%;
    margin-right: 5%;
  }
  margin-top: 3%;
  border-radius: 5%;
  display: flex;
  justify-content: space-between;
  gap: 7em;

  div {
    &.image-wrapper {
      width: 60%;
      background-color: rgb(40, 40, 40);

      // 添加对应编号的图片 ！！注意文件命名规则！！
      @each $index in $indexs {
        &[id=#{$index}] {
            background-image: url($carouselBg + $index + '.png'); // 需要改路径
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
      gap: 1.6em;

      h2 {
        margin-top: 1.6em;
        margin-bottom: 0;
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