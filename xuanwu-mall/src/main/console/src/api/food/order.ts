import request from '@/utils/request';

export type Order = {
  id?: number;
  tableCode: string;
  orderSn?: string;
  memberUsername?: string;
  totalAmount?: number;
  payAmount?: number;
  freightAmount?: number;
  promotionAmount?: number;
  integrationAmount?: number;
  couponAmount?: number;
  discountAmount?: number;
  payType?: number;
  sourceType?: number;
  status?: number;
  orderType?: number;
  note?: string;
  deleteStatus?: number;
  paymentTime?: Date;
  orderItems?: OrderItem[];
  createdAt?: string;
}

export type OrderItem = {
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

export type ListOrderOptions = {
  page?: number;
  size?: number;
  id?: number;
  tableCode?: string;
}

export type CreateOrderRequest = {
  payType?: number;
  orderId?: number;
  tableCode: string;
}

export function listOrder(opts?: ListOrderOptions) {
  return request({
    url: '/api/v1/order',
    method: 'get',
    params: opts
  });
}

export function getOrder(id: number) {
  return request({
    url: `/api/v1/order/${id}`,
    method: 'get'
  });
}

export function createOrder(createReq: CreateOrderRequest) {
  return request({
    url: `/api/v1/order`,
    method: 'post',
    data: createReq
  });
}

export function deleteOrder(orderId: number) {
  return request({
    url: `/api/v1/order/${orderId}`,
    method: 'delete'
  });
}

export function cancelOrder(orderId: number) {
  return request({
    url: `/api/v1/order/cancel_order/${orderId}`,
    method: 'get'
  });
}

export function paySuccess(orderId: number, payType: number, printSalesTicket = true) {
  return request({
    url: `/api/v1/order/pay_success`,
    method: 'get',
    params: {
      orderId: orderId,
      payType: payType,
      printSalesTicket: printSalesTicket
    }
  });
}
