package Card;

public enum UnoColor {
    RED("Red"),
    BLUE("Blue"),
    YELLOW("Yellow"),
    GREEN("Green"),
    BLACK("Black");

    private String color;

    UnoColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
