package de.presti.ree6.commands.impl.info;

import de.presti.ree6.bot.BotInfo;
import de.presti.ree6.bot.BotUtil;
import de.presti.ree6.commands.Category;
import de.presti.ree6.commands.Command;
import de.presti.ree6.main.Data;
import de.presti.ree6.music.MusikWorker;
import de.presti.ree6.utils.TimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Stats extends Command {

    public Stats() {
        super("stats", "Show some BotStats!", Category.INFO);
    }

    @Override
    public void onPerform(Member sender, Message messageSelf, String[] args, TextChannel m) {

        long start = System.currentTimeMillis();

        messageSelf.delete().queue();

        EmbedBuilder em = new EmbedBuilder();

        em.setAuthor(BotInfo.botInstance.getSelfUser().getName(), Data.website,
                BotInfo.botInstance.getSelfUser().getAvatarUrl());
        em.setTitle("Stats!");
        em.setThumbnail(BotInfo.botInstance.getSelfUser().getAvatarUrl());
        em.setColor(BotUtil.randomEmbedColor());

        int i = 0;

        for(Guild guild : BotInfo.botInstance.getGuilds()) {
            i += guild.getMemberCount();
        }

        em.addField("**Server Stats:**", "", true);
        em.addField("**Guilds**", BotInfo.botInstance.getGuilds().size() + "", true);
        em.addField("**Users**", i + "", true);

        em.addField("**Bot Stats:**", "", true);
        em.addField("**Version**", BotInfo.build + "-" + BotInfo.version.name().toUpperCase(), true);
        em.addField("**Uptime**", TimeUtil.getTime(BotInfo.starttime), true);

        em.addField("**Network Stats:**", "", true);
        em.addField("**Response Time**", (Integer.parseInt((System.currentTimeMillis() - start) + "")) + "ms", true);
        em.addField("**System Date**" , new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()), true);

        em.setFooter(m.getGuild().getName(), m.getGuild().getIconUrl());

        sendMessage(em, 5, m);

    }
}
