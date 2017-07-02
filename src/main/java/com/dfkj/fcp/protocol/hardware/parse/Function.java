package com.dfkj.fcp.protocol.hardware.parse;

/**
 *
 * @param <T1>
 * @param <T2>
 * @param <R>
 */
public interface Function<T1, T2, R> {
	R apply(T1 t1, T2 t2);
}
