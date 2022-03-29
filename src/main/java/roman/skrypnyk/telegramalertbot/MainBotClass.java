package roman.skrypnyk.telegramalertbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import roman.skrypnyk.telegramalertbot.emoji.Emoji;
import roman.skrypnyk.telegramalertbot.entity.Entity;
import roman.skrypnyk.telegramalertbot.entity.EntityRepository;
import roman.skrypnyk.telegramalertbot.services.SendMessageService;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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

    @Autowired
    private EntityRepository entityRepository;

    @Value("${telegram.bot.adminId}")
    private Long adminId;

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
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+3"));
        String formattedDate = sdf.format(date);

        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                Long userId = update.getMessage().getFrom().getId();

                if (message.getText().equals("/start")) {
                    Entity entity = new Entity(userId);
                    entityRepository.save(entity);
                    SendMessage sendMessage = SendMessage.builder().text(
                                    Emoji.LEFT_RIGHT_ARROW +
                                            "<b>" + " Бот увімкнений" + "</b>"
                                            + "\n"
                                            + "\n" + formattedDate
                                            + "\n" + "! БОТ ПРАЦЮЄ В ТЕСТОВОМУ РЕЖИМІ ! перевіряйте дану інформацію !")
                            .parseMode("HTML")
                            .chatId(String.valueOf(message.getChatId()))
                            .build();
                    sendMessage.setChatId(String.valueOf(message.getChatId()));
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if (message.getText().equals("/help")) {
                    SendMessage sendMessage = SendMessage.builder().text(
                                    Emoji.LEFT_RIGHT_ARROW +
                                            "<b>" + " Бот телеграм Тривога ЖШ " + "</b>"
                                            + "\n"
                                            + "\n" + "1. адміністратори відправляють повідомлення про тривогу / відбій."
                                            + "\n" + "2. користувачу приходить повідомленя про тривогу."
                                            + "\n" + "3. користувачу приходить повідомлення про відбій."
                                            + "\n" + "4. бот не приймає жодних повідомлень; режим сповіщення!"
                                            + "\n" + "5. БОТ ПРАЦЮЄ В ТЕСТОВОМУ РЕЖИМІ ! перевіряйте дану інформацію !")
                            .parseMode("HTML")
                            .chatId(String.valueOf(message.getChatId()))
                            .build();
                    sendMessage.setChatId(String.valueOf(message.getChatId()));
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                //ADMIN
                if (userId.equals(adminId)) {
                    if (message.getText().equals("Тривога") || message.getText().equals("Відміна") || message.getText().equals("/start")) {
                        sendMessageService.KeyboadrAdmin(message, formattedDate);
                        //MESSAGE TO USERS
                        //ALERT
                        List<Long> ls = entityRepository.findAllUserIds();
                        if (message.getText().equals("Тривога")) {
                            for (Long i : ls) {
                                SendMessage sendMessage = SendMessage.builder().text(
                                                "<b>" + "Тривога " + "</b>" + Emoji.HEAVY_EXCLAMATION_MARK_SYMBOL
                                                        + Emoji.HEAVY_EXCLAMATION_MARK_SYMBOL
                                                        + Emoji.HEAVY_EXCLAMATION_MARK_SYMBOL
                                                        + Emoji.ALARM_CLOCK
                                                        + "\n" + formattedDate
                                                        + "\n" + Emoji.INFORMATION_SOURCE + " прямуйте до укриттів")
                                        .parseMode("HTML")
                                        .chatId(String.valueOf(message.getChatId()))
                                        .build();
                                sendMessage.setChatId(String.valueOf(i));
                                try {
                                    execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        //CANCEL
                        if (message.getText().equals("Відміна")) {
                            for (Long k : ls) {
                                SendMessage sendMessage = SendMessage.builder().text(
                                                "<b>" + "Відбій. " + "</b>" + Emoji.WHITE_EXCLAMATION_MARK_ORNAMENT
                                                        + "\n" + formattedDate
                                                        + "\n" + Emoji.INFORMATION_SOURCE + " можна залишати укриття")
                                        .parseMode("HTML")
                                        .chatId(String.valueOf(message.getChatId()))
                                        .build();
                                sendMessage.setChatId(String.valueOf(k));
                                try {
                                    execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
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
                } else {
                    try {
                        DeleteMessage deleteMessage = new DeleteMessage();
                        deleteMessage.setChatId(String.valueOf(message.getChatId()));
                        deleteMessage.setMessageId(message.getMessageId());
                        execute(deleteMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}



