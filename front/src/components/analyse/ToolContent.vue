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
              <el-button type="primary" size="small" @click="sectionByDIYClick">
                任意断面
              </el-button>
              <el-button type="success" size="small">文本输入</el-button>
            </div>
          </el-collapse-item>
          <el-collapse-item name="2">
            <template #title>
              <svg style="width: 20px; height: 20px; margin-right: 10px">
                <use xlink:href="#icon-bijiaotu"></use>
              </svg>
              <div>区域形态</div>
            </template>
            <div>
              <el-button type="primary" size="small"> 任意区域 </el-button>
              <el-button type="success" size="small">文本输入</el-button>
            </div>
          </el-collapse-item>
          <el-collapse-item name="3">
            <template #title>
              <svg style="width: 20px; height: 20px; margin-right: 10px">
                <use xlink:href="#icon-podupoxiangfenxi"></use>
              </svg>
              <div>坡度提取</div>
            </template>
            <div class="select">
              <el-select v-model="value" placeholder="选择dem图层" size="small">
                <el-option
                  v-for="(item, index) in options"
                  :key="index"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </div>
            <el-button type="primary" size="small" @click="slopeClick">
              坡度计算
            </el-button>
          </el-collapse-item>
          <el-collapse-item name="4">
            <template #title>
              <svg style="width: 20px; height: 20px; margin-right: 10px">
                <use xlink:href="#icon-fsux_tubiao_zhutiheliutu"></use>
              </svg>
              <div>深泓线</div>
            </template>
            <el-button type="primary" size="small">深泓线提取</el-button>
            <el-button type="success" size="small">深泓线对比</el-button>
          </el-collapse-item>
          <el-collapse-item name="5">
            <template #title>
              <svg style="width: 20px; height: 20px; margin-right: 10px">
                <use xlink:href="#icon-fsux_tubiao_denggaoxiantu"></use>
              </svg>
              <div>等深线</div>
            </template>
            <div class="select">
              <el-select
                v-model="contour"
                placeholder="请选择数据集"
                size="small"
              >
                <el-option
                  v-for="(item, index) in options"
                  :key="index"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </div>
            <div class="input-number">
              间隔：
              <el-input-number
                v-model="num"
                :precision="2"
                :step="0.1"
                :max="20"
                :min="5"
                size="small"
              />
            </div>
            <el-button type="primary" size="small" @click="contourClick">等深线提取</el-button>
          </el-collapse-item>
          <el-collapse-item name="6">
            <template #title>
              <svg style="width: 20px; height: 20px; margin-right: 10px">
                <use xlink:href="#icon-icon"></use>
              </svg>
              <div>河床冲淤</div>
            </template>
            <div class="select">
              <el-select v-model="benchmark" placeholder="基准年" size="small">
                <el-option
                  v-for="(item, index) in options"
                  :key="index"
                  :label="item.name"
                  :value="item.id"
                  :disabled="item.id == reference"
                />
              </el-select>
            </div>
            <div class="select">
              <el-select v-model="reference" placeholder="比较年" size="small">
                <el-option
                  v-for="(item, index) in options"
                  :key="index"
                  :label="item.name"
                  :value="item.id"
                  :disabled="item.id == benchmark"
                />
              </el-select>
            </div>
            <el-button type="primary" size="small" @click="flushSilt"
              >冲淤计算</el-button
            >
            <el-button type="success" size="small">冲淤等深线</el-button>
          </el-collapse-item>
        </el-collapse>
      </el-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from "vue";
export default defineComponent({
  props: {
    demLayers: {
      type: Array,
    },
  },
  emits: ["close", "sectionByDIYClick", "slopeClick", "flushSilt", "contourClick"],
  setup(props, context) {
    const value = ref("");
    const benchmark = ref("");
    const reference = ref("");
    const contour = ref("");
    const num = ref(10);

    const options = computed(() => {
      return props.demLayers;
    });

    const closeClick = () => {
      context.emit("close");
    };

    const sectionByDIYClick = () => {
      context.emit("sectionByDIYClick");
    };

    const flushSilt = () => {
      const params = {
        benchmark: {
          id: benchmark.value,
          name: "",
        },
        reference: {
          id: reference.value,
          name: "",
        },
      };
      props.demLayers?.forEach((item: any) => {
        if (item.id === params.benchmark.id) {
          params.benchmark.name = JSON.parse(
            JSON.stringify(item.name)
          ).substring(0, 6);
        }
        if (item.id === params.reference.id) {
          params.reference.name = JSON.parse(
            JSON.stringify(item.name)
          ).substring(0, 6);
        }
      });
      context.emit("flushSilt", params);
    };

    const slopeClick = () => {
      const val = {
        id: value.value,
        name: "",
      };
      props.demLayers?.forEach((item: any) => {
        if (item.id === value.value) {
          val.name = item.name;
        }
      });
      if (value.value != "") {
        context.emit("slopeClick", val);
      }
    };

    const contourClick = () => {
      const param = {
        id: contour.value,
        interval: num.value,
        name: ""
      }
      props.demLayers?.forEach((item: any) => {
        if(item.id === contour.value) {
          param.name = item.name
        }
      })
      context.emit("contourClick", param)
    }

    return {
      closeClick,
      sectionByDIYClick,
      slopeClick,
      value,
      benchmark,
      reference,
      options,
      flushSilt,
      contourClick,
      contour,
      num,
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

        .select, .input-number {
          margin-bottom: 5px;
        }
      }
    }
  }
}
</style>