package studio.overmine.overregion.utilities;

import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class TimeUtil {

    public long formatLong(String input) {
        if (input == null || input.isEmpty()) return -1L;

        long result = 0L;
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);

            if (Character.isDigit(c)) {
                number.append(c);
            }
            else {
                String str;
                if (Character.isLetter(c) && !(str = number.toString()).isEmpty()) {
                    result += convertLong(Integer.parseInt(str), c);
                    number = new StringBuilder();
                }
            }
        }
        return result;
    }

    private long convertLong(int value, char unit) {
        return switch (unit) {
            case 'y' -> value * TimeUnit.DAYS.toMillis(365L);
            case 'M' -> value * TimeUnit.DAYS.toMillis(30L);
            case 'd' -> value * TimeUnit.DAYS.toMillis(1L);
            case 'h' -> value * TimeUnit.HOURS.toMillis(1L);
            case 'm' -> value * TimeUnit.MINUTES.toMillis(1L);
            case 's' -> value * TimeUnit.SECONDS.toMillis(1L);
            default -> -1L;
        };
    }
}
