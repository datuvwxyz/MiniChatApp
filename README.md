# Ứng dụng Chat bằng Java

Đây là một ứng dụng chat đơn giản được xây dựng bằng Java, hỗ trợ các phòng chat nơi nhiều client có thể kết nối và gửi tin nhắn. Server sẽ phát broadcast các tin nhắn đến tất cả client trong cùng một phòng, và các client có thể xem lịch sử tin nhắn khi tham gia phòng.

## Tính năng

### Phía Server

- **Chấp nhận các kết nối client đến**: Server sẽ lắng nghe các kết nối từ các client qua socket.
- **Quản lý nhiều phòng chat**: Server hỗ trợ nhiều phòng chat, mỗi phòng có thể chứa nhiều client.
- **Lưu trữ và lấy lại lịch sử tin nhắn cho mỗi phòng**: Lịch sử tin nhắn được lưu trữ và gửi lại cho các client khi họ tham gia phòng.
- **Phát broadcast các tin nhắn đến các client trong cùng một phòng**: Server sẽ phát broadcast tin nhắn cho tất cả client trong phòng mà client đang tham gia.
- **Xóa client khỏi phòng khi họ ngắt kết nối**: Khi một client ngắt kết nối, server sẽ xóa client khỏi phòng và thông báo cho các client khác.

### Phía Client

- **Tham gia phòng chat**: Client sẽ nhập tên và phòng chat khi kết nối với server.
- **Gửi tin nhắn**: Client có thể gửi tin nhắn tới server, và tin nhắn sẽ được broadcast đến các client khác trong cùng phòng.
- **Xem lịch sử tin nhắn**: Client sẽ nhận lại toàn bộ lịch sử tin nhắn khi tham gia phòng chat.
- **Hiển thị tin nhắn trong GUI**: Tin nhắn của client được căn phải và tin nhắn từ người khác được căn trái trong giao diện.

## Cấu hình môi trường
1. **Cài đặt VS Code và môi trường Java**
   
- Cài đặt VS Code: Tải từ Visual Studio Code.
- Cài đặt JDK (Java Development Kit):
- Tải JDK từ Oracle JDK hoặc OpenJDK.
- Đảm bảo cấu hình biến môi trường JAVA_HOME.
  
2. **Kiểm tra cài đặt:**
Mở terminal hoặc Command Prompt (Window) chạy:
    ```bash
    java -version
    javac -version
3. **Cài đặt Extension cho Java**
- Mở VS Code.
- Vào Extensions Marketplace (Ctrl+Shift+X) và tìm Java Extension Pack.
- Cài đặt gói Java Extension Pack (bao gồm các công cụ như Language Support for Java, Debugger for Java, và Maven).

4. **Tạo dự án Java trong VS Code**
  - ***Tạo một thư mục mới:***
     Ví dụ: ChatApp.<br>
  - ***Mở thư mục trong VS Code:***
     Chọn File > Open Folder.
  - ***Tạo cấu trúc thư mục:***
    Tạo file Server.java và Client.java trong thư mục src.
    Cấu trúc thư mục:
       ```bash
       ChatApp/
       └── src/
           ├── Server.java
           └── Client.java
    
## Cài đặt Server

1. **Biên dịch và chạy server:**
   ```bash
   javac ChatServer.java
   java ChatServer
Server sẽ chạy trên cổng 12345.

2. **Chức năng của Server:**
- Chấp nhận các kết nối client đến.
- Quản lý nhiều phòng chat.
- Lưu trữ và lấy lại lịch sử tin nhắn cho mỗi phòng.
- Phát broadcast các tin nhắn đến các client trong cùng một phòng.
- Xóa client khỏi phòng khi họ ngắt kết nối.

## Cài đặt Client

1. **Biên dịch và chạy client:**
   ```bash
   javac ChatClient.java
   java ChatClient
Khi chạy client, bạn sẽ được yêu cầu nhập tên của bạn và tên phòng.

2. **Chức năng của Client:**
- Cho phép client tham gia một phòng chat với tên đã cho.
- Gửi tin nhắn tới server, và server sẽ phát broadcast tin nhắn đến các client khác trong cùng phòng.
- Hiển thị lịch sử tin nhắn của phòng khi client kết nối.

## Các tính năng trong GUI của Client 
- Hiển thị Tin nhắn: Tin nhắn được hiển thị trong JTextPane, với tin nhắn của người dùng căn phải và tin nhắn từ người khác căn trái.
- Khu vực Nhập Tin nhắn: Client có một trường nhập và nút "Gửi" để gửi tin nhắn.
- Nút Mở Client Mới: Người dùng có thể tạo client mới với tên và phòng khác ngay trong giao diện client.

## Kiến Trúc
**Server:**
- Sử dụng ServerSocket để lắng nghe các kết nối client trên cổng 12345.
- Xử lý mỗi client trong một Thread riêng biệt (ClientHandler).
- Sử dụng các phương thức đồng bộ để xử lý việc client tham gia và rời phòng, và phát broadcast tin nhắn.
- Lưu trữ lịch sử tin nhắn cho mỗi phòng.
- 
**Client:**
- Sử dụng Socket để kết nối với server.
- Gửi và nhận tin nhắn từ server.
- Hiển thị tin nhắn trong giao diện người dùng xây dựng bằng JFrame, JTextPane và JPanel.
- Sử dụng PrintWriter để gửi tin nhắn và BufferedReader để nhận tin nhắn.

## Thông Tin Bổ Sung
- Ứng dụng được xây dựng bằng Java 8 hoặc phiên bản mới hơn.
- Đảm bảo bạn đã cài đặt javac và java trong môi trường của bạn.

## Giấy phép
Dự án này được cấp phép theo Giấy phép MIT - xem chi tiết tại tệp LICENSE.
  





   
