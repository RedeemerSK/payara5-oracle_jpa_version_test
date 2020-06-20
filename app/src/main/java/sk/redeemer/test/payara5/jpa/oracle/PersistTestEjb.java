package sk.redeemer.test.payara5.jpa.oracle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
public class PersistTestEjb {
	
	static final Logger LOG = Logger.getLogger(PersistTestEjb.class.getName());
	static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd @ HH:mm:ss.SSSSSSSSS");
	static final String LOG_HEAD = "[payara5-oracle_jpa_version_test] ";
	static final String LOG_VALUE_PREFIX = "@Version field of persisted entity: ";
	static final String LOG_OK = "OK precision of @Version field";
	static final String LOG_NOK = "Lost precision of @Version field !!!";

	
	@PersistenceContext
	private EntityManager em;

	@PostConstruct
	public void init() {
		LocalDateTime time = testPersist();
		LOG.info(LOG_HEAD + LOG_VALUE_PREFIX + FORMAT.format(time));
		
		long nanos = time.getLong(ChronoField.NANO_OF_SECOND);
		LOG.severe(LOG_HEAD + ((nanos == 0) ? LOG_NOK : LOG_OK));
	}
	
	public LocalDateTime testPersist() {
		TestEntity entity = new TestEntity("some_data");
		
		em.persist(entity);
		em.flush();
		
		
		return entity.version.toLocalDateTime();
	}
	
}
