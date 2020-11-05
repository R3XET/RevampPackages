package eu.revamp.packages.utils;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CC {

    //----COLORS----//
    public static final String BLUE = org.bukkit.ChatColor.BLUE.toString();
    public static final String AQUA = org.bukkit.ChatColor.AQUA.toString();
    public static final String YELLOW = org.bukkit.ChatColor.YELLOW.toString();
    public static final String RED = org.bukkit.ChatColor.RED.toString();
    public static final String GRAY = org.bukkit.ChatColor.GRAY.toString();
    public static final String GOLD = org.bukkit.ChatColor.GOLD.toString();
    public static final String GREEN = org.bukkit.ChatColor.GREEN.toString();
    public static final String WHITE = org.bukkit.ChatColor.WHITE.toString();
    public static final String BLACK = org.bukkit.ChatColor.BLACK.toString();
    public static final String BOLD = org.bukkit.ChatColor.BOLD.toString();
    public static final String ITALIC = org.bukkit.ChatColor.ITALIC.toString();
    public static final String UNDER_LINE = org.bukkit.ChatColor.UNDERLINE.toString();
    public static final String STRIKE_THROUGH = org.bukkit.ChatColor.STRIKETHROUGH.toString();
    public static final String RESET = org.bukkit.ChatColor.RESET.toString();
    public static final String MAGIC = org.bukkit.ChatColor.MAGIC.toString();
    public static final String DARK_BLUE = org.bukkit.ChatColor.DARK_BLUE.toString();
    public static final String DARK_AQUA = org.bukkit.ChatColor.DARK_AQUA.toString();
    public static final String DARK_GRAY = org.bukkit.ChatColor.DARK_GRAY.toString();
    public static final String DARK_GREEN = org.bukkit.ChatColor.DARK_GREEN.toString();
    public static final String DARK_PURPLE = org.bukkit.ChatColor.DARK_PURPLE.toString();
    public static final String DARK_RED = org.bukkit.ChatColor.DARK_RED.toString();
    public static final String PINK = org.bukkit.ChatColor.LIGHT_PURPLE.toString();

    //----LINES----//
    public static final String MENU_BAR = org.bukkit.ChatColor.GRAY.toString() + org.bukkit.ChatColor.STRIKETHROUGH.toString() + "------------------------";
    public static final String CHAT_BAR = org.bukkit.ChatColor.GRAY.toString() + org.bukkit.ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------";
    public static final String SB_BAR = org.bukkit.ChatColor.GRAY.toString() + org.bukkit.ChatColor.STRIKETHROUGH.toString() + "----------------------";

    //----SYMBOLS----//
    public static String HEALTH = StringEscapeUtils.unescapeJava("\u2764");
    public static String ARROW_LEFT = StringEscapeUtils.unescapeJava("\u00AB");
    public static String ARROW_RIGHT = StringEscapeUtils.unescapeJava("\u00BB");
    public static String X = ChatColor.AQUA + StringEscapeUtils.unescapeJava("\u2716");
    public static String ALERT = StringEscapeUtils.unescapeJava("\u26A0");
    public static String STICK = StringEscapeUtils.unescapeJava("\u2503");
    public static String STAR1 = StringEscapeUtils.unescapeJava("\u2724");
    public static String STAR2 = StringEscapeUtils.unescapeJava("\u273b");
    public static String STAR3 = StringEscapeUtils.unescapeJava("\u2739");
    public static String STAR4 = StringEscapeUtils.unescapeJava("\u273a");
    public static String STAR5 = StringEscapeUtils.unescapeJava("\u2725");
    public static String STAR6 = StringEscapeUtils.unescapeJava("\u2735");


    public static ImmutableMap<Object, Object> CHAT_DYE_COLOUR_MAP = ImmutableMap.builder().put(org.bukkit.ChatColor.AQUA, DyeColor.LIGHT_BLUE).put(org.bukkit.ChatColor.BLACK, DyeColor.BLACK).put(org.bukkit.ChatColor.BLUE, DyeColor.LIGHT_BLUE).put(org.bukkit.ChatColor.DARK_AQUA, DyeColor.CYAN).put(org.bukkit.ChatColor.DARK_BLUE, DyeColor.BLUE).put(org.bukkit.ChatColor.DARK_GRAY, DyeColor.GRAY).put(org.bukkit.ChatColor.DARK_GREEN, DyeColor.GREEN).put(org.bukkit.ChatColor.DARK_PURPLE, DyeColor.PURPLE).put(org.bukkit.ChatColor.DARK_RED, DyeColor.RED).put(org.bukkit.ChatColor.GOLD, DyeColor.ORANGE).put(org.bukkit.ChatColor.GRAY, DyeColor.SILVER).put(org.bukkit.ChatColor.GREEN, DyeColor.LIME).put(org.bukkit.ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA).put(org.bukkit.ChatColor.RED, DyeColor.RED).put(org.bukkit.ChatColor.WHITE, DyeColor.WHITE).put(org.bukkit.ChatColor.YELLOW, DyeColor.YELLOW).build();

    public static String translate(String s) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> translate(List<String> paramList) {
        return paramList.stream().map(CC::translate).collect(Collectors.toList());
    }

    public static List<String> translate(String[] lines) {
        List<String> toReturn = new ArrayList<>();
        for (String line : lines) {
            if (line != null) {
                toReturn.add(org.bukkit.ChatColor.translateAlternateColorCodes('&', line));
            }
        }
        return toReturn;
    }

    public static DyeColor toDyeColor(org.bukkit.ChatColor colour) {
        return (DyeColor)CC.CHAT_DYE_COLOUR_MAP.get(colour);
    }

    public static net.md_5.bungee.api.ChatColor fromBukkit(org.bukkit.ChatColor chatColor) {
        return net.md_5.bungee.api.ChatColor.values()[chatColor.ordinal()];
    }

    public static String strip(String message) {
        return ChatColor.stripColor(message);
    }

    public static net.md_5.bungee.api.ChatColor toBungee(org.bukkit.ChatColor color) {
        switch (color) {
            case BLACK: {
                return net.md_5.bungee.api.ChatColor.BLACK;
            }
            case DARK_BLUE: {
                return net.md_5.bungee.api.ChatColor.DARK_BLUE;
            }
            case DARK_GREEN: {
                return net.md_5.bungee.api.ChatColor.DARK_GREEN;
            }
            case DARK_AQUA: {
                return net.md_5.bungee.api.ChatColor.DARK_AQUA;
            }
            case DARK_RED: {
                return net.md_5.bungee.api.ChatColor.DARK_RED;
            }
            case DARK_PURPLE: {
                return net.md_5.bungee.api.ChatColor.DARK_PURPLE;
            }
            case GOLD: {
                return net.md_5.bungee.api.ChatColor.GOLD;
            }
            case GRAY: {
                return net.md_5.bungee.api.ChatColor.GRAY;
            }
            case DARK_GRAY: {
                return net.md_5.bungee.api.ChatColor.DARK_GRAY;
            }
            case BLUE: {
                return net.md_5.bungee.api.ChatColor.BLUE;
            }
            case GREEN: {
                return net.md_5.bungee.api.ChatColor.GREEN;
            }
            case AQUA: {
                return net.md_5.bungee.api.ChatColor.AQUA;
            }
            case RED: {
                return net.md_5.bungee.api.ChatColor.RED;
            }
            case LIGHT_PURPLE: {
                return net.md_5.bungee.api.ChatColor.LIGHT_PURPLE;
            }
            case YELLOW: {
                return net.md_5.bungee.api.ChatColor.YELLOW;
            }
            case WHITE: {
                return net.md_5.bungee.api.ChatColor.WHITE;
            }
            case MAGIC: {
                return net.md_5.bungee.api.ChatColor.MAGIC;
            }
            case BOLD: {
                return net.md_5.bungee.api.ChatColor.BOLD;
            }
            case STRIKETHROUGH: {
                return net.md_5.bungee.api.ChatColor.STRIKETHROUGH;
            }
            case UNDERLINE: {
                return net.md_5.bungee.api.ChatColor.UNDERLINE;
            }
            case ITALIC: {
                return net.md_5.bungee.api.ChatColor.ITALIC;
            }
            case RESET: {
                return net.md_5.bungee.api.ChatColor.RESET;
            }
            default: {
                throw new IllegalArgumentException("Unrecognised Bukkit color " + color.name() + ".");
            }
        }
    }
}

