<template>
  <div class="main">
    <page-header :pageTitle="'分析中心'">
      <template #search>
        <div class="search">
          <el-input v-model="search" placeholder="搜索" :autofocus="true" />
          <el-button class="btn" type="primary" plain @click="searchClick"
            >搜索</el-button
          >
          <el-button class="btn" type="info" plain @click="createFlag = true"
            >创建项目</el-button
          >
        </div>
      </template>
    </page-header>

    <div class="body">
      <el-row>
        <el-col :span="4" v-for="(item, index) in projects" :key="index">
          <div class="project">
            <project-card
              class="card"
              :projectInfo="item"
              @click="toProject(item)"
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
        :current-page="currentPage"
        :page-size="12"
        :background="true"
      >
      </el-pagination>
    </div>

    <el-dialog v-model="createFlag" width="600px" :show-close="false">
      <create-project @createProject="createProject"></create-project>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getProjects } from "@/api/request";
import ProjectCard from "@/components/cards/ProjectCard.vue";
import router from "@/router";
import CreateProject from "@/components/tools/CreateProject.vue";
import PageHeader from "@/components/page/PageHeader.vue";
export default defineComponent({
  components: {
    ProjectCard,
    CreateProject,
    PageHeader,
  },
  setup() {
    const projects = ref<any[]>([]);
    const search = ref("");
    const total = ref(0);
    const createFlag = ref(false);
    const keyWord = ref("");
    const currentPage = ref(1);

    onMounted(async () => {
      const projectList = await getProjects({
        size: 12,
        page: 0,
        keyWord: keyWord.value,
      });
      if (projectList != null) {
        projects.value = (projectList.data as any).list;
        total.value = (projectList.data as any).total;
      }
    });

    const searchClick = async () => {
      keyWord.value = search.value;
      const data = await getProjects({
        size: 12,
        page: 0,
        keyWord: keyWord.value,
      });
      
      if (data != null) {
        if ((data as any).code === 0) {
          projects.value = (data.data as any).list;
          total.value = (data.data as any).total;
          currentPage.value = 1
        }
      }
    };

    const currentChange = async (page: number) => {
      currentPage.value = page
      const projectList = await getProjects({
        size: 12,
        page: page - 1,
        keyWord: keyWord.value,
      });
      if (projectList != null) {
        projects.value = (projectList.data as any).list;
        total.value = (projectList.data as any).total;
      }
      search.value = keyWord.value;
    };

    const createProject = (val: any) => {
      createFlag.value = false;
      router.push({
        name: "project",
        params: {
          id: val.id,
          name: val.project_name,
          result: val.result,
        },
      });
    };

    const toProject = (project: {
      id: string;
      project_name: string;
      result: string;
    }) => {
      router.push({
        name: "project",
        params: {
          id: project.id,
          name: project.project_name,
          result: project.result,
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
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  height: calc(100vh - 60px);
  position: relative;
  .search {
    // height: 50px;
    // line-height: 50px;
    // box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
    .el-input {
      width: 500px;
      margin-left: 50px;
      margin-right: 20px;
    }
  }
  .body {
    margin-top: 30px;
    width: 100%;
    .el-row {
      /deep/ .el-col {
        // width: 20%;
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
    position: absolute;
    width: 100%;
    bottom: 100px;
    display: flex;
    justify-content: space-around;
  }
}
/deep/.el-dialog {
  .el-dialog__header {
    padding: 0;
  }
  .el-dialog__body {
    padding: 0;
  }
}
</style>