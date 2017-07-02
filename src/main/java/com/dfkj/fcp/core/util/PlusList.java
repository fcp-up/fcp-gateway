package com.dfkj.fcp.core.util;

import java.util.ArrayList;

import com.dfkj.fcp.protocol.hardware.parse.Function;

/**
 * 运算工具类
 *
 * @param <E>
 */
public class PlusList<E> extends ArrayList<E> {

	private static final long serialVersionUID = 1L;

	public <T> PlusList<E> where(Function<E, T, Boolean> where, T target) {
		PlusList<E> result = new PlusList<E>();
		for (E item : this) {
			if (where.apply(item, target)) {
				result.add(item);
			}
		}
		return result;
	}

	public <T> E single(Function<E, T, Boolean> where, T target) {
		for (E item : this) {
			if (where.apply(item, target)) {
				return item;
			}
		}
		return null;
	}

	public <T> PlusList<E> delete(Function<E, T, Boolean> where, T target) {
		int size = this.size();
		for (int idx = 0; idx < size;) {
			E item = this.get(idx);
			if (where.apply(item, target)) {	//	find
				this.remove(idx);
				size -= 1;
			}
			else {
				idx += 1;
			}
		}
		return this;
	}
}
