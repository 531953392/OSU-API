package com.lvb.baseApi.restful.login.vo;

import lombok.Data;

@Data
public class SendMessageVo {

    public String touser;

    public String template_id;

    public String page;

    public String data;


}
