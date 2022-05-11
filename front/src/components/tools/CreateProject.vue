<template>
  <div class="create-project">
    <div class="body">
      <div class="title">创建项目</div>
      <div class="form">
        <el-form :model="form" label-width="120px" label-position="top">
          <el-form-item label="项目名：">
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="项目描述：">
            <el-input v-model="form.description" />
          </el-form-item>
        </el-form>
        <div class="btn">
          <el-button @click="commit">提交</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive } from "vue";
import { addProject, addProjectWithoutAvatar } from "@/api/request";
import { notice } from "@/utils/notice";
export default defineComponent({
  emits: ["createProject"],
  setup(_, context) {
    const form = reactive({
      name: "",
      description: "",
    });
    const result =
      '{"analyse":{"anyArea":{"analysisResultList":[],"classify":"任意区域冲淤","classifyCount":0},"area":{"analysisResultList":[],"classify":"断面面积冲淤","classifyCount":0},"boundary":{"analysisResultList":[],"classify":"边界分析","classifyCount":0},"branch":{"analysisResultList":[],"classify":"汊道断面比较","classifyCount":0},"deep":{"analysisResultList":[],"classify":"冲淤等深线","classifyCount":0},"deepContrast":{"analysisResultList":[],"classify":"等深线比较","classifyCount":0},"elev":{"analysisResultList":[],"classify":"特定高程冲淤","classifyCount":0},"line":{"analysisResultList":[],"classify":"深泓线比较","classifyCount":0},"section":{"analysisResultList":[],"classify":"断面形态","classifyCount":0},"sectionContrast":{"analysisResultList":[],"classify":"断面比较","classifyCount":0},"slope":{"analysisResultList":[],"classify":"河床坡度提取","classifyCount":0},"volume":{"analysisResultList":[],"classify":"河道容积计算","classifyCount":0}},"layerDataList":[]}';

    const commit = async () => {
      if (true) {
        const data = await addProjectWithoutAvatar({
          projectName: form.name,
          description: form.description,
          result: result,
        });
        if (data != null) {
          if ((data as any).code === 0) {
            notice("success", "成功", "创建成功");
          } else {
            notice("error", "错误", "创建错误");
          }
        }
      } else {
        const formData = new FormData();
        formData.append("projectName", form.name);
        formData.append("description", form.description);
        formData.append("result", result);
        formData.append("file", "");
        const data = await addProject(formData);
        if (data != null) {
          if ((data as any).code === 0) {
            notice("success", "成功", "创建成功");
          } else {
            notice("error", "错误", "创建错误");
          }
        }
      }

      context.emit("createProject");
    };

    return {
      form,
      commit,
    };
  },
});
</script>

<style lang="scss" scoped>
.create-project {
  height: 300px;
  padding: 20px 10px 10px 10px;
  width: 100%;
  background: #a6bed7;
  .body {
    background: #f0f0f0;
    height: 100%;
    .title {
      margin-left: 10px;
      height: 30px;
      line-height: 30px;
      font-size: 16px;
    }
    .form {
      height: calc(100% - 30px);
      width: 70%;
      margin: 0 auto;
      .btn {
        text-align: center;
      }
    }
  }
}
</style>