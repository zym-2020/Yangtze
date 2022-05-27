<template>
  <div class="home-wrapper">
    <NavBar />
    <PageWrapper />
  </div>
</template>

<script lang="ts">
//import HelloWorld from './components/HelloWorld.vue';

import NavBar from "../components/nav.vue";

import PageWrapper from "../components/pageWrapper.vue";

// @Options({

// })
export default {
  components: {
    NavBar,
    PageWrapper,
  },
};
</script>

<style lang="scss">
div.home-wrapper {
  height: 3350px;
}
</style>
