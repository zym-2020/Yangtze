<template>
  <div>
    <el-collapse @change="handleChange" v-model="activeNames">
      <el-collapse-item
        v-for="(item, index) in categoryList"
        :key="index"
        :title="item.title"
        :name="index"
        active
      >
      <p style="text-align :center;font-weight: bold">{{desciription[index]}}</p>
        <div v-for="(dataItem, index) in item.data" :key="index">
        <!-- :label="dataItem.name + '（' + dataItem.count + '）'"  -->
          <el-checkbox
            :label=dataItem.name 
            size="default"
            @change="change(dataItem)"
          />
        </div>
        <hr style="border-color: #d8d8d8" />
      </el-collapse-item>
      
    </el-collapse>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive } from "vue";
export default defineComponent({
  emits: ["selectList"],
  setup(props, context) {
    const activeNames=reactive([0,1,2,3,4])
    const desciription =reactive(['地形数据、水文数据、工程数据'])
    const categoryList = ref([
      {
        title: "基础数据、整合数据、可视化数据",
        data: [
          {
            name: "栅格ASC文件",
            count: 5,
          },
          {
            name: "栅格TXT文件",
            count: 7,
          },
          {
            name: "栅格文件",
            count: 7,
          },
          {
            name: "矢量文件",
            count: 7,
          },
          {
            name: "等高线",
            count: 7,
          },
          {
            name: "等深线",
            count: 7,
          },
          {
            name: "潮位",
            count: 7,
          },
          {
            name: "大断面结果",
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
            name: "输沙率",
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
          {
            name: "冲淤",
            count: 7,
          },
          {
            name: "深泓线",
            count: 7,
          },
          {
            name: "风速",
            count: 7,
          },
          {
            name: "风向",
            count: 7,
          },
          {
            name: "DWG工程文件",
            count: 7,
          },
          {
            name: "TXT工程文件",
            count: 7,
          },
        ],
      },
      {
        title: "数学模型",
        data: [
          {
            name: "流场",

            count: 7,
          },
        ],
      },
      {
        title: "物理模型",
        data: [
          {
            name: "地图数据",
            count: 5,
          },
          {
            name: "浓度场数据",
            count: 7,
          }
        ],
      },
      {
        title: "辅助数据",
        data: [
          {
            name: "Pdf",
            count: 5,
          },
          {
            name: "Excel",
            count: 7,
          },
          {
            name: "PPT",
            count: 7,
          },
          {
            name: "Word",
            count: 7,
          },
        ],
      },
      {
        title: "影像资料",
        data: [
          {
            name: "遥感影像",
            count: 5,
          },
          {
            name: "照片",
            count: 7,
          },
          {
            name: "视频",
            count: 7,
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
          context.emit("selectList", selectList.value);
          return;
        }
      }
      selectList.value.push(val.name);
      context.emit("selectList", selectList.value);
    };

    return {
      activeNames,
      desciription,
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