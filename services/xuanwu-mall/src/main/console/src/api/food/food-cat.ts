import request from '@/utils/request';

export type ListFoodCatOptions = {
  page?: number;
  size?: number;
  id?: number;
  name?: string;
  description?: string;
}

export type CreateFoodCatRequest = {
  name: string;
  description?: string;
}

export type UpdateFoodCatRequest = {
  id: number;
  name: string;
  description?: string;
}

export function listFoodCats(opts?: ListFoodCatOptions) {
  return request({
    url: '/api/v1/food/cats',
    method: 'get',
    params: opts
  });
}

export function getFoodCat(id: number) {
  return request({
    url: `/api/v1/food/cats/${id}`,
    method: 'get'
  });
}

export function updateFoodCat(id: number, updateReq: UpdateFoodCatRequest) {
  return request({
    url: `/api/v1/food/cats/${id}`,
    method: 'put',
    data: updateReq
  });
}

export function createFoodCat(createReq: CreateFoodCatRequest) {
  return request({
    url: `/api/v1/food/cats`,
    method: 'post',
    data: createReq
  });
}

export function deleteFoodCat(id: number) {
  return request({
    url: `/api/v1/food/cats/${id}`,
    method: 'delete'
  });
}