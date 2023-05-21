<template>
  <div class="file-upload">
    <el-upload
      ref="upload"
      :action="action"
      :show-file-list="false"
      :limit="1"
      :before-upload="handleBeforeUpload"
      :on-exceed="handleExceed"
      :on-success="handleSuccess"
      :headers="header"
    >
      <template #trigger>
        <el-button type="success" circle
          ><el-icon><FolderOpened /></el-icon
        ></el-button>
      </template>
    </el-upload>
  </div>
</template>

<script lang="ts">
/**
 * 给ParamSetting专用的组件，不具通用性
 */
import { computed, defineComponent, ref } from "vue";
import { getToken } from "@/utils/auth";
import { UploadInstance, UploadRawFile } from "element-plus";
export default defineComponent({
  props: {
    actionProp: {
      type: String,
    },
    indexProp: {
      type: Number,
    },
  },
  emits: ["responseHandle", "beforUpload"],
  setup(props, context) {
    const upload = ref<UploadInstance>();
    const action = computed(() => {
      return props.actionProp;
    });
    const header = {
      Authorization: `Bearer ${getToken()}`,
    };

    const handleExceed = (files: File[]) => {
      console.log(files);

      upload.value!.clearFiles();
      upload.value!.handleStart(files[0] as UploadRawFile);
      upload.value!.submit();
    };

    const handleSuccess = (response: any) => {
      if (response.code === 0) {
        context.emit("responseHandle", {
          index: props.indexProp,
          key: response.data.key,
          value: response.data.value,
        });
      }
    };

    const handleBeforeUpload = () => {
      context.emit("beforUpload");
    };

    return {
      upload,
      action,
      header,
      handleExceed,
      handleSuccess,
      handleBeforeUpload,
    };
  },
});
</script>

<style lang="scss" scoped>
</style>