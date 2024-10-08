package com.noom.interview.fullstack.sleep.db;

import com.noom.interview.fullstack.sleep.db.entity.*;
import com.noom.interview.fullstack.sleep.db.repository.*;
import com.noom.interview.fullstack.sleep.domain.*;
import com.noom.interview.fullstack.sleep.fixtures.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;

import java.time.*;
import java.util.*;

import static org.mockito.Mockito.*;

@ActiveProfiles("unittest")
@SpringBootTest
public class DailySleepLogProviderImplTest {

    @InjectMocks
    private DailySleepLogProviderImpl provider;

    @Mock
    private DailySleepLogRepository sleepLogRepository;

    @Test
    public void saveTest() {
        when(sleepLogRepository.save(any(DailySleepLogEntity.class)))
            .thenReturn(DailySleepLogEntityFixture.defaultValues());

        DailySleepLog toSave = DailySleepLogFixture.defaultValuesNoId();
        DailySleepLog result = provider.save(toSave);
        Assertions.assertEquals(1L, result.getId());

        ArgumentCaptor<DailySleepLogEntity> captor = ArgumentCaptor.forClass(DailySleepLogEntity.class);
        verify(sleepLogRepository, times(1)).save(captor.capture());
        DailySleepLogEntity data = captor.getValue();
        Assertions.assertEquals(toSave.getSleepStart(), data.getSleepStart());
        Assertions.assertEquals(toSave.getSleepEnd(), data.getSleepEnd());
        Assertions.assertEquals(toSave.getSleepQuality(), data.getSleepQuality());
        Assertions.assertEquals(toSave.getUserId(), data.getUserId());
    }

    @Test
    public void findByIntervalTest(){
        DailySleepLogEntity resultFixture = DailySleepLogEntityFixture.defaultValues();
        when(sleepLogRepository.findByUserIdAndSleepEndBetweenOrderBySleepEndDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(resultFixture));

        List<DailySleepLog> result = provider.findByUserIdAndInterval(1L, LocalDateTime.now(), LocalDateTime.now());
        verify(sleepLogRepository, times(1)).findByUserIdAndSleepEndBetweenOrderBySleepEndDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
        Assertions.assertEquals(1, result.size());

        DailySleepLog data = result.get(0);
        Assertions.assertEquals(resultFixture.getSleepStart(), data.getSleepStart());
        Assertions.assertEquals(resultFixture.getSleepEnd(), data.getSleepEnd());
        Assertions.assertEquals(resultFixture.getSleepQuality(), data.getSleepQuality());
        Assertions.assertEquals(resultFixture.getUserId(), data.getUserId());
        Assertions.assertEquals(resultFixture.getId(), data.getId());

    }
}
