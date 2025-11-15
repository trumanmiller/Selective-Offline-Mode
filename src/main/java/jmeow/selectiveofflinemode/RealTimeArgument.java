package jmeow.selectiveofflinemode;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class RealTimeArgument {
    public static final SimpleCommandExceptionType INVALID_UNIT =
            new SimpleCommandExceptionType(Text.literal("Invalid time unit. Use s, m, h, d."));

    public static HashMap<String, Float> UNITS = new HashMap<>();

    static {
        UNITS.put("d", 24000F);
        UNITS.put("h", 24000F);
        UNITS.put("m", 60F);
        UNITS.put("s", 1F);
        UNITS.put("", 1F);
    }

    public static Long parse(String string) throws CommandSyntaxException {
        StringReader stringReader = new StringReader(string);

        int firstChar = stringReader.getCursor();

        while (stringReader.canRead() && (Character.isDigit(stringReader.peek()) || stringReader.peek() == '.')) {
            stringReader.skip();
        }

        if (stringReader.getCursor() == firstChar) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedFloat().create();
        }

        Float floatValue = Float.parseFloat(stringReader.getString().substring(firstChar, stringReader.getCursor()));

        if (!stringReader.canRead()) {
            return floatValue.longValue();
        }

        char unit = stringReader.read();

        Float floatDuration = switch (unit) {
            case 's' -> floatValue;
            case 'm' -> floatValue * 60;
            case 'h' -> floatValue * 60 * 60;
            case 'd' -> floatValue * 60 * 60 * 24;
            default -> throw INVALID_UNIT.createWithContext(stringReader);
        };

        return floatDuration.longValue();
    }

    public static <CommandSourceStack> CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        StringReader stringReader = new StringReader(builder.getRemaining());

        try {
            stringReader.readFloat();
        } catch (CommandSyntaxException e) {
            return builder.buildFuture();
        }

        return CommandSource.suggestMatching(UNITS.keySet(), builder.createOffset(builder.getStart() + stringReader.getCursor()));
    }


}
