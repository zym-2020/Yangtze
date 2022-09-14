<template>
  <div>
    <!-- <el-drawer
      v-model="drawer"
      title="资源类型的一些介绍！"
      direction="ltr"
      :before-close="handleClose"
      size="320px"
      :append-to-body="true"
      style="overflow: auto"
      ><div style="text-align: center">Hi, there!</div>
    </el-drawer> -->
    <el-collapse v-model="activeNames">
      <el-collapse-item
        v-for="(item, indexs) in categoryList"
        :key="indexs"
        :title="item.title"
        :name="indexs"
        active
      >
        <div v-for="(dataItem, index) in item.data" :key="index">
          <el-card
            shadow="always"
            :style="[
              { marginLeft: dataItem.count == false ? '110px' : '50px' },
              { marginTop: '5px' },
            ]"
            class="video"
          >
            <el-checkbox :label="dataItem.name" size="default" @change="change(indexs, index)" />
          </el-card>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, computed } from "vue";
export default defineComponent({
  emits: ["selectList"],
  setup(props, context) {
    const activeNames = reactive([0, 1, 2, 3, 4]);
    const categoryList = ref([
      {
        title: "一级分类（必选）",
        data: [
          {
            name: "基础地形数据",
            count: false,
          },
          {
            name: "基础水文数据",
            count: false,
          },
          {
            name: "基础工程数据",
            count: false,
          },
          {
            name: "整合资料库",
            count: false,
          },
          {
            name: "数模案例库",
            count: false,
          },
          {
            name: "物模案例库",
            count: false,
          },
          {
            name: "影像资料库",
            count: false,
          },
          {
            name: "辅助资料库",
            count: false,
          },
          {
            name: "元数据",
            count: false,
          },
        ],
      },
      {
        title: "基础地形数据",
        data: [
          {
            name: "栅格TXT文件",
            count: false,
          },
          {
            name: "栅格ASC文件",
            count: false,
          },
        ],
      },
      {
        title: "基础水文数据",
        data: [
          {
            name: "潮位数据",
            count: false,
          },
          {
            name: "流速流向数据",
            count: false,
          },
          {
            name: "含沙量数据",
            count: false,
          },
          {
            name: "流量数据",
            count: false,
          },
          {
            name: "输沙率数据",
            count: false,
          },
          {
            name: "悬移质数据",
            count: false,
          },
          {
            name: "冲淤数据",
            count: false,
          },
          {
            name: "深泓线数据",
            count: false,
          },
          {
            name: "沙滩数据",
            count: false,
          },
          {
            name: "床沙数据",
            count: false,
          },
          {
            name: "含盐度数据",
            count: false,
          },
          {
            name: "风速风向数据",
            count: false,
          },
          {
            name: "报告文字数据",
            count: false,
          },
          {
            name: "水文测验布置",
            count: false,
          },
        ],
      },

      {
        title: "基础工程数据",
        data: [
          {
            name: "DWG工程文件",
            count: false,
          },
          {
            name: "码头工程",
            count: false,
          },
          {
            name: "桥梁工程",
            count: false,
          },
          {
            name: "规划未实施工程",
            count: false,
          },
          {
            name: "水利工程",
            count: false,
          },
          {
            name: "护岸工程",
            count: false,
          },
          {
            name: "航道整治工程",
            count: false,
          },
          {
            name: "水利工程",
            count: false,
          },
          {
            name: "航标",
            count: false,
          },
        ],
      },
      {
        title: "整合地形数据",
        data: [
          {
            name: "SHAPEFILE",
            count: false,
          },
          {
            name: "等高线",
            count: false,
          },
          {
            name: "等深线",
            count: false,
          },
          {
            name: "高程点",
            count: false,
          },
          {
            name: "边界",
            count: false,
          },
          {
            name: "TIN",
            count: false,
          },
          {
            name: "DEM",
            count: false,
          },
        ],
      },
      {
        title: "整合水文数据",
        data: [
          {
            name: "MDB关系数据库",
            count: false,
          },
        ],
      },
      {
        title: "整合工程数据",
        data: [
          {
            name: "DWG工程文件",
            count: false,
          },
          {
            name: "码头工程",
            count: false,
          },
          {
            name: "桥梁工程",
            count: false,
          },
          {
            name: "规划未实施工程",
            count: false,
          },
          {
            name: "水利工程",
            count: false,
          },
          {
            name: "护岸工程",
            count: false,
          },
          {
            name: "航道整治工程",
            count: false,
          },
          {
            name: "水利工程",
            count: false,
          },
          {
            name: "航标",
            count: false,
          },
        ],
      },
      {
        title: "数模案例库",
        data: [
          {
            name: "流场",
            count: false,
          },
        ],
      },
      {
        title: "物模案例库",
        data: [
          {
            name: "流速",
            count: false,
          },
          {
            name: "泥沙",
            count: false,
          },
          {
            name: "水位",
            count: false,
          },
          {
            name: "视频",
            count: false,
          },
          {
            name: "照片",
            count: false,
          },
        ],
      },
      {
        title: "影像资料库",
        data: [
          {
            name: "遥感影像",
            count: false,
          },
        ],
      },
      {
        title: "辅助资料库",
        data: [
          {
            name: "地名数据",
            count: false,
          },
          {
            name: "固定断面线",
            count: false,
          },
          {
            name: "制导线",
            count: false,
          },
        ],
      },
      {
        title: "元数据",
        data: [
          {
            name: "Pdf",
            count: false,
          },
          {
            name: "Word",
            count: false,
          },
          {
            name: "PPT",
            count: false,
          },
        ],
      },
    ]);
    const selectList: string[] = [];


    const change = (indexs: number, index: number) => {
      const str = categoryList.value[indexs].data[index].name
      for (let i = 0; i < selectList.length; i++) {
        if(str === selectList[i]) {
          selectList.splice(i, 1)
          context.emit("selectList", selectList)
          return;
        }
      }
      selectList.push(str)
      context.emit("selectList", selectList)
    };

    return {
      activeNames,
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

.video {
  width: 319px;
  height: 60px;
  border-radius: 10px;
  background-color: rgb(255, 255, 255);
  //opacity: 0.6;
  transition: all 0.3s ease-in-out;
  // -webkit-animation适配-webkit内核的浏览器
  // -webkit-animation: ripple 1s linear infinite;
  //animation: ripple 1s linear infinite;
}

.video:hover {
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
</style>