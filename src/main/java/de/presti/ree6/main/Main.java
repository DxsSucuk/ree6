package de.presti.ree6.main;

import de.presti.ree6.bot.BotInfo;
import de.presti.ree6.bot.BotState;
import de.presti.ree6.bot.BotUtil;
import de.presti.ree6.bot.BotVersion;
import de.presti.ree6.commands.CommandManager;
import de.presti.ree6.events.LoggingEvents;
import de.presti.ree6.events.OtherEvents;
import de.presti.ree6.music.MusikWorker;
import de.presti.ree6.sql.SQLConnector;
import de.presti.ree6.sql.SQLWorker;
import de.presti.ree6.utils.ArrayUtil;
import de.presti.ree6.utils.Config;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static Main insance;
    public static CommandManager cm;
    public static SQLConnector sqlConnector;
    public static SQLWorker sqlWorker;
    public static Thread checker;
    public static Config config;
    public static String lastday = "";

    public static void main(String[] args) {
        insance = new Main();

        config = new Config();

        config.init();

        sqlConnector = new SQLConnector(config.getConfig().getString("mysql.user"), config.getConfig().getString("mysql.pw"), config.getConfig().getString("mysql.host"), config.getConfig().getString("mysql.db"), config.getConfig().getInt("mysql.port"));

        sqlWorker = new SQLWorker();

        cm = new CommandManager();

        try {
            BotUtil.createBot(BotVersion.PUBLIC, "1.3.1");
            new MusikWorker();
            insance.addEvents();
        } catch (Exception ex) {
            System.out.println("Error while init: " + ex.getMessage());
        }
        insance.addHooks();
        BotInfo.starttime = System.currentTimeMillis();
    }

    private void addEvents() {
        BotUtil.addEvent(new OtherEvents());
        BotUtil.addEvent(new LoggingEvents());
    }

    private void addHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                shutdown();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }));
    }

    private void shutdown() throws SQLException {
        System.out.println("Shutdown init. !");
        try {
            sqlWorker.saveAllInvites();
            System.out.println("Uploaded Invitecache to Database!");
        } catch (Exception ex) {
            System.out.println("Couldnt save Invitecach!\nException: " + ex.getMessage());
        }

        try {
            sqlWorker.saveAllChatProtectors();
            System.out.println("Uploaded ChatProtector to Database!");
        } catch (Exception ex) {
            System.out.println("Couldnt save ChatProtector!\nException: " + ex.getMessage());
        }

        sqlConnector.close();
        System.out.println("Closed Database Connection");
        BotUtil.shutdown();
        System.out.println("JDA Instance has been shutdowned!");

        System.out.println("Good bye!");
    }

    int randomint = 0;

    public void createCheckerThread() {
        checker = new Thread(() -> {
            while (BotInfo.state != BotState.STOPPED) {

                if (!lastday.equalsIgnoreCase(new SimpleDateFormat("dd").format(new Date()))) {

                    ArrayUtil.messageIDwithMessage.clear();
                    ArrayUtil.messageIDwithUser.clear();

                    BotUtil.setActivity(BotInfo.botInstance.getGuilds().size() + " Guilds", Activity.ActivityType.WATCHING);

                    System.out.println();
                    System.out.println("Todays Stats:");
                    System.out.println("Guilds: " + BotInfo.botInstance.getGuilds().size());

                    int i = 0;

                    for (Guild guild : BotInfo.botInstance.getGuilds()) {
                        i += guild.getMemberCount();
                    }

                    System.out.println("Overall Users: " + i);

                    lastday = new SimpleDateFormat("dd").format(new Date());
                }


                if (randomint >= 6) {
                    sqlConnector.close();
                    sqlConnector.connect();
                    randomint = 0;
                } else {
                    randomint++;
                }

                try {
                    Thread.sleep((5 * (60000L)));
                } catch (InterruptedException e) {
                }
            }
        });
        checker.start();
    }
}
