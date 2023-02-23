<template>
  <div class="head-notice">
    <div class="left"></div>
    <div class="head-notice-body">
      <div class="content">
        <div v-for="(item, index) in content" :key="index">
          <div class="img">
            <div>
              <img
                :src="
                  prefix + 'multiSource/getMeteorologyPng/' + item.type + '.png'
                "
              />
            </div>
            <div class="text">
              <span v-html="replaceHandle(item.title)"></span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="right-zym"></div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, nextTick, onMounted, ref } from "vue";
import { prefix } from "@/prefix";
export default defineComponent({
  props: {
    content: {
      type: Array,
    },
  },
  setup(props) {
    const content = computed(() => {
      return props.content;
    });

    const width = computed(() => {
      if (props.content) {
        return props.content?.length * -500 + "px";
      } else {
        return "";
      }
    });

    const time = computed(() => {
      //   return (
      //     (((props.content as any).length * 500) / document.body.clientWidth - 100) / 10 +
      //     "s"
      //   );
      return ((props.content as any).length * 500) / 50 + "s";
    });

    const replaceHandle = (currentStr: string) => {
      const res = new RegExp("(发布)", "g");
      currentStr = currentStr.replace(
        res,
        "<span style='color:red; margin: 0px 5px'>发布</span>"
      );
      return currentStr;
    };

    return {
      content,
      replaceHandle,
      prefix,
      time,
      width,
    };
  },
});
</script>

<style lang="scss" scoped>
@keyframes move {
  0% {
    left: 100%;
  }

  100% {
    left: v-bind("width");
  }
}

.head-notice {
  z-index: 99;
  width: 100%;
  height: 50px;
  display: flex;
  .left,
  .right-zym {
    width: 50px;
    height: 50px;
    background:white;
  }
  .head-notice-body {
    padding: 10px 0px;
    width: calc(100% - 100px);
    height: 30px;
    background: white;
    overflow: hidden;

    position: relative;

    .content {
      display: flex;
      flex-shrink: 0;
      // transform: translateX(-300%);
      position: absolute;

      animation: v-bind("time") move infinite linear;
      // left: 100%;
      .img {
        display: flex;
        height: 100%;
        line-height: 30px;
        margin-right: 100px;
        img {
          height: 30px;
          width: 40px;
          margin-right: 10px;
        }
        .text {
          white-space: nowrap;
        }
      }
    }
  }
}
</style>