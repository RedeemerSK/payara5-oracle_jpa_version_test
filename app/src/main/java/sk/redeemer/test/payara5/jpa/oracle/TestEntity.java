package sk.redeemer.test.payara5.jpa.oracle;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "TEST_ENTITY")
public class TestEntity {

	@Id
	@Column(name = "ID")
	public String id;
	
	@Column(name = "DATA")
	public String data;
	
	@Version
    @Column(name = "VERSION")
	public Timestamp version;

	public TestEntity() {}
	
	public TestEntity(String data) {
		this.id = UUID.randomUUID().toString();
		this.data = data;
	}
	

}
