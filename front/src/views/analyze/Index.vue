<template>
  <div class="main">
    <div class="head">
      <div class="bg"></div>
      <div class="input">
        <div class="name">分析中心</div>
        <el-input v-model="search" placeholder="搜索项目..." size="large" />
        <el-button type="primary">搜索</el-button>
        <div class="text">
          综合研究分析平台以数据和模型分析库及分析功能为驱动，以中心数据库为中心，结合本地数据库的数据，支撑应用层的各种分析操作
        </div>
      </div>
    </div>

    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <div class="body" v-else>
      <el-empty description="暂无数据" v-if="projects.length === 0" />
      <el-row v-else>
        <el-col :span="4" v-for="(item, index) in projects" :key="index">
          <div class="project">
            <project-card
              class="card"
              :projectInfo="item"
              :flag="true"
              @click="toProject({ id: item.id, projectInfo: item })"
            ></project-card>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="page">
      <el-pagination
        :hide-on-single-page="true"
        layout="prev, pager, next"
        :total="total"
        @current-change="currentChange"
        v-model:current-page="currentPage"
        :page-size="12"
        :pager-count="5"
        :background="true"
      >
      </el-pagination>
    </div>
    <page-copyright />

    <el-dialog
      v-model="createFlag"
      width="500px"
      :show-close="false"
      title="创建项目"
    >
      <create-project @createProject="createProject"></create-project>
    </el-dialog>
    <el-backtop :right="100" :bottom="100" />
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getAll } from "@/api/request";
import ProjectCard from "@/components/cards/ProjectCard.vue";
import router from "@/router";
import CreateProject from "@/components/tools/CreateProject.vue";
import PageHeader from "@/components/page/PageHeader.vue";
import PageCopyright from "@/components/page/PageCopyright.vue";
import NProgress from "nprogress";

NProgress.configure({ showSpinner: false });
export default defineComponent({
  components: {
    ProjectCard,
    CreateProject,
    PageHeader,
    PageCopyright,
  },
  setup() {
    const projects = ref<any[]>([]);
    const search = ref("");
    const total = ref(0);
    const createFlag = ref(false);
    const keyword = ref("");
    const currentPage = ref(1);
    const skeletonFlag = ref(true);

    onMounted(async () => {
      skeletonFlag.value = true;
      const data = await getAll({
        size: 12,
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
        size: 12,
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
      currentPage.value = page;
      const data = await getAll({
        size: 12,
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

    const createProject = (val: string) => {
      createFlag.value = false;
      router.push({
        name: "project",
        params: {
          id: val,
        },
      });
    };

    const toProject = (project: { id: string; projectInfo: any }) => {
      router.push({
        name: "project",
        params: {
          id: project.id,
          projectInfo: JSON.stringify(project.projectInfo),
        },
      });
    };

    return {
      projects,
      search,
      createFlag,
      toProject,
      total,
      currentChange,
      createProject,
      searchClick,
      currentPage,
      skeletonFlag,
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
      top: 13%;
      font-family: "Microsoft YaHei";
      .name {
        color: #ffc200;
        font-weight: 900;
        font-size: 200px;
        text-align: center;
      }
      .el-input {
        height: 60px;
        width: calc(100% - 150px);
        margin-right: 10px;
      }
      .el-button {
        height: 60px;
        width: 140px;
      }
      .text {
        margin-top: 20px;
        color: white;
      }
    }
    .bg {
      height: 100%;
      background: url("/resource/analyse3.png");
      background-size: cover;
      animation: ibannerbg 60s linear infinite;
    }
  }

  .body {
    margin-top: 50px;
    width: 100%;
    .el-row {
      /deep/ .el-col {
        .project {
          width: 100%;
          .card {
            margin: 0 auto;
          }
          margin-bottom: 30px;
        }
      }
    }
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