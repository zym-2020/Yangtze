<template>
  <div class="tool-content">
    <div class="title">
      工具箱 <el-icon @click="closeClick"><Close /></el-icon>
    </div>
    <div class="body">
      <el-scrollbar>
        <el-collapse>
          <el-collapse-item name="1">
            <template #title>
              <svg style="width: 20px; height: 20px; margin-right: 10px">
                <use
                  xlink:href="#icon-gongjifenxitupugongjihangweiqushifenxi"
                ></use>
              </svg>
              <div>断面形态</div>
            </template>
            <div>
              <el-button type="primary" size="small">
                任意断面
              </el-button>
            </div>
          </el-collapse-item>
          <el-collapse-item title="Feedback" name="2">
            <div>
              Operation feedback: enable the users to clearly perceive their
              operations by style updates and interactive effects;
            </div>
            <div>
              Visual feedback: reflect current state by updating or rearranging
              elements of the page.
            </div>
          </el-collapse-item>
          <el-collapse-item title="Efficiency" name="3">
            <div>
              Simplify the process: keep operating process simple and intuitive;
            </div>
            <div>
              Definite and clear: enunciate your intentions clearly so that the
              users can quickly understand and make decisions;
            </div>
            <div>
              Easy to identify: the interface should be straightforward, which
              helps the users to identify and frees them from memorizing and
              recalling.
            </div>
          </el-collapse-item>
          <el-collapse-item title="Controllability" name="4">
            <div>
              Decision making: giving advices about operations is acceptable,
              but do not make decisions for the users;
            </div>
            <div>
              Controlled consequences: users should be granted the freedom to
              operate, including canceling, aborting or terminating current
              operation.
            </div>
          </el-collapse-item>
        </el-collapse>
      </el-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
export default defineComponent({
  emits: ["close"],
  setup(_, context) {
    const closeClick = () => {
      context.emit("close");
    };

    return {
      closeClick,
    };
  },
});
</script>

<style lang="scss" scoped>
.tool-content {
  height: 500px;
  width: 300px;
  background: rgba($color: #000000, $alpha: 0.5);
  border-radius: 6px;
  color: white;
  .title {
    height: 30px;
    box-sizing: border-box;
    border-bottom: solid 1px #d7d6d6;
    padding: 0 10px;
    line-height: 30px;
    position: relative;
    .el-icon {
      position: absolute;
      right: 5px;
      top: 5px;
      cursor: pointer;
    }
  }
  .body {
    .el-scrollbar {
      height: 470px;
      padding: 0 20px;
      .el-collapse {
        border-top: none;
        /deep/ .el-collapse-item__header {
          background: none;
          font-size: 16px;
          color: white;
          height: 40px;
        }

        /deep/ .el-collapse-item__wrap {
          background: none;
        }
        /deep/ .el-collapse-item__content {
          color: white;
        }
      }
    }
  }
}
</style>