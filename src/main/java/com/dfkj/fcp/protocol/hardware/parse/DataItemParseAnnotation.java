package com.dfkj.fcp.protocol.hardware.parse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dfkj.fcp.core.constant.EDeviceType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DataItemParseAnnotation {

	public int dataTypeId() default -1;
	public int dataLength() default 0;
	public double multiple() default 1.0;
	public EDeviceType deviceType() default EDeviceType.UNKNOWN;

}
