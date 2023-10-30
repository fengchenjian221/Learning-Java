package boot.constant;

/**
 * 自定义返回码
 *
 * @author admin
 */

public enum ErrorCode {
    /**
     * 成功
     */
    OK(0, "success"),
    FAIL(1000, "fail"),
    ALERT(1001, "alert"),
    EMPTY(1002,""),

    /**
     * oauth2返回码
     */
    INVALID_TOKEN(2000, "invalid_token"),
    INVALID_SCOPE(2001, "invalid_scope"),
    INVALID_REQUEST(2002, "invalid_request"),
    INVALID_CLIENT(2003, "invalid_client"),
    INVALID_GRANT(2004, "invalid_grant"),
    REDIRECT_URI_MISMATCH(2005, "redirect_uri_mismatch"),
    UNAUTHORIZED_CLIENT(2006, "unauthorized_client"),
    EXPIRED_TOKEN(2007, "expired_token"),
    UNSUPPORTED_GRANT_TYPE(2008, "unsupported_grant_type"),
    UNSUPPORTED_RESPONSE_TYPE(2009, "unsupported_response_type"),
    UNAUTHORIZED(2012, "unauthorized"),
    SIGNATURE_DENIED(2013, "signature_denied"),
    //oauth2 自定义短信验证码模式相关代码
    SMSCODE_ACCOUNTSTATUS_UNABLE(2014, "账号过期、锁定或已禁用"),
    SMSCODE_BADCREDENTIALS(2015, "登录认证失败"),
    SMSCODE_UNAUTHENTICATED(2016, "身份验证失败"),
    SMSCODE_CODE_UNABLE(2017, "验证码无效"),
    SMSCODE_MOBILE_INPUT_ERROR(2018, "手机号码输入有误"),
    SMSCODE_CODE_INPUT_ERROR(2019, "验证码输入有误"),


    ACCESS_DENIED(4030, "access_denied"),
    ACCESS_DENIED_BLACK_LIMITED(4031, "access_denied_black_limited"),
    ACCESS_DENIED_WHITE_LIMITED(4032, "access_denied_white_limited"),
    ACCESS_DENIED_AUTHORITY_EXPIRED(4033, "access_denied_authority_expired"),
    ACCESS_DENIED_UPDATING(4034, "access_denied_updating"),
    ACCESS_DENIED_DISABLED(4035, "access_denied_disabled"),
    ACCESS_DENIED_NOT_OPEN(4036, "access_denied_not_open"),
    /**
     * 账号错误
     */
    BAD_CREDENTIALS(3000, "bad_credentials"),
    ACCOUNT_DISABLED(3001, "account_disabled"),
    ACCOUNT_EXPIRED(3002, "account_expired"),
    CREDENTIALS_EXPIRED(3003, "credentials_expired"),
    ACCOUNT_LOCKED(3004, "account_locked"),
    USERNAME_NOT_FOUND(3005, "username_not_found"),

    /**
     * 请求错误
     */
    BAD_REQUEST(4000, "bad_request"),
    NOT_FOUND(4004, "not_found"),
    METHOD_NOT_ALLOWED(4005, "method_not_allowed"),
    MEDIA_TYPE_NOT_ACCEPTABLE(4006, "media_type_not_acceptable"),
    TOO_MANY_REQUESTS(4029, "too_many_requests"),
    /**
     * 系统错误
     */
    ERROR(5000, "error"),
    GATEWAY_TIMEOUT(5004, "gateway_timeout"),
    SERVICE_UNAVAILABLE(5003, "service_unavailable"),
    ACCOUNT_DEPEND(5005, "当前账号存在依赖"),
    DB_CREATE_ERROR(5006, "创建多租户-执行sql出现问题"),

