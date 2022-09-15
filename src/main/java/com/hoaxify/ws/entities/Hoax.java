package com.hoaxify.ws.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import lombok.Data;

@Entity
@Data
@Table(name = "HOAX")
public class Hoax {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	@Size(min = 1, max = 1000)
	@Column(name = "CONTENT")
	private String content;
		
	@CreationTimestamp // Bu annotation ile hoax eklerken date için new Date set etmemize gerek yok.
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP")
	private Date timestamp;
	
	@ManyToOne
	private User user;
	
	// FileAttachment ile Hoax arasında çift yönlü bağ kurduk fakat Hoax tablosunda FileAttachment sütunu bizim için gerekli değil.
	// FileAttachment içinde hoax bizim için yeterli. Yani Hem çift bağ kurduk hemde tekil Id ile işimizi yapacağız.
	@OneToOne(mappedBy = "hoax", cascade = CascadeType.REMOVE)
	private FileAttachment fileAttachment;
	
	// Hoax silinirse diye orphanRemoval = true ile FileAttachment de silebiliriz.
	// 2.yöntem cascade = CascadeType.REMOVE ilede silebiliriz.
}
