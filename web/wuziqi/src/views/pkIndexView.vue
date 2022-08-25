<template>
  <PlayGround v-if="$store.state.pk.status === 'playing'" />
  <MatchGround v-if = "$store.state.pk.status === 'matching'" />
</template>

<script>
import PlayGround from "@/components/PlayGround";
import MatchGround from "@/components/MatchGround";
import {useStore} from "vuex";
import {onMounted, onUnmounted} from "vue";

export default {
  name: "pkIndexView",
  components :{
    PlayGround,
    MatchGround,
  },
  setup(){
    const store = useStore();

    const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.pk.user_id}`;

    let socket =null;
    onMounted(()=>{
      socket = new WebSocket(socketUrl);
      socket.onopen = () => {
        store.commit("updateSocket", socket);
        console.log('connected');
      };
      socket.onmessage = (msg) => {
        const  data =  JSON.parse(msg.data);
        if(data.event === "start-matching") {
          store.commit("updateOpponent",data.event.opponent_id);
          console.log(data);
          store.commit("updateStatus","playing");
        }
      };
      socket.onclose = ()=>{
        console.log("disconnected");
      }
    });
    onUnmounted(()=>{
      socket.onclose();
      store.commit("updateStatus","matching");

    })

  }
}
</script>

<style scoped>

</style>