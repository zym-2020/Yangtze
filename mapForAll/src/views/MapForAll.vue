<template>
  <div class="scene-page-wrapper">
    <NavBar />
    <SceneWrapper />
  </div>
</template>

<script lang="ts">
//import HelloWorld from './components/HelloWorld.vue';

import NavBar from "../components/nav.vue";
import SceneWrapper from "../components/sceneWrapper.vue";

// @Options({

// })
export default {
  components: {
    NavBar,
    SceneWrapper, 
  },
};
</script>

<style lang="scss">
div.scene-page-wrapper {
    height: 100vh;
}
</style>
