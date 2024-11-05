package tn.esprit.spring.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
@Entity
public class Subscription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_sub") // Maps to num_sub in the database
    Long numSub;

    @Column(name = "start_date") // Maps to start_date in the database
    LocalDate startDate;

    @Column(name = "end_date") // Maps to end_date in the database
    LocalDate endDate;

    @Column(name = "price") // Maps to price in the database
    Float price;

    @Column(name = "type_sub") // Maps to type_sub in the database
    TypeSubscription typeSub;
}

