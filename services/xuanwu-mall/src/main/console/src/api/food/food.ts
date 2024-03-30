import request from '@/utils/request';

export type FoodInfo = {
  id: number;
  name?: string;
  price?: string;
  description?: string;
  minOrderCount?: number;
  unit?: string;
  boxNum?: number;
  boxPrice?: string;
  categoryName?: string;
  soldOut?: number;
  picture?: string;
  sort?: number;
  skus?: Sku[];
}

export type Sku = {
  id?: number;
  skuCode?: string;
  spec?: string;
  upc?: string;
  price?: string;
  sort?: number;
  stock?: string
  weight?: number;
  weightUnit?: string;
}

export type ListFoodOptions = {
  page?: number;
  size?: number;
  id?: number;
  name?: string;
  description?: string;
}

export type CreateFoodRequest = {
  name?: string;
  price?: number;
  description?: string;
  minOrderCount?: number;
  unit?: string;
  boxNum?: number;
  boxPrice?: number;
  categoryName?: string;
  soldOut?: number;
  picture?: string;
  sort?: number;
  skus?: Sku[];
}

export type UpdateFoodRequest = {
  id: number;
  name?: string;
  price?: string;
  description?: string;
  minOrderCount?: number;
  unit?: string;
  boxNum?: number;
  boxPrice?: string;
  categoryName?: string;
  soldOut?: number;
  picture?: string;
  sort?: number;
  skus?: Sku[];
}

export function listFood(opts?: ListFoodOptions) {
  return request({
    url: '/api/v1/foods',
    method: 'get',
    params: opts
  });
}

export function getFood(id: number) {
  return request({
    url: `/api/v1/foods/${id}`,
    method: 'get'
  });
}

export function updateFood(id: number, updateReq: UpdateFoodRequest) {
  return request({
    url: `/api/v1/foods/${id}`,
    method: 'put',
    data: updateReq
  });
}

export function createFood(createReq: CreateFoodRequest) {
  return request({
    url: `/api/v1/foods`,
    method: 'post',
    data: createReq
  });
}

export function deleteFood(id: number) {
  return request({
    url: `/api/v1/foods/${id}`,
    method: 'delete'
  });
}