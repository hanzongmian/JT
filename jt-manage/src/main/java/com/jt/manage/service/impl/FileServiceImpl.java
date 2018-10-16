package com.jt.manage.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;
import com.jt.manage.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Value("${image.locaPath}")
	private String  loadPath;       //="D:/JT-UPLOAD/";
	@Value("${image.urlPath}")
	private String urlPath ;        //= "http://image.jt.com/";
	/**
	 * 判断图片类型 jpg/png/git 
	 * 判断是否为恶意程序 
	 * 为了提高检索效率 将文件分文件存储
	 * 	  -UUID：hash随机算法（当前毫秒值+算法+hash）=32位hash值
	 * 如何杜绝文件名重复   - UUID+随机数(100).jpg
	 * 文件上传
	 */
	@Override
	public PicUploadResult fileUpload(MultipartFile uploadFile) {
		PicUploadResult uploadResult= new PicUploadResult();
		
		
		//判断图片类型
		String fileName = uploadFile.getOriginalFilename();
		fileName=fileName.toLowerCase();
		if(!fileName.matches("^.*\\.(jpg|png|gif)$")){
			uploadResult.setError(1);
			return uploadResult;
		}
		//判断是否为恶意程序
		try {
			BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
			//图片宽、高值
			int height=bufferedImage.getHeight();
			int width=bufferedImage.getWidth();
			if(height==0 || width==0){
				uploadResult.setError(1);
				return uploadResult;	
			}
			//文件进行分文件储存yyyy/MM/dd
			String datePath = 
					new SimpleDateFormat("yyyy/MM/dd").format(new Date());
			//定义文件保存路径
			String dirPath = loadPath + datePath ;
			//判断文件夹是否存在
			File dirfile = new File(dirPath);
			if(!dirfile.exists()){
				dirfile.mkdirs();
			}
			//动态生成文件名UUID+随机数
			String uuid = UUID.randomUUID().toString().replace("-", "");
			int randomNum = new Random().nextInt(1000);
			String imageFileType = 
					fileName.substring(fileName.lastIndexOf("."));
			String imageFileName = uuid+randomNum+imageFileType;
			//文件上传
			String imageLocalPath = dirPath+"/"+imageFileName;
			uploadFile.transferTo(new File(imageLocalPath));
			//参数封装
			uploadResult.setHeight(height+"");
			uploadResult.setWidth(width+"");
			System.out.println("文件上传成功");
			
			//虚拟路径  urlPath-"http://img.jt.com";
			String imgUrlPath =  urlPath + datePath +"/"+ imageFileName;
			uploadResult.setUrl(imgUrlPath);
			
		} catch (IOException e) {
			e.printStackTrace();
			uploadResult.setError(1);
			return uploadResult;
		}
		
		
		
		return uploadResult;
	}

}
