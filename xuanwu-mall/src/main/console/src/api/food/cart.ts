import request from '@/utils/request';

export type CartItem = {
  id?: number;
  tableCode: string;
  foodId: number;
  foodSkuId?: number;
  foodQuantity: number;
  foodPrice?: string;
  foodPic?: string;
  foodName?: string;
  foodSubTitle?: string;
  foodSkuCode?: string;
  memberNickname?: string;
  deleteStatus?: number;
  foodCategoryId?: number;
  foodSn?: string;
  foodAttr?: string;
}

export type ListCartItemOptions = {
  page?: number;
  size?: number;
  id?: number;
  tableCode: string;
}

export type AddCartItemRequest = {
  id?: number;
  tableCode: string;
  foodId: number;
  foodSkuId?: number;
  foodQuantity: number;
  foodPrice?: string;
  foodPic?: string;
  foodName?: string;
  foodSubTitle?: string;
  foodSkuCode?: string;
  memberNickname?: string;
  deleteStatus?: number;
  foodCategoryId?: number;
  foodSn?: string;
  foodAttr?: string;
}

export type UpdateCartItemRequest = {
  id?: number;
  tableCode: string;
  foodId: number;
  foodSkuId?: number;
  foodQuantity: number;
  foodPrice?: number;
  foodPic?: string;
  foodName?: string;
  foodSubTitle?: string;
  foodSkuCode?: string;
  memberNickname?: string;
  deleteStatus?: number;
  foodCategoryId?: number;
  foodSn?: string;
  foodAttr?: string;
}

export function listCartItem(opts?: ListCartItemOptions) {
  return request({
    url: '/api/v1/cart',
    method: 'get',
    params: opts
  });
}

export function getCartItem(id: number) {
  return request({
    url: `/api/v1/cart/${id}`,
    method: 'get'
  });
}

export function updateCartItem(id: number, updateReq: UpdateCartItemRequest) {
  return request({
    url: `/api/v1/cart/${id}`,
    method: 'put',
    data: updateReq
  });
}

export function addCartItem(createReq: AddCartItemRequest) {
  return request({
    url: `/api/v1/cart`,
    method: 'post',
    data: createReq
  });
}

export function updateQuantity(tableCode: string, foodId: number, quantity: number) {
  return request({
    url: `/api/v1/cart/update_quantity`,
    method: 'put',
    data: {
      tableCode: tableCode,
      foodId: foodId,
      foodQuantity: quantity
    }
  });
}

export function deleteCartItem(tableCode: string, foodId: number) {
  return request({
    url: `/api/v1/cart/delete_item`,
    method: 'delete',
    params: {
      tableCode: tableCode,
      foodId: foodId
    }
  });
}

