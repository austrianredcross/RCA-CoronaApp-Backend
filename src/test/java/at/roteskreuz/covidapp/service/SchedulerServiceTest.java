package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.model.ApiResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/*
 * @author Zoltán Puskai
 */
@SpringBootTest
public class SchedulerServiceTest {

	@Autowired
	private SchedulerService schedulerService;
	@MockBean
	private  ExportService exportService;
	@MockBean
	private ClientConfigurationService clientConfigService;
	
	
	@Test
	public void whenFlushCacheScheduledItWillFlushTheCache() {
		Mockito.doNothing().when(clientConfigService).flushCache();
		schedulerService.flushCache();
		Mockito.verify(clientConfigService, Mockito.times(1)).flushCache();

	}

	@Test
	public void whenExportScheduledItWillDoTheExport() throws Exception {
		Mockito.when(exportService.export()).thenReturn(ApiResponse.ok());
		schedulerService.exportFiles();
		Mockito.verify(exportService, Mockito.times(1)).export();

	}

	
}
