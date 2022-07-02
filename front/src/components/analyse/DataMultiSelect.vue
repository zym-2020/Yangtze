<template>
  <div class="main">
    <div class="title">
      多图层选择<el-icon @click="dataMultiSelectClose"><Close /></el-icon>
    </div>
    <div class="body">
      <el-checkbox-group v-model="checkList" @change="changeClick">
        <el-checkbox
          :label="item.name"
          v-for="(item, index) in demLayers"
          :key="index"
        />
      </el-checkbox-group>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
export default defineComponent({
  emits: ["dataMultiSelectClose", "dataMultiSelectChange"],
  props: {
    demLayers: {
      type: Array,
    },
  },
  setup(props, context) {
    const checkList = ref<string[]>([]);
    const demLayers = computed(() => {
      return props.demLayers;
    });

    const dataMultiSelectClose = () => {
      context.emit("dataMultiSelectClose");
    };
    const changeClick = (val: string[]) => {
      const temp: any[] = [];
      val.forEach((item) => {
        for (let i = 0; i < (props.demLayers as any[]).length; i++) {
          if (item === (props.demLayers as any[])[i].name) {
            temp.push({
              id: (props.demLayers as any[])[i].id,
              name: (props.demLayers as any[])[i].name,
            });
          }
        }
      });
      context.emit("dataMultiSelectChange", temp);
    };

    onMounted(() => {
      props.demLayers?.forEach((item: any) => {
        checkList.value.push(item.name);
      });
    });

    return {
      dataMultiSelectClose,
      checkList,
      demLayers,
      changeClick,
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  min-height: 100px;
  width: 150px;
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
  .body {
    padding: 0 10px;
    .el-checkbox-group {
      margin-top: 10px;
      .el-checkbox {
        color: white;
        display: block;
      }
    }
  }
}
</style>

