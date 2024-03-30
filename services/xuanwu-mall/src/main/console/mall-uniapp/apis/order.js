import api from './api'

export function createOrder(data) {
	return api.post(`/api/v1/order`, data, {
		login: false
	})
}

export function getOrderDetails(orderId) {
	return api.get(`/api/v1/order/${orderId}`, undefined, {
		login: false
	})
}

export function listOrders(data) {
	return api.get(`/api/v1/order`, {
		page: 0,
		size: 100,
		...data
	}, {
		login: false
	})
}

export function addCartItem(data) {
	return api.post(`/api/v1/cart/batch_add`, data, {
		login: false
	})
}

export function listOrderTables(data) {
	return api.get(`/api/v1/order_tables`, {
		page: 0,
		size: 100,
		...data
	}, {
		login: false
	})
}

export function paySuccdss(data) {
	return api.get(`/api/v1/order/pay_success`, data, {
		login: false
	})
}

export function getOrderTable(tableId) {
	return api.get(`/api/v1/order_tables/${tableId}`, undefined, {
		login: false
	})
}
