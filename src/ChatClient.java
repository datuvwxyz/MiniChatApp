import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ChatClient {
    private String clientName;
    private String roomName;
    private PrintWriter out;
    private JTextPane chatArea;  // Sử dụng JTextPane thay vì JTextArea
    private StyledDocument doc;

    public ChatClient(String clientName, String roomName) {
        this.clientName = clientName;
        this.roomName = roomName;
        createAndShowGUI();
        connectToServer();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame(clientName + " - Chat Client (" + roomName + ")");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Chat area
        chatArea = new JTextPane();  // Sử dụng JTextPane
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Input area
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        JTextField inputField = new JTextField();
        JButton sendButton = new JButton("Gửi");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Add input area to panel
        panel.add(inputPanel, BorderLayout.SOUTH);

        // Button to open new client
        JButton newClientButton = new JButton("Mở Client Mới");
        newClientButton.setFont(new Font("Arial", Font.BOLD, 14));
        newClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newClientName = JOptionPane.showInputDialog("Nhập tên cho client mới:");
                String newRoomName = JOptionPane.showInputDialog("Nhập tên phòng mới:");
                if (newClientName != null && !newClientName.trim().isEmpty() && newRoomName != null
                        && !newRoomName.trim().isEmpty()) {
                    new ChatClient(newClientName, newRoomName);
                }
            }
        });

        panel.add(newClientButton, BorderLayout.NORTH);

        // Add panel to frame
        frame.add(panel);
        frame.setVisible(true);

        // Get the document of JTextPane
        doc = chatArea.getStyledDocument();

        // Add action listener for sending messages
        sendButton.addActionListener(e -> {
            String message = inputField.getText();
            if (!message.isEmpty()) {
                out.println(message);
                inputField.setText("");
            }
        });
    }

    private void connectToServer() {
        try {
            @SuppressWarnings("resource")
            Socket socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Gửi thông tin client và phòng tới server
            out.println(clientName + "##" + roomName);

            // Nhận tin nhắn từ server
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        if (message.startsWith(clientName + ":")) {
                            // Tin nhắn của chính mình
                            appendMessage(message, true);
                        } else {
                            // Tin nhắn từ người khác
                            appendMessage(message, false);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendMessage(String message, boolean isSelf) {
        try {
            // Tạo kiểu định dạng cho tin nhắn
            SimpleAttributeSet attributes = new SimpleAttributeSet();
            if (isSelf) {
                // Căn phải cho tin nhắn của client
                StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_RIGHT);
            } else {
                // Căn trái cho tin nhắn từ người khác
                StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_LEFT);
            }
    
            // Thêm tin nhắn vào JTextPane với định dạng căn trái/phải
            doc.insertString(doc.getLength(), message + "\n", attributes);  // Thêm \n để xuống dòng
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String clientName = JOptionPane.showInputDialog("Nhập tên của bạn:");
            String roomName = JOptionPane.showInputDialog("Nhập tên phòng:");
            if (clientName != null && !clientName.trim().isEmpty() && roomName != null && !roomName.trim().isEmpty()) {
                new ChatClient(clientName, roomName);
            }
        });
    }
}
