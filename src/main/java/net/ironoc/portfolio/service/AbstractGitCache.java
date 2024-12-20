package net.ironoc.portfolio.service;

import java.util.Map;

public abstract class AbstractGitCache {

    public <T> void clear(Map<String, T> gitDetails) {
        gitDetails.clear();
    }
}
