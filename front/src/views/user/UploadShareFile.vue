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
        <el-form-item label="标签：" prop="tagList" >
          <el-select
            v-model="form.tagList"
            multiple
            placeholder="标签"
            
            size="large"
            style="width:300px"
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
        <!-- ///////////需要再设计，更改 ///////////-->
        <el-form-item label="原始数据：" prop="origin">
          <el-button type="primary" plain @click="openFolder('origin')">
            添加<el-icon class="el-icon--right"><Upload /></el-icon>
          </el-button>
          <div 
          v-for="(item,index) in fileInDataList" 
          :key="index"
          >
          <el-tag
            closable
            v-if="
              item.name != '' &&
              item.name != undefined &&
              item.name != null
            "
            size="large"
            class="tag"
            type="success"
            @close="tagClose(index)"
          >
            {{ item.name }}
          </el-tag>
        </div>
        </el-form-item>

        <el-form-item label="条目封面：">
          <avatar-upload @upload="upload"></avatar-upload>
        </el-form-item>

        <el-form-item label="条目缩略图：">
          <!-- <avatar-upload @upload="uploadTh"></avatar-upload> -->
          <thumb-upload  @uploadTh="uploadTh"></thumb-upload>
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
          <el-input v-model="form.provider_phone" />
        </el-form-item>
        <el-form-item label="联系邮箱：">
          <el-input v-model="form.provider_email" />
        </el-form-item>
        <el-form-item label="联系地址：">
          <el-input v-model="form.provider_address" />
        </el-form-item>
        <el-form-item label="原始数据类型：" prop="type">
          <el-input v-model="form.type" />
        </el-form-item>
        <el-form-item label="数据时间：">
          <el-input v-model="form.time" />
        </el-form-item>
        <el-form-item label="数据范围：">
          <el-input v-model="form.range" />
        </el-form-item>
        <el-form-item label="数据获取方式：" prop="getMode">
          <el-radio-group v-model="form.getMode">
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
    <div>
      <FindMap
      @getCoor="getCoor"></FindMap>
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
  onMounted,
  computed,
} from "vue";
import "@wangeditor/editor/dist/css/style.css"; // 引入 css
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { IDomEditor } from "@wangeditor/editor";
import PageHeader from "@/components/page/PageHeader.vue";
import ResourceDialog from "@/components/dialog/ResourceDialog.vue";
import {
  addMessage,
  examineById,
  getShareFileById,
} from "@/api/request";
import { notice } from "@/utils/notice";
import type { FormInstance } from "element-plus";
import AvatarUpload from "@/components/upload/AvatarUpload.vue";
import ThumbUpload from "@/components/upload/ThumbUpload.vue";
import FindMap from "@/components/scenePart/FindMap.vue";
import router from "@/router";
import axios from "axios";
import { useStore } from "@/store";
export default defineComponent({
  components: { PageHeader, Editor, Toolbar, ResourceDialog, AvatarUpload, ThumbUpload, FindMap },
  setup() {
    const store = useStore();
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const folderFlag = ref(false);
    const resourceType = ref("");
    const editorRef = shallowRef<IDomEditor>();
    const toolbarConfig = {};
    const fileList = ref<any[]>([]);
    const tempCache = ref<any[]>([]);
    const editorConfig = {
      scroll: true,
      autoFocus: true,
    };
    const fileInDataList =ref<any[]>([])
    const fileRef = ref<HTMLElement>();
    const metaRef = ref<HTMLElement>();
    const avatarFlag = ref(false);
    const thumbFlag =ref(false);
    const dataListCoor=ref<any[]>([])

    const getCoor=(val:any[])=>{
      console.log(val)
      
      dataListCoor.value=val;
      (form as any).location=dataListCoor.value
    }

    const handleCreated = (editor: any) => {
      editorRef.value = editor; // 记录 editor 实例，重要！
    };

    const openFolder = (type: string) => {
      resourceType.value = type;
      folderFlag.value = true;
    };

////////////////////////////////////////需要再设计
    const selectedFile = (val: any) => {
      folderFlag.value = false;
      fileInDataList.value.push({"name":val.file.name,"address":val.file.address})
      }


    const tagClose = (val: number) => {
      fileInDataList.value.splice(val)
          
      }
    
//////////////////////////////////////
    const upload = (val: any) => {
      avatarFlag.value = true;
      form.avatar = val;
    };

    const uploadTh = (val: any) => {
      thumbFlag.value = true;
      form.thumbnail = val;
    };

    const commit = async (
      formEl1: FormInstance | undefined,
      formEl2: FormInstance | undefined
    ) => {
      if (!formEl1 || !formEl2) return;
      await formEl1.validate(async (valid1, fields) => {
        await formEl2.validate(async (valid2) => {
          if (valid1 && valid2) {
            const jsonData = {
              fileInfo: {
                name: form.name,
                loaction : form.location,
                description: form.description,
                tags: form.tagList,
                provider: form.provider,
                time: form.time,
                range: form.range,
                //detail: metaForm.valueHtml,
                type: form.type,
                providerPhone: form.provider_phone,
                providerEmail: form.provider_email,
                providerAddress: form.provider_address,
                getOnline: form.getMode === "在线获取" ? true : false, 
                
              },
            };
            const formData = new FormData();
            formData.append("jsonString", JSON.stringify(jsonData));
            if (avatarFlag.value) {
              formData.append("file", form.avatar);
            } else {
              formData.append("file", new Blob());
            };
            if (thumbFlag.value) {
              formData.append("file2", form.thumbnail);
            } else {
              formData.append("file2", new Blob());
            }

             const data =await axios.post("http://172.21.213.244:8002/dataList/fuzzyQuery", formData,
    {headers:{'authorization':'Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bXNzIiwiaWQiOiI0MzYwODUxNC1kODMzLTRiNTAtOWE3NC0wOWI2MzhiOTM4OTEiLCJhdmF0YXIiOiIvZmlsZS9hdmF0YXIvMGE4MjhmMWItMDAyNC00ZDViLThlODUtNjQ1MDQwMzlhY2YyLmpwZyIsImV4cCI6MTY2MTczOTg5NCwiZW1haWwiOiIxMjNAcXEuY29tIn0.eLjI6Bh4qa5A7OIA3Q8W8XXFAl8KsvLfNxUUdK3SKWKaGxsT-7fDuiH5XhBpcwnMbUVaQs6urjkhp6uLSzUmsg'}}).
    then((res) => {
         return res.data
    })
            if (data != null) {
              if ((data as any).code === 0) {
                notice("success", "成功", "请等待管理员审核通过！");
                
                // const fileID = data.data.list;
                // const dataCacheById = await getShareFileById(fileID);
               
                // tempCache.value = dataCacheById.data.list;
                // const jsonDataById = computed(() => {
                //   return JSON.stringify(tempCache.value as any);
                // }); 
                          
                // const tempData = await addMessage({
                //   id: "",
                //   dataName: form.name,
                //   dataUploadTime: "",
                //   dataExamineTime: "2012-02-25",
                //   dataCache: jsonDataById.value,
                //   messageRequest: "fff",
                //   reply: false,
                //   fileId: fileID as string,
                //   messageSender: "fgz",
                //   messageReceiver: " ",
                //   messageResponse: "examine",
                //   messageType: "upload",
                //   replyUser: false,
                // });
                // console.log("ffff")
                // await examineById(fileID.value);
                init();
              } else {
                notice("error", "错误", "数据公布错误!");
              }
            }
          }
        });
      });
    };

    const init = () => {
      form.name = "";
      form.description = "";
      form.location =[] as any;
      form.tagList = [] as any;
      form.time = "";
      form.range = "";
      form.detail = "";
      form.type = "";
      form.provider_phone = "";
      form.provider_email = "";
      form.provider_address = "";
      form.getMode = "";
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
            name:"潮汐",
            conut:false

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
      name: "",
      description: "",
      tagList: [],
      location :[],
      avatar: "",
      thumbnail : "",
      provider: "",
      time: "",
      range: "",
      detail:"",
      type: "",
      provider_phone: "",
      provider_email: "",
      provider_address: "",
      getMode: "",
    });



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

    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
      const editor = editorRef.value;
      if (editor == null) return;
      editor.destroy();
    });

    onMounted(() => {
      if (
        router.currentRoute.value.params.originFileAddress != undefined &&
        router.currentRoute.value.params.originFileName != undefined
      ) {
        // form.name = router.currentRoute.value.params
        //   .originFileName as string;
        // form.address = router.currentRoute.value.params
        //   .originFileAddress as string;
      }
    });

    return {
      form,
      defaultProps,
      options,
      editorRef,
      toolbarConfig,
      dataListCoor,
      getCoor,
      editorConfig,
      handleCreated,
      commit,
      fileInDataList,
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
      /deep/ .el-popper {
        z-index: 99;
      }
    }
  }
  :deep(.scene-map-wrapper2){
    #map {
    position: absolute;
    top: 1700px;
    margin-left:550px
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