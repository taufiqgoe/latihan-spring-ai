package id.taufiq.latihan.spring_ai.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadedEvent {
    private String bucket;
    private String key;
    private String tenant;
    private String originalFilename;
}
