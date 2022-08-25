<template>
  <div class="match-ground">

      <div class="col-12" style="text-align: center;padding-top: 15vh">
        <button type = "button" @click="click_match_btn" class="btn btn-warning">{{ match_btn_info }}</button>
      </div>
</div>
</template>

<script>
import {ref} from "vue";
import {useStore} from "vuex";

export default {
  name: "MatchGround",
  components:{
  },
  setup(){
    let store = useStore();
    let match_btn_info = ref("开始匹配");
    const click_match_btn = () => {
      if(match_btn_info.value === "开始匹配"){
        match_btn_info.value = "取消匹配";
        store.state.pk.socket.send(JSON.stringify({
          event : "start-matching",
        }))
      }else{
        match_btn_info.value = "开始匹配";
        store.state.pk.socket.send(JSON.stringify({
          event : "stop-matching",
        }))
      }
    }
    return {
      match_btn_info,
      click_match_btn,
    }
  }
}
</script>

<style scoped>
div.match-ground{
  width:60vw;
  height: 70vh;
  background-color: rgba(50, 50, 50, 0.1);
  margin: 40px auto;
}

div.user-photo > img{
  border-radius: 50%;
  width: 20vh;
}

div.user-select-bot > select {
  width: 60%;
  margin: 0 auto;
}
</style>