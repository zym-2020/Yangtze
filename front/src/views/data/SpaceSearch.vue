<template>
  <div class="scene-map-wrapper3">
    <div id="box">
      <div id="search">
        <el-button>检索</el-button>
      </div>
      <div id="reset">
        <el-button>重置</el-button>
      </div>
      <div>
        <el-row>
          <el-col :span="4">
            <div id="data">资源条目:</div>
          </el-col>
          <el-col :span="20">
            <div id="opendig">
              <el-button text @click="dialogUpload = true"
                >选择资源集合</el-button
              >
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
            <div style="text-align: center; margin-top: 5px">数据量:</div>
          </el-col>
          <el-col :span="20">
            <el-select v-model="dataValue" class="m-2" placeholder="<=">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
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
              <el-checkbox v-model="checked1" label="矢量" border />
              <el-checkbox v-model="checked2" label="栅格" border />
              <el-checkbox v-model="checked3" label="Excel" border />
              <el-checkbox v-model="checked4" label="照片" border />
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
    <div id="des">
      <el-scrollbar max-height="550px">
        <div v-for="(item, index) in [1, 2, 3, 4, 5, 6, 7, 8]" :key="index">
          <MapDescription></MapDescription>
        </div>
      </el-scrollbar>
    </div>
    <div id="mapdialog">
      <el-dialog
        v-model="dialogUpload"
        width="20%"
        :show-close="false"
        title="请选择数据，最多一条"
      >
        <MapDialog></MapDialog>
        <!-- <upload-dialog
        :level="level"
        :parentId="path.length > 0 ? path[path.length - 1].id : '-1'"
        @commitFile="dialogUpload = false"
      ></upload-dialog> -->
      </el-dialog>
    </div>
    <div id="map3"></div>
  </div>
</template>
  
  <script lang="ts">
import { defineComponent, onMounted, ref, reactive } from "vue";
import "@mapbox/mapbox-gl-draw/dist/mapbox-gl-draw.css";
import MapDialog from "@/components/spaceSearch/MapDialog.vue";
import MapDescription from "@/components/spaceSearch/MapDescription.vue";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import mapBoxGl from "mapbox-gl";
import mapboxgl from "mapbox-gl";

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
    const dialogUpload = ref(false);
    const checked1 = ref();
    const checked2 = ref();
    const checked3 = ref();
    const checked4 = ref();
    const dataValue = ref();
    const options = [
      {
        label: "小于1MB",
        value: "1",
      },
      {
        label: "小于10MB",
        value: "10",
      },
      {
        label: "小于100MB",
        value: "100",
      },
      {
        label: "小于1GB",
        value: "1000",
      },
      {
        label: "无限制",
        value: "9999",
      },
    ];
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
      //console.log(pickDay.value)
      //console.log(queryParams.begintime)
      console.log(val);
      //console.log(formatDate(val[1]))
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
    onMounted(() => {
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
          //console.log(e.features[0].geometry.coordinates[0])
          emit("getCoor", e.features[0].geometry.coordinates[0]);
        } else if (e.type === "draw.update") {
          //修改绘制图形后
          emit("getCoor", e.features[0].geometry.coordinates[0]);
        } else if (e.type === "draw.delete") {
          emit("getCoor", e.features[0].geometry.coordinates[0]);
          //删除绘制图形
        }
      };
      //rgba(250, 235, 215, 0.8)
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
      dataValue,
      options,
      starDate,
      endDate,
      shortcuts,
      queryParams,
      dialogUpload,

      disabledDate,
      handleChange,
      handleFocus,
      formatDate,
    };
  },
});
</script>F
  
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
      left: 550px;
      text-align: center;
      display: inline;
      /deep/.el-button {
        width: 30px;
        height: 20px;
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