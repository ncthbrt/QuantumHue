package co.za.cuthbert.three.value_objects;

/**
 * Copyright Nick Cuthbert, 2014.
 */
public enum DiscreteColour {
    RED(new Colour(255,0,0,255)),
    YELLOW(new Colour(255,255,0,255)),
    GREEN(new Colour(0,255,0,255)),
    CYAN(new Colour(0,255,255,255)),
    BLUE(new Colour(0,0,255,255)),
    MAGENTA(new Colour(255,0,255,255)),
    WHITE(new Colour(255,255,255,255)),
    ALPHA(new Colour(0,0,0,0));

    private final Colour colour;

    private DiscreteColour(Colour colour){
        this.colour=colour;
    }

    public static DiscreteColour add(DiscreteColour colour1,DiscreteColour colour2) {
        if (colour1 == ALPHA)
            return colour2;

        if (colour1 == WHITE || colour2==WHITE)
            return WHITE;

        if (colour1 == colour2)
            return colour1;

        if (colour1 == RED && colour2 == GREEN)
            return YELLOW;
        if (colour1 == GREEN && colour2 == BLUE)
            return CYAN;

        if (colour1 == BLUE && colour2 == RED)
            return MAGENTA;

        if((colour1==CYAN && colour2==RED)
            ||(colour1==MAGENTA && colour2==GREEN)
            ||(colour1==YELLOW && colour2==BLUE))
            return WHITE;

        if (colour1 == YELLOW || colour1 == CYAN || colour1 == MAGENTA)
            return colour1;

        return add(colour2, colour1);
    }

    public static DiscreteColour subtract(DiscreteColour colour1,DiscreteColour colour2) {
        if(colour1==ALPHA)
            return ALPHA;

        if((colour1==RED && (colour2==RED || colour2==MAGENTA || colour2==YELLOW))
          ||(colour1==BLUE && (colour2==BLUE || colour2==MAGENTA || colour2==CYAN))
          ||(colour1==GREEN && (colour2==GREEN || colour2==YELLOW || colour2==CYAN))
          || colour1==colour2)
            return ALPHA;

        if(colour1==MAGENTA) {
            if (colour2 == RED)
                return BLUE;
            if(colour2 == BLUE)
                return RED;
        }

        if(colour1==CYAN){
            if (colour2 == GREEN)
                return BLUE;
            if(colour2 == BLUE)
                return GREEN;
        }

        if(colour1 == YELLOW){
            if (colour2 == GREEN)
                return RED;
            if(colour2 == RED)
                return GREEN;
        }

        return colour1;

    }

    public Colour toColour() {
        return colour;
    }

    public static DiscreteColour map(Colour colour){
        System.out.println("RGB: "+colour.red()+" , "+ colour.green()+" , "+ colour.blue());
        for(DiscreteColour discreteColour: DiscreteColour.values()){
            if(discreteColour.toColour().equals(colour)){
                return discreteColour;
            }
        }
        return null;
    }

}
