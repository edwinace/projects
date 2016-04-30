QuizApp.PushService = {
	API_KEY : QuizApp.gcm.Config.gcmAPIKey,
	GCM_ENDPOINT : 'https://android.googleapis.com/gcm/send',
	SUBSCRIPTION_ID : null,
	isPushEnabled : false,

	init : function() {
		this.addSubscribeSwitchEvent();
		this.registerServiceWorker();
	},
	addSubscribeSwitchEvent : function() {
		var pushButton = document.querySelector('#push-subscription');
		pushButton.addEventListener('click', function() {
			this.isPushEnabled ? this.unsubscribe() : this.subscribe(); 
		}.bind(this));
	},
	registerServiceWorker : function() {
		if ('serviceWorker' in navigator) {
			navigator.serviceWorker.register('/service-worker.js').then(this.initialiseState.bind(this));
		} else {
			this.showError('PUSH_NOT_SUPPORTED_MESSAGE');
		}  
	},
	initialiseState : function () {
		if (!('showNotification' in ServiceWorkerRegistration.prototype)) {
			this.showError('PUSH_NOT_SUPPORTED_MESSAGE');
			return;
		}

		if (Notification.permission === 'denied') {
			this.showError('PUSH_PERMISSION_DENINED');
			return;
		}

		if (!('PushManager' in window)) {
			this.showError('PUSH_NOT_SUPPORTED_MESSAGE');
			return;
		}

		navigator.serviceWorker.ready
			.then(this.getSubscription)
			.then(this.setInitialButtonState.bind(this))
			.catch(function(err) {
				this.showError('ERROR_WHEN_GET_SUBSCRIBE_INFO');
			}.bind(this));
	},
	getSubscription : function(serviceWorkerRegistration){
		return serviceWorkerRegistration.pushManager.getSubscription()
	},
	setInitialButtonState : function(subscription) {
		var pushButton = document.querySelector('#push-subscription');
		pushButton.disabled = false;

		if (!subscription) {
			return;
		}

		this.sendSubscriptionToServer(subscription);

		pushButton.textContent = this.message.CLICK_HERE_TO_UNSCRIBE;
		this.isPushEnabled = true;
	},
	sendSubscriptionToServer : function(subscription) {
		var endpointSections = subscription.endpoint.split('/');
		var subscriptionId = endpointSections[endpointSections.length - 1];
		this.SUBSCRIPTION_ID = subscriptionId;
		$.post('/api/private/push', {subscriptionId : subscriptionId});
	},
	subscribe : function () {
		var pushButton = document.querySelector('#push-subscription');
		pushButton.disabled = true;

		navigator.serviceWorker.ready
			.then(this.addSubscribeToServiceWorker)
			.then(this.changeButtonTextAndSyncSubscriptionInfoWithServer.bind(this, pushButton))
			.catch(function(e) {
				if (Notification.permission === 'denied') {
					this.showError('PUSH_PERMISSION_DENINED');
					pushButton.disabled = true;
				} else {
					this.showError('ERROR_WHEN_SUBSCRIBE');
					pushButton.disabled = false;
					pushButton.textContent = this.message.CLICK_HERE_TO_SUBSCRIBE;
				}
			}.bind(this));
	},
	addSubscribeToServiceWorker : function(serviceWorkerRegistration) {
		return serviceWorkerRegistration.pushManager.subscribe({userVisibleOnly: true})
	},
	changeButtonTextAndSyncSubscriptionInfoWithServer : function(pushButton, subscription) {
		this.isPushEnabled = true;
		pushButton.textContent = this.message.CLICK_HERE_TO_UNSCRIBE;
		pushButton.disabled = false;
		this.sendSubscriptionToServer(subscription);
	},
	unsubscribe : function () {
		var pushButton = document.querySelector('#push-subscription');
		pushButton.disabled = true;

		navigator.serviceWorker.ready
			.then(this.getSubscription)
			.then(this.deleteSubscribeFromServiceWorker.bind(this))
			.then(this.changeButtonTextAndSyncUnSubscriptionInfoWithServer.bind(this, pushButton))
			.catch(function(e) {
				this.showError('ERROR_WHEN_UNSCRIBE');
				pushButton.disabled = false;
			}.bind(this));
	},
	deleteSubscribeFromServiceWorker : function(pushSubscription) {
		if (!pushSubscription) {
			this.isPushEnabled = false;
			pushButton.disabled = false;
			pushButton.textContent = this.message.CLICK_HERE_TO_SUBSCRIBE;
			return;
		}
		return pushSubscription.unsubscribe()
	},
	changeButtonTextAndSyncUnSubscriptionInfoWithServer : function(pushButton, successful) {
		pushButton.disabled = false;
		pushButton.textContent = this.message.CLICK_HERE_TO_SUBSCRIBE;
		this.isPushEnabled = false;
		$.ajax({
			url: '/api/private/push/' + this.SUBSCRIPTION_ID,
			type: 'DELETE',
			success: function(result) {
				this.SUBSCRIPTION_ID = null;
			}
		});
	},
	showError : function(sType) {
		alert(this.message[sType]);
	},
	message : {
		PUSH_NOT_SUPPORTED: "죄송합니다. 사용중이신 브라우저는 Push 알림 서비스를 이용할 수 없습니다. 크롬 브라우저를 이용해 주세요",
		PUSH_PERMISSION_DENINED : "사용자의 요청에 의해 Push 알림 서비스가 제공되지 않습니다.",
		ERROR_WHEN_UNSCRIBE : "죄송합니다. 푸시 알림 수신 해제중 오류가 발생했습니다. 잠시후 다시 시도해 주세요",
		ERROR_WHEN_SUBSCRIBE : "죄송합니다. 푸시 알림 수신 등록중 오류가 발생했습니다. 잠시후 다시 시도해 주세요",
		ERROR_WHEN_GET_SUBSCRIBE_INFO : "죄송합니다. 푸시 알림 수신 여부 확인중 문제가 발생했습니다. 새로고침 해주세요",
		CLICK_HERE_TO_SUBSCRIBE : " 여기를 눌러 푸시 알림을 구독하세요 ",
		CLICK_HERE_TO_UNSCRIBE : " 여기를 눌러 푸시 알림을 구독을 해지하세요 ",
	}, 
}