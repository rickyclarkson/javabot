package javabot.operations;

import java.io.IOException;
import java.util.Arrays;

import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class GetFactoidOperationTest extends BaseOperationTest {
    private static final String REPLY_VALUE = "I'm a reply!";
    @Autowired
    private FactoidDao factoidDao;

    @BeforeClass
    public void createGets() {
        deleteFactoids();
        factoidDao.addFactoid(getTestBot().getNick(), "api", "http://java.sun.com/javase/current/docs/api/index.html");
        factoidDao.addFactoid(getTestBot().getNick(), "replyTest", "<reply>I'm a reply!");
        factoidDao.addFactoid(getTestBot().getNick(), "seeTest", "<see>replyTest");
        factoidDao.addFactoid(getTestBot().getNick(), "noReply", "I'm a reply!");
        factoidDao.addFactoid(getTestBot().getNick(), "replace $1", "<reply>I replaced you $1");
        factoidDao.addFactoid(getTestBot().getNick(), "camel $^", "<reply>$^");
        factoidDao.addFactoid(getTestBot().getNick(), "url $+", "<reply>$+");
        factoidDao.addFactoid(getTestBot().getNick(), "hey", "<reply>Hello, $who");
        factoidDao.addFactoid(getTestBot().getNick(), "coin", "<reply>(heads|tails)");
        factoidDao.addFactoid(getTestBot().getNick(), "hug $1", "<action> hugs $1");
    }

    @AfterClass
    public void deleteFactoids() {
        delete("api");
        delete("replyTest");
        delete("seeTest");
        delete("noReply");
        delete("replace $1");
        delete("url $+");
        delete("camel $C");
        delete("hey");
        delete("coin");
        delete("hug $1");
    }

    private void delete(final String key) {
        while (factoidDao.hasFactoid(key)) {
            factoidDao.delete(getTestBot().getNick(), key);
        }
    }

    public void straightGets() throws IOException {
        testMessage("api", getFoundMessage("api", "http://java.sun.com/javase/current/docs/api/index.html"));
        Assert.assertNotNull(factoidDao.getFactoid("api").getLastUsed());
    }

    public void replyGets() {
        testMessage("replyTest", REPLY_VALUE);
    }

    public void seeGets() {
        testMessage("seeTest", REPLY_VALUE);
    }

    public void seeReplyGets() {
        testMessage("seeTest", REPLY_VALUE);
    }

    public void parameterReplacement() {
        testMessage("replace " + getTestBot().getNick(), "I replaced you " + getTestBot().getNick());
        testMessage("url what up doc", "what+up+doc");
        testMessage("camel i should be camel case", "IShouldBeCamelCase");
    }

    public void whoReplacement() {
        testMessage("hey", "Hello, " + getTestBot().getNick());
    }

    public void randomList() {
        testMessageList("coin", Arrays.asList("heads", "tails"));
    }

    @Test(enabled = false)
    public void guessFactoid() {
        testMessage("bre", "I guess the factoid 'label line breaks' might be appropriate:");
    }

    public void noGuess() {
        testMessage("apiz", getTestBot().getNick() + ", I have no idea what apiz is.");
    }

    public void action() {
        testMessage("hug " + getTestBot().getNick(), " hugs " + getTestBot().getNick());
    }

    @Test
    public void channelMessage() throws IOException {
        testChannelMessage("pong is");
    }

    @Test
    public void tell() {
        testMessage("tell Javabot-Testing about hey", "Hello, Javabot-Testing");
        testMessage("tell Javabot-Testing about camel I am a test", "Javabot-Testing, IAmATest");
        testMessage("tell Javabot-Testing about url I am a test", "Javabot-Testing, I+am+a+test");

        sleep(6000);
        sendTell("~~ Javabot-Testing seeTest", "Javabot-Testing, I'm a reply!");
        sendTell("~~ Javabot-Testing bobblahblaw", "Javabot-Testing, I have no idea what bobblahblaw is.");
        sendTell("~~ Javabot-Testing api", "Javabot-Testing, api is http://java.sun.com/javase/current/docs/api/index.html");
        sendTell("~~ Javabot-Testing camel I am a test 2", "Javabot-Testing, IAmATest2");
        sendTell("~~ Javabot-Testing url I am a test 2", "Javabot-Testing, I+am+a+test+2");

        sleep(6000);
        sendTell("~~Javabot-Testing seeTest", "Javabot-Testing, I'm a reply!");
        sendTell("~~Javabot-Testing bobblahblaw", "Javabot-Testing, I have no idea what bobblahblaw is.");
        sendTell("~~Javabot-Testing api", "Javabot-Testing, api is http://java.sun.com/javase/current/docs/api/index.html");
        sendTell("~~Javabot-Testing camel I am a test 3", "Javabot-Testing, IAmATest3");
        sendTell("~~Javabot-Testing url I am a test 3", "Javabot-Testing, I+am+a+test+3");
    }

    @SuppressWarnings({"EmptyCatchBlock"})
    private void sleep(final int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
        }
    }
    private void sendTell(final String message, final String response) {
        final TestBot bot = getTestBot();
        bot.sendMessage(getJavabotChannel(), message);
        validateResponses(bot, response);
    }
}