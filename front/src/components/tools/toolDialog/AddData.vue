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
                <use xlink:href="#icon-shiliangshujuji"></use>
              </svg>
              <div>矢量数据</div>
            </div>
            <div
              :class="flag === 2 ? 'type active' : 'type'"
              @click="clickType(2)"
            >
              <svg style="width: 60px; height: 60px">
                <use xlink:href="#icon-zhageyingxiang"></use>
              </svg>
              <div>栅格数据</div>
            </div>
          </div>
        </el-col>
        <el-col :span="9">
          <div class="right" v-if="flag === 1">
            <el-empty description="暂无数据" v-if="vector.total === 0" />
            <div v-else>
              <div
                v-for="(item, index) in vector.list"
                :key="index"
                class="data"
                @dblclick="dblclick(item, 'vector')"
              >
                <svg style="width: 20px; height: 20px">
                  <use xlink:href="#icon-vector"></use>
                </svg>
                <span>{{ item.name }}</span>
              </div>
              <el-pagination
                small
                layout="prev, pager, next"
                :total="vector.total"
                :pager-count="5"
                :page-size="vector.pageSize"
                :current-page="vector.currentPage"
                @current-change="vector.currentChange"
              />
            </div>
          </div>
          <div class="right" v-if="flag === 2">
            <el-empty description="暂无数据" v-if="raster.total === 0" />
            <div v-else>
              <div
                v-for="(item, index) in raster.list"
                :key="index"
                class="data"
                @dblclick="dblclick(item, 'raster')"
              >
                <svg style="width: 20px; height: 20px">
                  <use xlink:href="#icon-raster"></use>
                </svg>
                <span>{{ item.name }}</span>
              </div>
              <el-pagination
                small
                layout="prev, pager, next"
                :total="raster.total"
                :pager-count="5"
                :page-size="raster.pageSize"
                :current-page="raster.currentPage"
                @current-change="raster.currentChange"
              />
            </div>
          </div>
        </el-col>
        <el-col :span="9">
          <div class="selected">
            <el-scrollbar height="400px">
              <div
                v-for="(item, index) in result.list"
                :key="index"
                class="result"
              >
                <svg style="width: 20px; height: 20px">
                  <use :xlink:href="'#icon-' + item.type"></use>
                </svg>
                <span>{{ item.name }}</span>
                <div class="del" @dblclick="delDbclick">
                  <el-icon :size="20" color="#DD001B">
                    <delete />
                  </el-icon>
                </div>
              </div>
            </el-scrollbar>
          </div>
        </el-col>
      </el-row>
      <div style="text-align: center;">
        <el-button style="margin-top: 5px" type="primary" plain @click="commit">确定</el-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
interface PageResult {
  id?: number
  name: string;
  type: string;
  meta?: string;
  time?: string;
  show?: boolean
  tableName?: string
  vectorType?: string
}
import { defineComponent, onBeforeMount, reactive, ref } from "vue";
import { vectorPageQuery, rasterPageQuery } from "@/api/request";
import { useStore } from '@/store'
import { getCurrentProjectId } from '@/utils/project'
export default defineComponent({
  emits: ['returnData'],
  setup(_, context) {
    const store = useStore()
    const flag = ref(1);
    const input = ref("");
    const clickType = (num: number) => {
      flag.value = num;
    };
    const dblclick = (item: PageResult, type: string) => {
      let hasResult = false;
      console.log(item.name, item.id)
      result.list.forEach((e) => {
        if (
          e.type === item.type &&
          e.id === item.id
        )
          hasResult = true;
      });
      if (!hasResult) {
        if(item.type === 'vector') {
          item.show = true
        }
        result.list.push(item);
        result.list[result.list.length - 1].type = type;
      }
    };
    const delDbclick = (index: number) => {
      result.list.splice(index, 1)
    }

    const vector = reactive({
      total: 0,
      currentPage: 1,
      pageSize: 14,
      list: ref<Array<PageResult>>([]),
      currentChange: async (page: number) => {
        const data = await vectorPageQuery(vector.pageSize, page)
        if(data != null) {
          vector.total = (data.data as any).total
          vector.list = (data.data as any).list
          
        }
      },
    });

    const raster = reactive({
      total: 0,
      currentPage: 1,
      pageSize: 14,
      list: ref<Array<PageResult>>([]),
      currentChange: async (page: number) => {
        const data = await rasterPageQuery(raster.pageSize, page)
        if(data != null) {
          raster.total = (data.data as any).total
          raster.list = (data.data as any).list
        }
      },
    });

    const result = reactive({
      list: ref<Array<PageResult>>([]),
    });

    const commit = async () => {
      await store.dispatch("setResource", {projectJsonBean: {layerDataList: result.list, analyse: store.state.resource.analyse},  id: parseInt(getCurrentProjectId() as string)})
      context.emit('returnData')
    }

    onBeforeMount(async () => {
      store.state.resource.layerDataList.forEach(item => {
        result.list.push({
          name: item.name,
          id: item.id,
          type: item.type,
          show: item.show,
        })
      })
      let temp1 = await vectorPageQuery(
        vector.pageSize,
        vector.currentPage - 1
      );
      if (temp1 != null) {
        vector.total = (temp1.data as any).total;
        vector.list = (temp1.data as any).list
        vector.list.forEach(item => {
          item.type = 'vector'
        })
      }
      let temp2 = await rasterPageQuery(
        raster.pageSize,
        raster.currentPage - 1
      );
      if (temp2 != null) {
        raster.list = (temp2.data as any).list;
        raster.total = (temp2.data as any).total;
        raster.list.forEach(item => {
          item.type = 'raster'
        })
      }
    });

    return {
      flag,
      input,
      clickType,
      vector,
      raster,
      result,
      dblclick,
      delDbclick,
      commit
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
          background: rgba($color: #FDD35E, $alpha: 0.8);
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