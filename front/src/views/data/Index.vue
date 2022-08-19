<template>
  <div class="data-main">
    <page-header :pageTitle="'数据门户'"></page-header>
    <div class="data-body">
      <div class="body">
        <div class="left">
          <div class="content">
            <div class="video" style="margin-bottom: 30px">
              <div>
                <el-card style="text-align: center">
                  <strong>资源类别</strong>
                </el-card>
              </div>
            </div>

            <data-collapse @selectList="getSelectList"></data-collapse>
          </div>
        </div>

        <div class="right">
          <div v-if="searchMap">
            <FindMap
              @getCoor="getCoor"
              style="width: 600px; height: 800px; margin: 10px"
            ></FindMap>
          </div>
            <div v-if="searchSet"
            >
            <div style="margin:10px
            ;padding:5px
            ;width:auto;
            border: 1px solid #8b7e66">
  <el-table
    ref="multipleTableRef"
    :data="tableData"
    style="width: 100%"
    @selection-change="handleSelectionChange"
    @row-dblclick="ddd"
    :row-style="tableStyleName"
    :row-class-name="tableRowClassName"
  >
    <el-table-column type="selection" width="40" />
    <el-table-column label="时间" width="150">
      <template #default="scope">{{ scope.row.date }}</template>
    </el-table-column>
    <el-table-column property="name" label="名称" width="400" />
    <el-table-column property="address" label="描述" show-overflow-tooltip  />
    <el-table-column fixed="right" label="删除" width="100">
      <template #default="scope">
        <el-button
          link
         type="danger" 
         icon="Delete"
         round
          size="small"
          @click.prevent="deleteRow(scope.$index)"
        >
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  </div>
  <div>
  <el-button class="mt-4" style="width: 100% ;margin-top:20px" @click="getSelection"
    >Add Item</el-button
  >
  </div>
          </div>
          <div class="list">
            <el-input
              v-model="input"
              placeholder="请输入关键字"
              @keyup.enter="search"
            >
              <template #append>
                <el-button @click="search"
                  ><el-icon><Search /></el-icon
                ></el-button>
              </template>
            </el-input>
            <div class="statistics">
              <div class="result">
                共<span>{{ total }}</span
                >条结果
              </div>
              <div class="sort">
                排序方式
                <el-select v-model="selectValue" @change="search">
                  <el-option
                    v-for="(item, index) in options"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </div>
            <el-divider />
            <div v-if="skeletonFlag">
              <div v-for="(item, index) in fileList" :key="index">
                <data-card
                  :fileInfo="item"
                  class="card"
                  @clickName="toDetail(index)"
                  @getDataSet="getDataSet"
                  :dataSelect="tableData"
                  :key="new Date().getTime()"

                ></data-card>
              </div>
            </div>
            <div v-else>
              <el-skeleton :rows="5" animated v-for="item in 3" :key="item" />
            </div>

            <div class="pagination">
              <el-pagination
                background
                :page-size="10"
                layout="prev, pager, next"
                :total="total"
                @current-page="currentPage"
                @current-change="pageChange"
                hide-on-single-page="false"
              />
            </div>
          </div>
        </div>
        <div>
          <div>
            <el-card
              shadow="always"
              :style="[
                { marginTop: searchMap == false ? '110px' : '250px' },
                { backgroundColor: searchMap == false ? '' : '#859ecc' },
                { transform: searchMap == false ? '' : 'scale(1.2)' },
              ]"
              class="video2"
              @click="isCoor"
              >空间位置查询
            </el-card>
          </div>
          <div>
            <el-card
              shadow="always"
              :style="[
                { marginTop: searchSet == false ? '10px' : '150px' },
                { backgroundColor: searchSet == false ? '' : '#859ecc' },
                { transform: searchSet == false ? '' : 'scale(1.2)' },
              ]"
              class="video2"
              @click="isSet"
              >资源融合
            </el-card>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import PageHeader from "@/components/page/PageHeader.vue";
