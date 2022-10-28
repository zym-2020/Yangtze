<template>
  <div>
    <el-collapse @change="handleChange" v-model="activeNames" accordion>
      <el-collapse-item
        v-for="(item, indexs) in categoryList.list"
        :key="indexs"
        :title="item.title"
        :name="indexs"
      >
        <div v-for="(dataItem, index) in item.data" :key="index">
          <el-checkbox
            :label="dataItem.name"
            size="default"
            @change="
              change(dataItem);
              getRealList(dataItem.name);
              dataItem.count = !dataItem.count;
            "
          />
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, computed } from "vue";
export default defineComponent({
  emits: ["selectList", "selectTitle", "tagRealList"],
  setup(props, context) {
    const activeNames = reactive([]);
    const desciription = reactive(["地形数据、水文数据、工程数据"]);
    const tagList = ref<any[]>([]);
    const categoryList = reactive({
      list: [
        {
          title: "地形数据",
          data: [
            {
              name: "DEM",
              count: false,
            },
            {
              name: "边界",
              count: false,
            },
            {
              name: "等高线",
              count: false,
            },
            {
              name: "DWG",
              count: false,
            },
            {
              name: "高程点",
              count: false,
            },
            {
              name: "固定断面线",
              count: false,
            },

            {
              name: "深泓线",
              count: false,
            },
          ],
        },
        {
          title: "工程数据",
          data: [
            {
              name: "航标",
              count: false,
            },
            {
              name: "护岸工程",
              count: false,
            },
            {
              name: "码头工程",
              count: false,
            },
            {
              name: "水利工程",
              count: false,
            },
            {
              name: "整治工程",
              count: false,
            },
            {
              name: "桥梁工程",
              count: false,
            },
          ],
        },

        {
          title: "物理模型",
          data: [
            {
              name: "浓度场",
              count: false,
            },
            {
              name: "照片",
              count: false,
            },
          ],
        },
        {
          title: "水文数据",
          data: [
            {
              name: "潮位",
              count: false,
            },
            {
              name: "断面输沙率",
              count: false,
            },
            {
              name: "含沙量",
              count: false,
            },
            {
              name: "含盐度",
              count: false,
            },
            {
              name: "流速流向",
              count: false,
            },
            {
              name: "悬移质",
              count: false,
            },
          ],
        },
        {
          title: "遥感影像",
          data: [
            {
              name: "遥感影像",
              count: false,
            },
          ],
        },
      ],
    });
    const selectList = ref<any[]>([]);
    const drawer = ref(false);
    const itemChosen = ref(false);
    const preTitile = ref("");
    //传递标签
    const getRealList = (val: string) => {
      let flag = 0;
      for (let i = 0; i < tagList.value.length; i++) {
        if (tagList.value[i] == val) {
          tagList.value.splice(i, 1);
          flag = 1;
          break;
        }
      }
      if (flag == 0) tagList.value.push(val);
      context.emit("tagRealList", tagList.value);
    };
    const handleChange = (val: string[]) => {
      if (
        categoryList.list[val as any]?.title == undefined ||
        categoryList.list[val as any]?.title != preTitile.value
      ) {
        selectList.value = [];
        for (let i = 0; i < categoryList.list.length; i++)
          for (let j = 0; j < categoryList.list[i].data.length; j++)
            categoryList.list[i].data[j].count = false;
      }
      preTitile.value = categoryList.list[val as any]?.title;
      context.emit("selectList", selectList.value);
      context.emit("selectTitle", categoryList.list[val as any]?.title);
    };
    const setIconStyle = computed(() => {
      return "marginTop: 5px; marginLeft: 100px";
    });
    const change = (val: any) => {
      for (let i = 0; i < selectList.value.length; i++) {
        if (val.name === selectList.value[i]) {
          selectList.value.splice(i, 1);
          context.emit("selectList", selectList.value);
          return;
        }
      }
      console.log("我在这走");
      selectList.value.push(val.name);
      context.emit("selectList", selectList.value);
    };
    return {
      activeNames,
      desciription,
      handleChange,
      categoryList,
      change,
      setIconStyle,
      drawer,
      itemChosen,
      getRealList,
    };
  },
});
</script>

<style lang="scss" scoped>
//用var来盛放--test变量名，用于js做动态修改，这里将后面的默认值去掉了后才可达到效果
$fontColor: var(--test);
/deep/.el-checkbox__inner {
  border: none;
  width: 0;
}

/deep/.el-collapse {
  border: none;
}
.el-collapse /deep/.el-collapse-item__content {
  background: rgba(255, 255, 255, 0.3);
  border: none;
}

.el-collapse /deep/.el-checkbox__label {
  font-size: 15px;
  font-weight: bold;
}

.el-collapse /deep/ .el-collapse-item__header {
  background: #f6f7fa;
  font-size: 19px;
  font-weight: bold;
  color: $fontColor;
}
.el-collapse /deep/ .el-collapse-item__header.is-active {
  color: #ff9933;
}
.el-collapse /deep/ .el-collapse-item__wrap {
  background: #f6f7fa;
}
.video {
  width: 250px;
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