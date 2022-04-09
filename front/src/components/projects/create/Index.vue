<template>
  <div>
    <el-form :model="projectForm" style="max-width: 460px" label-width="50">
      <el-form-item label="项目名">
        <el-input v-model="projectForm.name" />
      </el-form-item>
      <el-form-item>
        <template #label> 描&emsp;述 </template>
        <el-input v-model="projectForm.des" />
      </el-form-item>
    </el-form>
    <el-button type="primary" plain @click="commit">提交</el-button>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive } from "vue";
import { addProject } from "@/api/request";
import { notice } from "@/utils/notice";
export default defineComponent({
  setup() {
    const projectForm = reactive({
      name: "",
      des: "",
    });

    const commit = async () => {
      const data = {
        projectName: projectForm.name,
        description: projectForm.des,
        result: JSON.stringify({
          layerDataList: [],
          analysisResultList: [],
        }),
      };
      let result = (await addProject(data)) as any;
      if (result != null && result.code === 0) {
        notice("success", "成功", "创建成功");
      }
    };

    return {
      projectForm,
      commit,
    };
  },
});
</script>
