package com.foodkeeper.foodkeeperserver.notification.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record MemberFcmTokens(Map<String, List<String>> tokens) {

    public List<String> getTokensByMember(String memberKey) {
        return tokens.getOrDefault(memberKey, Collections.emptyList());
    }

    public boolean hasTokens(String memberKey) {
        return tokens.containsKey(memberKey) && !tokens.get(memberKey).isEmpty();
    }
}
