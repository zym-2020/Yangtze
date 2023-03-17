<template>
  <div class="upload-share">
    <div class="main">
      <div class="head">
        <strong>创建新的共享文件条目</strong>
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
        <el-form-item label="标签：" prop="tags">
          <el-tag
            v-for="tag in form.tags"
            :key="tag"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)"
          >
            {{ tag }}
          </el-tag>
          <el-input
            v-if="inputVisible"
            ref="InputRef"
            class="tag-input"
            v-model="inputValue"
            @keyup.enter="handleInputConfirm"
            @blur="handleInputConfirm"
          />
          <el-button v-else @click="showInput"> + New Tag </el-button>
        </el-form-item>

        <el-form-item label="条目封面：">
          <avatar-upload
            @upload="upload"
            :pictureName="''"
            ref="avatarUpload"
          ></avatar-upload>
        </el-form-item>
      </el-form>
      <el-divider />
      <el-form
        label-width="130px"
        :model="form"
        ref="metaRef"
        :rules="metaRules"
      >
        <el-form-item label="数据提供方：" prop="provider">
          <el-input v-model="form.provider" />
        </el-form-item>
        <el-form-item label="联系电话：">
          <el-input v-model="form.providerPhone" />
        </el-form-item>
        <el-form-item label="联系邮箱：">
          <el-input v-model="form.providerEmail" />
        </el-form-item>
        <el-form-item label="联系地址：">
          <el-input v-model="form.providerAddress" />
        </el-form-item>
        <el-form-item label="原始数据类型：" prop="type">
          <el-select
            v-model="form.type"
            placeholder="数据类型"
            size="large"
            style="width: 300px"
          >
            <el-option-group
              v-for="(group, groupIndex) in optionType"
              :key="groupIndex"
              :label="group.title"
            >
              <el-option
                v-for="(item, index) in group.data"
                :key="index"
                :label="item"
                :value="item"
              />
            </el-option-group>
          </el-select>
        </el-form-item>

        <el-form-item label="数据时间：">
          <el-input v-model="form.timeStamp" />
        </el-form-item>
        <el-form-item label="空间范围描述：">
          <el-input v-model="form.range" />
        </el-form-item>

        <el-form-item label="空间范围选取：">
          <div ref="container" class="container"></div>
        </el-form-item>

        <el-form-item label="数据绑定：">
          <data-bind @changeData="changeData" ref="dataBind" />
        </el-form-item>

        <el-form-item label="数据获取方式：" prop="getOnline">
          <el-radio-group v-model="form.getOnline">
            <el-radio :label="true">在线获取</el-radio>
            <el-radio :label="false">订单获取</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="其他描述：">
          <div style="border: 1px solid #ccc">
            <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              mode="default"
            />
            <Editor
              style="height: 320px; overflow-y: hidden"
              v-model="form.detail"
              :defaultConfig="editorConfig"
              mode="default"
              @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
    </div>
    <div class="btn">
      <el-button type="success" plain @click="commit(fileRef, metaRef)"
        >提交</el-button
      >
    </div>
    <page-copyright />
  </div>
</template>

