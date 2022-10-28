<template>
  <div class="upload-main">
    <div class="main">
      <div class="file-list">
        <el-scrollbar>
          <div>
            <el-tree
              :data="data"
              show-checkbox
              node-key="id"
              accordion
              :props="defaultProps"
              @check="handleCheck"
            />
          </div>
        </el-scrollbar>
      </div>
      <div class="bottom">
        <div class="btn">
          <el-button
            type="primary"
            size="small"
            @click="commit"
            style="margin-left: 10px"
            >确定</el-button
          >
        </div>
      </div>
    </div>
  </div>
</template>
  
  <script lang="ts">
import { defineComponent, ref } from "vue";
import { UploadFile } from "element-plus";
import { uuid } from "@/utils/common";

import { useStore } from "@/store";

export default defineComponent({
  props: {
    level: {
      type: Number,
    },
    parentId: {
      type: String,
    },
  },
  emits: ["commitFile", "commitData"],
  setup(props, context) {
    const diaCheck = ref(false);
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const data = [
      {
        id: 1,
        label: "地形数据",
        children: [
          {
            id: 6,
            label: "DEM",
          },
          {
            id: 7,
            label: "边界",
          },
          {
            id: 8,
            label: "等高线",
          },
          {
            id: 9,
            label: "DWG",
          },
          {
            id: 10,
            label: "高程点",
          },
          {
            id: 11,
            label: "固定断面线",
          },
          {
            id: 12,
            label: "深泓线",
          },
        ],
      },
      {
        id: 2,
        label: "工程数据",
        children: [
          {
            id: 13,
            label: "航标",
          },
          {
            id: 14,
            label: "护岸工程",
          },
          {
            id: 15,
            label: "码头工程",
          },
          {
            id: 16,
            label: "水利工程",
          },
          {
            id: 17,
            label: "整治工程",
          },
          {
            id: 18,
            label: "桥梁工程",
          },
        ],
      },
      {
        id: 3,
        label: "物理模型",
        children: [
          {
            id: 19,
            label: "浓度场",
          },
          {
            id: 20,
            label: "照片",
          },
        ],
      },
      {
        id: 4,
        label: "水文数据",
        children: [
          {
            id: 21,
            label: "潮位",
          },
          {
            id: 22,
            label: "断面输沙率",
          },
          {
            id: 23,
            label: "含沙量",
          },
          {
            id: 24,
            label: "含盐度",
          },
          {
            id: 25,
            label: "流速流向",
          },
          {
            id: 26,
            label: "悬移质",
          },
        ],
      },
      {
        id: 5,
        label: "遥感影像",
        children: [
          {
            id: 27,
            label: "遥感影像",
          },
        ],
      },
    ];
    const dataSelect = ref<string>("");
    const store = useStore();
    const uploadFileList = ref<UploadFile[]>([]);

    const handleCheck = (data: any, checkedData: any) => {
      console.log(checkedData); //包含checkedNodes，checkedKeys
      // 这是选中的节点的key数组
      console.log(checkedData.checkedKeys);
      // 这是选中的节点数组（proxy）对象
      console.log(checkedData.checkedNodes);
      //debugger
      if (checkedData.checkedNodes.length > 0) {
        console.log("haha", checkedData.checkedNodes[0].label);
        dataSelect.value = checkedData.checkedNodes[0].label;
      }
      if (checkedData.checkedNodes.length == 0) {
        console.log("hahawoshi0");
        dataSelect.value = "";
      }
    };

    const close = (index: number) => {};
    //提交资源并关闭对话框
    const commit = () => {
      diaCheck.value = false;
      context.emit("commitData", dataSelect.value);
      context.emit("commitFile");
    };

    return {
      diaCheck,
      defaultProps,
      data,
      commit,
      handleCheck,
      uploadFileList,
      close,
    };
  },
});
</script>
  
  <style lang="scss" scoped>
.upload-main {
  width: 95%;
  height: 400px;
  padding: 10px 10px 10px 10px;
  background: #a6bed7;

  .main {
    height: 390px;
    background: #f0f0f0;
    padding-top: 10px;
    .file-list {
      height: 350px;
      margin: 0 20px;
      background: white;
      border: 1px solid;
    }
    .bottom {
      margin-top: 5px;
      display: flex;
      justify-content: space-around;
      .btn {
        display: flex;
        justify-content: space-around;
      }
    }
  }
}
</style>