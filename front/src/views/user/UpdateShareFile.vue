<template>
  <div class="upload-share">
    <page-header :pageTitle="'修改共享条目'"></page-header>
    <div class="main">
      <div class="head">
        <strong>修改共享文件条目</strong>
      </div>
      <div class="des">
        请确保以下内容的<strong>真实性</strong>及<strong>完整性</strong>，以便管理员审核通过！审核工作预计在7个工作日内完成
      </div>
      <el-divider />
      <el-form
        label-width="130px"
        :model="form"
        :rules="fileRules"
        ref="fileRef"
      >
        <el-form-item label="条目名：" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="条目描述：" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            resize="none"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="标签：" prop="tagList">
          <el-select
            v-model="form.tagList"
            multiple
            placeholder="标签"
            
          >
            <el-option-group
              v-for="(group, groupIndex) in options"
              :key="groupIndex"
              :label="group.title"
            >
              <el-option
                v-for="(item, index) in group.data"
                :key="index"
                :label="item.name"
                :value="item.name"
              />
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="原始数据：" prop="origin">
          <el-button type="primary" plain @click="openFolder('origin')">
            添加<el-icon class="el-icon--right"><Upload /></el-icon>
          </el-button>
          <el-tag
            closable
            v-if="
              form.origin.name != '' &&
              form.origin.name != undefined &&
              form.origin.name != null
            "
            size="large"
            class="tag"
            type="success"
            @close="tagClose('origin')"
          >
            {{ form.origin.name }}
          </el-tag>
        </el-form-item>
        <el-form-item label="整合数据：">
          <el-button type="primary" plain @click="openFolder('struct')">
            添加<el-icon class="el-icon--right"><Upload /></el-icon>
          </el-button>
          <el-tag
            closable
            v-if="
              form.struct.name != '' &&
              form.struct.name != undefined &&
              form.struct.name != null
            "
            size="large"
            class="tag"
            type="success"
            @close="tagClose('struct')"
          >
            {{ form.struct.name }}
          </el-tag>
        </el-form-item>
        <el-form-item label="可视化数据：">
          <el-button type="primary" plain @click="openFolder('visual')">
            添加<el-icon class="el-icon--right"><Upload /></el-icon>
          </el-button>
          <el-tag
            closable
            v-if="
              form.visual.name != '' &&
              form.visual.name != undefined &&
              form.visual.name != null
            "
            size="large"
            class="tag"
            type="success"
            @close="tagClose('visual')"
          >
            {{ form.visual.name }}
          </el-tag>
        </el-form-item>
        <el-form-item label="条目封面：">
          <avatar-upload @upload="upload"></avatar-upload>
        </el-form-item>

        <el-form-item label="条目缩略图：">
          <!-- <avatar-upload @upload="uploadTh"></avatar-upload> -->
          <thumb-upload @uploadTh="uploadTh"></thumb-upload>
        </el-form-item>
      </el-form>
      <el-divider />
      <el-form
        label-width="130px"
        :model="metaForm"
        ref="metaRef"
        :rules="metaRules"
      >
        <el-form-item label="数据提供方：" prop="provider">
          <el-input v-model="metaForm.provider" />
        </el-form-item>
        <el-form-item label="联系电话：">
          <el-input v-model="metaForm.phone" />
        </el-form-item>
        <el-form-item label="联系邮箱：">
          <el-input v-model="metaForm.email" />
        </el-form-item>
        <el-form-item label="联系地址：">
          <el-input v-model="metaForm.address" />
        </el-form-item>
        <el-form-item label="原始数据类型：" prop="type">
          <el-input v-model="metaForm.type" />
        </el-form-item>
        <el-form-item label="数据时间：">
          <el-input v-model="metaForm.time" />
        </el-form-item>
        <el-form-item label="数据范围：">
          <el-input v-model="metaForm.range" />
        </el-form-item>
        <el-form-item label="数据获取方式：" prop="getMode">
          <el-radio-group v-model="metaForm.getMode">
            <el-radio label="在线获取" />
            <el-radio label="订单获取" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="数据详情：">
          <div style="border: 1px solid #ccc">
            <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              mode="default"
            />
            <Editor
              style="height: 320px; overflow-y: hidden"
              v-model="metaForm.valueHtml"
              :defaultConfig="editorConfig"
              mode="default"
              @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
    </div>
    <div class="btn">
      <el-button type="primary" plain @click="commit(fileRef, metaRef)"
        >更新</el-button
      >
    </div>

    <el-dialog v-model="folderFlag" width="700px" :show-close="false">
      <resource-dialog
        :type="resourceType"
        @selectedFile="selectedFile"
      ></resource-dialog>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  reactive,
  ref,
  shallowRef,
  onBeforeUnmount,
  watch,
  computed,
  onMounted,
} from "vue";
import "@wangeditor/editor/dist/css/style.css"; // 引入 css
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { IDomEditor } from "@wangeditor/editor";
import PageHeader from "@/components/page/PageHeader.vue";
import ResourceDialog from "@/components/dialog/ResourceDialog.vue";
import { updateShareFileNoAvatar, updateShareFile } from "@/api/request";
import { notice } from "@/utils/notice";
import type { FormInstance } from "element-plus";
import AvatarUpload from "@/components/upload/AvatarUpload.vue";
import ThumbUpload from "@/components/upload/ThumbUpload.vue";
import router from "@/router";
import { useStore } from "@/store";

