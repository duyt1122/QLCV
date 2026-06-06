package vn.hoidanit.jobhunter.domain.response.file;

import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResUploadFileDTO {
	private String fileName;
	private Instant uploadedAt;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Instant getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(Instant uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public ResUploadFileDTO(String fileName, Instant uploadedAt) {
		super();
		this.fileName = fileName;
		this.uploadedAt = uploadedAt;
	}

}
