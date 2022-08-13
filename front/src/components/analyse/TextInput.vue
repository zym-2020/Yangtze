<template>
  <div class="input-main">
    <div class="input-main-head">
      <el-button
        type="primary"
        :icon="Plus"
        size="small"
        @click="dialogVisible = true"
      />
      <el-button
        type="warning"
        :icon="Minus"
        size="small"
        @click="removeRow"
        :disabled="currentRow === undefined"
      />
      <el-button
        type="danger"
        :icon="Delete"
        size="small"
        @click="deleteAll"
        :disabled="tableData.length === 0"
      />
    </div>
    <el-table
      :data="tableData"
      height="150"
      size="small"
      :highlight-current-row="true"
      @row-click="rowClick"
    >
      <el-table-column prop="lon" label="经度" />
      <el-table-column prop="lat" label="纬度" />
    </el-table>
    <div class="btn">
      <el-button
        type="success"
        plain
        v-if="inputType === 'section'"
        :disabled="tableData.length < 2"
        @click="sectionCommitClick"
        >确定</el-button
      >
      <el-button
        type="success"
        plain
        v-if="inputType === 'region'"
        :disabled="tableData.length < 3"
        @click="regionCommitClick"
        >确定</el-button
      >
    </div>
  </div>

  <el-dialog v-model="dialogVisible" width="300px">
    <el-form label-width="50px" :model="form">
      <el-form-item label="经度">
        <el-input-number
          v-model="form.lonInput"
          :precision="6"
          :step="1"
          :min="-180"
          :max="180"
          size="small"
          style="width: 200px"
        />
      </el-form-item>
      <el-form-item label="纬度">
        <el-input-number
          v-model="form.latInput"
          :precision="6"
          :step="1"
          :min="-90"
          :max="90"
          size="small"
          style="width: 200px"
        />
      </el-form-item>
    </el-form>
    <div class="btn">
      <el-button @click="addClick">按钮</el-button>
    </div>
  </el-dialog>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, reactive, ref } from "vue";
import { Plus, Minus, Delete } from "@element-plus/icons-vue";
import { notice } from "@/utils/notice";
export default defineComponent({
  props: {
    textInputValue: {
      type: String,
    },
  },
  emits: ["returnPoints"],
  setup(props, context) {
    const dialogVisible = ref(false);
    const form = reactive({
      lonInput: 0,
      latInput: 0,
    });
    const tableData = ref<any[]>([]);
    const currentRow = ref<any>();

    const inputType = computed(() => {
      return props.textInputValue;
    });

    const deleteAll = () => {
      tableData.value = [];
    };

    const rowClick = (row: any) => {
      console.log(currentRow.value);
      currentRow.value = row;
    };

    const addClick = () => {
      for (let i = 0; i < tableData.value.length; i++) {
        if (
          tableData.value[i].lon === form.lonInput &&
          tableData.value[i].lat === form.latInput
        ) {
          notice("warning", "警告", "不能存在相同的点坐标！");
          return;
        }
      }
      tableData.value.push({
        lon: form.lonInput,
        lat: form.latInput,
      });
      dialogVisible.value = false;
    };

    const removeRow = () => {
      for (let i = 0; i < tableData.value.length; i++) {
        if (
          tableData.value[i].lon === currentRow.value.lon &&
          tableData.value[i].lat === currentRow.value.lat
        ) {
          tableData.value.splice(i, 1);
          return;
        }
      }
    };

    const sectionCommitClick = () => {
      context.emit("returnPoints", { type: "section", data: tableData.value });
    };

    const regionCommitClick = () => {
      context.emit("returnPoints", { type: "region", data: tableData.value });
    };

    onMounted(() => {
      console.log(props.textInputValue);
    });

    return {
      Plus,
      Minus,
      Delete,
      dialogVisible,
      tableData,
      deleteAll,
      form,
      addClick,
      rowClick,
      currentRow,
      removeRow,
      sectionCommitClick,
      regionCommitClick,
      inputType,
    };
  },
});
</script>

<style lang="scss" scoped>
.input-main {
  .input-main-head {
    margin-bottom: 10px;
  }
  .input-main-body {
    height: 150px;
    background: rgba($color: #a5abb2, $alpha: 0.5);
    .point-item {
      height: 25px;
      line-height: 25px;
      padding: 0 5px;
      cursor: pointer;
    }
  }
}
.btn {
  margin-top: 10px;
  text-align: center;
}
</style>