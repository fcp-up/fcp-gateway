package com.dfkj.fcp.protocol.platform.vo;

import com.dfkj.fcp.core.util.FormatUtil;

/**
 * 设备控制命令
 * 目前不支持all命令，只支持指定设备的控制
 * @author songfei
 * @date 2016-06-22
 */
public class ControllerCommandCmd extends Cmd {

	private String srv;
	private String dev;
	private int devt;
	private boolean all;
	private int act;	//	控制动作 查询（-1）、关闭/停止（0）、打开/启动（1）、暂停（2）、继续（3）、回滚（4）、中断（5）
	private Double vec;
	private Double sum;
	private Double rate;
	private Double time;

	public ControllerCommandCmd() {
		super();
		cmd = 6;
		all = false;
	}

	@Override
	public String toString() {
		return String.format("控制命令 协议号:%d 流水号:%d 指令编号:%d 服务器编号:%s 设备号:%s 设备类型:%d 控制全部:%s "
				+ "动作:%d 控制的向量:%s 控制的总量:%s 控制的速率:%s 控制的时间:%s 时间:%s", 
				ver, idx, cmd, srv, dev, devt, 
				(all) ? "是" : "否",
				act, 
				vec == null ? "" : FormatUtil.DEC_FORMAT.format(vec.doubleValue()),
				sum == null ? "" : FormatUtil.DEC_FORMAT.format(sum.doubleValue()),
				rate == null ? "" : FormatUtil.DEC_FORMAT.format(rate.doubleValue()),
				time == null ? "" : FormatUtil.DEC_FORMAT.format(time.doubleValue()),
				date
			);
	}
	
	public String getSrv() {
		return srv;
	}

	public void setSrv(String srv) {
		this.srv = srv;
	}

	public String getDev() {
		return dev;
	}

	public void setDev(String dev) {
		this.dev = dev;
	}

	public int getDevt() {
		return devt;
	}

	public void setDevt(int devt) {
		this.devt = devt;
	}

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	public int getAct() {
		return act;
	}

	public void setAct(int act) {
		this.act = act;
	}

	public Double getVec() {
		return vec;
	}

	public void setVec(Double vec) {
		this.vec = vec;
	}

	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getTime() {
		return time;
	}

	public void setTime(Double time) {
		this.time = time;
	}
}
