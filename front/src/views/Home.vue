<template>
    <!-- <HomeWrapper /> -->
    <HomePage />
</template>

<script lang="ts">
import HomeWrapper from '../components/pageWrapper/homeWrapper.vue';
import HomePage from '../components/pageWrapper/homePage.vue';
export default {
    name:'Home', 
    components: {
        HomeWrapper, HomePage
    }, 
    setup() {
    }
}
</script>

<style lang="scss">
body {
    background-color: rgb(231, 231, 231);
}
</style>