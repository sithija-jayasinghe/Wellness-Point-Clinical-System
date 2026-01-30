package edu.icet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrescriptionDto {
    private Long prescriptionId;
    private Long consultationId;
    private LocalDate issuedDate;
    private List<PrescriptionItemDto> prescriptionItems;

    @JsonProperty("consultation")
    private void unpackConsultation(Map<String, Object> consultation) {
        if (consultation != null && consultation.get("consultationId") != null) {
            Object idVal = consultation.get("consultationId");
            if (idVal instanceof Number) {
                this.consultationId = ((Number) idVal).longValue();
            }
        }
    }
}
