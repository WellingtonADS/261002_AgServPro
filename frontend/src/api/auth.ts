type CsrfResponse = { token: string }

export async function sendAuthRequest<T>(path: string, body?: unknown, method = 'POST'): Promise<T> {
  const csrfResponse = await fetch('/api/v1/auth/csrf', { credentials: 'include' })
  if (!csrfResponse.ok) throw new Error('Não foi possível preparar a sessão. Tente novamente.')
  const csrf = (await csrfResponse.json()) as CsrfResponse

  const response = await fetch(path, {
    method,
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json',
      'X-XSRF-TOKEN': csrf.token,
    },
    body: JSON.stringify(body),
  })

  if (!response.ok) {
    const error = (await response.json().catch(() => ({}))) as { detail?: string }
    throw new Error(error.detail ?? 'Não foi possível concluir a operação.')
  }

  return response.status === 204 ? (undefined as T) : ((await response.json()) as T)
}

export async function hasSession(): Promise<boolean> {
  try {
    const response = await fetch('/api/v1/auth/me', { credentials: 'include' })
    return response.ok
  } catch {
    return false
  }
}

export type CurrentUser = {
  papelPlataforma: string | null
  prestadoras: Array<{ prestadoraId: string; prestadora: string; prestadoraStatus: string; papel: string; status: string }>
}

export async function getCurrentUser(): Promise<CurrentUser | null> {
  const response = await fetch('/api/v1/auth/me', { credentials: 'include' })
  if (!response.ok) return null
  return (await response.json()) as CurrentUser
}
