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

</style>