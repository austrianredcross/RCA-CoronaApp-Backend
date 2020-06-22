package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.Lock;
import at.roteskreuz.covidapp.exception.LockNotAcquiredException;
import at.roteskreuz.covidapp.repository.LockRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/*
 * @author Zoltán Puskai
 */
@SpringBootTest
public class LockServiceTest {

	@Autowired
	private LockService service;
	@MockBean
	private  LockRepository repository;


	@Test
	public void whenLockDoesNotExistItShouldCreateNewLock() throws LockNotAcquiredException {
		String lockId = "lockId";
		Mockito.when(repository.findById(lockId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(Mockito.any())).then(i -> i.getArgument(0, Lock.class));
		LocalDateTime lockTime = service.acquireLock(lockId, Duration.ofMinutes(5));
		assertThat(lockTime).isNotNull();
		assertThat(lockTime).isAfter(LocalDateTime.now().minusMinutes(5));
	}

	
	@Test
	public void whenLockDoesExistButExpiredItShouldCreateNewLock() throws LockNotAcquiredException {
		String lockId = "lockId";
		LocalDateTime lTime = LocalDateTime.now().minusMinutes(10);
		Mockito.when(repository.findById(lockId)).thenReturn(Optional.of(new Lock(lockId, lTime)));
		Mockito.when(repository.save(Mockito.any())).then(i -> i.getArgument(0, Lock.class));
		LocalDateTime lockTime = service.acquireLock(lockId, Duration.ofMinutes(5));
		assertThat(lockTime).isNotNull();
		assertThat(lockTime).isAfter(LocalDateTime.now().minusMinutes(5));
	}	
	
	@Test
	public void whenLockDoesExistItThrowException() {
		String lockId = "lockId";
		LocalDateTime lTime = LocalDateTime.now().plusMinutes(3);
		Mockito.when(repository.findById(lockId)).thenReturn(Optional.of(new Lock(lockId, lTime)));
		Assertions.assertThatThrownBy(() -> service.acquireLock(lockId, Duration.ofMinutes(5))).isInstanceOf(LockNotAcquiredException.class);
	}	

}
