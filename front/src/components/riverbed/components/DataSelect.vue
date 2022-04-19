<template>
  <div class="main">
    <div class="selectHead">当前分析数据</div>
    <div>
      <el-select v-model="value" placeholder="选择分析数据" style="width: 150px">
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
  id: number;
  name: string;
}
import { computed, defineComponent, ref } from "vue";
import { useStore } from "@/store";
export default defineComponent({
  setup() {
    const store = useStore();
    const value = ref("199801_dem");
    const options = ref<Array<DataDem>>([
      { id: 3, name: "199801_dem" },
      { id: 4, name: "200408_dem" },
      { id: 5, name: "200602_dem" },
    ]); 


    return {
      value,
      options,
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