    /**
     * 电子证照业务返回码
     */
    SUCCESS(10000, "请求成功"),
    SYSTEM_ERROR(-1, "系统错误"),
    PARAM_ERROR(10001, "必填参数为空"),
    REQUEST_ERROR(10002, "请求错误"),
    SEND_SMS_ERROR(10003, "短信发送异常"),
    AUTH_FAIL(11000, "认证失败"),
    RESOURCES_NOT_ALLOWED_ACCESSE(11001, "资源不允许访问"),
    REQUEST_RESOURCES_NOT_EXIST(11002, "请求资源不存在"),
    INVAILD_DATA_TYPE(11003, "无效的数据类型"),
    CREDENTIAL_VERIFY_FAIL(12000,"验证失败"),
    CREDENTIAL_EXPIRE_FAIL(12001,"二维码已过期"),
    CREDENTIAL_DATA_ERROR(12002,"二维码数据异常"),
    CREDENTIAL_CREATE_ERROR(12003,"暂未查到您的证书信息"),
    CREDENTIAL_CPT_ERROR(12004,"匹配不到该类型CPT模板，请联系管理员！"),
    CREDENTIAL_UNREVOKE_ERROR(12005,"调用下游存证服务异常"),
    CREDENTIAL_REVOKE_ERROR(12006,"撤销存证异常"),
    CREDENTIAL_DID_ERROR(12007,"该用户不存在DID,请先创建DID"),
    CREDENTIAL_ADD_ERROR(12008,"证照已存在,请勿重复添加"),
    CREDENTIAL_DOCTOR_QR_CODE_DATA_ERROR(12009,"医师证照二维码数据信息异常"),
    CREDENTIAL_QR_CODE_REDIS_DATA_ERROR(12010,"医师证照二维码缓存数据缺失异常"),
    CREDENTIAL_QR_CODE_RANDOM_CODE_DATA_ERROR(12011,"医师证照二维码验证码数据异常"),
    CREDENTIAL_QR_CODE_SECRET_CODE_DATA_ERROR(12012,"医师证照二维码加密数据异常"),
    CREDENTIAL_DEL_CREATE_ERROR(12013,"非法操作，请检查是否存在此证照！"),
    CREDENTIAL_EXPIRED_ERROR(12014,"暂未查到您的证书信息"),
    PUBLIC_KEY_EXPIRED_ERROR(12015,"secureId失效或不正确"),
    USER_HASH_ERROR(12016,"USER HASH与实际业务数据不符"),
    CREDENTIAL_SOURCE_UNUSABLE(12017,"认证失败"),
    CREDENTIAL_SOURCE_AVAILABLE(12018,"认证成功"),
    LOGIN_INFO_FAIL(20028, "帐号或密码错误"),
    /**
     * IM业务返回码
     */
    COMMUNITY_API_SUCCESS(200, "success"),
    COMMUNITY_USER_EXIST_ERROR(13000, "社区服务用户不存在"),

