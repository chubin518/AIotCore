<template>
	<view class="content">
	
		<button type="primary" @click="connect">连接IOT</button>

		<button type="default" @click="publish">发布消息</button>

		<button type="default" @click="subscribe">监听消息</button>

		<button type="warn" @click="disconnect">断开IOT连接</button>

	</view>
</template>

<script>
	const aiotMqttClient = uni.requireNativePlugin('AIoT-Core')
	export default {
		data() {
			return {
			}
		},
		onLoad() {

		},
		methods: {
			connect() {
				try {
					aiotMqttClient.connect({
						"ProductKey": "a1j4KvvPJb1",
						"DeviceName": "test_device1",
						"DeviceSecret": "eQHe9y0CCXnDRVb8QnFRCN2Wd1adJQ3C"
					}, result => {
						console.log(result)
					})
				} catch (e) {
					console.error(e)
				}
			},
			disconnect() {
				aiotMqttClient.disconnect(result => {
					console.log(result)
				});
			},
			publish() {

			},
			subscribe() {
				aiotMqttClient.subscribe({
					topic: `/sys/a1j4KvvPJb1/test_device1/thing/service/property/set`,
				}, result => {
					console.log(result)
				}, result => {
					console.log(result)
				})


				aiotMqttClient.subscribe({
					topic: `/a1j4KvvPJb1/test_device1/user/get`,
				}, result => {
					console.log(result)
					uni.redirectTo({
						url: '../Detail/Detail?id=' + result.data.payload
					})
				}, result => {
					console.log(result)
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
