package com.example.userservice.model;

import com.example.userservice.enums.DocumentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pet_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetDocument extends BaseEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pet_id", nullable = false)
	private Pet pet;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "document_type", nullable = false, length = 32)
	private DocumentType documentType;
	
	@Column(name = "file_id", nullable = false, length = 128)
	private String fileId;
	
	@Column(name = "title", length = 160)
	private String title;
	
	@Column(name = "notes", columnDefinition = "TEXT")
	private String notes;
}