import api from './api'

export function listFoods(data) {
  return api.get(`/api/v1/foods`, data, { login: false })
}
