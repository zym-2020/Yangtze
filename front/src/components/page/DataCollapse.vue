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
        <!-- :label="dataItem.name + '（' + dataItem.count + '）'"  -->
          <el-checkbox
            :label=dataItem.name 
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
  setup() {
    const categoryList = ref([
      {
        title: "水文参数数据",
        data: [
          {
            name: "潮位",
            count: 5,
          },
          {
            name: "大断面",
            count: 7,
          },
          {
            name: "含沙量",
            count: 7,
          },
          {
            name: "流量",
            count: 7,
          },
          {
            name: "流速",
            count: 7,
          },
          {
            name: "流向",
            count: 7,
          },
          {
            name: "悬移质",
            count: 7,
          },
        ],
      },
      {
        title: "水文数据",
        data: [
          {
            name: "深泓线",
            
            //count: 
          },
          {
            name: "沙滩",
            count: 7,
          },
          {
            name: "浓度场",
            count: 7,
          },
          {
            name: "流场",
            count: 7,
          },
                    {
            name: "冲淤",
            count: 7,
          },
        ],
      },
            {
        title: "流场数据",
        data: [
          {
            name: "流场矢量线数据",
            count: 5,
          },
          {
            name: "流场栅格数据",
            count: 7,
          },
        ],
      },
      {
        title: "物理模型数据",
        data: [
          {
            name: "模型照片",
            count: 5,
          },
          {
            name: "试验照片",
            count: 7,
          },
          {
            name: "等高线",
            count: 7,
          },
          {
            name: "长江BMP图像",
            count: 7,
          },
        ],
      },
      {
        title: "基础数据",
        data: [
          {
            name: "水文参数数据",
            count: 5,
          },
          {
            name: "三维点数据",
            count: 7,
          },
          {
            name: "流场数据",
            count: 7,
          },
          {
            name: "工程数据",
            count: 7,
          },
          {
            name: "数模数据",
            count: 7,
          },
          {
            name: "物模数据",
            count: 7,
          },
          {
            name: "影像数据",
            count: 7,
          },
        ],
      },
      {
        title: "辅助数据",
        data: [
          {
            name: "潮位站",
            count: 5,
          },
          {
            name: "长江流域遥感影像",
            count: 7,
          },
          {
            name: "等高线",
            count: 7,
          },
          {
            name: "长江BMP图像",
            count: 7,
          },
        ],
      },
      {
        title: "工程实施数据",
        data: [
          {
            name: "工程前数据",
            count: 5,
          },
          {
            name: "工程后数据",
            count: 7,
          },
        ],
      },
      {
        title: "处理数据",
        data: [
          {
            name: "原始数据",
            count: 5,
          },
          {
            name: "整合数据",
            count: 7,
          },
        ],
      },
      {
        title: "辅助数据",
        data: [
          {
            name: "PPT",
            count: 6,
          },
          {
            name: "PDF",
            count: 1,
          },
          {
            name: "DOC",
            count: 1,
          },
          {
            name: "XLS",
            count: 1,
          },
        ],
      },
      {
        title: "数据库数据",
        data: [
          {
            name: "水文Access数据库",
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
      console.log(val);
    };

    const change = (val: any) => {
      for (let i = 0; i < selectList.value.length; i++) 
      {
        if (JSON.stringify(val) === JSON.stringify(selectList.value[i]))
        {
          selectList.value.splice(i, 1);
          console.log(selectList.value);
          return;
        }
      }
      selectList.value.push(val);
      console.log(selectList.value);

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