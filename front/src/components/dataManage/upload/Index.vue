<template>
  <div>
    <el-upload
      ref="uploadRef"
      class="upload-demo"
      action=""
      :auto-upload="false"
      :multiple="true"
      :on-change="handleChange"
      
    >
      <template #trigger>
        <el-button type="primary" size="small">选择文件</el-button>
      </template>

      <el-button type="success" style="margin-left: 10px" size="small">
        上传
      </el-button>
    </el-upload>
    <!-- <div>
      <div v-for="(item, index) in fileList" :key="index">
          {{item}}
        <el-progress
          :percentage="item.percentage"
          :status="item.status"
          v-if="item.flag != -1"
        ></el-progress>
      </div>
    </div> -->
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { UploadFile, UploadFiles } from "element-plus";
export default defineComponent({
  setup() {
    const fileList = ref<Array<any>>();
    function handleChange(file: UploadFile, files: UploadFiles) {
        console.log(file)
    //   fileList.value = files;
    //   fileList.value.forEach((item) => {
    //     item.flag = -1;
    //     item.status = undefined;
    //     item.percentage = 0;
    //     item.objectName = "zym/" + item.name;
    //   });
    }

    return {
      fileList,
      handleChange,
    };
  },
});
</script>
