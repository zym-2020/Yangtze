<template>
  <header id="topnav" class="topNav">
    <div class="inner">
      <div class="logo">
        <h1 class="logo-text">NHRI</h1>
        <h1 class="logo-text">NHRI</h1>
      </div>
      <nav role="navigation">
        <ul>
          <li v-for="port in ports" :key="port.index">
            <a :href="port.href">{{ port.text }}</a>
          </li>
        </ul>
      </nav>
    </div>
    <div id="view-account" class="button"></div>
  </header>
</template>

<script lang="ts">
import { ref } from "vue";
export default {
  name: "NavBar",
  setup() {
    const ports = ref([
      { href: "#", text: "资源门户" },
      { href: "/amap", text: "一张图" },
      { href: "#", text: "分析中心" },
      { href: "#", text: "账号" },
    ]);
    return {
      ports,
    };
  },
};
</script>

<style scope>
* {
  margin: 0;
  padding: 0;
  line-height: 0.8;
}
ul,
li,
a,
.logo {
  font-family: "Lato", "Verdana", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-weight: 400;
  margin: 0;
  padding: 0;
}

.inner {
  width: 90%;
  position: relative;
  margin: 0 auto;
}

header {
  position: fixed;
  width: 100%;
  padding-top: 0px;
  background-image: linear-gradient(
    rgba(13, 21, 27, 0.8),
    rgba(24, 64, 95, 0.3)
  );
  transition: 0.7s all;
  z-index: 3000;
  backdrop-filter: blur(8px);
}

body.header-fixed header {
  position: fixed;
  top: 0;
  right: 0;
  padding-top: 0;
  width: 100%;
  z-index: 2;
}

li {
  list-style: none;
}

nav {
  overflow: hidden;
}

nav ul {
  float: right;
}

nav li {
  display: inline;
  float: left;
}

nav li:last-child {
  float: right;
}

nav a {
  display: inline-block;
  color: rgba(245, 247, 255, 1);
  text-decoration: none;
  font-family: "SimHei";
  font-size: 16px;
  padding: 24px;
  transition: 4s all;
  background-image: url("../assets/wave_cut_new.png"); /* 换地址！！！ */
  background-position: top left;
}

nav a:hover {
  transition: 3s all;
  border-bottom-left-radius: 10%;
  border-bottom-right-radius: 10%;
  background-position: bottom right;
}

.logo {
  float: left;
  margin-top: 0;
  font-size: 48px;
  line-height: 50px;
  color: #fff;
}

.logo h1.logo-text {
  position: absolute;
  padding: 10px;
  color: #fff;
  font-size: 1em;
}

.logo h1:nth-child(1) {
  color: transparent;
  -webkit-text-stroke: 2px #03a9f4;
}

.logo h1:nth-child(2) {
  color: #03a9f4;
  animation: water-wave 4s infinite;
}

@keyframes water-wave {
  0%,
  100% {
    clip-path: polygon(
      0% 18%,
      15% 28%,
      32% 72%,
      54% 70%,
      70% 18%,
      84% 20%,
      100% 60%,
      100% 100%,
      0% 100%
    );
  }
  50% {
    clip-path: polygon(
      0 80%,
      16% 45%,
      34% 20%,
      51% 45%,
      67% 60%,
      84% 72%,
      100% 18%,
      100% 100%,
      0% 100%
    );
  }
}

body.header-fixed .logo {
  font-size: 30px;
}
</style>