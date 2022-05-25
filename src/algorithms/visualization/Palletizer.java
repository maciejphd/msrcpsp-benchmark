package algorithms.visualization;

public class Palletizer {
  private final double kPhi = 0.618033988749895;
  private final double kSeed = 0.127878832961928;
  private final double kSaturation = 0.5;
  private final double kValue = 0.95;

  private double hue;


  public Palletizer() {
    this.hue = kSeed;
  }

  public Palletizer(double hue) {
    this.hue = hue;
  }

  public String getColor() {
    int color = generateColor();
    return String.format("%06X", color);
  }

  private int generateColor() {
    hue += kPhi;
    hue -= Math.floor(hue);
    return HSVtoRGB(hue, kSaturation, kValue);
  }

  private int HSVtoRGB(double h, double s, double v) {
    int remainder = (int)Math.floor(h * 6);
    if (remainder == 6) {
      remainder = 0;
    }
    double f = h * 6 - remainder;
    double p = v * (1 - s);
    double q = v * (1 - f * s);
    double t = v * (1 - (1 - f) * s);

    double r, g, b;
    switch (remainder) {
      case 0:
        r = v; g = t; b = p;
        break;
      case 1:
        r = q; g = v; b = p;
        break;
      case 2:
        r = p; g = v; b = t;
        break;
      case 3:
        r = p; g = q; b = v;
        break;
      case 4:
        r = t; g = p; b = v;
        break;
      case 5:
        r = v; g = p; b = q;
        break;
      default:
        throw new IndexOutOfBoundsException();
    }

    int red   = (int)Math.floor(255 * r);
    int green = (int)Math.floor(255 * g);
    int blue  = (int)Math.floor(255 * b);

    return (red << 16) + (green << 8) + blue;
  }
}
