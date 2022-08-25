<template>
  <div class="home">
      <GameMap/>
  </div>
</template>

<script>
// @ is an alias to /src
import GameMap from "@/components/GameMap";
export default {
  name: 'playGround',
  components: {
    GameMap,
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
</style>