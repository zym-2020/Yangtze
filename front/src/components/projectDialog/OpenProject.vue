<template>
  <div class="main">
    <el-scrollbar max-height="250px">
      <el-radio-group v-model="radio" size="large" class="radioDiv">
        <div v-for="(item, index) in projectList" :key="index">
          <el-radio :label="item.id">{{ item.projectname }}</el-radio>
        </div>
      </el-radio-group>
    </el-scrollbar>
    <div v-if="radio != 0">
      {{ description }}
    </div>
    <div style="text-align: center">
      <el-button type="primary" class="btn" @click="clickCommit"
        >确定</el-button
      >
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
  projectname: string;
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
            setCurrentProjectName(item.projectname);
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
.main {
  .radioDiv {
    //   margin: 0 auto;
    width: auto;
    text-align: left;
    display: table;
    margin-bottom: 5px;
  }
  .btn {
    margin-top: 5px;
  }
}
</style>