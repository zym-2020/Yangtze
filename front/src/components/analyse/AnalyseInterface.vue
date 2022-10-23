<template>
  <div>
    <el-page-header :icon="ArrowLeft" @back="backClick">
      <template #content>
        <span> {{ title }} </span>
      </template>
    </el-page-header>

    <div class="analyse">
      <div class="introduce">
        <strong v-if="analyseType === 'section'"
          >断面形态是指随着断面起点距的变化河道高程的变化趋势，选择相应的河道DEM与断面数据，计算该断面形态</strong
        >
        <strong v-if="analyseType === 'sectionCompare'"
          >断面比较是指一条断面随着时间不同，河道地形发生的变化</strong
        >
        <strong v-if="analyseType === 'sectionFlush'"
          >断面冲於是通过当前年数据和参考年数据，对这个时间段内河道的冲刷和淤泥进行比较分析</strong
        >
        <strong v-if="analyseType === 'regionFlush'"
          >区域冲於是通过当前年数据和参考年数据，对这个时间段内河床区域的冲刷和淤泥进行比较分析</strong
        >
        <strong v-if="analyseType === 'elevationFlush'"
          >针对整个长江南京以下河段，选择参数生成特定高程冲淤</strong
        >
        <strong v-if="analyseType === 'flushContour'"
          >针对整个长江南京以下河段，选择参数生成冲淤等深线</strong
        >
        <strong v-if="analyseType === 'slope'"
          >选择河床参数，计算河床坡度</strong
        >
      </div>
      <el-select
        v-model="sectionValue"
        :placeholder="placeholder"
        v-if="selectFlag"
      >
        <el-option
          v-for="(item, index) in options"
          :key="index"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
      <div
        v-if="
          analyseType === 'section' ||
          analyseType === 'sectionCompare' ||
          analyseType === 'slope'
        "
      >
        <div
          class="text"
          v-if="analyseType === 'section' || analyseType === 'slope'"
        >
          选择DEM数据，DEM将默认添加至数据与图层中
        </div>
        <div class="text" v-if="analyseType === 'sectionCompare'">
          选择DEM数据，DEM将默认添加至数据与图层中，至少选择两个DEM数据
        </div>
        <el-skeleton :rows="5" animated v-if="skeletonFlag" />
        <div class="dem" v-else>
          <el-scrollbar>
            <el-tree :data="treeData" :props="defaultProps" default-expand-all>
              <template #default="{ data }">
                <div class="custom">
                  <div class="icon" v-if="data.flag">
                    <el-icon><FolderOpened /></el-icon>
                  </div>
                  <div class="text" v-if="data.flag">
                    <strong v-if="data.flag">{{ data.label }}</strong>
                  </div>
                  <el-checkbox
                    v-model="data.checkFlag"
                    size="large"
                    v-else
                    @change="changeClick(data)"
                    >{{ data.label }}</el-checkbox
                  >
                </div>
              </template>
            </el-tree>
          </el-scrollbar>
        </div>
      </div>

      <div
        v-if="
          analyseType === 'sectionFlush' ||
          analyseType === 'regionFlush' ||
          analyseType === 'elevationFlush' ||
          analyseType === 'flushContour'
        "
        class="section-flush"
      >
        <div class="left">
          <div class="text">选择基准面</div>
          <el-skeleton :rows="5" animated v-if="skeletonFlag" />
          <div class="dem" v-else>
            <el-scrollbar>
              <el-tree
                :data="benchmarkTreeData"
                :props="defaultProps"
                default-expand-all
              >
                <template #default="{ data }">
                  <div class="custom">
                    <div class="icon" v-if="data.flag">
                      <el-icon><FolderOpened /></el-icon>
                    </div>
                    <div class="text" v-if="data.flag">
                      <strong v-if="data.flag">{{ data.label }}</strong>
                    </div>
                    <el-checkbox
                      v-model="data.checkFlag"
                      size="large"
                      v-else
                      @change="changeClick(data, 'benchmark')"
                      >{{ data.label }}</el-checkbox
                    >
                  </div>
                </template>
              </el-tree>
            </el-scrollbar>
          </div>
        </div>
        <div class="right">
          <div class="text">选择参考面</div>
          <el-skeleton :rows="5" animated v-if="skeletonFlag" />
          <div class="dem" v-else>
            <el-scrollbar>
              <el-tree
                :data="referTreeData"
                :props="defaultProps"
                default-expand-all
              >
                <template #default="{ data }">
                  <div class="custom">
                    <div class="icon" v-if="data.flag">
                      <el-icon><FolderOpened /></el-icon>
                    </div>
                    <div class="text" v-if="data.flag">
                      <strong v-if="data.flag">{{ data.label }}</strong>
                    </div>
                    <el-checkbox
                      v-model="data.checkFlag"
                      size="large"
                      v-else
                      @change="changeClick(data, 'refer')"
                      >{{ data.label }}</el-checkbox
                    >
                  </div>
                </template>
              </el-tree>
            </el-scrollbar>
          </div>
        </div>
      </div>
      <div class="btn">
        <el-button type="primary" plain @click="btnClick">确定</el-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
