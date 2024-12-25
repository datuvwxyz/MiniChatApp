import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Map<String, List<ClientHandler>> rooms = new HashMap<>();
    private static Map<String, List<String>> messageHistory = new HashMap<>(); // Lưu trữ lịch sử tin nhắn

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Chat server is running on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void addClientToRoom(String roomName, ClientHandler clientHandler) {
        rooms.computeIfAbsent(roomName, k -> new ArrayList<>()).add(clientHandler);
    }

    private static synchronized void removeClientFromRoom(String roomName, ClientHandler clientHandler) {
        List<ClientHandler> clients = rooms.get(roomName);
        if (clients != null) {
            clients.remove(clientHandler);
            if (clients.isEmpty()) {
                rooms.remove(roomName);
            }
        }
    }

    private static synchronized void broadcastToRoom(String roomName, String message) {
        List<ClientHandler> clients = rooms.get(roomName);
        if (clients != null) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
    }

    // Lưu tin nhắn vào lịch sử
    private static synchronized void addMessageToHistory(String roomName, String message) {
        messageHistory.computeIfAbsent(roomName, k -> new ArrayList<>()).add(message);
    }

    // ClientHandler xử lý từng client
    static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientName;
        private String roomName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Nhận tên client và tên phòng
                String initialInput = in.readLine();
                String[] parts = initialInput.split("##");
                if (parts.length != 2) {
                    out.println("Sai định dạng. Hãy gửi 'TênClient##TênPhòng'.");
                    return;
                }
                clientName = parts[0];
                roomName = parts[1];

                // Thêm client vào phòng chat
                addClientToRoom(roomName, this);
                broadcastToRoom(roomName, clientName + " đã tham gia phòng.");

                System.out.println(clientName + " đã kết nối vào phòng " + roomName);

                // Gửi lại lịch sử tin nhắn cho client mới
                List<String> history = messageHistory.get(roomName);
                if (history != null) {
                    for (String message : history) {
                        sendMessage(message); // Gửi lại lịch sử tin nhắn
                    }
                }

                // Nhận và xử lý tin nhắn
                String message;
                while ((message = in.readLine()) != null) {
                    // Kiểm tra tin nhắn rỗng
                    if (message.trim().isEmpty()) {
                        continue;
                    }

                    String formattedMessage = clientName + ": " + message;
                    System.out.println("[" + roomName + "] " + formattedMessage);
                    addMessageToHistory(roomName, formattedMessage); // Lưu tin nhắn vào lịch sử
                    broadcastToRoom(roomName, formattedMessage); // Phát tin nhắn cho các client trong phòng
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (clientName != null && roomName != null) {
                    removeClientFromRoom(roomName, this);
                    broadcastToRoom(roomName, "Client " + clientName + " đã rời phòng.");
                    System.out.println("Client " + clientName + " đã rời phòng " + roomName);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }
}
