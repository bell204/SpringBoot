function checkImageType(fileName){
	
	var pattern = /jpg|gif|png|jpeg/i;
	
	return fileName.match(pattern);

}

function getFileInfo(fullName){
		
	var fileName,imgsrc, getLink;
	
	var fileLink;
	
	if(checkImageType(fullName)){
		imgsrc = "/displayFile?fileName="+fullName;
		console.log("getFileInfo : imgsrc : " + imgsrc);
		
		fileLink = fullName.substr(14);
		console.log("getFileInfo : fileLink : " + fileLink);
		
		var front = fullName.substr(0,12); 
		console.log("getFileInfo : front : " + front);
		
		var end = fullName.substr(14);
		console.log("getFileInfo : end : " + end);
		
		getLink = "/displayFile?fileName="+front + end;
		console.log("getFileInfo : getLink : " + getLink);
		
	}else{
		imgsrc ="/resources/dist/img/file.png";
		fileLink = fullName.substr(12);
		
		getLink = "/displayFile?fileName="+fullName;
		
	}
	fileName = fileLink.substr(fileLink.indexOf("_")+1);
	console.log("getFileInfo : fileName : " + fileName);
	
	return  {fileName:fileName, imgsrc:imgsrc, getLink:getLink, fullName:fullName};
	
}