import DataCollapse from "@/components/page/DataCollapse.vue";
import DataCard from "@/components/cards/DataCard.vue";
import FindMap from "@/components/scenePart/FindMap.vue";
import {dateFormat } from "@/utils/common";
import { DataSetJsonData } from "@/api/type/userType"
import { 
  fuzzyQuery,
  fuzzyQueryClassify,
  getShpByCoordinates,
} from "@/api/request";
import router from "@/router";
import { ElTable } from 'element-plus'
import {
  Check,
  Delete,
  Edit,
  Message,
  Search,
  Star,
} from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import NProgress from "nprogress";
import { uuid } from "@/utils/common";
NProgress.configure({ showSpinner: false });
interface User {
  date: string
  name: string
  address: string
  tags: string[]
}
export default defineComponent({
  components: {
    PageHeader,
    DataCollapse,
    DataCard,
    FindMap,
    
  },
  
  setup() {
    const skeletonFlag = ref(false);
    const input = ref("");
    const keyWord = ref("");
    const selectValue = ref("download");
    const fileList = ref<any[]>([]);
    const coorList = ref<any[]>([]);
    const classify = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);
    const getsSelectList = ref<any[]>([]);
    const searchMap = ref(false);
    const searchSet = ref(false);
    const selectionData= ref<DataSetJsonData[]>([])
    const getSelection=()=>{
      //selectionData.value[0].name=multipleSelection?.value[0].name
      console.log(multipleSelection.value[0])
      //(selectionData.value[0] as any).name="fff"
      let tempdes=''
      let tagSum:string[]=[]
     for(let i=0;i<multipleSelection.value.length;i++){

       tempdes = tempdes +'资源'+i+':'+multipleSelection.value[i].address+';'+'\n'
       multipleSelection.value[i].tags.forEach((item : any)=>{
        if(tagSum.indexOf(item)==-1)
            tagSum.push(item)
       })
        //tagSum.concat(multipleSelection.value[i].tags as string[])
        //console.log(multipleSelection.value[i].tags)
     }
      console.log(tagSum)
     const sumData: DataSetJsonData[] = [
  {
    id: uuid(),
    name :'资源融合数据集',
    description: tempdes,
    originName: 'string',
    structuredName: 'string',
    originAddress: 'string',
    //
    visualSource: 'string',
    //
    visualType: 'string',
    //
    structuredSource: 'string',
    //
    tags: tagSum, 
    creator: '123qq.com',
    createTime: String(new Date()),
    updateTime: 'string',
    download: 0,
    watch: 0,
    meta: 'string',
    avatar :'string',
    status: 1,
    thumbnail: 'string',
    visual_method :['dd','lala']
  },
  ]
      console.log(sumData[0])
      console.log(sumData[0].name)

    }
    const options = ref<{ label: string; value: string }[]>([
      {
        label: "下载量",
        value: "download",
      },
      {
        label: "浏览量",
        value: "watch",
      },
      {
        label: "上次更新时间",
        value: "update",
      },
      {
        label: "名称",
        value: "name",
      },
    ]);
    const now = new Date()
const tableStyleName = (row:any,rowIndex:any)=>{
        // console.log(row.row.date )
        // console.log(multipleSelection.value[0]?.date)
        for(let i =0;i<multipleSelection.value.length;i++){
        if(multipleSelection.value[i]?.date==row.row.date)
         return 'background-color:rgba(21,69,153,0.3)'
        }
         return ''
}
const tableRowClassName = (row:any,rowIndex:any)=>{
  return ''
}
const tableData = ref([
  {
    date: '2016-05-03',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
    tags:['Tom','Tom']
  },
  {
    date: '2016-05-02',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
    tags:['Tom','Tom']
  },
  {
    date: '2016-05-04',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
    tags:['Tom','Tom']
  },

])
const multipleSelection = ref<any[]>([])
const handleSelectionChange = (val: any[]) => {
  multipleSelection.value = val

}


const deleteRow = (index: number) => {
  tableData.value.splice(index, 1)
}
const ddd=(row:any)=>{
}
const onAddItem = (val:any) => {
  //此处val为fileInfo
  now.setDate(now.getDate() + 1)
  tableData.value.push({
    date: dateFormat(
        val.createTime,
        "yyyy年MM月dd日"
      ),
    name: val.name,
    address: val.id,
    tags:val.tags,
  
  })
}
    //空间查询
    const isCoor = async () => {
      searchMap.value = !searchMap.value;
      if (searchMap.value == false) {
        const data = await fuzzyQuery({
          size: 10,
          page: 0,
          property: "download",
          flag: false,
          keyWord: keyWord.value,
        });
        skeletonFlag.value = true;
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value = data.data.list;
            total.value = data.data.total;
          }
        }
      }
    };
    const isSet = async () => {
      searchSet.value = !searchSet.value;
    };
    const getDataSet= (val:any)=>{
      onAddItem(val)
    }
    const getCoor = async (val: any[]) => {
      let arr = [];
      for (let i = 0; i < val.length; i++) {
        arr.push(val[i][0]);
        arr.push(val[i][1]);
      }
      coorList.value = arr;
      const data = await getShpByCoordinates(coorList.value.toString());
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data;
          total.value = data.data.length;
          currentPage.value = 1;
        }
      }
    };
    const search = async () => {
      NProgress.start();
      keyWord.value = input.value;
      let property = "";
      let flag = false;
      switch (selectValue.value) {
        case "download":
          property = "download";
          flag = false;
          break;
        case "watch":
          property = "watch";
          flag = false;
          break;
        case "update":
          property = "update_time";
          flag = false;
          break;
        case "name":
          property = "name";
          flag = false;
          break;
      }
      if (classify.value.length > 0) {
        const data = await fuzzyQueryClassify({
          size: 10,
          page: currentPage.value - 1,
          property: property,
          flag: flag,
          keyWord: keyWord.value,
          tags: classify.value,
        });
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value = data.data.list;
            total.value = data.data.total;
            currentPage.value = 1;
          }
        }
      } else {
        const data = await fuzzyQuery({
          size: 10,
          page: currentPage.value - 1,
          property: property,
          flag: flag,
          keyWord: keyWord.value,
        });
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value = data.data.list;
            total.value = data.data.total;
            //currentPage.value = 1;
          }
        }
      }
      NProgress.done();
    };

    const pageChange = async (val: any) => {
      //classify记录了分类的项目
      classify.value = getsSelectList.value;
      NProgress.start();
      currentPage.value = val;
      let property = "";
      let flag = false;
      switch (selectValue.value) {
        case "download":
          property = "download";
          flag = false;
          break;
        case "watch":
          property = "watch";
          flag = false;
          break;
        case "update":
          property = "update_time";
          flag = false;
          break;
        case "name":
          property = "name";
          flag = false;
          break;
      }
      if (getsSelectList.value.length > 0) {
        const data = await fuzzyQueryClassify({
          size: 10,
          page: currentPage.value - 1,
          property: property,
          flag: flag,
          keyWord: keyWord.value,
          tags: getsSelectList.value,
        });
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value = data.data.list;
            total.value = data.data.total;
          }
        }
      } else {
        const data = await fuzzyQuery({
          size: 10,
          page: currentPage.value - 1,
          property: property,
          flag: flag,
          keyWord: keyWord.value,
        });
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value = data.data.list;
            total.value = data.data.total;
          }
        }
      }
      NProgress.done();
    };
    const toDetail = (index: number) => {
      router.push({
        name: "shareFile",
        params: {
          id: fileList.value[index].id,
          fileInfo: JSON.stringify(fileList.value[index]),
        },
      });
    };
    const getSelectList = async (val: any[]) => {
      classify.value = val;
      NProgress.start();
      let property = "";
      let flag = false;
      switch (selectValue.value) {
        case "download":
          property = "download";
          flag = false;
          break;
        case "watch":
          property = "watch";
          flag = false;
          break;
        case "update":
          property = "update_time";
          flag = false;
          break;
        case "name":
          property = "name";
          flag = false;
          break;
      }
      if (val.length > 0) {
        const data = await fuzzyQueryClassify({
          size: 10,
          page: currentPage.value - 1,
          property: property,
          flag: flag,
          keyWord: keyWord.value,
          tags: val,
        });
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value = data.data.list;
            total.value = data.data.total;
            currentPage.value = 1;
          }
        }
      } else {
        const data = await fuzzyQuery({
          size: 10,
          page: currentPage.value - 1,
          property: property,
          flag: flag,
          keyWord: keyWord.value,
        });
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value = data.data.list;
            total.value = data.data.total;
            currentPage.value = 1;
          }
        }
      }
      NProgress.done();
      getsSelectList.value = val;
    };

    onMounted(async () => {
      const data = await fuzzyQuery({
        size: 10,
        page: 0,
        property: "download",
        flag: false,
        keyWord: keyWord.value,
      });
      skeletonFlag.value = true;
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
          total.value = data.data.total;
        }
      }
    });

    return {
      skeletonFlag,
      input,
      searchMap,
      searchSet,
      options,
      ddd,
      selectValue,
      fileList,
      total,
      coorList,
      toDetail,
      currentPage,
      getDataSet,
      deleteRow,
      tableRowClassName,
      onAddItem,
      tableStyleName,
      getSelection,
      pageChange,
      search,
      getSelectList,
      handleSelectionChange,
      getCoor,
      tableData,
      isCoor,
      isSet,
    };
  },
});
</script>

