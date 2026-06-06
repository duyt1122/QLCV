package vn.hoidanit.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.hoidanit.jobhunter.domain.response.RestResponse;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestControllerAdvice
public class FormmatRestResponse implements ResponseBodyAdvice<Object> {
// ResponseBodyAdvice chạy trước GlobalException cho nên sẽ ko xử lý đk lỗi mà gửi message lỗi mặc định
	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return true;
	}

	// dễ gây lỗi cannot be cast to ... body đang muốn trả về một object
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		// TODO Auto-generated method stub
		// ServerHttpResponse không cung cấp trực tiếp mã status code (HTTP status) vì
		// nó chưa được set ở thời điểm này.
		// Để lấy hoặc thay đổi status code, bạn phải ép kiểu ServerHttpResponse về
		// ServletServerHttpResponse, rồi lấy HttpServletResponse bên trong.
		HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
		int status = servletResponse.getStatus();
		RestResponse<Object> res = new RestResponse<Object>();
		res.setStatusCode(status);
		// case error
		if (body instanceof String) {
			return body;
		}

		String path = request.getURI().getPath();
		if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
			return body;
		}

		if (status >= 400) {
			return body;
		} else {
			// case success
			res.setData(body);
			ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
			res.setMessage(message != null ? message.value() : "CALL API SUCCESS");

		}
		return res;

	}
}
