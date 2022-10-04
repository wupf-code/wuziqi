<template>
  <ContentFiled>
    <div class="container">
      <div class="row justify-content-md-center">
        <div class="col-3">
          <form @submit.prevent="register">
            <div class="mb-3">
              <label for="username" class="form-label">用户名</label>
              <input type="text" v-model="username" class="form-control" id="username" placeholder="用户名">
            </div>
            <div class="mb-3">
              <label for="password" class="form-label">Password</label>
              <input type="password" v-model="password" class="form-control" id="password" placeholder="请输入密码">
            </div>
            <div class="mb-3">
              <label for="confirmedPassword" class="form-label">confirmedPassword</label>
              <input type="password" v-model="confirmedPassword" class="form-control" id="confirmedPassword" placeholder="请确认密码">
            </div>
            <div class="error_message">{{ error_message }}</div>
            <button type="submit" class="btn btn-primary">注册</button>
          </form>
        </div>
      </div>
    </div>
  </ContentFiled>
</template>

<script>
import { ref } from "vue";
import router from "@/router";
import {useStore} from "vuex";
import ContentFiled from "@/components/ContentField";
import $ from "jquery"
export default {
  name: "UserAccountRegisterView",
  components:{
    ContentFiled,
  },
  setup(){
    const store = useStore();
    let username = ref("");
    let password = ref("");
    let confirmedPassword = ref("");
    let error_message = ref("");
    const register = ()=>{
      $.ajax({
        url:"http://127.0.0.1:3000/api/user/account/register/",
        // url:"https://chess.ymswdfg.top/api/user/account/register/",

        type:"post",

        data:{
          username:username.value,
          password:password.value,
          confirmedPassword:confirmedPassword.value,
        },
        success(resp){
          if(resp.error_message === "success"){
            router.push({name: "user_account_login"});
          }else{
            error_message.value = resp.error_message;
          }
        },
        error(resp){
          console.log(resp);
        }
      });
    }
    return {
      username,
      password,
      confirmedPassword,
      error_message,
      register,
      store,
    }
  }
}
</script>

<style scoped>
button{
  width: 100%;
}
.error_message{
  color: red;
}
</style>