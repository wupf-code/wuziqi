<template>
  <PlayGround ref="playground" v-if="$store.state.pk.status === 'playing'" />
  <MatchGround v-if = "$store.state.pk.status === 'matching'" />
  <ResultBoard v-if="$store.state.pk.result !== 'none'" />
</template>

<script>
import PlayGround from "@/components/PlayGround";
import MatchGround from "@/components/MatchGround";
import ResultBoard from "@/components/ResultBoard";
import {useStore} from "vuex";
import {onMounted, onUnmounted, ref,} from "vue";
import {AC_GAME_OBJECTS} from "@/assets/scripts/AcGameObject";

export default {
  name: "pkIndexView",
  components :{
    PlayGround,
    MatchGround,
    ResultBoard,

  },

  setup(){
    // let chatfield = ref(null);
    // let chathistory = ref(null);
    // let chatinput = ref(null);
    const store = useStore();
    let playground = ref(null);
    // let chathistory = window.document.getElementById("history");
    // const socketUrl = `wss://chess.ymswdfg.top/websocket/${store.state.pk.user_id}`;
    const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.pk.user_id}`;

    // console.log(chathistory);
    let socket =null;
    // let count = 1;
    // let a=[];
    onMounted(()=>{

       // chathistory = chatfield.value.chathistory;
      // let chathistory = this.$refs.chatfield;
      // console.log(chathistory);
      socket = new WebSocket(socketUrl);
      socket.onopen = () => {
        store.commit("updateSocket", socket);
        console.log('connected');
      };
      socket.onmessage = (msg) => {
        const  data =  JSON.parse(msg.data);
        if(data.event === "start-matching") {
          store.commit("updateOpponent",data.opponent_id);
          store.commit("updateColor",{
            own_color : data.own_color,
            opponent_color : data.opponent_color,
          });
          store.commit("updateStatus","playing");
          store.commit("updateCanStep",data.can_next);
        }else if(data.event === "moveown") {
          let gamemap = AC_GAME_OBJECTS[0];
          store.commit("updateCanStep",data.can_next);
          gamemap.drawChessOwn(data.own_x,data.own_y);
          store.commit("updateOwnStep",{
            own_x:data.own_x,
            own_y:data.own_y,
          })
        } else if(data.event === "moveopponent"){
          store.commit("updateCanStep",data.can_next);
          let gamemap = AC_GAME_OBJECTS[0];
          gamemap.drawChessOpponent(data.opponent_x,data.opponent_y);
          store.commit("updateOpponentStep",{
            opponent_x:data.opponent_x,
            opponent_y:data.opponent_y,
          })
        } else if(data.event === "result"){
          store.commit("updateCanStep",data.can_next);
          store.commit("updateResult",data.win);
          if(data.win==="winner"){
            let game = AC_GAME_OBJECTS[0];
            game.drawChessOwn(data.own_x,data.own_y);
          }else if(data.win === "loser"){
            let game = AC_GAME_OBJECTS[0];
            game.drawChessOpponent(data.opponent_x,data.opponent_y);
          }
        }else if(data.event === "chat"){

         let chathistory =  (playground.value.$refs.chatfield.$refs.chathistory);
          let div = window.document.createElement("div");
          div.innerText = data.chat;
          chathistory.appendChild(div);

          chathistory.scrollTop = chathistory.scrollHeight;

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
    return {
      playground,
      // chatinput,
      // chathistory,
      // chathistory,
    }

  }
}
</script>

<style scoped>

</style>