import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import App from './App.vue'
import HomeView from './views/HomeView.vue'
import LoginView from './views/LoginView.vue'
import InviteView from './views/InviteView.vue'
import AdminView from './views/AdminView.vue'
import SetupView from './views/SetupView.vue'
import { getCurrentUser, hasSession } from './api/auth'
import './style.css'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: HomeView, meta: { requiresAuth: true } },
    { path: '/login', component: LoginView },
    { path: '/convite', component: InviteView },
    { path: '/admin', component: AdminView, meta: { requiresAuth: true, requiresAdmin: true } },
    { path: '/configurar/:prestadoraId', component: SetupView, meta: { requiresAuth: true } },
  ],
})

router.beforeEach(async (to) => {
  if (!to.meta.requiresAuth) return
  if (!(await hasSession())) return '/login'
  if (to.meta.requiresAdmin) {
    const user = await getCurrentUser()
    if (user?.papelPlataforma !== 'ADMIN_SAAS') return '/'
  }
})

createApp(App).use(router).mount('#app')
