<template>
  <div class="project-main">
    <el-scrollbar height="80vh" :always="false" v-if="data.length > 0">
      <div style="padding: 80px 10px">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(item, index) in data" :key="index">
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

                        <el-dropdown-item
                          :command="{ type: 'copy', index: index }"
                          >拷贝项目</el-dropdown-item
                        >

                        <el-dropdown-item
                          :command="{ type: 'delete', index: index }"
                          >删除项目</el-dropdown-item
                        >
                        <el-dropdown-item
                          :command="{ type: 'nav', index: index }"
                          >查看项目</el-dropdown-item
                        >
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </template>
            </project-card>
          </el-col>
        </el-row>
      </div>
      <div class="pagination">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :pager-count="5"
          v-model:current-page="currentPage"
          @current-change="currentChange"
          :hide-on-single-page="true"
        />
      </div>
    </el-scrollbar>
    <el-empty description="暂无数据" v-else />
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
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getAllByEmail, deleteProject } from "@/api/request";
import ProjectCard from "@/components/cards/ProjectCard.vue";
import CreateProject from "@/components/tools/CreateProject.vue";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import { notice } from "@/utils/notice";
export default defineComponent({
  components: { ProjectCard, CreateProject },
  setup() {
    const data = ref<any[]>([]);
    const total = ref(0);
    const projectInfo = ref<any>();
    const updateFlag = ref(false);
    const currentPage = ref(1);

    const getProjectList = async (page: number, size: number) => {
      const projectList = await getAllByEmail(page, size);
      if (projectList != null) {
        data.value = projectList.data.list;
        total.value = projectList.data.total;
      }
    };

    const currentChange = async (val: number) => {
      await getProjectList(val - 1, 8);
    };

    const commandHandle = (val: { type: string; index: number }) => {
      console.log(val);
      if (val.type === "nav") {
        router.push({
          name: "project",
          params: {
            id: data.value[val.index].id,
            projectInfo: JSON.stringify(data.value[val.index]),
          },
        });
      } else if (val.type === "update") {
        projectInfo.value = data.value[val.index];
        updateFlag.value = true;
      } else if (val.type === "delete") {
        ElMessageBox.confirm("确定删除该项目吗?该操作执行后无法撤销", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(async () => {
            const res = await deleteProject(data.value[val.index].id);
            if (res != null && (res as any).code === 0) {
              data.value.splice(val.index, 1);
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
      for (let i = 0; i < data.value.length; i++) {
        if (data.value[i].id === val.id) {
          data.value[i].projectName = val.projectName;
          data.value[i].isPublic = val.isPublic;
          data.value[i].avatar = val.avatar;
          updateFlag.value = false;
          return;
        }
      }
    };

    onMounted(async () => {
      await getProjectList(0, 8);
    });

    return {
      data,
      commandHandle,
      projectInfo,
      updateFlag,
      updateProject,
      total,
      currentPage,
      currentChange,
    };
  },
});
</script>

<style lang="scss" scoped>
.project-main {
  height: 80vh;
  border-bottom: solid 2px #ebeef5;
  .project-card {
    margin: 0 auto 30px;
    .operate {
      position: absolute;
      padding-left: 110px;
      display: flex;
      left: 50%;
    }
  }
  .pagination {
    position: absolute;
    bottom: 20px;
    left: calc(50% - 200px);
    width: 400px;
    margin-top: 10px;
    display: flex;
    justify-content: space-around;
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