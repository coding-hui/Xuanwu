import request from '@/utils/request';
import { Order } from '@/api/food/order';

export type OrderTable = {
  id: number;
  code?: string;
  description?: string;
  status?: number;
  numberOfDiners?: number;
  order?: Order;
}

export type ListOrderTableOptions = {
  page?: number;
  size?: number;
  id?: number;
  status?: number;
  code?: string;
  description?: string;
}

export type CreateOrderTableRequest = {
  code?: string;
  description?: string;
  status?: number;
}

export type UpdateOrderTableRequest = {
  id: number;
  code?: string;
  description?: string;
  status?: number;
  numberOfDiners?: number;
}

export function listOrderTable(opts?: ListOrderTableOptions) {
  return request({
    url: '/api/v1/order_tables',
    method: 'get',
    params: opts
  });
}

export function getOrderTable(id: number) {
  return request({
    url: `/api/v1/order_tables/${id}`,
    method: 'get'
  });
}

export function updateOrderTable(id: number, updateReq: UpdateOrderTableRequest) {
  return request({
    url: `/api/v1/order_tables/${id}`,
    method: 'put',
    data: updateReq
  });
}

export function createOrderTable(createReq: CreateOrderTableRequest) {
  return request({
    url: `/api/v1/order_tables`,
    method: 'post',
    data: createReq
  });
}

export function deleteOrderTable(id: number) {
  return request({
    url: `/api/v1/order_tables/${id}`,
    method: 'delete'
  });
}

export function completedOrderTable(id: number) {
  return request({
    url: `/api/v1/order_tables/completed/${id}`,
    method: 'delete'
  });
}