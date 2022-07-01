<template>
  <div class="data-select">
    <div class="title">
      图层选择<el-icon @click="dataSelectClose"><Close /></el-icon>
    </div>
    <div class="select">
      <el-select
        v-model="value"
        placeholder="选择dem图层"
        size="small"
        @change="changeHandle"
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
import { computed, defineComponent, onMounted, ref } from "vue";
export default defineComponent({
  props: {
    demLayers: Array,
  },
  emits: ["dataSelectClose", "dataSelectChange"],
  setup(props, context) {
    const value = ref("");

    const options = computed(() => {
      return props.demLayers;
    });

    const dataSelectClose = () => {
      context.emit("dataSelectClose");
    };

    const changeHandle = (val: string) => {
      const json = {
        id: val,
        name: "",
      };
      options.value?.forEach((item: any) => {
        if (item.id === val) {
          json.name = item.name;
        }
      });
      context.emit("dataSelectChange", json);
    };

    onMounted(() => {
      console.log(props.demLayers);
    });

    return {
      options,
      value,
      dataSelectClose,
      changeHandle,
    };
  },
});
</script>


<style lang="scss" scoped>
.data-select {
  height: 80px;
  width: 120px;
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
  .select {
    padding: 10px 5px;
  }
}
</style>