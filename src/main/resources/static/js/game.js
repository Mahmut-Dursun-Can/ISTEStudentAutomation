const users = [];
let currentUserIndex = 0;
let intervalId;
let currentDrawer = '';
let stompClient;
let randword ='';
const canvas = document.getElementById('drawingCanvas');
const ctx = canvas.getContext('2d');
let drawing = false;


document.getElementById('joinButton').addEventListener('click', () => {
  if (!currentUsername) {
    alert('Kullanıcı adı yok!');
    return;
  }
  initWebSocket(currentUsername);
  users.push(currentUsername);
  document.getElementById('joinButton').style.display = 'none';

  ['drawingCanvas', 'guessInput', 'guessButton', 'resultMessage']
    .forEach(id => document.getElementById(id).style.display = 'block');
  startGame();
});

function startGame() {
  if (intervalId) clearInterval(intervalId);

  currentUserIndex = 0;
  currentDrawer = users[currentUserIndex];
  showCurrentUser();

  sendCurrentDrawer(currentDrawer);

}

function showCurrentUser() {
  console.log(`Sıradaki kullanıcı: ${currentDrawer}`);
}

document.getElementById('guessButton').addEventListener('click', () => {
  const guess = document.getElementById('guessInput').value.trim().toLowerCase();
  const messageElement = document.getElementById('resultMessage');
  if (!randword) {
    messageElement.innerText = '❌ Kelime henüz alınmadı!';
    messageElement.style.color = 'red';
    return;
  }

  if (guess === randword) {
    messageElement.innerText = '🎁 Tahmin doğru.';
    messageElement.style.color = 'green';
    console.log('✅ Success! Tahmin doğru.');
  } else {
    messageElement.innerText = `Yanlış tahmin.`;
    messageElement.style.color = 'red';
    console.log('❌ Yanlış tahmin.');
  }
    stompClient.send("/app/guess", {}, JSON.stringify({
      username: currentUsername,
      guess: guess
    }));
});

// WebSocket and canvas
function initWebSocket(userName) {
  const socket = new SockJS('/ws');
  stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    // Kullanıcı join mesajı
    stompClient.send('/app/join', {}, JSON.stringify({ username: userName }));

    // Kelimeyi sadece çizici kullanıcıya gönderilen özel kanal
    stompClient.subscribe('/topic/word', (message) => {
      const data = JSON.parse(message.body);
      randword = data.word;
      console.log(message.body);
      console.log(randword);
      if (currentDrawer === userName) {
        document.getElementById('randomWord').style.display ='block';
        document.getElementById('randomWord').innerText = `${randword}`;
        document.getElementById('guessInput').style.display = 'none';
        document.getElementById('guessButton').style.display = 'none';
      }
      else{
              document.getElementById('randomWord').style.display ='none';
      }
    });

    // Çizim hareketlerini dinle
    stompClient.subscribe('/topic/draw', (message) => {
      const data = JSON.parse(message.body);
      if (!ctx) return;
      if (data.type === 'start') {
        ctx.beginPath();
        ctx.moveTo(data.x, data.y);
      } else if (data.type === 'draw') {
        ctx.lineTo(data.x, data.y);
        ctx.stroke();
        ctx.beginPath();
        ctx.moveTo(data.x, data.y);
      }
    });

    // Yeni çizici bilgisi dinle
    stompClient.subscribe('/topic/currentDrawer', (message) => {
      const data = JSON.parse(message.body);
      handleDrawerChange(data.currentDrawer, userName);
    });
  });


  function canDraw() {
    return userName === currentDrawer;
  }

  canvas.addEventListener('mousedown', e => {
    if (!canDraw()) return;
    drawing = true;
    const x = e.offsetX;
    const y = e.offsetY;
    ctx.beginPath();
    ctx.moveTo(x, y);
    sendStartDraw(x, y);
  });

  canvas.addEventListener('mousemove', e => {
    if (!drawing || !canDraw()) return;
    const x = e.offsetX;
    const y = e.offsetY;
    ctx.lineTo(x, y);
    ctx.stroke();
    ctx.beginPath();
    ctx.moveTo(x, y);
    sendDraw(x, y);
  });

  canvas.addEventListener('mouseup', () => {
    drawing = false;
    ctx.beginPath();
  });

  canvas.addEventListener('mouseout', () => {
    drawing = false;
    ctx.beginPath();
  });

// TOUCH EVENTLER
canvas.addEventListener('touchstart', (e) => {
  if (!canDraw()) return;
  e.preventDefault(); // Kaydırmayı engelle
  const touch = e.touches[0];
  const rect = canvas.getBoundingClientRect();
  const x = touch.clientX - rect.left;
  const y = touch.clientY - rect.top;
  drawing = true;
  ctx.beginPath();
  ctx.moveTo(x, y);
  sendStartDraw(x, y);
});

canvas.addEventListener('touchmove', (e) => {
  if (!drawing || !canDraw()) return;
  e.preventDefault();
  const touch = e.touches[0];
  const rect = canvas.getBoundingClientRect();
  const x = touch.clientX - rect.left;
  const y = touch.clientY - rect.top;
  ctx.lineTo(x, y);
  ctx.stroke();
  ctx.beginPath();
  ctx.moveTo(x, y);
  sendDraw(x, y);
});

canvas.addEventListener('touchend', () => {
  drawing = false;
  ctx.beginPath();
});



  function sendStartDraw(x, y) {
    stompClient.send('/app/draw', {}, JSON.stringify({ type: 'start', x, y ,username: currentUsername }));
  }

  function sendDraw(x, y) {
    stompClient.send('/app/draw', {}, JSON.stringify({ type: 'draw', x, y ,username: currentUsername}));
  }

  function handleDrawerChange(newDrawer, localUser) {
    currentDrawer = newDrawer;
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    const statusMessage = document.getElementById('statusMessage');
        if (!statusMessage) {
            console.error('statusMessage bulunamadı');
            return;
        }
    statusMessage.style.display = 'block';

    if (localUser === currentDrawer) {
      statusMessage.textContent = '🎨 Sıra sende, çizim yapabilirsin!';
      document.getElementById('guessInput').style.display = 'none';
      document.getElementById('guessButton').style.display = 'none';

    } else {
      statusMessage.textContent = `🧩 ${newDrawer} çiziyor, sen tahmin etmelisin.`;
      document.getElementById('guessInput').style.display = 'block';
      document.getElementById('guessButton').style.display = 'block';
    }
  }
}

function sendCurrentDrawer(currentDrawer) {
  if (stompClient && stompClient.connected) {
    stompClient.send('/app/current-drawer', {}, JSON.stringify({ currentDrawer }));
  }
}

