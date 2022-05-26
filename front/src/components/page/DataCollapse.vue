<template>
  <div>
    <el-collapse @change="handleChange">
      <el-collapse-item
        v-for="(item, index) in categoryList"
        :key="index"
        :title="item.title"
        :name="index"
      >
        <div v-for="(dataItem, index) in item.data" :key="index">
          <el-checkbox
            :label="dataItem.name + '（' + dataItem.count + '）'"
            size="default"
            @change="change(dataItem)"
          />
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
export default defineComponent({
  emits: ["selectList"],
  setup(props, context) {
    const categoryList = ref([
      // {
      //   title: "国家和经济",
      //   data: [
      //     {
      //       name: "阿富汗",
      //       count: 5,
      //     },
      //     {
      //       name: "非洲",
      //       count: 7,
      //     },
      //   ],
      // },
      // {
      //   title: "国家和经济",
      //   data: [
      //     {
      //       name: "阿富汗",
      //       count: 5,
      //     },
      //     {
      //       name: "非洲",
      //       count: 7,
      //     },
      //   ],
      // },
      {
        title: "国家和经济",
        data: [
          {
            name: "阿富汗",
            count: 5,
          },
          {
            name: "非洲",
            count: 7,
          },
        ],
      },
      {
        title: "源目录",
        data: [
          {
            name: "金融",
            count: 6,
          },
          {
            name: "世行数据目录",
            count: 1,
          },
        ],
      },
      {
        title: "收集",
        data: [
          {
            name: "乌克兰 数据集",
            count: 3,
          },
        ],
      },
      {
        title: "资源类型",
        data: [
          {
            name: "excel",
            count: 1,
          },
          {
            name: "pdf",
            count: 1,
          },
        ],
      },
    ]);
    const selectList = ref<any[]>([]);
    const handleChange = (val: string[]) => {
      // console.log(val);
    };

    const change = (val: any) => {
      for (let i = 0; i < selectList.value.length; i++) {
        if (val.name === selectList.value[i]) {
          selectList.value.splice(i, 1);
          context.emit("selectList", selectList.value)
          return;
        }
      }
      selectList.value.push(val.name);
      context.emit("selectList", selectList.value)
    };

    return {
      handleChange,
      categoryList,
      change,
    };
  },
});
</script>

<style lang="scss" scoped>
.el-collapse /deep/ .el-collapse-item__header {
  background: #f6f7fa;
  font-size: 16px;
}
.el-collapse /deep/ .el-collapse-item__wrap {
  background: #f6f7fa;
}
</style>