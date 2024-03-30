import api from './api'

export function listCategorys(data) {
  return api.get(`/api/v1/food/cats`, data, { login: false })
}
