package jmeow.selectiveofflinemode;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class RealTimeArgumentType implements ArgumentType<Integer> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0w", "0d", "0m", "0");
    private static final SimpleCommandExceptionType INVALID_UNIT_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("argument.time.invalid_unit"));
    private static final Dynamic2CommandExceptionType SECOND_COUNT_TOO_LOW_EXCEPTION = new Dynamic2CommandExceptionType((value, minimum) -> Text.stringifiedTranslatable("argument.time.second_count_too_low", minimum, value));
    private static final Object2IntMap<String> UNITS = new Object2IntOpenHashMap<>();

    static {
        UNITS.put("w", 604800);
        UNITS.put("d", 86400);
        UNITS.put("m", 60);
        UNITS.put("", 1);
    }

    final int minimum = 1;

    public static RealTimeArgumentType time() {
        return new RealTimeArgumentType();
    }

    public Integer parse(StringReader stringReader) throws CommandSyntaxException {
        float f = stringReader.readFloat();
        String string = stringReader.readUnquotedString();
        int unit = UNITS.getOrDefault(string, 0);
        if (unit == 0) {
            throw INVALID_UNIT_EXCEPTION.createWithContext(stringReader);
        } else {
            int j = Math.round(f * (float) unit);
            if (j < this.minimum) {
                throw SECOND_COUNT_TOO_LOW_EXCEPTION.createWithContext(stringReader, j, this.minimum);
            } else {
                return j;
            }
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        StringReader stringReader = new StringReader(builder.getRemaining());

        try {
            stringReader.readFloat();
        } catch (CommandSyntaxException e) {
            return builder.buildFuture();
        }

        return CommandSource.suggestMatching(UNITS.keySet(), builder.createOffset(builder.getStart() + stringReader.getCursor()));
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
