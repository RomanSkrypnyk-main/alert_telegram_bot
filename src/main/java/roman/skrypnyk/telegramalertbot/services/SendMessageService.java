package roman.skrypnyk.telegramalertbot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import roman.skrypnyk.telegramalertbot.messagesender.MessageSender;

import java.util.ArrayList;

@Service
public class SendMessageService {

    private final MessageSender messageSender;

    public SendMessageService(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void hello(Message message){
        SendMessage sendMessage = SendMessage.builder().text("<b>" +  "Бот увімкнений" +  "</b>")
                .parseMode("HTML")
                .chatId(String.valueOf(message.getChatId()))
                .build();
        messageSender.sendMessage(sendMessage);
    }

    public void KeyboadrAdmin(Message message, String date){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<KeyboardRow>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add("Тривога");
        row3.add("Відбій");
        keyboardRows.add(row1);
        keyboardRows.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        //SendMessage sendMessage = new SendMessage();
        SendMessage sendMessage = SendMessage.builder().text("<b>" + "натиснуто: " + message.getText() + "\n" + date + "</b>")
                .parseMode("HTML")
                .chatId(String.valueOf(message.getChatId()))
                .build();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        messageSender.sendMessage(sendMessage);
    }
}
