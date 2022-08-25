import { createRouter, createWebHashHistory } from 'vue-router'
import PkIndexView from "@/views/pkIndexView";
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
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
