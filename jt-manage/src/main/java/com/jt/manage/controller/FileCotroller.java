package com.jt.manage.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;
import com.jt.manage.service.FileService;

@Controller
public class FileCotroller {
	
	@Autowired
	private FileService fileService;
	
	//如果参数需要赋值，需要通过springMVC解析器
	/**
	 * 参数接收，必须和页面提交的 name 属性相同
	 * @param image ---  name
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("/file")
	public String imageFile(MultipartFile image) throws IllegalStateException, IOException{
		//
		File imageFile = new File("D:/JT-UPLOAD");
		//
		if(!imageFile.exists()){
			imageFile.mkdirs();
		}
		//
		String fileName = image.getOriginalFilename();
		//
		image.transferTo(new File("D:/JT-UPLOAD/"+fileName));
		//跳转到index
		return "index";
	}
	
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public PicUploadResult fileUpload(MultipartFile uploadFile){
		return fileService.fileUpload(uploadFile);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}













