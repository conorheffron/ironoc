package net.ironoc.portfolio.logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractLogger implements LoggerI {

    @Override
    public void info(String message, Object...values) {
        log.info(message, values);
    }

    @Override
    public void debug(String message, Object...values) {
        log.debug(message, values);
    }

    @Override
    public void error(String message, Object...values) {
        log.error(message, values);
    }

    @Override
    public void warn(String message, Object...values) {
        log.warn(message, values);
    }
}
