<template>
  <div v-if="dataSelectType === 'region'">
    <el-select v-model="regionValue">
      <el-option
        v-for="(item, index) in options"
        :key="index"
        :label="item.name"
        :value="item.id"
      />
    </el-select>
    <div class="btn">
      <el-button type="primary" plain size="small" @click="btnClick"
        >确定</el-button
      >
    </div>
  </div>
  <div v-if="dataSelectType === 'slope'">
    <el-select v-model="slopeValue" placeholder="选择dem图层">
      <el-option
        v-for="(item, index) in options"
        :key="index"
        :label="item.name"
        :value="item.id"
      />
    </el-select>
    <div class="btn">
      <el-button type="primary" plain size="small" @click="btnClick"
        >确定</el-button
      >
    </div>
  </div>
  <div v-if="dataSelectType === 'flushSilt'">
    <div class="select">
      <el-select v-model="benchmark" placeholder="基准年" size="small">
        <el-option
          v-for="(item, index) in options"
          :key="index"
          :label="item.name"
          :value="item.id"
          :disabled="item.id == reference"
        />
      </el-select>
    </div>
    <div class="select">
      <el-select v-model="reference" placeholder="比较年" size="small">
        <el-option
          v-for="(item, index) in options"
          :key="index"
          :label="item.name"
          :value="item.id"
          :disabled="item.id == benchmark"
        />
      </el-select>
    </div>
    <div class="btn">
      <el-button type="primary" plain size="small" @click="btnClick"
        >确定</el-button
      >
    </div>
  </div>
  <div v-if="dataSelectType === 'contour'">
    <div class="select">
      <el-select v-model="contour" placeholder="请选择数据集" size="small">
        <el-option
          v-for="(item, index) in options"
          :key="index"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
    </div>
    <div class="input-number">
      间隔：
      <el-input-number
        v-model="num"
        :precision="2"
        :step="0.1"
        :max="20"
        :min="5"
        size="small"
      />
    </div>
    <div class="btn">
      <el-button type="primary" plain size="small" @click="btnClick"
        >确定</el-button
      >
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref } from "vue";
import { notice } from "@/utils/notice";
export default defineComponent({
  props: {
    demLayers: {
      type: Array,
    },
    dataSelectType: {
      type: String,
    },
  },
  emits: ["dataSelectReturn"],
  setup(props, context) {
    const dataSelectType = computed(() => {
      return props.dataSelectType;
    });
    const regionValue = ref("");
    const slopeValue = ref("");
    const benchmark = ref("");
    const reference = ref("");
    const contour = ref("");
    const num = ref(10);
    const options = computed(() => {
      return props.demLayers;
    });

    const btnClick = () => {
      if (dataSelectType.value === "region") {
        context.emit("dataSelectReturn", {
          data: regionValue.value,
          type: dataSelectType.value,
        });
      } else if (dataSelectType.value === "slope") {
        const val = {
          id: slopeValue.value,
          name: "",
        };
        props.demLayers?.forEach((item: any) => {
          if (item.id === slopeValue.value) {
            val.name = item.name;
          }
        });
        context.emit("dataSelectReturn", {
          data: val,
          type: dataSelectType.value,
        });
      } else if (dataSelectType.value === "flushSilt") {
        const params = {
          benchmark: {
            id: benchmark.value,
            name: "",
          },
          reference: {
            id: reference.value,
            name: "",
          },
        };
        props.demLayers?.forEach((item: any) => {
          if (item.id === params.benchmark.id) {
            params.benchmark.name = JSON.parse(
              JSON.stringify(item.name)
            ).substring(0, 6);
          }
          if (item.id === params.reference.id) {
            params.reference.name = JSON.parse(
              JSON.stringify(item.name)
            ).substring(0, 6);
          }
        });
        context.emit("dataSelectReturn", {
          data: params,
          type: dataSelectType.value,
        });
      } else if (dataSelectType.value === "contour") {
        if (contour.value != "") {
          const param = {
            id: contour.value,
            interval: num.value,
            name: "",
          };
          props.demLayers?.forEach((item: any) => {
            if (item.id === contour.value) {
              param.name = item.name;
            }
          });
          context.emit("dataSelectReturn", {
            data: param,
            type: dataSelectType.value,
          });
        } else {
          notice("warning", "警告", "请选择数据集");
        }
      }
    };

    return {
      regionValue,
      slopeValue,
      options,
      btnClick,
      dataSelectType,
      benchmark,
      reference,
      contour,
      num,
    };
  },
});
</script>

<style lang="scss" scoped>
.el-select {
  width: 100%;
  margin-bottom: 10px;
}
.btn {
  text-align: center;
}
</style>