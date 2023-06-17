package kr.bit.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileAddController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String UPLOAD_DIR="file_repo";         
		String uploadPath = request.getServletContext().getRealPath("") + File.separator+UPLOAD_DIR;
		File currentDirPath = new File(uploadPath);
		if (!currentDirPath.exists()) {
			currentDirPath.mkdir();
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024 * 1024);

		String fileName = null;

		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> items = upload.parseRequest(request);
			for (int i = 0; i < items.size(); i++) {
				FileItem fileItem = items.get(i);
				if (fileItem.isFormField()) {
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString("utf-8"));
				} else {
					if (fileItem.getSize() > 0) {
						int idx = fileItem.getName().lastIndexOf("\\"); // Window 경로
						if (idx == -1) {
							idx = fileItem.getName().lastIndexOf("/"); // Linux 경로
						}
						fileName = fileItem.getName().substring(idx + 1);
						File uploadFile = new File(currentDirPath + "\\" + fileName);
						if(uploadFile.exists()) {
							fileName=System.currentTimeMillis()+"_"+fileName;
							uploadFile = new File(currentDirPath+"\\"+fileName);
						}
						fileItem.write(uploadFile);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setContentType("text/html;charset=euc-kr");
		response.getWriter().print(fileName);
		return null;
	}

}
