<template>
  <div class="main">
    <div class="selectHead">
      <div>当前分析数据</div>
      <el-icon color="white" :size="16" @click="close"><Close /></el-icon>
    </div>
    <div>
      <el-select
        v-model="value"
        placeholder="选择分析数据"
        style="width: 150px"
        @change="selectChange"
      >
        <el-option
          v-for="(item, index) in options"
          :key="index"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
    </div>
  </div>
</template>

<script lang="ts">
interface DataDem {
  id: string;
  name: string;
}
import { computed, defineComponent, onMounted, reactive, ref } from "vue";
import { useStore } from "@/store";
import mapBoxGl from "mapbox-gl";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import { sectionUtil } from "./ts/mapUtils";
import router from "@/router";
export default defineComponent({
  props: {
    map: {
      type: mapBoxGl.Map,
    },
  },
  setup(props) {
    const store = useStore();
    const value = ref("");
    const flag = ref(false);

    const draw = new MapboxDraw({
      controls: {
        combine_features: false,
        uncombine_features: false,
        trash: false,
        point: false,
        polygon: false,
      },
    });

    const options = computed(() => {
      const result: DataDem[] = [];
      store.state.resource.layerDataList.forEach((item) => {
        if (item.type === "riverBed") {
          result.push({
            id: item.id as string,
            name: item.name,
          });
        }
      });
      return result;
    });

    const selectChange = (val: string) => {
      console.log(val);
      const json = {
        id: val,
        name: "",
      };
      options.value.forEach((item) => {
        if (item.id === val) {
          json.name = item.name;
        }
      });
      store.commit("SET_EDIT_STATE", {
        type: "section",
        flag: true,
        state: JSON.stringify(json),
      });
      if (flag.value === false) {
        flag.value = true;

      }
    };

    const close = () => {
      store.commit("SET_EDIT_STATE", { type: "", flag: false, state: "" });
      props.map?.removeControl(draw);
      flag.value = false;
      value.value = "";
    };

    return {
      value,
      options,
      selectChange,
      close,
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  background: #fafafa;
  border: solid 1px #888888;
  padding: 1px;
  width: 150px;
  .selectHead {
    background: #a0a0a0;
    height: 25px;
    line-height: 25px;
    display: flex;
    &:hover {
      cursor: move;
    }
    .el-icon {
      margin-top: 3px;
      margin-left: 50px;
      &:hover {
        cursor: pointer;
      }
    }
  }
}
</style>