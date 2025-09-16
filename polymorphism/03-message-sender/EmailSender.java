public class EmailSender implements MessageSender {
    @Override
    public void send(String to, String subject, String body) {
        System.out.println("[EMAIL] to=" + to + ", subject=" + subject + ", body=" + body);
    }
}


