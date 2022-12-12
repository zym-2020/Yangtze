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
                :label="item.name"
                :value="item.name"
              />
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="数据时间描述：">
          <el-input v-model="form.time" />
        </el-form-item>
        <el-form-item label="时间详细描述：">
          <el-date-picker
            v-model="form.timeStamp"
            type="date"
            placeholder="Pick a day"
            size="default"
          />
        </el-form-item>
        <el-form-item label="数据范围描述：">
          <el-input v-model="form.range" />
        </el-form-item>

        <el-form-item label="数据条目定位：">
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
  id: string;
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
  timeStamp: string;
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

import { addDataList, addRelational } from "@/api/request";
import { notice } from "@/utils/notice";
import type { FormInstance } from "element-plus";
import AvatarUpload from "@/components/upload/AvatarUpload.vue";
import DataBind from "./components/DataBind.vue";

import mapBoxGl, { AnySourceData } from "mapbox-gl";
import "@mapbox/mapbox-gl-draw/dist/mapbox-gl-draw.css";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import { uuid } from "@/utils/common";
export default defineComponent({
  components: {
    PageHeader,
    Editor,
    Toolbar,

    AvatarUpload,
    DataBind,
  },
  setup() {
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const container = ref<HTMLElement>();
    const dataBind = ref();
    const options = ref([
      {
        title: "时间",
        data: [
          {
            name: "2002以前",
            count: false,
          },
          {
            name: "2002~2012",
            count: false,
          },
          {
            name: "2013~2022",
            count: false,
          },
        ],
      },
      {
        title: "范围",
        data: [
          {
            name: "长江区域",
            count: false,
          },
          {
            name: "南京区域",
            count: false,
          },
        ],
      },
      {
        title: "地点",
        data: [
          {
            name: "白茆小沙",
            count: false,
          },
          {
            name: "福中+福北",
            count: false,
          },
          {
            name: "横港沙",
            count: false,
          },
          {
            name: "黄铁沙",
            count: false,
          },
          {
            name: "护漕港边滩",
            count: false,
          },
          {
            name: "沪通大桥",
            count: false,
          },
          {
            name: "江阴大桥",
            count: false,
          },
          {
            name: "苏通大桥",
            count: false,
          },
          {
            name: "双涧沙",
            count: false,
          },
          {
            name: "通白",
            count: false,
          },
          {
            name: "通州沙",
            count: false,
          },
          {
            name: "民主沙",
            count: false,
          },
          {
            name: "福姜沙",
            count: false,
          },
          {
            name: "新开沙",
            count: false,
          },
          {
            name: "西水道",
            count: false,
          },
        ],
      },

      {
        title: "文件格式",
        data: [
          {
            name: "shp",
            count: false,
          },
          {
            name: "dwg",
            count: false,
          },
          {
            name: "txt",
            count: false,
          },
          {
            name: "jpg",
            count: false,
          },
          {
            name: "excel",
            count: false,
          },
        ],
      },
      {
        title: "文件性质",
        data: [
          {
            name: "栅格文件",
            count: false,
          },
          {
            name: "矢量文件",
            count: false,
          },
          {
            name: "文本数据",
            count: false,
          },
          {
            name: "图片",
            count: false,
          },
          {
            name: "遥感影像",
            count: false,
          },
        ],
      },
    ]);
    const optionType = ref([
      {
        title: "地形数据",
        data: [
          {
            name: "DEM",
            count: false,
          },
          {
            name: "边界",
            count: false,
          },
          {
            name: "等高线",
            count: false,
          },
          {
            name: "DWG",
            count: false,
          },
          {
            name: "高程点",
            count: false,
          },
          {
            name: "固定断面线",
            count: false,
          },

          {
            name: "深泓线",
            count: false,
          },
        ],
      },
      {
        title: "工程数据",
        data: [
          {
            name: "航标",
            count: false,
          },
          {
            name: "护岸工程",
            count: false,
          },
          {
            name: "码头工程",
            count: false,
          },
          {
            name: "水利工程",
            count: false,
          },
          {
            name: "整治工程",
            count: false,
          },
          {
            name: "桥梁工程",
            count: false,
          },
        ],
      },
      {
        title: "物理模型",
        data: [
          {
            name: "浓度场",
            count: false,
          },
          {
            name: "照片",
            count: false,
          },
        ],
      },
      {
        title: "水文数据",
        data: [
          {
            name: "潮位",
            count: false,
          },
          {
            name: "断面输沙率",
            count: false,
          },
          {
            name: "含沙量",
            count: false,
          },
          {
            name: "含盐度",
            count: false,
          },
          {
            name: "流速流向",
            count: false,
          },
          {
            name: "悬移质",
            count: false,
          },
        ],
      },
      {
        title: "遥感影像",
        data: [
          {
            name: "遥感影像",
            count: false,
          },
        ],
      },
    ]);
    const form: Form = reactive({
      id: uuid(),
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
      timeStamp: "",
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

    const uploadTh = (val: any) => {
      thumbnail.value = val;
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

    const init = () => {
      form.id = uuid();
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
      (form.timeStamp = ""), avatarUpload.value.initPicture();
      thumbUpload.value.initPicture();
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
      options,
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
      uploadTh,
      avatarUpload,
      thumbUpload,
      changeData,
      dataBind,
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