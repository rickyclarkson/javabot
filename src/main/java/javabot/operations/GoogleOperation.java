package javabot.operations;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;
import java.io.UnsupportedEncodingException;

import javabot.BotEvent;
import javabot.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ricky_clarkson
 */
public class GoogleOperation implements BotOperation {
    private static final String PLAIN_GOOGLE = "http://www.google.com/search?q=";
    private static final Log log = LogFactory.getLog(GoogleOperation.class);

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String sender = event.getSender();
        String channel = event.getChannel();
        if(!message.startsWith("google ")) {
            return messages;
        }
        message = message.substring("google ".length());
        try {
            messages.add(new Message(channel, PLAIN_GOOGLE + URLEncoder.encode(message, Charset.defaultCharset().displayName()), false));
        } catch(UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
//        Googler googler = new Googler();
//        googler.search(message);
//        messages.add(new Message(event.getChannel(),
//            "I'll tell you the results in a private message, " + sender, false));
//        int numResults = googler.getNumberOfResults();
//        for(int a = 0; a < Math.min(numResults, 5); a++) {
//            messages.add(new Message(sender,
//                googler.getNextResult().toString(), false));
//        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}