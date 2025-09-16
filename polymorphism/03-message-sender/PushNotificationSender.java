public class PushNotificationSender implements MessageSender {
    @Override
    public void send(String to, String subject, String body) {
        System.out.println("[PUSH] to=" + to + ", title=" + subject + ", body=" + body);
    }
}


