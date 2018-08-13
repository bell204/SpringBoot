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

		// 실제로 파일 업로드 하는 함수는 uploadFile
		String savedName = uploadFile(file.getOriginalFilename(), file.getBytes());
		model.addAttribute("savedName", savedName);

	}

	@RequestMapping(value = "/uploadAjax", method = RequestMethod.GET)
	public void uploadAjax() {
	}

	// 한국어를 정상적으로 전송하기 위한 설정인 produces 
	@ResponseBody
	@RequestMapping(value = "/uploadAjax", method = RequestMethod.POST, 
	produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> uploadAjax(MultipartFile file) throws Exception {

		logger.info("originalName: " + file.getOriginalFilename());

		// 이미지 파일의 경우 파일이 2개 저장됨.
		// 보통 파일은 파일이 1개 저장됨.
		
		// 파일 업로드 위해 UploadFileUtils 클래스 사용
		return new ResponseEntity<>(UploadFileUtils.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes()),
				HttpStatus.CREATED); // 원하는 리소스가 정상적으로 생성되었다는 상태코드 // HttpStatus.CREATED
	}

	
	// 이 부분이 아직 미완성
	@ResponseBody // 리스폰스 바디 명시해서 byte[] 데이터가 그대로 전송될 것임을 명시함.
	@RequestMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String fileName) throws Exception {

		InputStream in = null;
		ResponseEntity<byte[]> entity = null;

		logger.info("FILE NAME: " + fileName);

		try {
			// 확장자 추출
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
			
			// 이미지 파일이면 mType 리턴
			MediaType mType = MediaUtils.getMediaType(formatName);
			
			
			HttpHeaders headers = new HttpHeaders();
			in = new FileInputStream(uploadPath + fileName);

			if (mType != null) {
				
				// 이미지 파일이면 적절한 MIME 타입 지정
				headers.setContentType(mType);

			} else {
				
				fileName = fileName.substring(fileName.indexOf("_") + 1);
			
				// 이미지가 아닌 경우는 MIME 타입을 다운로드용으로 사용되는 APPLICATION_OCTET_STREAM로 지정.
				// 브라우저는 이 MIME 타입을 보고 사용자에게 자동으로 다운로드 창 열어줌.
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

				
				// 사용자에게 보여야 하므로 한글처리해서 파일 전송, 깨지지 않게.
				headers.add("Content-Disposition",
						"attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
			}
			
			// 실제 데이터를 읽는 부분은 IOUtils.toByteArray(in) 이 부분임.
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
		// 파일의 확장자 추출
		
		logger.info("delete file"+fileName);
		
		String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
		// 이미지 파일 여부 검사
		MediaType mType = MediaUtils.getMediaType(formatName);
		// 이미지 파일의 경우(썸네일 + 원본 파일 삭제), 이미지 파일이 아닌 경우 원본 파일만 삭제
		// 이미지 파일이면
		if (mType != null) {
			// 썸네일 이미지 파일 추출
			String front = fileName.substring(0, 12);
            String end = fileName.substring(14);
            
            // 썸네일 이미지 삭제
            new File(uploadPath + (front + end).replace('/', File.separatorChar)).delete();
		}
		// 원본 파일 삭제
		new File(uploadPath + fileName.replace('/', File.separatorChar)).delete();
		
		// 데이터와 HTTP 상태 코드 전송
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
		
		//중복되지 않는 고유한 키 값을 설정할 때 사용.
		UUID uid = UUID.randomUUID();
		String savedName = uid.toString() + "_" + originalFilename;
		File target = new File(uploadPath, savedName);
		
		// 실제 파일 처리는 FileCopyUtils가 함.
		// 데이터가 담긴 바이트의 배열을 파일에 기록한다.
		FileCopyUtils.copy(fileData, target);
		return savedName;

	}
}