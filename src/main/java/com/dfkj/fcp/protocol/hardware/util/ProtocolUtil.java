package com.dfkj.fcp.protocol.hardware.util;

import com.dfkj.fcp.core.constant.*;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.*;
import com.dfkj.fcp.core.vo.ControllerCommandMessage;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.core.vo.UniversalResponseMessage;
import com.dfkj.fcp.protocol.hardware.parse.IParseGtw1P1;
import com.dfkj.fcp.protocol.hardware.parse.impl.ControllerParseImpl;
import com.dfkj.fcp.protocol.hardware.parse.impl.SensorParseImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ProtocolUtil {

	private final static AcpLogger logger = new AcpLogger(ProtocolUtil.class);
	
	/**
	 * 解包数据
	 * 
	 * @param byteArray
	 * @return
	 */
	public static Message unpack(ByteArray byteArray) {
		Message message = null;

		do {
			if (byteArray.getBeginByte() != (byte) 0x7E || byteArray.getLastByte() != (byte) 0x7E) {
				logger.debug("无效的数据包.");
				break;
			}

			byteArray.removeBeginByte().removeLastByte(); // 掐头去尾
			byteArray.replace(new byte[] { 0x7D, 0x02 }, new byte[] { 0x7E }).replace(new byte[] { 0x7D, 0x01 },
					new byte[] { 0x7D }); // 反转义

			// 因校验规则为异或，所以连同校验位不为0则数据不正确
			if (VerifyUtil.calcVerifyValue(byteArray) != 0) {
				logger.debug("数据包校验未通过.");
				break;
			}
			byteArray.removeLastByte();

			message = new Message(HexUtil.ByteToString(byteArray.toBytes(), " "));

			// 流水号
			message.setSequence((int) (byteArray.getBeginByte() & 0xFF));
			byteArray.removeBeginByte();

			/*
			 *  消息ID(简单设置，并不能保证唯一性，仅仅是为了保存协议中msgid)
			 */
			message.setId(byteArray.getShortAt(0));
			
			//logger.debug(String.format("消息标识[%2X].", byteArray.getAt(8)));
			
			// 消息类型
			message.setMsgType(EMessageTypeFactory(byteArray.getShortAt(0)));
			
			//解决控制器上报问题 2016-09-19 mhz
			if(byteArray.getAt(8) == 0x40){
				message.setMsgType(EMessageType.CTRL_REPORT_STATUS);
			}
			
			byteArray.removeAt(0, 2);
			
			//	应答标志位
			if (message.getMsgType() == EMessageType.UNIVERSAL_RESPONSE) {
				message.setAck(1);
			}

			// TODO 暂不做消息长度的验证
			short messageLength = byteArray.getShortAt(0);
			byteArray.removeAt(0, 2);

			// message.setDeviceNo(HexHelper.LongToString(byteArray.getLongAt(0),
			// "-"));
			// byteArray.removeAt(0, 8);
			message.setDeviceNo(HexUtil.ShortToString(byteArray.getShortAt(0), "-"));
			byteArray.removeAt(0, 2); // 设备ID改为2个字节

			// 消息时间
			message.setRecvMsgDate(new Date());

			// 设备类别
			message.setDevCategory(EDeviceCategoryFactory(message.getMsgType()));

		} while (false);

		return message;
	}

	public static ByteArray pack(Message message) {

		if (!(message instanceof UniversalResponseMessage) && !(message instanceof ControllerCommandMessage)) {
			logger.debug("暂时只支持UniversalResponseMessage或ControllerCommandMessage格式的打包.");
			//return null;
		}

		ByteArray byteArray = null;
		if (message instanceof UniversalResponseMessage) {
			byteArray = createUniversalResponse((UniversalResponseMessage) message);
		} else if (message instanceof ControllerCommandMessage) {
			byteArray = createCtrlActionMessage((ControllerCommandMessage) message);
		}
		else if (message.getMsgType() == EMessageType.GATEWAY_HEARTBEAT) {
			byteArray = createGatewayHeartbeat(message);
		}
		
		if (byteArray == null || byteArray.size() == 0) {
			return null;
		}

		byteArray.append(VerifyUtil.calcVerifyValue(byteArray)); // 添加校验位

		// 转义
		byteArray.replace(new byte[] { 0x7D }, new byte[] { 0x7D, 0x01 }).replace(new byte[] { 0x7E },
				new byte[] { 0x7D, 0x02 });

		byteArray.insertAt(0, (byte) 0x7E);
		byteArray.append((byte) 0x7E);

		logger.debug("下发:\n" + HexUtil.ByteToString(byteArray.toBytes(), " "));
		return byteArray;
	}

	public static Message parse(Message message, ByteArray content) {
		// TODO 简单的进行处理，后期还需要重构
		Message retMessage = message;
		IParseGtw1P1 parse = null;
		if (message.getDevCategory() == EDeviceCategory.CONTROLLER) {
			parse = new ControllerParseImpl();
		} else if (message.getDevCategory() == EDeviceCategory.SENSOR) {
			parse = new SensorParseImpl();
		}

		if (parse != null) {
			retMessage = parse.parse(retMessage, content);
		}

		return retMessage;
	}

	public static EMessageType EMessageTypeFactory(short msgId) {
		int value = (int)(msgId & 0xFFFF);
		
		EMessageType tp = null;
		switch (value) {
			case 0x0000: // 控制器设备的心跳
				tp = EMessageType.CTRL_REPORT_STATUS;
				break;
			case 0x0002:
				tp = EMessageType.HEARTBEAT;
				break;
			case 0x200:
				tp = EMessageType.SENSOR_DATA;
				break;
			case 0xFFF0:
				tp = EMessageType.GATEWAY_HEARTBEAT;
				break;
			case 0x8001:
				tp = EMessageType.UNIVERSAL_RESPONSE;
				break;
			default:
				tp = EMessageType.UNKNOWN;
				break;
		}
		return tp;
	}

	/**
	 * 从MsgType推断出设备的类别（通过协议中的msgid来判断设备的类别）
	 * 
	 * @param msgType
	 * @return
	 */
	public static EDeviceCategory EDeviceCategoryFactory(EMessageType msgType) {
		EDeviceCategory category = EDeviceCategory.COMMON;
		if (msgType == EMessageType.HEARTBEAT) {
			category = EDeviceCategory.COMMON;
		} else if (msgType == EMessageType.SENSOR_DATA) {
			category = EDeviceCategory.SENSOR;
		} else if (msgType == EMessageType.CTRL_REPORT_STATUS) {
			category = EDeviceCategory.CONTROLLER;
		} else if (msgType == EMessageType.GATEWAY_HEARTBEAT) {
			category = EDeviceCategory.GATEWAY;
		}
		return category;
	}

	public static ByteArray createUniversalResponse(UniversalResponseMessage message) {
		ByteArray response = new ByteArray();
		ByteArray body = createUniversalResponseBody(message, message.getRespCode());

		response.append((byte) SequenceUtil.generate()); // 消息流水号

		// 消息ID:0x8001
		response.append((byte) 0x80);
		response.append((byte) 0x01);

		response.appendShort((short) body.size()); // 消息体属性（长度）

		response.append(HexUtil.HexStringToBytes(message.getDeviceNo(), "-")); // 设备ID

		response.append(body); // 消息体

		return response;
	}

	public static ByteArray createUniversalResponseBody(UniversalResponseMessage message, EUniversalResponseCode code) {
		ByteArray response = new ByteArray();
		response.append((byte) message.getRespMsgSequence());
		response.appendShort((short) message.getId());
		response.append((byte) code.getCode());

		// BCD时间
		Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.CHINA);
		nowCal.setTime(message.getRespDate());

		response.append(BCDUtil.ConvertToBCD((byte) (nowCal.get(Calendar.YEAR) % 100)));
		response.append(BCDUtil.ConvertToBCD((byte) (nowCal.get(Calendar.MONTH) + 1)));
		response.append(BCDUtil.ConvertToBCD((byte) (nowCal.get(Calendar.DATE))));
		response.append(BCDUtil.ConvertToBCD((byte) (nowCal.get(Calendar.HOUR_OF_DAY))));
		response.append(BCDUtil.ConvertToBCD((byte) (nowCal.get(Calendar.MINUTE))));
		response.append(BCDUtil.ConvertToBCD((byte) (nowCal.get(Calendar.SECOND))));

		return response;
	}

	public static ByteArray createCtrlActionMessage(ControllerCommandMessage actionMessage) {
		ByteArray content = new ByteArray();

		ByteArray body = createCtrlActionMessageBody(actionMessage);

		content.append((byte)SequenceUtil.generate()); // 消息流水号

		// 消息ID:0x8105
		content.append((byte) 0x81);
		content.append((byte) 0x05);

		content.appendShort((short) body.size()); // 消息体属性（长度）

		byte[] bytes = HexUtil.HexStringToBytes(actionMessage.getDeviceNo(), "-");
		byte[] deviceNo = new byte[2];
		if (bytes != null && bytes.length >= 2) { 
			deviceNo[1] = bytes[bytes.length - 1];
			deviceNo[0] = bytes[bytes.length - 2];
		}
		content.append(deviceNo); // 设备ID

		content.append(body); // 消息体

		return content;
	}

	public static ByteArray createCtrlActionMessageBody(ControllerCommandMessage actionMessage) {
		ByteArray body = new ByteArray();
		
		/**
		 * TODO 当前1.1中的开关只有两种模式，即对应 状态开关，触发开发
		 */
		if (actionMessage.getDevType() == EDeviceType.STATUS_SWITCH) {
			actionMessage.setParamTime(Integer.MAX_VALUE);
		}
		
		/**
		 * 打开设备，如果时长参数为Integer.MAX_VALUE 则表示，一直打开，
		 * 否则认为打开指定时长（单位为秒）
		 * 关闭设备一样
		 */
		if (actionMessage.getAction() == EAction.OPEN) {
			if (actionMessage.getParamTime() == Integer.MAX_VALUE) {
				body.append((byte)0x02);	//	命令ID
				body.append((byte)actionMessage.getChannelNo());	//	通道号
			}
			else {
				body.append((byte)0x04);	//	命令ID
				body.append((byte)actionMessage.getChannelNo());	//	通道号
				body.append((byte)(actionMessage.getParamTime() / 60));	//	时长 单位分钟
			}
		}
		else if (actionMessage.getAction() == EAction.CLOSE) {
			if (actionMessage.getParamTime() == Integer.MAX_VALUE) {
				body.append((byte)0x03);	//	命令ID
				body.append((byte)actionMessage.getChannelNo());	//	通道号
			}
			else {
				body.append((byte)0x05);	//	命令ID
				body.append((byte)actionMessage.getChannelNo());	//	通道号
				body.append((byte)(actionMessage.getParamTime() / 60));	//	时长 单位分钟
			}
		}
		
		return body;
	}
	
	/**
	 * 网关心跳
	 * 在此协议中没有规定，但是为了模拟出网关而添加的
	 * @param message
	 * @return
	 */
	public static ByteArray createGatewayHeartbeat(Message message) {
		ByteArray content = new ByteArray();

		content.append((byte)SequenceUtil.generate()); // 消息流水号

		
		//	获取网关ID
		String gid = GatewayUtil.getUUID();
		ByteArray body = new ByteArray(gid.getBytes(), 16);
		
		// 消息ID:0xFFF0
		content.append((byte) 0xFF);
		content.append((byte) 0xF0);

		content.appendShort((short) body.size()); // 消息体属性（长度）

		byte[] deviceNo = new byte[] {0x00, 0x00};

		content.append(deviceNo); // 设备ID

		content.append(body); // 消息体

		return content;
	}
}
