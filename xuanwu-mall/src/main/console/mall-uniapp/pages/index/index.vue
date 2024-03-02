<template>
	<view class="container">
		<view class="header">
			<!-- 搜索栏 begin -->
			<!-- <view class="search-box">
				<view class="search-input" @tap="showSearch=true">
					<image src="/static/images/common/search-icon.png" class="search-icon"></image>
					<view>搜索</view>
				</view>
			</view> -->
			<!-- 搜索栏 end -->
			<view class="center">
				<view class="store">
					<view class="title">
						<!-- 为了测试方便，这里使用静态店铺数据 -->
						<view class="address">{{ (orderTable && orderTable.code) || "请先选择桌台"  }}</view>
						<image src="/static/images/common/black_arrow_right.png" class="right-icon"></image>
					</view>
					<!-- 外卖&自取switch begin -->
					<!-- <view class="buttons">
						<button type="default" class="button" 
								:class="{active: orderType == 'takein'}" plain 
								hover-class="none" @tap="switchOrderType">
							自取
						</button>
						<button type="default" class="button" 
								:class="{active: orderType == 'takeout'}" plain 
								hover-class="none" @tap="switchOrderType">
							外卖
						</button>
					</view> -->
					<!-- 外卖&自取switch end -->
				</view>
				<!-- <view class="location">10人就餐</view> -->
			</view>
		</view>
		<view class="main">
			<!-- 左侧菜单 begin -->
			<scroll-view class="menu-bar" scroll-y scroll-with-animation>
				<view class="wrapper">
					<view class="menu-item" @tap="handleMenuSelected(category.id)"
						:class="{active: currentCategoryId == category.id}" v-for="(category, index) in categories"
						:key="index">
						<!-- <image :src="category.category_image_url" class="image" mode="widthFix"></image> -->
						<view class="title">{{ category.name }}</view>
					</view>
				</view>
			</scroll-view>
			<!-- 左侧菜单 end -->
			<!-- 右侧商品列表 begin -->
			<scroll-view class="product-section" scroll-y scroll-with-animation :scroll-top="productsScrollTop"
				@scroll="productsScroll">
				<view class="wrapper">
					<!-- 商品 begin -->
					<view class="products-list" v-for="(category, index) in categories" :key="index"
						:id="`products-${category.id}`">
						<view class="category-name">{{ category.name }}</view>
						<view class="products">
							<view class="product" v-for="(product, key) in category.foods" :key="key">
								<!-- <image :src="product.images[0].url" mode="widthFix" class="image"></image> -->
								<view class="content">
									<view class="name">{{ product.name }}</view>
									<!-- <view class="labels">
										<view class="label"
											:style="{color: label.label_color, background: $util.hexToRgba(label.label_color, 0.2)}"
											v-for="label in product.labels" :key="label.id">{{ label.name }}</view>
									</view> -->
									<view class="description">{{ product.description }}</view>
									<view class="price">
										<view>￥{{ product.price }}</view>
										<actions @materials="showProductDetailModal(product)"
											:number="productCartNum(product.id)" @add="handleAddToCart(product)"
											@minus="handleMinusFromCart(product)" />
									</view>
								</view>
							</view>
						</view>
					</view>
					<!-- 商品 end -->
				</view>
			</scroll-view>
			<!-- 右侧商品列表 end -->
		</view>
		<!-- 商品详情 modal begin -->
		<product-modal :product="product" :visible="productModalVisible" @cancel="closeProductDetailModal"
			@add-to-cart="handleAddToCartInModal" />
		<!-- 商品详情 modal end -->
		<!-- 购物车栏 begin -->
		<cart-bar :cart="cart" @add="handleAddToCart" @minus="handleMinusFromCart" @clear="clearCart" @pay="pay" />
		<!-- 购物车栏 end -->
		<search :show="showSearch" :categories="categories" @hide="showSearch=false" @choose="showProductDetailModal">
		</search>
	</view>
</template>

