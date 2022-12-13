<template>
  <div class="scene-map-wrapper3">
    <div id="box">
      <div id="search">
        <el-button @click="commit()">检索</el-button>
      </div>
      <div id="reset">
        <el-button @click="reset()">重置</el-button>
      </div>
      <div>
        <el-row>
          <el-col :span="4">
            <div id="data">资源条目:</div>
          </el-col>
          <el-col :span="20">
            <div id="opendig">
              <el-button text @click="dialogUploadFlag = true"
                >选择资源集合</el-button
              >
              <div style="display: inline; margin-left: 20px">
                <el-tag
                  v-if="dataSelect"
                  style="
                    margin-right: 8px;
                    background-color: rgba(21, 69, 153, 0.8);
                    color: white;
                  "
                  effect="dark"
                  round
                >
                  {{ dataSelect }}
                </el-tag>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div id="clock">
        <el-row>
          <el-col :span="4">
            <div id="text">时间范围:</div>
          </el-col>
          <el-col :span="20">
            <!-- 日期选择器 -->
            <el-date-picker
              v-model="queryParams.begintime"
              type="daterange"
              unlink-panels
              :start-placeholder="starDate"
              :end-placeholder="endDate"
              :shortcuts="shortcuts"
              value-format="YYYY-MM-DD"
              :disabled-date="disabledDate"
              @calendar-change="handleChange"
              @focus="handleFocus"
            />
          </el-col>
        </el-row>
      </div>
      <div id="data-size">
        <el-row>
          <el-col :span="4">
            <div style="text-align: center; margin-top: 5px">数据标签:</div>
          </el-col>
          <el-col :span="20">
            <el-select
              v-model="mapJsonData.tags"
              multiple
              placeholder="标签"
              size="large"
              style="width: 300px"
            >
              <el-option-group
                v-for="(group, groupIndex) in optionTag"
                :key="groupIndex"
                :label="group.title"
              >
                <el-option
                  v-for="(item, index) in group.data"
                  :key="index"
                  :label="item.name"
                  :value="item.name"
                />
              </el-option-group>
            </el-select>
          </el-col>
        </el-row>
      </div>
      <div id="data-visual">
        <el-row>
          <el-col :span="4">
            <div style="text-align: center; margin-top: 5px">可视化类型:</div>
          </el-col>
          <el-col :span="20">
            <div class="mt-4">
              <el-checkbox v-model="checked1" label="栅格" border />
              <el-checkbox v-model="checked2" label="矢量" border />
              <el-checkbox v-model="checked3" label="文本" border />
              <el-checkbox v-model="checked4" label="图片" border />
              <el-checkbox v-model="checked5" label="遥感影像" border />
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
    <div id="des">
      <el-scrollbar max-height="550px">
        <div v-for="(item, index) in dataSearch" :key="index">
          <MapDescription :fileInfo="item" :key="new Date().getTime()">
          </MapDescription>
        </div>
      </el-scrollbar>
    </div>
    <div id="mapdialog">
      <el-dialog
        v-model="dialogUploadFlag"
        width="20%"
        :show-close="false"
        title="请选择数据，最多一条"
      >
        <MapDialog
          @commitFile="dialogUploadFlag = false"
          @commitData="commitData"
        >
        </MapDialog>
      </el-dialog>
    </div>
    <div id="map3"></div>
  </div>
</template>
  
  <script lang="ts">
