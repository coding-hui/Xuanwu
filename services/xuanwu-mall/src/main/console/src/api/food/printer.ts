import request from '@/utils/request';

export type ListPrinterOptions = {
  page?: number;
  size?: number;
  id?: number;
  name?: string;
  description?: string;
}

export type CreatePrinterRequest = {
  name: string;
  description?: string;
}

export type UpdatePrinterRequest = {
  id: number;
  name: string;
  description?: string;
}

export function listPrinters(opts?: ListPrinterOptions) {
  return request({
    url: '/api/v1/printer/service',
    method: 'get',
    params: opts
  });
}

export function getPrinter(id: number) {
  return request({
    url: `/api/v1/printer/${id}`,
    method: 'get'
  });
}

export function updatePrinter(id: number, updateReq: UpdatePrinterRequest) {
  return request({
    url: `/api/v1/printer/${id}`,
    method: 'put',
    data: updateReq
  });
}

export function createPrinter(createReq: CreatePrinterRequest) {
  return request({
    url: `/api/v1/printer`,
    method: 'post',
    data: createReq
  });
}

export function deletePrinter(id: number) {
  return request({
    url: `/api/v1/printer/${id}`,
    method: 'delete'
  });
}

export function printTestPage(id: number) {
  return request({
    url: `/api/v1/printer/print_test_page/${id}`,
    method: 'get'
  });
}

export function submitPrintJob(orderId: number, type: number) {
  return request({
    url: `/api/v1/printer/submit_print_job/${orderId}`,
    params: {
      type: type
    },
    method: 'get'
  });
}