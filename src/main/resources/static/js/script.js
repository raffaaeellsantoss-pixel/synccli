 const params = new URLSearchParams(window.location.search);
    const urlRoomId = params.get("roomId");

    let socket;
    let roomId;

    const isMobile = window.innerWidth <= 768;

    // se veio do QR → conecta automático
    if (urlRoomId) {
        roomId = urlRoomId;
        connect(roomId);
    }
    // gerar QR no desktop
    if (!isMobile) {
        roomId = Math.random().toString(36).substring(2, 8);
        const url = `${window.location.origin}?roomId=${roomId}`;

        QRCode.toCanvas(document.createElement('canvas'), url, function (err, canvas) {
            document.getElementById("qrcode").appendChild(canvas);
        });

        document.getElementById("roomCode").textContent = "Código: " + roomId;

        connect(roomId);
    }

    // conectar manual (mobile)
    function connectRoom() {
        roomId = document.getElementById("roomInput").value;
        if (!roomId) return alert("Digite um código");

        connect(roomId);
    }

   function connect(id) {

    if (socket && socket.readyState === WebSocket.OPEN) {
        return; // já conectado
    }

    socket = new WebSocket(`wss://synccli-production.up.railway.app/raw?roomId=${id}`);

        socket.onopen = () => {
            statusEl.textContent = "Conectado ✅";
        };

        socket.onclose = () => {
            statusEl.textContent = "Desconectado ❌";
        };

        socket.onerror = () => {
            statusEl.textContent = "Erro na conexão ⚠️";
        };

        socket.onmessage = async (event) => {
            await navigator.clipboard.writeText(event.data);

            statusEl.textContent = "Texto copiado! Agora é só colar (Ctrl+V) ✅";
        };
    }

    function sendMessage() {
    const input = document.getElementById("message");
    const statusEl = document.getElementById("status");
    const text = input.value;

    if (!text) {
        statusEl.textContent = "Cole algo antes 😉";
        return;
    }

    if (!socket || socket.readyState !== WebSocket.OPEN) {
        statusEl.textContent = "Conexão perdida ❌";
        return;
    }

   try {
           // 🔒 trava o botão
           button.disabled = true;
           button.textContent = "Enviando...";

           socket.send(text);

           statusEl.textContent = "Enviado 🚀";
           input.value = "";

           // 🔓 libera depois de 500ms
           setTimeout(() => {
               button.disabled = false;
               button.textContent = "Enviar";
           }, 500);

       }  catch (e) {
        statusEl.textContent = "Erro ao enviar ⚠️";
    }
}

const pcStatus = document.getElementById("pcStatus");

socket.onopen = () => {
    pcStatus.textContent = "Conectado ✅";
};

socket.onclose = () => {
    pcStatus.textContent = "Desconectado ❌";
};

socket.onmessage = async (event) => {
    await navigator.clipboard.writeText(event.data);

    pcStatus.textContent = "Texto copiado! Pode colar (Ctrl+V) ✅";
};

    // popup ajuda
    function toggleHelp() {
        const p = document.getElementById("popup");
        p.style.display = p.style.display === "block" ? "none" : "block";
    }