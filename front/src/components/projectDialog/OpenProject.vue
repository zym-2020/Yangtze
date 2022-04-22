<template>
  <div class="select-project">
    <div class="main">
      <div class="head">
        <svg style="width: 20px; height: 20px">
          <use xlink:href="#icon-service"></use>
        </svg>
        请选择要添加的工程
      </div>
      <div class="data-select">
        <el-row :gutter="10">
          <el-col :span="12">
            <div class="scroll">
              <el-scrollbar height="200px">
                <el-radio-group v-model="radio" size="large" class="radioDiv">
                  <div v-for="(item, index) in projectList" :key="index">
                    <el-radio :label="item.id">{{ item.projectName }}</el-radio>
                  </div>
                </el-radio-group>
              </el-scrollbar>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="des">
              项目描述
              <div v-if="radio != 0">{{ description }}</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <div style="text-align: center">
        <el-button type="primary" class="btn" @click="clickCommit"
          >确定</el-button
        >
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import { getProjectId } from "@/api/request";
import {
  getCurrentProjectId,
  setCurrentProjectId,
  setCurrentProjectName,
} from "@/utils/project";
import { notice } from "@/utils/notice";

interface Project {
  id: number;
  projectName: string;
  des: string;
}

export default defineComponent({
  emits: ["selectProjectId"],
  setup(_, context) {
    const radio = ref(0);
    const projectList = ref<Project[]>([]);

    const getProject = async () => {
      let data = await getProjectId();
      if (data != null) {
        projectList.value = data.data;
      }
    };

    const description = computed(() => {
      if (radio.value === 0) {
        return "";
      } else {
        for (let i = 0; i < projectList.value.length; i++) {
          if (radio.value === projectList.value[i].id) {
            return projectList.value[i].des;
          }
        }
      }
    });

    const clickCommit = () => {
      if (radio.value === 0) {
        notice("warning", "警告", "请先选择要加载的工程！");
      } else {
        setCurrentProjectId(radio.value.toString());
        projectList.value.forEach((item) => {
          if (item.id === radio.value) {
            setCurrentProjectName(item.projectName);
          }
        });
        context.emit("selectProjectId");
      }
    };

    onMounted(async () => {
      await getProject();
      let temp = getCurrentProjectId();
      if (temp === null) {
        radio.value = 0;
      } else {
        radio.value = parseInt(temp);
      }
    });

    return {
      projectList,
      radio,
      clickCommit,
      description,
    };
  },
});
</script>

<style lang="scss" scoped>
.select-project {
  padding: 20px 10px 10px 10px;
  background: #A6BED7;
  .main {
    padding-top: 10px;
    padding-bottom: 10px;
    background: #f0f0f0;
    .head {
      margin-left: 10px;
    }
    .data-select {
      padding: 0px 10px;
      height: 230px;
      .scroll {
        margin-top: 15px;
        background: white;
        border: solid 0.5px;
        height: 200px;
        .radioDiv {
          width: auto;
          text-align: left;
          display: table;
          margin-left: 10px;
          // margin-bottom: 5px;
        }
      }
      .des {
        margin-top: 15px;
        border: solid 0.5px;
        height: 200px;
        background: white;
      }
    }

    .btn {
      margin-top: 5px;
    }
  }
}
</style>