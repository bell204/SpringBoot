package org.zerock.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

public class UploadFileUtils {

	private static final Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);

	public static String uploadFile(String uploadPath, String originalName, byte[] fileData) throws Exception {

		UUID uid = UUID.randomUUID();
		String savedName = uid.toString() + "_" + originalName;
		
		// 저장될 경로 계산
		String savedPath = calcPath(uploadPath);
		
		
		File target = new File(uploadPath + savedPath, savedName);

		// 원본 파일 저장하는 부분
		FileCopyUtils.copy(fileData, target);
		
		// 원본 파일의 확장자 의미.
		String formatName = originalName.substring(originalName.lastIndexOf(".") + 1);
		
		
		String uploadedFileName = null;

		// formatName으로 이미지 파일인지 아닌지 판단.
		if (MediaUtils.getMediaType(formatName) != null) {
			
			// 이미지 파일이면 썸네일 생성
			uploadedFileName = makeThumbnail(uploadPath, savedPath, savedName);
		} else {
			
			// 이미지 파일 아니면 파일 생성.
			uploadedFileName = makeIcon(uploadPath, savedPath, savedName);
		}
		return uploadedFileName;
	}

	private static String makeIcon(String uploadPath, String path, String fileName) throws Exception {
		String iconName = uploadPath + path + File.separator + fileName;
		return iconName.substring(uploadPath.length()).replace(File.separatorChar, '/');
	}

	
	
	private static String calcPath(String uploadPath) {

		Calendar cal = Calendar.getInstance();
		String yearPath = File.separator + cal.get(Calendar.YEAR);
		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
		String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));
		makeDir(uploadPath, yearPath, monthPath, datePath);
		logger.info(datePath);

		return datePath;
	}

	private static void makeDir(String uploadPath, String... paths) {

		if (new File(paths[paths.length - 1]).exists()) {
			return;
		}

		for (String path : paths) {
			File dirPath = new File(uploadPath + path);
			if (!dirPath.exists()) {
				dirPath.mkdir();
			}
		}
	}

	private static String makeThumbnail(String uploadPath, //  썸네일 생성코드는 imgscalr-lib 라이브러리 사용
			String path, //  
			String fileName //  
	) throws Exception {
		
		// 버퍼드 이미지는 실제 이미지가 아닌 메모리상의 이미지
		BufferedImage sourceImg = ImageIO.read(new File(uploadPath + path, fileName));

		// 썸네일 이미지 파일의 높이를 뒤에 지정된 100px로 동일하게 만들어주는 역할.
		BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);

		// 썸네일은 s로 시작하는 파일명
		String thumbnailName = uploadPath + path + File.separator + "s_" + fileName;

		File newFile = new File(thumbnailName);
		String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);

		ImageIO.write(destImg, formatName.toUpperCase(), newFile);
		
		// '/'로 치환
		return thumbnailName.substring(uploadPath.length()).replace(File.separatorChar, '/');
	}

}
