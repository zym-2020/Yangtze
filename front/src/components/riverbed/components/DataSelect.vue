<template>
  <div class="main">
    <div class="selectHead">当前分析数据</div>
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
export default defineComponent({
  setup() {
    const store = useStore();
    const value = ref('c6e72c9a-b9c8-4677-9560-8262416d8079');
    const options = ref<Array<DataDem>>([
      { id: 'c6e72c9a-b9c8-4677-9560-8262416d8079', name: "199801_dem" },
      { id: 'd7ad6d22-5458-4915-8918-d014fa8772cd', name: "200408_dem" },
      { id: 'a2efbd6a-5b2f-4144-9f32-cfa74c1d88c5', name: "200602_dem" },
    ]);
    const selectChange = (val: string) => {
      const select = options.value.filter((item) => {
        item.id === val;
      });
      store.commit("SET_DATA_SELECT", {
        id: val,
        name: select[0].name,
      });
    };

    onMounted(() => {
      store.commit("SET_DATA_SELECT", { id: "c6e72c9a-b9c8-4677-9560-8262416d8079", name: "199801_dem" });
    });

    return {
      value,
      options,
      selectChange,
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  background: #fafafa;
  border: solid 1px #888888;
  padding: 1px;
  .selectHead {
    background: #a0a0a0;
    height: 25px;
    line-height: 25px;
    &:hover {
      cursor: move;
    }
  }
}
</style>