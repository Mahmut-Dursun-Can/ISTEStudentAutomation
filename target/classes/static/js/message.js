    function goBack() {
        document.getElementById("chat-box").style.display = "none";
    }

function handleClick(element) {
    // Tıklanan öğeden kullanıcı bilgilerini al
    const targetUserId = element.getAttribute('data-user-id');
    const targetUsername = element.getAttribute('data-username');

    // Messaging panelini aç ve kullanıcı bilgilerini ilet
    openMessagingPanel(targetUserId, targetUsername);
}

    let stompClients = {};
    let messageHistories = {};

    function openMessagingPanel(targetUserId, targetUsername) {
        // Chat kutusunu göster
        const chatBox = document.getElementById("chat-box");
        chatBox.style.display = "block";

        // Kullanıcı adını yaz
        const usernameSpan = document.getElementById("chat-username");
        usernameSpan.textContent = targetUsername;

        // Mesaj alanını temizle
        const messageArea = document.getElementById("chat-messages");
        messageArea.innerHTML = '';

        // Geçmiş mesajları kontrol et ve varsa göster
        const history = messageHistories?.[targetUserId];
        if (history && Array.isArray(history)) {
            history.forEach(msg => {
                showMessage(msg.content, msg.senderUsername);
            });
        }

        // Mesajlaşma bağlantısını başlat
        connect(targetUserId);
    }


    function connect(targetUserId) {
        if (!stompClients[targetUserId]) {
            const socket = new SockJS('/ws');
            stompClients[targetUserId] = Stomp.over(socket);
            stompClients[targetUserId].connect({}, function () {
                console.log(`STOMP bağlantısı kuruldu, ${targetUserId} için abone olunuyor...`);
                stompClients[targetUserId].subscribe('/topic/messages', function (messageOutput) {
                    console.log("Gelen mesaj:", messageOutput.body);
                    const message = JSON.parse(messageOutput.body);
                    if ((message.senderId == currentUserId && message.recipientId == targetUserId)
                        || (message.senderId == targetUserId && message.recipientId == currentUserId)) {
                        showMessage(message.content, message.senderUsername);
                        if (!messageHistories[targetUserId]) {
                            messageHistories[targetUserId] = [];
                        }
                        messageHistories[targetUserId].push({
                            content: message.content,
                            senderUsername: message.senderUsername
                        });
                    }
                });
            });
        }
        document.getElementById("sendBtn").onclick = function () {
            const content = document.getElementById("messageInput").value;
            console.log("Gönderilen mesaj:", content);
            stompClients[targetUserId].send("/app/chat.sendMessage", {}, JSON.stringify({
                senderId: currentUserId,
                senderUsername: currentUsername,
                recipientId: targetUserId,
                content: content,
                timestamp: new Date().toISOString()
            }));
            document.getElementById("messageInput").value = '';
        };
    }

    function showMessage(content, senderUsername) {
        const messageArea = document.getElementById("chat-messages");
        const msg = document.createElement("div");
        let senderSpan = document.createElement("strong");
        senderSpan.textContent = senderUsername + ": ";
        let contentSpan = document.createElement("span");
        contentSpan.textContent = content;
        msg.appendChild(senderSpan);
        msg.appendChild(contentSpan);
        messageArea.appendChild(msg);
        console.log("Mesaj ekleniyor:", senderUsername, content);
    }
