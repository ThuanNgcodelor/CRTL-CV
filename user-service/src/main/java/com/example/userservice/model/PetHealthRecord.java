package com.example.userservice.model;

import com.example.userservice.enums.HealthEventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "pet_health_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetHealthRecord extends BaseEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pet_id", nullable = false)
	private Pet pet;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "event_type", nullable = false, length = 32)
	private HealthEventType eventType;
	
	@Column(name = "event_date", nullable = false)
	private LocalDate eventDate;
	
	@Column(name = "vet_name", length = 120)
	private String vetName;
	
	@Column(name = "clinic", length = 160)
	private String clinic;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	// Nếu có file minh chứng (ảnh/văn bản) lưu ở file-storage
	@Column(name = "attachment_id", length = 128)
	private String attachmentId;
}