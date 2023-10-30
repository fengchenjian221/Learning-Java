package boot.module;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import boot.constant.ErrorCode;
import com.google.common.collect.Maps;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Map;
import java.util.ResourceBundle;

/**
* @author
 *
 */
    @ApiModel(value = "响应结果")
    public class ResultBody<T> implements Serializable {
        private static final long serialVersionUID = -6190689122701100762L;

        /**
         * 响应编码
         */
        @ApiModelProperty(value = "响应编码:0-请求处理成功")
        private int code = 0;
        /**
         * 提示消息
         */
        @ApiModelProperty(value = "提示消息")
        private String message;

        /**
         * 提示消息
         */
        @ApiModelProperty(value = "提示消息")
        private String msg;

        /**
         * 请求路径
         */
        @ApiModelProperty(value = "请求路径")
        private String path;

        /**
         * 响应数据
         */
        @ApiModelProperty(value = "响应数据")
        private T data;

        /**
         * http状态码
         */
        private int httpStatus;

        /**
         * 附加数据
         */
        @ApiModelProperty(value = "附加数据")
        private Map<String, Object> extra;

        /**
         * 响应时间
         */
        @ApiModelProperty(value = "响应时间")
        private long timestamp = System.currentTimeMillis();

        public ResultBody() {
            super();
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String getMsg() {
            return msg;
        }

        public String getPath() {
            return path;
        }

        public T getData() {
            return data;
        }

        public Map<String, Object> getExtra() {
            return extra;
        }

        public long getTimestamp() {
            return timestamp;
        }

        @JSONField(serialize = false, deserialize = false)
        @JsonIgnore
        public int getHttpStatus() {
            return httpStatus;
        }

        @JSONField(serialize = false, deserialize = false)
        @JsonIgnore
        public boolean isOk() {
            return this.code == ErrorCode.OK.getCode();
        }


        public static ResultBody ok() {
            return new ResultBody().code(ErrorCode.OK.getCode()).msg(ErrorCode.OK.getMessage());
        }

        public static ResultBody failed() {
            return new ResultBody().code(ErrorCode.FAIL.getCode()).msg(ErrorCode.FAIL.getMessage());
        }

        public static ResultBody failed(String msg) {
            return new ResultBody().code(ErrorCode.FAIL.getCode()).msg(msg);
        }

        public static ResultBody define(int code,String message) {
            return new ResultBody().code(code).msg(message);
        }


        public static ResultBody ok(Object data) {
            return new ResultBody().code(ErrorCode.OK.getCode()).msg(ErrorCode.OK.getMessage()).data(data);
        }
        public static ResultBody failed(ErrorCode errorCode) {
            return new ResultBody().code(errorCode.getCode()).msg(errorCode.getMessage());
        }
        public static ResultBody success(Object data) {
            return new ResultBody().code(ErrorCode.COMMUNITY_API_SUCCESS.getCode()).msg(ErrorCode.COMMUNITY_API_SUCCESS.getMessage()).data(data);
        }

        public ResultBody code(int code) {
            this.code = code;
            return this;
        }

        public ResultBody msg(String message) {
            this.message = i18n(ErrorCode.getResultEnum(this.code).getMessage(), message);
            this.msg = i18n(ErrorCode.getResultEnum(this.code).getMessage(), message);
            return this;
        }

        public ResultBody data(T data) {
            this.data = data;
            return this;
        }

        public ResultBody path(String path) {
            this.path = path;
            return this;
        }

        public ResultBody httpStatus(int httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ResultBody put(String key, Object value) {
            if (this.extra == null) {
                this.extra = Maps.newHashMap();
            }
            this.extra.put(key, value);
            return this;
        }

        @Override
        public String toString() {
            return "ResultBody{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ", msg='" + msg + '\'' +
                    ", path='" + path + '\'' +
                    ", data=" + data +
                    ", httpStatus=" + httpStatus +
                    ", extra=" + extra +
                    ", timestamp=" + timestamp +
                    '}';
        }

        /**
         * 错误信息配置
         */
        @JSONField(serialize = false, deserialize = false)
        @JsonIgnore
        private static ResourceBundle resourceBundle = ResourceBundle.getBundle("error");

        /**
         * 提示信息国际化
         *
         * @param message
         * @param defaultMessage
         * @return
         */
        @JSONField(serialize = false, deserialize = false)
        @JsonIgnore
        private static String i18n(String message, String defaultMessage) {
            return resourceBundle.containsKey(message)?resourceBundle.getString(message):defaultMessage;
        }



}
