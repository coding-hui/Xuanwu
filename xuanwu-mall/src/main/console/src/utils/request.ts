import axios, { AxiosResponse } from 'axios';
import { Message, Modal } from '@arco-design/web-react';
import { getToken } from '@/utils/token';

export type Response = {
  success: boolean;
  code: number;
  data?: Recordable;
  msg?: string;
}

// create an axios instance
const service = axios.create({
  baseURL: '', // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 10000 // request timeout,
});

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    // let each request carry token
    // ['X-Token'] is a custom headers key
    // please modify it according to the actual situation
    config.headers['Authorization'] = 'Bearer ' + getToken();
    config.headers['Content-Type'] = 'application/json';
    return config;
  },
  error => {
    // do something with request error
    console.log(error); // for debug
    return Promise.reject(error);
  }
);

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const httpStatus = response.status;
    const { code, msg, success } = response.data;
    if (httpStatus === 401) {
      if (location.href.indexOf('login') !== -1) {
        location.reload(); // 为了重新实例化vue-router对象 避免bug
      } else {
        Modal.warning({
          title: '系统提示',
          content: '登录状态已过期，您可以继续留在该页面，或者重新登录',
          okText: '重新登录',
          cancelText: '取消',
          onConfirm: () => {
            location.reload();
          }
        });
      }
    } else if (httpStatus === 400 || code === 403) {
      Message.error({
        content: msg,
        duration: 5 * 1000
      });
    } else if (httpStatus !== 200) {
      Message.error({
        content: msg
      });
      return Promise.reject('error');
    } else if (code === 100206) {
      Modal.warning({
        title: '系统提示',
        content: '登录状态已过期，您可以继续留在该页面，或者重新登录',
        okText: '重新登录',
        cancelText: '取消',
        onConfirm: () => {
          location.reload();
        }
      });
    } else if (!success) {
      Message.error({
        content: msg,
        duration: 5 * 1000
      });
    } else {
      return response.data;
    }
  },
  error => {
    if (error.message === 'Network Error') {
      Message.error({
        content: '服务器连接异常，请检查服务器！',
        duration: 5 * 1000
      });
      return;
    }
    let errorMsg = error.message;
    if (error.response && error.response.data && error.response.data.msg) {
      errorMsg = error.response.data.msg;
    }
    Message.error({
      content: errorMsg,
      duration: 5 * 1000
    });
    return Promise.reject(error);
  }
);

export default service;