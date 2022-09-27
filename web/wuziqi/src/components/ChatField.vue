<template>
<div >
    <div ref="chathistory"   class="ac-game-chat-field-history">
      历史记录
    </div>
    <input  ref="chatinput" type="text" class="ac-game-chat-field-input">
</div>
</template>

<script>
import { onMounted, ref} from "vue";
import {useStore} from "vuex";

export default {
  name: "ChatField",
  setup(){
    let chats = ref(null);
    const store = useStore();
    let chathistory = ref(null);
    let chatinput = ref(null);
    onMounted(()=>{
      chats = store.state.pk.chats;
      chatinput.value.addEventListener("keydown", function (e) {
        if (e.keyCode == 13) {  // ENTER
          let username = store.state.user.username;
          let text = chatinput.value.value;
          if (text) {
            let message = username + ": " + text;
            store.state.pk.socket.send(JSON.stringify({
              event: "chat",
              message: message,
            }));
            chatinput.value.value = "";
          }
        }
      });
    })

    return {
      chatinput,
      chats,
      chathistory,
    }

  }
}

</script>

<style scoped>
.ac-game-chat-field-history {
  position: absolute;
  top: 66%;
  left: 90%;

  transform: translate(-50%, -50%);

  width: 20%;
  height: 32%;

  color: white;
  font-size: 2vh;

  padding: 1vh;
  overflow: auto;
}

.ac-game-chat-field-history::-webkit-scrollbar {
  width: 0;
}

.ac-game-chat-field-input {
  position: absolute;
  top: 86%;
  left: 85%;
  transform: translate(-50%, -50%);

  width: 10%;
  height: 3vh;

  color: white;
  font-size: 2vh;

  background-color: rgba(222,225,230, 0.2);
}
</style>