import java.rmi.RemoteException;
import java.util.*;

public class TestSequencer {
    static Stack<Long> seguences;
    static Sequencer<Message[]> sequencer;
    static GUI gui;

    public static void main(String[] args) {
        seguences = new Stack<>();
        Date date = new Date();

        //static multicast IPAddress
        String multicastAddress = "234.20.7.1";

        // Getting input from the user
        String sender = null;
        Scanner input = new Scanner(System.in);
        System.out.print("Enter your name: ");
        sender = input.nextLine();
        String finalSender = sender;

        GUI.GUIHandler guiHandler = new GUI.GUIHandler() {
            @Override
            public void getTextInput(String message) {
                send(date, message, sequencer, finalSender, false);
            }

            @Override
            public void stressTest() {
                for (int i = 0; i <= 30; i++) {
                    send(date, "Stress testing network: " + i, sequencer, finalSender, true);
                }
            }

            @Override
            public void showMessageHistory() {
                String[] messages = Arrays.stream(sequencer.getMessageHistory())
                        .map(message -> "History: " + message.getSender() + ": " + new String(message.getMsg()))
                        .toArray(String[]::new);

                gui.showHistory(messages);
            }
        };


        Group.MsgHandler handler = (count, msg) -> {
            try {
                Message messageFrom = Message.fromByteStream(msg);
                String message = new String(messageFrom.getMsg());

                if (messageFrom.getMsgID() != -1) {
                    gui.queueMessage("Message from " + messageFrom.getSender() + ": " + message);
                } else {
                    System.out.println("Pinging: " + messageFrom.getSender() + ": " + message);
                }

                if (messageFrom.getMsgID() != -1) {
                    seguences.push(messageFrom.getLastSequence());
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        };

        Group.HeartBeater.HeartBeaterHandler heartBeaterHandler = (int i) -> {
            send(date, "Testing network: " + i, sequencer, finalSender, true);
        };

        gui = new GUI(guiHandler);
        sequencer = new SequencerImpl(multicastAddress, handler, heartBeaterHandler, sender);

        try {
            sequencer.join(sender);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static void send(Date date, String message, Sequencer<Message[]> sequencer, String sender, boolean pinging) {

        try {

            String message_id = "15786" + date.getTime();

            if (pinging) {
                message_id = "-1";
            }

            if (message.toLowerCase().trim().equals("exit")) {
                if (gui != null) {
                    gui.close();
                }
                sequencer.leave(sender);
            } else if (!message.toLowerCase().trim().isEmpty()) {
                long lastSequence = 0;
                if (!seguences.empty()) lastSequence = seguences.peek();
                sequencer.send(sender, message.trim().getBytes(), Long.parseLong(message_id), lastSequence);
                seguences.push(lastSequence + 1);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}
