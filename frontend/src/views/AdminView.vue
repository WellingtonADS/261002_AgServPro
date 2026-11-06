<script setup lang="ts">
import { ref } from 'vue'
import { sendAuthRequest } from '../api/auth'

const nomePrestadora = ref('')
const nomeDiretor = ref('')
const emailDiretor = ref('')
const error = ref('')
const busy = ref(false)
const invite = ref<{ prestadoraId: string; conviteUrl: string; expiraEm: string } | null>(null)
const copied = ref(false)

async function provision() {
  error.value = ''
  invite.value = null
  copied.value = false
  busy.value = true
  try {
    invite.value = await sendAuthRequest('/api/v1/admin/prestadoras', {
      nomePrestadora: nomePrestadora.value,
      nomeDiretor: nomeDiretor.value,
      emailDiretor: emailDiretor.value,
    })
  } catch (cause) {
    error.value = cause instanceof Error ? cause.message : 'Não foi possível provisionar o acesso.'
  } finally {
    busy.value = false
  }
}

async function copyInvite() {
  if (!invite.value) return
  await navigator.clipboard.writeText(invite.value.conviteUrl)
  copied.value = true
}
</script>

<template>
  <main class="admin-page">
    <header class="admin-header">
      <div>
        <p class="auth-brand">AgServPro · Administração SaaS</p>
        <h1>Provisionar prestadora</h1>
        <p>Crie o acesso do diretor. Ele receberá um convite para configurar a prestadora.</p>
      </div>
      <a class="admin-link" href="/">Visão inicial</a>
    </header>

    <section class="admin-card" aria-labelledby="provision-title">
      <h2 id="provision-title">Dados iniciais</h2>
      <form class="auth-form" @submit.prevent="provision">
        <label for="provider-name">Nome da prestadora</label>
        <input id="provider-name" v-model.trim="nomePrestadora" maxlength="180" required />

        <label for="director-name">Nome do diretor</label>
        <input id="director-name" v-model.trim="nomeDiretor" autocomplete="name" maxlength="160" required />

        <label for="director-email">E-mail do diretor</label>
        <input id="director-email" v-model.trim="emailDiretor" type="email" autocomplete="email" maxlength="254" required />

        <p v-if="error" class="auth-error" role="alert">{{ error }}</p>
        <button class="auth-submit" type="submit" :disabled="busy">
          {{ busy ? 'Criando acesso…' : 'Criar prestadora e convite' }}
        </button>
      </form>
    </section>

    <section v-if="invite" class="admin-card invite-result" aria-live="polite">
      <h2>Acesso provisionado</h2>
      <p>Compartilhe este link com o diretor. Ele expira em {{ new Date(invite.expiraEm).toLocaleString('pt-BR') }}.</p>
      <label for="invite-link">Convite de uso único</label>
      <input id="invite-link" :value="invite.conviteUrl" readonly />
      <button class="auth-submit" type="button" @click="copyInvite">
        {{ copied ? 'Link copiado' : 'Copiar link' }}
      </button>
      <p class="auth-footnote">O link é exibido somente nesta tela. Guarde-o em local seguro até compartilhar.</p>
    </section>
  </main>
</template>
