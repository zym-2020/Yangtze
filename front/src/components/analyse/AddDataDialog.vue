<template>
  <div>
    <div v-show="dataListShow">
      <el-skeleton :rows="5" animated v-if="skeletonFlag" />
      <div class="add-data-dialog" v-else>
        <div class="tree">
          <el-tree :data="treeData" :props="defaultProps" />
        </div>
        <div class="add-data-card">
          <div class="search">
            <el-input v-model="search" :suffix-icon="Search" />
          </div>
          <el-row>
            <el-col :span="6" v-for="(item, index) in dataList" :key="index">
              <div class="card" @click="cardClick(index)">
                <div class="img">
                  <img :src="getAvatar(item.avatar, item.name)" />
                </div>
                <div class="name">{{ item.name }}</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="page">
        <el-pagination
          small
          background
          layout="prev, pager, next"
          :total="total"
          :hide-on-single-page="true"
          v-model:current-page="currentPage"
          @current-change="pageChange"
        />
      </div>
    </div>

    <div v-show="!dataListShow">
      <el-page-header :icon="ArrowLeft" @back="dataListShow = true">
        <template #content>
          <span> {{ selectDataList.name }} </span>
        </template>
      </el-page-header>
      <div class="content">
        <div class="table">
          <el-table :data="tableData" stripe>
            <el-table-column width="55">
              <template #header> 选择 </template>
              <template #default="scope">
                <el-checkbox
                  v-model="scope.row.flag"
                  size="large"
                  @change="checkboxChange(scope.row)"
                />
              </template>
            </el-table-column>
            <el-table-column prop="name" label="文件名称" width="300" />
            <el-table-column prop="size" label="文件大小" width="100" />
            <el-table-column prop="time" label="时间" width="150" />
            <el-table-column prop="location" label="位置" />
          </el-table>
        </div>
      </div>
    </div>

    <div class="btn">
      <el-button type="primary" plain @click="commit">确定</el-button>
    </div>
  </div>
</template>

<script lang="ts">
interface Tree {
  label: string;
  children?: Tree[];
}
type DataListType = {
  id: string;
  avatar: string;
  name: string;
};
type TableDataType = {
  id: string;
  name: string;
  size: string;
  time: string;
  location: string;
  visualType: string;
  visualId: string;
  flag: boolean;
};
type SelectFileType = {
  fileId: string;
  dataListId: string;
  fileName: string;
  dataListName: string;
  visualType: string;
  visualId: string;
};
import { defineComponent, nextTick, onMounted, ref } from "vue";
import { Search, ArrowLeft } from "@element-plus/icons-vue";
import { fuzzyQueryDataList, findFiles } from "@/api/request";
import { imgBase64 } from "@/utils/common";

