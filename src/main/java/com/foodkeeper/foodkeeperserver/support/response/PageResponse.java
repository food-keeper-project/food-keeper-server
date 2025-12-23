package com.foodkeeper.foodkeeperserver.support.response;

import java.util.List;

public record PageResponse<T>(List<T> content, boolean hasNext) {
}
