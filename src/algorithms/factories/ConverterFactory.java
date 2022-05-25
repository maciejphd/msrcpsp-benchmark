package algorithms.factories;


import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.evolutionary_algorithms.converters.BaseConverter;
import algorithms.evolutionary_algorithms.converters.ConverterType;
import algorithms.evolutionary_algorithms.converters.TransformationConverter;
import algorithms.evolutionary_algorithms.converters.TruncatingConverter;

public class ConverterFactory {

  private ParameterSet parameters;

  public ConverterFactory(ParameterSet parameters) {
    this.parameters = parameters;
  }

  /**
   * Creates converter based on provided type.
   * Default: {@link TruncatingConverter}
   *
   * @param type type to use
   * @return chosen converter
   */
  public BaseConverter createConverter(ConverterType type) {
    switch (type) {
      case TRANSFORMATION:
        return new TransformationConverter(parameters.upperBounds);
      case TRUNCATING:
        return new TruncatingConverter();
      default:
        return new TruncatingConverter();
    }
  }

}
