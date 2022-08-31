<template>
  <div class="home">
    <div class="result-board">
      <div class="result-board-text" v-if="$store.state.pk.own_color === 'black'" >
        执黑
      </div>
      <div class="result-board-text" v-else-if="$store.state.pk.own_color === 'white'">
        执白
      </div>
    </div>
    <div v-if="$store.state.pk.can_next === true" style="color: black; font-weight: 400;font-size: 30px;">请落子</div>
    <div v-else-if="$store.state.pk.can_next === false" style="color: black; font-weight: 400; font-size: 30px;">请等待对手落子</div>
    <div><ChatField ref="chatfield"/></div>
    <GameMap/>
  </div>
</template>

<script>
// @ is an alias to /src
import GameMap from "@/components/GameMap";
import ChatField from "@/components/ChatField";
import {onMounted, ref} from "vue";
export default {
  name: 'playGround',
  components: {
    GameMap,
    ChatField,
  },
  setup(){
    let chatfield = ref(null);
    let chathistory = ref(null);
    onMounted(()=>{
      chathistory = chatfield.value.$refs.chathistory;
      console.log(chathistory);
    })
    return {
      chatfield,
      chathistory,
    }

  }
}
</script>

<style>
div.home{
  width:60vw;
  height: 70vh;
  /*background: lightgray;*/
  margin: 40px auto;
}
div.result-board {
  /*height: 30vh;*/
  /*width: 30vw;*/
  background-color: rgba(50, 50, 50, 0);
  position: absolute;
  margin-right: 5vw;
  margin-top: 5vh;
}
div.result-board-text {
  text-align: center;
  color: yellow;
  font-size: 50px;
  font-weight: 600;
  font-style: italic;
  padding-top: 5vh;
}
</style>