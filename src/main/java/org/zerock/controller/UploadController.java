package org.zerock.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.util.MediaUtils;
import org.zerock.util.UploadFileUtils;
 

@Controller
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@RequestMapping(value = "/uploadForm", method = RequestMethod.GET)
	public void uploadForm() {
	}

	@Resource(name = "uploadPath")
	private String uploadPath;

	@RequestMapping(value = "/uploadForm", method = RequestMethod.POST)
	public void uploadForm(MultipartFile file, Model model) throws Exception {

		logger.info("originalName: " + file.getOriginalFilename());
		logger.info("size: " + file.getSize());
		logger.info("contentType: " + file.getContentType());

		// ������ ���� ���ε� �ϴ� �Լ��� uploadFile
		String savedName = uploadFile(file.getOriginalFilename(), file.getBytes());
		model.addAttribute("savedName", savedName);

	}

	@RequestMapping(value = "/uploadAjax", method = RequestMethod.GET)
	public void uploadAjax() {
	}

	// �ѱ�� ���������� �����ϱ� ���� ������ produces 
	@ResponseBody
	@RequestMapping(value = "/uploadAjax", method = RequestMethod.POST, 
	produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> uploadAjax(MultipartFile file) throws Exception {

		logger.info("originalName: " + file.getOriginalFilename());

		// �̹��� ������ ��� ������ 2�� �����.
		// ���� ������ ������ 1�� �����.
		
		// ���� ���ε� ���� UploadFileUtils Ŭ���� ���
		return new ResponseEntity<>(UploadFileUtils.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes()),
				HttpStatus.CREATED); // ���ϴ� ���ҽ��� ���������� �����Ǿ��ٴ� �����ڵ� // HttpStatus.CREATED
	}

	
	// �� �κ��� ���� �̿ϼ�
	@ResponseBody // �������� �ٵ� ����ؼ� byte[] �����Ͱ� �״�� ���۵� ������ �����.
	@RequestMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String fileName) throws Exception {

		InputStream in = null;
		ResponseEntity<byte[]> entity = null;

		logger.info("FILE NAME: " + fileName);

		try {
			// Ȯ���� ����
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
			
			// �̹��� �����̸� mType ����
			MediaType mType = MediaUtils.getMediaType(formatName);
			
			
			HttpHeaders headers = new HttpHeaders();
			in = new FileInputStream(uploadPath + fileName);

			if (mType != null) {
				
				// �̹��� �����̸� ������ MIME Ÿ�� ����
				headers.setContentType(mType);

			} else {
				
				fileName = fileName.substring(fileName.indexOf("_") + 1);
			
				// �̹����� �ƴ� ���� MIME Ÿ���� �ٿ�ε������ ���Ǵ� APPLICATION_OCTET_STREAM�� ����.
				// �������� �� MIME Ÿ���� ���� ����ڿ��� �ڵ����� �ٿ�ε� â ������.
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

				
				// ����ڿ��� ������ �ϹǷ� �ѱ�ó���ؼ� ���� ����, ������ �ʰ�.
				headers.add("Content-Disposition",
						"attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
			}
			
			// ���� �����͸� �д� �κ��� IOUtils.toByteArray(in) �� �κ���.
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);

		} finally {
			in.close();

		}

		return entity;
	}

	
	@ResponseBody
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	public ResponseEntity<String> deleteFile(String fileName) {
		// ������ Ȯ���� ����
		
		logger.info("delete file"+fileName);
		
		String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
		// �̹��� ���� ���� �˻�
		MediaType mType = MediaUtils.getMediaType(formatName);
		// �̹��� ������ ���(����� + ���� ���� ����), �̹��� ������ �ƴ� ��� ���� ���ϸ� ����
		// �̹��� �����̸�
		if (mType != null) {
			// ����� �̹��� ���� ����
			String front = fileName.substring(0, 12);
            String end = fileName.substring(14);
            
            // ����� �̹��� ����
            new File(uploadPath + (front + end).replace('/', File.separatorChar)).delete();
		}
		// ���� ���� ����
		new File(uploadPath + fileName.replace('/', File.separatorChar)).delete();
		
		// �����Ϳ� HTTP ���� �ڵ� ����
        return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}
	@ResponseBody
	@RequestMapping(value="/deleteAllFiles", method=RequestMethod.POST)
	public ResponseEntity<String> deleteFile(@RequestParam("files[]") String[] files){

		logger.info("delete all files: "+ files);
	
		if(files == null || files.length == 0) {
		  return new ResponseEntity<String>("deleted", HttpStatus.OK);
		}
	
		for (String fileName : files) {
		  String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
		  
		  MediaType mType = MediaUtils.getMediaType(formatName);
		  
		  if(mType != null){      
			
			String front = fileName.substring(0,12);
			String end = fileName.substring(14);
			new File(uploadPath + (front+end).replace('/', File.separatorChar)).delete();
		  }
		  
		  new File(uploadPath + fileName.replace('/', File.separatorChar)).delete();
		  
		}
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	} 
	
	
	private String uploadFile(String originalFilename, byte[] fileData) throws Exception {
		
		//�ߺ����� �ʴ� ������ Ű ���� ������ �� ���.
		UUID uid = UUID.randomUUID();
		String savedName = uid.toString() + "_" + originalFilename;
		File target = new File(uploadPath, savedName);
		
		// ���� ���� ó���� FileCopyUtils�� ��.
		// �����Ͱ� ��� ����Ʈ�� �迭�� ���Ͽ� ����Ѵ�.
		FileCopyUtils.copy(fileData, target);
		return savedName;

	}
}