type Section = {
  id: string;
  name: string;
};
type AnalyticDataset = {
  id: string;
  fileName: string;
  visualId: string;
  visualType: string;
};
type AnalyticParameter = {
  fileId: string;
  fileName: string;
  dataListId: string;
  dataListName: string;
  visualId: string;
  visualType: string;
};
type TreeData = {
  label: string;
  flag: boolean;
  children: TreeData[];
  id: string;
  checkFlag?: boolean;
  visualId?: string;
  visualType?: string;
  parentId?: string;
  parentName?: string;
};
import { computed, defineComponent, nextTick, onMounted, ref } from "vue";
import { ArrowLeft } from "@element-plus/icons-vue";
import { findByType } from "@/api/request";
import router from "@/router";
import { notice } from "@/utils/notice";
export default defineComponent({
  props: {
    analyseType: {
      type: String,
    },
    analyticDataList: {
      type: Array,
    },
  },
  emits: ["back", "returnParameter"],
  setup(props, context) {
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const title = computed(() => {
      if (props.analyseType === "section") {
        return "断面形态";
      } else if (props.analyseType === "sectionCompare") {
        return "断面比较";
      } else if (props.analyseType === "sectionFlush") {
        return "断面冲淤";
      } else if (props.analyseType === "regionFlush") {
        return "区域冲淤";
      } else if (props.analyseType === "elevationFlush") {
        return "特定高程冲淤";
      } else if (props.analyseType === "flushContour") {
        return "冲淤等深线";
      } else if (props.analyseType === "slope") {
        return "河床坡度提取";
      }
    });
    const placeholder = computed(() => {
      if (
        props.analyseType === "section" ||
        props.analyseType === "sectionCompare" ||
        props.analyseType === "sectionFlush"
      ) {
        return "选择断面";
      } else {
        return "选择区域";
      }
    });
    const selectFlag = computed(() => {
      if (
        props.analyseType === "elevationFlush" ||
        props.analyseType === "flushContour" ||
        props.analyseType === "slope"
      ) {
        return false;
      } else {
        return true;
      }
    });
    const analyticDataList = computed(() => {
      return props.analyticDataList as AnalyticDataset[];
    });
    const treeData = ref<TreeData[]>([]);
    const benchmarkTreeData = ref<TreeData[]>([]);
    const referTreeData = ref<TreeData[]>([]);
    const skeletonFlag = ref(true);

    const sectionValue = ref("");
    const options = ref<Section[]>([]);
    const sectionDem = ref<AnalyticParameter>();
    const sectionDemList = ref<AnalyticParameter[]>([]);
    const benchmarkDem = ref<AnalyticParameter>();
    const referDem = ref<AnalyticParameter>();

    const backClick = () => {
      context.emit("back");
    };

    const changeClick = (val: TreeData, type: string) => {
      if (props.analyseType === "section" || props.analyseType === "slope") {
        treeData.value.forEach((item) => {
          item.children.forEach((c) => {
            if (c.id != val.id) {
              c.checkFlag = false;
            } else {
              if (c.checkFlag) {
                sectionDem.value = {
                  fileId: c.id,
                  fileName: c.label,
                  dataListId: item.id,
                  dataListName: item.label,
                  visualId: c.visualId as string,
                  visualType: c.visualType as string,
                };
              } else {
                sectionDem.value = undefined;
              }
            }
          });
        });
      } else if (props.analyseType === "sectionCompare") {
        if (val.checkFlag) {
          sectionDemList.value.push({
            fileId: val.id,
            fileName: val.label,
            dataListId: val.parentId as string,
            dataListName: val.parentName as string,
            visualId: val.visualId as string,
            visualType: val.visualType as string,
          });
        } else {
          for (let i = 0; i < sectionDemList.value.length; i++) {
            if (sectionDemList.value[i].fileId === val.id) {
              sectionDemList.value.splice(i, 1);
              return;
            }
          }
        }
      } else if (
        props.analyseType === "sectionFlush" ||
        props.analyseType === "regionFlush" ||
        props.analyseType === "elevationFlush" ||
        props.analyseType === "flushContour"
      ) {
        if (type === "benchmark") {
          benchmarkTreeData.value.forEach((item) => {
            item.children.forEach((c) => {
              if (c.id != val.id) {
                c.checkFlag = false;
              } else {
                if (c.checkFlag) {
                  benchmarkDem.value = {
                    fileId: c.id,
                    fileName: c.label,
                    dataListId: item.id,
                    dataListName: item.label,
                    visualId: c.visualId as string,
                    visualType: c.visualType as string,
                  };
                } else {
                  benchmarkDem.value = undefined;
                }
              }
            });
          });
        } else if (type === "refer") {
          referTreeData.value.forEach((item) => {
            item.children.forEach((c) => {
              if (c.id != val.id) {
                c.checkFlag = false;
              } else {
                if (c.checkFlag) {
                  referDem.value = {
                    fileId: c.id,
                    fileName: c.label,
                    dataListId: item.id,
                    dataListName: item.label,
                    visualId: c.visualId as string,
                    visualType: c.visualType as string,
                  };
                } else {
                  referDem.value = undefined;
                }
              }
            });
          });
        }
      }
    };

    const btnClick = () => {
      if (props.analyseType === "section") {
        if (sectionValue.value != "" && sectionDem.value != undefined) {
          console.log(sectionDem.value);
          context.emit("returnParameter", {
            section: sectionValue.value,
            dem: sectionDem.value,
          });
        } else {
          notice(
            "warning",
            "警告",
            (sectionValue.value === "" ? "断面" : "DEM") + "不得为空"
          );
        }
      } else if (props.analyseType === "sectionCompare") {
        if (sectionValue.value != "" && sectionDemList.value.length >= 2) {
          context.emit("returnParameter", {
            section: sectionValue.value,
            demList: sectionDemList.value,
          });
        } else {
          notice(
            "warning",
            "警告",
            sectionValue.value === "" ? "不得为空" : "至少选择两个DEM数据"
          );
        }
      } else if (
        props.analyseType === "sectionFlush" ||
        props.analyseType === "regionFlush"
      ) {
        if (
          sectionValue.value != "" &&
          benchmarkDem.value != undefined &&
          referDem.value != undefined
        ) {
          if (referDem.value.fileId === benchmarkDem.value.fileId) {
            notice("warning", "警告", "基准DEM数据不能与参考DEM数据相同");
          } else {
            context.emit("returnParameter", {
              section: sectionValue.value,
              benchmarkDem: benchmarkDem.value,
              referDem: referDem.value,
            });
          }
        } else if (sectionValue.value === "") {
          if (props.analyseType === "sectionFlush") {
            notice("warning", "警告", "请选择断面");
          } else if (props.analyseType === "regionFlush") {
            notice("warning", "警告", "请选择区域");
          }
        } else if (benchmarkDem.value === undefined) {
          notice("warning", "警告", "基准DEM数据不得为空");
        } else {
          notice("warning", "警告", "参考DEM数据不得为空");
        }
      } else if (
        props.analyseType === "elevationFlush" ||
        props.analyseType === "flushContour"
      ) {
        if (benchmarkDem.value != undefined && referDem.value != undefined) {
          if (referDem.value.fileId === benchmarkDem.value.fileId) {
            notice("warning", "警告", "基准DEM数据不能与参考DEM数据相同");
          } else {
            context.emit("returnParameter", {
              benchmarkDem: benchmarkDem.value,
              referDem: referDem.value,
            });
          }
        } else {
          notice(
            "warning",
            "警告",
            (benchmarkDem.value === undefined ? "基准DEM" : "参考DEM") +
              "数据不得为空"
          );
        }
      } else if (props.analyseType === "slope") {
        if (sectionDem.value != undefined) {
          context.emit("returnParameter", sectionDem.value);
        } else {
          notice("warning", "警告", "请选择dem数据");
        }
      }
    };

    const getParame = async (type: string) => {
      const data = await findByType("dem");
      if (data != null && (data as any).code === 0) {
        for (let i = 0; i < data.data.length; i++) {
          let flag = true;
          for (let j = 0; j < treeData.value.length; j++) {
            if (treeData.value[j].id === data.data[i].dataListId) {
              treeData.value[j].children.push({
                id: data.data[i].fileId,
                label: data.data[i].fileName,
                flag: false,
                children: [],
                checkFlag: false,
                visualId: data.data[i].visualId,
                visualType: data.data[i].visualType,
                parentId: data.data[i].dataListId,
                parentName: data.data[i].dataListName,
              });
              flag = false;
              break;
            }
          }
          if (flag) {
            treeData.value.push({
              id: data.data[i].dataListId,
              label: data.data[i].dataListName,
              flag: true,
              children: [],
            });
            treeData.value[treeData.value.length - 1].children.push({
              id: data.data[i].fileId,
              label: data.data[i].fileName,
              flag: false,
              checkFlag: false,
              children: [],
              visualId: data.data[i].visualId,
              visualType: data.data[i].visualType,
              parentId: data.data[i].dataListId,
              parentName: data.data[i].dataListName,
            });
          }
        }
        if (
          type === "sectionFlush" ||
          type === "regionFlush" ||
          type === "elevationFlush" ||
          type === "flushContour"
        ) {
          benchmarkTreeData.value = JSON.parse(JSON.stringify(treeData.value));
          referTreeData.value = JSON.parse(JSON.stringify(treeData.value));
        }
        if (
          type === "section" ||
          type === "sectionCompare" ||
          type === "sectionFlush"
        ) {
          options.value = [];
          analyticDataList.value.forEach((item) => {
            if (item.visualType === "geoJsonLine") {
              options.value.push({
                id: item.id,
                name: item.fileName,
              });
            }
          });
        } else if (type === "regionFlush") {
          options.value = [];
          analyticDataList.value.forEach((item) => {
            if (item.visualType === "geoJsonPolygon") {
              options.value.push({
                id: item.id,
                name: item.fileName,
              });
            }
          });
        }
      }
    };

    onMounted(async () => {
      skeletonFlag.value = true;
      await getParame(props.analyseType as string);

      skeletonFlag.value = false;
    });

    return {
      ArrowLeft,
      title,
      analyticDataList,
      backClick,
      sectionValue,
      options,
      treeData,
      benchmarkTreeData,
      referTreeData,
      defaultProps,
      changeClick,
      btnClick,
      skeletonFlag,
      placeholder,
      selectFlag,
    };
  },
});
</script>

<style lang="scss" scoped>
.introduce {
  margin-top: 30px;
  margin-left: 5px;
  margin-bottom: 30px;
}
.analyse {
  .el-select {
    width: 100%;
  }
  .text {
    height: 20px;
    line-height: 20px;
    margin: 5px 0;
  }
  .dem {
    border: solid #dcdfe6 1px;
    height: 200px;
    border-radius: 4px;
    cursor: pointer;
    .custom {
      display: flex;
      .icon {
        width: 15px;
        margin-top: 6px;
        margin-right: 5px;
      }
    }
    .el-tree {
      /deep/ .el-tree-node__content {
        height: 40px;
      }
    }
  }
  .section-flush {
    display: flex;
    .left,
    .right {
      width: 50%;
    }
    .left {
      margin-right: 10px;
    }
  }
  .btn {
    text-align: center;
    margin-top: 10px;
  }
}
</style>