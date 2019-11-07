依赖原生 aar 包地址：

https://www.yuque.com/aliyun_iot/product/android-device-sdk

组件源代码：

https://github.com/chubin518/AIotCore/tree/master/AIoT_SDK

插件使用示例：

https://github.com/chubin518/AIotCore/tree/master/%E7%BB%84%E4%BB%B6%E5%BC%95%E7%94%A8UI

代码示例

```
const aiotMqttClient = uni.requireNativePlugin('AIoT-Core')
//阿里IOT三元组
	const conf = {
		ProductKey: "",
		DeviceName: "",
		DeviceSecret: ""
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
```
