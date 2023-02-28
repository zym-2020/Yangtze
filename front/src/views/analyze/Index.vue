<template>
  <div class="main">
    <div class="head">
      <div class="bg"></div>
      <div class="input">
        <div class="name">分析中心</div>
        <div class="text">
          综合研究分析平台以数据和模型分析库及分析功能为驱动，以中心数据库为中心，结合本地数据库的数据，支撑应用层的各种分析操作
        </div>
        <div class="text">
          您能在此浏览社区用户公开的分析工程，或前往工作空间创建自己的分析工程
        </div>
        <div>
          <el-button @click="ClickHandle"
            >前往工作空间<el-icon style="margin-left: 5px"><Right /></el-icon
          ></el-button>
        </div>
      </div>
    </div>

    <el-affix :offset="offset" v-if="affixFlag">
      <div class="search">
        <el-button type="primary" plain size="large" @click="searchClick"
          >检索</el-button
        >
        <el-input
          v-model="search"
          placeholder="项目名 / 创建人"
          size="large"
          @keydown.enter="searchClick"
        />
      </div>
    </el-affix>

    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <div class="body" v-else>
      <el-empty description="暂无数据" v-if="projects.length === 0" />
      <el-row v-else :gutter="20">
        <el-col :span="6" v-for="(item, index) in projects" :key="index">
          <project-card :info="item" :keyword="keyword"></project-card>
        </el-col>
      </el-row>
    </div>
    <div class="page">
      <el-pagination
        layout="total, prev, pager, next, jumper"
        :total="total"
        @current-change="currentChange"
        v-model:current-page="currentPage"
        :page-size="16"
        :pager-count="5"
        :background="true"
      >
      </el-pagination>
    </div>
    <page-copyright />
    <el-backtop :right="100" :bottom="100" />
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  nextTick,
  onActivated,
  onDeactivated,
  onMounted,
  ref,
} from "vue";
import { getAll } from "@/api/request";
import ProjectCard from "@/components/analyse/ProjectCard.vue";
import PageCopyright from "@/layout/components/PageCopyright.vue";
import NProgress from "nprogress";
import router from "@/router";

NProgress.configure({ showSpinner: false });
export default defineComponent({
  components: {
    ProjectCard,
    PageCopyright,
  },
  setup() {
    const projects = ref<any[]>([]);
    const search = ref("");
    const total = ref(0);
    const keyword = ref("");
    const currentPage = ref(1);
    const skeletonFlag = ref(true);
    const offset = ref(0);
    const affixFlag = ref(true);

    onActivated(() => {
      affixFlag.value = true;
    });

    onDeactivated(() => {
      affixFlag.value = false;
    });

    onMounted(async () => {
      computeOffset();
      skeletonFlag.value = true;
      const data = await getAll({
        size: 16,
        page: 0,
        keyword: keyword.value,
      });
      if (data != null && (data as any).code === 0) {
        projects.value = data.data.list;
        total.value = data.data.total;
      }
      skeletonFlag.value = false;
    });

    const searchClick = async () => {
      NProgress.start();
      keyword.value = search.value;
      const data = await getAll({
        size: 16,
        page: 0,
        keyword: keyword.value,
      });

      if (data != null) {
        if ((data as any).code === 0) {
          projects.value = data.data.list;
          total.value = data.data.total;
          currentPage.value = 1;
        }
      }
      NProgress.done();
    };

    const currentChange = async (page: number) => {
      NProgress.start();
      const data = await getAll({
        size: 16,
        page: page - 1,
        keyword: keyword.value,
      });
      if (data != null && (data as any).code === 0) {
        projects.value = data.data.list;
        total.value = data.data.total;
      }
      search.value = keyword.value;
      NProgress.done();
    };

    const computeOffset = () => {
      let div = document.createElement("div");
      div.style.height = "7vh";
      div.style.maxHeight = "none";
      div.style.boxSizing = "content-box";
      document.body.appendChild(div);
      let h = div.clientHeight;
      document.body.removeChild(div);
      console.log(h);
      offset.value = h;
    };

    const ClickHandle = () => {
      router.push({
        name: "UserSpaceProject",
      });
    };

    return {
      projects,
      search,
      total,
      currentChange,
      searchClick,
      currentPage,
      skeletonFlag,
      offset,
      keyword,
      ClickHandle,
      affixFlag,
    };
  },
});
</script>

<style lang="scss" scoped>
@keyframes ibannerbg {
  50% {
    transform: scale(1.2, 1.2);
  }
  100% {
    transform: scale(1, 1);
  }
}
.main {
  .head {
    height: 93vh;
    overflow: hidden;
    position: relative;
    .input {
      position: absolute;
      width: 40%;
      left: 30%;
      top: 15%;
      font-family: "Microsoft YaHei";
      .name {
        color: #ffc200;
        font-weight: 900;
        font-size: 200px;
        text-align: center;
      }

      .el-button {
        height: 50px;
        font-size: 20px;
        background: rgba($color: white, $alpha: 0.3);
        margin-top: 10px;
        color: white;
      }
      .text {
        margin-top: 20px;
        color: #dad5c4;
        font-size: 23px;
        line-height: 40px;
      }
    }
    .bg {
      height: 100%;
      background: url("/resource/resource6.jfif");
      background-size: cover;
      animation: ibannerbg 60s linear infinite;
    }
  }

  .search {
    height: 80px;
    background: white;
    position: relative;
    padding: 0px 150px;
    .el-input {
      margin-top: 20px;
      float: right;
      width: 600px;
    }
    .el-button {
      margin-top: 20px;
      float: right;
      margin-left: 10px;
    }
  }

  .body {
    padding: 0 150px;
  }

  .page {
    width: 100%;
    display: flex;
    justify-content: space-around;
    margin-bottom: 30px;
  }
}
/deep/.el-dialog {
  .el-dialog__header {
    padding: 10px;
    margin: 0;
    background: #25aef3;
    .el-dialog__title {
      color: white;
    }
  }
  .el-dialog__body {
    padding: 0;
  }
}
</style>