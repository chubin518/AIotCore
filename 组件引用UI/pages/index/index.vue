<template>
	<view class="content">

		<button type="primary" @click="connect">连接IOT</button>

		<button type="default" @click="publish">发布消息</button>

		<button type="default" @click="subscribe">监听消息</button>

		<button type="warn" @click="disconnect">断开IOT连接</button>
		
		/**
		 * 暂时模拟器打开崩溃，安装至手机端OK
		 */
		<map style="width: 100%; height: 300px;" :latitude="latitude" :longitude="longitude">
		</map>
		
	</view>
</template>

<script>
	const aiotMqttClient = uni.requireNativePlugin('AIoT-Core')
	//阿里IOT三元组
	const conf = {
		ProductKey: "a1j4KvvPJb1",
		DeviceName: "test_device1",
		DeviceSecret: "eQHe9y0CCXnDRVb8QnFRCN2Wd1adJQ3C"
	}
	export default {
		data() {
			return {
				latitude: 39.873371, //纬度
				longitude: 116.501376, //经度
			}
		},
		onLoad() {

		},
		methods: {
			connect() {
				try {
					aiotMqttClient.connect(conf, result => {
						uni.showToast({
							title: JSON.stringify(result)
						})
					})
				} catch (e) {
					console.error(e)
				}
			},
			disconnect() {
				aiotMqttClient.disconnect(result => {
					uni.showToast({
						title: JSON.stringify(result)
					})
				});
			},
			publish() {
				aiotMqttClient.publish({
					topic: `/${conf.ProductKey}/${conf.DeviceName}/user/update`,
					content: 'dadefrer'
				}, result => {
					uni.showToast({
						title: JSON.stringify(result)
					})
				})
			},
			subscribe() {
				aiotMqttClient.subscribe({
					topic: `/sys/${conf.ProductKey}/${conf.DeviceName}/thing/service/property/set`,
				}, result => {
					uni.showToast({
						title: JSON.stringify(result)
					})
				}, result => {
					uni.showToast({
						title: JSON.stringify(result)
					})
				})


				aiotMqttClient.subscribe({
					topic: `/${conf.ProductKey}/${conf.DeviceName}/user/get`,
				}, result => {
					uni.showToast({
						title: JSON.stringify(result)
					})
				}, result => {
					uni.showToast({
						title: JSON.stringify(result)
					})
				})
			}
		}
	}
</script>

<style>
	button {
		margin-top: 30upx;
		margin-bottom: 30upx;
	}

	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}
</style>