    /**
     * 权限返回码
     */
    UNAUTHORIZED_OF_DELETE(403, "访问受限，未授权的请求"),
    NOT_EXISTED(404, "{}接口不存在"),
    TIMEOUT(504, "{}服务超时"),
    MISSING_PARAM(505, "缺少参数{}"),
    CLASS_CAST_EXCEPTION(506, "类转换异常"),
    FAILURE(1000, "failure"),
    EXCEPTION(-1, "系统异常"),
    PARAM_IS_INVALID(10001, "{}参数无效"),
    PARAM_IS_BLANK(10002, "{}参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "{}参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "{}参数缺失"),
    PARAM_NOT_NULL(10005, "参数不能为空"),
    PARAM_READABLE_ERR(10006, "请求参数不完整或不正确：【%s】"),
    USER_NOT_LOGGED_IN(20001, "用户{}未登录"),
    USER_LOGIN_ERROR(20002, "帐号{}不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(20003, "帐号{}已被禁用"),
    USER_NOT_EXIST(20004, "用户{}不存在"),
    USER_HAS_EXISTED(20005, "用户{}已存在"),
    USER_NOT_MAIN_GROUP(20006, "用户{}未设置主岗位"),
    USER_IS_EXPIRE(20007, "用户{}已过期"),
    USER_IS_NOT_VALID_DATE(20008, "用户{}不在有效期内"),
    USER_IS_NOT_VALID(20009, "用户{}无效"),
    USER_ACCOUNT_LOCKED(20010, "帐号{}已被锁定"),
    USER_CREDENTIALS_EXPIRED(20011, "用户{}凭证已过期"),
    USER_NOT_BIND_OPENID(20012, "此用户未绑定内部帐号"),
    PASSWORD_NOT_VALID(20013, "密码设置不符合规则，请重新设置"),
    REGISTER_FAIL(20014, "app注册失败"),
    CODE_ERROR(20015, "短信验证码错误"),
    CODE_ERROR_MAX(20115, "验证码已失效，请点击重新发送"),
    ID_CARD_ERROR_DESC(20016, "证件号码错误，请重新输入"),
    TRANS_ID_NOT_EXIST(20017, "事务ID不存在"),
    FLOW_ERROR(20018, "注册流程不正确，请检查"),
    PHONE_REGEX_ERROR(20019, "手机号码错误，请重新输入"),
    CODE_NULL_ERROR(20020, "验证码不能为空"),
    VERIFY_CODE_TYPE_ERROR(20021, "验证码类型不正确"),
    REALNAME_DOES_NOT_MATCH(20022, "身份证号码和姓名不一致，请重新输入"),
    VERIFY_FACEID_FAIL(20023, "本次人脸识别失败，请再次进行人脸识别"),
    CODE_NOT_FOUND(20024, "短信验证码已失效"),
    Valid_FACE_FAIL(20025, "填写的用户信息和注册流程中填写的信息不一致，请检查"),
    ACCOUNT_FAIL(20026, "帐号格式错误，请检查是否为手机号或身份证"),
    IDCARD_FAIL(20027, "身份证号码错误，请重新输入"),
    USER_HAS_EXISTED2(20030, "该证件已注册，请直接登录"),
    TOKEN_NOT_VALID(20031, "token无效"),
    UUID_NOT_EXIST(20032, "UUID不存在"),
    SCAN_ERROR(20033, "扫码登录流程错误"),
    OAuth_LOGIN_FAIL(20034, "OAuth登录失败"),
    EXPIRED_ERROR(506,"用户密码已过期"),

    // 数字身份设置验证
    UPDATE_PASSWORD_COUNT_MAX(20034, "数字身份认证密码错误次数过多，请3小时后再试"),
    PASSWORD_SAME(20035, "新旧数字身份认证密码相同，请重新设置"),
    UPDATE_PASSWORD_COUNT(20036, "数字身份认证密码不正确,您还可以输入{}次"),
    UPDATE_PASSWORD_ERROR(20037, "数字身份认证密码不正确,请重新输入"),
    // 用在开处方验证
    UPDATE_PASSWORD_COUNT_MAX2(20038, "身份校验失败，请重新扫码验证！"),
    UPDATE_PASSWORD_COUNT2(20040, "数字身份认证密码不正确,您还可以输入{}次"),
    UPDATE_PASSWORD_ERROR2(20041, "数字身份认证密码不正确"),
    // 用在取药时验证
    UPDATE_PASSWORD_COMMON_ERROR(20042, "身份校验失败，请重新输入"),


    BUS_ERROR(30001, "{}业务出现问题"),
    BUS_FILE_UPLOAD(30002, "附件上传失败"),
    SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试"),
    RESULE_DATA_NONE(50001, "{}数据未找到"),
    DATA_IS_WRONG(50002, "{}数据有误"),
    DATA_ALREADY_EXISTED(50003, "{}数据已存在"),
    DATA_IS_UNIQUE(50004, "{}违反数据唯一性校验"),
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统{}接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统{}接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该{}接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址{}无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "{}接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "{}接口负载过高"),
    PERMISSION_NO_ACCESS(70001, "{}无访问权限"),
    PERMISSION_NO_TOKEN(70002, "token无效"),
    PERMISSION_ERROR(70003, "访问认证的资源{}时，用户{}没有相关凭证：未登录、token失效"),
    PERMISSION_REFRESH(70004, "刷新token"),
    DOCTOR_TOKEN_NULL(80001, "查询医师token不存在"),
    DOCTOR_INFO_NULL(80002, "查询医师不存在"),
    DOCTOR_IDENTITY_ERROR(80003, "医师身份不存在，无法开具处方"),
    DOCTOR_CREDENTIAL_ERROR(80004, "查询医师证书不存在"),
    DOCTOR_IDENTITY_FAIL(80005, "身份校验失败，请确认！"),
    DOCTOR_IDENTITY_TRANSFER_FAIL(80006, "身份证书转换失败"),
    DOCTOR_AUTHOR_FAIL(80007, "个人身份无法登录，请核对后重试！"),
    SCAN_LOGIN_CANCEL(80008, "取消登录"),
    APPROVE_NOT_FOUND(80009, "审批记录不存在"),
    APOTHECARY_NOT_FOUND(80010, "没有对应的药师身份"),
    AUTH_NOT_HAS(80011, "用户未被分配该角色，无法切换"),
    DOMAIN_FAIL(80012, "域名上链失败"),
    PRACTICECOMPANY_MATCH_FAIL(80013, "请匹配执业单位"),
    DOCTOR_IDENTITY_WRONG(80014, "身份校验失败，请确认！"),
    WEID_NOT_FOUND(80015, "没有找到该用户WeId信息"),
    SORT_HAS_EXITS(90001, "顺序为{}的分类已存在，请勿重复添加"),
    OFFICE_NAME_HAS_EXITS(90002, "{}科室已存在，请重新输入"),
    BLOCK_CHAIN_FAIL(90003, "处方上链失败"),
    OFFICE_IN_USED_E(90004, "有医院或者医生正在使用，无法编辑"),
    OFFICE_IN_USED_D(90005, "有医院或者医生正在使用，无法删除"),
    CONTENT_MANAGE_HAS_ALL_INDEXED(90006, "您选择的资讯已在APP首页显示，再次操作将覆盖之前的内容"),
    CONTENT_NEWS_HAS_ALL_INDEXED(90007, "您选择的动态已在APP首页显示，再次操作将覆盖之前的内容"),
    CONTENT_MANAGE_HAS_PART_INDEXED(90008, "您选择的资讯包含已在APP首页显示中的资讯，重复添加将覆盖之前的内容。可选择【全部添加】、【添加新资讯】操作，【全部添加】后覆盖历史更新到最新；【添加新资讯】仅添加还未在首页显示的数据"),
    CONTENT_NEWS_HAS_PART_INDEXED(90009, "您选择的动态包含已在APP首页显示中的动态，重复添加将覆盖之前的内容。可选择【全部添加】、【添加新动态】操作，【全部添加】后覆盖历史更新到最新；【添加新动态】仅添加还未在首页显示的数据"),
    DOCTOR(90010, "{}中还有必填项需要完善"),
    CONTENT_IN_USED_D(90011, "有资讯正在使用，无法删除"),
    DOCTOR_INFO_INVALID(90012, "请完善必填项信息"),

    ////////////////////////////////////////
    ROLE_NO_AUTHORIZATION(1001, "当前角色无操作权限"),
    PRES_STATUS_EXPIRE(555, "处方已失效，无法购买药品。"),

    //////////////// 处方 /////////////////
    SET_OFF_DUTY_ERR(5001, "已停止接单，但您当前还存有处方未审核，请处理"),
    AUDIT_REASON_IS_NULL(5002, "审核不通过，请填写原因"),
    PRES_NOT_FOUND(5003, "处方不存在"),
    PRES_STATE_EXCEPTION(5004, "当前处方状态不支持此操作"),
    PRES_NOT_BELONG_TO_USER(5005, "处方不属于当前用户"),
    PHARMACY_NO_APOTHECARY(5006, "药房未绑定任何药师"),
    PRES_IS_EXPIRED(5007, "处方已过期"),
    NO_AUTH_AUDIT(5008, "无权审核该处方"),
    POINT_ALARM(5009, "该药房已被暂停执业，请重新选择药房"),

    //////////////// 查询处方状态返回码 /////////////////
    PRES_STATUS_QUREY_SUCCESS(200, "处方状态查询成功"),
    PRES_INTERNAL_ERROR(50001, "系统异常"),
    PRES_HOSPITAL_NOT_EXIST(50002, "互联网医院不存在"),
    PRES_DECODE_ERROR(50003, "数据解密失败"),
    PRES_PARAM_ERROR(50004, "参数不完整或不正确"),
    PRES_NOT_EXIST(50005, "处方不存在或医师无权限查看该处方"),
    PRES_DOCTER_NOT_EXIST(50006, "医师不存在"),
    PRES_DOCTER_CHECK_ERROR(50006, "医师姓名和身份证不匹配"),

    ///////////////医师身份校验返回码，返回给APP//////////////
    DOCTOR_AUTH_SUCCESS(200, "医师身份校验成功"),
    DOCTOR_AUTH_FAIL(40000, "医师身份校验失败"),
    DOCTOR_AUTH_INTERNAL_ERROR(40001, "系统异常"),
    DOCTOR_AUTH_PARAM_ERROR(40002, "参数不完整或不正确"),
    DOCTOR_AUTH_DECODE_ERROR(40003, "数据解密失败"),
    DOCTOR_AUTH_URL_ERROR(40004, "回调URL失败"),
    DOCTOR_AUTH_HOSPITAL_NOT_EXIST(40005, "互联网医院不存在"),

    PARAMS_NOT_MAX(100001, "{}长度不能超过{}"),

    // 200000 自定义错误码,已被使用, 请注意！！！！！！！！！！！！
    SENSITIVE_WORD_ERROR(20000,"您提交的文本存在敏感字符，请重新输入！"),
    VERIFY_INVITATION_EXPIRE(200001,"邀请链接已过期"),
    VERIFY_INVITATION_COUNT(200002,"加入圈子次数已用完，请联系管理员");

    private int code;
    private String message;

    ErrorCode() {
    }

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCode getResultEnum(int code) {
        for (ErrorCode type : ErrorCode.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return EMPTY;
    }

    public static ErrorCode getResultEnum(String message) {
        for (ErrorCode type : ErrorCode.values()) {
            if (type.getMessage().equals(message)) {
                return type;
            }
        }
        return EMPTY;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
