package kr.bit.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileGetController implements Controller {
	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String filename = request.getParameter("filename");

		String UPLOAD_DIR = "file_repo";
		String uploadPath = request.getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
		File f = new File(uploadPath+"\\"+filename);

		// 클라이언트로부터 넘어오는 파일이름이 깨지지 않게
		filename = URLEncoder.encode(filename, "utf-8");
		filename = filename.replace("+", " ");
		
		// 다운로드 준비
		response.setContentLength((int) f.length());
		response.setContentType("application/x-msdownload;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename+";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");

		// 다운로드 진행
		FileInputStream in = new FileInputStream(f);
		OutputStream out = response.getOutputStream();
		byte[] buffer = new byte[104];
		while (true) {
			int count = in.read(buffer);
			if (count == -1) {
				break;
			}
			out.write(buffer, 0, count);
		}
		out.close();
		return null;
	}

}
