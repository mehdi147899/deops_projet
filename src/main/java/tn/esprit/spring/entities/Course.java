package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Course implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long numCourse;
	int level;
	@Enumerated(EnumType.STRING)
	TypeCourse typeCourse;
	@Enumerated(EnumType.STRING)
	Support support;
	Float price;
	int timeSlot;

	@JsonIgnore
	@OneToMany(mappedBy = "course")
	transient Set<Registration> registrations; // Marked as transient // No need to change this if Registration is Serializable
}
