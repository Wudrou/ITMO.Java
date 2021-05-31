import Weather.Weather;
import Weather.Model;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args){
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();

        keyboardRow1.add(new KeyboardButton("/subscribe"));
        keyboardRow1.add(new KeyboardButton("/unsubscribe"));

        keyboardRowList.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public String getBotUsername() {
        return "WeatherFromDK";
    }

    @Override
    public String getBotToken() {
        return "1786858458:AAFVVfKKqANfelO1kLgL7ArDHwATRAYHGW4";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Model model = new Model();
        if(message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    sendMsg(message, "Добро пожаловть, я погодный Telegram-бот. Введи название города, а я скажу погоду");
                    break;
                case "Привет":
                    sendMsg(message, "Привет, введи название города, а я скажу погоду");
                    break;
                case "Пока":
                    sendMsg(message, "Пока");
                    break;
                case "/help":
                    sendMsg(message, "Чем помочь?");
                    break;
                case "/setting":
                    sendMsg(message, "Что настроим?");
                    break;
                case "/subscribe":
                    sendMsg(message, "Вы подписались на ежедневную рассылку");

                    break;
                case "/unsubscribe":
                    sendMsg(message, "Жаль, больше не буду отправлять тебе прогноз");

                    break;
                default:
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                        //sendMsg(message, Weather.getForecastWeather(model));
                    } catch (IOException e) {
                        sendMsg(message, "Я не знаю такого города, попробуй еще раз");
                    }
            }
        }
    }
}
