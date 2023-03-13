<template>
  <div class="search-component">
    <el-input v-model="inputValue" size="large" @focus="focusHandle">
      <template #prepend>
        <el-select
          v-model="selectValue"
          style="width: 120px"
          size="large"
          @change="changeHandle"
        >
          <el-option
            v-for="(item, index) in options"
            :key="index"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </template>
      <template #append>
        <el-button @click="searchClick"
          ><el-icon><Search /></el-icon
        ></el-button>
      </template>
    </el-input>

    <div class="content" v-if="showFlag">
      <div>
        <div class="title">
          <span>{{ optionMap[selectValue] }}</span>
          <div class="switch" v-if="selectValue === 'ship'">
            <span>( </span>
            <el-switch
              v-model="switchValue"
              active-text="实时数据"
              inactive-text="模拟数据"
              @change="switchChange"
            />
            <span> )</span>
          </div>
          <el-icon @click="closeHandle"><Close /></el-icon>
        </div>
        <div>
          <el-skeleton :rows="5" animated v-if="skeletonFlag" />
          <el-table
            :data="tableData"
            border
            style="width: 100%"
            v-else
            @cell-dblclick="dblclickHnadle"
          >
            <el-table-column
              :label="item.label"
              v-for="(item, index) in propLabelMap[selectValue]"
              :key="index"
            >
              <template #default="scope">
                <span v-html="replaceHandle(scope.row[item.prop])"></span>
              </template>
            </el-table-column>
          </el-table>
          <div class="page">
            <el-pagination
              small
              background
              layout="total, prev, pager, next"
              :total="total"
              hide-on-single-page
              :pager-count="5"
              @current-change="pageChange"
              v-model:current-page="currentPage"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import {
  SearchTable,
  Buoy,
  Anchor,
  Park,
  Bridge,
  Station,
  RealShip,
  Ship,
} from "@/type";
import { pageList } from "@/api/request";
export default defineComponent({
  emits: ["returnPoint", "shipDataModeChange"],
  setup(_, context) {
    const inputValue = ref("");
    let keyword = "all";
    const selectValue = ref("ship");
    const showFlag = ref(false);
    const skeletonFlag = ref(false);
    const tableData = ref<SearchTable[]>([]);
    const switchValue = ref(true);

    const currentPage = ref(1);
    const total = ref(0);

    const options = [
      {
        label: "AIS船舶",
        value: "ship",
      },
      {
        label: "航标",
        value: "buoy",
      },
      {
        label: "停泊区",
        value: "park",
      },
      {
        label: "锚地",
        value: "anchor",
      },
      {
        label: "桥梁",
        value: "bridge",
      },
      {
        label: "水位站点",
        value: "station",
      },
    ];
    const optionMap = {
      ship: "AIS船舶",
      buoy: "航标",
      park: "停泊区",
      anchor: "锚地",
      bridge: "桥梁",
      station: "水位站点",
    };
    const propLabelMap = {
      ship: [
        { prop: "mmsi", label: "mmsi" },
        { prop: "cbmc", label: "船舶名称" },
      ],
      buoy: [
        { prop: "sshd", label: "所属航道" },
        { prop: "hbmc", label: "航标名称" },
      ],
      park: [
        { prop: "mc", label: "停泊区名称" },
        { prop: "yt", label: "用途" },
      ],
      anchor: [
        { prop: "mdmc", label: "锚地名称" },
        { prop: "yt", label: "用途" },
      ],
      bridge: [
        { prop: "桥梁名称", label: "桥梁名称" },
        { prop: "桥梁属性", label: "桥梁属性" },
      ],
      station: [{ prop: "name", label: "站点" }],
    };

    const focusHandle = async () => {
      if (showFlag.value == false) {
        showFlag.value = true;
        skeletonFlag.value = true;
        await getData(0, 10);
        skeletonFlag.value = false;
      }
    };

    const closeHandle = () => {
      showFlag.value = false;
    };

    const getData = async (page: number, size: number) => {
      let type = selectValue.value;
      if (switchValue.value && selectValue.value == "ship") {
        type = "realShip";
      }
      const data = await pageList(type, page, size, keyword);
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.list;
        total.value = data.data.total;
      }
    };

    const pageChange = async () => {
      skeletonFlag.value = true;

      await getData(currentPage.value - 1, 10);
      skeletonFlag.value = false;
    };

    const changeHandle = async () => {
      skeletonFlag.value = true;
      keyword = "all";
      await getData(currentPage.value - 1, 10);
      skeletonFlag.value = false;
      currentPage.value = 1;
    };

    const replaceHandle = (currentStr: string) => {
      const res = new RegExp(
        "(" + (keyword == "all" ? "" : keyword) + ")",
        "g"
      );
      currentStr = currentStr.replace(
        res,
        "<span style='color:red;'>" +
          (keyword == "all" ? "" : keyword) +
          "</span>"
      );
      return currentStr;
    };

    const searchClick = async () => {
      keyword = inputValue.value === "" ? "all" : inputValue.value;
      skeletonFlag.value = true;
      await getData(currentPage.value - 1, 10);
      skeletonFlag.value = false;
      currentPage.value = 1;
    };

    const dblclickHnadle = (
      row: Buoy | Bridge | Park | Anchor | Station | RealShip
    ) => {
      if ("hbmc" in row) {
        context.emit("returnPoint", {
          point: [row.jdwz_84jd, row.jdwz_84wd],
          info: row,
        });
      } else if ("keys_cn" in row) {
        context.emit("returnPoint", {
          point: [row.lon, row.lat],
          info: row,
        });
      } else if ("polygon" in row) {
        context.emit("returnPoint", {
          point: row.polygon.coordinates[0][0],
          info: row,
        });
      } else if ("mc" in row) {
        console.log("1", row);
        context.emit("returnPoint", {
          point: [row.zbjd, row.zbwd],
          info: row,
        });
      } else if ("mdmc" in row) {
        context.emit("returnPoint", {
          point: [row.zbjd, row.zbwd],
          info: row,
        });
      } else if ("mmsi" in row) {
        if (switchValue) {
          const ship: Ship = {
            mmsi: row.mmsi,
            update_time: row.jssj,
            register_time: row.cjsj,
            name: row.cbmc,
            name_cn: row.zwmc,
            direction: Number(row.cbhx),
            velocity: Number(row.dqhs),
            length: row.cd,
            width: row.kd,
            lon: Number(row.zbjd),
            lat: Number(row.zbwd),
          };
          context.emit("returnPoint", {
            point: [row.zbjd, row.zbwd],
            info: ship,
          });
        }
      }
    };

    const switchChange = async (val: boolean) => {
      context.emit("shipDataModeChange", val);
      if (selectValue.value === "ship") {
        skeletonFlag.value = true;
        await getData(0, 10);
        skeletonFlag.value = false;
      }
    };

    return {
      inputValue,
      selectValue,
      switchValue,
      options,
      showFlag,
      focusHandle,
      closeHandle,
      optionMap,
      propLabelMap,
      tableData,
      skeletonFlag,
      total,
      currentPage,
      pageChange,
      changeHandle,
      replaceHandle,
      searchClick,
      dblclickHnadle,
      switchChange,
    };
  },
});
</script>

<style lang="scss" scoped>
.search-component {
  width: 420px;
  .el-input {
    box-shadow: 0 0 3px 3px #ccc;
  }

  .content {
    border-radius: 4px;
    padding: 10px;
    width: 400px;
    min-height: 300px;
    background: white;
    margin-top: 10px;
    box-shadow: 0 0 3px 3px #ccc;
    .title {
      position: relative;
      border-bottom: solid 1px;
      height: 30px;
      margin-bottom: 10px;
      .switch {
        position: absolute;
        left: 65px;
        top: -3px;
      }
      .el-icon {
        position: absolute;
        right: 0px;
        cursor: pointer;
      }
    }
    .el-table {
      cursor: pointer;
    }
    .page {
      display: flex;
      justify-content: center;
      margin-top: 5px;
    }
  }
}
</style>