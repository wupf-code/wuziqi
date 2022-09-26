import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from "@/views/pkIndexView";
import UserAccountRegisterView from "@/views/UserAccountRegisterView";
import store from "@/store";
import UserAccountLoginView from "@/views/UserAccountLoginView";
const routes = [
  {
    path:"/",
    name:"home",
    redirect:"/pk/",
  },
  {
    path:"/pk/",
    name:"pk_index",
    component:PkIndexView,
    meta:{
      requestAuth:true
    }
  },
  {
    path:"/user/account/login/",
    name:"user_account_login",
    component:UserAccountLoginView,
    meta:{
      requestAuth:false
    }
  },
  {
    path:"/user/account/register/",
    name:"user_account_register",
    component:UserAccountRegisterView,
    meta:{
      requestAuth:false
    }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next)=>{
  if(to.meta.requestAuth && !store.state.user.is_login){
    next({name : "user_account_login"});
  }else{
    next();
  }

})

export default router
