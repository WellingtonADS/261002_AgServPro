<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCurrentUser, sendAuthRequest } from '../api/auth'

const route = useRoute()
const router = useRouter()
const email = ref('')
const password = ref('')
const error = ref('')
const busy = ref(false)

async function login() {
  error.value = ''
  busy.value = true
  try {
    await sendAuthRequest('/api/v1/auth/session', { email: email.value, password: password.value })
    const user = await getCurrentUser()
    const pendingProvider = user?.prestadoras.find((membership) => membership.papel === 'DIRETOR' && membership.prestadoraStatus === 'PENDENTE_CONFIGURACAO')
    await router.replace(user?.papelPlataforma === 'ADMIN_SAAS' ? '/admin' : pendingProvider ? `/configurar/${pendingProvider.prestadoraId}` : '/')
  } catch (cause) {
    error.value = cause instanceof Error ? cause.message : 'Não foi possível entrar.'
  } finally {
    busy.value = false
  }
}
</script>

<template>
  <main class="auth-page">
    <section class="auth-card" aria-labelledby="login-title">
      <p class="auth-brand">AgServPro</p>
      <p class="auth-kicker">Acesso seguro</p>
      <h1 id="login-title">Entre na sua conta</h1>
      <p class="auth-description">Acesse a gestão dos atendimentos da sua prestadora.</p>

      <p v-if="route.query.activated === '1'" class="auth-success" role="status">
        Convite aceito. Entre com seu e-mail e a senha que acabou de criar.
      </p>
      <form class="auth-form" @submit.prevent="login">
        <label for="email">E-mail</label>
        <input id="email" v-model.trim="email" type="email" autocomplete="username" required />

        <label for="password">Senha</label>
        <input id="password" v-model="password" type="password" autocomplete="current-password" required />

        <p v-if="error" class="auth-error" role="alert">{{ error }}</p>
        <button class="auth-submit" type="submit" :disabled="busy">
          {{ busy ? 'Entrando…' : 'Entrar' }}
        </button>
      </form>
      <p class="auth-footnote">O acesso é criado pelo Administrador SaaS da plataforma.</p>
    </section>
  </main>
</template>