<script>
	import {
		mapState,
		mapMutations
	} from 'vuex'
	import Actions from './components/actions/actions.vue'
	import CartBar from './components/cartbar/cartbar.vue'
	import ProductModal from './components/product-modal/product-modal.vue'
	import cartPopup from './components/cart-popup/cart-popup.vue'
	import Search from './components/search/search.vue'

	import {
		listCategorys
	} from '@/apis/category'
	import categories from '../../api/categories'
	import {
		listFoods
	} from '@/apis/foods'

	import {
		addCartItem,
		createOrder,
		getOrderTable
	} from '@/apis/order'

	export default {
		components: {
			Actions,
			CartBar,
			ProductModal,
			cartPopup,
			Search
		},
		data() {
			return {
				orderTable: "请行选择桌台",
				categories: [],
				cart: [],
				product: {},
				foods: [],
				currentCategoryId: 0,
				notices: [],
				ads1: [
					"https://go.cdn.heytea.com/storage/ad/2020/05/28/752a5519e89541bd8417614c599cf8c3.jpg",
					"https://go.cdn.heytea.com/storage/ad/2020/05/24/38b7f686cf10449c85b0f5489d5d958e.jpg",
					"https://go.cdn.heytea.com/storage/ad/2020/05/21/2315beb5105944e8b795c5c0084ec99f.jpg",
					"https://go.cdn.heytea.com/storage/ad/2020/05/21/b88c6780a73249b0b0166784917a5046.jpg"
				],
				ads2: [
					"https://go.cdn.heytea.com/storage/ad/2020/05/21/acfc6504f3074cf6b730f516adc558f6.jpg",
					"https://go.cdn.heytea.com/storage/ad/2020/04/26/2373600789c64752b2415293877ead40.jpg",
					"https://go.cdn.heytea.com/storage/ad/2020/04/22/515df8c726e740089ae4c55582b4ce09.jpg",
					"https://go.cdn.heytea.com/storage/ad/2020/04/14/d0e51cb22c0a437293c0e6a879b59c7d.jpg"
				],
				productModalVisible: false,
				cartPopupShow: false,
				productsScrollTop: 0,
				showSearch: false
			}
		},
		computed: {
			...mapState(['orderType', 'address']),
			productCartNum() { //计算单个饮品添加到购物车的数量
				return id => this.cart.reduce((acc, cur) => {
					if (cur.id === id) {
						return acc += cur.number
					}
					return acc
				}, 0)
			}
		},
		async onLoad(option) {
			let self = this

			let {
				orderTableId
			} = option

			self.orderTable = await getOrderTable(orderTableId)

			let categoryRes = await listCategorys({
				"page": 0,
				"size": 1000
			})
			this.categories = categoryRes.records

			let foodRes = await listFoods({
				"page": 0,
				"size": 1000
			})
			this.foods = foodRes.records

			this.categories.forEach(item => {
				if (!item.foods) {
					item.foods = []
				}

				item.foods = this.foods.filter(food => {
					return food.category && food.category.id === item.id
				}) || []
			})


			// this.notices = await this.$api('notices')
			// this.categories = await this.$api('categories')
			this.currentCategoryId = this.categories.length && this.categories[0].id
			this.$nextTick(() => this.calcSize())
		},
		methods: {
			...mapMutations(['SET_ORDER_TYPE']),
			switchOrderType() {
				if (this.orderType === 'takein') {
					uni.navigateTo({
						url: '/pages/addresses/addresses'
					})
				} else {
					this.SET_ORDER_TYPE('takein')
				}
			},
			handleAddToCart(product) { //添加到购物车
				const index = this.cart.findIndex(item => {
					// if (!product.is_single) {
					// 	return (item.id == product.id) && (item.materials_text == product.materials_text)
					// } else {
					return item.foodId === product.id || item.foodId === product.foodId
					// }
				})

				if (index > -1) {
					this.cart[index].foodQuantity += 1
					return
				}

				this.cart.push({
					tableCode: this.orderTable.code,
					foodId: product.id || product.foodId,
					foodPrice: product.price,
					foodQuantity: 1,
					tableCode: 1,
					foodName: product.name
				})
			},
			handleMinusFromCart(product) { //从购物车减商品
				let index
				// if (product.is_single) {
				index = this.cart.findIndex(item => item.foodId == product.foodId)
				// } else {
				// 	index = this.cart.findIndex(item => (item.foodId == product.id) && (item.materials_text == product
				// 		.materials_text))
				// }
				this.cart[index].foodQuantity -= 1
				if (this.cart[index].foodQuantity <= 0) {
					this.cart.splice(index, 1)
				}
			},
			showProductDetailModal(product) {
				this.product = product
				this.productModalVisible = true
			},
			handleAddToCartInModal(product) {
				this.handleAddToCart(product)
				this.closeProductDetailModal()
			},
			closeProductDetailModal() {
				this.productModalVisible = false
				this.product = {}
			},
			openCartDetailsPopup() {
				this.$refs['cartPopup'].open()
			},
			clearCart() {
				this.cart = []
			},
			handleMenuSelected(id) {
				this.productsScrollTop = this.categories.find(item => item.id == id).top
				this.$nextTick(() => this.currentCategoryId = id)
			},
			productsScroll({
				detail
			}) {
				const {
					scrollTop
				} = detail
				let tabs = this.categories.filter(item => item.top <= scrollTop).reverse()
				if (tabs.length > 0) {
					this.currentCategoryId = tabs[0].id
				}
			},
			calcSize() {
				let h = 0
				let view = uni.createSelectorQuery().select('#ads')
				view.fields({
					size: true
				}, data => {
					h += Math.floor(data.height)
				}).exec()

				this.categories.forEach(item => {
					let view = uni.createSelectorQuery().select(`#products-${item.id}`)
					view.fields({
						size: true
					}, data => {
						item.top = h
						h += Math.floor(data.height)
						item.bottom = h
					}).exec()
				})
			},
			pay() {
				this.cart.forEach(item => {
					item.tableCode = this.orderTable.code
				})
				let orderId = this.orderTable.order ? this.orderTable.order.id : null
				console.log("orderId=", orderId)
				addCartItem(this.cart).then(() => {
					createOrder({
						payType: 0,
						orderId: orderId,
						tableCode: this.orderTable.code
					}).then(res => {
						this.cart = []
						uni.removeStorageSync('currentOrderTable');
						uni.showToast({
							title: '下单成功',
							duration: 2000
						})
						setTimeout(() => {
							uni.navigateTo({
								url: `/pages/order/detail?id=${res.order.id}`
							})
						}, 500)
					})
				})
				// uni.setStorageSync('cart', this.cart)
			}
		}
	}
</script>

<style lang="scss">
	@import './index.scss';
</style>