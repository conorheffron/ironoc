package net.ironoc.portfolio.logger;

public interface LoggerI {

    void info(String message, Object...values);

    void debug(String message, Object...values);

    void error(String message, Object...values);

    void warn(String message, Object...values);
}
