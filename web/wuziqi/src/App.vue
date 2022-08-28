<template>
  <router-view/>
</template>

<script>
import {useStore} from "vuex";

  export default {
    components : {
    },
    setup(){
      const  store = useStore();
      const userId = Math.floor(Math.random() * (1000 - 1 + 1)) + 1;
      store.commit("updateUserId",userId);
      // console.log(userId);
    }

  }
</script>


<style>
body{
  background-image: url("@/assets/image/01.png");
  background-size: cover;
}
</style>
