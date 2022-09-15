package com.hoaxify.ws.entities;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;	
import lombok.Data;

@Data
@Entity
public class FileAttachment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "NAME")
	private String name;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE")
	private Date date;
	
	private String fileType;
	
	@OneToOne
	private Hoax hoax;
	
	
	
}
