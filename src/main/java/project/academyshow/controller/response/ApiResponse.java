package project.academyshow.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse<T> {
    private int code;
    private int count;
    private String msg;
    private T data;

    public static final ApiResponse DELETE_SUCCESS_RESPONSE =
            new ApiResponse(200, "삭제에 성공했습니다", null);

    public static final ApiResponse SUCCESS_NO_DATA_RESPONSE =
            new ApiResponse(200, "성공했씁니다.", null);


    public ApiResponse(int code, String msg, T data) {
        if (data != null)
            this.count = data instanceof List ? ((List<?>) data).size() : 1;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    public static <T> ApiResponse<T> authenticateFailed() {
        return new ApiResponse<>(401, "자격증명에 실패하였습니다.", null);
    }

    public static <T> ApiResponse<T> resourceNotFound() {
        return new ApiResponse<>(404, "리소스를 찾을 수 없습니다.", null);
    }
}
