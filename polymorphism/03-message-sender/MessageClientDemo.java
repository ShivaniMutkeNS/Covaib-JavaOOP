public class MessageClientDemo {
    public static void main(String[] args) {
        MessageSender[] senders = new MessageSender[] {
            new EmailSender(), new SMSSender(), new PushNotificationSender()
        };
        for (MessageSender sender : senders) {
            sender.send("user@example.com", "Hello", "This is a test message");
        }
    }
}


