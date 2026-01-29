package edu.icet.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "clinic")
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clinicId;

    private String name;
    private String address;
    private String phone;

    private String status;
}