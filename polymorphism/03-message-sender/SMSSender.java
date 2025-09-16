public class SMSSender implements MessageSender {
    @Override
    public void send(String to, String subject, String body) {
        System.out.println("[SMS] to=" + to + ", body=" + body);
    }
}


