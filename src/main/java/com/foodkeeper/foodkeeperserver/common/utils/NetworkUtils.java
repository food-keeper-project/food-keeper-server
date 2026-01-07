package com.foodkeeper.foodkeeperserver.common.utils;

import com.foodkeeper.foodkeeperserver.common.enums.IpHeader;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NetworkUtils {
    private static final String UNKNOWN = "unknown";

    public static String getClientIp(HttpServletRequest request) {
        for (IpHeader header : IpHeader.values()) {
            String ip = request.getHeader(header.getValue());
            if (isValidIp(ip)) {
                return getFirstIp(ip);
            }
        }

        return request.getRemoteAddr();
    }

    private static String getFirstIp(String ip) {
        return ip.split(",")[0].trim();
    }

    private static boolean isValidIp(String ip) {
        return Strings.isNotBlank(ip) && !ip.equalsIgnoreCase(UNKNOWN);
    }
}
