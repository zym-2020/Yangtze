<template>
  <div class="upload-main">
    <div class="main">
      <div class="file-list">
        <el-scrollbar>
          <el-empty description="暂无数据" v-if="uploadFileList.length === 1" />
          <div v-else>
            <el-tree
              :data="data"
              show-checkbox
              node-key="id"
              accordion
              :props="defaultProps"
              @check="handleCheck"
            />
            <!-- :default-expanded-keys="[2, 3]"
              :default-checked-keys="[5]" -->
            <!-- <div v-for="(item, index) in uploadFileList" :key="index" class="file-item">
                <div class="name">{{ item.name }}</div>
                <div class="over">
                  <el-icon @click="close(index)"><CloseBold /></el-icon>
                </div>
              </div> -->
          </div>
        </el-scrollbar>
      </div>
      <div class="bottom">
        <div class="btn">
          <!-- <el-upload
              action="#"
              ref="upload"
              :multiple="true"
              :auto-upload="false"
              :show-file-list="false"
              :on-change="handelChange"
            >
              <template #trigger>
                <el-button type="warning" size="small">选择文件</el-button>
              </template>
            </el-upload> -->
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
  emits: ["commitFile"],
  setup(props, context) {
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const data = [
      {
        id: 1,
        label: "基础地形数据",
        children: [
          {
            id: 4,
            label: "dem",
          },
        ],
      },
      {
        id: 2,
        label: "基础水文数据",
        children: [
          {
            id: 5,
            label: "潮位",
          },
          {
            id: 6,
            label: "深弘线",
          },
        ],
      },
      {
        id: 3,
        label: "基础工程数据",
        children: [
          {
            id: 7,
            label: "水利工程",
          },
        ],
      },
    ];

    const store = useStore();
    const uploadFileList = ref<UploadFile[]>([]);

    const handleCheck = (data: any, checkedData: any) => {
      console.log(checkedData); //包含checkedNodes，checkedKeys
      // 这是选中的节点的key数组
      console.log(checkedData.checkedKeys);
      // 这是选中的节点数组（proxy）对象
      console.log(checkedData.checkedNodes);
      //debugger

      // if (file.status === "ready") {
      //   uploadFileList.value.push(file);
      // }
    };

    const close = (index: number) => {
      //uploadFileList.value.splice(index, 1)
    };

    const commit = () => {

    };

    return {
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
      // .file-item {
      //   height: 25px;
      //   line-height: 25px;
      //   position: relative;
      //   .name {
      //     margin: 0 10px;
      //   }
      //   .over {
      //     position: absolute;
      //     opacity: 0;
      //     top: 0;
      //     height: 25px;
      //     width: 100%;
      //     background: #7F7F7F;
      //     cursor: pointer;
      //     .el-icon {
      //       float: right;
      //       font-size: 17px;
      //       margin-top: 4px;
      //       margin-right: 4px;
      //       color: black;
      //     }
      //     &:hover {
      //       opacity: 0.2;
      //     }

      //   }
      // }
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