import { defineComponent, onMounted, ref, reactive } from "vue";
import "@mapbox/mapbox-gl-draw/dist/mapbox-gl-draw.css";
import MapDialog from "@/views/user/components/MapDialog.vue";
import MapDescription from "@/views/user/components/MapDescription.vue";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import { fuzzyQueryDataList, clearQueryDataList } from "@/api/request";
import mapBoxGl from "mapbox-gl";
import { notice } from "@/utils/notice";
import { ElMessageBox, ElMessage, ElNotification } from "element-plus";
import {
  Check,
  Delete,
  Edit,
  Message,
  Search,
  Star,
} from '@element-plus/icons-vue'
import mapboxgl from "mapbox-gl";
import { emit } from "process";
export default defineComponent({
  name: "FindMap",
  components: {
    MapDialog,
    MapDescription,
  },

  setup(props, { emit }) {
    // 日期选择器默认开始时间-上周年月日
    let starDate = new Date();
    starDate.setTime(starDate.getTime() - 3600 * 1000 * 24 * 7);
    starDate = getNewTime(starDate) as unknown as Date;
    // 日期选择器默认结束时间-当前年月日
    let endDate = getNewTime(new Date());
    let jsonDatass = {
      page: 0,
      size: 100,
      titleKeyword: "",
      property: "id",
      flag: false,
      type: "",
    };
    //判断是否显示资源选择界面
    const dialogUploadFlag = ref(false);
    //选择的资源条目
    const dataSelect = ref("");
    //矢量、栅格、Excel、照片
    const checked1 = ref(false);
    const checked2 = ref(false);
    const checked3 = ref(false);
    const checked4 = ref(false);
    const checked5 = ref(false);
    //
    const dataValue = ref("");
    const timeValue = ref<any>([]);
    const dataSearch = ref<any[]>([]);
    //查询JsonData
    const mapJsonData = reactive({
      tags: [],
      type: "",
      location: "",
      startDate: "",
      endDate: "",
    });
    //可选的数据标签（已更新）
    const optionTag = ref([
      {
        title: "时间",
        data: [
          {
            name: "2002以前",
            count: false,
          },
          {
            name: "2002~2012",
            count: false,
          },
          {
            name: "2013~2022",
            count: false,
          },
        ],
      },
      {
        title: "范围",
        data: [
          {
            name: "长江区域",
            count: false,
          },
          {
            name: "南京区域",
            count: false,
          },
        ],
      },
      {
        title: "地点",
        data: [
          {
            name: "白茆小沙",
            count: false,
          },
          {
            name: "福中+福北",
            count: false,
          },
          {
            name: "横港沙",
            count: false,
          },
          {
            name: "黄铁沙",
            count: false,
          },
          {
            name: "护漕港边滩",
            count: false,
          },
          {
            name: "沪通大桥",
            count: false,
          },
          {
            name: "江阴大桥",
            count: false,
          },
          {
            name: "苏通大桥",
            count: false,
          },
          {
            name: "双涧沙",
            count: false,
          },
          {
            name: "通白",
            count: false,
          },
          {
            name: "通州沙",
            count: false,
          },
          {
            name: "民主沙",
            count: false,
          },
          {
            name: "福姜沙",
            count: false,
          },
          {
            name: "新开沙",
            count: false,
          },
          {
            name: "西水道",
            count: false,
          },
        ],
      },

      {
        title: "文件格式",
        data: [
          {
            name: "shp",
            count: false,
          },
          {
            name: "dwg",
            count: false,
          },
          {
            name: "txt",
            count: false,
          },
          {
            name: "jpg",
            count: false,
          },
          {
            name: "excel",
            count: false,
          },
        ],
      },
      {
        title: "文件性质",
        data: [
          {
            name: "栅格文件",
            count: false,
          },
          {
            name: "矢量文件",
            count: false,
          },
          {
            name: "文本数据",
            count: false,
          },
          {
            name: "图片",
            count: false,
          },
          {
            name: "遥感影像",
            count: false,
          },
        ],
      },
    ]);
    // 日期选择器快捷方式
    const shortcuts = [
      {
        text: "上周",
        value: () => {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
          return [start, end];
        },
      },
      {
        text: "上月",
        value: () => {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
          return [start, end];
        },
      },
      {
        text: "过去三个月",
        value: () => {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
          return [start, end];
        },
      },
      {
        text: "过去一年",
        value: () => {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 365);
          return [start, end];
        },
      },
    ];
    // 查询条件
    const queryParams = reactive({
      begintime: null,
    });
    // 当前选择的开始日期
    const pickDay = ref();
    // 用来存放坐标信息
    const coorList = ref<any>([]);

    // 获取当前年月日的函数
    function getNewTime(date: any) {
      let y = date.getFullYear(); //年
      let m = date.getMonth() + 1; //月，月是从0开始的所以+1
      let d = date.getDate(); //日
      m = m < 10 ? "0" + m : m; //小于10补0
      d = d < 10 ? "0" + d : d; //小于10补0
      return y + "-" + m + "-" + d; //返回时间形式yyyy-mm-dd
    }
    // 时间选择器禁用范围
    const disabledDate = (time: any) => {
      if (!pickDay.value) {
        return false;
      } else {
        const con1 =
          new Date(pickDay.value).getTime() - 1 * 24 * 60 * 60 * 1000;
        const con2 =
          new Date(pickDay.value).getTime() + 1000 * 24 * 60 * 60 * 1000;
        return time < con1 || time > con2;
      }
    };
    // 日期选择器选择时触发
    function handleChange(val: any) {
      const [pointDay] = val;
      pickDay.value = pointDay;
      timeValue.value = val;
      mapJsonData.startDate = String(formatDate(timeValue.value[0]));
      if (timeValue.value[1] != undefined)
        mapJsonData.endDate = String(formatDate(timeValue.value[1]));
    }
    //格式化时间
    function formatDate(date: any) {
      const year = date.getFullYear();
      let month = date.getMonth() + 1;
      let day = date.getDate();
      if (month < 10) {
        month = `0${month}`;
      }
      if (day < 10) {
        day = `0${day}`;
      }
      return `${year}-${month}-${day}`;
    }

    // 日期选择器获得焦点时触发
    function handleFocus() {
      pickDay.value = null;
    }
    //从资源选择对话框中选择的资源条目
    const commitData = (val: string) => {
      dataSelect.value = val;
      mapJsonData.type = dataSelect.value;
    };
    //检索
    const commit = async () => {
      if (mapJsonData.location != "") {
        if (queryParams.begintime == null) {
          mapJsonData.startDate = "";
          mapJsonData.endDate = "";
        }

        const data = await clearQueryDataList(mapJsonData);
        dataSearch.value = data.data.list;
        ElNotification({
          title: "检索成功",
          type: "success",
          offset: 80,
          position: "top-right",
        });
      } else {
        ElNotification({
          title: "未选定空间位置",
          type: "error",
          offset: 80,
          position: "top-right",
        });
      }
    };
    //重置初始化所有选项
    const reset = () => {
      checked1.value = false;
      checked2.value = false;
      checked3.value = false;
      checked4.value = false;
      timeValue.value = [];
      queryParams.begintime = null;
      dataSelect.value = "";
      mapJsonData.tags = [];
    };
    onMounted(async () => {
      const data = await fuzzyQueryDataList(jsonDatass);
      dataSearch.value = data.data.list;
      const map = ref<mapBoxGl.Map>();
      mapboxgl.accessToken =
        "pk.eyJ1IjoiMTY2NTE2OTkzNzYiLCJhIjoiY2ttMDh5amJpMHE2dzJ3cTd5eWZsMGQxZyJ9.XErH3kSOuRC_OWXWCpDLkQ";
      map.value = new mapboxgl.Map({
        container: "map3",
        style: "mapbox://styles/16651699376/ckmpu8kuk0h8r17msqpz351vf",
        center: [121.18, 31.273],
        zoom: 8,
      });
      //控件
      map.value?.addControl(new mapboxgl.NavigationControl());
      //自定义绘制面
      const polygonDraw = new MapboxDraw({
        controls: {
          combine_features: false,
          uncombine_features: false,
          trash: true,
          point: false,
          line_string: false,
        },
      });
      //绘制事件
      const updateArea = function (e: any) {
        if (e.type === "draw.create") {
          //图形绘制完成
          emit("getCoor", e.features[0].geometry.coordinates[0]);
        } else if (e.type === "draw.update") {
          //修改绘制图形后
          emit("getCoor", e.features[0].geometry.coordinates[0]);
        } else if (e.type === "draw.delete") {
          emit("getCoor", e.features[0].geometry.coordinates[0]);
          console.log(e.features[0].geometry.coordinates[0]);
          //删除绘制图形
        }
        let arr = [];
        mapJsonData.location = "";
        for (let i = 0; i < e.features[0].geometry.coordinates[0].length; i++) {
          arr.push(e.features[0].geometry.coordinates[0][i][0]);
          arr.push(e.features[0].geometry.coordinates[0][i][1]);
        }
        if (coorList.value.toString() == arr.toString()) {
          mapJsonData.location = "";
        } else {
          coorList.value = arr;
          mapJsonData.location = coorList.value.toString();
        }
      };
      map.value.addControl(polygonDraw, "top-right");
      map.value.on("draw.create", updateArea);
      map.value.on("draw.delete", updateArea);
      map.value.on("draw.update", updateArea);
    });
    return {
      checked1,
      checked2,
      checked3,
      checked4,
      checked5,
      dataSearch,
      dataSelect,
      dataValue,
      timeValue,
      optionTag,
      starDate,
      endDate,
      shortcuts,
      queryParams,
      dialogUploadFlag,
      mapJsonData,
      coorList,
      commitData,
      commit,
      reset,
      disabledDate,
      handleChange,
      handleFocus,
      formatDate,
    };
  },
});
</script>
  
  <style lang="scss" scoped>
