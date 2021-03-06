package javabot.operations;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

public class DaysToChristmasOperation extends BotOperation {
    public DaysToChristmasOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public List<Message> handleMessage(final BotEvent event) {
        final List<Message> responses = new ArrayList<Message>();
        if ("countdown to christmas".equals(event.getMessage().toLowerCase())) {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            calendar.set(Calendar.DAY_OF_MONTH, 25);
            long millis = calendar.getTimeInMillis() - new Date().getTime() / 86400000;
            responses.add(new Message(event.getChannel(), event, "There are " + millis + " days until Christmas."));
        }
        return responses;
    }
}
