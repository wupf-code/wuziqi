<template>
  <div class="result-board">

    <div class="result-board-text" v-if="$store.state.pk.result === 'winner'">
      Win
    </div>
    <div class="result-board-text" v-else-if="$store.state.pk.result === 'loser'">
      Lose
    </div>

    <div class="result-board-btn">
      <button @click="restart" type="button" class="btn btn-warning btn-lg">
        再来！
      </button>
    </div>
  </div>

</template>

<script>
// import router from "@/router";
import { useStore } from 'vuex';
import {AC_GAME_OBJECTS} from "@/assets/scripts/AcGameObject";
// import router from "@/router";
export default {
  name: "ResultBoard",
  setup() {
    const store = useStore();
    // const $router = router;
    const restart = () => {
      store.commit("updateStatus", "matching");
      store.commit("updateResult", "none");
      AC_GAME_OBJECTS.splice(0,AC_GAME_OBJECTS.length);
      // router.go(0);
      // router.push({name:"pk_index"});
    }

    return {
      restart
    };
  }

}
</script>

<style scoped>
div.result-board {
  height: 30vh;
  width: 30vw;
  background-color: rgba(50, 50, 50, 0.5);
  position: absolute;
  top: 30vh;
  left: 35vw;
}
div.result-board-text {
  text-align: center;
  color: white;
  font-size: 50px;
  font-weight: 600;
  font-style: italic;
  padding-top: 5vh;
}

div.result-board-btn {
  padding-top: 7vh;
  text-align: center;
}

</style>