div.scene-map-wrapper3 {
  cursor: pointer;
  background-color: transparent;
  width: 52%;
  height: 63%;

  div#box {
    width: 680px;
    height: 250px;
    top: 100px;
    left: 20px;
    background-color: rgba(33, 37, 41, 0.6);
    position: absolute;
    z-index: 20;
    color: white;
    div#search {
      position: absolute;
      top: 50px;
      left: 525px;
      text-align: center;
      display: inline;

      /deep/.el-button {
        width: 80px;
        height: 80px;
        > span {
          font-size: 18px;
        }
      }
    }
    div#reset {
      position: absolute;
      top: 140px;
      left: 525px;
      text-align: center;
      display: inline;
      /deep/.el-button {
        width: 80px;
        height: 30px;
      }
    }
    div#data {
      position: relative;
      top: 25px;
      text-align: center;
    }
    div#opendig {
      position: relative;
      top: 20px;
    }
    div#clock {
      position: relative;
      top: 40px;
    }
    div#text {
      margin-top: 5px;
      text-align: center;
    }
    div#data-size {
      position: relative;
      top: 60px;
    }
    div#data-visual {
      position: relative;
      top: 81px;
      /deep/.el-checkbox__label {
        color: white;
      }
    }
  }
  div#des {
    width: 680px;
    position: absolute;
    z-index: 20;
    top: 350px;
    left: 20px;
  }
  div#mapdialog {
    /deep/.el-dialog__title {
      color: rgb(47, 45, 156);
    }
  }
  div#map3 {
    position: absolute;
    top: 60px;
    bottom: 0;

    width: 100%;
    height: auto;
    background: rgba(255, 255, 255, 0.2);
  }
}
</style>