<template>
  <form>
    <div class="multi-select">
      <label class="toggle-open" for="toggle-open">选择组件</label>
      <input type="checkbox" id="toggle-open" />
      <ul class="multi-select-options">
        <li v-for="com in componentInfo" :key="com.index">
          <label>
            <input
              type="checkbox"
              funcId="{{ com.funcId }}"
            /><span>{{ com.name }}</span>
          </label>
        </li>
      </ul>
    </div>
    <ul class="multi-selected"></ul>
  </form>
</template>

<script lang='ts'>
export default {
  name: "DropSelector",
};
</script>

<script setup lang='ts'>
import { commponentOpt } from '../../frontData';
import { ref } from 'vue';

interface Props {
    selectorId : string, 
}

const props = defineProps<Props>();

const comIndex = ref(props.selectorId);

const componentInfo = ref(commponentOpt[+comIndex.value]);


</script>


<style lang='scss' scoped>
form {
  position: absolute;
  right: 3vw;
  top: 5vh;
  margin: 30px auto;
  width: 20vw;
  z-index: 1000;

  div.multi-select {
    &,
    ul.multi-select-options {
      background: rgba(255, 255, 255, 0.5);
      width: 100%;
    }

    display: block;
    position: relative;
    line-height: 35px;
    border: 1px solid #ddd;
    margin: 0 0 50px;
    padding: 0;
    border-radius: 1em;

    label {
      margin: 0;
      padding: 0;

      &.toggle-open {
        display: block;
        font-size: 1em;
        font-weight: bold;
        padding: 0 10px;
        height: 3.6vh;
        width: auto;
        line-height: 3.6vh;
        cursor: pointer;
        &:after {
          clear: both;
          display: block;
          position: absolute;
          top: 1.6vh;
          right: 0.6vw;
          content: "";
          height: 0;
          width: 0;
          padding: 0;
          margin: 0 auto;
          border: 8px solid transparent;
          border-top-color: #07afef;
        }
      }
    }

    input[type="checkbox"] {
      display: none;
    }

    ul.multi-select-options {
      position: absolute;
      top: 3.8vh;
      left: 0;
      list-style: none;
      margin: 0;
      padding: 0;
      overflow: auto;
      border-radius: 1em;
      &::-webkit-scrollbar {
        display: none;
      }
      & > li {
        &:first-child {
          span {
            border-top: 1px solid #ddd;
          }
        }
        span {
          display: none;
          height: 3.6vh;
          width: auto;
          padding: 5px 10px;
          background: rgba(255, 255, 255, 0.5);
          border: 1px solid #ddd;
          border-top: 0;
          line-height: 3.6vh;
          cursor: pointer;
          &:after {
            content: "\002B";
            display: inline-block;
            font-size: 1.5em;
            font-weight: bold;
            position: absolute;
            right: 10px;
            z-index: 99;
            border-radius: 1ems;
          }
          &:hover {
            background: rgba(214, 214, 214, 0.5);
          }
        }
        input[type="checkbox"]:checked {
          & + span {
            background: rgba(164, 203, 234, 0.5);
            font-weight: bold;
            &:after {
              content: "\2212";
              font-size: 1.3em;
            }
          }
        }
      }
    }

    input#toggle-open {
      &:checked {
        & ~ .multi-select-options {
          height: 200px;
        }
      }
      &:not(:checked) {
        & ~ .multi-select-options {
          display: block;
          background: none;
          border: none;
          padding: 5px 0;
          input[type="checkbox"]:checked {
            & + span {
              display: block;
              float: left;
              margin: 0 0.5em 0.5em 0;
              padding: 0px 5px;
              width: auto;
              height: 2.4em;
              position: inherit;
              top: 0;
              border-top: 1px solid #ddd;
              line-height: 2.4em;
              border-radius: 1em;

              &:before {
                content: "\2A09";
                padding: 0 5px 0 0;
              }
              &:after {
                content: "";
                border-radius: 1em;
              }
            }
          }
        }
      }
    }
    #toggle-open:checked ~ .multi-select-options label,
    #toggle-open:checked ~ .multi-select-options span {
      display: block;
    }
  }
}
</style>