<style lang="scss" scoped>

.warning-row {
  background: #0C90b8;
}
.success-row {
  background: #0C90b8;
}

.video2 {
  width: 60px;
  height: 180px;
  border-radius: 15px;
  background-color: rgb(255, 255, 255);
  //opacity: 0.6;
  transition: all 0.3s ease-in-out;
  margin-left: 45px;
  margin-right: 45px;
  // -webkit-animation适配-webkit内核的浏览器
  // -webkit-animation: ripple 1s linear infinite;
  //animation: ripple 1s linear infinite;
}

.video2:hover {
  background-color: #859ecc;
  transform: scale(1.2);
}
@-webkit-keyframes ripple {
  0% {
    /* 在box四周添加三层白色阴影 */
    box-shadow: 0 0 0 0 rgb(255, 255, 255 / 25%),
      0 0 0 10px rgb(255, 255, 255 / 25%), 0 0 0 20px rgb(255, 255, 255 / 25%);
  }

  100% {
    /* 分别改变三层阴影的距离
          形成两帧的动画,然后在transition的过渡下形成动画 */
    box-shadow: 0 0 0 10px rgb(255, 255, 255 / 25%),
      0 0 0 20px rgb(255, 255, 255 / 25%), 0 0 0 40px rgba(50, 100, 245, 0);
  }
}
@keyframes ripple {
  0% {
    box-shadow: 0 0 0 0 rgb(255, 255, 255 / 25%),
      0 0 0 10px rgb(255, 255, 255 / 25%), 0 0 0 20px rgb(255, 255, 255 / 25%);
  }
  100% {
    box-shadow: 0 0 0 10px rgb(255, 255, 255 / 25%),
      0 0 0 20px rgb(255, 255, 255 / 25%), 0 0 0 40px rgba(255, 255, 255, 0);
  }
}
.data-main {
  .data-body {
    .body {
      width: 1550px;
      margin-left: 100px;
      margin: 0 auto;
      margin-top: 10px;
      display: flex;
      .left {
        width: 400px;
        background: #f6f7fa;
        min-height: calc(100vh - 170px);
        .content {
          margin: 20px 15px 10px 15px;
          .title {
            height: 50px;
            border-bottom: solid 1px #d2d2d2;
            text-align: center;
          }
        }
      }
      .right {
        width: 1000px;
        //float:left;

        .list {
          margin: 30px 20px 10px 20px;
          .statistics {
            height: 50px;
            position: relative;
            .result {
              position: absolute;
              left: 0px;
              top: 20px;
              span {
                color: #22a4f1;
              }
            }
            .sort {
              position: absolute;
              right: 0px;
              top: 20px;
            }
          }
        }
        .card {
          cursor: pointer;
          margin-bottom: 40px;
        }
      }
    }
    .body2 {
      width: 150px;
    }
  }
}
.pagination {
  margin-top: 40px;
  margin-bottom: 40px;
  display: flex;
  justify-content: space-around;
}

