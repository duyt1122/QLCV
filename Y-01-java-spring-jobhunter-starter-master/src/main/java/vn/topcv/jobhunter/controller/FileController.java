package vn.topcv.jobhunter.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.topcv.jobhunter.domain.response.file.ResUploadFileDTO;
import vn.topcv.jobhunter.service.FileService;
import vn.topcv.jobhunter.util.annotation.ApiMessage;
import vn.topcv.jobhunter.util.error.StorageException;

@RestController
@RequestMapping("/api/v1")
public class FileController {
	@Value("${topcv.upload-file.base-uri}")
	private String baseUri;

	private final FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	@PostMapping("/files")
	@ApiMessage("upload single file")
	public ResponseEntity<ResUploadFileDTO> upload(@RequestParam(name = "file", required = false) MultipartFile file,
			@RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {

		// Skip validate
		if (file == null || file.isEmpty()) {
			throw new StorageException("file is a empty. please upload a file");
		}
		String fileName = file.getOriginalFilename();
		List<String> allowedExtensions = Arrays.asList("pdf", "png", "jpg", "jpeg", "doc", "docx");
		boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));

		if (!isValid) {
			throw new StorageException("Invalid file extension. only allow " + allowedExtensions.toString());
		}
		// create a directory if not exist
		this.fileService.createDirectory(baseUri + folder);
		// store file

		String uploadFile = this.fileService.store(file, folder);

		ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

		return ResponseEntity.ok().body(res);
	}
}
