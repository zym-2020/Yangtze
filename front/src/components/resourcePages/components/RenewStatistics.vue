<template>
  <div class="renew-statistics">
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="date" label="Date" />
      <el-table-column prop="name" label="Name" />
      <el-table-column prop="address" label="Address" />
    </el-table>

    <div class="page">
      <el-pagination small background layout="prev, pager, next" :total="50" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
export default defineComponent({
  setup() {
    const tableData = [
      {
        date: "2016-05-03",
        name: "Tom",
        address: "No. 189, Grove St, Los Angeles",
      },
      {
        date: "2016-05-02",
        name: "Tom",
        address: "No. 189, Grove St, Los Angeles",
      },
      {
        date: "2016-05-04",
        name: "Tom",
        address: "No. 189, Grove St, Los Angeles",
      },
      {
        date: "2016-05-01",
        name: "Tom",
        address: "No. 189, Grove St, Los Angeles",
      },
    ];

    return {
      tableData,
    };
  },
});
</script>

<style lang="scss" scoped>
.renew-statistics {
  .page {
    margin-top: 40px;
    margin-bottom: 40px;
    width: 100%;

    display: flex;
    justify-content: space-around;
  }
}
</style>