.video {
  width: 366px;
  height: 61px;
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 0.6);
  //opacity: 0.6;
  transition: all 0.3s ease-in-out;
  // -webkit-animation适配-webkit内核的浏览器
  //-webkit-animation: ripple 1s linear infinite;
  //animation: ripple 1s linear infinite;
}

.video:hover {
  background-color: #ffffff;
  transform: scale(1.2);
}
@-webkit-keyframes ripple {
  0% {
    /* 在box四周添加三层白色阴影 */
    box-shadow: 0 0 0 0 rgb(255, 255, 255 / 25%),
      0 0 0 10px rgb(255, 255, 255 / 25%), 0 0 0 20px rgb(255, 255, 255 / 25%);
  }

  100% {
    /* 分别改变三层阴影的距离
          形成两帧的动画,然后在transition的过渡下形成动画 */
    box-shadow: 0 0 0 10px rgb(255, 255, 255 / 25%),
      0 0 0 20px rgb(255, 255, 255 / 25%), 0 0 0 40px rgba(50, 100, 245, 0);
  }
}
@keyframes ripple {
  0% {
    box-shadow: 0 0 0 0 rgb(255, 255, 255 / 25%),
      0 0 0 10px rgb(255, 255, 255 / 25%), 0 0 0 20px rgb(255, 255, 255 / 25%);
  }
  100% {
    box-shadow: 0 0 0 10px rgb(255, 255, 255 / 25%),
      0 0 0 20px rgb(255, 255, 255 / 25%), 0 0 0 40px rgba(50, 100, 245, 0);
  }
}
</style>