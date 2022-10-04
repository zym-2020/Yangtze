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
          <el-select
            v-model="form.tags"
            multiple
            placeholder="标签"
            size="large"
            style="width: 300px"
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

        <el-form-item label="条目封面：">
          <avatar-upload
            @upload="upload"
            :pictureName="''"
            ref="avatarUpload"
          ></avatar-upload>
        </el-form-item>

        <el-form-item label="条目缩略图：">
          <avatar-upload
            @upload="uploadTh"
            :pictureName="''"
            ref="thumbUpload"
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
        <el-form-item label="数据类型：" prop="type">
          <el-input v-model="form.type" />
        </el-form-item>
        <el-form-item label="数据时间描述：">
          <el-input v-model="form.time" />
        </el-form-item>
        <el-form-item label="数据范围描述：">
          <el-input v-model="form.range" />
        </el-form-item>

        <el-form-item label="数据条目定位：">
          <div ref="container" class="container"></div>
        </el-form-item>

        <el-form-item label="数据获取方式：" prop="getOnline">
          <el-radio-group v-model="form.getOnline">
            <el-radio :label="true">在线获取</el-radio>
            <el-radio :label="false">订单获取</el-radio>
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
  </div>
</template>

<script lang="ts">
type Form = {
  name: string;
  description: string;
  tags: string[];
  location: string[];
  provider: string;
  time: string;
  range: string;
  detail: string;
  type: string;
  providerPhone: string;
  providerEmail: string;
  providerAddress: string;
  getOnline: boolean;
};
import {
  defineComponent,
  reactive,
  ref,
  shallowRef,
  onBeforeUnmount,
  onMounted,
} from "vue";
import "@wangeditor/editor/dist/css/style.css"; // 引入 css
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { IDomEditor } from "@wangeditor/editor";
import PageHeader from "@/components/page/PageHeader.vue";
import ResourceDialog from "@/components/dialog/ResourceDialog.vue";
import { addDataList } from "@/api/request";
import { notice } from "@/utils/notice";
import type { FormInstance } from "element-plus";
import AvatarUpload from "@/components/upload/AvatarUpload.vue";

import mapBoxGl, { AnySourceData } from "mapbox-gl";
import "@mapbox/mapbox-gl-draw/dist/mapbox-gl-draw.css";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
export default defineComponent({
  components: {
    PageHeader,
    Editor,
    Toolbar,
    ResourceDialog,
    AvatarUpload,
  },
  setup() {
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const container = ref<HTMLElement>();
    const form: Form = reactive({
      name: "",
      description: "",
      tags: [],
      location: [],
      provider: "",
      time: "",
      range: "",
      detail: "",
      type: "",
      providerPhone: "",
      providerEmail: "",
      providerAddress: "",
      getOnline: true,
    });
    const avatar = ref<File>();
    const thumbnail = ref<File>();
    const avatarUpload = ref();
    const thumbUpload = ref();

    const editorRef = shallowRef<IDomEditor>();
    const toolbarConfig = {};
    const editorConfig = {
      scroll: true,
      autoFocus: true,
    };
    const fileInDataList = ref<any[]>([]);
    const fileRef = ref<HTMLElement | undefined>();
    const metaRef = ref<HTMLElement | undefined>();

    let map: mapBoxGl.Map;

    const handleCreated = (editor: any) => {
      editorRef.value = editor; // 记录 editor 实例，重要！
    };

    const upload = (val: any) => {
      avatar.value = val;
    };

    const uploadTh = (val: any) => {
      thumbnail.value = val;
    };

    const commit = async (
      formEl1: FormInstance | undefined,
      formEl2: FormInstance | undefined
    ) => {
      if (!formEl1 || !formEl2) return;
      await formEl1.validate(async (valid1, fields) => {
        await formEl2.validate(async (valid2) => {
          if (valid1 && valid2) {
            console.log(form, avatar.value, thumbnail.value);
            const formData = new FormData();
            formData.append("jsonString", JSON.stringify(form));
            if (avatar.value != undefined) {
              formData.append("avatar", avatar.value);
            } else {
              formData.append("avatar", new Blob());
            }
            if (thumbnail.value != undefined) {
              formData.append("thumbnail", thumbnail.value);
            } else {
              formData.append("thumbnail", new Blob());
            }
            const data = await addDataList(formData);
            if (data != null && (data as any).code === 0) {
              notice("success", "成功", "请等待管理员审核通过！");
              init();
            }
          }
        });
      });
    };

    const init = () => {
      form.name = "";
      form.description = "";
      form.location = [] as string[];
      form.tags = [] as string[];
      form.time = "";
      form.range = "";
      form.detail = "";
      form.provider = "";
      form.type = "";
      form.providerPhone = "";
      form.providerEmail = "";
      form.providerAddress = "";
      form.getOnline = true;

      avatarUpload.value.initPicture();
      thumbUpload.value.initPicture();
    };

    const options = ref([
      {
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
        ],
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
          },
          {
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
        ],
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
            name: "实施工程",
            count: false,
          },
          {
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
          },
          {
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
          },
          {
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
            name: "潮汐",
            conut: false,
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
          },
          {
            name: "固定断面线",
            count: false,
          },
          {
            name: "制导线",
            count: false,
          },
        ],
      },
      {
        title: "元数据",
        data: [
          {
            name: "Pdf",
            count: false,
          },
          {
            name: "Word",
            count: false,
          },
          {
            name: "PPT",
            count: false,
          },
        ],
      },
    ]);

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
      options,
      editorRef,
      toolbarConfig,
      editorConfig,
      handleCreated,
      commit,
      fileInDataList,
      fileRules,
      metaRules,
      fileRef,
      metaRef,
      upload,
      uploadTh,
      avatarUpload,
      thumbUpload,
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
      .container {
        height: 400px;
        width: 100%;
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