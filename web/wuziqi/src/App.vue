<template>
  <NarBar/>
  <router-view/>
</template>

<script>
import {useStore} from "vuex";
import NarBar from "@/components/NarBar";
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
  export default {
    components : {
      NarBar
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
  background-image: url("@/assets/image/a.jpg");
  background-size: cover;
}
</style>