export default defineComponent({
  emits: ["returnData"],
  setup(_, context) {
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const treeData: Tree[] = [
      {
        label: "Level one 1",
        children: [
          {
            label: "Level two 1-1",
          },
        ],
      },
      {
        label: "Level one 2",
        children: [
          {
            label: "Level two 2-1",
          },
          {
            label: "Level two 2-2",
          },
        ],
      },
      {
        label: "Level one 3",
        children: [
          {
            label: "Level two 3-1",
          },
          {
            label: "Level two 3-2",
          },
        ],
      },
    ];

    const search = ref("");
    const keyword = ref("");
    const total = ref(0);
    const currentPage = ref(1);
    const dataListShow = ref(true);
    const dataList = ref<DataListType[]>([]);
    const selectDataList = ref<DataListType>({ id: "", name: "", avatar: "" });
    const tableData = ref<TableDataType[]>([]);
    const fileList = ref<SelectFileType[]>([]);
    const skeletonFlag = ref(true);

    const getAvatar = (avatar: string, name: string) => {
      if (avatar === "") {
        return imgBase64(name);
      } else {
        return `http://localhost:8002/visual/getAvatar/${avatar}`;
      }
    };

    const formatData = (list: any[]) => {
      dataList.value = [];
      list.forEach((item) => {
        dataList.value.push({
          id: item.id,
          name: item.name,
          avatar: item.avatar,
        });
      });
    };

    const formatTableData = (list: any[]) => {
      tableData.value = [];
      list.forEach((item) => {
        if (item.visualType != "") {
          tableData.value.push({
            id: item.id,
            name: item.fileName,
            visualType: item.visualType,
            time: item.time,
            location: item.location,
            size: item.size,
            flag: false,
            visualId: item.visualId,
          });
          let flag = false;
          for (let i = 0; i < fileList.value.length; i++) {
            if (item.id === fileList.value[i].fileId) {
              flag = true;
              break;
            }
          }
          tableData.value[tableData.value.length - 1].flag = flag;
        }
      });
    };

    const pageChange = async (val: number) => {
      const jsonData = {
        page: val - 1,
        size: 8,
        keyword: keyword.value,
        tags: [],
        property: "update_time",
        flag: false,
        type: "",
      };
      const data = await fuzzyQueryDataList(jsonData);
      if (data != null && (data as any).code === 0) {
        formatData(data.data.list);
        total.value = data.data.total;
      }
    };

    const cardClick = async (index: number) => {
      const data = await findFiles(dataList.value[index].id);
      if (data != null && (data as any).code === 0) {
        formatTableData(data.data);
        selectDataList.value = dataList.value[index];
        dataListShow.value = false;
      }
    };

    const checkboxChange = (val: TableDataType) => {
      if (val.flag) {
        fileList.value.push({
          fileId: val.id,
          fileName: val.name,
          dataListId: selectDataList.value.id,
          dataListName: selectDataList.value.name,
          visualType: val.visualType,
          visualId: val.visualId,
        });
      } else {
        for (let i = 0; i < fileList.value.length; i++) {
          if (val.id === fileList.value[i].fileId) {
            fileList.value.splice(i, 1);
            return;
          }
        }
      }
    };

    const commit = () => {
      context.emit("returnData", fileList.value);
    };

    onMounted(async () => {
      skeletonFlag.value = true;
      const jsonData = {
        page: 0,
        size: 8,
        keyword: keyword.value,
        tags: [],
        property: "update_time",
        flag: false,
        type: "",
      };
      const data = await fuzzyQueryDataList(jsonData);
      if (data != null && (data as any).code === 0) {
        formatData(data.data.list);
        total.value = data.data.total;
        skeletonFlag.value = false;
      }
    });

    return {
      defaultProps,
      treeData,
      search,
      dataList,
      Search,
      total,
      getAvatar,
      currentPage,
      pageChange,
      dataListShow,
      ArrowLeft,
      selectDataList,
      cardClick,
      tableData,
      checkboxChange,
      commit,
      skeletonFlag,
    };
  },
});
</script>


<style lang="scss" scoped>
@keyframes move {
  0% {
    transform: scale(1);
  }

  100% {
    transform: scale(1.1);
  }
}

.add-data-dialog {
  display: flex;
  .tree {
    padding-top: 10px;
    width: 200px;
    height: 300px;
    background: #f0f0f0;
    border-radius: 8px;
    .el-tree {
      background: none;
    }
  }
  .add-data-card {
    width: 800px;
    margin-left: 5px;
    .card {
      margin: 7px;
      box-sizing: border-box;
      border: solid 0.5px rgba($color: #000000, $alpha: 0);
      background: rgba($color: #f0f0f0, $alpha: 0.6);
      &:hover {
        border: solid 0.5px #0069bd;
        cursor: pointer;
        img {
          animation: move 0.3s linear forwards;
        }
      }

      .img {
        width: 150px;
        margin: 15px auto 10px;
        overflow: hidden;
        img {
          width: 100%;
          height: 80px;
        }
      }
      .name {
        height: 40px;
        width: 150px;
        margin: 0px auto 10px;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
        text-align: center;
      }
    }
  }
}
.page {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
.btn {
  margin-top: 10px;
  position: relative;
  height: 32px;
  .el-button {
    right: 0px;
    position: absolute;
  }
}

.content {
  display: flex;
  margin-top: 15px;
  .avatar {
    width: 200px;
    height: 200px;
    margin-right: 10px;
    img {
      height: 100%;
      width: 100%;
    }
  }
  .table {
    border: solid 1px;
    box-sizing: border-box;
    padding: 15px;
    .el-table {
      width: 920px;
      height: 300px;
    }
    /deep/ .el-table__header-wrapper .el-checkbox {
      display: none;
    }
  }
}
</style>