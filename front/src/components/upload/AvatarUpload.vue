<template>
  <div>
    <el-upload
      action="#"
      :show-file-list="false"
      :auto-upload="false"
      :on-change="change"
    >
      <img v-if="imageUrl" :src="imageUrl" class="avatar" />
      <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
    </el-upload>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import type { UploadFile } from "element-plus";
export default defineComponent({
  emits: ["upload"],
  setup(_, context) {
    const imageUrl = ref("");
    const file = ref<File>();

    const avatar = ref<HTMLElement>();
    const change = (uploadFile: UploadFile) => {
      if (uploadFile.status === "ready") {
        imageUrl.value = URL.createObjectURL(uploadFile.raw);
        file.value = uploadFile.raw;
        context.emit('upload', file.value)
      }
    };

    return {
      imageUrl,
      change,
      avatar,
    };
  },
});
</script>

<style lang="scss" scoped>
/deep/ .el-upload {
  border: 1px dashed #8c939d;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
  .avatar {
    width: 100%;
    height: 100%;
    display: block;
  }
}
/deep/ .el-upload:hover {
  border-color: #a6bed7;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  text-align: center;
}
</style>