<template>
  <div>
    <el-container>
      <el-header>
        <header-component></header-component>
      </el-header>
      <el-main>
        <!-- <router-view /> -->
        <router-view v-slot="{ Component }">
          <keep-alive>
            <component
              :is="Component"
              v-if="route.meta.keepAlive"
              :key="route.path"
            />
          </keep-alive>
          <component
            :is="Component"
            v-if="!route.meta.keepAlive"
          />
        </router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts">
import HeaderComponent from "./components/Header.vue";
import { computed, defineComponent } from "vue";
import router from "@/router";
export default defineComponent({
  components: {
    HeaderComponent,
  },
  setup() {
    const route = computed(() => {
      return router.currentRoute.value;
    });

    return {
      route,
    };
  },
});
</script>


<style lang="scss" scoped>

.el-header {
  padding: 0;
  height: 60px;
}
.el-main {
  padding: 0;
  height: 100%;
}

body,dl,dd,p,h1,h2,h3,h4,h5,h6{margin:0;}
h1,h2,h3,h4,h5,h6{font-size:100%}   /*继承body设定的字体大小*/    
body{font-family: "Microsoft YaHei",Tahoma,Arial,Simsun,sans-self;}
.clearfix:after{content:"."; display:block; visibility:hidden; height:0; clear:both;} /*除去浮动*/
.clearfix{zoom:1;}   
a:hover{text-decoration: none;}  
ul,ol{list-style: none;margin:0;padding:0;}    /*当要添加小图标时可修改*/
img{vertical-align:middle;border:0;-ms-interpolation-mode:bicubic;} /*在IE下除去边框和底部空白*/
em,i{font-style: normal}  /*如需默认样式可修改*/
input,select,textarea{vertical-align:middle;outline:none;}  /*出去获得焦点下的蓝色边框*/
textarea{resize:none;}  /*禁止用户缩放文本框*/
table {border-collapse: collapse;border-spacing: 0;}  
button,input[type="button"],input[type="reset"],input[type="submit"] {cursor:pointer;-webkit-appearance:button;-moz-appearance:button;} /*按钮初始化*/
input::-moz-placeholder,textarea::-moz-placeholder {color: #ccc;}   /*修改placeholder文字颜色*/
input:-ms-input-placeholder,textarea:-ms-input-placeholder {color: #ccc;}
input::-webkit-input-placeholder,textarea::-webkit-input-placeholder {color: #ccc;}
</style>