<script lang="ts">
type Form = {
  id: string;
  name: string;
  description: string;
  tags: string[];
  location: string[];
  provider: string;
  range: string;
  detail: string;
  type: string;
  providerPhone: string;
  providerEmail: string;
  providerAddress: string;
  getOnline: boolean;
  timeStamp: string;
};
import {
  defineComponent,
  reactive,
  ref,
  shallowRef,
  onBeforeUnmount,
  onMounted,
  nextTick,
} from "vue";
import "@wangeditor/editor/dist/css/style.css"; // 引入 css
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { IDomEditor } from "@wangeditor/editor";
import { ElInput } from "element-plus";
import { addDataList, addRelational } from "@/api/request";
import { notice } from "@/utils/notice";
import type { FormInstance } from "element-plus";
import AvatarUpload from "@/components/upload/AvatarUpload.vue";
import DataBind from "./components/DataBind.vue";
import PageCopyright from "@/layout/components/PageCopyright.vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import "@mapbox/mapbox-gl-draw/dist/mapbox-gl-draw.css";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import { uuid } from "@/utils/common";
export default defineComponent({
  components: {
    Editor,
    Toolbar,
    AvatarUpload,
    DataBind,
    PageCopyright,
  },
  setup() {
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const container = ref<HTMLElement>();
    const dataBind = ref();
    const inputValue = ref("");
    const inputVisible = ref(false);
    const InputRef = ref<InstanceType<typeof ElInput>>();

    const optionType = ref([
      {
        title: "地形数据",
        data: [
          "DEM",
          "边界",
          "等高线",
          "DWG",
          "高程点",
          "固定断面线",
          "深泓线",
        ],
      },
      {
        title: "工程数据",
        data: [
          "航标",
          "护岸工程",
          "码头工程",
          "水利工程",
          "整治工程",
          "桥梁工程",
        ],
      },
      {
        title: "物理模型",
        data: ["浓度场", "照片", "视频"],
      },
      {
        title: "水文数据",
        data: ["潮位", "断面输沙率", "含沙量", "含盐度", "流速流向", "悬移质"],
      },
      {
        title: "遥感影像",
        data: ["遥感影像"],
      },
    ]);
    const form: Form = reactive({
      id: uuid(),
      name: "",
      description: "",
      tags: [],
      location: [],
      provider: "",
      range: "",
      detail: "",
      type: "",
      providerPhone: "",
      providerEmail: "",
      providerAddress: "",
      getOnline: true,
      timeStamp: "",
    });
    const avatar = ref<File>();
    const avatarUpload = ref();

    const editorRef = shallowRef<IDomEditor>();
    const toolbarConfig = {};
    const editorConfig = {
      scroll: true,
      autoFocus: true,
    };
    const fileList = ref<string[]>([]);
    const fileRef = ref<HTMLElement | undefined>();
    const metaRef = ref<HTMLElement | undefined>();

    let map: mapBoxGl.Map;

    const handleCreated = (editor: any) => {
      editorRef.value = editor; // 记录 editor 实例，重要！
    };

    const upload = (val: any) => {
      avatar.value = val;
    };

    const changeData = (
      val: {
        id: string;
        name: string;
        folder: boolean;
        size: string[];
        flag: boolean;
        parentId: string;
      }[]
    ) => {
      fileList.value = [];
      val.forEach((item) => {
        fileList.value.push(item.id);
      });
    };

    const commit = async (
      formEl1: FormInstance | undefined,
      formEl2: FormInstance | undefined
    ) => {
      if (!formEl1 || !formEl2) return;
      await formEl1.validate(async (valid1, fields) => {
        await formEl2.validate(async (valid2) => {
          if (valid1 && valid2) {
            const formData = new FormData();
            formData.append("jsonString", JSON.stringify(form));
            if (avatar.value != undefined) {
              formData.append("avatar", avatar.value);
            } else {
              formData.append("avatar", new Blob());
            }

            const data = await addDataList(formData);
            const data1 = await addRelational({
              dataListId: form.id,
              fileIdList: fileList.value,
            });
            if (
              data != null &&
              (data as any).code === 0 &&
              data1 != null &&
              (data1 as any).code === 0
            ) {
              notice("success", "成功", "请等待管理员审核通过！");
              init();
            }
          }
        });
      });
    };

    const handleClose = (tag: string) => {
      form.tags.splice(form.tags.indexOf(tag), 1);
    };

    const handleInputConfirm = () => {
      if (inputValue.value) {
        form.tags.push(inputValue.value);
      }
      inputVisible.value = false;
      inputValue.value = "";
    };

    const showInput = () => {
      inputVisible.value = true;
      nextTick(() => {
        InputRef.value!.input!.focus();
      });
    };

    const init = () => {
      form.id = uuid();
      form.name = "";
      form.description = "";
      form.location = [] as string[];
      form.tags = [] as string[];
      form.range = "";
      form.detail = "";
      form.provider = "";
      form.type = "";
      form.providerPhone = "";
      form.providerEmail = "";
      form.providerAddress = "";
      form.getOnline = true;
      form.timeStamp = "";
      avatarUpload.value.initPicture();

      dataBind.value?.clearData();
    };

    const fileRules = reactive({
      name: [{ required: true, message: "条目名不得为空！", trigger: "blur" }],
      tags: [{ required: true, message: "标签不得为空！", trigger: "blur" }],
    });
    const metaRules = reactive({
      provider: [
        { required: true, message: "数据提供方不得为空！", trigger: "blur" },
      ],
      type: [
        { required: true, message: "数据类型不得为空！", trigger: "blur" },
      ],
      getOnline: [
        { required: true, message: "数据获取方式不得为空！", trigger: "blur" },
      ],
    });

    const initMap = () => {
      map = new mapBoxGl.Map({
        container: container.value as HTMLElement,
        style: "mapbox://styles/16651699376/ckmpu8kuk0h8r17msqpz351vf",
        center: [121.18, 31.723],
        zoom: 8,
        accessToken:
          "pk.eyJ1IjoiMTY2NTE2OTkzNzYiLCJhIjoiY2ttMDh5amJpMHE2dzJ3cTd5eWZsMGQxZyJ9.XErH3kSOuRC_OWXWCpDLkQ",
      });
      //自定义绘制面
      const polygonDraw = new MapboxDraw({
        controls: {
          combine_features: false,
          uncombine_features: false,
          trash: true,
          point: false,
          line_string: false,
        },
      });
      //绘制事件
      const updateArea = function (e: any) {
        if (e.type === "draw.create") {
          if (polygonDraw.getAll().features.length > 1) {
            polygonDraw.delete(polygonDraw.getAll().features[0].id as string);
          }
          form.location = [] as string[];
          (e.features[0].geometry.coordinates[0] as number[][]).forEach(
            (item) => {
              form.location.push(item[0].toString());
              form.location.push(item[1].toString());
            }
          );
        } else if (e.type === "draw.update") {
          form.location = [] as string[];
          (e.features[0].geometry.coordinates[0] as number[][]).forEach(
            (item) => {
              form.location.push(item[0].toString());
              form.location.push(item[1].toString());
            }
          );
        } else if (e.type === "draw.delete") {
          form.location = [] as string[];
        }
      };
      map.addControl(polygonDraw, "top-right");
      map.on("draw.create", updateArea);
      map.on("draw.delete", updateArea);
      map.on("draw.update", updateArea);
    };

    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
      const editor = editorRef.value;
      if (editor == null) return;
      editor.destroy();
    });

    onMounted(() => {
      initMap();
    });

    return {
      container,
      form,
      defaultProps,
      inputValue,
      inputVisible,
      InputRef,
      optionType,
      editorRef,
      toolbarConfig,
      editorConfig,
      handleCreated,
      commit,
      fileRules,
      metaRules,
      fileRef,
      metaRef,
      upload,
      avatarUpload,
      changeData,
      dataBind,
      handleClose,
      handleInputConfirm,
      showInput,
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
      .container {
        height: 400px;
        width: 100%;
      }
      .el-tag {
        margin-right: 10px;
        margin-bottom: 5px;
      }
      .tag-input {
        width: 100px;
      }
    }
  }

  .btn {
    text-align: center;
    height: 80px;
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