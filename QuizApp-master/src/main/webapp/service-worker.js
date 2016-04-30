var DATA_URL = "/api/push/data";
self.addEventListener('push', function(event) {
		var icon = '/images/icon-192x192.png';

    event.waitUntil(
        fetch(DATA_URL).then(function(response) {
            if (response.status !== 200) {
                throw new Error();
            }
            return response.json()
        }).then(function(data) {
            self.registration.showNotification(data.title, {
                body: data.message,
                // icon: icon,
                tag: data.notiTag,
                data: data.openUrl
            });
        })
    );
});

self.addEventListener('notificationclick', function(event) {
    // Android doesn’t close the notification when you click on it
    event.notification.close();

    // This looks to see if the current is already open and focuses if it is
    event.waitUntil(clients.matchAll({
        type: "window"
    }).then(function(clientList) {
        for (var i = 0; i < clientList.length; i++) {
            var client = clientList[i];
            if (client.url == '/' && 'focus' in client)
                return client.focus();
        }
        
        if (clients.openWindow) {
            return clients.openWindow(event.notification.data);
        }
    }));
});