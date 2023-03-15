<template>
  <div class="upload-share">
    <div class="main">
      <div class="head">
        <strong>修改共享文件条目</strong>
      </div>
      <div class="des">
        请确保以下内容的<strong>真实性</strong>及<strong>完整性</strong>，以便管理员审核通过！审核工作预计在7个工作日内完成
        <div>项目修改后需要管理员<strong>重新审核</strong>才能上线</div>
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
            ref="avatarUpload"
            @upload="upload"
            :pictureName="avatarName"
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
          <data-bind ref="dataBind" @changeData="changeData" />
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
        >更新</el-button
      >
    </div>
    <page-copyright />
  </div>
</template>

<script lang="ts">
type TableDataType = {
  id: string;
  name: string;
  folder: boolean;
  size: string;
  flag: boolean;
  parentId: string;
};
import {
  defineComponent,
  reactive,
  ref,
  shallowRef,
  onBeforeUnmount,
  onMounted,
  onActivated,
  nextTick,
} from "vue";
import "@wangeditor/editor/dist/css/style.css"; // 引入 css
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { IDomEditor } from "@wangeditor/editor";
import DataBind from "./components/DataBind.vue";
import type { FormInstance } from "element-plus";
import AvatarUpload from "@/components/upload/AvatarUpload.vue";
import mapBoxGl from "mapbox-gl";
import "@mapbox/mapbox-gl-draw/dist/mapbox-gl-draw.css";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import { updateDataList, updateRelational } from "@/api/request";
import router from "@/router";
import { notice } from "@/utils/notice";
import PageCopyright from "@/layout/components/PageCopyright.vue";
import { ElInput } from "element-plus";
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

    const inputValue = ref("");
    const inputVisible = ref(false);
    const InputRef = ref<InstanceType<typeof ElInput>>();
    const avatarUpload = ref();
    const dataBind = ref();

    const editorRef = shallowRef<IDomEditor>();
    const toolbarConfig = {};
    const tableData = ref<TableDataType[]>([]);
    const editorConfig = {
      scroll: true,
      autoFocus: true,
    };

    const avatar = ref<File>();
    const avatarName = ref<string>(
      (router.currentRoute.value.params.fileInfo as any).avatar
    );

    const fileList = ref<string[]>([]);

    let map: mapBoxGl.Map;
    const container = ref<HTMLElement>();

    const fileRef = ref<HTMLElement>();
    const metaRef = ref<HTMLElement>();

    const handleCreated = (editor: any) => {
      editorRef.value = editor; // 记录 editor 实例，重要！
    };

    const upload = (val: any) => {
      avatar.value = val;
    };

    const changeData = (val: TableDataType[]) => {
      fileList.value = [];
      val.forEach((item) => {
        fileList.value.push(item.id);
      });
    };

    const getCoordinates = (location: string[]) => {
      const coordinates = [];
      for (let i = 0; i + 1 < location.length; i = i + 2) {
        coordinates.push([
          parseFloat(location[i]),
          parseFloat(location[i + 1]),
        ]);
      }
      return coordinates;
    };

    const computeCenter = (location: string[]): [number, number] => {
      if (location.length > 0) {
        const center: [number, number] = [0, 0];
        for (let i = 0; i < location.length; i = i + 2) {
          center[0] += parseFloat(location[i]);
          center[1] += parseFloat(location[i + 1]);
        }
        center[0] = center[0] / (location.length / 2);
        center[1] = center[1] / (location.length / 2);
        return center;
      } else {
        return [121.18, 31.723];
      }
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
            const data = await updateDataList(formData);
            if (data != null && (data as any).code === 0) {
              const data1 = await updateRelational({
                dataListId: form.id,
                fileIdList: fileList.value,
              });
              if (data1 != null && (data1 as any).code === 0) {
                notice("success", "成功", "更新成功！");
              }
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
            name: "流量输沙率",
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
    const form = reactive({
      id: (router.currentRoute.value.params.fileInfo as any).id,
      name: (router.currentRoute.value.params.fileInfo as any).name,
      description: (router.currentRoute.value.params.fileInfo as any)
        .description,
      tags: (router.currentRoute.value.params.fileInfo as any).tags,
      location: (router.currentRoute.value.params.fileInfo as any).location,
      provider: (router.currentRoute.value.params.fileInfo as any).provider,
      range: (router.currentRoute.value.params.fileInfo as any).range,
      detail: (router.currentRoute.value.params.fileInfo as any).detail,
      type: (router.currentRoute.value.params.fileInfo as any).type,
      providerPhone: (router.currentRoute.value.params.fileInfo as any)
        .providerPhone,
      providerEmail: (router.currentRoute.value.params.fileInfo as any)
        .providerEmail,
      providerAddress: (router.currentRoute.value.params.fileInfo as any)
        .providerAddress,
      getOnline: (router.currentRoute.value.params.fileInfo as any).getOnline,
      timeStamp: (router.currentRoute.value.params.fileInfo as any).timeStamp,
    });

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
    const addPolygonDraw = () => {
      polygonDraw.deleteAll();
      if (
        (router.currentRoute.value.params.fileInfo as any).location.length > 0
      ) {
        polygonDraw.add({
          type: "Feature",
          properties: {},
          geometry: {
            type: "Polygon",
            coordinates: [
              getCoordinates(
                (router.currentRoute.value.params.fileInfo as any).location
              ),
            ],
          },
        });
      }
    };

    const initMap = () => {
      map = new mapBoxGl.Map({
        container: container.value as HTMLElement,
        style: "mapbox://styles/16651699376/ckmpu8kuk0h8r17msqpz351vf",
        center: computeCenter(
          (router.currentRoute.value.params.fileInfo as any).location
        ),
        zoom: 8,
        accessToken:
          "pk.eyJ1IjoiMTY2NTE2OTkzNzYiLCJhIjoiY2ttMDh5amJpMHE2dzJ3cTd5eWZsMGQxZyJ9.XErH3kSOuRC_OWXWCpDLkQ",
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
      map.on("load", addPolygonDraw);
    };

    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
      const editor = editorRef.value;
      if (editor == null) return;
      editor.destroy();
    });

    onActivated(() => {
      (router.currentRoute.value.params.files as any[]).forEach((item) => {
        fileList.value.push(item.id);
      });
      form.id = (router.currentRoute.value.params.fileInfo as any).id;
      form.name = (router.currentRoute.value.params.fileInfo as any).name;
      form.description = (
        router.currentRoute.value.params.fileInfo as any
      ).description;
      form.tags = (router.currentRoute.value.params.fileInfo as any).tags;
      form.location = (
        router.currentRoute.value.params.fileInfo as any
      ).location;
      form.provider = (
        router.currentRoute.value.params.fileInfo as any
      ).provider;
      form.range = (router.currentRoute.value.params.fileInfo as any).range;
      form.detail = (router.currentRoute.value.params.fileInfo as any).detail;
      form.type = (router.currentRoute.value.params.fileInfo as any).type;
      form.providerPhone = (
        router.currentRoute.value.params.fileInfo as any
      ).providerPhone;
      form.providerEmail = (
        router.currentRoute.value.params.fileInfo as any
      ).providerEmail;
      form.providerAddress = (
        router.currentRoute.value.params.fileInfo as any
      ).providerAddress;
      form.getOnline = (
        router.currentRoute.value.params.fileInfo as any
      ).getOnline;
      form.timeStamp = (
        router.currentRoute.value.params.fileInfo as any
      ).timeStamp;
      avatarName.value = (
        router.currentRoute.value.params.fileInfo as any
      ).avatar;

      avatarUpload.value.initPicture(
        (router.currentRoute.value.params.fileInfo as any).avatar
      );
      dataBind.value.getTableData(router.currentRoute.value.params.files);
      addPolygonDraw();
      map.setCenter(
        computeCenter(
          (router.currentRoute.value.params.fileInfo as any).location
        )
      );
    });

    onMounted(() => {
      initMap();
    });

    return {
      inputValue,
      inputVisible,
      InputRef,
      form,
      container,
      defaultProps,
      editorRef,
      toolbarConfig,
      editorConfig,
      optionType,
      handleCreated,
      commit,
      fileRules,
      metaRules,
      fileRef,
      metaRef,
      upload,
      avatarName,
      changeData,
      tableData,
      handleClose,
      handleInputConfirm,
      showInput,
      avatarUpload,
      dataBind,
      computeCenter,
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
    .des {
      line-height: 30px;
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