<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { sendAuthRequest } from '../api/auth'

const route = useRoute()
const router = useRouter()
const providerId = String(route.params.prestadoraId)
const nome = ref('')
const fusoHorario = ref('America/Manaus')
const error = ref('')
const busy = ref(false)

async function completeSetup() {
  error.value = ''
  busy.value = true
  try {
    await sendAuthRequest(
      `/api/v1/prestadoras/${providerId}/configuracao`,
      { nome: nome.value, fusoHorario: fusoHorario.value },
      'PUT',
    )
    await router.replace('/')
  } catch (cause) {
    error.value = cause instanceof Error ? cause.message : 'Não foi possível salvar a configuração.'
  } finally {
    busy.value = false
  }
}
</script>

<template>
  <main class="auth-page">
    <section class="auth-card" aria-labelledby="setup-title">
      <p class="auth-brand">AgServPro</p>
      <p class="auth-kicker">Configuração inicial</p>
      <h1 id="setup-title">Configure sua prestadora</h1>
      <p class="auth-description">Confirme o nome e o fuso horário usados nos atendimentos.</p>

      <form class="auth-form" @submit.prevent="completeSetup">
        <label for="provider-name">Nome da prestadora</label>
        <input id="provider-name" v-model.trim="nome" maxlength="180" required />

        <label for="timezone">Fuso horário</label>
        <select id="timezone" v-model="fusoHorario" required>
          <option value="America/Manaus">Manaus</option>
          <option value="America/Sao_Paulo">São Paulo</option>
          <option value="America/Belem">Belém</option>
          <option value="America/Boa_Vista">Boa Vista</option>
          <option value="America/Rio_Branco">Rio Branco</option>
        </select>

        <p v-if="error" class="auth-error" role="alert">{{ error }}</p>
        <button class="auth-submit" type="submit" :disabled="busy">
          {{ busy ? 'Salvando…' : 'Concluir configuração' }}
        </button>
      </form>
    </section>
  </main>
</template>
