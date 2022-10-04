<template>
  <div>
    <el-drawer
      v-model="drawer"
      title="资源类型的一些介绍！"
      direction="ltr"
      :before-close="handleClose"
      size="320px"
      :append-to-body="true"
      style="overflow: auto"
      ><div style="text-align: center">Hi, there!</div>
    </el-drawer>
    <el-collapse
      @change="handleChange"
      v-model="activeNames"
      accordion
      
    >
      <el-collapse-item
        v-for="(item, indexs) in categoryList"
        :key="indexs"
        :title="item.title"
        :name="indexs"
        active

      >
        <!-- <div>
          <el-card
            shadow="always"
            style="margin-left: 50px; margin-top: 5px"
            class="video"
            @click="drawer = true"
          >
            点我介绍此条目下数据
          </el-card>
        </div> -->
        <!-- <el-card shadow="always" style="margin-top: 10px;margin-bottom:10px; margin-left: 30px; ">
      <p style="text-align :center;font-weight: bold;font-size: large;">{{desciription[index]}}</p>
      </el-card> -->
        <div v-for="(dataItem, index) in item.data" :key="index">
          <!-- :label="dataItem.name + '（' + dataItem.count + '）'"  -->

          <el-card
            shadow="always"
            :style="[
              { marginLeft: dataItem.count == false ? '50px' : '0px' },
              { marginTop: '5px' },
            ]"
            class="video"
          >
            <el-checkbox
              :label="dataItem.name"
              size="default"
              @change="
                change(dataItem, index);
                dataItem.count = !dataItem.count;
              "
            />
          </el-card>
        </div>
        <!-- <hr style="border-color: #d8d8d8" /> -->
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
    const desciription = reactive(["地形数据、水文数据、工程数据"]);
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
        title: "基础地理数据",
        data: [
          {
            name: "地形数据",
            count: false,
          },
          {
            name: "水文数据",
            count: false,
          },
        ],
      },
      {
        title: "工程数据",
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
        title: "Excel数据",
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
        title: "模型数据",
        data: [
          {
            name: "流场",
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
    ]);
    const selectList = ref<any[]>([]);
    const drawer = ref(false);
    const itemChosen =ref(false)
    const handleChange = (val: string[]) => {
      itemChosen.value=!itemChosen.value
      // if(itemChosen.value == true)
      // document.getElementsByTagName('body')[0].style.setProperty('--test', '#859ecc');
      // else
      // document.getElementsByTagName('body')[0].style.setProperty('--test', '#358b5c');
      //  console.log(val);
      //  console.log(itemChosen.value)
    };
    const setIconStyle = computed(() => {
      return "marginTop: 5px; marginLeft: 100px";
    });
    const mar = reactive([
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
      false,
    ]);
    const change = (val: any, index: any) => {
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
      setIconStyle,
      mar,
      drawer,
      itemChosen,
    };
  },
});
</script>

<style lang="scss" scoped>
//用var来盛放--test变量名，用于js做动态修改，这里将后面的默认值去掉了后才可达到效果
$fontColor: var(--test);
.el-collapse /deep/ .el-collapse-item__header {
  background: #f6f7fa;
  font-size: 19px;
  font-weight: bold;
  color:$fontColor
}
.el-collapse /deep/ .el-collapse-item__header.is-active {
  color:#358b5c
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