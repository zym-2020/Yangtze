<template>
  <div class="upload-share">
    <page-header :pageTitle="'创建共享条目'"></page-header>
    <div class="main">
      <div class="head">
        <strong>创建新的共享文件条目</strong>
      </div>
      <div class="des">
        请确保以下内容的<strong>真实性</strong>及<strong>完整性</strong>，以便管理员审核通过！审核工作预计在7个工作日内完成
      </div>
      <el-divider />
      <el-form label-width="100px" :model="form">
        <el-form-item label="条目名：">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="条目描述：">
          <el-input
            v-model="form.description"
            type="textarea"
            resize="none"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="标签：">
          <el-select
            v-model="form.tagList"
            multiple
            placeholder="标签"
            collapse-tags
          >
            <el-option-group
              v-for="(group, groupIndex) in options"
              :key="groupIndex"
              :label="group.label"
            >
              <el-option
                v-for="(item, index) in group.options"
                :key="index"
                :label="item"
                :value="item"
              />
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="原始数据：">
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
      </el-form>
      <el-divider />
      <el-form label-width="100px" :model="metaForm">
        <el-form-item label="数据提供方：">
          <el-input v-model="metaForm.provider" />
        </el-form-item>
        <el-form-item label="数据时间：">
          <el-input v-model="metaForm.time" />
        </el-form-item>
        <el-form-item label="数据范围：">
          <el-input v-model="metaForm.range" />
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
      <el-button type="success" plain @click="commit">提交</el-button>
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
} from "vue";
import "@wangeditor/editor/dist/css/style.css"; // 引入 css
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { IDomEditor } from "@wangeditor/editor";
import PageHeader from "@/components/page/PageHeader.vue";
import ResourceDialog from "@/components/dialog/ResourceDialog.vue";
import { addShareFile } from "@/api/request";
import { notice } from "@/utils/notice";

export default defineComponent({
  components: { PageHeader, Editor, Toolbar, ResourceDialog },
  setup() {
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const folderFlag = ref(false);
    const resourceType = ref("");
    const editorRef = shallowRef<IDomEditor>();
    const toolbarConfig = {};
    const editorConfig = {
      scroll: true,
      autoFocus: true,
    };

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
    const commit = async () => {
      const jsonData = {
        meta: {
          provider: metaForm.provider,
          time: metaForm.time,
          range: metaForm.range,
          detail: metaForm.valueHtml,
        },
        fileInfo: {
          name: form.name,
          description: form.description,
          originAddress: form.origin.address,
          visualSource: "",
          visualType: "",
          structuredSource: "",
          tags: form.tagList,
        },
      };
      const data = await addShareFile(jsonData);
      if (data != null) {
        if ((data as any).code === 0) {
          notice("success", "成功", "公布成功!");
          init()
        } else if ((data as any).code === -99) {
          notice("warning", "警告", "您没有权限！");
        } else {
          notice("error", "错误", "数据公布错误!");
        }
      }
    };

    const init = () => {
      form.name = ''
      form.description = ''
      form.tagList = [],
      form.origin.name = ''
      form.origin.address = ''
      form.struct.name = ''
      form.struct.address = ''
      form.visual.name = ''
      form.visual.address = ''
      metaForm.provider = ''
      metaForm.time = ''
      metaForm.range = ''
      metaForm.valueHtml = ''
    }

    const options = ref([
      {
        label: "国家和经济",
        options: ["阿富汗", "非洲"],
      },
      {
        label: "国家和经济",
        options: ["阿富汗", "非洲"],
      },
      {
        label: "国家和经济",
        options: ["阿富汗", "非洲"],
      },
      {
        label: "源目录",
        options: ["金融", "世行数据目录"],
      },
      {
        label: "收集",
        options: ["乌克兰 数据集"],
      },
      {
        label: "资源类型",
        options: ["excel", "pdf"],
      },
    ]);

    const form = reactive({
      name: "",
      description: "",
      tagList: [],
      origin: {
        name: "",
        address: "",
      },
      struct: {
        name: "",
        address: "",
      },
      visual: {
        name: "",
        address: "",
      },
    });

    const metaForm = reactive({
      provider: "",
      time: "",
      range: "",
      valueHtml: "",
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