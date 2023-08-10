package hiFes.hiFes.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import hiFes.hiFes.dto.UpdateMarkerRequest;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="Marker")
public class Marker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="markerId", updatable = false)
    private Long markerId;

    @Column(name="boothName", nullable = false, length = 15)
    private String boothName;

    @Column(name="boothLatitude", nullable = false)
    private BigDecimal boothLatitude;

    @Column(name="boothLongitude", nullable = false)
    private BigDecimal boothLongitude;


    @Column(name="description")
    private String description;

    @Column(name="boothNo", nullable = false)
    private Integer boothNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festivalId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private OrganizedFestival organizedFestival;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "hostId")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Host host;

    @Builder
    public Marker(BigDecimal boothLatitude, BigDecimal boothLongitude, String boothName,
                  String description, Integer boothNo, OrganizedFestival organizedFestival){
        this.boothLatitude =boothLatitude;
        this.boothLongitude = boothLongitude;
        this.boothName = boothName;
        this.description =description;
        this.boothNo = boothNo;
        this.organizedFestival = organizedFestival;
    }

    public void update(UpdateMarkerRequest request){
//        this.markerId =request.getMarkerId();
        this.boothLatitude =request.getBoothLatitude();
        this.boothLongitude = request.getBoothLongitude();
        this.boothName = request.getBoothName();
        this.description = request.getDescription();
        this.boothNo = request.getBoothNo();

//        this.organizedFestival = organizedFestival;

    }
}
