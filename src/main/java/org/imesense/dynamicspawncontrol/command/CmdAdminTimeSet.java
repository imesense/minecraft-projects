package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;

import javax.annotation.Nonnull;

@InitLog
@TODO(value = "Add this class on diagram project", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class CmdAdminTimeSet extends CommandBase
{
    public CmdAdminTimeSet()
    {
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_time_set";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return "/dsc_time_set <day|night|time_ticks|HH:MM> [dayNumber]\n" +
                "Примеры:\n" +
                "  /dsc_time_set day 5        - Установить день на 5-й день\n" +
                "  /dsc_time_set night        - Установить ночь на текущий день\n" +
                "  /dsc_time_set 12000        - Установить полдень (12:00)\n" +
                "  /dsc_time_set 18:30 3      - Установить 18:30 на 3-й день\n" +
                "  /dsc_time_set +1           - Перейти на следующий день (относительно)\n" +
                "  /dsc_time_set -2           - Перейти на 2 дня назад";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
    {
        if (args.length < 1 || args.length > 2)
            throw new CommandException(getUsage(sender));

        WorldServer world = server.getWorld(0);
        long currentTime = world.getWorldTime();
        long currentDay = currentTime / 24000L;
        long currentTimeOfDay = currentTime % 24000L;

        long targetDay = currentDay;
        boolean useRelativeDay = false;

        if (args.length == 2)
        {
            targetDay = parseDayArgument(args[1], currentDay);
        }
        else if (args[0].startsWith("+") || args[0].startsWith("-"))
        {
            try
            {
                long dayOffset = Long.parseLong(args[0]);
                targetDay = currentDay + dayOffset;
                if (targetDay < 0) targetDay = 0;
                useRelativeDay = true;
            }
            catch (NumberFormatException ignored) { }
        }

        String timeArg = args[0].toLowerCase();
        long timeTicks;

        if (useRelativeDay)
        {
            timeTicks = currentTimeOfDay;
        }
        else if ("day".equals(timeArg))
        {
            timeTicks = 1000L;
        }
        else if ("night".equals(timeArg))
        {
            timeTicks = 13000L;
        }
        else if ("noon".equals(timeArg) || "midday".equals(timeArg))
        {
            timeTicks = 6000L;
        }
        else if ("midnight".equals(timeArg))
        {
            timeTicks = 18000L;
        }
        else if (timeArg.contains(":"))
        {
            timeTicks = parseTimeFormat(timeArg);
        }
        else
        {
            timeTicks = parseTicks(timeArg);
        }

        if (timeTicks < 0 || timeTicks >= 24000)
        {
            throw new CommandException("Время должно быть в диапазоне 0-23999 тиков (0:00 - 23:59)");
        }

        long targetTime = targetDay * 24000L + timeTicks;

        Logger.info("[DSC DEBUG] Current time: " + currentTime +
                " (day " + currentDay + ", time " + currentTimeOfDay + ")");
        Logger.info("[DSC DEBUG] Setting to: " + targetTime +
                " (day " + targetDay + ", time " + timeTicks + ")");

        world.setWorldTime(targetTime);

        long newTime = world.getWorldTime();
        long newDay = newTime / 24000L;
        long newTimeOfDay = newTime % 24000L;

        Logger.info("[DSC DEBUG] New time: " + newTime +
                " (day " + newDay + ", time " + newTimeOfDay + ")");

        String timeStr = formatTime(timeTicks);
        String dayInfo = formatDayInfo(targetDay, currentDay);

        sendMessage(sender, "§aВремя установлено на §e" + timeStr + "§a (§b" + dayInfo + "§a)");
        sendMessage(sender, "§7Текущий день: §6" + currentDay +
                "§7 → Целевой день: §6" + targetDay +
                "§7 (тики: §6" + timeTicks + "§7)");
        sendMessage(sender, "§7Общее время: §6" + targetTime);

        if (newDay != targetDay || Math.abs(newTimeOfDay - timeTicks) > 10)
        {
            sendMessage(sender, "§cВнимание: Время могло не установиться корректно!");
            sendMessage(sender, "§cОжидалось: день " + targetDay + ", время " + timeTicks);
            sendMessage(sender, "§cПолучилось: день " + newDay + ", время " + newTimeOfDay);
        }
    }

    private long parseDayArgument(String dayArg, long currentDay) throws CommandException
    {
        try
        {
            if (dayArg.startsWith("+") || dayArg.startsWith("-"))
            {
                long offset = Long.parseLong(dayArg);
                long result = currentDay + offset;
                return Math.max(0, result);
            }
            else
            {
                long day = Long.parseLong(dayArg);

                if (day < 0)
                {
                    throw new CommandException("День не может быть отрицательным: " + dayArg);
                }

                return day;
            }
        }
        catch (NumberFormatException exception)
        {
            throw new CommandException("Неверный формат дня: " + dayArg);
        }
    }

    private String formatDayInfo(long targetDay, long currentDay)
    {
        if (targetDay == currentDay)
        {
            return "текущий день";
        }
        else
        {
            long diff = targetDay - currentDay;
            String direction = diff > 0 ? "вперёд" : "назад";
            return "день " + targetDay + " (§e" + Math.abs(diff) + " дней " + direction + "§b)";
        }
    }

    private void sendMessage(ICommandSender sender, String message)
    {
        String cleanMessage = message.replaceAll("§[0-9a-fklmnor]", "");

        if (sender instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) sender;
            player.sendMessage(new TextComponentString(message));
        }
        else
        {
            sender.sendMessage(new TextComponentString(cleanMessage));
        }
    }

    private long parseTimeFormat(String timeStr) throws CommandException
    {
        try
        {
            String[] parts = timeStr.split(":");
            if (parts.length != 2)
                throw new CommandException("Неверный формат времени. используйте ЧЧ:ММ");

            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            if (hours < 0 || hours > 23)
                throw new CommandException("Часы должны быть в диапазоне 0-23");
            if (minutes < 0 || minutes > 59)
                throw new CommandException("Минуты должны быть в диапазоне 0-59");

            return (hours * 1000L) + (minutes * 1000L / 60);
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("Неверный формат времени: " + timeStr);
        }
    }

    private long parseTicks(String ticksStr) throws CommandException
    {
        try
        {
            return Long.parseLong(ticksStr);
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("Неверный формат тиков: " + ticksStr);
        }
    }

    private String formatTime(long ticks)
    {
        long adjustedTicks = (ticks + 6000) % 24000;

        int hours = (int) (adjustedTicks / 1000);
        int minutes = (int) ((adjustedTicks % 1000) * 60 / 1000);

        String period = (hours < 12) ? "AM" : "PM";
        if (hours == 0) hours = 12;
        else if (hours > 12) hours -= 12;

        return String.format("%d:%02d %s", hours, minutes, period) +
                " (§6" + ticks + " тиков§r)";
    }

    @Override
    public java.util.List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, net.minecraft.util.math.BlockPos targetPos)
    {
        if (args.length == 1)
        {
            java.util.List<String> suggestions = new java.util.ArrayList<>();

            suggestions.add("day");
            suggestions.add("night");
            suggestions.add("noon");
            suggestions.add("midnight");
            suggestions.add("12000");
            suggestions.add("0");
            suggestions.add("6000");
            suggestions.add("18000");
            suggestions.add("+1");
            suggestions.add("-1");
            suggestions.add("+2");
            suggestions.add("-2");

            return CommandBase.getListOfStringsMatchingLastWord(args, suggestions);
        }
        else if (args.length == 2)
        {
            java.util.List<String> suggestions = new java.util.ArrayList<>();

            suggestions.add("0");
            suggestions.add("1");
            suggestions.add("3");
            suggestions.add("5");
            suggestions.add("10");
            suggestions.add("+1");
            suggestions.add("-1");

            return CommandBase.getListOfStringsMatchingLastWord(args, suggestions);
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}