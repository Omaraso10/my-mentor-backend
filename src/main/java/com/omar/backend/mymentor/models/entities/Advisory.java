package com.omar.backend.mymentor.models.entities;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="advisorys")
public class Advisory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    private String description;

    @Column(name = "advisory_date", columnDefinition = "TIMESTAMP")
    private LocalDate advisoryDate;

    @Column(name = "advisorys_details", nullable = false)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="advisory_id")
    private List<AdvisoryDetail> advisorysDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_professionals_id")
    private UserProfessional userProfessional;


    @PrePersist
    public void prePersist(){
        advisoryDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AdvisoryDetail> getAdvisorysDetails() {
        return advisorysDetails;
    }

    public void setAdvisorysDetails(List<AdvisoryDetail> advisorysDetails) {
        this.advisorysDetails = advisorysDetails;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDate getAdvisoryDate() {
        return advisoryDate;
    }

    public void setAdvisoryDate(LocalDate advisoryDate) {
        this.advisoryDate = advisoryDate;
    }

    public UserProfessional getUserProfessional() {
        return userProfessional;
    }

    public void setUserProfessional(UserProfessional userProfessional) {
        this.userProfessional = userProfessional;
    }
    
}
