package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.domain.Lock;
import at.roteskreuz.covidapp.exception.LockNotAcquiredException;
import at.roteskreuz.covidapp.repository.LockRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zolika
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LockService {
	
	private final LockRepository lockRepository;
	
	
	public LocalDateTime acquireLock(String lockId, Duration ttl) throws LockNotAcquiredException {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiresAt = now.plus(ttl);
		Lock lock = lockRepository.findById(lockId).orElse(null);
		if (lock == null) {
			lockRepository.save(new Lock(lockId, expiresAt));			
		} else {
			//we have a lock, checking if it is expired
			if (lock.getExpires().isAfter(now)) {
				//did not expired, we cannot acquire it
				throw new LockNotAcquiredException();
			}
			lock.setExpires(expiresAt);
			lockRepository.save(lock);
		}
		return expiresAt;
	}
	
	public boolean releaseLock(String lockId, LocalDateTime timestamp) {
		Lock lock = lockRepository.findById(lockId).orElse(null);
		if (lock != null) {
			if (!lock.getExpires().equals(timestamp)) {
				//Another process acquired an expired lock
				return false;
			}
			//we can delete it
			lockRepository.delete(lock);
		}
		return true;
	}
}
