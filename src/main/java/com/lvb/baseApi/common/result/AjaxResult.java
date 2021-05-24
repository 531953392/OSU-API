/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 ‭‭‭‭‭‭‭‭‭‭‭‭[smallbun] www.smallbun.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.lvb.baseApi.common.result;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

/**
 * AJAX请求返回结果工具类
 *
 * @author SanLi
 * Created by 2689170096@qq.com/SanLi on 2018/5/1
 */

public class AjaxResult extends AbstractResult {

	/**
	 * 返回结果内容 ,为方便返回不同类型，这个使用Object类型
	 */
	private Object data;

	public void setData(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	private AjaxResult(int code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}




	@Override
	public String toString() {
		return "AjaxResult{" + "data=" + data + ", code='" + code + '\'' + ", msg='" + msg + '\'' + '}';
	}

	public static AjaxResultBuilder builder() {
		return new AjaxResultBuilder();
	}

	public static class AjaxResultBuilder {
		private int code;
		private String msg;
		private Object data;

		AjaxResultBuilder() {
		}

		public AjaxResultBuilder code(int code) {
			this.code = code;
			return this;
		}

		public AjaxResultBuilder msg(String msg) {
			this.msg = msg;
			return this;
		}

		public AjaxResultBuilder result(Object data) {
			this.data = data;
			return this;
		}


		public AjaxResult build() {
			if (code==0) {
				code = HttpStatus.OK.value();
			}
			if (StringUtils.isEmpty(msg)) {
				msg = HttpStatus.OK.getReasonPhrase();
			}
			return new AjaxResult(this.code, this.msg, this.data);
		}

		/**
		 * 加密构建
		 * @return
		 */
		public AjaxResult encryptBuild() {
			if (StringUtils.isEmpty(code)) {
				code = HttpStatus.OK.value();
			}
			if (StringUtils.isEmpty(msg)) {
				msg = HttpStatus.OK.getReasonPhrase();
			}
			//进行加密操作

			return new AjaxResult(this.code, this.msg, this.data);
		}

		@Override
		public String toString() {
			return "AjaxResult.AjaxResultBuilder(code=" + this.code + ", msg=" + this.msg + ", data=" + this.data
					+ ")";
		}
	}
}
