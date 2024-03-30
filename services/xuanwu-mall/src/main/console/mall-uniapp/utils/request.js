const templateKey = 'CX2002030923534650064';
const domainLinks = "http://localhost:9099"

const TOKEN = "XUANWU_MALL_TOKEN"


function serverGet(url, data) {
	let promise = new Promise((resolve) => {
		let token = uni.getStorageSync(TOKEN).token;
		let headers = {
			'template-key': templateKey, //模版服务码
		};
		if (token) {
			headers.authorization = token;
		}
		let postData = data;
		uni.request({
			url: domainLinks + url,
			data: postData,
			method: 'GET',
			header: headers,
			success: function(res) {
				resolve(res.data);
			}
		})
	});
	return promise;
}

function serverPost(url, data) {
	let promise = new Promise((resolve) => {
		let token = uni.getStorageSync(TOKEN).token;
		let headers = {
			'template-key': templateKey, //模版服务码
		};
		if (token) {
			headers.authorization = token;
		}
		let postData = data;
		uni.request({
			url: domainLinks + url,
			data: postData,
			method: 'POST',
			header: headers,
			success: function(res) {
				resolve(res.data);
			}
		})
	});
	return promise;
}

function serverPut(url, data) {
	let promise = new Promise((resolve) => {
		let token = uni.getStorageSync(TOKEN).token;
		let headers = {
			'template-key': templateKey, //模版服务码
		};
		if (token) {
			headers.authorization = token;
		}
		let postData = data;
		uni.request({
			url: domainLinks + url,
			data: postData,
			method: 'PUT',
			header: headers,
			success: function(res) {
				resolve(res.data);
			}
		})
	});
	return promise;
}

function serverDelete(url, data) {
	let promise = new Promise((resolve) => {
		let token = uni.getStorageSync(TOKEN).token;
		let headers = {
			'template-key': templateKey, //模版服务码
		};
		if (token) {
			headers.authorization = token;
		}
		let postData = data;
		uni.request({
			url: domainLinks + url,
			data: postData,
			method: 'DELETE',
			header: headers,
			success: function(res) {
				resolve(res.data);
			}
		})
	});
	return promise;
}

module.exports = {
	get: serverGet,
	post: serverPost,
	put: serverPut,
	delete: serverDelete
}