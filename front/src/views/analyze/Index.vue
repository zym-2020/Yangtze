<template>
  <div>
    <div v-for="(item, index) in projects" :key="index">
      <el-button @click="toProject(item)">{{ item.project_name }}</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getProjects } from "@/api/request";
import router from "@/router";
export default defineComponent({
  setup() {
    const projects = ref<any[]>([]);
    onMounted(async () => {
      const projectList = await getProjects();
      if (projectList != null) {
        projects.value = projectList.data;
      }
    });

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
      toProject,
    };
  },
});
</script>

<style lang="scss" scoped>
</style>