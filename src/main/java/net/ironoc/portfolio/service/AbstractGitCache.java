package net.ironoc.portfolio.service;

import java.util.Map;

public abstract class AbstractGitCache {

    <T> void clear(Map<String, T> gitDetails) {
        gitDetails.clear();
    }

    abstract void remove(String key);
}
