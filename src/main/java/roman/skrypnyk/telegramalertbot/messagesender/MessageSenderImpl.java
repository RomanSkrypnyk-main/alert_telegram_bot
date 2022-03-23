package roman.skrypnyk.telegramalertbot.messagesender;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import roman.skrypnyk.telegramalertbot.MainBotClass;

@Service
public class MessageSenderImpl implements MessageSender {

    private MainBotClass mainBotClass;

    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            mainBotClass.execute(sendMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    @Autowired
    public void setMainBotClass(MainBotClass mainBotClass) {
        this.mainBotClass = mainBotClass;
    }


}
