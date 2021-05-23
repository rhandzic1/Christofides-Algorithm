package ba.unsa.etf.diplomski;

public class City {

    private double x;
    private double y;

    public City(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getDistance(City g) {
        return Math.sqrt(Math.pow(this.x - g.getX(), 2) + Math.pow(this.y - g.getY(), 2));
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
