package javabot.commands;

import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;

/**
 * Created Jan 26, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class ListOperations extends OperationsCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(ListOperations.class);

    @Override
    public void execute(final List<Message> responses, final Javabot bot, final BotEvent event) {
        responses.add(new Message(event.getChannel(), event, "I know of the following operations:"));
        responses.add(new Message(event.getChannel(), event, StringUtils.join(Javabot.OPERATIONS, ", ")));
        listCurrent(responses, bot, event);
        responses.add(new Message(event.getChannel(), event, "use admin enableOperation or disableOperation to turn"
            + " operations on or off"));
    }
}