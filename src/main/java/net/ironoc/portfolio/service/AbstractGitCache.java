package net.ironoc.portfolio.service;

import module java.base;

public abstract sealed class AbstractGitCache permits GitProjectCacheService, GitRepoCacheService {

    <T> void clear(Map<String, T> gitDetails) {
        gitDetails.clear();
    }

    abstract void remove(String key);
}
