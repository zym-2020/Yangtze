<template>
  <div class="main">
    <div class="title">添加数据</div>
    <div class="body">
      <el-input
        v-model="input"
        size="small"
        placeholder="搜索"
        suffix-icon="Search"
      />
      <el-row :gutter="10">
        <el-col :span="4" :offset="1">
          <div class="left">
            <div
              :class="flag === 1 ? 'type active' : 'type'"
              @click="clickType(1)"
            >
              <svg style="width: 60px; height: 60px">
                <use xlink:href="#icon-zhageyingxiang"></use>
              </svg>
              <div>河床数据</div>
            </div>
            <div
              :class="flag === 2 ? 'type active' : 'type'"
              @click="clickType(2)"
            >
              <svg style="width: 60px; height: 60px">
                <use xlink:href="#icon-shiliangshujuji"></use>
              </svg>
              <div>水文数据</div>
            </div>
            <div
              :class="flag === 3 ? 'type active' : 'type'"
              @click="clickType(3)"
            >
              <svg style="width: 60px; height: 60px">
                <use xlink:href="#icon-yaogancehui"></use>
              </svg>
              <div>影像数据</div>
            </div>
          </div>
        </el-col>
        <el-col :span="9">
          <div class="right" v-if="flag === 1">
            <el-empty description="暂无数据" v-if="riverBedData.length === 0" />
            <div v-else>
              <div
                v-for="(item, index) in riverBedData"
                :key="index"
                class="data"
                @dblclick="dblclick(item)"
              >
                <svg style="width: 20px; height: 20px">
                  <use xlink:href="#icon-raster"></use>
                </svg>
                <span>{{ item.name }}</span>
              </div>
            </div>
          </div>
          <div class="right" v-if="flag === 2">
            <el-empty
              description="暂无数据"
              v-if="hydrologyData.length === 0"
            />
            <div v-else>
              <div
                v-for="(item, index) in hydrologyData"
                :key="index"
                class="data"
                @dblclick="dblclick(item)"
              >
                <svg style="width: 20px; height: 20px">
                  <use xlink:href="#icon-vector"></use>
                </svg>
                <span>{{ item.name }}</span>
              </div>
            </div>
          </div>
          <div class="right" v-if="flag === 3">
            <el-empty
              description="暂无数据"
              v-if="satelliteData.length === 0"
            />
            <div v-else>
              <div
                v-for="(item, index) in satelliteData"
                :key="index"
                class="data"
                @dblclick="dblclick(item)"
              >
                <svg style="width: 20px; height: 20px; margin-left: 5px">
                  <use xlink:href="#icon-tiff"></use>
                </svg>
                <span>{{ item.name }}</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="9">
          <div class="selected">
            <el-scrollbar height="400px">
              <div v-for="(item, index) in result" :key="index" class="result">
                <svg style="width: 20px; height: 20px; margin-left: 5px;">
                  <use :xlink:href="getIcon(item.type)"></use>
                </svg>
                <span>{{ item.name }}</span>
                <div class="del" @dblclick="delDbclick(index)">
                  <el-icon :size="20" color="#DD001B">
                    <delete />
                  </el-icon>
                </div>
              </div>
            </el-scrollbar>
          </div>
        </el-col>
      </el-row>
      <div style="text-align: center">
        <el-button style="margin-top: 5px" type="primary" plain @click="commit"
          >确定</el-button
        >
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import { findDataByType } from "@/api/request";
import { useStore } from "@/store";
import router from "@/router";
export default defineComponent({
  emits: ["returnData"],
  props: {
    layers: {
      type: Array
    }
  },
  setup(props, context) {
    const store = useStore();
    const flag = ref(1);
    const input = ref("");
    const riverBedData = ref<any[]>([]);
    const hydrologyData = ref<any[]>([]);
    const satelliteData = ref<any[]>([])
    const result = ref<any[]>([]);
    const projectId = computed(() => {
      return router.currentRoute.value.params.id;
    });

    const dblclick = (item: any) => {
      let hasResult = false;
      console.log(item);
      result.value.forEach((e) => {
        if (item.id === e.id) {
          hasResult = true;
        }
      });
      if (!hasResult) {
        result.value.push(item);
      }
    };

    const delDbclick = (index: number) => {
      result.value.splice(index, 1);
    };

    const clickType = (num: number) => {
      flag.value = num;
    };

    const getIcon = (type: string) => {
      if (type === "riverBed") {
        return "#icon-raster";
      } else if (type === "hydrology") {
        return "#icon-vector";
      } else if (type === 'satellite') {
        return "#icon-tiff"
      }
    };

    const commit = async () => {
      const layerDataList: any[] = [];
      result.value.forEach((item) => {
        layerDataList.push({
          id: item.id,
          name: item.name,
          type: item.type,
          show: item.has_tiles,
        });
      });
      const tempString: string[] = []
      props.layers?.forEach((item: any) => {
        tempString.push(item.id)
      })
      store.commit("SET_TEMP_LAYERS", tempString)
      console.log("tempString", tempString)
      console.log("layers", props.layers)
      await store.dispatch("setResource", {
        projectJsonBean: {
          layerDataList: layerDataList,
          analyse: store.state.resource.analyse,
        },
        id: projectId.value as string,
      });
      context.emit("returnData");
    };

    onMounted(async () => {
      store.state.resource.layerDataList.forEach((item) => {
        result.value.push({
          name: item.name,
          id: item.id,
          type: item.type,
          has_tiles: item.show,
        });
      });

      const data1 = await findDataByType("riverBed");
      if (data1 != null) {
        if ((data1 as any).code === 0) {
          console.log(data1);
          riverBedData.value = data1.data;
        }
      }
      const data2 = await findDataByType("hydrology");
      if (data2 != null) {
        if ((data2 as any).code === 0) {
          console.log(data2);
          hydrologyData.value = data2.data;
        }
      }
      const data3 = await findDataByType('satellite') 
      if (data3 != null) {
        if((data3 as any).code === 0) {
          satelliteData.value = data3.data
        }
      }
    });

    return {
      flag,
      input,
      clickType,
      result,
      dblclick,
      delDbclick,
      commit,
      riverBedData,
      hydrologyData,
      satelliteData,
      getIcon,
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  height: 500px;
  padding: 20px 10px 10px 10px;
  width: 100%;
  background: #a6bed7;

  .title {
    height: 20px;
    line-height: 20px;
    font-weight: 700;
  }
  .body {
    height: calc(100% - 20px);
    background: #f0f0f0;
    .left {
      height: 400px;
      background: #fffbf7;
      margin-top: 10px;
      .type {
        text-align: center;
        height: 100px;
        svg {
          margin-top: 10px;
        }
        &:hover {
          cursor: pointer;
          background: rgba($color: #d7ba7d, $alpha: 0.3);
        }
      }
      .active {
        background: rgba($color: #d7ba7d, $alpha: 0.5);
        box-shadow: inset 0 3px 5px 5px rgba($color: #d7ba7d, $alpha: 1);
      }
    }
    .right {
      background: #ffffff;
      margin-top: 10px;
      height: 400px;
      border: solid 1px black;
      position: relative;
      .data {
        height: 25px;
        svg {
          position: relative;
          top: 5px;
          margin-right: 5px;
        }
        &:hover {
          cursor: pointer;
          background: rgba($color: #25ee2a, $alpha: 0.2);
        }
      }
      .el-pagination {
        position: absolute;
        bottom: 5px;
        /deep/ .number {
          background: #ffffff;
        }
        /deep/ .btn-prev {
          background: #ffffff;
        }
        /deep/ .btn-next {
          background: #ffffff;
        }
      }
    }
    .selected {
      background: #ffffff;
      margin-top: 10px;
      height: 400px;
      border: solid 1px black;

      .result {
        height: 25px;
        position: relative;
        svg {
          position: relative;
          top: 5px;
          margin-right: 5px;
        }
        .del {
          height: 25px;
          width: 100%;
          background: rgba($color: #fdd35e, $alpha: 0.8);
          position: absolute;
          z-index: 99;
          top: 0px;
          left: 0px;
          opacity: 0;
          text-align: center;
          &:hover {
            opacity: 1;
            cursor: pointer;
          }
        }
      }
    }
  }
}
</style>