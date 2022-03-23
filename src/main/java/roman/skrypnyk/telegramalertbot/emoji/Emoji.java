package roman.skrypnyk.telegramalertbot.emoji;

public enum Emoji {
    ALARM_CLOCK(null, '\u23F0'),
    HEAVY_EXCLAMATION_MARK_SYMBOL(null, '\u2757'),
    WHITE_EXCLAMATION_MARK_ORNAMENT(null, '\u2755'),
    LEFT_RIGHT_ARROW(null, '\u2194'),
    INFORMATION_SOURCE(null, '\u2139');

    Character firstChar;
    Character secondChar;

    Emoji(Character firstChar, Character secondChar) {
        this.firstChar = firstChar;
        this.secondChar = secondChar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (this.firstChar != null) {
            sb.append(this.firstChar);
        }
        if (this.secondChar != null) {
            sb.append(this.secondChar);
        }

        return sb.toString();
    }
}
