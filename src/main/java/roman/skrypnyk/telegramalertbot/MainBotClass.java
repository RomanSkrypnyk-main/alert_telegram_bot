package roman.skrypnyk.telegramalertbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.MemberStatus;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import roman.skrypnyk.telegramalertbot.emoji.Emoji;
import roman.skrypnyk.telegramalertbot.services.SendMessageService;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Component
public class MainBotClass extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String username;

    @Value("${telegram.bot.token}")
    private String token;

    private SendMessageService sendMessageService;

    @Autowired
    public void setSendMessageService(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Value("${telegram.bot.adminId}")
    private Set<Long> adminId;

    @Value("${telegram.bot.userId}")
    private Set<Long> userIdList;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long timeUnix = Long.valueOf(update.getMessage().getDate());
        // convert seconds to milliseconds
        Date date = new java.util.Date(timeUnix * 1000L);
        // the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        // give a timezone reference for formatting
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+2"));
        String formattedDate = sdf.format(date);

        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                Long userId = update.getMessage().getFrom().getId();
                if (message.getText().equals("/start")) {
                    userIdList.add(userId);
                    sendMessageService.hello(message);
                }
                //ADMIN
                if (adminId.contains(userId)) {
                    if (message.getText().equals("Тривога") || message.getText().equals("Відбій") || message.getText().equals("/start")) {
                        sendMessageService.KeyboadrAdmin(message, formattedDate);

                        //MESSAGE TO USERS
                        //ALERT
                        if (message.getText().equals("Тривога")) {

                            for (Long i : userIdList) {
                                for (int j = 0; j < 10; j++) {
                                    SendMessage sendMessage = SendMessage.builder().text(
                                                    "<b>" + "Тривога " + "</b>" + Emoji.HEAVY_EXCLAMATION_MARK_SYMBOL + Emoji.ALARM_CLOCK
                                                            + "\n" + formattedDate
                                                            + "\n" + Emoji.INFORMATION_SOURCE + " прямуйте до укриттів")
                                            .parseMode("HTML")
                                            .chatId(String.valueOf(message.getChatId()))
                                            .build();
                                    sendMessage.setChatId(String.valueOf(i));
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    try {
                                        execute(sendMessage);
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        //CANCEL
                        if (message.getText().equals("Відбій")) {
                            for (Long i : userIdList) {
                                for (int k = 1; k < 4; k++) {
                                    SendMessage sendMessage = SendMessage.builder().text(
                                                    "<b>" + "Відбій: " + "</b>" + k + ". " + Emoji.WHITE_EXCLAMATION_MARK_ORNAMENT
                                                            + "\n" + formattedDate
                                                            + "\n" + Emoji.INFORMATION_SOURCE + " можна залишати укриття")
                                            .parseMode("HTML")
                                            .chatId(String.valueOf(message.getChatId()))
                                            .build();
                                    sendMessage.setChatId(String.valueOf(i));
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    try {
                                        execute(sendMessage);
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } else if (!Objects.equals(message.getText(), "/start")) {
                        try {
                            DeleteMessage deleteMessage = new DeleteMessage();
                            deleteMessage.setChatId(String.valueOf(message.getChatId()));
                            deleteMessage.setMessageId(message.getMessageId());
                            execute(deleteMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                } //else {
                   // try {
                    //    DeleteMessage deleteMessage = new DeleteMessage();
                    //    deleteMessage.setChatId(String.valueOf(message.getChatId()));
                   //     deleteMessage.setMessageId(message.getMessageId());
                   //     execute(deleteMessage);
                   // } catch (TelegramApiException e) {
                   //     e.printStackTrace();
                    }
                //}
            }
        }
    }
}

