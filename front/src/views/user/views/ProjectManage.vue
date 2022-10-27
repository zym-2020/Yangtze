<template>
  <div class="body">
    <div class="head">
      <el-input v-model="search" placeholder="搜索" @keyup.enter="searchFile" />
      <el-button type="primary" plain @click="searchFile">搜索</el-button>
    </div>
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <el-row v-else>
      <el-col :span="4" v-for="(item, index) in projectList" :key="index">
        <project-card :flag="false" :projectInfo="item">
          <template #operate>
            <div class="operate">
              <el-dropdown trigger="click" @command="commandHandle">
                <svg style="width: 16px; height: 16px; margin-top: 5px">
                  <use xlink:href="#icon-caozuo"></use>
                </svg>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item
                      :command="{ type: 'update', index: index }"
                      >修改信息</el-dropdown-item
                    >

                    <el-dropdown-item :command="{ type: 'copy', index: index }"
                      >拷贝项目</el-dropdown-item
                    >

                    <el-dropdown-item
                      :command="{ type: 'delete', index: index }"
                      >删除项目</el-dropdown-item
                    >
                    <el-dropdown-item :command="{ type: 'nav', index: index }"
                      >跳转</el-dropdown-item
                    >
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </template>
        </project-card>
      </el-col>
    </el-row>
    <el-dialog
      v-model="updateFlag"
      width="500px"
      :show-close="false"
      title="修改项目"
    >
      <create-project
        :info="projectInfo"
        @updateProject="updateProject"
      ></create-project>
    </el-dialog>
    <div class="pagination">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        v-model:current-page="currentPage"
        @current-change="currentChange"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getAllByAdmin, deleteProject } from "@/api/request";
import ProjectCard from "@/components/cards/ProjectCard.vue";
import router from "@/router";
import { notice } from "@/utils/notice";
import { ElMessageBox } from "element-plus";
import CreateProject from "@/components/tools/CreateProject.vue";
import NProgress from 'nprogress'
NProgress.configure({ showSpinner: false })
export default defineComponent({
  components: { ProjectCard, CreateProject },
  setup() {
    const search = ref("");
    const keyword = ref("");

    const projectList = ref<any[]>([]);
    const total = ref(0);
    const skeletonFlag = ref(true);
    const projectInfo = ref<any>();
    const updateFlag = ref(false);
    const currentPage = ref(1);

    const getData = async (page: number, size: number) => {
      NProgress.start()
      const data = await getAllByAdmin({
        keyword: keyword.value,
        page: page,
        size: size,
      });
      if (data != null && (data as any).code === 0) {
        projectList.value = data.data.list;
        total.value = data.data.total;
      }
      NProgress.done()
    };

    const searchFile = async () => {
      keyword.value = search.value;
      await getData(0, 10);
    };

    const currentChange = async (val: number) => {
      await getData(val - 1, 10);
      currentPage.value = 1;
      search.value = keyword.value;
    };
    const commandHandle = (val: { type: string; index: number }) => {
      if (val.type === "nav") {
        router.push({
          name: "project",
          params: {
            id: projectList.value[val.index].id,
            projectInfo: JSON.stringify(projectList.value[val.index]),
          },
        });
      } else if (val.type === "update") {
        projectInfo.value = projectList.value[val.index];
        updateFlag.value = true;
      } else if (val.type === "delete") {
        ElMessageBox.confirm("确定删除该项目吗?该操作执行后无法撤销", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(async () => {
            const res = await deleteProject(projectList.value[val.index].id);
            if (res != null && (res as any).code === 0) {
              projectList.value.splice(val.index, 1);
              notice("success", "成功", "删除成功");
            }
          })
          .catch(() => {});
      }
    };

    const updateProject = (val: {
      avatar: string;
      id: string;
      projectName: string;
      isPublic: boolean;
    }) => {
      for (let i = 0; i < projectList.value.length; i++) {
        if (projectList.value[i].id === val.id) {
          projectList.value[i].projectName = val.projectName;
          projectList.value[i].isPublic = val.isPublic;
          projectList.value[i].avatar = val.avatar;
          updateFlag.value = false;
          return;
        }
      }
    };

    onMounted(async () => {
      console.log(1)
      skeletonFlag.value = true;
      await getData(0, 10);
      skeletonFlag.value = false;
    });

    return {
      search,
      searchFile,
      projectList,
      commandHandle,
      skeletonFlag,
      updateFlag,
      projectInfo,
      updateProject,
      currentChange,
      currentPage,
      total,
    };
  },
});
</script>

<style lang="scss" scoped>
.body {
  height: 100%;
  position: relative;
  .head {
    padding-top: 20px;
    padding-bottom: 70px;
    .el-input {
      width: 400px;
      margin-left: 30px;
      margin-right: 20px;
    }
  }
  .project-card {
    margin: 0 auto;
    .operate {
      position: absolute;
      padding-left: 110px;
      display: flex;
      left: 50%;
    }
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
.pagination {
  position: absolute;
  bottom: 150px;
  width: 450px;
  left: calc(50% - 225px);
  margin-top: 10px;
  display: flex;
  justify-content: space-around;
}
</style>