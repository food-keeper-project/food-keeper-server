package com.foodkeeper.foodkeeperserver.notification.implement;

import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.FcmRepository;
import com.foodkeeper.foodkeeperserver.notification.fixture.FcmFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FcmManagerTest {

    @InjectMocks FcmManager fcmManager;
    @Mock FcmRepository fcmRepository;

    @Test
    @DisplayName("memberKey 리스트에 매핑되는 fcm 토큰 리스트 조회 후 Map 에 저장")
    void findTokens_SUCCESS() throws Exception {
        //given
        List<String> memberKeys = List.of("key1","key2");
        List<FcmTokenEntity> tokens = List.of(
                FcmFixture.createMock(1L,"token1","2000-10-10 08:10:10","2000-10-10 08:12:10"),
                FcmFixture.createMock(2L,"token2","2001-10-10 08:10:10","2001-10-10 08:12:10"),
        );
        //when

        //then

    }
}
