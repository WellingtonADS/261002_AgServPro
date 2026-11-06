<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { sendAuthRequest } from '../api/auth'

const route = useRoute()
const router = useRouter()
const token = computed(() => typeof route.query.token === 'string' ? route.query.token : '')
const name = ref('')
const password = ref('')
const passwordConfirmation = ref('')
const error = ref('')
const busy = ref(false)

async function acceptInvitation() {
  error.value = ''
  if (!token.value) {
    error.value = 'O link está incompleto. Solicite um novo convite ao Administrador SaaS.'
    return
  }
  if (password.value !== passwordConfirmation.value) {
    error.value = 'As senhas informadas não são iguais.'
    return
  }
  busy.value = true
  try {
    await sendAuthRequest('/api/v1/auth/invitations/accept', {
      token: token.value,
      name: name.value,
      password: password.value,
    })
    await router.replace({ path: '/login', query: { activated: '1' } })
  } catch (cause) {
    error.value = cause instanceof Error ? cause.message : 'Não foi possível aceitar o convite.'
  } finally {
    busy.value = false
  }
}
</script>

<template>
  <main class="auth-page">
    <section class="auth-card" aria-labelledby="invite-title">
      <p class="auth-brand">AgServPro</p>
      <p class="auth-kicker">Convite para a prestadora</p>
      <h1 id="invite-title">Configure seu acesso</h1>
      <p class="auth-description">Crie sua senha para concluir o acesso de diretor.</p>

      <form class="auth-form" @submit.prevent="acceptInvitation">
        <label for="name">Nome completo</label>
        <input id="name" v-model.trim="name" autocomplete="name" maxlength="160" required />

        <label for="password">Senha</label>
        <input id="password" v-model="password" type="password" autocomplete="new-password" minlength="12" maxlength="72" required />
        <small>Use pelo menos 12 caracteres.</small>

        <label for="password-confirmation">Repita a senha</label>
        <input id="password-confirmation" v-model="passwordConfirmation" type="password" autocomplete="new-password" minlength="12" maxlength="72" required />

        <p v-if="error" class="auth-error" role="alert">{{ error }}</p>
        <button class="auth-submit" type="submit" :disabled="busy">
          {{ busy ? 'Salvando…' : 'Aceitar convite' }}
        </button>
      </form>
    </section>
  </main>
</template>