export default defineComponent({
  components: {
    PageHeader,
    Editor,
    Toolbar,
    ResourceDialog,
    AvatarUpload,
    ThumbUpload,
  },
  setup() {
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const folderFlag = ref(false);
    const updateAvatar = ref(false);
    const updateThumbnail = ref(false);
    const resourceType = ref("");
    const editorRef = shallowRef<IDomEditor>();
    const toolbarConfig = {};
    const editorConfig = {
      scroll: true,
      autoFocus: true,
    };
    const thumbFlag = ref(false);
    const updateAvatarFlag = ref(false);
    const check = () => {
      if (form.avatar) updateAvatarFlag.value = true;
    };

    const fileRef = ref<HTMLElement>();
    const metaRef = ref<HTMLElement>();
    const status = ref(
      (router.currentRoute.value.params.fileInfo as any).status
    );

    const handleCreated = (editor: any) => {
      editorRef.value = editor; // 记录 editor 实例，重要！
    };

    const openFolder = (type: string) => {
      resourceType.value = type;
      folderFlag.value = true;
    };

    const selectedFile = (val: any) => {
      folderFlag.value = false;
      switch (val.type) {
        case "origin":
          form.origin.name = val.file.name;
          form.origin.address = val.file.address;
          break;
        case "struct":
          form.struct.name = val.file.name;
          form.struct.address = val.file.address;
          break;
        case "visual":
          form.visual.name = val.file.name;
          form.visual.address = val.file.address;
          break;
      }
    };
    const tagClose = (type: string) => {
      switch (type) {
        case "origin":
          form.origin.name = "";
          form.origin.address = "";
          break;
        case "struct":
          form.struct.name = "";
          form.struct.address = "";
          break;
        case "visual":
          form.visual.name = "";
          form.visual.address = "";
          break;
      }
    };

    const upload = (val: any) => {
      // if (val == router.currentRoute.value.params.avatar)
      //   updateAvatar.value = true;
      form.avatar = val;
      updateAvatarFlag.value = true;
    };

    const uploadTh = (val: any) => {
      // if (val == router.currentRoute.value.params.thumbnail)
      //   updateThumbnail.value = true;
      form.thumbnail = val;
      thumbFlag.value = true;
    };

    const commit = async (
      formEl1: FormInstance | undefined,
      formEl2: FormInstance | undefined
    ) => {
      if (
        form.avatar == 
        (router.currentRoute.value.params.fileInfo as any).avatar
      )
        updateAvatar.value = true;
      if (
        form.thumbnail ==
        (router.currentRoute.value.params.fileInfo as any).thumbnail
      )
        updateThumbnail.value = true;
      if (!formEl1 || !formEl2) return;
      await formEl1.validate(async (valid1, fields) => {
        await formEl2.validate(async (valid2) => {
          if (valid1 && valid2) {
            if (status.value === 1) {
            } else if (status.value === -1) {
              const jsonData = {
                id: router.currentRoute.value.params.id,
                provider: metaForm.provider,
                time: metaForm.time,
                range: metaForm.range,
                detail: metaForm.valueHtml,
                type: metaForm.type,
                providerPhone: metaForm.phone,
                providerEmail: metaForm.email,
                providerAddress: metaForm.address,
                getOnline: metaForm.getMode === "在线获取" ? true : false,
                name: form.name,
                description: form.description,
                originName: form.origin.name,
                structuredName: form.struct.name,
                visualName: form.visual.name,
                originAddress: form.origin.address,
                visualSource: "",
                visualType: "",
                structuredSource: "",
                tags: form.tagList,
                avatar: form.avatar,
                thumbnail: form.thumbnail,
              };
              if ((router.currentRoute.value.params.fileInfo as any).avatar)
                updateAvatarFlag.value = true;
              // console.log(
              //   "fff" +
              //     (router.currentRoute.value.params.fileInfo as any).avatar
              // );
              // console.log("ggg1" + form.avatar);
              // console.log("ggg2" + form.thumbnail);

              if (updateAvatar.value == true) {
                const file1 = new File([], "index.js", {
                  type: "text/javascript",
                });
                form.avatar = file1;
              }
              if (updateThumbnail.value == true) {
                const file2 = new File([], "index.js", {
                  type: "text/javascript",
                });
                form.thumbnail = file2;
              }
              console.log("hh" + updateThumbnail.value);

              if (true) {
                //delete jsonData.avatar;
                const formData = new FormData();
                formData.append("jsonString", JSON.stringify(jsonData));
                formData.append("multipartFile", form.avatar);
                formData.append("multipartFile2", form.thumbnail);
                const data = await updateShareFile(formData);
                if (data != null) {
                  if ((data as any).code === 0) {
                    notice("success", "成功", "更新成功!");
                  } else if ((data as any).code === -99) {
                    notice("warning", "警告", "您没有权限！");
                  } else {
                    notice("error", "错误", "数据公布错误!");
                  }
                }
              } else {
                const data = await updateShareFileNoAvatar(jsonData);
                if (data != null) {
                  if ((data as any).code === 0) {
                    notice("success", "成功", "更新成功!");
                  } else if ((data as any).code === -99) {
                    notice("warning", "警告", "您没有权限！");
                  } else {
                    notice("error", "错误", "数据公布错误!");
                  }
                }
              }
            }
          }
        });
      });
    };

    const options = ref([     {
        title: "一级分类（必选）",
        data: [
          {
            name: "基础地形数据",
            count: false,
          },
          {
            name: "基础水文数据",
            count: false,
          },
          {
            name: "基础工程数据",
            count: false,
          },
          {
            name: "整合资料库",
            count: false,
          },
          {
            name: "数模案例库",
            count: false,
          },
          {
            name: "物模案例库",
            count: false,
          },
          {
            name: "影像资料库",
            count: false,
          },
          {
            name: "辅助资料库",
            count: false,
          },
          {
            name: "元数据",
            count: false,
          },
        ],
      },
        {
        title: "基础地形数据",
        data: [
          {
            name: "栅格TXT文件",
            count: false,
          },
                    {
            name: "栅格ASC文件",
            count: false,
          },
        ]
        },
                {
        title: "基础水文数据",
        data: [
          {
            name: "潮位数据",
            count: false,
          },
                    {
            name: "流速流向数据",
            count: false,
          },
                              {
            name: "含沙量数据",
            count: false,
          },
                              {
            name: "流量数据",
            count: false,
          },
                                        {
            name: "输沙率数据",
            count: false,
          },
          {
            name: "悬移质数据",
            count: false,
          },          {
            name: "冲淤数据",
            count: false,
          },
                    {
            name: "深泓线数据",
            count: false,
          },
                    {
            name: "沙滩数据",
            count: false,
          },
                    {
            name: "床沙数据",
            count: false,
          },
              {
            name: "含盐度数据",
            count: false,
          },
                        {
            name: "风速风向数据",
            count: false,
          },
                                  {
            name: "报告文字数据",
            count: false,
          },
                                            {
            name: "水文测验布置",
            count: false,
          },
        ]
        },

      {
        title: "基础工程数据",
        data: [
                    {
            name: "DWG工程文件",
            count: false,
          },
          {
            name: "码头工程",
            count: false,
          },
                    {
            name: "桥梁工程",
            count: false,
          },
                              {
            name: "规划未实施工程",
            count: false,
          },
                              {
            name: "水利工程",
            count: false,
          },   
          {
            name: "护岸工程",
            count: false,
          },      
                    {
            name: "航道整治工程",
            count: false,
          },    
                              {
            name: "水利工程",
            count: false,
          },                        {
            name: "航标",
            count: false,
          },  
        ],
      },
      {
        title: "整合地形数据",
        data: [
          {
            name: "SHAPEFILE",
            count: false,
          },
          {
            name: "等高线",
            count: false,
          },
                    {
            name: "等深线",
            count: false,
          },
                    {
            name: "高程点",
            count: false,
          },
                    {
            name: "边界",
            count: false,
          },
                    {
            name: "TIN",
            count: false,
          },          {
            name: "DEM",
            count: false,
          },
        ],
      },
      {
        title: "整合水文数据",
        data: [
          {
            name: "MDB关系数据库",
            count: false,
          },
        ],
      },
      {
        title: "整合工程数据",
        data: [
                   {
            name: "DWG工程文件",
            count: false,
          },
          {
            name: "码头工程",
            count: false,
          },
                    {
            name: "桥梁工程",
            count: false,
          },
                              {
            name: "规划未实施工程",
            count: false,
          },
                              {
            name: "水利工程",
            count: false,
          },   
          {
            name: "护岸工程",
            count: false,
          },      
                    {
            name: "航道整治工程",
            count: false,
          },    
                              {
            name: "水利工程",
            count: false,
          },                        {
            name: "航标",
            count: false,
          },  
        ],
      },
            {
        title: "数模案例库",
        data: [
          {
            name: "流场",
            count: false,
          },
        ],
      },
                  {
        title: "物模案例库",
        data: [
          {
            name: "流速",
            count: false,
          },
                    {
            name: "泥沙",
            count: false,
          },
                    {
            name: "水位",
            count: false,
          },
                              {
            name: "视频",
            count: false,
          },
          {
            name: "照片",
            count: false,
          },
        ],
      },     
       {
        title: "影像资料库",
        data: [
          {
            name: "遥感影像",
            count: false,
          },
        ],
      },
             {
        title: "辅助资料库",
        data: [
          {
            name: "地名数据",
            count: false,
          },          {
            name: "固定断面线",
            count: false,
          },          {
            name: "制导线",
            count: false,
          },
        ],
      },             {
        title: "元数据",
        data: [
          {
            name: "Pdf",
            count: false,
          },          {
            name: "Word",
            count: false,
          },          {
            name: "PPT",
            count: false,
          },
        ],
      },
     
    ]);

    const form = reactive({
      name: (router.currentRoute.value.params.fileInfo as any).name,
      description: (router.currentRoute.value.params.fileInfo as any)
        .description,
      tagList: (router.currentRoute.value.params.fileInfo as any).tags,
      origin: {
        name: (router.currentRoute.value.params.fileInfo as any).originName,
        address: (router.currentRoute.value.params.fileInfo as any)
          .originAddress,
      },
      struct: {
        name: (router.currentRoute.value.params.fileInfo as any).structuredName,
        address: (router.currentRoute.value.params.fileInfo as any)
          .structuredSource,
      },
      visual: {
        name: (router.currentRoute.value.params.fileInfo as any).visualName,
        address: (router.currentRoute.value.params.fileInfo as any)
          .visualSource,
      },
      avatar: (router.currentRoute.value.params.fileInfo as any).avatar,
      thumbnail: (router.currentRoute.value.params.fileInfo as any).thumbnail,
    });

    const metaForm = reactive({
      provider: (router.currentRoute.value.params.fileMeta as any).provider,
      time: (router.currentRoute.value.params.fileMeta as any).time,
      range: (router.currentRoute.value.params.fileMeta as any).range,
      valueHtml: (router.currentRoute.value.params.fileMeta as any).detail,
      phone: (router.currentRoute.value.params.fileMeta as any).provider_phone,
      email: (router.currentRoute.value.params.fileMeta as any).provider_email,
      address: (router.currentRoute.value.params.fileMeta as any)
        .provider_address,
      type: (router.currentRoute.value.params.fileMeta as any).type,
      getMode: (router.currentRoute.value.params.fileMeta as any).get_online
        ? "在线获取"
        : "订单获取",
    });

    watch(
      () => router.currentRoute.value.path,
      () => {
        if (router.currentRoute.value.name === "updateShare") {
          form.name = (router.currentRoute.value.params.fileInfo as any).name;
          form.description = (
            router.currentRoute.value.params.fileInfo as any
          ).description;
          form.tagList = (
            router.currentRoute.value.params.fileInfo as any
          ).tags;
          form.origin.name = (
            router.currentRoute.value.params.fileInfo as any
          ).originName;
          form.origin.address = (
            router.currentRoute.value.params.fileInfo as any
          ).originAddress;
          form.struct.name = (
            router.currentRoute.value.params.fileInfo as any
          ).structuredName;
          form.struct.address = (
            router.currentRoute.value.params.fileInfo as any
          ).structuredSource;
          form.visual.name = (
            router.currentRoute.value.params.fileInfo as any
          ).visualName;
          form.visual.address = (
            router.currentRoute.value.params.fileInfo as any
          ).visualSource;
          form.avatar = (
            router.currentRoute.value.params.fileInfo as any
          ).avatar;
          form.thumbnail = (
            router.currentRoute.value.params.fileInfo as any
          ).thumbnail;
          metaForm.provider = (
            router.currentRoute.value.params.fileMeta as any
          ).provider;
          metaForm.time = (
            router.currentRoute.value.params.fileMeta as any
          ).time;
          metaForm.range = (
            router.currentRoute.value.params.fileMeta as any
          ).range;
          metaForm.valueHtml = (
            router.currentRoute.value.params.fileMeta as any
          ).detail;
          metaForm.phone = (
            router.currentRoute.value.params.fileMeta as any
          ).provider_phone;
          metaForm.email = (
            router.currentRoute.value.params.fileMeta as any
          ).provider_email;
          metaForm.address = (
            router.currentRoute.value.params.fileMeta as any
          ).provider_address;
          metaForm.type = (
            router.currentRoute.value.params.fileMeta as any
          ).type;
          metaForm.getMode = (router.currentRoute.value.params.fileMeta as any)
            .get_online
            ? "在线获取"
            : "订单获取";
        }
      }
    );

    const validateOrigin = (rule: any, value: any, callback: any) => {
      if (value.name === "" || value.address === "") {
        return callback(new Error("原始数据不得为空！"));
      } else {
        callback();
      }
    };

    const fileRules = reactive({
      name: [{ required: true, message: "条目名不得为空！", trigger: "blur" }],
      tagList: [{ required: true, message: "标签不得为空！", trigger: "blur" }],
      origin: [{ required: true, validator: validateOrigin, trigger: "blur" }],
    });
    const metaRules = reactive({
      provider: [
        { required: true, message: "数据提供方不得为空！", trigger: "blur" },
      ],
      type: [
        { required: true, message: "数据类型不得为空！", trigger: "blur" },
      ],
      getMode: [
        { required: true, message: "数据获取方式不得为空！", trigger: "blur" },
      ],
    });
    onMounted(() => {
      // console.log("zzz1" + form.avatar);
      // console.log(
      //   "zzz11" + (router.currentRoute.value.params.fileInfo as any).avatar
      // );
      // console.log("zzz2" + form.thumbnail);
      // console.log(
      //   "zzz22" + (router.currentRoute.value.params.fileInfo as any).thumbnail
      // );
      // console.log(updateThumbnail.value);
    });
    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
      const editor = editorRef.value;
      if (editor == null) return;
      editor.destroy();
    });

    return {
      form,
      defaultProps,
      options,
      metaForm,
      editorRef,
      toolbarConfig,
      editorConfig,
      handleCreated,
      commit,
      folderFlag,
      selectedFile,
      tagClose,
      resourceType,
      openFolder,
      fileRules,
      metaRules,
      fileRef,
      metaRef,
      upload,
      uploadTh,
      check,
    };
  },
});
</script>

<style lang="scss" scoped>
.upload-share {
  .main {
    width: 900px;
    margin: 0 auto;
    .head {
      height: 50px;
      font-size: 20px;
      line-height: 50px;
    }

    .el-form {
      .tag {
        margin-left: 15px;
      }
    }
  }
  .btn {
    text-align: center;
    margin-bottom: 40px;
  }
  /deep/.el-dialog {
    .el-dialog__header {
      padding: 0;
    }
    .el-dialog__body {
      padding: 0;
    }
  }
}
</style>