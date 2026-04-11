const params = new URLSearchParams(window.location.search);
const urlRoomId = params.get("roomId");

let socket;
let roomId;

const isMobile = window.innerWidth <= 768;

const statusEl = document.getElementById("status");
const pcStatus = document.getElementById("pcStatus");

// 📱 MOBILE vindo do QR
if (isMobile && urlRoomId) {
    roomId = urlRoomId;
    connect(roomId);
}

// 🖥️ DESKTOP gera sala
if (!isMobile) {
    roomId = Math.random().toString(36).substring(2, 8);

    const url = `${window.location.origin}?roomId=${roomId}`;

    QRCode.toCanvas(document.createElement('canvas'), url, function (err, canvas) {
        document.getElementById("qrcode").appendChild(canvas);
    });

    document.getElementById("roomCode").textContent = "Código: " + roomId;

    connect(roomId);
}

// 📱 conectar manual
function connectRoom() {
    const input = document.getElementById("roomInput");

    if (!input.value) {
        statusEl.textContent = "Digite um código";
        return;
    }

    roomId = input.value;

    connect(roomId);

    // 🔒 trava input + botão
    input.disabled = true;
    document.querySelector(".mobile button").disabled = true;
}

let connected = false;
// 🔌 conexão websocket
function connect(id) {

    if (socket && socket.readyState === WebSocket.OPEN) return;

    socket = new WebSocket(`wss://synccli-production.up.railway.app/raw?roomId=${id}`);

       socket.onopen = () => {
           connected = true;

           if (isMobile) {
               statusEl.textContent = "Conectado ✅";
           } else {
               pcStatus.textContent = "Aguardando envio...";
           }
       };

       // 👇 valida conexão após 2s
       setTimeout(() => {
           if (!connected && isMobile) {
               statusEl.textContent = "Código inválido ❌";
           }
       }, 2000);

    socket.onerror = () => {
        if (isMobile) {
            statusEl.textContent = "Erro ⚠️";
        }
    };

    socket.onmessage = async (event) => {

        if (!isMobile) {
            try {
                await navigator.clipboard.writeText(event.data);
                pcStatus.textContent = "Copiado! Ctrl+V ✅";
            } catch (e) {
                pcStatus.textContent = "Clique para copiar ⚠️";
                showCopyButton(event.data);
            }
        } else {
            statusEl.textContent = "Enviado e recebido no PC ✅";
        }
    };
}

// 📤 envio
function sendMessage() {
    const input = document.getElementById("message");
    const text = input.value;

    if (!text) {
        statusEl.textContent = "Cole algo antes 😉";
        return;
    }

    if (!socket || socket.readyState !== WebSocket.OPEN) {
        statusEl.textContent = "Sem conexão ❌";
        return;
    }

    socket.send(text);

    statusEl.textContent = "Enviado 🚀";
    input.value = "";
}

// popup
function toggleHelp() {
    const p = document.getElementById("popup");
    p.style.display = p.style.display === "block" ? "none" : "block";
}